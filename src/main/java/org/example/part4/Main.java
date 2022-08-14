package org.example.part4;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


//TODO ask about iterator inside sync block
public class Main {
    public static final Queue<Object> objects = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {

        BlockingObjectPoolSemaphore pool = new BlockingObjectPoolSemaphore(5);

        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Optional.ofNullable(pool.get()).ifPresent(obj -> {
                        objects.add(obj);
                        System.out.println(Thread.currentThread().getName() + " get: " + obj);
                    });
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                try {
                    if (objects.isEmpty()) {
                        continue;
                    }
                   Object o = objects.poll();
                    // uncomment to test take with full pool
//                   Object o = objects.peek();
                    pool.take(o);
                    System.out.println(Thread.currentThread().getName() + " take " + o);
                    Thread.sleep(100);
                }catch (Exception e) {
                    e.printStackTrace();
                }}}
        );

        thread1.start();
        Thread.sleep(2000);
        thread2.start();
    }
}
