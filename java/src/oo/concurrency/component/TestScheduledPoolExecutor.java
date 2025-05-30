package oo.concurrency.component;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/11 / 01:28
 */
public class TestScheduledPoolExecutor {

    private AtomicInteger sequence = new AtomicInteger(0);
    private ScheduledThreadPoolExecutor service;

    public TestScheduledPoolExecutor(int corePoolSize) {
        this.service = new ScheduledThreadPoolExecutor(corePoolSize);
    }

    private void s() {
        System.out.println(Thread.currentThread() + " " + sequence.incrementAndGet());
    }

    private void c() {
        System.out.println(Thread.currentThread() + " c running");
        while (true) {
            // never finish loop unless interrupted
            if (Thread.interrupted()) {
                break;
            }
        }
        System.out.println(Thread.currentThread() + "c interrupted");
    }

    void basicTest() throws InterruptedException {
//        service.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        service.schedule(this::s, 2, TimeUnit.SECONDS);
        service.schedule(this::c, 1, TimeUnit.SECONDS);

        service.shutdown();
//        service.shutdownNow();
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }

    void howManyThreads() {

        // 如果使用默认策略取消一个任务，那么非核心线程就不会被销毁
        /*
        service.schedule(this::s, 2, TimeUnit.SECONDS);
        BlockingQueue<Runnable> queue = service.getQueue();
        FutureTask<Void> task = (FutureTask<Void>) queue.peek();
        task.cancel(false);
         */

        for (; ; ) {
            ScheduledFuture<?> schedule = service.schedule(this::s, 0, TimeUnit.MILLISECONDS);
//            TimeUnit.MILLISECONDS.sleep(5);
            for (; ; ) {
                if (schedule.isDone())
                    break;
            }
            if (sequence.get() >= 10) {
                schedule.cancel(false);
                break;
            }
        }
        System.out.println("largest pool size: " + service.getLargestPoolSize());
        service.shutdown();
    }

    void cancelSchedule() throws InterruptedException {
        service.setRemoveOnCancelPolicy(true);
        // task to cancelled
        service.schedule(this::s, 10, TimeUnit.SECONDS);
        BlockingQueue<Runnable> queue = service.getQueue();
        Runnable task = queue.peek();
        if (task instanceof RunnableScheduledFuture) {
            ((FutureTask<?>) task).cancel(false);
        }

        service.schedule(this::s, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(2);
        // should be 1
        System.out.println("queue size: " + queue.size());

        service.shutdown();
        // removed by onShutdown hook method
        System.out.println("queue size: " + queue.size());
    }

    void shutdownPolicy() throws InterruptedException {
        // 如果任务在shutdown()之后仍在delay，那么将值设置为false可以取消任务的执行
        // 其默认值为true
        service.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        service.schedule(this::s, 1, TimeUnit.MILLISECONDS);

        // 如果是周期执行的任务，将此值设置为true可以在调用shutdown()之后让其继续执行，否则结束执行
        // 其默认值为false
        service.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        service.scheduleWithFixedDelay(this::s, 2, 1, TimeUnit.SECONDS);

        service.shutdown();
        TimeUnit.SECONDS.sleep(10);
        // shutdownNow interrupt all tasks
        service.shutdownNow();
        // could be true or false
        System.out.println(service.isTerminated());
    }

    void shutdownNow() throws InterruptedException {
        service.schedule(this::c, 0, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1000);
//        service.shutdown();
        service.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
        TestScheduledPoolExecutor ts = new TestScheduledPoolExecutor(0);
//        ts.basicTest();
//        ts.howManyThreads();
//        ts.cancelSchedule();
        ts.shutdownPolicy();
//        ts.shutdownNow();s
    }

}
