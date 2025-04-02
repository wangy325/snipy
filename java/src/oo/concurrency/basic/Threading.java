package oo.concurrency.basic;

import java.util.concurrent.TimeUnit;

/**
 *Author: wangy325
 *Date: 2024-08-07 20:00:13
 *Description: 线程争用
**/
public class Threading {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            MyThread t = new MyThread();
            t.start();
        }
        // uncertain output~
        System.out.printf("shardInt: %d\n", MyThread.shardInt);
    }
}

class MyThread extends Thread {
    public static int shardInt = 0;
    @Override
    public void run() {
        shardInt++;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}