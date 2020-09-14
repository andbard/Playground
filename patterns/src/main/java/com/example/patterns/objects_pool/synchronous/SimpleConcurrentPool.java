package com.example.patterns.objects_pool.synchronous;

import com.example.patterns.objects_pool.IObjectFactory;
import com.example.patterns.objects_pool.IPool;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleConcurrentPool<T> implements ISimplePool<T> {

    private IObjectFactory<T> factory;
    ConcurrentLinkedQueue<T> queue;
    private int capacity;

    /**
     * Get an unbound objects' pool
     * @param factory the factory providing the object's instances
     */
    public SimpleConcurrentPool(IObjectFactory<T> factory) {
        this(factory, -1);
    }

    /**
     * Get an objects' pool of given capacity
     * @param factory the factory providing the object's instances
     * @param capacity the pool capacity. For negative values an unbound pool is instantiated
     */
    public SimpleConcurrentPool(IObjectFactory<T> factory, int capacity) {
        this.factory = factory;
        if (capacity < 0) {
            queue = new ConcurrentLinkedQueue<>();
        } else {
            queue = new ConcurrentLinkedQueue<>();
        }
        this.capacity = capacity;
    }

    @Override
    public T acquire() {
        T instance;
        if (queue.isEmpty()) {
            instance = factory.newInstance();
        } else {
            int lastPos = queue.size() - 1;
            if (lastPos < 0) {
                System.out.println("*** negative position => pool.isEmpty() returned false but the computed last position index is " + lastPos + " => probably concurrent operations issue ***");
                throw new RuntimeException("concurrency problem");
            }
            try {
                instance = queue.poll();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("*** trying to access pool (of size: " + queue.size() + ") by index: " + lastPos + " ***");
                throw new RuntimeException("concurrency problem");
            }

        }
        return instance;
    }

    @Override
    public void release(T object) {
        if (object == null) {
            System.out.println("*** trying to release a null object ***");
            throw new RuntimeException("trying to release a null object");
        }
        if (capacity >= 0 && capacity > queue.size()) {
            queue.add(object);
        } else {
            System.out.println("release: pool is full, cannot add instance (that will be GC soon)");
        }
    }

    @Override
    public IObjectFactory<T> getFactory() {
        return factory;
    }
}
