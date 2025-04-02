/*
 * Another important concept in operating systems is memory management. In Java, memory management 
 * is handled automatically by the Java Virtual Machine (JVM) through a process called garbage 
 * collection. Here is an example of a Java program that demonstrates how the garbage collector 
 * works: 
 * 
 * In this extended example:
 * - A method createObject() is called repeatedly, creating new Objects
 * - Objects become eligible for garbage collection when no longer referenced
 * - Added memory monitoring features to observe garbage collection effects
 * - Included different object types to show varying memory usage
 * - Added explicit garbage collection requests (though not guaranteed)
 * 
 * The garbage collector runs in the background, freeing memory from unreachable objects.
 * This example demonstrates object creation, reference management, and memory monitoring.
 * */

public class GarbageCollectionExample {
    // Static class to create larger objects for more noticeable memory impact
    static class MemoryHeavyObject {
        private long[] data; // Array to consume more memory
        private String name;
        
        public MemoryHeavyObject(String name) {
            this.data = new long[1000]; // 8KB of data (1000 * 8 bytes)
            this.name = name;
        }
    }
    
    // Counter to track object creations
    private static int objectCount = 0;
    
    public static void main(String[] args) {
        // Get Runtime instance for memory monitoring
        Runtime runtime = Runtime.getRuntime();
        
        // Initial memory snapshot
        System.out.println("=== Garbage Collection Demonstration ===");
        printMemoryStats(runtime, "Initial state");
        
        // Create many objects in a loop
        for (int i = 0; i < 100000; i++) {
            createObject();
            createHeavyObject("HeavyObj" + i);
            
            // Periodically print memory stats and request GC
            if (i % 10000 == 0 && i > 0) {
                System.out.println("\nAfter " + i + " iterations:");
                printMemoryStats(runtime, "Before explicit GC request");
                // Request garbage collection (not guaranteed to run immediately)
                System.gc();
                try {
                    Thread.sleep(100); // Brief pause to allow GC to potentially run
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printMemoryStats(runtime, "After GC request");
            }
        }
        
        // Final memory stats
        System.out.println("\nFinal state after all iterations:");
        printMemoryStats(runtime, "Program end");
        System.out.println("Total objects created: " + objectCount);
    }

    // Method to create a simple Object and make it eligible for GC
    public static void createObject() {
        Object obj = new Object();
        // Simulate some work with the object
        obj.toString();
        // Set to null to make it eligible for garbage collection
        obj = null;
        objectCount++;
    }
    
    // Method to create a larger object to demonstrate memory impact
    public static void createHeavyObject(String name) {
        MemoryHeavyObject heavyObj = new MemoryHeavyObject(name);
        // Simulate some work
        heavyObj.hashCode();
        // Object reference is lost when method ends, making it eligible for GC
        objectCount++;
    }
    
    // Utility method to print current memory statistics
    private static void printMemoryStats(Runtime runtime, String phase) {
        long totalMemory = runtime.totalMemory() / 1024; // Convert to KB
        long freeMemory = runtime.freeMemory() / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println(phase + ":");
        System.out.printf("Total memory: %d KB%n", totalMemory);
        System.out.printf("Free memory: %d KB%n", freeMemory);
        System.out.printf("Used memory: %d KB%n", usedMemory);
    }
}