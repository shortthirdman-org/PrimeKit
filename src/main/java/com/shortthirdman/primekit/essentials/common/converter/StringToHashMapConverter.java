package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class StringToHashMapConverter implements Converter<String, HashMap<String, Object>> {

    @Override
    public HashMap<String, Object> convert(String input, @Nullable Object... options) throws Exception {
        return null;
    }
}
