package oo;

import java.util.*;
import java.util.stream.Collectors;
import static com.google.common.base.Optional.fromNullable;

/**
 * {@link java.util.Optional} and
 * {@link com.google.common.base.Optional}
 * offer useful methods to deal with
 * <b>{@code null}</b> troubles.
 *
 * @author wangy
 * @date 2021.01.26/0026 14:13
 */
@SuppressWarnings({ "raw", "unused" })
public class SuckNull {

    private static Object[] OBA = new Object[] {
            1,
            "2",
            null,
            new SuckNull() };

    /**
     * <b>!不要</b>直接使用<code>Arrays.asList(OBA)</code>，
     * 这样做无法推断List的类型。
     * <p>
     * 会导致后续使用{@link Optional}对集合元素进行操作时抛出
     * {@link UnsupportedOperationException}
     */
    private static List<Object> someList = new ArrayList<>(
            Arrays.asList(OBA));

    private static Set<Object> someHashSet = new HashSet<>(
            Arrays.asList(OBA));

    private static Map<Object, Object> someMap = new HashMap<>() {
        {
            Key key;
            put(key = new Key(), new Value(key));
            put(key = new Key(), new Value(key));
            put(null, new Value(null));
            put(key = new Key(), new Value(key));
            put(key = new Key(), null);
        }
    };

    /**
     * the <b>guava</b> also offer
     * {@link com.google.common.base.Optional}
     * and {@link com.google.common.base.Objects}
     * for basic Object operations.
     * <p>
     *
     * @see java.util.Optional
     * @see java.util.Objects
     */
    static void listOption() {
        // non-null elements in someList
        // usage of Optional in jdk
        System.out.println("NULL elements in list: " +
                someList
                        .stream()
                        .filter(e -> !Optional
                                .ofNullable(e)
                                .isPresent())
                        .count());

        // use stream filter null element
        // usage of Objects in jdk
        List<Object> list1 = someList
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println("list1: " + list1);

        // stream operation does not change origin list
        System.out.println("Stream operation did not " +
                "modify origin list: " + someList);

        // just operate the stream
        someList
                .stream()
                .filter(Objects::nonNull)
                .forEach(System.out::print);
        System.out.println();

        /*
         * Stream is much convenient than Optional.
         * Different to Stream operation, the Optional operation
         * changes the original List
         */
        List<Object> list2 = Optional
                .ofNullable(someList)
                .map(l -> {
                    l.removeIf(Objects::isNull);
                    return l;
                })
                .get();
        System.out.println("list2: " + list2);
        System.out.println("someList modified by Optional Operation: "
                + someList);

        someList.add(null);
        System.out.println("someList: " + someList);

        // the google guava common utils offer similar operations.
        List<Object> list3 = fromNullable(someList)
                .transform(input -> {
                    input.removeIf(e -> !fromNullable(e).isPresent());
                    return input;
                })
                .get();

        System.out.println("list3: " + list3);
        System.out.println("someList: " + someList);
    }

    static void setOption() {
        // Remove null element from `someHashSet` by using Optional,
        // and the origin `someHashSet` is modified permanently.
        Optional.ofNullable(someHashSet)
                .filter(s -> s.contains(null))
                .ifPresent(s -> s.removeIf(
                        o -> !Optional
                                .ofNullable(o)
                                .isPresent()));
        System.out.println(someHashSet);
    }

    /**
     * Stream and Optional operation for HashMap
     */
    static void mapOption() {

        // use stream to filter null key and null values
        Map<Key, Value> map = someMap.entrySet().stream()
                .filter(entry -> Optional
                        .ofNullable(entry.getKey())
                        .isPresent()
                        &&
                        Optional
                                .ofNullable(entry.getValue())
                                .isPresent())
                .collect(Collectors.toMap(
                        entry -> (Key) entry.getKey(),
                        entry -> (Value) entry.getValue()));

        map.forEach(
                (key, value) -> System.out.println(key + ", " +
                        value.getValue()));

        // use Optional to get value with `null` key and `null` value
        Optional<Map<Object, Object>> mapNullKeyValue = Optional
                .ofNullable(someMap)
                .filter(m -> m.containsKey(null))
                .filter(m -> m.containsValue(null));
        /*
         * The Optional present shows that `someMap` contains `null` key
         * and `null` value, but it doesn't work like stream, no change
         * made to `someMap`.
         *
         * So the consumer below will print all elements in `someMap`.
         */
        // mapNullKeyValue
        // .ifPresent(objectObjectMap -> objectObjectMap.forEach((k, v)
        // -> System.out.println(k + "," + v)));

        final Value[] nullKeyValue = new Value[1];
        final Key[] nullValueKey = new Key[1];
        mapNullKeyValue.ifPresent(mp -> {
            nullKeyValue[0] = (Value) mp.get(null);
            for (Map.Entry<Object, Object> entry : mp.entrySet()) {
                if (Objects.isNull(entry.getValue())) {
                    nullValueKey[0] = (Key) entry.getKey();
                }
            }
        });
        System.out.println(null + ", " + nullKeyValue[0]);
        System.out.println(nullValueKey[0] + ", " + null);
    }

    public static void main(String[] args) {

        // listOption();
        // setOption();
        mapOption();
    }

    static class Key {
        static int seed = new Random(47).nextInt(100);

        int serial;

        public Key() {
            this.serial = seed++;
        }

        @Override
        public String toString() {
            return "Key{" + serial + "}";
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    static class Value {
        private Key key;

        public Value(Key key) {
            this.key = key;
        }

        public int getValue() {
            return key.serial;
        }

        @Override
        public String toString() {
            return Objects.isNull(key) ? null : "Value{" + getValue() + "}";
        }
    }
}
