package com.lzy.jshow.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class EncryptUtils {
	
	private static final String salt = "1DExf00gYqc5nSKu3SzRFyuhAEei2tWV";
	private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";

	public static String randomFun(int length) {
		SecureRandom random = new SecureRandom();
		List<Character> passwordChars = new ArrayList<>();

		passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
		passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
		passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
		passwordChars.add(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

		String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
		for (int i = 4; i < length; i++) {
			passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
		}

		Collections.shuffle(passwordChars, random);

		StringBuilder password = new StringBuilder();
		for (char c : passwordChars) {
			password.append(c);
		}

		return password.toString();
	}

	public static void main(String[] args) {
		int passwordLength = 12;
		String password = randomFun(passwordLength);
		System.out.println("生成的复杂密码: " + password);
	}

	public static byte[] encryptPBKDF2(String sSrc) throws Exception{
		KeySpec spec = new PBEKeySpec(sSrc.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
	}
	
	public static String encryptPBKDF2Hash(String sSrc) throws Exception{
		return bytesToHex(encryptPBKDF2(sSrc));
	}
	 
	public static String encryptASE(String sSrc, String sKey) throws Exception{
		if (sKey == null || sKey.length() != 16) {
        	throw new Exception("Key was wrong");
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        String encrypt = Base64.getEncoder().encodeToString(encrypted);
        return encrypt;
    }
	
	public static String encryptASE_HEX(String sSrc, String sKey) throws Exception{
		String encrypt = bytesToHex(encryptASE(sSrc, sKey).getBytes("utf-8"));
		System.out.println(encrypt);
		return encrypt;//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
	
	public static String decryptASE(String sSrc, String sKey) throws Exception{
		if (sKey == null || sKey.length() != 16) {
        	throw new Exception("Key was wrong");
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original,"utf-8");
        return originalString;
    }
	
	public static String decryptASE_HEX(String sSrc, String sKey) throws Exception{
		return decryptASE(new String(hexToBytes(sSrc),"utf-8"), sKey);
	}
	
    public static String encryptDES(String data, String key) throws Exception{
    	String encrypt = bytesToHex(encrypt(data.getBytes(), key.getBytes()));
    	return encrypt;
    }  

    public static String decryptDES(String data, String key) throws Exception {  
    	return new String(decrypt(hexToBytes(data), key.getBytes()));
    }
    
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
        return cipher.doFinal(data);  
    }  

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {  
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance("DES");  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
        return cipher.doFinal(data);  
    }  

    public static String ecryptMD5_32(String str) throws Exception{ 
      MessageDigest md5 = MessageDigest.getInstance("MD5"); 
      byte[] bytes = md5.digest(str.getBytes()); 
      StringBuffer stringBuffer = new StringBuffer(); 
      for (byte b : bytes){ 
        int bt = b&0xff; 
        if (bt < 16){ 
          stringBuffer.append(0); 
        }  
        stringBuffer.append(Integer.toHexString(bt)); 
      } 
      System.out.println(stringBuffer.toString());
      return stringBuffer.toString(); 
    } 
      
    public static String ecryptMD5_16(String str) throws Exception{ 
      String reStr = ecryptMD5_32(str); 
      if (reStr != null){ 
        reStr = reStr.toUpperCase().substring(8, 24); 
      } 
      System.out.println(reStr);
      return reStr; 
    } 
    
	public static String encryptHEX(String data) throws Exception{
		byte[] inData = data.getBytes("UTF-8");
		
		byte[] outData = new byte[data.length()];
		int seed = 0x1b;
		int size = data.length();
		
		boolean isNull= false;
		int xorCode = seed;
		
		for (int i = 0; i < size; i++) {
			if (isNull){
				break;
			}
			if (inData[i] == 0){
				isNull = true;
			}
			outData[i] = (byte)((inData[i] ^ xorCode) & 0x00ff);
			xorCode = (outData[i] ^ seed) & 0x00ff;
			seed += 1;
		}
		String encrypt = bytesToHex(outData);
		//System.out.println(encrypt);
	    return encrypt;
		
	}
	
	public static String decryptHEX(String encryptData) throws Exception{
		byte[] inData = hexToBytes(encryptData);
		byte[] outData = new byte[inData.length];
		int seed = 0x1b;
		int size = inData.length;
		
		int i = 0;
		int xorCode;
		
		xorCode = seed;
		for (i = 0; i < size; i++) {
			outData[i] = (byte)((inData[i] ^ xorCode) & 0x00ff);
			xorCode = (inData[i] ^ seed) & 0x00ff;
			seed += 1;
			if (outData[i] == 0) {
				i++;
				break;
			}
		}
	
		if (i < size) {
			for(; i < size; i++){
				outData[i] = 0;
			}
		}
	
		return new String(outData, "UTF-8");
	}
	
	public static String hexToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	
	public static String stringToHex(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
			}
		return str;
	}
	
	public static String stringToUnicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			unicode.append("\\u" + Integer.toHexString(c));
		}
		return unicode.toString();
	}
	
	public static byte[] hexToBytes(String inHex) throws Exception{
    	int hexlen = inHex.length();
		if (hexlen % 2 == 1){
			hexlen++;
			inHex = "0" + inHex;
		}
		byte[] result = new byte[hexlen / 2];
		for(int i = 0; i < hexlen; i += 2){
			result[i / 2] = (byte) Integer.parseInt(inHex.substring(i, i + 2), 16);
		}
		return result;
	}
	
	public static String bytesToHex(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < bytes.length; i++){
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if(hex.length() < 2){
				sb.append(0);
			}          
			sb.append(hex);      
		}      
		return sb.toString();
	}
    
	
}
