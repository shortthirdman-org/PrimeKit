package com.shortthirdman.sharedlibs.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtils {

    private CollectionUtils() {

    }

    /**
     * Converts a array of elements into list
     *
     * @param array the typed array
     * @return list of elements
     */
    public static <T> List<T> toList(T[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }

    /**
     * Converts a array of elements into set
     *
     * @param array the typed array
     * @return set of elements
     */
    public static <T> Set<T> toSet(T[] array) {
        return Arrays.stream(array).collect(Collectors.toSet());
    }

    /**
     * @param array the typed array
     * @return
     */
    public static <T> Set<T> toSet(T[] array, boolean preserveOrder) {
        if (preserveOrder) {
            return Arrays.stream(array)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return Arrays.stream(array).collect(Collectors.toSet());
    }

    /**
     * @param collection
     * @param preserveOrder
     * @param <T>
     * @return
     */
    public static <T> Set<T> toSet(Collection<T> collection, boolean preserveOrder) {
        if (preserveOrder) {
            return new LinkedHashSet<>(collection);
        }
        return new HashSet<>(collection);
    }

    /**
     * @param array the array of items
     * @param predicate the filter predicate condition
     * @param <T> the typed-data of items in collection
     * @return the filtered collection of elements
     */
    public static <T> List<T> filterArray(T[] array, Predicate<? super T> predicate) {
        return Arrays.stream(array).filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param collection the collection of items
     * @param predicate the filter predicate condition
     * @param <T> the typed-data of items in collection
     * @return the filtered collection of items
     */
    public static <T> List<T> filterCollection(Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream().filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks if the list is valid - whether the list is empty or size is zero
     *
     * @param list the list of object to be checked
     * @return boolean value
     */
    public static Boolean isListValid(List<?> list) {
        if (list != null) {
            if (list.isEmpty()) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } else {
            return Boolean.FALSE;
        }
    }
}
