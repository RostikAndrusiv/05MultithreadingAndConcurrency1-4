package org.example.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Part2 {

    private static volatile boolean flag = true;

    private static final Logger logger = LoggerFactory.getLogger(Part2.class.getName());

    private static final Map<Integer, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

    private static int iterations;

    private static int DEFAULT_ITERATIONS = 10;

    public static void main(String[] args) {
        if (args.length == 0) {
            iterations = DEFAULT_ITERATIONS;
        } else iterations = Integer.parseInt(args[0]);

        Thread thread2 = new Thread(() -> {
//            while (flag) {
            while (!Thread.interrupted()) {
                try {
                    countSum();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
            thread2.interrupt();
//            flag=false;
        });

        thread1.start();
        thread2.start();
    }

    public static void addToMap() {
        int num = ThreadLocalRandom.current().nextInt(1, 10);
        syncMap.put(num, null);
        logger.info(Thread.currentThread().getName() + ": added " + num);
    }

    public static void countSum() {
        synchronized (syncMap) {
            syncMap.keySet().stream()
                    .reduce(Integer::sum)
                    .ifPresent(sum -> logger.info(Thread.currentThread().getName() + ": sum is: " + sum));
        }
    }
}
