package org.example.part2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//TODO thread 2/3 dont stop properly:/
public class SynchronizedBlockWithCopy {

    private static final Object MUTEX = new Object();
    private static final List<Integer> collection = new LinkedList<>();
    private static boolean flag = true;

    private static class T1 extends Thread {

        public void run() {
            while (true) {
                synchronized (MUTEX) {
                    int num = ThreadLocalRandom.current().nextInt(1, 10);
                    collection.add(num);
                    System.out.println(currentThread().getName() + " added: " + num);
                    MUTEX.notifyAll();
                    try {
                        MUTEX.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                flag = false;
            }
        }
    }

    private static class T2 extends Thread {
        public void run() {
            List<Integer> copy;
            while (true) {
                synchronized (MUTEX) {
                    copy = getListCopy();
                    MUTEX.notifyAll();
                    try {
                        MUTEX.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
            List<Integer> copy;
            while (true) {
                synchronized (MUTEX) {
                    copy = getListCopy();
                    MUTEX.notifyAll();
                    try {
                        MUTEX.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                double sumOfSquares = copy.stream()
                        .mapToDouble(num -> (double)num)
                        .reduce(0L, (acc, num) -> acc+Math.pow(num, 2));
                System.out.println(Thread.currentThread().getName() + " sqrt of all nums is: " + Math.sqrt(sumOfSquares));
                MUTEX.notifyAll();
            }
        }
    }

    private static List<Integer> getListCopy() {
        if (!collection.isEmpty()) {
            return List.copyOf(collection);
        }
        return Collections.emptyList();
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
