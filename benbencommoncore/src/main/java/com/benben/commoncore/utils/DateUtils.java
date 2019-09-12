package com.benben.commoncore.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * 类名：DateUtils 说明：日期相关的工具类
 * <p>
 * static int DayOf(java.util.Date date) 功能：根据日期返回号数
 * </p>
 * <p>
 * static java.lang.String dayofweek(java.util.Date date) 功能：获取指定日期是星期几
 * </p>
 * <p>
 * static long daysBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少天
 * </p>
 * <p>
 * static int DaysInMonth(int year, int month) 功能：获取指定月有多少天
 * </p>
 * <p>
 * static int DaysInYear(int year) 功能：指定年一共有多少天
 * </p>
 * <p>
 * static java.util.Date EndOfTheDay() 功能：获取今天的结束时间
 * </p>
 * <p>
 * static java.util.Date EndOfTheMonth() 功能：获取本月的最后一天
 * </p>
 * <p>
 * static java.util.Date EndOfTheWeek() 功能：返回这个星期的最后一天
 * </p>
 * <p>
 * static java.util.Date EndOfTheYear() 功能：返回这一年的最后一天
 * </p>
 * <p>
 * static int HourOf(java.util.Date date) 功能：根据日期返回小时（24小时制）
 * </p>
 * <p>
 * static long hoursBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少小时
 * </p>
 * <p>
 * static java.util.Date incDay(java.util.Date date, int day) 功能：获取day天后（前）的时间
 * </p>
 * <p>
 * static java.util.Date incHour(java.util.Date date, int hour)
 * 功能：获取hour小时后（前）的时间
 * </p>
 * <p>
 * static java.util.Date incMinute(java.util.Date date, int minute)
 * 功能：获取minute分钟后（前）的时间
 * </p>
 * <p>
 * static java.util.Date incSecond(java.util.Date date, int second)
 * 功能：获取second秒后（前）的时间
 * </p>
 * <p>
 * static java.util.Date incYear(java.util.Date date, int year)
 * 功能：获取year年后（前）的时间
 * </p>
 * <p>
 * static int MinuteOf(java.util.Date date) 功能：根据日期返回分钟
 * </p>
 * <p>
 * static long minutesBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少分钟
 * </p>
 * <p>
 * static int MonthOf(java.util.Date date) 功能：根据日期获取月份
 * </p>
 * <p>
 * static long mouthsBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少月
 * </p>
 * <p>
 * static int SecondOf(java.util.Date date) 功能：根据日期返回秒数
 * </p>
 * <p>
 * static long secondsBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少秒
 * </p>
 * <p>
 * static java.util.Date StartOfTheDay() 功能：获取今天的开始时间
 * </p>
 * <p>
 * static java.util.Date StartOfTheMonth() 功能：获取本月的第一天
 * </p>
 * <p>
 * static java.util.Date StartOfTheWeek() 功能：这个星期的第一天
 * </p>
 * <p>
 * static java.util.Date StartOfTheYear() 功能：返回这一年的第一天
 * </p>
 * <p>
 * static java.util.Date Tomorrow() 功能：获取明天的日期
 * </p>
 * <p>
 * static int WeeksInYear(java.util.Date date) 功能：获取指定日期是一天中的第几天
 * </p>
 * <p>
 * static int YearOf(java.util.Date date) 功能：根据日期获取年份
 * </p>
 * <p>
 * static long YearsBetween(java.util.Date beginDate, java.util.Date endDate)
 * 功能：获取两个日期相差多少年
 * </p>
 * <p>
 * static java.util.Date Yesterday() 功能：获取昨天的日期
 * </p>
 * <p>
 * static boolean SameDateTime(Date date1, Date date2) 功能：判断两个日期是否相等
 * </p>
 * <p>
 * static String parseYMD(Date date) 功能：获取yyyy-MM-dd格式的字符串
 * </p>
 * <p>
 * static String parseYMDHms(Date date) 功能：获取yyyy-MM-dd HH:ss:mm格式的字符串
 * </p>
 * <p>
 * static String parseYM(Date date) 功能：获取yyyy-MM格式的字符串
 * </p>
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	private static final int YEAR_RETURN = 0;

	private static final int MONTH_RETURN = 1;

	private static final int DAY_RETURN = 2;

	private static final int HOUR_RETURN = 3;

	private static final int MINUTE_RETURN = 4;

	private static final int SECOND_RETURN = 5;

	/**
	 * 获取当前年份
	 * @return
	 */
	public static int getCurrentYear(){
		int mYear = 0;
		Calendar c = Calendar.getInstance();//
		mYear = c.get(Calendar.YEAR); // 获取当前年份
		return mYear;
	}

	/**
	 * 功能：获取year年后（前）的时间
	 *
	 * @param date
	 *            需要操作的时间
	 * @param year
	 *            几年，比如1是1年后，-1是1年前
	 */
	public static Date incYear(Date date, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * 功能：获取day天后（前）的时间
	 *
	 * @param date
	 *            需要操作的时间
	 * @param day
	 *            几天，比如1是1天后，-1是1天前
	 */
	public static Date incDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 功能：获取hour小时后（前）的时间
	 *
	 * @param date
	 *            需要操作的时间
	 * @param hour
	 *            几小时，比如1是1小时后，-1是1小时前
	 */
	public static Date incHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}

	/**
	 * 功能：获取minute分钟后（前）的时间
	 *
	 * @param date
	 *            需要操作的时间
	 * @param minute
	 *            几分钟，比如1是1分钟后，-1是1分钟前
	 */
	public static Date incMinute(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * 功能：获取second秒后（前）的时间
	 *
	 * @param date
	 *            需要操作的时间
	 * @param second
	 *            几秒，比如1是1秒后，-1是1秒前
	 */
	public static Date incSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 功能：获取指定日期是星期几
	 *
	 * @param date
	 * @return
	 */
	public static String dayofweek(Date date) {
		if (date == null)
			return "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		String week = null;
		switch (dayOfWeek) {
			case 1:
				week = "星期日";
				break;
			case 2:
				week = "星期一";
				break;
			case 3:
				week = "星期二";
				break;
			case 4:
				week = "星期三";
				break;
			case 5:
				week = "星期四";
				break;
			case 6:
				week = "星期五";
				break;
			case 7:
				week = "星期六";
				break;
		}
		return week;
	}

	/**
	 * 功能：获取指定日期是一天中的第几天
	 *
	 * @param date
	 * @return
	 */
	public static int WeeksInYear(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		return cd.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 功能：指定年一共有多少天
	 *
	 * @return
	 */
	public static int DaysInYear(int year) {
		int days = 365;
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			days = 366;
		}
		return days;
	}

	/**
	 * 功能：获取指定月有多少天
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int DaysInMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);// Java月份才0开始算
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 功能：获取昨天的日期
	 *
	 * @return
	 */
	public static Date Yesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 功能：获取明天的日期
	 *
	 * @return
	 */
	public static Date Tomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * 功能：根据日期获取年份
	 *
	 * @param date
	 * @return
	 */
	public static int YearOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 功能：根据日期获取月份
	 *
	 * @param date
	 * @return
	 */
	public static int MonthOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能：根据日期返回号数
	 *
	 * @param date
	 * @return
	 */
	public static int DayOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能：根据日期返回小时（24小时制）
	 *
	 * @param date
	 * @return
	 */
	public static int HourOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能：根据日期返回分钟
	 *
	 * @param date
	 * @return
	 */
	public static int MinuteOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 功能：根据日期返回秒数
	 *
	 * @param date
	 * @return
	 */
	public static int SecondOf(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.SECOND);
	}

	/**
	 * 功能：返回这一年的第一天
	 *
	 * @return
	 */
	public static Date StartOfTheYear() {
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		return currentDate.getTime();
	}

	/**
	 * 功能：返回这一年的最后一天
	 *
	 * @return
	 */
	public static Date EndOfTheYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		String date = year + "-12-31";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 功能：获取本月的第一天
	 *
	 * @return
	 */
	public static Date StartOfTheMonth() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		return lastDate.getTime();
	}

	/**
	 *
	 * 功能：获取本月的最后一天
	 *
	 * @return
	 */
	public static Date endOfTheMonth() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		return lastDate.getTime();
	}

	/**
	 * 功能：这个星期的第一天
	 *
	 * @return
	 */
	public static Date StartOfTheWeek() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		return monday;
	}

	/**
	 * 功能：返回这个星期的最后一天
	 *
	 * @return
	 */
	public static Date EndOfTheWeek() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		return monday;
	}

	/**
	 * 功能：获取今天的开始时间
	 *
	 * @return
	 */
	public static Date StartOfTheDay() {
		Calendar calendar = Calendar.getInstance();
		String str = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 功能：获取今天的结束时间
	 *
	 * @return
	 */
	public static Date EndOfTheDay() {
		Calendar calendar = Calendar.getInstance();
		String str = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 功能：获取两个日期相差多少年
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long YearsBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, YEAR_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取两个日期相差多少月
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long mouthsBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, MONTH_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取两个日期相差多少天
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long daysBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, DAY_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取两个日期相差多少小时
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long hoursBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, HOUR_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取两个日期相差多少分钟
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long minutesBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, MINUTE_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取两个日期相差多少秒
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long secondsBetween(Date beginDate, Date endDate) {
		try {
			return getBetween(beginDate, endDate, SECOND_RETURN);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 功能：获取相差的年数
	 *
	 * @return
	 */
	private static int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	/**
	 * 功能：获取两个日期相差
	 *
	 * @param beginDate
	 * @param endDate
	 * @param returnPattern
	 * @return
	 */
	private static long getBetween(Date beginDate, Date endDate,
								   int returnPattern) {

		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);
		endCalendar.setTime(endDate);
		switch (returnPattern) {
			case YEAR_RETURN:
				return DateUtils.getByField(beginCalendar, endCalendar,
						Calendar.YEAR);
			case MONTH_RETURN:
				return DateUtils.getByField(beginCalendar, endCalendar,
						Calendar.YEAR)
						* 12
						+ DateUtils.getByField(beginCalendar, endCalendar,
						Calendar.MONTH);
			case DAY_RETURN:
				return DateUtils.getTime(beginDate, endDate)
						/ (24 * 60 * 60 * 1000);
			case HOUR_RETURN:
				return DateUtils.getTime(beginDate, endDate) / (60 * 60 * 1000);
			case MINUTE_RETURN:
				return DateUtils.getTime(beginDate, endDate) / (60 * 1000);
			case SECOND_RETURN:
				return DateUtils.getTime(beginDate, endDate) / 1000;
			default:
				return 0;
		}
	}

	/**
	 * 功能：获取两个列相减的值
	 *
	 * @param beginCalendar
	 * @param endCalendar
	 * @param calendarField
	 * @return
	 */
	private static long getByField(Calendar beginCalendar,
								   Calendar endCalendar, int calendarField) {
		return endCalendar.get(calendarField)
				- beginCalendar.get(calendarField);
	}

	/**
	 * 功能：获取两个日期相差的毫秒数
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private static long getTime(Date beginDate, Date endDate) {
		return endDate.getTime() - beginDate.getTime();
	}

	/**
	 * 功能：获得当前日期与本周日相差的天数
	 *
	 * @return
	 */
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 功能：判断两个日期是否相等
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean SameDateTime(Date date1, Date date2) {
		return date1.getTime() == date2.getTime();
	}

	/**
	 * 功能：获取yyyy-MM-dd格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String parseYMD(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(date);
	}

	/**
	 * 功能：获取yyyy-MM-dd HH:mm:ss格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String parseYmdHms(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 功能：获取HH:mm:ss格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String parseHms(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 功能：获取yyyy-MM-dd HH:mm格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String parseYmdHm(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		return sf.format(date);
	}

	/**
	 * 功能：获取对应的毫秒值
	 *
	 * @param date
	 * @return
	 */
	public static String parseSSS(Date date) {
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat sf = new SimpleDateFormat("SSS");
		return sf.format(date);
	}

	/**
	 * 功能：自己输入格式
	 *
	 * @param date
	 * @return
	 */
	public static String parseCustomFormat(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	/**
	 * 功能：unix时间戳转换格式
	 *
	 * @param strLong
	 * @return format
	 */
	public static String parseCustomFormat(String strLong, String format) {
		if(StringUtils.isEmpty(strLong)){
			return "";
		}
		SimpleDateFormat sf = new SimpleDateFormat(format);
		// 毫秒转日期
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.parseLong(strLong));
		Date date = c.getTime();
		return sf.format(date);
	}

	/*
	 * 将时间戳转换为时间  （时间戳的格式为秒） 为毫秒则调用parseCustomFormat()
	 * wanghk add
	 */
	public static String stampToDate(String s){
		if(StringUtils.isEmpty(s)){
			return "";
		}
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = Long.valueOf(s);
		Date date = new Date(lt*1000);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 功能：获取yyyy-MM格式的字符串
	 *
	 * @param date
	 * @return
	 */
	public static String parseYm(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		return sf.format(date);
	}

	/**
	 * 功能：获取对应的秒
	 *
	 * @param date
	 * @return
	 */
	public static String parseSS(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("ss");
		return sf.format(date);
	}

	/**
	 * 功能：把yyyy-MM-dd hh:mm:ss格式的字符串转化成日期
	 *
	 * @param strYmdHms
	 * @return
	 */
	public static Date YmdHmsToDate(String strYmdHms) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sf.parse(strYmdHms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 功能：把yyyy-MM-dd HH:mm:ss.SSS格式的字符串转化成日期
	 *
	 * @param strYmdHms
	 * @return
	 */
	public static Date YmdHmsSSSToDate(String strYmdHms) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			date = sf.parse(strYmdHms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * 功能：把yyyy-MM-dd hh:mm:ss格式的字符串转化成日期
	 *
	 * @param strYmdHms
	 * @return
	 */
	public static Date YmdHmToDate(String strYmdHms) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			date = sf.parse(strYmdHms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 功能：把yyyy-MM-dd格式的字符串转化成日期
	 *
	 * @param strYmd
	 * @return
	 */
	public static Date YmdToDate(String strYmd) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sf.parse(strYmd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Date YmToDate(String strYm) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		try {
			date = sf.parse(strYm);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 功能：把"HH:mm"格式的字符串转化成日期
	 *
	 * @param strYmd
	 * @return
	 */
	public static Date HmToDate(String strYmd) {
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		try {
			date = sf.parse(strYmd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * 功能：把"HH:mm"格式的字符串转化成日期
	 *
	 * @param date
	 * @return
	 */
	public static String dateToHm(Date date) {
		String strHm = "";
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		try {
			strHm = sf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strHm;
	}

	/**
	 * 功能:根据秒数获得一个字符串时间,例如00:12:01
	 *
	 * @param nSec
	 * @return
	 */
	public static String FormatSec(int nSec) {
		int _hour = nSec / 60 / 60 >> 0;
		int _minute = (nSec / 60) % 60;
		int _second = nSec % 60;
		String _timeTxt = "";
		_timeTxt += ((_hour > 100) ? _hour : String.valueOf(100 + _hour)
				.substring(1)) + ":";
		_timeTxt += ((_minute < 10) ? ("0" + _minute) : _minute) + ":";
		_timeTxt += (_second < 10) ? ("0" + _second) : _second;
		return _timeTxt;
	}

	/**
	 * 功能:根据秒数获得一个字符串时间,例如00:12
	 *
	 * @param nSec
	 * @return
	 */
	public static String FormatSecToMinuTeHour(int nSec) {
		int _hour = nSec / 60 / 60;
		int _minute = (nSec / 60) % 60;
		int _second = nSec % 60;
		String _timeTxt = "";
		if (_hour > 0) {
			_timeTxt += _hour + "小时";
		}
		if (_minute > 0) {
			_timeTxt += _minute + "分钟";
		}
		if (_second < 60 && _hour <= 0 && _minute <= 0) {
			_timeTxt += "不足一分钟";
		}
		return _timeTxt;
	}

	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static String getFirstOfWeek(String dataStr) throws ParseException {
		Calendar cal = Calendar.getInstance();

		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));

		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		// 所在周开始日期
		String data1 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		return data1;

	}

	public static String getLastOfWeek(String dataStr) throws ParseException {
		Calendar cal = Calendar.getInstance();

		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));

		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		// 所在周结束日期
		String data2 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return data2;

	}

}
