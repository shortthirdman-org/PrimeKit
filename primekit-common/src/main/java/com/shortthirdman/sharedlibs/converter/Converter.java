package com.shortthirdman.sharedlibs.converter;

public interface Converter<P, Q> {

    Q convert(P input) throws Exception;
}
