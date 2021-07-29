package com.example.patterns.objects_pool;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Wrapper class used within the {@link AbstractPoolableObjectFactory} and the
 * {@link com.example.patterns.objects_pool.concurrent.AbstractPoolableObjectConcurrentPool},
 * abstract implementations of {@link IObjectFactory} and {@link IPoolableObjectPool}
 * backed up by {@link PoolableObject} wrapping instances.
 * <br>
 * The factory should returned a new wrapped object instance to the pool.
 * <br>
 * The pool should be able to track the instances usage.
 * <br>
 * The pool (its abstract implementation) should provide a method that the concrete classes
 * should implement in order to clean/clear the wrapped instance.
 * @param <T> the wrapped instance object
 */
public final class PoolableObject<T> implements IPoolableObject<T> {

    private final T object;
    private volatile Status status;
    private final long creationTime;
    private volatile long lastAcquisitionTime;
    private AtomicLong acquisitionCount = new AtomicLong(0);

    public PoolableObject(T object) {
        /*
        the instantiating-instantiated interval should take into consideration also the wrapped object creation.
        This might be obtained:
        (1) instantiating a PoolableObject with the default empty constructor (=> status = INSTANTIATING),
        (2) defining a setObject(T object) method that should be accessed only by the factory and that sets status to INSTANTIATED

        Alternatively, it might be not necessary to track here the wrapped object instantiation time
        and as the wrapper instance reaches the pool (=> status == INSTANTIATED) the only meaningful
        statuses are AVAILABLE/IDLE, IN_USE/BUSY, INVALID. Following this approach there will be no
        setObject(T object) method and the object variable can be final
        */
        this.object = object;
        this.creationTime = System.currentTimeMillis();
        status = Status.AVAILABLE;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    /* this method is package private because only the pool can set the related field */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public long getLastAcquisitionTime() {
        return lastAcquisitionTime;
    }

    /* this method is package private because only the pool can set the related field */
    public void setLastAcquisitionTime(long millis) {
        this.lastAcquisitionTime = millis;
    }

    @Override
    public long getAcquisitionCount() {
        return acquisitionCount.get();
    }

    public void setAcquisitionCount(long acquisitionCount) {
        this.acquisitionCount.set(acquisitionCount);
    }

    /* this method is package private because only the pool can set the related field */
    public void incrementAcquisitionCount() {
        this.acquisitionCount.incrementAndGet();
    }
}
