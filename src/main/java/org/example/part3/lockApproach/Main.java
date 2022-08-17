package org.example.part3.lockApproach;

import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    //TODO ask about sonarlint .interrupt()
    private static CustomQueue queue = new CustomQueue();

    public static void main(String[] args) {

        Thread t1 = new Thread(new Producer(queue));
        Thread t2 = new Thread(new Consumer(queue));
        Thread t3 = new Thread(new Consumer(queue));
        t1.start();
        t2.start();
        t3.start();
    }
}
