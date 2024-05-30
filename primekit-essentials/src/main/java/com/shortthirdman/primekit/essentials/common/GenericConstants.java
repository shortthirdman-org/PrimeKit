package com.shortthirdman.primekit.essentials.common;

import lombok.Getter;

@Getter
public enum GenericConstants {

    START("START"),
    END("END"),

    COMMA_SPACE(", "),
    COMMA(","),
    DOT_PERIOD("."),
    UNDERSCORE("_"),

    EMPTY_SPACE(""),
    BLANK_SPACE(" "),
    DEFAULT_DELIMITER(","),
    DOUBLE_COLON("::"),
    COLON(":"),
    HYPHEN("-"),
    SEMICOLON(";"),
    PIPE("|"),
    ASTERISK("*"),

    OPEN_PARENTHESIS("("),
    CLOSED_PARENTHESIS(")"),
    OPEN_BRACES("{"),
    CLOSED_BRACES("}"),

    SINGLE_QUOTE("'"),
    DOUBLE_QUOTE("\""),

    FORWARD_SLASH("/"),
    BACKWARD_SLASH("\\"),

    ZERO("0"),

    Y("Y"),
    N("N"),
    YES("Yes"),
    NO("No"),

    TRUE_VALUE("true"),
    FALSE_VALUE("false");

    private final String value;

    GenericConstants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static GenericConstants fromString(String parameterName) {
        if (parameterName != null) {
            for (GenericConstants objType: GenericConstants.values()) {
                if (parameterName.equals(objType.value)) {
                    return objType;
                }
            }
        }

        return null;
    }
}
