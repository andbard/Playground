package com.example.patterns.objects_pool;

public interface IObjectFactory<T> {

    T newInstance();
}
