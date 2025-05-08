package oo.innerclass;


interface Selector{
    boolean end();
    Object current();
    void next();
}

class SequenceL {
    private Object[] items;
    private int next = 0;

    public SequenceL(int size) {
        items  = new Object[size];
    }
    public void add(Object o){
        if (next < items.length){
            items[next++] = o;
        }
    }
    // private inner class
    private class SequenceSelector implements Selector{
        private int i = 0;

        @Override
        public boolean end() { return  i == items.length; }

        @Override
        public Object current() { return items[i]; }

        @Override
        public void next() { if (i < items.length)  i++;  }
    }

    public SequenceSelector selector(){ return new SequenceSelector(); }


}

public class PSequence {
    public static void main(String[] args) {
        SequenceL s = new SequenceL(10);
        for (int i = 0; i <10 ; i++) {
            s.add(Integer.toString(i));
        }
        Selector selector = s.selector();
        // 私有内部类在此处不可见
        while (!selector.end()){
            System.out.printf("%s, ", selector.current());
            selector.next();
        }
    }
}
