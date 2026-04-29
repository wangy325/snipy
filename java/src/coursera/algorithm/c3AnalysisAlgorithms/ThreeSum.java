package coursera.algorithm.c3AnalysisAlgorithms;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ThreeSum {

    public static int count(int[] a) {
        int N = a.length;
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (int k = j + 1; k < N; k++) {
                    if (a[i] + a[j] + a[k] == 0)
                        count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {

        // int[] a = StdIn.readAllInts();
        int[] a = Arrays.stream(
                Files.readString(Path.of(args[0])).trim().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
        StdOut.println("array length: " + a.length);
        // using StopWatch to time a program
        Stopwatch stopwatch = new Stopwatch();
        StdOut.println(count(a));
        double time = stopwatch.elapsedTime();
        StdOut.println("time: " + time);
    }
}