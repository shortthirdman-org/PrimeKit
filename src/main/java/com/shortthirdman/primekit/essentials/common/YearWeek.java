package com.shortthirdman.primekit.essentials.common;

import java.text.MessageFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;


public record YearWeek(int year, int week) {

    public static YearWeek from(TemporalAccessor temporal) {
        Objects.requireNonNull(temporal, "temporal");
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(temporal))) {
                temporal = LocalDate.from(temporal);
            }
            return new YearWeek(temporal.get(ChronoField.YEAR), temporal.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        } catch (DateTimeException ex) {
            String mf = MessageFormat.format("Unable to obtain YearWeek from TemporalAccessor: {0} of type {1}", temporal, temporal.getClass().getName());
            throw new DateTimeException(mf, ex);
        }
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}-{1}", year, week);
    }
}
