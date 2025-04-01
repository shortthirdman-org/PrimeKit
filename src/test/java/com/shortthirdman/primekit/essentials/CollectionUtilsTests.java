package com.shortthirdman.primekit.essentials;

import com.shortthirdman.primekit.essentials.common.util.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionUtilsTests {

    @Test
    public void distinctByKeysTest() {
        // Test with distinct keys
        Predicate<String> predicate = CollectionUtils.distinctByKeys(String::length);
        assertTrue(predicate.test("apple"));
        assertTrue(predicate.test("banana"));

        // Test with duplicate keys
        assertFalse(predicate.test("orange"));
        assertTrue(predicate.test("grape"));

        // Test with null input
        assertThrows(NullPointerException.class, () -> CollectionUtils.distinctByKeys((Function<Object, ?>) null));
    }

    @Test
    public void sortByValueTest() {
        // Test with normal map
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 5);
        map.put("banana", 2);
        map.put("orange", 8);
        map.put("grape", 1);

        Map<String, Integer> sortedMap = CollectionUtils.sortByValue(map);
        assertEquals(1, sortedMap.get("grape"));
        assertEquals(2, sortedMap.get("banana"));
        assertEquals(5, sortedMap.get("apple"));
        assertEquals(8, sortedMap.get("orange"));

        // Test with empty map
        Map<String, Integer> emptyMap = new HashMap<>();
        Map<String, Integer> sortedEmptyMap = CollectionUtils.sortByValue(emptyMap);
        assertTrue(sortedEmptyMap.isEmpty());

        // Test with null map
        assertThrows(NullPointerException.class, () -> CollectionUtils.sortByValue(null));
    }
}
