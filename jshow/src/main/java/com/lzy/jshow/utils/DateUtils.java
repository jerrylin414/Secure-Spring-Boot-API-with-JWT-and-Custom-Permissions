package com.lzy.jshow.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

public static String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	
	public static String FORMAT_TIMESTAMP1 = "yyyy/MM/dd HH:mm:ss";
	
	public static String FORMAT_TIMESTAMP2 = "yyyyMMddHHmmss";
	
	public static String FORMAT_TIMESTAMP4 = "yyyy-MM-dd HH:mm";
	
	public static String FORMAT_TIMESTAMP5 = "yyyy/MM/dd HH:mm";
	
	public static String FORMAT_TIMESTAMP6 = "yyyy/MM/dd HH:mm";
	
	public static String FORMAT_TIMESTAMP7 = "hh:mm a d MMM yyyy";
	
	public static String FORMAT_DAY = "yyyy-MM-dd";
	
	public static String FORMAT_DAY1 = "yyyy/MM/dd";
	
	public static String FORMAT_DAY2 = "yyyyMMdd";
	
	public static String FORMAT_DATE = "MM-dd";
	
	public static String FORMAT_DATE1 = "MM/dd";
	
	public static String FORMAT_DATE2 = "MMdd";
	
	public static String FORMAT_DATE3 = "d MMM";

	public static String FORMAT_DATE4 = "MM/dd HH:mm";

	public static String FORMAT_DATE5 = "d MMM, yyyy";
	
	public static String FORMAT_DATE6 = "MMM d, yyyy";
	
	public static String FORMAT_DATE7 = "MM-dd HH:mm";
	
	public static String FORMAT_DATE8 = "MM.dd HH:mm";
	
	public static String FORMAT_TIME = "HH:mm:ss";
	
	public static String FORMAT_TIME1 = "HHmmss";
	
	public static String FORMAT_TIME2 = "HH:mm";
	
	//public static String FORMAT_GMT = "EEE, d MMM yyyy HH:mm:ss z";
	public static String FORMAT_GMT = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
	
	//public static String TIME_FORMAT_GMT = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
	
	public static String TIMING_LOWER = "lower";
	
	public static String TIMING_UPPER = "upper";
	
	public static String TIMING_CH = "ch";
	
	public static Date getDate(String str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY, Locale.TAIWAN);
			return sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate(String format, String str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);  
			return sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate(String format, String str, Locale locale) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);  
		return sdf.parse(str);
	}
	
	public static String getTimeString(String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);
			return sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTimeString(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMESTAMP, Locale.TAIWAN);
			return sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTimeString(Long time){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMESTAMP, Locale.TAIWAN);
			return sdf.format(time == null || time <= 0? new Date(): time);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTimeString(Long time, Locale locale) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMESTAMP, locale);
		return sdf.format(time == null || time <= 0? new Date(): time);
	}
	
	public static String getTimeString(Long time, String format) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format == null || format.trim().equals("")? FORMAT_TIMESTAMP: format);
		return sdf.format(time == null || time <= 0? new Date(): time);
	}
	
	public static String getTimeString(Long time, String format, Locale locale) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format == null || format.trim().equals("")? FORMAT_TIMESTAMP: format, locale);
		return sdf.format(time == null || time <= 0? new Date(): time);
	}
	
	public static String getTimeStringGMT(Long time, String format) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format == null || format.trim().equals("")? FORMAT_TIMESTAMP: format, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(time == null || time <= 0? new Date(): time);
	}
	
	public static String getTimeStringGMT(Long time, String format, Locale locale) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat(format == null || format.trim().equals("")? FORMAT_TIMESTAMP: format, locale);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(time == null || time <= 0? new Date(): time);
	}

	
	/*public static long[] getLastDays(int dayCount) throws Exception{
		long[] time = new long[2];
		Calendar cal = Calendar.getInstance();
		time[1] = cal.getTimeInMillis();
		cal.add(Calendar.DATE, -dayCount);
		time[0] = cal.getTimeInMillis();
		return time;
	}
	
	public static long[] getLastMonth(int monthCount) throws Exception{
		long[] time = new long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		time[1] = cal.getTimeInMillis();
		cal.add(Calendar.MONTH, -monthCount);
		time[0] = cal.getTimeInMillis();
		return time;
	}

	public static long[] getLastWeek(int weekCount) throws Exception{
		long[] time = new long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, -((weekCount - 1) * 7));
		time[1] = cal.getTimeInMillis();
		cal.add(Calendar.DAY_OF_MONTH, -(weekCount * 7));
		time[0] = cal.getTimeInMillis();
		return time;
	}
	
	public static long[] getLastDays(int dayCount, int hour) throws Exception{
		long[] time = new long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if(cal.get(Calendar.HOUR_OF_DAY) < hour){
			cal.add(Calendar.DATE, -1);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, hour);
		time[1] = cal.getTimeInMillis();
		
		cal.add(Calendar.DAY_OF_MONTH, -dayCount);
		time[0] = cal.getTimeInMillis();
		return time;
	}
	
	public static Date[] getThisMonth() throws Exception{
		Date[] dates = new Date[2];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dates[0] = new Date(cal.getTimeInMillis());
		
		cal.add(Calendar.MONTH, 1);
		dates[1] =  new Date(cal.getTimeInMillis());
		
		return dates;
	}
	
	public static Date[] getThisDay(int addDay){
		try {
			Date[] dates = new Date[2];
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			if(addDay != 0){
				cal.add(Calendar.DAY_OF_MONTH, addDay);
			}
			dates[0] = new Date(cal.getTimeInMillis());
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
			dates[1] =  new Date(cal.getTimeInMillis());
			
			return dates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getTodayHour(int hour){
		return getTodayHour(hour, null);
	}
	
	public static Date getTodayHour(int hour, Long time){
		try {
			Calendar cal = Calendar.getInstance();
			if(time != null){
				cal.setTimeInMillis(time);
			}
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date[] getThisHour(int addHours) {
		try {
			Date[] dates = new Date[2];
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			if(addHours != 0){
				cal.add(Calendar.HOUR_OF_DAY, addHours);
			}
			dates[0] = new Date(cal.getTimeInMillis());
			
			
			cal.add(Calendar.HOUR_OF_DAY, 1);
			dates[1] =  new Date(cal.getTimeInMillis());
			
			return dates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date[] getThisMinute() throws Exception{
		Date[] dates = new Date[2];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dates[0] = new Date(cal.getTimeInMillis());
		
		cal.add(Calendar.MINUTE, 1);
		dates[1] =  new Date(cal.getTimeInMillis());
		
		return dates;
	}

	public static long getNextTime(int hour) throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if(cal.getTimeInMillis() < System.currentTimeMillis()){
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return cal.getTimeInMillis();
	}
	
	public static String getTiming(long ms){
		return getTiming(ms, TIMING_UPPER);
	}
	
	public static Long getLongFromString(String format, String timeString){
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			if(StringUtils.isBlank(timeString)) {
				return null;
			}
			Date d = f.parse(timeString);
			return d.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	*/
	
	public static String getTiming(long ms, String mode){
		int d = 0;
		if(ms > ((long) 24 * 60 * 60 * 1000)){
			d = (int) Math.floor(ms / (24 * 60 * 60 * 1000));
			ms = ms % (24 * 60 * 60 * 1000);
		}
		int h = 0;
		if(ms > ((long) 60 * 60 * 1000)){
			h = (int) Math.floor(ms / (60 * 60 * 1000));
			ms = ms % (60 * 60 * 1000);
		}
		int m = 0;
		if(ms > ((long) 60 * 1000)){
			m = (int) Math.floor(ms / (60 * 1000));
			ms = ms % (60 * 1000);
		}
		if(m == 60){
			m = 0;
			h += 1;
		}
		int s = 0;
		if(ms > (long) 1000){
			s = (int) Math.floor(ms / 1000);
			ms = ms % 1000;
		}
		
		if(mode != null && mode.equals(TIMING_LOWER)){
			return "" + (d == 0?"": d + "d") + (h == 0?"": h + "h") + (m == 0?"": m + "m") + (s == 0?(ms == 0? "": ms + "ms"): s + "s");
		}else if(mode != null && mode.equals(TIMING_UPPER)){
			return "" + (d == 0?"": d + "D") + (h == 0?"": h + "H") + (m == 0?"": m + "M") + (s == 0?(ms == 0? "": ms + "MS"): s + "S");
		}else if(mode != null && mode.equals(TIMING_CH)){
			return "" + (d == 0?"": d + "天") + (h == 0?"": h + "小时") + (m == 0?"": m + "分") + (s == 0?(ms == 0? "": ms + "毫秒"): s + "秒");
		}else{
			return "" + (d == 0?"": d + "d") + (h == 0?"": h + "h") + (m == 0?"": m + "m") + (s == 0?(ms == 0? "": ms + "ms"): s + "s") + (s == 0?"": ms);
		}
	}
	
	public static boolean isUnderYearsOld(Date dateOfBirth, int isUnderYearsOld) {
		Calendar birth = Calendar.getInstance();
		birth.setTime(dateOfBirth);
		
		Calendar now = Calendar.getInstance();
		int yearsBetween = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		
		if(birth.get(Calendar.MONTH) > now.get(Calendar.MONTH) || (birth.get(Calendar.MONTH) == now.get(Calendar.MONTH) && birth.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
			yearsBetween--;
		}
		return yearsBetween < isUnderYearsOld;
	}
    
    
}
