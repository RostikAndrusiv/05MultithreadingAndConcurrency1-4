package org.example.part3;

import java.util.Queue;

//TODO add lock.Condition
//TODO Refactor wait and read
public class Consumer implements Runnable {

    private final Queue<String> queue;

    public Consumer(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    @SuppressWarnings("java:S2189")
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void consume() throws InterruptedException {
        while (true) {
            synchronized (queue) {
                waitForProducer();
                readTopic();
            }
        }
    }

    private void waitForProducer() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + ": Queue is empty, waiting for Producer");
                queue.wait();
            }
        }
    }

    private void readTopic() {
        synchronized (queue) {
            String message = queue.poll();
            System.out.println(Thread.currentThread().getName() + " consumed:" + message);
            queue.notifyAll();
        }
    }
}
