package com.shortthirdman.sharedlibs.util;

import com.shortthirdman.sharedlibs.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringUtilsTest {

    @Test
    public void testSnakeToCamelCase() {
        String inputValue = "";
        String expectedValue = "";

        String actualValue = StringUtils.snakeToCamel(inputValue);
        Assert.assertEquals("Snake-case to camel-case conversion successful", expectedValue, actualValue);
    }

    @Test
    public void testCamelToSnakeCase() {
        String inputValue = "";
        String expectedValue = "";

        String actualValue = StringUtils.camelToSnake(inputValue);
        Assert.assertEquals("Camel-case to snake-case conversion successful", expectedValue, actualValue);
    }

    @Test
    public void testConvertDelimitedStringToList() {
        String inputValue = "";
        List<String> expectedValue = Arrays.asList("", "", "");

        List<String> actualValue = StringUtils.convertToList(inputValue, Constants.DEFAULT_DELIMITER.toString());
        boolean result = actualValue.size() == expectedValue.size() && actualValue.containsAll(expectedValue) && expectedValue.containsAll(actualValue);
        Assert.assertTrue("Given delimited string converted to list", result);
    }
}
