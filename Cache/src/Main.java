
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cache.Cache;
import cache.models.LFUEvictionStrategy;
import cache.models.LRUEvictionStrategy;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Testing LRU Cache (Sequential) ===");
        testLRUCache();

        System.out.println("\n=== Testing LFU Cache (Sequential) ===");
        testLFUCache();

        System.out.println("\n=== Testing Concurrent LRU Cache ===");
        testConcurrentLRUCache();

        System.out.println("\n=== Testing Concurrent LFU Cache ===");
        testConcurrentLFUCache();
    }

    private static void testLRUCache() {
        Cache<Integer, String> lruCache = new Cache<>(3, new LRUEvictionStrategy<>());

        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");
        System.out.println("Added: 1->One, 2->Two, 3->Three");

        System.out.println("Get key 1: " + lruCache.get(1));

        lruCache.put(4, "Four");
        System.out.println("Added 4->Four (should evict 2)");

        System.out.println("Get key 2: " + lruCache.get(2)); // Should be null
        System.out.println("Get key 1: " + lruCache.get(1));
        System.out.println("Get key 3: " + lruCache.get(3));
        System.out.println("Get key 4: " + lruCache.get(4));
    }

    private static void testLFUCache() {
        Cache<Integer, String> lfuCache = new Cache<>(3, new LFUEvictionStrategy<>());

        lfuCache.put(1, "One");
        lfuCache.put(2, "Two");
        lfuCache.put(3, "Three");
        System.out.println("Added: 1->One, 2->Two, 3->Three");

        lfuCache.get(1);
        lfuCache.get(1);
        lfuCache.get(2);
        System.out.println("Accessed: 1 twice, 2 once");

        lfuCache.put(4, "Four");
        System.out.println("Added 4->Four (should evict 3 - least frequently used)");

        System.out.println("Get key 3: " + lfuCache.get(3)); // Should be null
        System.out.println("Get key 1: " + lfuCache.get(1));
        System.out.println("Get key 2: " + lfuCache.get(2));
        System.out.println("Get key 4: " + lfuCache.get(4));
    }

    private static void testConcurrentLRUCache() throws InterruptedException {
        Cache<Integer, String> cache = new Cache<>(100, new LRUEvictionStrategy<>());
        int threadCount = 10;
        int operationsPerThread = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successfulPuts = new AtomicInteger(0);
        AtomicInteger successfulGets = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    int key = threadId * operationsPerThread + j;

                    // Put operation
                    cache.put(key, "Value-" + key);
                    successfulPuts.incrementAndGet();

                    // Get operation
                    String value = cache.get(key);
                    if (value != null) {
                        successfulGets.incrementAndGet();
                    }

                    // Also try to get some random keys
                    cache.get((key + 50) % 1000);
                }
                latch.countDown();
            });
        }

        System.out.println("Starting " + threadCount + " threads...");
        long startTime = System.currentTimeMillis();

        latch.await(); // Wait for all threads to complete

        long endTime = System.currentTimeMillis();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Total operations: " + (threadCount * operationsPerThread * 3) + " (" + (threadCount * operationsPerThread) + " puts, " + (threadCount * operationsPerThread * 2) + " gets)");
        System.out.println("Successful puts: " + successfulPuts.get());
        System.out.println("Successful gets (non-null): " + successfulGets.get());
        System.out.println("Final cache size: " + cache.size());
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }

    private static void testConcurrentLFUCache() throws InterruptedException {
        Cache<Integer, String> cache = new Cache<>(100, new LFUEvictionStrategy<>());
        int threadCount = 10;
        int operationsPerThread = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successfulPuts = new AtomicInteger(0);
        AtomicInteger successfulGets = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    int key = threadId * operationsPerThread + j;

                    // Put operation
                    cache.put(key, "Value-" + key);
                    successfulPuts.incrementAndGet();

                    // Get operation
                    String value = cache.get(key);
                    if (value != null) {
                        successfulGets.incrementAndGet();
                    }

                    // Also try to get some random keys
                    cache.get((key + 50) % 1000);
                }
                latch.countDown();
            });
        }

        System.out.println("Starting " + threadCount + " threads...");
        long startTime = System.currentTimeMillis();

        latch.await(); // Wait for all threads to complete

        long endTime = System.currentTimeMillis();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Total operations: " + (threadCount * operationsPerThread * 3) + " (" + (threadCount * operationsPerThread) + " puts, " + (threadCount * operationsPerThread * 2) + " gets)");
        System.out.println("Successful puts: " + successfulPuts.get());
        System.out.println("Successful gets (non-null): " + successfulGets.get());
        System.out.println("Final cache size: " + cache.size());
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
