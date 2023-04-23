package dataStructures;

import exception.DuplicatedKeyException;

public class HashTable<K,V> implements IHashTable<K,V> {
    private final int ARR_SIZE = 127;
    private HNode<K,V>[] nodes;

    public HashTable() {
        this.nodes = new HNode[ARR_SIZE];
    }

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

    @Override
    public V search(K key) {
        int index = h1(key);

        HNode<K,V> current = nodes[index];
        while(current != null) {
            if(current.getKey() == key) return current.getValue();
            current = current.getPrevious();
        }
        return null;
    }

    @Override
    public void delete(K key) {
        int index = h1(key);
        HNode<K,V> target = null;

        HNode<K,V> current = nodes[index];
        while(current != null) {
            if(current.getKey() == key) {
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

    public int getTakenElements() {
        int ans = 0;
        for(int i=0; i<ARR_SIZE; i++) {
            HNode<K,V> current = nodes[i];
            if(current != null) {
                ans++;
                while(current.getPrevious() != null) {
                    current = current.getPrevious();
                    ans++;

                }
            }
        }
        return ans;
    }

    // HASH FUNCTIONS

    private int h1(K key) {
        String keyStr = key.toString();
        int keyInt = toInteger(keyStr);
        return keyInt % ARR_SIZE;
    }

    //

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
}
