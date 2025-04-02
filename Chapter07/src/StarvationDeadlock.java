/*
 * Here's a basic Java code that demonstrates starvation deadlocks
 * 
 * In this code, we have three tasks and three resources. Each task acquires two resources in 
 * a specific order and holds on to them for one second. When all three tasks start executing 
 * concurrently, they all try to acquire their first resource, and the first task that acquires 
 * the first resource will have the chance to acquire the second resource, thus leading to 
 * starvation deadlocks for the other two tasks.
 * 
 * This example showcases how resource contention can lead to starvation scenarios where
 * some threads may never get the chance to execute due to resource acquisition patterns.
 * 
 * The code uses synchronized blocks to control resource access and Thread.sleep() to
 * simulate work being done while holding resources.
 * */

public class StarvationDeadlock {
	
    // Resource class representing a shared resource with a name
    static class Resource {
        private String name;
        
        // Constructor to initialize resource with a name
        public Resource(String name) {
            this.name = name;
        }
    }
    
    // Task class that implements Runnable to simulate concurrent tasks
    static class Task implements Runnable {
        private Resource[] resources;
        
        // Constructor to assign resources needed by this task
        public Task(Resource[] resources) {
            this.resources = resources;
        }
        
        // Run method containing the task's execution logic
        public void run() {
            // Iterate through required resources
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                // Synchronize on the current resource to ensure exclusive access
                synchronized (resource) {
                    // Print which thread acquired which resource
                    System.out.println(Thread.currentThread().getName() + " acquired " + resource.name);
                    try {
                        // Simulate work by sleeping for 1 second while holding the resource
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Handle potential interruption during sleep
                        e.printStackTrace();
                    }
                } // Resource is automatically released when synchronized block ends
            }
        }
    }
    
    // Main method to set up and start the demonstration
    public static void main(String[] args) {
        // Create array of three shared resources
        Resource[] resources = new Resource[]{
            new Resource("Resource 1"), 
            new Resource("Resource 2"), 
            new Resource("Resource 3")
        };
        
        // Create three tasks with different resource acquisition orders
        Task task1 = new Task(new Resource[]{resources[0], resources[1]}); // Needs R1, R2
        Task task2 = new Task(new Resource[]{resources[1], resources[2]}); // Needs R2, R3
        Task task3 = new Task(new Resource[]{resources[2], resources[0]}); // Needs R3, R1
        
        // Start all three tasks concurrently as separate threads
        new Thread(task1, "Task 1").start();
        new Thread(task2, "Task 2").start();
        new Thread(task3, "Task 3").start();
    }
}