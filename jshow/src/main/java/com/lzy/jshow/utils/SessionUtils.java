package com.lzy.jshow.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;

public class SessionUtils {
	
	private static Gson gson = new Gson();
	
	public static String getId(HttpServletRequest request) throws Exception{
		return request.getSession().getId();
	}
	
	/*public static void set(HttpServletRequest request, String... data) throws Exception{
		if(data != null && data.length % 2 == 0){
			for(int i = 0; i < data.length / 2; i++){
				int index = i * 2;
				set(request, data[index], data[index + 1]);
			}
		}
	}*/
	
	public static void set(HttpServletRequest request, String name, Object value) throws Exception{
		request.getSession().setAttribute(name, value);
	}
	
	public static void setObj(HttpServletRequest request, String name, Object obj) throws Exception{
		request.getSession().setAttribute(name, gson.toJson(obj));
	}
	
	public static void setJson(HttpServletRequest request, String name, JsonObject json) throws Exception{
		request.getSession().setAttribute(name, gson.toJson(json));
	}
	
	public static String get(HttpServletRequest request, String name) throws Exception{
		Object value = request.getSession().getAttribute(name);
		if(value != null){
			return String.valueOf(value);
		}
		return "";
	}
	
	public static Boolean getBoolean(HttpServletRequest request, String name) throws Exception{
		Object value = request.getSession().getAttribute(name);
		if(value != null){
			if(value instanceof String){
				return value != null && ( String.valueOf(value).equals("1") || String.valueOf(value).equals("true"));
			}
			return Boolean.parseBoolean(String.valueOf(value));
		}
		return false;
		
	}
	
	public static int getInteger(HttpServletRequest request, String name) throws Exception{
		String value = get(request, name);
		try {
			return value == null? 0: Integer.parseInt(value);
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static long getLong(HttpServletRequest request, String name) throws Exception{
		String value = get(request, name);
		try {
			return value == null? 0L: Long.parseLong(value);
		} catch (Exception e) {
		}
		return 0L;
	}
	
	public static double getDouble(HttpServletRequest request, String name) throws Exception{
		String value = get(request, name);
		try {
			return value == null? 0D: Double.parseDouble(value);
		} catch (Exception e) {
		}
		return 0D;
	}
	
	public static JsonObject getJson(HttpServletRequest request, String name) throws Exception{
		String value = get(request, name);
		try {
			return JsonParser.parseString(value).getAsJsonObject();
		} catch (Exception e) {
		}
		return null;
	}
	
	public static <T> T getJsonToObject(HttpServletRequest request, String name, Class<T> clazz) throws Exception{
		String value = get(request, name);
		try {
			return gson.fromJson(value, clazz);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void remove(HttpServletRequest request, String... data) throws Exception{
		if(data != null){
			for(String name: data){
				if(request.getSession().getAttribute(name) != null){
					request.getSession().removeAttribute(name);
				}
			}
		}
	}
	
	public static boolean has(HttpServletRequest request, String... data) throws Exception{
		if(data != null && data.length > 0){
			for(String name: data){
				if(request.getSession().getAttribute(name) == null){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	
	
	

}
