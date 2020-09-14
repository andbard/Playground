package com.example.patterns.objects_pool.synchronous;

import com.example.patterns.objects_pool.IObjectFactory;
import com.example.patterns.objects_pool.IPool;

interface ISimplePool<T> extends IPool<T> {

    IObjectFactory<T> getFactory();
}
