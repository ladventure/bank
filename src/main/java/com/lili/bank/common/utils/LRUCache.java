package com.lili.bank.common.utils;



import java.util.LinkedHashMap;
import java.util.Map;


/**
 * LRU cache
 */
public class LRUCache<K,V> {
    private final int capacity;
    private final LinkedHashMap<K,V> cache;

    public LRUCache(int capacity) {
        this.cache = new LinkedHashMap<K,V>(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    public V get(K key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }
//    performance is not ok ,can use other cache opt. just for test！
   public synchronized void put(K key, V value) {

        cache.putLast(key, value);
        if (cache.size() > capacity) {
            K oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
        }

    }
    public synchronized V remove(K key) {
        if (cache.containsKey(key)) {
            return cache.remove(key);
        }
        return null;
    }
}

