package oo.concurrency.component;


import java.util.concurrent.*;

/**
 * 展示了{@link FutureTask#run()}和{@link FutureTask#runAndReset()}的区别
 * <p>
 * 展示了cancel方法取消任务是通过中断任务实现的
 *
 * @author wangy
 * @version 1.0
 * @date 2020/10/21 / 14:51
 */
public class FutureTaskImpl<V> extends FutureTask<V> {
    private int runTime = 0;
    private boolean isDone = false;


    public FutureTaskImpl(Callable<V> callable) {
        super(callable);
    }

    public FutureTaskImpl(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            System.out.println("task is canceled");
            return;
        }
        isDone = true;
        runTime++;
    }

    @Override
    protected boolean runAndReset() {
        if (super.runAndReset()) {
            runTime++;
        } else {
            return false;
        }
        return true;
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("task running");
            for (; ; ) {
                if (Thread.interrupted()) {
                    break;
                }
            }
            // Thread is interrupted
            System.out.println("interrupted by cancel");
        }
    }

    static class Task2 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            return sum;
        }
    }

    void cancel() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        FutureTask<?> future = (FutureTask<?>) pool.submit(this);
        // 防止任务没有被执行就被cancel
        TimeUnit.MILLISECONDS.sleep(1);
        if (future.cancel(true)) {
            pool.shutdown();
        }

    }

    /**
     * 先执行{@link FutureTask#run()}再执行{@link #runAndReset()}
     * <p>
     * 任务不可执行
     */
    void resetAfterRun() {
        run();
        System.out.println(runAndReset()); // false
        System.out.println("runTime:" + runTime);
        System.out.println("isDone:" + isDone);
    }

    /**
     * 先执行{@link #runAndReset()}再执行{@link FutureTask#run()}
     * <p>
     * 任务可以再次执行
     * <p>
     * 对于有返回值的任务，执行{@link #runAndReset()}之后调用{@link FutureTask#get()}
     * 方法获取返回值会造成阻塞
     */
    void runAfterReset() throws ExecutionException, InterruptedException {
        for (; ; ) {
            runAndReset();
            if (runTime > 1) break;
        }
//        V v = get(); // blocked
        System.out.println("isDone: " + isDone); // false
        run();
        System.out.println("runTime: " + runTime);
        V v1 = get();
        System.out.println("result: " + v1);
        System.out.println("isDone: " + isDone); // true
    }

    public static void main(String[] args) throws InterruptedException {
        // 构造一个没有返回值的FutureTask
        FutureTaskImpl<?> ft = new FutureTaskImpl<>(new Task(), null);
        ft.cancel();
        FutureTaskImpl<?> ft2 = new FutureTaskImpl<>(new Task2());
//        ft2.runAfterReset();
//        ft2.resetAfterRun();
    }
}
