package com.shortthirdman.primekit.essentials.common.util;

import com.shortthirdman.primekit.essentials.common.YearWeek;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @apiNote Utility class for operations on {@link LocalDate}, {@link Date}, {@link LocalDateTime}
 * @author shortthirdman
 * @since 1.0
 */
public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Checks if the year is leap year or not
     * @param year the calendar year
     * @return true if calendar year is leap year, otherwise false
     */
    public static boolean isLeapYear(int year) {
        return (year % 100 != 0) || (year % 400 == 0);
    }

    public static Date convertToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static ZonedDateTime convertToZonedDateTime(Date date, ZoneId zone) {
        return date.toInstant().atZone(zone);
    }

    public static Timestamp convertToTimeStampFromInstant(ZonedDateTime zonedDateTime) {
        Instant instant = zonedDateTime.toInstant();
        return Timestamp.from(instant);
    }

    public static Timestamp convertToTimeStamp(ZonedDateTime zonedDateTime) {
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }

    public static ZonedDateTime convertToInstantZonedDateTime(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        return instant.atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime convertToCalendarZonedDateTime(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.toInstant().atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime convertToZonedDateTimeFromLocalDateTime(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.atZone(ZoneId.systemDefault());
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

    public static boolean isWeekend(final LocalDate localDate) {
        DayOfWeek day = DayOfWeek.of(localDate.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    public static boolean isWeekend(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
    }

    /**
     * @param localDate the {@link LocalDate} to add days
     * @param days      the number of business days
     * @param holidays  the list of holidays
     * @return the final business day
     */
    public static LocalDate addBusinessDays(LocalDate localDate, int days, List<LocalDate> holidays) {
        if (localDate == null || days <= 0 || holidays.isEmpty()) {
            throw new IllegalArgumentException("Invalid method argument(s) " + "to addBusinessDays(" + localDate + "," + days + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> !holidays.isEmpty() && holidays.contains(date);

        Predicate<LocalDate> isWeekend = date
                -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        LocalDate result = localDate;
        while (days > 0) {
            result = result.plusDays(1);
            if (isHoliday.or(isWeekend).negate().test(result)) {
                days--;
            }
        }

        return result;
    }

    /**
     * @param localDate the {@link LocalDate} to subtract days
     * @param days      the number of business days
     * @param holidays  the list of holidays
     * @return the final business day
     */
    public static LocalDate subtractBusinessDays(LocalDate localDate, int days, List<LocalDate> holidays) {
        if (localDate == null || days <= 0 || holidays == null) {
            throw new IllegalArgumentException("Invalid method argument(s) "
                    + "to subtractBusinessDays(" + localDate + "," + days + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> !holidays.isEmpty() && holidays.contains(date);

        Predicate<LocalDate> isWeekend =
                date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                        || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        LocalDate result = localDate;
        while (days >= 0) {
            result = result.minusDays(1);
            if (isHoliday.or(isWeekend).negate().test(result)) {
                days--;
            }
        }

        return result;
    }

    /**
     * @param start the start date
     * @param end   the end date
     * @return the list of dates in between inclusive of both
     */
    public static List<LocalDate> datesBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to datesBetween(" + start + "," + end + ")");
        }

        // https://howtodoinjava.com/java/date-time/dates-between-two-dates/

        final List<LocalDate> list = start.datesUntil(end)
                .collect(Collectors.toList());

        return list;
    }

    public static List<LocalDate> allBusinessDays(final LocalDate startDate, final LocalDate endDate, final List<LocalDate> holidays) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween ("
                    + startDate + "," + endDate + "," + holidays + ")");
        }
        List<LocalDate> businessDays = List.of();

        // Predicate 1: Is a given date is a holiday
        Predicate<LocalDate> isHoliday = date -> !holidays.isEmpty() && holidays.contains(date);

        // Predicate 2: Is a given date is a weekday
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Iterate over stream of all dates and check each day against any weekday or holiday
        businessDays = startDate.datesUntil(endDate)
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());

        return businessDays;
    }

    /**
     * @param startDate the start date
     * @param endDate the end date
     * @param holidays the list of holidays
     * @return the list of dates
     */
    @Deprecated(forRemoval = true, since = "1.0.0")
    public static List<LocalDate> businessDaysBetween(final LocalDate startDate, final LocalDate endDate, final List<LocalDate> holidays) {
        // Validate method arguments
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Invalid method argument(s) to countBusinessDaysBetween(" + startDate
                            + "," + endDate + "," + holidays + ")");
        }

        Predicate<LocalDate> isHoliday = date -> !holidays.isEmpty() && holidays.contains(date);

        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(daysBetween)
                .filter(isHoliday.or(isWeekend).negate())
                .collect(Collectors.toList());
    }

    /**
     * @param dateValue the date value in {@link String}
     * @param formatPattern the date format pattern
     * @return the parsed {@link LocalDate}
     */
    public static LocalDate strictParseDate(String dateValue, String formatPattern) {
        LocalDate parsedDate = null;

        if (dateValue == null || StringUtils.isBlank(dateValue)) {
            throw new IllegalArgumentException("Invalid method argument(s) to parseDateStrictly(" + dateValue + ", " + formatPattern + ")");
        }

        if (formatPattern == null || StringUtils.isBlank(formatPattern)) {
            formatPattern = "uuuu-MM-dd";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatPattern);

        parsedDate = LocalDate.parse(dateValue, dtf.withResolverStyle(ResolverStyle.STRICT));

        return parsedDate;
    }

    /**
     * @param dateValue the date value in {@link String}
     * @param formatPattern the date-time format pattern
     * @return the parsed {@link LocalDateTime}
     */
    public static LocalDateTime strictParseDateTime(String dateValue, String formatPattern) {
        LocalDateTime parsedDateTime = null;
        if (dateValue == null || StringUtils.isBlank(dateValue)) {
            throw new IllegalArgumentException("Invalid method argument(s) to parseDateTimeStrictly(" + dateValue + ", " + formatPattern + ")");
        }

        if (formatPattern == null || StringUtils.isBlank(formatPattern)) {
            formatPattern = "uuuu-MM-dd'T'HH:mm:ss.SSSS";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatPattern);

        parsedDateTime = LocalDateTime.parse(dateValue, dtf.withResolverStyle(ResolverStyle.STRICT));

        return parsedDateTime;
    }

    public static String monthNumberToShortName(int monthNumber) {
        return Month.of(monthNumber).getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public static String monthNumberToFullName(int monthNumber) {
        return Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public static String monthNumberToName(int monthNumber) {
        return Month.of(monthNumber).name();
    }

    public static int monthNameToNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    public static int monthShortNameToNumber(String abbreviation) {
        Optional<Month> monthOptional = Arrays.stream(Month.values())
                .filter(month -> month.name().substring(0, 3).equalsIgnoreCase(abbreviation))
                .findFirst();

        return monthOptional.orElseThrow(IllegalArgumentException::new).getValue();
    }

    public static String monthShortNameToFullName(String abbreviation) {
        Optional<Month> monthOptional = Arrays.stream(Month.values())
                .filter(month -> month.name().substring(0, 3).equalsIgnoreCase(abbreviation))
                .findFirst();

        return monthOptional.orElseThrow(IllegalArgumentException::new)
                .getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    /**
     * Get the number of quarters in between two dates
     * @param start the start date
     * @param end the end date
     * @return the number of quarters
     */
    public static Long quarterCount(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        return IsoFields.QUARTER_YEARS.between(start, end);
    }

    /**
     * Get the quarter number in a short pattern-style
     * @param date the {@link LocalDate}
     * @return the quarter in the format "Q{1-4}"
     */
    public static String shortQuarterNumber(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

        return date.format(DateTimeFormatter.ofPattern("QQQ", Locale.ENGLISH));
    }

    /**
     * Get the quarter number in a long pattern-style
     * @param date the {@link LocalDate}
     * @return the quarter in the format "n-th quarter"
     */
    public static String longQuarterNumber(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

        return date.format(DateTimeFormatter.ofPattern("QQQQ", Locale.ENGLISH));
    }

    /**
     * Get quarter number for a given date
     * @param date the {@link LocalDate}
     * @return quarter number
     */
    public static Integer getQuarterNumber(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date can not be null");
        }

        return date.get(IsoFields.QUARTER_OF_YEAR);
    }

    /**
     * Split date-time range into equal intervals
     * @param start the start date
     * @param end the end date
     * @param n the intervals
     * @return list of date-time
     */
    public static List<LocalDateTime> splitDateTimeRange(LocalDateTime start, LocalDateTime end, int n) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        Duration range = Duration.between(start, end);
        Duration interval = range.dividedBy(n - 1);
        List<LocalDateTime> listOfDates = new ArrayList<>();
        LocalDateTime timeline = start;
        for (int i = 0; i < n - 1; i++) {
            listOfDates.add(timeline);
            timeline = timeline.plus(interval);
        }
        listOfDates.add(end);
        return listOfDates;
    }

    /**
     * Split date-time range into days
     * @param start the start date
     * @param end the end date
     * @return list of date
     */
    public static List<LocalDate> splitDateRangeIntoDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        long numOfDaysBetween = ChronoUnit.DAYS.between(start, end);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(start::plusDays)
                .collect(Collectors.toList());
    }

    /**
     * Split date-time range into months
     * @param start the start date
     * @param end the end date
     * @return list of year-month
     */
    public static List<YearMonth> splitDateRangeIntoMonths(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        long numOfDaysBetween = ChronoUnit.MONTHS.between(start, end);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> YearMonth.from(start.plusMonths(i)))
                .collect(Collectors.toList());
    }

    /**
     * Split date-time range into years
     * @param start the start date
     * @param end the end date
     * @return the list of years
     */
    public static List<Year> splitDateRangeIntoYears(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        long numOfDaysBetween = ChronoUnit.YEARS.between(start, end);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> Year.from(start.plusYears(i)))
                .collect(Collectors.toList());
    }

    /**
     * Split date-time range into weeks
     * @param start the start date
     * @param end the end date
     * @return the list of year-week
     */
    public static List<YearWeek> splitDateRangeIntoWeeks(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start date or end date can not be null");
        }

        long numOfDaysBetween = ChronoUnit.WEEKS.between(start, end);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> YearWeek.from(start.plusWeeks(i)))
                .collect(Collectors.toList());
    }
}
