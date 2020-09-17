package com.example.patterns.objects_pool;

public interface IBasePool<T> extends IPool<T> {

    IObjectFactory<T> getFactory();
}
