package org.example.part3.lockApproach;

import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class CustomQueue{
    private Queue<String> queue = new LinkedList<>();

    private final Lock lock = new ReentrantLock(true);

    private Condition notFullCondition = lock.newCondition();

    private Condition notEmptyCondition = lock.newCondition();
}
