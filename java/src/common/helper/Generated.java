package common.helper;

import java.lang.reflect.Array;

/**
 * array generator
 *
 * @author mindview
 * @version 1.0
 * @date 2020/11/21 / 21:38
 */
public class Generated {
    // Fill an existing array:
    public static <T> T[] array(T[] a, Generator<T> gen) {

        return CollectionData.list(gen, a.length).toArray(a);
//        return new CollectionData<>(gen, a.length).toArray(a);
    }

    // Create a new array:
    @SuppressWarnings("unchecked")
    public static <T> T[] array(Class<T> type, Generator<T> gen, int size) {
        T[] a = (T[]) Array.newInstance(type, size);
        return new CollectionData<>(gen, size).toArray(a);
    }
}
