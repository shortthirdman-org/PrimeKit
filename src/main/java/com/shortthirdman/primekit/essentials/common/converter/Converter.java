package com.shortthirdman.primekit.essentials.common.converter;

import jakarta.annotation.Nullable;

public interface Converter<P, Q> {

    Q convert(P input, @Nullable Object... options) throws Exception;
}
