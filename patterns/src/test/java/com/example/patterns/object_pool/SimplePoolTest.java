package com.example.patterns.object_pool;

import com.example.patterns.objects_pool.synchronous.SimplePool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePoolTest {

    @Test
    public void acquire_release_sequentially() {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory());

        int acquisitionCounter = 0;
        int newInstanceCounter = 0;
        int releaseCounter = 0;
        Random random = new Random();
        ArrayList<CustomObject> inUse = new ArrayList<>();
        for (int i=0; i<100; i++) {
            if (inUse.isEmpty() || random.nextInt()%2 == 0) {
                CustomObject obj = pool.acquire();
                acquisitionCounter++;
                inUse.add(obj);
                // use the object before returning it
                // (1) no automatic cleanup => perform it manually
                obj.setIntVar(0);
                obj.setStringVar(null);
                // (2) do something
                System.out.println(obj.toString());
            } else {
                CustomObject obj = inUse.remove(inUse.size() - 1);
                pool.release(obj);
                releaseCounter++;
            }
        }
        /*
        // release the remaining in-use objects
        for (int i=0; i<inUse.size(); i++) {
            CustomObject obj = inUse.remove(inUse.size() - 1);
            pool.release(obj);
            releaseCounter++;
        }
        */
        newInstanceCounter = pool.getFactory().getCounter();

        System.out.println("acquisitions: " + acquisitionCounter + " (new instances: " + newInstanceCounter + ") " + ", release: " + releaseCounter);
    }

    @Test
    public void acquire_release_concurrently() throws InterruptedException {
        CustomObjectSimplePool pool = new CustomObjectSimplePool(new CustomObjectSimplePoolFactory());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CustomObjectUser user = new CustomObjectUser(pool, countDownLatch);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(user);
        countDownLatch.await();
    }

    public static class CustomObjectUser implements Runnable {

        private CountDownLatch countDownLatch;
        private SimplePool<CustomObject> pool;
        private CustomObjectSimplePoolFactory factory;

        public CustomObjectUser(CustomObjectSimplePool pool, CountDownLatch countDownLatch) {
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
            for (int i=0; i<100; i++) {
                if (inUse.isEmpty() || random.nextInt()%2 == 0) {
                    CustomObject obj = pool.acquire();
                    acquisitionCounter++;
                    inUse.add(obj);
                } else {
                    CustomObject obj = inUse.remove(inUse.size() - 1);
                    pool.release(obj);
                    releaseCounter++;
                }
            }
            newInstanceCounter = factory.getCounter();

            System.out.println("acquisitions: " + acquisitionCounter + " (new instances: " + newInstanceCounter + ") " + ", release: " + releaseCounter);

            countDownLatch.countDown();
        }
    }
}
