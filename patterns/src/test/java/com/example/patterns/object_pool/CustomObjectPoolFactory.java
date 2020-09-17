package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.AbstractPoolableObjectFactory;

public class CustomObjectPoolFactory extends AbstractPoolableObjectFactory<CustomObject> {

    // unique instance counter/id
    private int counter = 0;

    @Override
    public CustomObject instantiate() {
        CustomObject instance = new CustomObject(counter, "new");
        counter++;
        return instance;
    }

    public int getCounter() {
        return counter;
    }
}
