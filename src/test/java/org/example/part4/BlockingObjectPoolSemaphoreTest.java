package org.example.part4;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class BlockingObjectPoolSemaphoreTest {

    BlockingObjectPoolSemaphore pool = new BlockingObjectPoolSemaphore(5);

    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "org.example.part4";
    private static final String MSG = "Pool is empty";

    @Before
    public void beforeTest() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
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
        for (int i = 0; i < 6; i++) {
            pool.get();
        }
        assertEquals(1, memoryAppender.search(MSG, Level.INFO).size());
    }

    @Test
    public void take() {
    }
}