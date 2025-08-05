package com.shortthirdman.primekit.essentials.common.enums;

import lombok.Getter;

@Getter
public enum Seasons {

    WINTER("Winter"),
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    AUTUMN("Autumn");

    private final String name;

    Seasons(String name) {
        this.name = name;
    }
}
