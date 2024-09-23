package com.tech.common.collection;

import java.util.List;
import java.util.stream.IntStream;

/**
 * The type List handler.
 */
public class ListHandler {

    /**
     * Partition list.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @param size the size
     * @return the list
     */
    public static <T> List<List<T>> partitionList(List<T> list, int size) {
        return IntStream.range(0, (list.size() + size - 1) / size)
                .mapToObj(i -> list.subList(i * size, Math.min(size * (i + 1), list.size())))
                .toList();
    }

}
