
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Here's a basic Java code that demonstrates thread pooling:
 * 
 * In this code, we have created a fixed size thread pool of 5 threads using the 
 * Executors.newFixedThreadPool method. We then submit 10 tasks to the executor service, 
 * which are executed by the available threads in the pool. When all tasks are completed, 
 * we shut down the executor service to release its resources.
 * 
 * */

public class ThreadPool {
	
    static class Task implements Runnable {
    	
        private int taskNumber;
        public Task(int taskNumber) {
            this.taskNumber = taskNumber;
        }
        public void run() {
            System.out.println("Task " + taskNumber + " is running");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
    	
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Task task = new Task(i);
            executorService.execute(task);
        }
        executorService.shutdown();
    }
}
