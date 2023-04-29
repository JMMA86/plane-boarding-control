package com.planeboarding.planeboardingcontrol.dataStructures;

import com.planeboarding.planeboardingcontrol.exception.*;

public interface IPriorityQueue<K extends Comparable<K>, V> {

    // For max priority queues
    V heapMaximun();

    V heapExtractMax() throws HeapUnderFlowException;

    void heapIncreaseKey(int i, K key) throws KeyIsSmallerException;

    void maxHeapInsert(K key, V value) throws KeyIsSmallerException;
}

