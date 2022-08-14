package org.example.part4;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;


/**
 * Pool that block when it has not any items or it full
 */
//TODO Semaphore
public class BlockingObjectPoolSemaphore {

    private final Set<Object> pool = new HashSet<>();

    Semaphore semaphore;

    private final int size;

    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPoolSemaphore(int size) {
        this.size = size;
        init();
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            pool.add(new Object());
        }
        semaphore = new Semaphore(size);
    }

    /**
     * Gets object from pool or blocks if pool is empty * * @return object from pool
     */
    public Object get() {
        try {
            semaphore.acquire();
            var instance = pool.iterator().next();
            pool.remove(instance);
            return instance;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) {
            pool.add(object);
            semaphore.release();
    }
}
