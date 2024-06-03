
/*
 * Here's a basic Java code that demonstrates starvation deadlocks
 * 
 * In this code, we have three tasks and three resources. Each task acquires two resources in 
 * a specific order and holds on to them for one second. When all three tasks start executing 
 * concurrently, they all try to acquire their first resource, and the first task that acquires 
 * the first resource will have the chance to acquire the second resource, thus leading to 
 * starvation deadlocks for the other two tasks.
 * 
 * */

public class StarvationDeadlock {
	
    static class Resource {
        private String name;
        public Resource(String name) {
            this.name = name;
        }
    }
    
    static class Task implements Runnable {
        private Resource[] resources;
        public Task(Resource[] resources) {
            this.resources = resources;
        }
        public void run() {
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                synchronized (resource) {
                    System.out.println(Thread.currentThread().getName() + " acquired " + resource.name);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Resource[] resources = new Resource[]{new Resource("Resource 1"), new Resource("Resource 2"), new Resource("Resource 3")};
        
        Task task1 = new Task(new Resource[]{resources[0], resources[1]});
        Task task2 = new Task(new Resource[]{resources[1], resources[2]});
        Task task3 = new Task(new Resource[]{resources[2], resources[0]});
        
        new Thread(task1, "Task 1").start();
        new Thread(task2, "Task 2").start();
        new Thread(task3, "Task 3").start();
    }
}
