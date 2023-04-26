package dataStructures;

import exception.HeapUnderFlowException;
import exception.KeyIsBiggerException;
import exception.KeyIsSmallerException;

import java.util.ArrayList;

public class PriorityQueue<K extends Comparable<K>, V> implements IPriorityQueue<K, V> {
    private ArrayList<PQNode<K, V>> array;
    private int heapSize;
    public PriorityQueue() {
        this.array = new ArrayList<>();
    }

    /**
     * @param i The ith element to be heapified in the array
     *
     * This function organize the nodes in the heap taking the bigger keys as the roots
     */
    private void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest = i;

        if (l <= heapSize && isBigger(array.get(l).getKey(), array.get(i).getKey())) {
            largest = l;
        }

        if (r <= heapSize && isBigger(array.get(r).getKey(), array.get(largest).getKey())) {
            largest = r;
        }

        if (largest != i) {
            exchange(array, i, largest);
            maxHeapify(largest);
        }
    }

    /**
     * @param i The ith element to be heapified in the array
     *
     * This function organize the nodes in the heap taking the smallest keys as the roots
     */
    private void minHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int shortest = i;

        if (l <= heapSize && isBigger(array.get(shortest).getKey(), array.get(l).getKey())) {
            shortest = l;
        }

        if (r <= heapSize && isBigger(array.get(shortest).getKey(), array.get(r).getKey())) {
            shortest = r;
        }

        if (shortest != i) {
            exchange(array, shortest, i);
            minHeapify(shortest);
        }
    }

    /**
     * This function builds the whole heap taking the biggest element as the root
     */
    public void buildMaxHeap() {
        heapSize = array.size() - 1;

        for (int i = array.size() / 2; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    /**
     * This function build the whole heap taking the smallest element as the root
     */
    public void buildMinHeap() {
        heapSize = array.size() - 1;

        for (int i = array.size() / 2; i >= 0; i--) {
            minHeapify(i);
        }
    }

    /**
     * @param reversed Changes the order of the heap to descending
     *
     * This function organizes the array in ascending order if reversed is false
     */
    public void heapSort(Boolean reversed) {
        if (reversed) {
            buildMinHeap();
            for (int i = array.size() - 1; i >= 1; i--) {
                exchange(array, 0, i);
                heapSize = heapSize - 1;
                minHeapify(0);
            }
        } else {
            buildMaxHeap();
            for (int i = array.size() - 1; i >= 1; i--) {
                exchange(array, 0, i);
                heapSize = heapSize - 1;
                maxHeapify(0);
            }
        }
    }


    /**
     * @return The biggest element in the array
     *
     * returns the biggest element in a maxHipified heap
     */
    @Override
    public V heapMaximun() {
        return array.get(0).getValue();
    }


    /**
     * @return The biggest element in the heap
     * @throws HeapUnderFlowException When the heap is empty
     *
     * This function extracts the biggest element in a maxHipified heap, otherwise will not work
     */
    @Override
    public V heapExtractMax() throws HeapUnderFlowException {
        if (heapSize < 1) {
            throw new HeapUnderFlowException("PriorityQueue underflow");
        }
        PQNode<K, V> max = array.get(0);
        array.set(0, array.get(heapSize - 1));
        heapSize--;
        maxHeapify(0);
        return max.getValue();
    }


    /**
     * @param i The element to increase its priority
     * @param key The new key of the element
     * @throws KeyIsSmallerException Threw if the key is smaller than the actual key
     *
     * Only works with a maxHipified heap
     */
    @Override
    public void heapIncreaseKey(int i, K key) throws KeyIsSmallerException {
        if (isBigger(array.get(i).getKey(), key)) {
            throw new KeyIsSmallerException("The key given is smaller than the actual key");
        }

        array.get(i).setKey(key);

        while (i > 0 && isBigger(array.get(i).getKey(), array.get(i / 2).getKey())) {
            exchange(array, i, i / 2);
            i = i / 2;
        }
    }


    /**
     * @param key The new maximum key in the heap
     * @throws KeyIsSmallerException Threw by the heapIncreasekey when there is no bigger key
     *
     * This function expands the maximum key
     */
    @Override
    public void maxHeapInsert(K key, V element) throws KeyIsSmallerException {
        heapSize++;
        PQNode<K, V> node = new PQNode<>(key, element);
        array.add(node);
        heapIncreaseKey(array.size() - 1, key);
    }

    private void exchange(ArrayList<PQNode<K, V>> A, int index1, int index2) {
        PQNode<K, V> a1 = A.get(index1);
        PQNode<K, V> a2 = A.get(index2);
        A.set(index1, a2);
        A.set(index2, a1);
    }

    /**
     * @param element1 Element to be compared
     * @param element2 Comparing element
     * @return Vrue if element1 is bigger than element 2, otherwise it will return false
     */
    private boolean isBigger(K element1, K element2) {
        return element1.compareTo(element2) > 0;
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return 2 * i + 1;
    }

    @Override
    public String toString() {
        StringBuilder arr = new StringBuilder();
        for (PQNode<K, V> PQNode : array) {
            arr.append(PQNode.toString().indent(4));
        }
        return "PriorityQueue{" + "\n" +
                "array=".indent(2) + arr +
                '}';
    }
}
