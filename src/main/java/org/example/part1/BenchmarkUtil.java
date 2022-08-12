package org.example.part1;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */

//TODO read about CAS alghorithm
//TODO Run in diff versions, add Benchm
public class BenchmarkUtil {
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"100"})
        public String iterations;
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    @Threads(1)
    public void part1Bench(BenchmarkState state) {
        String[] args = {state.iterations};
        Part1.main(args);
    }

//    @Benchmark
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    @BenchmarkMode(Mode.AverageTime)
//    @Fork(value = 1)
//    @Warmup(iterations = 1)
//    @Measurement(iterations = 1)
//    public void part2Bench(BenchmarkState state) {
//        String[] args = {state.iterations};
//        Part2.main(args);
//    }
//
//    @Benchmark
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    @BenchmarkMode(Mode.AverageTime)
//    @Fork(value = 1)
//    @Warmup(iterations = 1)
//    @Measurement(iterations = 1)
//    public void part3Bench(BenchmarkState state) {
//        String[] args = {state.iterations};
//        Part3.main(args);
//    }
//
//    @Benchmark
//    @OutputTimeUnit(TimeUnit.NANOSECONDS)
//    @BenchmarkMode(Mode.AverageTime)
//    @Fork(value = 1)
//    @Warmup(iterations = 1)
//    @Measurement(iterations = 1)
//    @Threads(1)
//    public void part4Bench(BenchmarkState state) throws InterruptedException {
//        String[] args = {state.iterations};
//        Part4.main(args);
//    }
}
