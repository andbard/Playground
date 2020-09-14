package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.synchronous.SimplePool;

class CustomObjectSimplePool extends SimplePool<CustomObject> {

    public CustomObjectSimplePool(CustomObjectSimplePoolFactory factory) {
        super(factory);
    }

    public CustomObjectSimplePool(CustomObjectSimplePoolFactory factory, int capacity) {
        super(factory, capacity);
    }

    @Override
    public CustomObjectSimplePoolFactory getFactory() {
        return (CustomObjectSimplePoolFactory) super.getFactory();
    }
}
