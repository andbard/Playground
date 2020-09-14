package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.IObjectFactory;

public class CustomObjectSimplePoolFactory implements IObjectFactory<CustomObject> {

    private int counter = 0;
    @Override
    public CustomObject newInstance() {
        CustomObject instance = new CustomObject(counter, "new");
        counter++;
        return instance;
    }

    public int getCounter() {
        return counter;
    }
}
