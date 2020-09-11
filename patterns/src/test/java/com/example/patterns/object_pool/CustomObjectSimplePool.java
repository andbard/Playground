package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.synchronous.SimplePool;

class CustomObjectSimplePool extends SimplePool<CustomObject> {

    public CustomObjectSimplePool(CustomObjectSimplePoolFactory factory) {
        super(factory);
    }

    @Override
    public CustomObjectSimplePoolFactory getFactory() {
        return (CustomObjectSimplePoolFactory) super.getFactory();
    }
}
