package com.example.patterns.objects_pool.synchronous;

import com.example.patterns.objects_pool.IPool;

import java.util.ArrayList;

public class SimplePool<T> implements IPool<T> {

    private PoolObjectFactory<T> factory;
    private ArrayList<T> pool;

    public SimplePool(SimplePool.PoolObjectFactory<T> factory) {
        this.factory = factory;
        pool = new ArrayList<>();
    }

    public PoolObjectFactory<T> getFactory() {
        return factory;
    }

    @Override
    public T acquire() {
        T instance = null;

        if (pool.isEmpty()) {
            instance = factory.newInstance();
        } else {
            instance = pool.remove(pool.size() - 1);
        }
        return instance;
    }

    @Override
    public void release(T object) {
        pool.add(object);
    }

    public interface PoolObjectFactory<T> {
        T newInstance();
    }
}
