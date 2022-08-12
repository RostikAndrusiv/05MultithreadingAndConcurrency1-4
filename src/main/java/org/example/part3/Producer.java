package org.example.part3;

import java.util.Queue;

//TODO add lock.Condition
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
            while (true) {
                synchronized (queue) {
                    waitForConsumer();
                    writeToQueue();
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
            queue.wait();
        }
    }

    private void writeToQueue() {
        String veryImportantMessage = "blablab" + System.currentTimeMillis();
        queue.add(veryImportantMessage);
        System.out.println(Thread.currentThread() + " added " + veryImportantMessage);
        queue.notifyAll();
    }
}
