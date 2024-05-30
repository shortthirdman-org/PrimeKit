package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class StringToBooleanConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convert(String input, @Nullable Object... options) throws Exception {

        if (input == null || StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Input can not be null or empty");
        }

        if ("true".equalsIgnoreCase(input) || "T".equalsIgnoreCase(input)) {
            return true;
        } else if ("false".equalsIgnoreCase(input) || "F".equalsIgnoreCase(input)) {
            return false;
        }

        return null;
    }
}
