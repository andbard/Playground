package com.example.patterns.objects_pool.synchronous;

import com.example.patterns.objects_pool.IObjectFactory;
import com.example.patterns.objects_pool.IBasePool;

import java.util.ArrayList;

public class SimplePool<T> implements IBasePool<T> {

    private IObjectFactory<T> factory;
    private ArrayList<T> pool;
    private int capacity;

    /**
     * Get an unbound objects' pool
     * @param factory the factory providing the object's instances
     */
    public SimplePool(IObjectFactory<T> factory) {
        this(factory, -1);
    }

    /**
     * Get an objects' pool of given capacity
     * @param factory the factory providing the object's instances
     * @param capacity the pool capacity. For negative values an unbound pool is instantiated
     */
    public SimplePool(IObjectFactory<T> factory, int capacity) {
        this.factory = factory;
        if (capacity < 0) {
            pool = new ArrayList<>();
        } else {
            pool = new ArrayList<>(capacity);
        }
        this.capacity = capacity;
    }

    @Override
    public T acquire() {
        T instance;
        if (pool.isEmpty()) {
            instance = factory.newInstance();
        } else {
            int lastPos = pool.size() - 1;
            if (lastPos < 0) {
                System.out.println("*** negative position => pool.isEmpty() returned false but the computed last position index is " + lastPos + " => probably concurrent operations issue ***");
                throw new RuntimeException("concurrency problem");
            }
            try {
                instance = pool.remove(lastPos);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("*** trying to access pool (of size: " + pool.size() + ") by index: " + lastPos + " ***");
                throw new RuntimeException("concurrency problem");
            }

        }
        return instance;
    }

    @Override
    public void release(T object) {
        if (object == null) {
            // todo: cleanup
            System.out.println("*** trying to release a null object ***");
            throw new RuntimeException("trying to release a null object");
        }
        if (capacity < 0 || capacity > pool.size()) {
            pool.add(object);
        } else {
            System.out.println("release: pool is full, cannot add instance (that will be GC soon)");
        }
    }

    @Override
    public IObjectFactory<T> getFactory() {
        return factory;
    }
}
