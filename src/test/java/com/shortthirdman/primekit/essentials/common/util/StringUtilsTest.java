package com.shortthirdman.primekit.essentials.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSnakeToCamel() {
        // Basic conversions
        assertEquals("HelloWorld", StringUtils.snakeToCamel("hello_world"));
        assertEquals("MyVariableName", StringUtils.snakeToCamel("my_variable_name"));
        assertEquals("SnakeCaseExample", StringUtils.snakeToCamel("snake_case_example"));

        // Already camel case input
        assertEquals("CamelCase", StringUtils.snakeToCamel("camelCase"));

        // Input with no underscores
        assertEquals("Hello", StringUtils.snakeToCamel("hello"));

        // Input with leading underscore (should throw StringIndexOutOfBoundsException or incorrect behavior)
        assertEquals("HelloWorld", StringUtils.snakeToCamel("_hello_world")); // Depending on intent, may need to handle this

        // All uppercase input
        assertEquals("TestCase", StringUtils.snakeToCamel("test_case"));
        assertEquals("XMLHttpRequest", StringUtils.snakeToCamel("x_m_l_http_request"));

        // Underscores at the end
        assertEquals("Example_", StringUtils.snakeToCamel("example_")); // might throw or behave oddly

        // Multiple consecutive underscores
        assertEquals("Multiple_ConsecutiveUnderscores", StringUtils.snakeToCamel("multiple__consecutive_underscores"));

        // Empty string
        assertThrows(IllegalArgumentException.class, () -> StringUtils.snakeToCamel(""));

        // Single character
        assertEquals("A", StringUtils.snakeToCamel("a"));
    }

    @Test
    void testCamelToSnake() {
        // Basic conversions
        assertEquals("hello_world", StringUtils.camelToSnake("HelloWorld"));
        assertEquals("my_variable_name", StringUtils.camelToSnake("MyVariableName"));
        assertEquals("snake_case_example", StringUtils.camelToSnake("SnakeCaseExample"));

        // Single word, no uppercase transitions
        assertEquals("hello", StringUtils.camelToSnake("Hello"));

        // Mixed casing and acronyms
        assertEquals("x_m_l_http_request", StringUtils.camelToSnake("XMLHttpRequest"));
        assertEquals("get_u_r_l", StringUtils.camelToSnake("getURL"));

        // Lower camelCase
        assertEquals("camel_case", StringUtils.camelToSnake("camelCase"));

        // All lowercase input
        assertEquals("simple", StringUtils.camelToSnake("simple"));

        // Leading capital letter
        assertEquals("a_test", StringUtils.camelToSnake("ATest"));

        // Edge cases: null and empty input
        assertThrows(IllegalArgumentException.class, () -> StringUtils.camelToSnake(null));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.camelToSnake(""));

        // Single character input
        assertEquals("a", StringUtils.camelToSnake("A"));
        assertEquals("b", StringUtils.camelToSnake("b"));
    }

    @Test
    void testGenerateRandomAlphanumeric() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.generateRandomAlphanumeric(0));

        // Test length of generated string
        int length = 10;
        String randomString = StringUtils.generateRandomAlphanumeric(length);
        assertEquals(length, randomString.length());

        // Test if the generated string contains only alphanumeric characters
        assertTrue(randomString.matches("^[a-zA-Z0-9]+$"));

        // Test with different lengths
        for (int i = 1; i <= 100; i++) {
            String randomStr = StringUtils.generateRandomAlphanumeric(i);
            assertEquals(i, randomStr.length());
            assertTrue(randomStr.matches("^[a-zA-Z0-9]+$"));
        }
    }
}