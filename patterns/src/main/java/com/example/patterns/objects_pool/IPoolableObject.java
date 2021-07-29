package com.example.patterns.objects_pool;

/**
 * Class used by the {@link IPoolableObjectFactory} to wrap the requested object instance
 * and by the {@link IPool} to track/get/manage the {@link IPool}'s acquired/released instance.
 *
 * For comparison see the more detailed
 * <a href="http://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/PooledObject.html">Apache PooledObject doc</a>
 * and
 * <a href="https://github.com/apache/commons-pool/blob/master/src/main/java/org/apache/commons/pool2/PooledObject.java">its implementation</a>
 * (where <a href="https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html">default method</a> is used)
 *
 * @param <T> the specific wrapped object's type
 */
public interface IPoolableObject<T> {

    /**
     * Get the wrapped generic object instance
     * @return the wrapped instance
     */
    T getObject();

    //---

    Status getStatus();

    long getCreationTime();

    long getLastAcquisitionTime();

    long getAcquisitionCount();

    enum Status {
//        INSTANTIATING("instantiating"), // set by the factory
//        INSTANTIATED("instantiated"), // set by the factory
        AVAILABLE("available"), // IDLE("idle") // set by the pool
        IN_USE("in_use"), // BUSY("busy") // set by the pool
        INVALID("invalid"); // set by the pool

        private String tag;

        Status(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public Status getByTag(String tag) {
            for (Status value : values()) {
                if (value.tag.equalsIgnoreCase(tag)) {
                    return value;
                }
            }
            return null;
        }
    }
}
