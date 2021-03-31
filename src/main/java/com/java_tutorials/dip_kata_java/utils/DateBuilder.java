package com.java_tutorials.dip_kata_java.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateBuilder {

    private final TimeZone timeZone;
    private int year = 0;
    private int monthZeroBased = 0;
    private int dayOfMonth = 0;
    private int hour24h = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int millis = 0;

    private DateBuilder(TimeZone timeZone) {
	this.timeZone = timeZone;
    }

    public DateBuilder day(int year, int monthZeroBased, int dayOfMonth) {
        this.year = year;
	this.monthZeroBased = monthZeroBased;
	this.dayOfMonth = dayOfMonth;
        return this;
    }

    @SuppressWarnings("unused") public DateBuilder time(int hour24h, int minutes, int seconds, int millis) {
	this.hour24h = hour24h;
	this.minutes = minutes;
	this.seconds = seconds;
	this.millis = millis;
	return this;
    }

    public Date asDate() {
	Calendar calendar = new GregorianCalendar();
	calendar.setTimeZone(timeZone);
	//noinspection MagicConstant
	calendar.set(year, monthZeroBased, dayOfMonth, hour24h, minutes, seconds );
	calendar.set(Calendar.MILLISECOND, millis);
	return calendar.getTime();
    }

    public static DateBuilder date(TimeZone timeZone) {
        return new DateBuilder(timeZone);
    }
}
