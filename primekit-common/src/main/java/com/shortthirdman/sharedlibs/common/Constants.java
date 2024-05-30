package com.shortthirdman.sharedlibs.common;

public enum Constants {

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

    ZERO("0");

    private final String text;

    Constants(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Constants fromString(String parameterName) {
        if (parameterName != null) {
            for (Constants objType: Constants.values()) {
                if (parameterName.equals(objType.text)) {
                    return objType;
                }
            }
        }
        return null;
    }
}
