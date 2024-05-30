package com.shortthirdman.sharedlibs.util;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringPadderImpl implements StringPadder {

    StringPadderImpl() {
    }

    @Override
    public String padLeft(String stringToPad, int totalLength) {
        return padLeft(stringToPad, totalLength, ' ');
    }

    @Override
    public String padLeft(String stringToPad, int totalLength, char paddingCharacter) {
        return getStringToBeAdded(stringToPad, totalLength, paddingCharacter) + stringToPad;
    }

    @Override
    public String padRight(String stringToPad, int totalLength) {
        return padRight(stringToPad, totalLength, ' ');
    }

    @Override
    public String padRight(String stringToPad, int totalLength, char paddingCharacter) {
        return stringToPad + getStringToBeAdded(stringToPad, totalLength, paddingCharacter);
    }

    private String getStringToBeAdded(String stringToPad, int totalLength, char paddingCharacter) {
        int quantity = totalLength - stringToPad.length();
        if (quantity > 0) {
            String text = Character.toString(paddingCharacter);
            return String.join("", Collections.nCopies(quantity, text));
//            return Stream.generate(() -> text).limit(quantity).collect(Collectors.joining());
        } else {
            return "";
        }
    }
}
