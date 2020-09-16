package com.example.patterns.objects_pool;

public abstract class AbstractPoolableObjectFactory<T> implements IPoolableObjectFactory<T> {

    public abstract T instantiate();

    public PoolableObject<T> wrap(T wrappedObjectInstance) {
        return new PoolableObject<>(wrappedObjectInstance); // return the wrapping object instance
    }

    @Override
    public final PoolableObject<T> newInstance() {
        return wrap(instantiate());
    }
}
