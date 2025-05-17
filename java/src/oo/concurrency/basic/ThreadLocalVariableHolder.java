package oo.concurrency.basic;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 线程本地变量的简单使用
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/17 / 15:14
 */
public class ThreadLocalVariableHolder {
    // Java 8 提供的方法
    private static final ThreadLocal<Integer> VALUE
                 = ThreadLocal.withInitial(() -> {
                        Random r = new Random();
                        return r.nextInt(10);
                });

    /** 每个线程独享一个本地变量，互不影响 */
    static class Task implements Runnable {

        static void increment() {
            VALUE.set(VALUE.get() + 1);
        }

        static Integer getValue() {
            return VALUE.get();
        }

        @Override
        public String toString() {
            return Thread.currentThread() + ": " + getValue();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                increment();
                System.out.println(this);
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            new Thread(new Task()).start();
        }
        TimeUnit.MILLISECONDS.sleep(20);
        System.exit(0);
    }
}

/// :~
// Thread[Thread-1,5,main]: 2
// Thread[Thread-0,5,main]: 5
// Thread[Thread-1,5,main]: 3
// Thread[Thread-0,5,main]: 6
// Thread[Thread-1,5,main]: 4
// Thread[Thread-0,5,main]: 7
// Thread[Thread-1,5,main]: 5
/// :~