package org.example.part4;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool that block when it has not any items or it full
 */
//TODO Semaphore
//TODO await bad approach
@Data
@Slf4j
public class BlockingObjectPoolConditionApproach {

    private final Queue<Object> pool = new LinkedList<>();

    private final Lock lock = new ReentrantLock();

    private final Condition empty = lock.newCondition();

    private final Condition full = lock.newCondition();

    private final int size;

    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPoolConditionApproach(int size) {
        this.size = size;
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
    public Object get() throws InterruptedException {
        lock.lock();
        try {
            while (pool.isEmpty()) {
                log.info("Pool is empty");
                empty.await();
            }
            var instance = pool.poll();
            full.signal();
            return instance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) throws InterruptedException {
        lock.lock();
        try {
            while (pool.size() == size) {
                log.info("Pool is full");
                full.await();
            }
            pool.offer(object);
            empty.signal();
        } finally {
            lock.unlock();
        }
    }
}
