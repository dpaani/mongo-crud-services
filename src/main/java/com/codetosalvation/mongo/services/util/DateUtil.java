package com.codetosalvation.mongo.services.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public final class DateUtil {

	private DateUtil() {}

	public static String currentDateTimeAsString() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Calendar.getInstance().getTime());
	}

}
