package com.planeboarding.planeboardingcontrol.dataStructures;

import com.planeboarding.planeboardingcontrol.exception.*;

import java.util.Arrays;

public class HashTable<K,V> implements IHashTable<K,V> {
    private final int ARR_SIZE = 127;
    private HNode<K,V>[] nodes;

    public HashTable() {
        this.nodes = new HNode[ARR_SIZE];
    }

    /** this functions creates a node given its key and value and adds it to the hash table
     * @param key the attribute that the hash function uses to calculate the slot
     * @param value the object of the node to be inserted
     * @throws DuplicatedKeyException when the key is already in the hash table
     */
    @Override
    public void insert(K key, V value) throws DuplicatedKeyException {
        if(search(key) != null) {
            throw new DuplicatedKeyException("The key is already on the hash table");
        }
        HNode<K,V> newNode = new HNode<>(key, value);
        int index = h1(key);
        if(nodes[index] != null) {
            nodes[index].setNext(newNode);
            newNode.setPrevious(nodes[index]);
        }
        nodes[index] = newNode;
    }

    /**
     * Searches for a value associated with the given key in the hash table.
     * @param key the key to search for in the hash table
     * @return the value associated with the given key, or null if the key is not found
     */
    @Override
    public V search(K key) {
        int index = h1(key);

        HNode<K,V> current = nodes[index];
        while(current != null) {
            if(current.getKey().equals(key)) return current.getValue();
            current = current.getPrevious();
        }
        return null;
    }


    /**
     * Removes the entry with the given key from the hash table.
     * @param key the key of the entry to remove from the hash table
     */
    @Override
    public void delete(K key) {
        int index = h1(key);
        HNode<K,V> target = null;

        HNode<K,V> current = nodes[index];
        while(current != null) {
            if(current.getKey().equals(key)) {
                target = current;
                break;
            }
            current = current.getPrevious();
        }
        if(target == null) return;

        HNode<K,V> previous = target.getPrevious();
        HNode<K,V> next = target.getNext();
        if(next != null) {
            next.setPrevious(previous);
        }
        if(previous != null) {
            previous.setNext(next);
        } else {
            nodes[index] = next;
        }

        if(previous == null && next == null) {
            nodes[index] = null;
        }
    }

    /**
     * Calculates the number of elements in the hash table.
     * @return the elements in the hash table
     */
    public int getTakenElements() {
        int ans = 0;
        for(int i=0; i<ARR_SIZE; i++) {
            HNode<K,V> current = nodes[i];
            if(current == null) continue;
            ans++;
            while(current.getPrevious() != null) {
                current = current.getPrevious();
                ans++;

            }
        }
        return ans;
    }

    // HASH FUNCTIONS

    /**
     * Computes the index of the hash table bucket where the entry with the given key should be stored.
     * @param key the key of the entry whose bucket index is to be computed
     * @return the index of the bucket where the entry with the given key should be stored
     */
    private int h1(K key) {
        String keyStr = key.toString();
        int keyInt = toInteger(keyStr);
        return keyInt % ARR_SIZE;
    }

    /**
     * Converts the given string to an integer value, so that the hash function can use it
     * @param str the string to be converted to an integer value
     * @return the integer value corresponding to the given string
     */
    private int toInteger(String str) {
        int ans = 0;
        int j=str.length()-1;
        for(int i=0; i<str.length(); i++) {
            int v = str.charAt(i);
            ans += v * Math.pow(128, j);
            j--;
        }
        return ans;
    }

    public HNode<K, V>[] getNodes() {
        return nodes;
    }

    public void setNodes(HNode<K, V>[] nodes) {
        this.nodes = nodes;
    }

    public int getARR_SIZE() {
        return ARR_SIZE;
    }

    /**
     * Returns a string representation of the hash table, showing all the key-value pairs in the table.
     * @return a string representation of the hash table
     */
    @Override
    public String toString() {
        String listStr = "";
        for(int i=0; i<ARR_SIZE; i++) {
            HNode<K,V> current = nodes[i];
            if(current != null) {
                listStr += "\n - " + current.getValue().toString();
                while(current.getPrevious() != null) {
                    current = current.getPrevious();
                    listStr += "\n - " + current.getValue().toString();
                }
            }
        }
        return listStr;
    }
}
