package org.example.part3.lockApproach;

import lombok.SneakyThrows;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer implements Runnable {
    private final Queue<String> queue;

    private Lock lock;

    private final Condition notFullCondition;

    private final Condition notEmptyCondition;

    private int MAX_SIZE = 5;

    public Producer(CustomQueue queue) {
        this.queue = queue.getQueue();
        this.lock = queue.getLock();
        this.notEmptyCondition = queue.getNotEmptyCondition();
        this.notFullCondition = queue.getNotFullCondition();
    }

    @Override
    @SuppressWarnings("java:S2189")
    public void run() {
        try {
            while (true) {
                lock.lock();
                try{
                    waitForConsumer();
                    writeToQueue();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void waitForConsumer() throws InterruptedException {
        while (MAX_SIZE <= queue.size()) {
            System.out.println(Thread.currentThread().getName() + ": Queue is full, waiting for Consumer");
            notFullCondition.await();
        }
    }

    @SneakyThrows
    private void writeToQueue() {
        String veryImportantMessage = "blablab" + System.currentTimeMillis();
        queue.add(veryImportantMessage);
        System.out.println(Thread.currentThread() + " added " + veryImportantMessage);
        notEmptyCondition.signalAll();
    }
}
