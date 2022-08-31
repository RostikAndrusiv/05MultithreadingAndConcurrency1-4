package org.example.part4;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;


/**
 * Pool that block when it has not any items or it full
 */
//TODO Semaphore
@Data
@Slf4j
public class BlockingObjectPoolSemaphore {

    private final Queue<Object> pool;

    private Semaphore semaphoreFull;

    private Semaphore semaphoreEmpty;

    protected final int size;

    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPoolSemaphore(int size) {
        this.size = size;
        pool = new LinkedBlockingQueue<>(size);
        semaphoreFull = new Semaphore(size);
        semaphoreEmpty = new Semaphore(size);
    }

    /**
     * Gets object from pool or blocks if pool is empty * * @return object from pool
     */
    public Object get() throws InterruptedException {
        semaphoreFull.acquire();
        semaphoreEmpty.release();
        return pool.poll();
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) throws InterruptedException {
        semaphoreEmpty.acquire();
        semaphoreFull.release();
        pool.add(object);
    }
}
