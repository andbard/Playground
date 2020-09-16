package com.example.patterns.objects_pool;

public interface ISimplePool<T> extends IPool<T> {

    IObjectFactory<T> getFactory();
}
