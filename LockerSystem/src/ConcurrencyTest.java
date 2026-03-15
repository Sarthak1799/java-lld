import lockersystem.enums.PackageSize;
import lockersystem.models.*;
import lockersystem.errors.LockerSystemException;
import lockersystem.LockerSystem;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

public class ConcurrencyTest {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Locker System Concurrency Tests =====\n");
        
        testConcurrentPackageAssignment();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        testConcurrentPickup();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        testMixedConcurrentOperations();
    }
    
    /**
     * Test 1: Multiple threads trying to assign packages concurrently
     */
    public static void testConcurrentPackageAssignment() throws InterruptedException {
        System.out.println("Test 1: Concurrent Package Assignment");
        System.out.println("--------------------------------------");
        
        // Create a fresh locker system
        LockerSystem lockerSystem = LockerSystem.getInstance();
        
        // Add 10 compartments
        for (int i = 0; i < 3; i++) {
            lockerSystem.addCompartment(new Compartment(PackageSize.SMALL));
        }
        for (int i = 0; i < 4; i++) {
            lockerSystem.addCompartment(new Compartment(PackageSize.MEDIUM));
        }
        for (int i = 0; i < 3; i++) {
            lockerSystem.addCompartment(new Compartment(PackageSize.LARGE));
        }
        System.out.println("Initialized with 10 compartments (3 Small, 4 Medium, 3 Large)\n");
        
        // Create thread pool
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        AssignStrategy strategy = new DefaultSizeBasedStrategy();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        List<String> tokens = Collections.synchronizedList(new ArrayList<>());
        
        System.out.println("Launching " + threadCount + " threads to assign packages...");
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Each thread tries to assign a package
                    PackageSize size = PackageSize.values()[threadId % 3];
                    lockersystem.models.Package pack = new lockersystem.models.Package(
                        size, 
                        "Package from Thread-" + threadId
                    );
                    
                    Compartment compartment = lockerSystem.findCompartment(pack, strategy);
                    String token = lockerSystem.assignCompartment(compartment, pack);
                    
                    tokens.add(token);
                    successCount.incrementAndGet();
                    System.out.println("Thread-" + threadId + ": Successfully assigned " + size + " package");
                    
                } catch (LockerSystemException e) {
                    failureCount.incrementAndGet();
                    System.out.println("Thread-" + threadId + ": Failed - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        latch.await();
        executor.shutdown();
        
        System.out.println("\nResults:");
        System.out.println("  Successful assignments: " + successCount.get());
        System.out.println("  Failed assignments: " + failureCount.get());
        System.out.println("  Expected: Max 10 successes (limited by compartments)");
        System.out.println("  Tokens generated: " + tokens.size());
    }
    
    /**
     * Test 2: Multiple threads trying to pick up packages concurrently
     */
    public static void testConcurrentPickup() throws InterruptedException {
        System.out.println("Test 2: Concurrent Package Pickup");
        System.out.println("----------------------------------");
        
        // Create a fresh locker system with fresh singleton instance
        LockerSystem lockerSystem = LockerSystem.getInstance();
        
        // Add compartments
        for (int i = 0; i < 10; i++) {
            lockerSystem.addCompartment(new Compartment(PackageSize.MEDIUM));
        }
        System.out.println("Initialized with 10 medium compartments\n");
        
        // Assign packages and collect tokens
        AssignStrategy strategy = new DefaultSizeBasedStrategy();
        List<String> tokens = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            lockersystem.models.Package pack = new lockersystem.models.Package(
                PackageSize.MEDIUM, 
                "Package-" + i
            );
            try {
                Compartment compartment = lockerSystem.findCompartment(pack, strategy);
                String token = lockerSystem.assignCompartment(compartment, pack);
                tokens.add(token);
            } catch (LockerSystemException e) {
                System.err.println("Setup error: " + e.getMessage());
            }
        }
        System.out.println("Assigned 10 packages\n");
        
        // Now try to pick them up concurrently
        int threadCount = 20; // More threads than tokens
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        System.out.println("Launching " + threadCount + " threads to pickup packages (only 10 tokens available)...");
        
        // Each thread tries to pick a package
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Use token if available, or use invalid token
                    String token = threadId < tokens.size() ? tokens.get(threadId) : "invalid-token-" + threadId;
                    
                    lockersystem.models.Package pack = lockerSystem.pickPackage(token);
                    successCount.incrementAndGet();
                    System.out.println("Thread-" + threadId + ": Successfully picked up package: " + pack.getData());
                    
                } catch (LockerSystemException e) {
                    failureCount.incrementAndGet();
                    System.out.println("Thread-" + threadId + ": Failed - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        latch.await();
        executor.shutdown();
        
        System.out.println("\nResults:");
        System.out.println("  Successful pickups: " + successCount.get());
        System.out.println("  Failed pickups: " + failureCount.get());
        System.out.println("  Expected: Exactly 10 successes (limited by tokens)");
    }
    
    /**
     * Test 3: Mixed concurrent operations (assign and pickup)
     */
    public static void testMixedConcurrentOperations() throws InterruptedException {
        System.out.println("Test 3: Mixed Concurrent Operations");
        System.out.println("------------------------------------");
        
        // Create a fresh locker system
        LockerSystem lockerSystem = LockerSystem.getInstance();
        
        // Add compartments
        for (int i = 0; i < 15; i++) {
            lockerSystem.addCompartment(new Compartment(PackageSize.MEDIUM));
        }
        System.out.println("Initialized with 15 medium compartments\n");
        
        AssignStrategy strategy = new DefaultSizeBasedStrategy();
        ConcurrentLinkedQueue<String> tokens = new ConcurrentLinkedQueue<>();
        
        int assignThreads = 15;
        int pickupThreads = 10;
        int totalThreads = assignThreads + pickupThreads;
        
        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch assignLatch = new CountDownLatch(assignThreads);
        CountDownLatch pickupLatch = new CountDownLatch(pickupThreads);
        
        AtomicInteger assignSuccess = new AtomicInteger(0);
        AtomicInteger assignFailure = new AtomicInteger(0);
        AtomicInteger pickupSuccess = new AtomicInteger(0);
        AtomicInteger pickupFailure = new AtomicInteger(0);
        
        System.out.println("Launching " + assignThreads + " assignment threads and " + pickupThreads + " pickup threads...\n");
        
        // Start assignment threads
        for (int i = 0; i < assignThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Small delay to stagger operations
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50));
                    
                    lockersystem.models.Package pack = new lockersystem.models.Package(
                        PackageSize.MEDIUM, 
                        "Concurrent-Package-" + threadId
                    );
                    
                    Compartment compartment = lockerSystem.findCompartment(pack, strategy);
                    String token = lockerSystem.assignCompartment(compartment, pack);
                    
                    tokens.add(token);
                    assignSuccess.incrementAndGet();
                    System.out.println("ASSIGN Thread-" + threadId + ": Success");
                    
                } catch (LockerSystemException e) {
                    assignFailure.incrementAndGet();
                    System.out.println("ASSIGN Thread-" + threadId + ": Failed - " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    assignLatch.countDown();
                }
            });
        }
        
        // Start pickup threads (they will wait for tokens)
        for (int i = 0; i < pickupThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Wait a bit for some tokens to be generated
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                    
                    String token = tokens.poll();
                    if (token == null) {
                        pickupFailure.incrementAndGet();
                        System.out.println("PICKUP Thread-" + threadId + ": No token available");
                    } else {
                        lockersystem.models.Package pack = lockerSystem.pickPackage(token);
                        pickupSuccess.incrementAndGet();
                        System.out.println("PICKUP Thread-" + threadId + ": Retrieved " + pack.getData());
                    }
                    
                } catch (LockerSystemException e) {
                    pickupFailure.incrementAndGet();
                    System.out.println("PICKUP Thread-" + threadId + ": Failed - " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    pickupLatch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        assignLatch.await();
        pickupLatch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Final Results:");
        System.out.println("  Assignment successes: " + assignSuccess.get());
        System.out.println("  Assignment failures: " + assignFailure.get());
        System.out.println("  Pickup successes: " + pickupSuccess.get());
        System.out.println("  Pickup failures: " + pickupFailure.get());
        System.out.println("  Tokens remaining: " + tokens.size());
        System.out.println("\nConcurrency test completed!");
    }
}
