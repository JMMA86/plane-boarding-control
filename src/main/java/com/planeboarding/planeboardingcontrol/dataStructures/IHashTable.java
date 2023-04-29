package com.planeboarding.planeboardingcontrol.dataStructures;

public interface IHashTable<K, V> {
    void insert(K key, V value) throws Exception;
    V search(K key);
    void delete(K key);
}
