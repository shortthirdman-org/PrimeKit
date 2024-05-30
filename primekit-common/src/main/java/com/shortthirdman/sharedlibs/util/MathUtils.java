package com.shortthirdman.sharedlibs.util;

public class MathUtils {

    /**
     * Calculate the variance for collection of elements
     *
     * @param array the input array
     * @param n the number of array elemnts
     * @return
     */
    public static double variance(double[] array, int n) {
        double sum = 0;
        for (int i = 0; i < n; i++)
            sum += array[i];
        double mean = sum / (double) n;

        double sqDiff = 0;
        for (int i = 0; i < n; i++)
            sqDiff += (array[i] - mean) * (array[i] - mean);

        return sqDiff / n;
    }

    /**
     * Calculate the standard deviation for collection of elements
     *
     * @param array the input Double array
     * @param n the number of array elemnts
     * @return
     */
    public static double standardDeviation(double[] array, int n) {
        return Math.sqrt(variance(array, n));
    }

    /**
     * Calculate the average for collection of elements
     *
     * @param array the input array
     * @param n the number of elements in array
     * @return
     */
    public static double average(double[] array, int n) {
        double sum = 0;
        double finalsum = 0;
        for (double i : array) {
            finalsum = (sum += i);
        }
        return finalsum / n;
    }
}
