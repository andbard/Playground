package com.example.patterns.objects_pool.synchronous;

import com.example.patterns.objects_pool.IPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractPool<T> implements IPool<T> {

    private final LinkedBlockingQueue<PoolableObject<T>> queue;

    private Map<T, PoolableObject<T>> objectsPool;

    private AbstractPoolableObjectFactory<T> factory;

    public AbstractPool(AbstractPoolableObjectFactory<T> factory) {
        this.factory = factory;
        this.queue = new LinkedBlockingQueue<>();
        this.objectsPool = new HashMap<>();
    }

    public abstract void clear(T object);

    @Override
    public T acquire() {
        PoolableObject<T> wrapper = null;

        /*if (objectsPool.isEmpty()) {
            // no instances in the pool => ask the factory
            wrapper = factory.newInstance();
        } else {
            Iterator<Map.Entry<T, IPoolableObject<T>>> iterator = objectsPool.entrySet().iterator();
            Map.Entry<T, IPoolableObject<T>> entry = iterator.next();
            wrapper = entry.getValue();
            iterator.remove();
        }*/

        if (queue.isEmpty()) {
            // no instances in the pool => ask the factory
            wrapper = factory.newInstance();
        } else {
            try {
                wrapper = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (wrapper != null) {
            wrapper.setLastAcquisitionTime(System.currentTimeMillis());
            wrapper.incrementAcquisitionCount();
            return wrapper.getObject();
        }
        return null;
    }

    @Override
    public void release(T object) {
        if (object == null) {
            return;
        }
        clear(object);
        PoolableObject<T> wrapper = objectsPool.get(object);
        if (wrapper == null) {
            wrapper = new PoolableObject<>(object);
        }
    }
}
