package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.IPool;
import com.example.patterns.objects_pool.synchronous.SimplePool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePoolTest {

    //--- single thread

    @Test
    public void acquire_release_sequentially_on_zero_capacity_pool() throws InterruptedException { // 0 capacity ≡ no pool (=> every acquisition result in an instantiation)
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 0);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        user.run();
        countDownLatch.await();
    }

    @Test
    public void acquire_release_sequentially_on_unbound_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        user.run();
        countDownLatch.await();
    }

    @Test
    public void acquire_release_sequentially_on_single_capacity_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        user.run();
        countDownLatch.await();
    }

    //--- single worker thread

    @Test
    public void acquire_release_from_single_worker_thread_on_zero_capacity_pool() throws InterruptedException { // 0 capacity ≡ no pool (=> every acquisition result in an instantiation)
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 0);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(user);
        countDownLatch.await();
    }

    @Test
    public void acquire_release_from_single_worker_thread_on_unbound_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(user);
        countDownLatch.await();
    }

    @Test
    public void acquire_release_from_single_worker_thread_on_single_capacity_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(1, pool, countDownLatch);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(user);
        countDownLatch.await();
    }

    //--- concurrent threads

    @Test
    public void acquire_release_concurrently_on_zero_capacity_not_thread_safe_pool() throws InterruptedException { // 0 capacity ≡ no pool (=> every acquisition result in an instantiation)
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 0);
        int num = 4;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        CustomObjectUser[] users = new CustomObjectUser[num];
        for (int i=0; i<num; i++) {
            users[i] = new CustomObjectUser(i+1, pool, countDownLatch);
        }
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i=0; i<num; i++) {
            service.execute(users[i]);
        }
        countDownLatch.await();
    }

    @Test
    public void acquire_release_concurrently_on_unbound_not_thread_safe_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory());
        int num = 4;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        CustomObjectUser[] users = new CustomObjectUser[num];
        for (int i=0; i<num; i++) {
            users[i] = new CustomObjectUser(i+1, pool, countDownLatch);
        }
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i=0; i<num; i++) {
            service.execute(users[i]);
        }
        countDownLatch.await();
    }

    /**
     * With a not thread safe data structure the following might happen:
     * - suppose we have a single capacity pool with an element
     * - user_i checks if pool is empty and gets false (=> there is an element that might be remove)
     * - before user_i calls pool.remove (or because of the underlying pool data structure is not thread safe)
     * user_j might check if pool is empty getting false. When user_j tries actually to call pool.remove
     * this should fail since user_i already did it and the single capacity pool should now be empty
     *
     * @throws InterruptedException
     */
    @Test
    public void acquire_release_concurrently_on_single_capacity_not_thread_safe_pool() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory(), 1);
        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        CustomObjectUser[] users = new CustomObjectUser[num];
        for (int i=0; i<num; i++) {
            users[i] = new CustomObjectUser(i+1, pool, countDownLatch);
        }
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0; i<num; i++) {
            service.execute(users[i]);
        }
        countDownLatch.await();
    }

    //---

    @Test
    public void acquire_release_concurrently_on_single_capacity_thread_safe_pool() throws InterruptedException {
        CustomObjectSimpleConcurrentPool pool = new CustomObjectSimpleConcurrentPool(new CustomObjectSimplePoolFactory(), 1);
        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        CustomObjectUser[] users = new CustomObjectUser[num];
        for (int i=0; i<num; i++) {
            users[i] = new CustomObjectUser(i+1, pool, countDownLatch);
        }
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0; i<num; i++) {
            service.execute(users[i]);
        }
        countDownLatch.await();
    }

    //---

    public static class CustomObjectUser implements Runnable {

        private int id;
        private CountDownLatch countDownLatch;
        private IPool<CustomObject> pool;

        public CustomObjectUser(int id, IPool<CustomObject> pool, CountDownLatch countDownLatch) {
            this.id = id;
            this.pool = pool;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            int acquisitionCounter = 0;
            int newInstanceCounter = 0;
            int releaseCounter = 0;
            Random random = new Random();
            ArrayList<CustomObject> inUse = new ArrayList<>();
            for (int i=0; i<40; i++) {
                if (inUse.isEmpty() || random.nextInt()%2 == 0) {
                    CustomObject obj = pool.acquire();
                    /*
                    pool.acquire() operation
                    should never return null for UNBOUND pool (at least should block until a new instance is created)
                    but might return null for BOUND ones
                    */
                    System.out.println(obj + " acquired (on thread " + Thread.currentThread().getId() + ")");
                    acquisitionCounter++;
                    inUse.add(obj);
                    useCustomObject(this, obj);
                } else {
                    CustomObject obj = inUse.remove(inUse.size() - 1);
                    cleanCustomObject(obj);
                    pool.release(obj);
                    System.out.println(obj + " released (on thread " + Thread.currentThread().getId() + ")");
                    releaseCounter++;
                }
            }
            newInstanceCounter = ((CustomObjectSimpleConcurrentPool) pool).getFactory().getCounter();
            System.out.println("acquisitions: " + acquisitionCounter + " (new instances: " + newInstanceCounter + ") " + ", release: " + releaseCounter);
            countDownLatch.countDown();
        }

        @Override
        public String toString() {
            return "CustomObjectUser{" +
                    "id=" + id +
                    '}';
        }
    }

    private static void cleanCustomObject(CustomObject obj) {
        obj.setStringVar("cleaned");
    }

    static Random random = new Random();
    private static void useCustomObject(CustomObjectUser user, CustomObject obj) {
        int millis = random.nextInt(50);
        System.out.println(user + " is using " + obj + "(after having slept for " + millis + "[ms])");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
