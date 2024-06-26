package com.shortthirdman.primekit.essentials;

import com.shortthirdman.primekit.essentials.common.util.DateUtils;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DateUtilsTest {

    @Test
    public void givenZonedDateTime_whenConvertToDate_thenCorrect() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        Date date = DateUtils.convertToDate(zdt);
        assertEquals(Date.from(zdt.toInstant()), date);
    }

    @Test
    public void givenDate_whenConvertToZonedDateTime_thenCorrect() {
        Date date = new Date();
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdt = DateUtils.convertToZonedDateTime(date, zoneId);
        assertEquals(date.toInstant().atZone(zoneId), zdt);
    }

    @Test
    public void givenZonedDateTime_whenUsingLocalDateTime_thenConvertToTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 4, 17, 12, 30, 0, 0, ZoneId.systemDefault());
        Timestamp actualResult = DateUtils.convertToTimeStamp(zonedDateTime);
        Timestamp expectedResult = Timestamp.valueOf("2024-04-17 12:30:00");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void givenZonedDateTime_whenUsingInstant_thenConvertToTimestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 4, 17, 12, 30, 0, 0, ZoneId.systemDefault());
        Timestamp actualResult = DateUtils.convertToTimeStampFromInstant(zonedDateTime);
        Timestamp expectedResult = Timestamp.valueOf("2024-04-17 12:30:00");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void givenTimestamp_whenUsingLocalDateTime_thenConvertToZonedDateTime() {
        Timestamp timestamp = Timestamp.valueOf("2024-04-17 12:30:00");
        ZonedDateTime actualResult = DateUtils.convertToZonedDateTimeFromLocalDateTime(timestamp);
        ZonedDateTime expectedResult = ZonedDateTime.of(2024, 4, 17, 12, 30, 0, 0, ZoneId.systemDefault());
        assertEquals(expectedResult.toLocalDate(), actualResult.toLocalDate());
        assertEquals(expectedResult.toLocalTime(), actualResult.toLocalTime());
    }

    @Test
    public void givenTimestamp_whenUsingCalendar_thenConvertToZonedDateTime() {
        Timestamp timestamp = Timestamp.valueOf("2024-04-17 12:30:00");
        ZonedDateTime actualResult = DateUtils.convertToCalendarZonedDateTime(timestamp);
        ZonedDateTime expectedResult = ZonedDateTime.of(2024, 4, 17, 12, 30, 0, 0, ZoneId.systemDefault());
        assertEquals(expectedResult.toLocalDate(), actualResult.toLocalDate());
        assertEquals(expectedResult.toLocalTime(), actualResult.toLocalTime());
    }

    @Test
    public void givenTimestamp_whenUsingInstant_thenConvertToZonedDateTime() {
        Timestamp timestamp = Timestamp.valueOf("2024-04-17 12:30:00");
        ZonedDateTime actualResult = DateUtils.convertToInstantZonedDateTime(timestamp);
        ZonedDateTime expectedResult = ZonedDateTime.of(2024, 4, 17, 12, 30, 0, 0, ZoneId.systemDefault());
        assertEquals(expectedResult.toLocalDate(), actualResult.toLocalDate());
        assertEquals(expectedResult.toLocalTime(), actualResult.toLocalTime());
    }

    @Test
    public void givenLocalDate_isWeekend_thenTrue() {
        LocalDate someDate = LocalDate.of(2021, 1, 2); // 2nd-Jan-2021
        assertTrue(DateUtils.isWeekend(someDate));
    }

    @Test
    public void givenDate_isWeekend_thenTrue() {
        Date someDate = new Date(2021, 0, 2);
        assertTrue(DateUtils.isWeekend(someDate));
    }

    @Test
    public void givenLocalDate_addBusinessDays() {
        LocalDate today = LocalDate.of(2020, 5, 5);

        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2020, 5, 11));
        holidays.add(LocalDate.of(2020, 5, 1));

        LocalDate expectedDate1 = LocalDate.of(2020, 5, 15);
        LocalDate actualDate1 = DateUtils.addBusinessDays(today, 8, List.of());
        assertEquals(expectedDate1, actualDate1, "Dates are matching");

        LocalDate expectedDate2 = LocalDate.of(2020, 5, 18);
        LocalDate actualDate2 = DateUtils.addBusinessDays(today, 8, holidays);
        assertEquals(expectedDate2, actualDate2, "Dates are matching");
    }

    @Test
    public void givenLocalDate_subtractBusinessDays() {
        LocalDate today = LocalDate.of(2020, 5, 5);

        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2020, 5, 11));
        holidays.add(LocalDate.of(2020, 5, 1));

        LocalDate expectedDate1 = LocalDate.of(2020, 4, 22);
        LocalDate actualDate1 = DateUtils.subtractBusinessDays(today, 8, List.of());
        assertEquals(expectedDate1, actualDate1, "Dates are matching");

        LocalDate expectedDate2 = LocalDate.of(2020, 4, 21);
        LocalDate actualDate2 = DateUtils.subtractBusinessDays(today, 8, holidays);
        assertEquals(expectedDate2, actualDate2, "Dates are matching");
    }

    @Test
    public void givenStartDate_givenEndDate_datesBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(2);

        List<LocalDate> dateList = DateUtils.datesBetween(startDate, endDate);
        assertEquals(61, dateList.size());
        assertNotEquals(List.of(), dateList);
    }

    @Test
    public void givenDateString_parseStrictly() {
        LocalDate expectedValue1 =  LocalDate.of(2019, 2, 27);
        assertEquals(expectedValue1, DateUtils.strictParseDate("2019-02-27", "yyyy-MM-dd"));

        LocalDate expectedValue2 =  LocalDate.of(2019, 2, 28);
        assertEquals(expectedValue2, DateUtils.strictParseDate("2019-02-28", "yyyy-MM-dd"));

        LocalDate expectedValue3 =  LocalDate.of(2019, 2, 29);
        assertEquals(expectedValue3, DateUtils.strictParseDate("2019-02-29", "yyyy-MM-dd"));
    }

    @Test
    public void givenDateTimeString_parseStrictly() {
        LocalDateTime expectedValue1 = LocalDateTime.of(2019, 2, 27, 11, 23, 56, 1234);
        assertEquals(expectedValue1, DateUtils.strictParseDateTime("2019-02-27T11:23:56.1234", null));

        LocalDateTime expectedValue2 = LocalDateTime.of(2019, 2, 27, 11, 23, 56, 1234);
        assertEquals(expectedValue2, DateUtils.strictParseDateTime("2019-02-28T11:23:56.1234", null));

        LocalDateTime expectedValue3 = LocalDateTime.of(2019, 2, 27, 11, 23, 56, 1234);
        assertEquals(expectedValue3, DateUtils.strictParseDateTime("2019-02-29T11:23:56.1234", null));
    }

    @Test
    public void givenMonthNumber_monthShortName() {
        assertEquals("Jan", DateUtils.monthNumberToShortName(1));
        assertNotEquals("JAN", DateUtils.monthNumberToShortName(1));
        assertNotNull(DateUtils.monthNumberToShortName(1));

        assertEquals("Jun", DateUtils.monthNumberToShortName(6));
        assertNotEquals("JUN", DateUtils.monthNumberToShortName(6));
        assertNotNull(DateUtils.monthNumberToShortName(6));
    }

    @Test
    public void givenMonthNumber_monthFullName() {
        assertEquals("January", DateUtils.monthNumberToFullName(1));
        assertNotEquals("JANUARY", DateUtils.monthNumberToFullName(1));
        assertNotNull(DateUtils.monthNumberToFullName(1));
    }

    @Test
    public void givenMonthNumber_monthName() {
        assertEquals("JANUARY", DateUtils.monthNumberToName(1));
        assertNotEquals("January", DateUtils.monthNumberToName(1));
        assertNotNull(DateUtils.monthNumberToName(1));
    }

    @Test
    public void givenMonthShortName_convertMonthNumber() {
        assertEquals(1, DateUtils.monthShortNameToNumber("Jan"));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.monthShortNameToNumber("Duy"));
    }

    @Test
    public void givenMonthFullName_convertMonthNumber() {
        assertEquals(1, DateUtils.monthNameToNumber("January"));
    }

    @Test
    public void givenMonthShortName_convertFullName() {
        assertEquals("January", DateUtils.monthShortNameToFullName("Jan"));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.monthShortNameToFullName("Fut"));
    }

    @Test
    public void givenLocalDate_getQuarter() {
        LocalDate date = LocalDate.of(2024, Month.FEBRUARY, 19);
        assertEquals(1, DateUtils.getQuarterNumber(date));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.getQuarterNumber(null));
    }

    @Test
    public void givenLocalDate_getFormattedQuarter() {
        LocalDate date = LocalDate.of(2024, Month.FEBRUARY, 19);
        assertEquals("Q1", DateUtils.shortQuarterNumber(date));
        assertEquals("1st quarter", DateUtils.longQuarterNumber(date));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.shortQuarterNumber(null));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.longQuarterNumber(null));
    }

    @Test
    public void givenStartDate_givenEndDate_getQuarterCount() {
        LocalDate start = LocalDate.of(2024, Month.FEBRUARY, 19);
        LocalDate end = LocalDate.of(2024, Month.MAY, 5);
        assertEquals(1, DateUtils.quarterCount(start, end));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.quarterCount(null, end));
        assertThrows(IllegalArgumentException.class, () -> DateUtils.quarterCount(start, null));
    }
}
