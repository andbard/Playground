package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.synchronous.SimpleConcurrentPool;

public class CustomObjectSimpleConcurrentPool extends SimpleConcurrentPool<CustomObject> {

    public CustomObjectSimpleConcurrentPool(CustomObjectSimplePoolFactory factory) {
        super(factory);
    }

    public CustomObjectSimpleConcurrentPool(CustomObjectSimplePoolFactory factory, int capacity) {
        super(factory, capacity);
    }

    @Override
    public CustomObjectSimplePoolFactory getFactory() {
        return (CustomObjectSimplePoolFactory) super.getFactory();
    }
}
