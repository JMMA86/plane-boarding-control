package com.planeboarding.planeboardingcontrol.dataStructures;

public class PQNode<K, V> {
    private K key;
    private V value;

    public PQNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setV(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PQNode{" +
                "key=" + key.toString() +
                ", value=" + value.toString() +
                '}';
    }
}
