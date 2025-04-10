package oo.concurrency.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/5/13 / 17:04
 */
public class LockingAttempt {
    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        LockingAttempt al = new LockingAttempt();
        al.untimed();
        al.timed();
        // 这是一个匿名类 使用后台线程占用锁
        new Thread(){
            {setDaemon(true);}

            @Override
            public void run() {
                al.lock.lock();
                System.out.println("locked");
            }
        }.start();

        // 使主线程让出cpu时间
        Thread.sleep(100);
        al.untimed();
        al.timed();

    }

    void untimed() {
        // 尝试获取锁并且立即返回
        boolean b = lock.tryLock();
        try {
            System.out.println("tryLock(): " + b);
        } finally {
            if (b) lock.unlock();
        }
    }

    void timed() {
        boolean b = false;
        try {
            b = lock.tryLock(2, TimeUnit.SECONDS);
            System.out.println("tryLock(2, TimeUnit.SECONDS): " + b);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        } finally {
            if (b) lock.unlock();
        }
    }
}
