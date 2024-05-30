package com.shortthirdman.sharedlibs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

    /**
     * Parse the date string value with date format
     *
     * @param dateValue
     * @param dateFormat
     * @return
     */
    public static LocalDate parseDate(String dateValue, String dateFormat) {
        if (dateFormat == null || dateFormat.isEmpty()) {
            return LocalDate.parse(dateValue, DateTimeFormatter.BASIC_ISO_DATE);
        } else {
            return LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(dateFormat, Locale.ENGLISH));
        }
    }

    /**
     * Checks if the year is leap year or not
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Converts LocalDate to Date
     * @param localDate the LocalDate to convert
     * @return
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts LocalDateTime as Date
     * @param localDateTime the LocalDateTime to convert
     * @return
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts Date to LocalDate
     * @param date the Date to convert
     * @return
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Converts Date to LocalDateTime
     * @param date the Date to convert
     * @return
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * @param dateValue
     * @return
     */
    public static ZonedDateTime asZonedDateTime(String dateValue, String pattern) {
        String defaultPattern = (pattern == null) ? "yyyy-MM-dd HH:mm:ss z": pattern;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultPattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateValue, formatter);
        return zonedDateTime;
    }

    /**
     * @param startDate the starting date
     * @param endDate the ending date
     * @return
     */
    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());
    }

    /**
     * Converts a date from source to target format in specified locale
     *
     * @param dateValue the date value to be formatted
     * @param fromFmt the original source format
     * @param toFmt the target destination format
     * @param locale the locale, if not passed defaults to `Locale.ENGLISH`
     * @return
     * @throws ParseException
     */
    public static String formatDate(String dateValue, String fromFmt, String toFmt, Locale locale) throws ParseException {
        if (StringUtils.isBlank(dateValue) || StringUtils.isBlank(fromFmt) || StringUtils.isBlank(toFmt)) {
            return null;
        }

        if (Objects.isNull(locale)) {
            locale = Locale.ENGLISH;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fromFmt, locale);
        Date d = sdf.parse(dateValue);

        sdf = new SimpleDateFormat(toFmt, locale);

        return sdf.format(d);
    }

    /**
     * Converts current date into required format
     *
     * @param format the target format
     * @return
     */
    public static String getCurrentDate(String format) {
        if (StringUtils.isBlank(format)) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        return formatter.format(date);
    }
}
