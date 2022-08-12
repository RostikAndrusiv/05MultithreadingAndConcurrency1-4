package org.example.part2;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


//TODO 3rd thread with copy + benchmark
public class SynchronizedApp {

    private static final Object MUTEX = new Object();
    private static final List<Integer> collection = new LinkedList<>();

    private static class T1 extends Thread {
        public void run() {
            while (true) {
                synchronized (MUTEX) {
                    int num = ThreadLocalRandom.current().nextInt(1, 10);
                    collection.add(num);
                    System.out.println(currentThread().getName() + " added: " + num);
                }
            }
        }
    }

    private static class T2 extends Thread {
        public void run() {
            List<Integer> copy = Collections.emptyList();
            while (true) {
                synchronized (MUTEX) {
                    if (!collection.isEmpty()) {
                        copy = List.copyOf(collection);
                    }
                }
                copy.stream()
                        .reduce(Integer::sum)
                        .ifPresent(s -> System.out.println(Thread.currentThread().getName() + " sum is: " + s));
            }
        }
    }

    private static class T3 extends Thread {
        public void run() {
            List<Integer> copy = Collections.emptyList();
            while (true) {
                synchronized (MUTEX) {
                    if (!collection.isEmpty()) {
                        copy = List.copyOf(collection);
                        MUTEX.notifyAll();
                    }
                }
                double sumOfSquares = copy.stream()
                        .mapToDouble(num -> (double) num)
                        .reduce(0L, (acc, num) -> acc + num * num);
                System.out.println(Thread.currentThread().getName() + " sqrt of all nums is: " + Math.sqrt(sumOfSquares));
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new T1();
        Thread t2 = new T2();
        Thread t3 = new T3();
        t1.start();
        t2.start();
        t3.start();
    }
}
