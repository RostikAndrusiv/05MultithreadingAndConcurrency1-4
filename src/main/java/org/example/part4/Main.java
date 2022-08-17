package org.example.part4;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;


//TODO ask about iterator inside sync block
// add unit tests
public class Main {
//    public static final Queue<Object> objects = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {

        BlockingObjectPoolSemaphore pool = new BlockingObjectPoolSemaphore(5);


        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
//                    Optional.ofNullable(pool.get()).ifPresent(obj -> {
//                        objects.add(obj);
//                        System.out.println(Thread.currentThread().getName() + " get: " + obj);
//                });
                    pool.get();
                    sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                try {
//                    if (objects.isEmpty()) {
//                        continue;
//                    }
//                    Object o = objects.poll();
                    // uncomment to test take with full pool
//                   Object o = objects.peek();

                    pool.take(new Object());
  //                  System.out.println(Thread.currentThread().getName() + " take " + o);
                    sleep(100);
                }catch (Exception e) {
                    e.printStackTrace();
                }}}
        );

//        thread1.start();
//        Thread.sleep(2000);
        thread2.start();
        sleep(500);
        System.out.println(pool.getPool().size());
        thread2.interrupt();
        sleep(100);
        System.out.println(pool.getPool().size());
        System.out.println(pool.getPool().toString());
    }
}
