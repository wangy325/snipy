package oo.innerclass;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 17:06
 */

// 内部类实现的接口
interface Selector {
    boolean end();

    Object current();

    void next();
}

public class Sequence {
    private Object[] items;
    private int next = 0;

    public Sequence(int size) {
        items = new Object[size];
    }

    public void add(Object o) {
        if (next < items.length) {
            items[next++] = o;
        }
    }

    // 内部类实现自接口
    // 这是一个private修饰的内部类
    private class SequenceSelector implements Selector {
        private int i = 0;

        // 内部类直接访问了外围类的私有域
        @Override
        public boolean end() {
            return i == items.length;
        }

        @Override
        public Object current() {
            return items[i];
        }

        @Override
        public void next() {
            if (i < items.length)
                i++;
        }
    }

    // 获取内部类实例
    public Selector selector() {
        return new SequenceSelector();
    }

    public static void main(String[] args) {
        Sequence s = new Sequence(10);
        for (int i = 0; i < 10; i++) {
            s.add(Integer.toString(i));
        }
        // Selector selector = s.selector();
        // equals to
        // 私有内部类在外部类可见
        Sequence.SequenceSelector selector = (SequenceSelector) s.selector();
        while (!selector.end()) {
            System.out.printf("%s, ", selector.current());
            selector.next();
        }
    }
}
