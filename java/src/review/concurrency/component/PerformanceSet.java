package review.concurrency.component;

import common.helper.CountingIntegerList;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * {@link CopyOnWriteArraySet} 读性能良好，但写性能不如{@link Collections#synchronizedSet(Set)}
 * <p>
 * {@link ConcurrentSkipListSet}的读写性能最好
 *
 * @author wangy
 * @version 1.0
 * @date 2020/11/23 / 11:20
 */
public class PerformanceSet {

    abstract class SetTest extends ConcurrentContainerTester<Set<Integer>> {

        SetTest(String testId, int nReaders, int nWriters) {
            super(testId, nReaders, nWriters);
        }


        class Writer extends TestTask {

            @Override
            void test() {
                for (long i = 0; i < testCycles; i++) {
                    for (int index = 0; index < containerSize; index++) {
                        testContainer.add(writeData[index]);
                    }
                }
            }

            @Override
            synchronized void putResults() {
                writeTime += duration;
            }
        }

        class Reader extends TestTask {

            private long result;

            @Override
            void test() {
                // synchronized set generated by Collections.synchronizedSet()
                // is quite necessary for avoiding concurrentModificationException
                synchronized (testContainer) {
                    Iterator<Integer> iterator = testContainer.iterator();
                    for (long i = 0; i < testCycles; i++) {
                        while (iterator.hasNext()) {
                            result += iterator.next();
                        }
                    }
                }
            }

            @Override
            synchronized void putResults() {
                readTime += duration;
                readResult += result;
            }
        }

        @Override
        void startReadersAndWriters() {
            for (int i = 0; i < nReaders; i++) {
                exec.execute(new Reader());
            }
            for (int i = 0; i < nWriters; i++) {
                exec.execute(new Writer());
            }
        }
    }

    class SynchronizedSetTest extends SetTest {

        SynchronizedSetTest(int nReaders, int nWriters) {
            super("sync Set", nReaders, nWriters);
        }

        @Override
        Set<Integer> containerInitializer() {
            return Collections.synchronizedSet(
                new HashSet<>(
                    new CountingIntegerList(containerSize)));
        }

    }

    class CopyOnWriteArraySetTest extends SetTest {

        CopyOnWriteArraySetTest(int nReaders, int nWriters) {
            super("CopyOnWriteArraySet", nReaders, nWriters);
        }

        @Override
        Set<Integer> containerInitializer() {
            return new CopyOnWriteArraySet<>(
                new CountingIntegerList(containerSize));
        }
    }

    class ConcurrentSkipListSetTest extends SetTest {

        ConcurrentSkipListSetTest(int nReaders, int nWriters) {
            super("ConcurrentSkipListSet", nReaders, nWriters);
        }

        @Override
        Set<Integer> containerInitializer() {
            // elements add to ConcurrentSkipListSet must be comparable
            return new ConcurrentSkipListSet<>(
                new CountingIntegerList(containerSize));
        }
    }

    void test(String[] args) {
        ConcurrentContainerTester.initMain(args);
        new CopyOnWriteArraySetTest(10, 0);
        new CopyOnWriteArraySetTest(8, 2);
        new CopyOnWriteArraySetTest(5, 5);
        new SynchronizedSetTest(10, 0);
        new SynchronizedSetTest(8, 2);
        new SynchronizedSetTest(5, 5);
        new ConcurrentSkipListSetTest(10, 0);
        new ConcurrentSkipListSetTest(8, 2);
        new ConcurrentSkipListSetTest(5, 5);
        ConcurrentContainerTester.exec.shutdown();
    }

    public static void main(String[] args) {
        PerformanceSet ps = new PerformanceSet();
        ps.test(new String[]{"10", "1000", "1000"});
    }
}
