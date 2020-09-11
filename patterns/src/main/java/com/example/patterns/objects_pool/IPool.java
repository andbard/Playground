package com.example.patterns.objects_pool;

public interface IPool<T> {

    T acquire();

    void release(T object);
}
