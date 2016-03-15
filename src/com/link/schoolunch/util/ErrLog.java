package com.link.schoolunch.util;

public class ErrLog {
	
	private static String LAST_ERROR_MSG;
	private static String LAST_MESSAGE;
	
	public static void LogError( String error ) {
		LAST_ERROR_MSG = error;
	}
	
	public static void LogMessage( String msg ) {
		LAST_MESSAGE = msg;
	}
	
	public static String getLastError() {
		String temp = LAST_ERROR_MSG;
		LAST_ERROR_MSG = null;
		return temp;
	}
	
	public static String getLastMessage() {
		String temp = LAST_MESSAGE;
		LAST_MESSAGE = null;
		return temp;
	}
}
