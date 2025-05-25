# 1.Spring-Security

First, if you want to start using Spring Security, you need to understand some basic knowledge; otherwise, you won't know how to use the methods provided by Spring in your code.

### Security Filters

So, what filters are provided in Spring Security? Which filters are loaded by default?

|                                           |                                                              |                   |
| ----------------------------------------- | ------------------------------------------------------------ | ----------------- |
| Filter                                    | Filter Function                                              | Loaded by Default |
| ChannelProcessingFilter                   | Filters request protocols such as HTTP and HTTPS             | NO                |
| `WebAsyncManagerIntegrationFilter`        | Integrates WebAsyncManager with the Spring Security context  | YES               |
| `SecurityContextPersistenceFilter`        | Loads security information into the SecurityContextHolder before processing a request | YES               |
| `HeaderWriterFilter`                      | Adds header information to the response                      | YES               |
| CorsFilter                                | Handles cross - origin requests                              | NO                |
| `CsrfFilter`                              | Handles CSRF attacks                                         | YES               |
| `LogoutFilter`                            | Handles user logout                                          | YES               |
| OAuth2AuthorizationRequestRedirectFilter  | Handles OAuth2 authentication redirection                    | NO                |
| Saml2WebSsoAuthenticationRequestFilter    | Handles SAML authentication                                  | NO                |
| X509AuthenticationFilter                  | Handles X509 authentication                                  | NO                |
| AbstractPreAuthenticatedProcessingFilter  | Handles pre - authentication issues                          | NO                |
| CasAuthenticationFilter                   | Handles CAS single - sign - on                               | NO                |
| `OAuth2LoginAuthenticationFilter`         | Handles OAuth2 authentication                                | NO                |
| Saml2WebSsoAuthenticationFilter           | Handles SAML authentication                                  | NO                |
| `UsernamePasswordAuthenticationFilter`    | Handles form - based login                                   | YES               |
| OpenIDAuthenticationFilter                | Handles OpenID authentication                                | NO                |
| `DefaultLoginPageGeneratingFilter`        | Configures the default login page                            | YES               |
| `DefaultLogoutPageGeneratingFilter`       | Configures the default logout page                           | YES               |
| ConcurrentSessionFilter                   | Handles session validity                                     | NO                |
| DigestAuthenticationFilter                | Handles HTTP digest authentication                           | NO                |
| BearerTokenAuthenticationFilter           | Handles the access token for OAuth2 authentication           | NO                |
| `BasicAuthenticationFilter`               | Handles HttpBasic login                                      | YES               |
| `RequestCacheAwareFilter`                 | Handles request caching                                      | YES               |
| `SecurityContextHolderAwareRequestFilter` | Wraps the original request                                   | YES               |
| JaasApiIntegrationFilter                  | Handles JAAS authentication                                  | NO                |
| `RememberMeAuthenticationFilter`          | Handles "Remember Me" login                                  | NO                |
| `AnonymousAuthenticationFilter`           | Configures anonymous authentication                          | YES               |
| `OAuth2AuthorizationCodeGrantFilter`      | Handles the authorization code in OAuth2 authentication      | NO                |
| `SessionManagementFilter`                 | Handles session concurrency issues                           | YES               |
| `ExceptionTranslationFilter`              | Handles exceptions during authentication/authorization       | YES               |
| `FilterSecurityInterceptor`               | Handles authorization - related operations                   | YES               |

# 2.Simplified Request Flow in the Project

The entire request process is as follows:

