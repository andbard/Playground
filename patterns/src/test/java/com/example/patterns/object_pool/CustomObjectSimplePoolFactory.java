package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.synchronous.SimplePool;

class CustomObjectSimplePoolFactory implements SimplePool.PoolObjectFactory<CustomObject> {

    private int counter = 0;
    @Override
    public CustomObject newInstance() {
        CustomObject instance = new CustomObject(counter, "counter = " + counter);
        counter++;
        return instance;
    }

    public int getCounter() {
        return counter;
    }
}
