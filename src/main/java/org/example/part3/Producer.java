package org.example.part3;

import java.util.Queue;

//TODO add lock.Condition
//TODO Refactor wait and read
public class Producer implements Runnable {

    private final Queue<String> queue;

    private int MAX_SIZE = 5;

    public Producer(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    @SuppressWarnings("java:S2189")
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("java:S2189")
    private void produce() throws InterruptedException {
        while (true) {
            synchronized (queue) {
                waitForConsumer();
                writeToQueue();
            }
        }
    }

    private void waitForConsumer() throws InterruptedException {
        synchronized (queue) {
            while (MAX_SIZE <= queue.size()) {
                System.out.println(Thread.currentThread().getName() + ": Queue is full, waiting for Consumer");
                queue.wait();
            }
        }
    }

    private void writeToQueue() {
        synchronized (queue) {
            String veryImportantMessage = "blablab" + System.currentTimeMillis();
            queue.add(veryImportantMessage);
            System.out.println(Thread.currentThread() + " added " + veryImportantMessage);
            queue.notifyAll();
        }
    }
}
