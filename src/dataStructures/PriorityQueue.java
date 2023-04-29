package dataStructures;

import exception.HeapUnderFlowException;
import exception.KeyIsBiggerException;
import exception.KeyIsSmallerException;

import java.util.ArrayList;
import java.util.Collections;

public class PriorityQueue<K extends Comparable<K>, V> implements IPriorityQueue<K, V> {
    private final ArrayList<PQNode<K, V>> array;

    private int heapsize;

    /**
     * Creates an empty priority queue
     */
    public PriorityQueue() {
        this.array = new ArrayList<>();
        this.heapsize = 0;
    }

    /**
     * @param i The ith element to be heapify in the array
     *
     * This function organize the nodes in the heap taking the bigger keys as the roots
     */
    private void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest = i;

        if (l <= heapsize && isBigger(array.get(l).getKey(), array.get(largest).getKey())) {
            largest = l;
        }

        if (r <= heapsize && isBigger(array.get(r).getKey(), array.get(largest).getKey())) {
            largest = r;
        }

        if (largest != i) {
            Collections.swap(array, i, largest);
            maxHeapify(largest);
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
     * This function extracts the biggest element in a maxHipyfied heap, otherwise will not work
     */
    @Override
    public V heapExtractMax() throws HeapUnderFlowException {
        if (heapsize < 1) {
            throw new HeapUnderFlowException("PriorityQueue underflow");
        }
        PQNode<K, V> max = array.get(0);
        array.set(0, array.get(heapsize - 1));
        heapsize--;
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
            throw new KeyIsSmallerException("The given key is smaller than the actual key");
        }

        array.get(i).setKey(key);

        while (i > 0 && isBigger(array.get(i).getKey(), array.get(i / 2).getKey())) {
            Collections.swap(array, i, i / 2);
            i = i / 2;
        }
    }

    /**
     * @param key The new key to be added to the heap
     * @throws KeyIsSmallerException Threw by the heapIncreasekey when there is no bigger key
     *
     * This function expands the maximum key
     */
    @Override
    public void maxHeapInsert(K key, V element) throws KeyIsSmallerException {
        PQNode<K, V> node = new PQNode<>(key, element);
        heapsize++;
        array.add(node);
        heapIncreaseKey(heapsize - 1, key);
    }

    /**
     * @param element1 Element to be compared
     * @param element2 Comparing element
     * @return True if element1 is bigger than element 2, otherwise it will return false
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

    public ArrayList<PQNode<K, V>> getArray() {
        return array;
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
