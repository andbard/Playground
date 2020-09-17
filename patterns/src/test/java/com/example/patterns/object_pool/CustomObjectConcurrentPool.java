package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.AbstractPoolableObjectFactory;
import com.example.patterns.objects_pool.concurrent.AbstractPoolableObjectConcurrentPool;

public class CustomObjectConcurrentPool extends AbstractPoolableObjectConcurrentPool<CustomObject> {

    public CustomObjectConcurrentPool(AbstractPoolableObjectFactory<CustomObject> factory) {
        super(factory);
    }

    public CustomObjectConcurrentPool(AbstractPoolableObjectFactory<CustomObject> factory, int capacity) {
        super(factory, capacity);
    }

    @Override
    protected void recycle(CustomObject instance) {
        if (instance != null) {
            instance.setStringVar("recycled");
        }
    }
}
