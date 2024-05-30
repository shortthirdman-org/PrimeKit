package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToDoubleConverter implements Converter<String, Double> {

    @Override
    public Double convert(String input, @Nullable Object... options) throws Exception {
        return 0.0;
    }
}
