package oo.concurrency.sync;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 在监视器上等待<br>
 * 假设播放之前必须录音，播放之后必须等待再次录音才能继续播放
 * <p>
 * 在本例中，线程没有"合适地"被终结，而是使用了
 * <code>
 * System.exit(0);
 * </code>
 * 来终止程序的运行
 *
 * @author wangy
 * @version 1.0
 * @date 2020/5/23 / 14:20
 */
public class WaitOnCondition {

    /**
     * 2个任务，2个线程
     */
    private volatile boolean tracked = false;

    synchronized void playTrack() throws InterruptedException {
        if (!tracked) {
            wait();
        }
        System.out.println("play ");
        tracked = false;
    }

    synchronized void recordTrack() {
        if (tracked) {
            return;
        }
        System.out.println("record ");
        tracked = true;
        // 最好不要使用notify,除非你明确地知道期待的线程一定被唤醒
        notifyAll();
    }

    class Play implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    playTrack();
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Record implements Runnable {
        @Override
        public void run() {
            while (true) {
                recordTrack();
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitOnCondition tp = new WaitOnCondition();
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(tp.new Play());
        pool.submit((tp.new Record()));

        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }
}
