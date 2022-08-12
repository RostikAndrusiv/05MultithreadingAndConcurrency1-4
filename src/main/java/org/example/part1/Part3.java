package org.example.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Part3 {
    private static boolean flag = true;

    private static final Logger logger = LoggerFactory.getLogger(Part3.class.getName());

    private static ConcurrentMap<Integer, Integer> map = new ConcurrentHashMap<>();

    private static int iterations;

    private static int DEFAULT_ITERATIONS = 10000;

    public static void main(String[] args) {
        if (args.length == 0) {
            iterations = DEFAULT_ITERATIONS;
        } else iterations = Integer.parseInt(args[0]);

        Thread thread1 = new Thread(() -> {
            int counter = 0;
            while (iterations > counter) {
                try {
                    addToMap();
                    System.out.println("counter: " + counter++);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            flag = false;
        });

        Thread thread2 = new Thread(() -> {
            while (flag) {
                try {
                    countSum();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    private static void addToMap() {
        int num = ThreadLocalRandom.current().nextInt(1, 10);
        map.put(num, num);
        logger.info(Thread.currentThread().getName() + ": added " + num);
    }

    private static void countSum() {
        map.keySet().stream()
                .reduce(Integer::sum)
                .ifPresent(sum -> logger.info(Thread.currentThread().getName() + ": sum is: " + sum));
    }
}
