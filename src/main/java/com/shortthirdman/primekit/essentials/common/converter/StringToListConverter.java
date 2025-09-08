package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class StringToListConverter implements Converter<String, List<String>> {

    @Override
    public List<String> convert(String input, @Nullable Object... options) throws Exception {

        String delimiter = "";
        List<String> result = List.of();

        if (Objects.nonNull(options)) {
            delimiter = options.length == 1 ? String.valueOf(options[0]) : "";
        }

        if (input == null) {
            throw new IllegalArgumentException("");
        }

        return result;
    }
}