1. First, the request goes through our custom filter (here, it's the JwtFilter).

2. Then, it reaches our custom permission verification: `@PreAuthorize("@cs.hasAnyAuthority('user:view', 'admin:all')")` // Both users and admins have permission.

3. Next, it enters the Service layer logic, where user account and password authentication is required.

   ```java
       public AppUserDto login(AppUser appUser) {
           UsernamePasswordAuthenticationToken authenticationToken
                   = new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword());
           Authentication authenticate = null;
           try {
               authenticate = authenticationManager.authenticate(authenticationToken);//UserDetails -> loadUserByUsername
           } catch (AuthenticationException e) {
               throw new RuntimeException("Authentication failed, invalid username or password.");
           }
           SecurityContextHolder.getContext().setAuthentication(authenticate);
           LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
           Long id = loginUser.getAppUserDto().getId();
           String jwt = TokenUtils.getToken(id, config.getTokenSign(), appUser.getUsername(), loginUser.getPermissions());
           AppUserDto userDto = loginUser.getAppUserDto();
           userDto.setPassword(null);
           userDto.setToken(jwt);
           return userDto;
       }
   ```

4. When the code reaches this line, it will enter our custom - overridden verification method: `authenticate = authenticationManager.authenticate(authenticationToken)`
   ```java
   @Service
   public class UserDetailsServiceImpl implements UserDetailsService {
       @Autowired
       private AppUserMapper appUserMapper;
   
       @Override
       public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
           AppUserExample userExample = new AppUserExample();
           userExample.createCriteria().andUsernameEqualTo(username);
           List<AppUser> list = appUserMapper.selectByExample(userExample);
           AppUser first = CommonUtils.getFirst(list);
   
           if (first == null){
               throw new RuntimeException("Username or Password Error!");
           }
           AppUserDto userDto = new AppUserDto();
           BeanUtils.copyProperties(first,userDto);
   
           List<String> permissions = appUserMapper.permissionList(userDto.getId());
           return new LoginUser(userDto,permissions);
       }
   }
   ```

5. After a successful return, it goes back to the Service layer. Finally, the generated JWT contains user information and user permissions. For subsequent requests, the information will be put into the SecurityContextHolder in the Jwt Filter:
   ```java
   @Component
   public class JwtFilter extends OncePerRequestFilter {
       @Autowired
       private ConfigProperties configProperties;
   
       @Autowired
       private AppUserMapper appUserMapper;
   
       @Override
       protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
           String token = request.getHeader("token");
           if (StringUtils.isBlank(token)) {
               filterChain.doFilter(request, response);
               return;
           }
           try {
               DecodedJWT decodedJWT = TokenUtils.verify(token, configProperties.getTokenSign());
               String userId = decodedJWT.getAudience().get(0);
               String username = decodedJWT.getClaim("username").asString();
               List<String> permissions = decodedJWT.getClaim("permissions").asList(String.class);
   
               AppUserDto userDto = new AppUserDto();
               userDto.setId(Long.valueOf(userId));
               userDto.setUsername(username);
   
               LoginUser loginUser = new LoginUser(userDto, permissions);
               UsernamePasswordAuthenticationToken authToken =
                       new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authToken);
   
           } catch (Exception e) {
               throw new RuntimeException("invalid token");
           }
           filterChain.doFilter(request, response);
       }
   }
   ```



Here, we also need to create a `LoginUser` class that extends `UserDetails` and put our own entity and `List<String> permissionList` into it. The filter needs to be executed before the `UsernamePasswordAuthenticationFilter`. So, we need the following code:

```java
@Configuration
@EnableMethodSecurity
public class SecruityConfig {

    @Autowired
    private SecruityPathFilter secruityPathFilter;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
               .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usr/login", "/api/usr/register").permitAll()
                        .anyRequest().authenticated()
                )
               .csrf(CsrfConfigurer::disable)
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Here is our token verification
               .addFilterBefore(secruityPathFilter, AuthorizationFilter.class); // This is optional

        return http.build();
    }
}
```



Summary:
Each subsequent request will go through the above cycle. After a successful login, the user will have permissions, which Spring will manage automatically. Of course, you can also customize it:

```java
@Component("cs")
public class CustomAuthority {
    public boolean hasAnyAuthority(String ... authority){
        System.out.println("custom check authority...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        if (permissions != null){
            for (String permission : permissions) {
                if (permission.contains("admin")){
                    return true;
                }
            }
        }
        return permissions.contains(authority);
    }
}
```

Then, in our Controller:
```java
@RestController
@RequestMapping("/api/")
public class AppManageController {
    // Custom permission judgment
    @RequestMapping("/hello")
    @PreAuthorize("@cs.hasAnyAuthority('user:view', 'admin:all')") // Both users and admins have permission
    public String hello() {
        System.out.println("hello");
        return "hello";
    }
    // Default permission judgment
    @RequestMapping("/bye")
    @PreAuthorize("hasAnyAuthority('admin:all')") // Only admins have permission
    public String bye() {
        System.out.println("bye");
        return "bye";
    }
}
```

