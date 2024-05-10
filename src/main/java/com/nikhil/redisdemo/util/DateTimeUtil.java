package com.nikhil.redisdemo.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author macrena
 * @version 1.0, 28-Feb-2022
 */
public class DateTimeUtil {

    public static final long       MINUTE          = 60l;
    public static final long       HOUR            = 60 * 60l;
    public static final long       DAY             = 24 * 60 * 60l;
    public static final long       YEAR            = 365 * 24 * 60 * 60l;
    public static final String     ZONE_ID         = "Asia/Kolkata";
    public static final ZoneId     ZONE_IST        = ZoneId.of("Asia/Kolkata");
    public static final ZoneOffset ZONE_OFFSET_IST = DateTimeUtil.ZONE_IST.getRules().getOffset(Instant.now());
    public static final String     TODAY           = "Today";
    public static final String     TOMORROW        = "Tomorrow";

    private DateTimeUtil() {

    }

    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000l;
    }

    public static String toDateString(Date date, String format) {
        final SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat(format);
        return DEFAULT_FORMAT.format(date);
    }

    public static String toDateString(long epochSeconds, String format) {
        return toDateString(new Date(epochSeconds * 1000L), format);
    }

    public static Long convertToHours(Long seconds) {
        return seconds / HOUR;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of(ZONE_ID));
    }

    private static Date localDateTimeToDate(LocalDateTime startOfDay) {
        return Date.from(startOfDay.atZone(ZoneId.of(ZONE_ID)).toInstant());
    }

    public static Long getStartOfDayEpocInSec(Long endTimeInSec) {
        Date date = new Date(endTimeInSec * 1000);
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        date = localDateTimeToDate(startOfDay);
        return date.getTime() / 1000;
    }

    public static Long getDateDiff(long date1epochInSec, long date2epochInSec) {
        return Math.abs(date1epochInSec - date2epochInSec) / DateTimeUtil.DAY;
    }

    public static Long getEndOfDayEpoch(long epochMillis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of(ZONE_ID));
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        Date date = localDateTimeToDate(endOfDay);
        return date.getTime();
    }

    public static boolean checkEndOfDay(long epochSeconds) {
        return getEndOfDayEpoch(epochSeconds * 1000L) / 1000L == epochSeconds;
    }

    public static Date getDateWithZone(long epochMillis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of(ZONE_ID));
        return localDateTimeToDate(localDateTime);
    }

    public static int getHoursFromEpoch(long epochMillis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of(ZONE_ID));
        return getHours(localDateTimeToDate(localDateTime));
    }

    public static String getHoursAndMinFromEpoch(long epochMillis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of(ZONE_ID));
        return getHoursAndMinutes(localDateTimeToDate(localDateTime));
    }

    public static int getHours(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(ZONE_ID)));
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(ZONE_ID)));
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    public static String getHoursAndMinutes(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(date);
    }

    public static Date toDate(Long epochSeconds) {
        return new Date(epochSeconds * 1000);
    }

    public static String getDayOfWeek(Long epochSeconds, TextStyle textStyle, Locale locale) {
        DayOfWeek dayOfWeek = Instant.ofEpochSecond(epochSeconds).atOffset(DateTimeUtil.ZONE_OFFSET_IST).getDayOfWeek();
        return dayOfWeek.getDisplayName(textStyle, locale);
    }

    public static String getFormattedDate(Long epoc, String pattern) {
        if (null != epoc) {
            Date date = new Date(epoc * 1000);
            SimpleDateFormat df3 = new SimpleDateFormat(pattern);
            df3.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
            return df3.format(date);
        }
        return null;
    }

    public static String getDayOfMonthWithSuffix(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(ZONE_ID)));
        c.setTime(date);
        int n = c.get(Calendar.DATE);
        if (n >= 11 && n <= 13) {
            return n + "th";
        }
        switch (n % 10) {
            case 1:  return n + "st";
            case 2:  return n + "nd";
            case 3:  return n + "rd";
            default: return n + "th";
        }
    }

    public static long convertToDays(Long seconds) {
        return seconds / DAY;
    }

    public static Long convertToMins(Long seconds) {
        return seconds / MINUTE;
    }

    public static Long getEpochSec(Date date) {
        if (null != date) {
            return date.getTime() / 1000;
        }
        return null;
    }

    public static Long getXFinancialYearsBeforeStartEpoch(int years) {
        LocalDate currentDate = LocalDate.now();
        int financialYearStartMonth = 4; // April
        int financialYearStartDay = 1;

        LocalDate financialYearStartDate = LocalDate.of(currentDate.getYear(), financialYearStartMonth, financialYearStartDay);

        // If the current date is before the financial year start date, subtract a year
        if (currentDate.isBefore(financialYearStartDate)) {
            financialYearStartDate = financialYearStartDate.minusYears(1);
        }

        return financialYearStartDate.minusYears(years).atStartOfDay(ZoneId.of(ZONE_ID)).toEpochSecond();
    }
}
