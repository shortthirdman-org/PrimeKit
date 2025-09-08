package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;

import java.util.Map;

public class StringToMapConverter implements Converter<String, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(String input, @Nullable Object... options) throws Exception {
        return Map.of();
    }
}
