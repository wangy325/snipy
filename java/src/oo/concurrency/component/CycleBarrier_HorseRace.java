package oo.concurrency.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/11/16 / 18:16
 */
public class CycleBarrier_HorseRace {

    static class Horse implements Runnable {
        private static int counter = 0;
        private final int id = counter++;
        private volatile int strides = 0;
        private static Random rand = new Random(47);
        private static CyclicBarrier barrier;

        public Horse(CyclicBarrier b) {
            barrier = b;
        }

        public int getStrides() {
            return strides;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    strides += rand.nextInt(3); // Produces 0, 1 or 2
                    barrier.await();
                }
            } catch (InterruptedException e) {
                // A legitimate way to exit
            } catch (BrokenBarrierException e) {
                // This one we want to know about
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "Horse " + id + " ";
        }

        public String tracks() {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < getStrides(); i++) {
                s.append("*");
            }
            s.append(id);
            return s.toString();
        }
    }

    static final int FINISH_LINE = 20;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;

    /** 这是一构造器 */
    public CycleBarrier_HorseRace(int nHorses, final int pause) {
        barrier = new CyclicBarrier(nHorses, () -> {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < FINISH_LINE; i++) {
                s.append("="); // The fence on the racetrack
            }
            System.out.println(s);
            for (Horse horse : horses) {
                System.out.println(horse.tracks());
            }
            for (Horse horse : horses) {
                if (horse.getStrides() >= FINISH_LINE) {
                    System.out.println(horse + "won!");
                    exec.shutdownNow();
                    return;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(pause);
            } catch (InterruptedException e) {
                System.out.println("barrier-action sleep interrupted");
            }
        });
        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 50;
        if (args.length > 0) { // Optional argument
            int n = new Integer(args[0]);
            nHorses = n > 0 ? n : nHorses;
        }
        if (args.length > 1) { // Optional argument
            int p = new Integer(args[1]);
            pause = p > -1 ? p : pause;
        }
        new CycleBarrier_HorseRace(nHorses, pause);
    }
}
