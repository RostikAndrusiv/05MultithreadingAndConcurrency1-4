package org.example.part2;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExecutorServiceApp {

    private static final Lock lock = new ReentrantLock(true);
    private static final List<Integer> collection = new LinkedList<>();

    private static void addNum() {
        while (true) {
            lock.lock();
            try {
                int num = ThreadLocalRandom.current().nextInt(1, 10);
                collection.add(num);
                System.out.println(Thread.currentThread().getName() + " added: " + num);
            } finally {
                lock.unlock();
            }
        }
    }

    @SuppressWarnings("java:S2189")
    private static void countSum() {
        while (true) {
            List<Integer> copy = getListCopy();
            copy.stream()
                    .reduce(Integer::sum)
                    .ifPresent(s -> System.out.println(Thread.currentThread().getName() + " sum is: " + s));
        }
    }

    @SuppressWarnings("java:S2189")
    private static void countSqrt() {
        while (true) {
            List<Integer> copy = getListCopy();
            double sumOfSquares = copy.stream()
                    .mapToDouble(num -> (double) num)
                    .reduce(0L, (acc, num) -> acc + num * num);
            System.out.println(Thread.currentThread().getName() + " sqrt of all nums is: " + Math.sqrt(sumOfSquares));
        }
    }

    private static List<Integer> getListCopy() {
        lock.lock();
        try {
            if (!collection.isEmpty()) {
                return List.copyOf(collection);
            }
        } finally {
            lock.unlock();
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        List<Runnable> taskList = new ArrayList<>();
        taskList.add(ExecutorServiceApp::addNum);
        taskList.add(ExecutorServiceApp::countSum);
        taskList.add(ExecutorServiceApp::countSqrt);
        ExecutorService service = Executors.newFixedThreadPool(3);
        taskList.forEach(service::execute);
    }
}
