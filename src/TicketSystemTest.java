import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketSystemTest {

    public static void main(String[] args) throws InterruptedException {
        // Test each synchronization mechanism
        testSynchronizationMechanism(SynchronizationMechanism.SYNCHRONIZED);
        testSynchronizationMechanism(SynchronizationMechanism.REENTRANT_LOCK);
        testSynchronizationMechanism(SynchronizationMechanism.BLOCKING_QUEUE);

        // Run performance comparison
        comparePerformance();
    }

    /**
     * Tests the functionality and thread-safety of a specific synchronization mechanism.
     *
     * @param mechanism the synchronization mechanism to test
     */
    private static void testSynchronizationMechanism(SynchronizationMechanism mechanism) throws InterruptedException {
        System.out.println("\n====== Testing " + mechanism + " ======");

        // Create a TicketPoolManager with 100 tickets and set its synchronization strategy
        TicketPoolManager pool = new TicketPoolManager(100);
        pool.switchSynchronizationMechanism(mechanism);

        // Test 1: Basic functionality - add and purchase tickets
        System.out.println("\nTesting basic add/purchase:");
        boolean addResult = pool.addTicket("Test Ticket 1");
        System.out.println("Add result: " + addResult);
        System.out.println("Available tickets: " + pool.getAvailableTickets());

        String purchased = pool.purchaseTicket();
        System.out.println("Purchased: " + purchased);
        System.out.println("Available tickets after purchase: " + pool.getAvailableTickets());

        // Test 2: Concurrent operations
        System.out.println("\nTesting concurrent operations:");
        ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool
        CountDownLatch latch = new CountDownLatch(20); // Waits for all tasks to finish
        AtomicInteger addedCount = new AtomicInteger(0); // Tracks added tickets
        AtomicInteger purchasedCount = new AtomicInteger(0); // Tracks purchased tickets

        // Submit 10 producer tasks
        for (int i = 0; i < 10; i++) {
            final int producerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        if (pool.addTicket("Producer-" + producerId + "-Ticket-" + j)) {
                            addedCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Submit 10 consumer tasks
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        try {
                            String ticket = pool.purchaseTicket();
                            if (ticket != null) {
                                purchasedCount.incrementAndGet();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all producers and consumers to finish
        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Added tickets: " + addedCount.get());
        System.out.println("Purchased tickets: " + purchasedCount.get());
        System.out.println("Remaining tickets: " + pool.getAvailableTickets());

        // Validate correctness of remaining tickets
        boolean isCorrect = (addedCount.get() - purchasedCount.get() == pool.getAvailableTickets());
        System.out.println("Counts match: " + isCorrect);
    }

    /**
     * Compares the performance of different synchronization mechanisms
     * by running high-load ticket add/purchase operations.
     */
    private static void comparePerformance() throws InterruptedException {
        System.out.println("\n====== Performance Comparison ======");

        int numThreads = 100;
        int operationsPerThread = 1000;

        List<Long> syncTimes = new ArrayList<>();
        List<Long> lockTimes = new ArrayList<>();
        List<Long> queueTimes = new ArrayList<>();

        // Run three iterations for each mechanism to average out noise
        for (int iter = 0; iter < 3; iter++) {
            System.out.println("\nIteration " + (iter + 1));

            // Test with 'synchronized'
            TicketPoolManager syncPool = new TicketPoolManager(numThreads * operationsPerThread);
            syncPool.switchSynchronizationMechanism(SynchronizationMechanism.SYNCHRONIZED);
            long syncTime = measurePerformance(syncPool, numThreads, operationsPerThread);
            syncTimes.add(syncTime);
            System.out.println("Synchronized time: " + syncTime + " ms");

            // Test with 'ReentrantLock'
            TicketPoolManager lockPool = new TicketPoolManager(numThreads * operationsPerThread);
            lockPool.switchSynchronizationMechanism(SynchronizationMechanism.REENTRANT_LOCK);
            long lockTime = measurePerformance(lockPool, numThreads, operationsPerThread);
            lockTimes.add(lockTime);
            System.out.println("ReentrantLock time: " + lockTime + " ms");

            // Test with 'BlockingQueue'
            TicketPoolManager queuePool = new TicketPoolManager(numThreads * operationsPerThread);
            queuePool.switchSynchronizationMechanism(SynchronizationMechanism.BLOCKING_QUEUE);
            long queueTime = measurePerformance(queuePool, numThreads, operationsPerThread);
            queueTimes.add(queueTime);
            System.out.println("BlockingQueue time: " + queueTime + " ms");
        }

        // Calculate average execution time for each mechanism
        double syncAvg = syncTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double lockAvg = lockTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double queueAvg = queueTimes.stream().mapToLong(Long::longValue).average().orElse(0);

        System.out.println("\nPerformance Results (average):");
        System.out.println("Synchronized: " + syncAvg + " ms");
        System.out.println("ReentrantLock: " + lockAvg + " ms");
        System.out.println("BlockingQueue: " + queueAvg + " ms");

        // Determine the fastest mechanism
        String fastest = "Synchronized";
        double fastestTime = syncAvg;

        if (lockAvg < fastestTime) {
            fastest = "ReentrantLock";
            fastestTime = lockAvg;
        }

        if (queueAvg < fastestTime) {
            fastest = "BlockingQueue";
        }

        System.out.println("\nFastest approach: " + fastest);
    }

    /**
     * Measures the time taken to concurrently perform ticket add and purchase operations.
     *
     * @param pool                the TicketPool to test
     * @param numThreads          number of producer and consumer threads
     * @param operationsPerThread number of operations each thread performs
     * @return the time taken in milliseconds
     */
    private static long measurePerformance(TicketPool pool, int numThreads, int operationsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads * 2);
        CountDownLatch latch = new CountDownLatch(numThreads * 2);

        long startTime = System.currentTimeMillis();

        // Submit producer tasks
        for (int i = 0; i < numThreads; i++) {
            final int producerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        pool.addTicket("Producer-" + producerId + "-Ticket-" + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Submit consumer tasks
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        try {
                            pool.purchaseTicket();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to complete
        latch.await();
        long endTime = System.currentTimeMillis();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        return endTime - startTime;
    }

}
