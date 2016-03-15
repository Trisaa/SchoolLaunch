package com.link.schoolunch.model;

import java.util.Calendar;

public class Time {

	public int hour;
	public int minute;
	
	public Time( String str ) {
		String[] list = str.split("-");
		hour = Integer.valueOf( list[0] );
		minute = Integer.valueOf( list[1] );
	}
	
	public Time() {
		Calendar c = Calendar.getInstance();
		hour = c.get( Calendar.HOUR_OF_DAY );
		minute = c.get( Calendar.MINUTE );
	}

	public String toString() {
		return hour + ":" + minute;
	}
	
	public boolean onTime() {
		if( hour >= 9 && hour <= 11 ) {
			return true;
		}else if( hour == 15 || ( hour == 16 && minute < 20 ) ) {
			return true;
		} else 
			return false;
	}
	
	public String getSendTime() {
		if( hour >= 9 && hour <= 11 ) {
			return "11:35";
		} else if( hour == 15 || ( hour == 16 && minute < 20 ) ) {
			return "16:55";
		} else 
			return "不在配送时间内";
	}
}
