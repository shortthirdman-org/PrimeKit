package com.shortthirdman.primekit.essentials;

import com.shortthirdman.primekit.essentials.common.util.DateUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

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
}
