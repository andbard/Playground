package com.example.patterns.objects_pool;

public interface IPoolableObjectPool<T> extends IPool<T> {

    IObjectFactory<IPoolableObject<T>> getFactory();
}
