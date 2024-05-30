package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Component
public class StringToDateConverter implements Converter<String, Date> {

    private static final String ISO_DATE_FORMAT = "";

    @Override
    public Date convert(String input, @Nullable Object... options) throws Exception {
        Date result = null;
        String format = ISO_DATE_FORMAT;
        Locale locale = Locale.ENGLISH;

        if (Objects.nonNull(options)) {
            format = options.length > 1 ? String.valueOf(options[0]) : ISO_DATE_FORMAT;
            locale = options.length > 1 ? (Locale) options[1] : Locale.ENGLISH;
        }

        DateFormat fmt = new SimpleDateFormat(format, locale);

        try {
            if (!input.isEmpty()) {
                result = fmt.parse(input);
            }
        } catch (ParseException pe) {
            throw pe;
        }

        return result;
    }
}
