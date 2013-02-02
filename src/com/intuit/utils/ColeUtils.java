package com.intuit.utils;

import java.util.Calendar;

public class ColeUtils {
	
	public static boolean CoversFYStart(Calendar one, Calendar two, Calendar fy_start) throws Exception {
		if (one == null || two == null)
			throw new Exception("CoversFYStart: Null dates passed in");
		if (fy_start == null)
			throw new Exception("CoversFYStart: Null FY Start");

		int fy_month = fy_start.get(Calendar.MONTH);
		int fy_day = fy_start.get(Calendar.DAY_OF_MONTH);
		Calendar cal = (Calendar)one.clone();
				
		while (true) {
			cal.add(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			if (month==fy_month && day==fy_day)
				break;
		}

		return cal.before(two);
	}
	
	public static boolean SameDay(Calendar one, Calendar two) {
		return one.get(Calendar.YEAR) == two.get(Calendar.YEAR) &&
                one.get(Calendar.DAY_OF_YEAR) == two.get(Calendar.DAY_OF_YEAR);
	}

}
