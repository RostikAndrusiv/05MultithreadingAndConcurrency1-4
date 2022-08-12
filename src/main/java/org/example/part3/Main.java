package org.example.part3;

import java.util.PriorityQueue;
import java.util.Queue;

//TODO add lock.Condition
public class Main {

    //TODO ask about sonarlint .interrupt()
    private static Queue<String> queue = new PriorityQueue<>();

    public static void main(String[] args) {

        Thread t1 = new Thread(new Producer(queue));
        Thread t2 = new Thread(new Consumer(queue));
        Thread t3 = new Thread(new Consumer(queue));
        t1.start();
        t2.start();
        t3.start();
    }
}
