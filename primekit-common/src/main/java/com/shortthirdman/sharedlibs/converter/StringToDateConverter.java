package com.shortthirdman.sharedlibs.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String input) throws Exception {
        Date result = null;
        DateFormat fmt = new SimpleDateFormat("", Locale.ENGLISH);
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
