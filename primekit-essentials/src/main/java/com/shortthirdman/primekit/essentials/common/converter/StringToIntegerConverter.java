package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String input, @Nullable Object... options) throws Exception {

        Integer result = null;

        try {
            if (Objects.nonNull(input) && !input.isBlank()) {
                result = Integer.parseInt(input);
            }
        } catch (NumberFormatException nfe) {
            throw nfe;
        }

        return result;
    }
}
