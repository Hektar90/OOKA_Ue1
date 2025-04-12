package org.hbrs.ooka.uebung1;

import java.util.List;

public interface Caching<K,V> {
    void cacheResult( String key, List<Object> value);
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    void clear();
}
