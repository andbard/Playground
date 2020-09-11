package com.example.patterns.objects_pool;

public interface IPoolableObjectFactory<T> {

    IPoolableObject<T> newInstance();
}
