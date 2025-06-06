package oo.concurrency.component;



import java.util.concurrent.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/5 / 10:40
 */
public class TestThreadPoolExecutor {


    void poolSize() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2,
            0, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(1),
            new ThreadPoolExecutor.AbortPolicy());
        executor.prestartCoreThread();
        executor.submit(() -> {
            System.out.println(Thread.currentThread() + "executed");
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread() + "executing");
            while (true) {
                //
            }
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread() + "executed");
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread() + "executed..");
        });
        executor.awaitTermination(10, TimeUnit.MILLISECONDS);
        // 获取当前真在执行任务的活动线程数
        System.out.println(executor.getActiveCount());
        // 获取当前线程池中的线程数
        System.out.println(executor.getPoolSize());
        System.out.println(executor.getQueue().size());
    }

    void initPoolWithNonEmptyQueue() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(2) {{
            add(() -> {
                System.out.println(Thread.currentThread() + "1st task done");
            });
            add(() -> {
                System.out.println(Thread.currentThread() + "2nd task done");
            });
        }};

        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.MILLISECONDS, queue, abortPolicy);

//        poolExecutor.prestartCoreThread();
        poolExecutor.prestartAllCoreThreads();
        poolExecutor.shutdown();

    }

    void cachedPool() {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        service.setKeepAliveTime(5, TimeUnit.SECONDS);
        service.submit(() -> {
            System.out.println("task done");
        });
        while (true) {
            if (Thread.activeCount() > 2) {
                continue;
            }
            break;
        }
        // 线程池5s之后结束运行了，池中没有线程，队列也为空
        // 但是线程池的状态还是RUNNING
        System.out.println(service.getPoolSize());
        System.out.println(service.getActiveCount());
        System.out.println(service.getQueue().isEmpty());
        System.out.println(service.isShutdown()); // false
        System.out.println(service.isTerminated()); // false

    }

    public static void main(String[] args) throws InterruptedException {
        TestThreadPoolExecutor te = new TestThreadPoolExecutor();

//        te.poolSize();
//        te.initPoolWithNonEmptyQueue();
        te.cachedPool();
    }
}
