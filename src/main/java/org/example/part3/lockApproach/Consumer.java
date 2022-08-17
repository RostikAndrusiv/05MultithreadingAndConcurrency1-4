package org.example.part3.lockApproach;

import lombok.SneakyThrows;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {
    private final Queue<String> queue;

    private Lock lock;

    private final Condition notFullCondition;

    private final Condition notEmptyCondition;

    private int MAX_SIZE = 5;

    public Consumer(CustomQueue queue) {
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
                try {
                    waitForProducer();
                    readTopic();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void waitForProducer() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + ": Queue is empty, waiting for Producer");
            notEmptyCondition.await();
        }
    }

    private void readTopic() {
        String message = queue.poll();
        System.out.println(Thread.currentThread().getName() + " consumed:" + message);
        notFullCondition.signalAll();
    }
}
