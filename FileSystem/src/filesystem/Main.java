package filesystem;

import filesystem.models.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static void concurrentTest(FileSystem fs){
         System.out.println("=== File System Concurrent Testing ===\n");

         // Setup initial folder structure
         System.out.println("Setting up initial folders...");
         try {
             fs.createFolder("/home");
             fs.createFolder("/var");
             fs.createFolder("/tmp");
             System.out.println("✓ Initial folders created\n");
         } catch (Exception e) {
             System.out.println("✗ Error in setup: " + e.getMessage() + "\n");
         }

         int numThreads = 20;
         CountDownLatch startLatch = new CountDownLatch(1);
         CountDownLatch endLatch = new CountDownLatch(numThreads);
         ExecutorService service = Executors.newFixedThreadPool(numThreads); 
         AtomicInteger successCount = new AtomicInteger(0);
         AtomicInteger failureCount = new AtomicInteger(0);

         // Test 1: Concurrent file creation in different folders
         System.out.println("Test 1: Concurrent file creation in different folders...");
         for (int i = 0; i < numThreads / 2; i++) {
             final int threadId = i;
             service.submit(() -> {
                 try {
                     startLatch.await();
                     String folderPath = "/home/user" + threadId;
                     fs.createFolder(folderPath);
                     fs.createFile(folderPath + "/file.txt", "Data from thread " + threadId);
                     successCount.incrementAndGet();
                 } catch (Exception e) {
                     failureCount.incrementAndGet();
                 } finally {
                     endLatch.countDown();
                 }
             });
         }

         // Test 2: Concurrent file creation in same folder (should have conflicts)
         for (int i = numThreads / 2; i < numThreads; i++) {
             final int threadId = i;
             service.submit(() -> {
                 try {
                     startLatch.await();
                     fs.createFile("/var/file" + threadId + ".txt", "Data " + threadId);
                     successCount.incrementAndGet();
                 } catch (Exception e) {
                     failureCount.incrementAndGet();
                 } finally {
                     endLatch.countDown();
                 }
             });
         }

         try {
             startLatch.countDown(); // Start all threads
             endLatch.await(); // Wait for completion
             System.out.println("✓ Concurrent operations completed");
             System.out.println("  Success: " + successCount.get());
             System.out.println("  Failures: " + failureCount.get() + "\n");
         } catch (InterruptedException e) {
             System.out.println("✗ Test interrupted\n");
         }

         // Test 3: Concurrent reads
         System.out.println("Test 2: Concurrent reads (should all succeed)...");
         CountDownLatch readStart = new CountDownLatch(1);
         CountDownLatch readEnd = new CountDownLatch(10);
         AtomicInteger readSuccess = new AtomicInteger(0);
         
         for (int i = 0; i < 10; i++) {
             service.submit(() -> {
                 try {
                     readStart.await();
                     fs.list("/var");
                     readSuccess.incrementAndGet();
                 } catch (Exception e) {
                     // Should not fail
                 } finally {
                     readEnd.countDown();
                 }
             });
         }

         try {
             readStart.countDown();
             readEnd.await();
             System.out.println("✓ Concurrent reads completed: " + readSuccess.get() + "/10 successful\n");
         } catch (InterruptedException e) {
             System.out.println("✗ Read test interrupted\n");
         }

         // Test 4: Mixed operations on different paths
         System.out.println("Test 3: Mixed read/write operations on different paths...");
         CountDownLatch mixedStart = new CountDownLatch(1);
         CountDownLatch mixedEnd = new CountDownLatch(10);
         AtomicInteger mixedSuccess = new AtomicInteger(0);

         for (int i = 0; i < 5; i++) {
             final int id = i;
             service.submit(() -> {
                 try {
                     mixedStart.await();
                     fs.createFile("/tmp/file" + id + ".txt", "Data");
                     mixedSuccess.incrementAndGet();
                 } catch (Exception e) {
                     // Handle conflict
                 } finally {
                     mixedEnd.countDown();
                 }
             });
         }

         for (int i = 0; i < 5; i++) {
             service.submit(() -> {
                 try {
                     mixedStart.await();
                     fs.list("/home");
                     mixedSuccess.incrementAndGet();
                 } catch (Exception e) {
                     // Should not fail
                 } finally {
                     mixedEnd.countDown();
                 }
             });
         }

         try {
             mixedStart.countDown();
             mixedEnd.await();
             System.out.println("✓ Mixed operations completed: " + mixedSuccess.get() + "/10 successful\n");
         } catch (InterruptedException e) {
             System.out.println("✗ Mixed test interrupted\n");
         }

         // Test 5: Concurrent move operations
         System.out.println("Test 4: Concurrent rename operations...");
         try {
             fs.createFolder("/tmp/rename_test");
             for (int i = 0; i < 5; i++) {
                 fs.createFile("/tmp/rename_test/file" + i + ".txt", "Data");
             }
         } catch (Exception e) {
             System.out.println("Setup error: " + e.getMessage());
         }

         CountDownLatch renameStart = new CountDownLatch(1);
         CountDownLatch renameEnd = new CountDownLatch(5);
         AtomicInteger renameSuccess = new AtomicInteger(0);

         for (int i = 0; i < 5; i++) {
             final int id = i;
             service.submit(() -> {
                 try {
                     renameStart.await();
                     fs.rename("/tmp/rename_test/file" + id + ".txt", "renamed" + id + ".txt");
                     renameSuccess.incrementAndGet();
                 } catch (Exception e) {
                     // Handle error
                 } finally {
                     renameEnd.countDown();
                 }
             });
         }

         try {
             renameStart.countDown();
             renameEnd.await();
             System.out.println("✓ Rename operations completed: " + renameSuccess.get() + "/5 successful\n");
         } catch (InterruptedException e) {
             System.out.println("✗ Rename test interrupted\n");
         }

         service.shutdown();
         System.out.println("=== Concurrent testing completed ===\n");
    }

    private static void regularTest(FileSystem fs){
         System.out.println("=== File System Testing ===\n");
        
        // Test 1: Create folders
        System.out.println("1. Creating folder structure...");
        try {
            fs.createFolder("/home");
            fs.createFolder("/home/user");
            fs.createFolder("/home/user/documents");
            fs.createFolder("/home/user/downloads");
            fs.createFolder("/var");
            fs.createFolder("/var/log");
            System.out.println("✓ Folders created successfully\n");
        } catch (Exception e) {
            System.out.println("✗ Error creating folders: " + e.getMessage() + "\n");
        }
        
        // Test 2: Create files
        System.out.println("2. Creating files...");
        try {
            fs.createFile("/home/user/documents/readme.txt", "This is a readme file");
            fs.createFile("/home/user/documents/report.pdf", "PDF content here");
            fs.createFile("/home/user/downloads/download.zip", "Zip file data");
            fs.createFile("/var/log/system.log", "System log entries");
            System.out.println("✓ Files created successfully\n");
        } catch (Exception e) {
            System.out.println("✗ Error creating files: " + e.getMessage() + "\n");
        }
        
        // Test 3: List directory contents
        System.out.println("3. Listing directory contents...");
        try {
            System.out.println("Contents of /home/user:");
            List<FileSystemNode> items = fs.list("/home/user");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            
            System.out.println("\nContents of /home/user/documents:");
            items = fs.list("/home/user/documents");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error listing directory: " + e.getMessage() + "\n");
        }
        
        // Test 4: Get file and read data
        System.out.println("4. Reading file content...");
        try {
            FileSystemNode node = fs.get("/home/user/documents/readme.txt");
            if (!node.isDirectory()) {
                File file = (File) node;
                System.out.println("Content of readme.txt: " + file.getData());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error reading file: " + e.getMessage() + "\n");
        }
        
        // Test 5: Rename file
        System.out.println("5. Renaming file...");
        try {
            fs.rename("/home/user/documents/readme.txt", "README.md");
            System.out.println("✓ Renamed readme.txt to README.md");
            System.out.println("Contents of /home/user/documents after rename:");
            List<FileSystemNode> items = fs.list("/home/user/documents");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error renaming file: " + e.getMessage() + "\n");
        }
        
        // Test 6: Move file
        System.out.println("6. Moving file...");
        try {
            fs.move("/home/user/downloads/download.zip", "/home/user/documents/archive.zip");
            System.out.println("✓ Moved download.zip to documents/archive.zip");
            System.out.println("Contents of /home/user/documents:");
            List<FileSystemNode> items = fs.list("/home/user/documents");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error moving file: " + e.getMessage() + "\n");
        }
        
        // Test 7: Get full path
        System.out.println("7. Testing getPath() method...");
        try {
            FileSystemNode node = fs.get("/home/user/documents/README.md");
            System.out.println("Full path: " + node.getPath());
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error getting path: " + e.getMessage() + "\n");
        }
        
        // Test 8: Delete file
        System.out.println("8. Deleting file...");
        try {
            fs.delete("/home/user/documents/report.pdf");
            System.out.println("✓ Deleted report.pdf");
            System.out.println("Contents of /home/user/documents after deletion:");
            List<FileSystemNode> items = fs.list("/home/user/documents");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error deleting file: " + e.getMessage() + "\n");
        }
        
        // Test 9: Delete folder
        System.out.println("9. Deleting folder...");
        try {
            fs.delete("/home/user/downloads");
            System.out.println("✓ Deleted downloads folder");
            System.out.println("Contents of /home/user:");
            List<FileSystemNode> items = fs.list("/home/user");
            for (FileSystemNode item : items) {
                String type = item.isDirectory() ? "[DIR]" : "[FILE]";
                System.out.println("  " + type + " " + item.getName());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error deleting folder: " + e.getMessage() + "\n");
        }
        
        // Test 10: Error handling - duplicate file
        System.out.println("10. Testing error handling (duplicate file)...");
        try {
            fs.createFile("/home/user/documents/README.md", "Duplicate content");
            System.out.println("✗ Should have thrown an error\n");
        } catch (Exception e) {
            System.out.println("✓ Correctly caught error: " + e.getMessage() + "\n");
        }
        
        // Test 11: Error handling - invalid path
        System.out.println("11. Testing error handling (invalid path)...");
        try {
            fs.get("/nonexistent/path");
            System.out.println("✗ Should have thrown an error\n");
        } catch (Exception e) {
            System.out.println("✓ Correctly caught error: " + e.getMessage() + "\n");
        }
        
        // Test 12: Error handling - list file instead of directory
        System.out.println("12. Testing error handling (list file)...");
        try {
            fs.list("/home/user/documents/README.md");
            System.out.println("✗ Should have thrown an error\n");
        } catch (Exception e) {
            System.out.println("✓ Correctly caught error: " + e.getMessage() + "\n");
        }
        
        System.out.println("=== All tests completed ===");
    }
    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        
        // Run regular tests
        regularTest(fs);
        
        // Run concurrent tests
        System.out.println("\n");
        FileSystem fs2 = new FileSystem();
        concurrentTest(fs2);
    }
}
