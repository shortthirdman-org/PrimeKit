package com.shortthirdman.sharedlibs.util;

import java.util.Arrays;

/**
 * Class containing utility methods on Arrays
 *
 * @author shortthirdman-org
 * @version 1.0
 */
public class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * Rotate an array of objects by an provided degree
     *
     * @param array the input array of object
     * @param amount the amount to rotate
     * @return the rotated array
     */
    public static Object[] rotateArray(Object[] array, int amount) {
        if (array == null || amount < 0) {
            throw new IllegalArgumentException("Input array or rotation amount can not be null");
        }

        Object[] arr = Arrays.copyOf(array, array.length);

        for (int i = 0; i < amount; i++) {
            for (int j = arr.length - 1; j > 0; j--) {
                Object temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
        return arr;
    }

    /**
     * Rotate a floating-point array by an provided degree
     *
     * @param array the input array of floating-point numbers
     * @param amount the amount to rotate
     * @return the rotated array
     */
    public static float[] rotateArray(float[] array, int amount) {
        if (array == null || amount < 0) {
            throw new IllegalArgumentException("Input array or rotation amount can not be null");
        }

        float[] arr = Arrays.copyOf(array, array.length);

        for (int i = 0; i < amount; i++) {
            for (int j = arr.length - 1; j > 0; j--) {
                float temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
        return arr;
    }

    /**
     * Rotate an array of objects by a provided range
     *
     * @param array the input array of objects
     * @param from the starting index of range
     * @param to the ending index of range
     * @param n the number of elements
     * @param <T> the parameterized data-type
     */
    public static <T> void rotateArrayRange(T[] array, int from, int to, int n) {
        rotateArrayRange(array, from, to, n, Arrays.copyOfRange(array, from, to));
    }

    /**
     * Rotate an array of objects by a provided range by creating copy of range
     *
     * @param array the input array of objects
     * @param from the starting index of range
     * @param to the ending index of range
     * @param n the number of elements
     * @param copyOfRange the copy of range of elements
     * @param <T> the parameterized data-type
     */
    public static <T> void rotateArrayRange(T[] array, int from, int to, int n, T[] copyOfRange) {
        final int d = to - from;
        if (n < 0)
            n = to - from + n;
        for (int i = 0; i < d; ++i)
            array[from + i] = copyOfRange[(i + n) % d];
    }

    /**
     * Rotate an array of integers by a provided range
     *
     * @param array the input array of integer
     * @param from the starting index of range
     * @param to the ending index of range
     * @param n the number of elements
     */
    public static void rotateArrayRange(int[] array, int from, int to, int n) {
        rotateArrayRange(array, from, to, n, Arrays.copyOfRange(array, from, to));
    }

    /**
     * Rotate an array of integers by a provided range
     *
     * @param array the input array of integer
     * @param from the starting index of range
     * @param to the ending index of range
     * @param n the number of elements
     * @param copyOfRange the copy of range of elements
     */
    public static void rotateArrayRange(int[] array, int from, int to, int n, int[] copyOfRange) {
        final int d = to - from;
        if (n < 0)
            n = to - from + n;
        for (int i = 0; i < d; ++i)
            array[from + i] = copyOfRange[(i + n) % d];
    }
}
