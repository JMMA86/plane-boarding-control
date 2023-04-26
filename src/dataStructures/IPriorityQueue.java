package dataStructures;

import exception.HeapUnderFlowException;
import exception.KeyIsBiggerException;
import exception.KeyIsSmallerException;

public interface IPriorityQueue<K extends Comparable<K>, V> {

    // Para colas de prioridad máximas
    V heapMaximun();

    V heapExtractMax() throws HeapUnderFlowException;

    void heapIncreaseKey(int i, K key) throws KeyIsSmallerException;

    void maxHeapInsert(K key, V value) throws KeyIsSmallerException;

    // Para colas de prioridad mínimas
    V heapMinimun();

    V heapExtractMin() throws HeapUnderFlowException;

    void heapDecreaseKey(int i, K key) throws KeyIsBiggerException;

    void minHeapInsert(K key, V value) throws KeyIsBiggerException;
}

