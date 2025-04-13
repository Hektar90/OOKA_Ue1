package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.uebung1.entities.Product;

import java.util.List;

public interface Caching {
    void cacheResult( String key, List<Object> value);
    void put(String key, Product value);
    Product get(String key);
    void remove(String key);
    void clear();
}
