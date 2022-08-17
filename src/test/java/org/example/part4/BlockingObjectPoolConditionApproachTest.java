package org.example.part4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class BlockingObjectPoolConditionApproachTest {

    BlockingObjectPoolConditionApproach pool = new BlockingObjectPoolConditionApproach(5);

    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "org.example.part4";
    private static final String MSG_EMPTY = "Pool is empty";
    private static final String MSG_FULL = "Pool is full";

    @Before
    public void beforeTest() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @After
    public void afterTest() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    public void get() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    pool.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        assertEquals(1, memoryAppender.search(MSG_EMPTY, Level.INFO).size());
    }

    @Test
    public void take() {
        pool.take(new Object());
        assertEquals(1, memoryAppender.search(MSG_FULL, Level.INFO).size());

    }
}