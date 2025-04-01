package com.shortthirdman.primekit.essentials;

import com.shortthirdman.primekit.essentials.common.util.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathUtilsTests {

    @Test
    void testVariance() {
        double[] array = {1, 2, 3, 4, 5};
        int n = array.length;
        assertEquals(2.5, MathUtils.variance(array, n));
    }

    @Test
    void testStandardDeviation() {
        double[] array = {1, 2, 3, 4, 5};
        int n = array.length;
        assertEquals(1.5811388300841898, MathUtils.standardDeviation(array, n));
    }

    @Test
    void testAverage() {
        double[] array = {1, 2, 3, 4, 5};
        int n = array.length;
        assertEquals(3.0, MathUtils.average(array, n));
    }

    @Test
    void testVarianceWithEmptyArray() {
        double[] array = {};
        int n = array.length;
        assertThrows(IllegalArgumentException.class, () -> MathUtils.variance(array, n));
    }

    @Test
    void testStandardDeviationWithEmptyArray() {
        double[] array = {};
        int n = array.length;
        assertThrows(IllegalArgumentException.class, () -> MathUtils.standardDeviation(array, n));
    }

    @Test
    void testAverageWithEmptyArray() {
        double[] array = {};
        int n = array.length;
        assertThrows(IllegalArgumentException.class, () -> MathUtils.average(array, n));
    }

    @Test
    void testVarianceWithSingleElement() {
        double[] array = {5};
        int n = array.length;
        assertEquals(0.0, MathUtils.variance(array, n));
    }

    @Test
    void testStandardDeviationWithSingleElement() {
        double[] array = {5};
        int n = array.length;
        assertEquals(0.0, MathUtils.standardDeviation(array, n));
    }

    @Test
    void testAverageWithSingleElement() {
        double[] array = {5};
        int n = array.length;
        assertEquals(5.0, MathUtils.average(array, n));
    }
}
