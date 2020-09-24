package com.pavilion.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	//private static final String format = "yyyy-MM-dd";

	//每一个线程
	private static final ThreadLocal<SimpleDateFormat> threadLocal = new
			ThreadLocal<SimpleDateFormat>();

	public static Date strToDate(String dateStr,String format){
		SimpleDateFormat sdf = null;
		sdf = threadLocal.get();
		if (sdf == null){
			sdf = new SimpleDateFormat(format);
		}
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}


	public static String DateToStr(Date date,String format){
		SimpleDateFormat sdf = null;
		sdf = threadLocal.get();
		if (sdf == null){
			sdf = new SimpleDateFormat(format);
		}
		String dateStr = null;
		dateStr = sdf.format(date);

		return dateStr;
	}

  public static void main(String[] args) {
	  System.out.println(DateFormatUtils.format(strToDate(DateToStr(new Date(),"yyyy-MM-dd")+" 00:00:00","yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd'T'HH:mm:ssZZ"));
  }
}
