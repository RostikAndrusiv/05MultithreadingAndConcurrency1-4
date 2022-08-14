package org.example.part4;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool that block when it has not any items or it full
 */
//TODO Semaphore
public class BlockingObjectPoolConditionApproach {

    private final Set<Object> pool = new HashSet<>();

    final Lock lock = new ReentrantLock();

    final Condition empty = lock.newCondition();

    final Condition full = lock.newCondition();

    private final int size;

    private int availableObjects;


    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPoolConditionApproach(int size) {
        this.size = size;
        availableObjects = size;
        init();
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            pool.add(new Object());
        }
    }

    /**
     * Gets object from pool or blocks if pool is empty * * @return object from pool
     */
    public Object get() {
        lock.lock();
        try {
            while (availableObjects == 0) {
                System.err.println("No objects available");
                try {
                    empty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            var instance = pool.iterator().next();
            pool.remove(instance);
            availableObjects--;
            full.signalAll();
            return instance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) {
        lock.lock();
        try {
            while (availableObjects == size) {
                System.err.println("Pool is full");
                try {
                    full.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            pool.add(object);
            availableObjects++;
            empty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
