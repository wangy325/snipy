package common.helper;

import java.util.Random;

/**
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/21 / 21:50
 */
public class RandomGenerator {

    private static Random r = new Random(47);

    public static class
    Boolean implements Generator<java.lang.Boolean> {
        @Override
        public java.lang.Boolean next() {
            return r.nextBoolean();
        }
    }

    public static class
    Byte implements Generator<java.lang.Byte> {
        @Override
        public java.lang.Byte next() {
            return (byte) r.nextInt();
        }
    }

    public static class
    Character implements Generator<java.lang.Character> {
        @Override
        public java.lang.Character next() {
            return CountingGenerator.chars[
                r.nextInt(CountingGenerator.chars.length)];
        }
    }

    public static class
    String extends CountingGenerator.String {
        // Plug in the random Character generator:
        {
            cg = new Character();
        } // Instance initializer

        public String() {
        }

        public String(int length) {
            super(length);
        }
    }

    public static class
    Short implements Generator<java.lang.Short> {
        @Override
        public java.lang.Short next() {
            return (short) r.nextInt();
        }
    }

    public static class
    Integer implements Generator<java.lang.Integer> {
        private int mod = 10000;

        public Integer() {
        }

        public Integer(int modulo) {
            mod = modulo;
        }

        @Override
        public java.lang.Integer next() {
            return r.nextInt(mod);
        }
    }

    public static class
    Long implements Generator<java.lang.Long> {
        private int mod = 10000;

        public Long() {
        }

        public Long(int modulo) {
            mod = modulo;
        }

        @Override
        public java.lang.Long next() {
            return new java.lang.Long(r.nextInt(mod));
        }
    }

    public static class
    Float implements Generator<java.lang.Float> {
        @Override
        public java.lang.Float next() {
            // Trim all but the first two decimal places:
            int trimmed = Math.round(r.nextFloat() * 100);
            return ((float) trimmed) / 100;
        }
    }

    public static class
    Double implements Generator<java.lang.Double> {
        @Override
        public java.lang.Double next() {
            long trimmed = Math.round(r.nextDouble() * 100);
            return ((double) trimmed) / 100;
        }
    }
}
