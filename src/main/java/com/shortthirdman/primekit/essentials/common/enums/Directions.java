package com.shortthirdman.primekit.essentials.common.enums;

import lombok.Getter;

@Getter
public enum Directions {

    EAST("East", "E"),
    NORTH("North", "N"),
    NORTH_EAST("North-East", "NE"),
    NORTH_WEST("North-West", "NW"),
    SOUTH("South", "S"),
    SOUTH_EAST("South-East", "SE"),
    SOUTH_WEST("South-West", "SW"),
    WEST("West", "SW");

    private final String name;
    private final String symbol;

    Directions(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
