package com.example.patterns.objects_pool;

public interface IPoolableObjectFactory<T> extends IObjectFactory<IPoolableObject<T>> {

    @Override
    IPoolableObject<T> newInstance();
}
