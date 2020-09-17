package com.example.patterns.objects_pool.concurrent;

import com.example.patterns.objects_pool.AbstractPoolableObjectFactory;
import com.example.patterns.objects_pool.IObjectFactory;
import com.example.patterns.objects_pool.IPoolableObject;
import com.example.patterns.objects_pool.IPoolableObjectPool;
import com.example.patterns.objects_pool.PoolableObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractPoolableObjectConcurrentPool<T> implements IPoolableObjectPool<T> {

    private AbstractPoolableObjectFactory<T> factory;
    private ConcurrentLinkedQueue<PoolableObject<T>> available;
    private ConcurrentHashMap<T, PoolableObject<T>> inUse;

    private int capacity;

    /**
     * Get an unbound objects' pool
     * @param factory the factory providing the object's instances
     */
    public AbstractPoolableObjectConcurrentPool(AbstractPoolableObjectFactory<T> factory) {
        this(factory, -1);
    }

    /**
     * Get an objects' pool of given capacity
     * @param factory the factory providing the object's instances
     * @param capacity the pool capacity. For negative values an unbound pool is instantiated
     */
    public AbstractPoolableObjectConcurrentPool(AbstractPoolableObjectFactory<T> factory, int capacity) {
        this.factory = factory;
        this.capacity = capacity;
        available = new ConcurrentLinkedQueue<>();
        inUse = new ConcurrentHashMap<>();
    }

    /**
     * Acquire a new wrapping instance ({@link IPoolableObject}) with the required wrapped instance (T)
     * @return
     */
    @Override
    public T acquire() {
        PoolableObject<T> wrapper;
        if (available.isEmpty()) {
            // create a new (wrapped) instance...
            wrapper = factory.newInstance();
        } else {
            int lastPos = available.size() - 1;
            if (lastPos < 0) {
                // todo: cleanup and if necessary make acquire throw an exception
                System.out.println("*** negative position => pool.isEmpty() returned false but the computed last position index is " + lastPos + " => probably concurrent operations issue ***");
                throw new RuntimeException("concurrency problem");
            }
            try {
                // remove a recycled (wrapped) instance from the queue...
                wrapper = available.poll();
            } catch (ArrayIndexOutOfBoundsException e) {
                // todo: cleanup and if necessary make acquire throw an exception
                System.out.println("*** trying to access pool (of size: " + available.size() + ") by index: " + lastPos + " ***");
                throw new RuntimeException("concurrency problem");
            }
        }
        wrapper.setLastAcquisitionTime(System.currentTimeMillis());
        wrapper.incrementAcquisitionCount();
        wrapper.setStatus(IPoolableObject.Status.IN_USE);
        // ...and put it into the "in use" instances
        inUse.put(wrapper.getObject(), wrapper);
        System.out.println("acquisition #" + wrapper.getAcquisitionCount() + " of " + wrapper.getObject());
        return wrapper.getObject();
    }

    @Override
    public void release(T object) {
        if (object == null) {
            // todo: cleanup
            System.out.println("*** trying to release a null object ***");
            throw new RuntimeException("trying to release a null object");
        }
        // retrieve the wrapping instance
        PoolableObject<T> wrapper = inUse.remove(object);
        if (capacity < 0 || (capacity >= 0 && capacity > available.size())) {
            // recycle the wrapped instance
            recycle(object);
            // make it available after having recycled it
            available.add(wrapper);
            wrapper.setStatus(IPoolableObject.Status.AVAILABLE);
        } else {
            System.out.println("release: pool is full, cannot add instance (that will be GC soon)");
        }
    }

    @Override
    public AbstractPoolableObjectFactory<T> getFactory() {
        return factory;
    }

    /**
     * Recycle/clean/clear the
     */
    protected abstract void recycle(T instance);
}
