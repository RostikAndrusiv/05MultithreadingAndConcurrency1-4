package org.example.part4;

import java.util.HashSet;
import java.util.Set;

/**
 * Pool that block when it has not any items or it full
 */
//TODO Semaphore
public class BlockingObjectPool {

    private final Set<Object> pool = new HashSet<>();

    private final Set<Object> inUse = new HashSet<>();

    private final int size;

    private static final Object MUTEX = new Object();

    private int availableObjects;

    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPool(int size) {
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
        synchronized (MUTEX) {
            if (availableObjects == 0) {
                System.err.println("No objects available");
                try {
                    MUTEX.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            var instance = pool.iterator().next();
            pool.remove(instance);
            inUse.add(instance);
            availableObjects--;
            MUTEX.notifyAll();
            return instance;
        }
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) {
        synchronized (MUTEX) {
            if (availableObjects == size) {
                try {
                    MUTEX.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            inUse.remove(object);
            pool.add(object);
            availableObjects++;
            MUTEX.notifyAll();
        }
    }
}