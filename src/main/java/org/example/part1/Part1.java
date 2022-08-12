package org.example.part1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Part1 {

    private static boolean flag = true;

    private static final Logger logger = LoggerFactory.getLogger(Part1.class.getName());

    private static final Map<Integer, Integer> map = new HashMap<>();

    private static int iterations;

    private static int DEFAULT_ITERATIONS = 10000;

    public static void main(String[] args) {
        if (args.length == 0) {
            iterations = DEFAULT_ITERATIONS;
        } else iterations = Integer.parseInt(args[0]);

        Thread thread1 = new Thread(() -> {
            int counter = 0;
            while (flag && iterations > counter) {
                try {
                    addToMap();
                    System.out.println("counter: " + counter++);
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = false;
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
                    flag = false;
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    private static void addToMap() {
        int num = ThreadLocalRandom.current().nextInt(1, 10);
        map.put(num, null);
        logger.info(Thread.currentThread().getName() + ": added " + num);
    }

    private static void countSum() {
        map.keySet().stream()
                .reduce(Integer::sum)
                .ifPresent(sum -> logger.info(Thread.currentThread().getName() + ": sum is: " + sum));
    }
}
