import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

/*
 * Here's a basic Java code that demonstrates thread pooling:
 * * In this code, we have created a fixed size thread pool of 5 threads using the
 * Executors.newFixedThreadPool method. We then submit 10 tasks to the executor service,
 * which are executed by the available threads in the pool. When all tasks are completed,
 * we shut down the executor service to release its resources.
 * * Extensions:
 * 1. Returning values from tasks using Callable and Future.
 * 2. Handling exceptions thrown by tasks.
 * 3. Using a ScheduledExecutorService for delayed and periodic tasks.
 * 4. Graceful shutdown with timeout.
 * 5. Dynamic thread pool sizing.
 * */

public class ThreadPoolExtended {

    static class Task implements Runnable {
        private int taskNumber;

        public Task(int taskNumber) {
            this.taskNumber = taskNumber;
        }

        public void run() {
            System.out.println("Task " + taskNumber + " is running");
            try {
                Thread.sleep(1000);
                if (taskNumber == 3) {
                    throw new RuntimeException("Task 3 failed!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableTask implements Callable<String> {
        private int taskNumber;

        public CallableTask(int taskNumber) {
            this.taskNumber = taskNumber;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return "Result from task " + taskNumber;
        }
    }

    public static void main(String[] args) {
        // 1. Returning Values and Exception Handling
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            futures.add(executorService.submit(new CallableTask(i)));
            executorService.execute(new Task(i));
        }

        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 2. Handling exceptions from Runnable tasks.
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Tasks interrupted");
        } finally {
            if (!executorService.isTerminated()) {
                System.err.println("Cancel non-finished tasks");
            }
            executorService.shutdownNow();
            System.out.println("Shutdown finished");
        }

        // 3. ScheduledExecutorService
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("Periodic task running"), 1, 3, TimeUnit.SECONDS);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        scheduledExecutorService.shutdown();

        try (// 4. Dynamic thread pool sizing using ThreadPoolExecutor
		ThreadPoolExecutor dynamicExecutor = new ThreadPoolExecutor(
                2, // core pool size
                4, // max pool size
                30, // keep-alive time
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        )) {
			for(int i = 0; i < 8; i++){
			    dynamicExecutor.execute(new Task(i));
			}

			dynamicExecutor.shutdown();

			try {
			    dynamicExecutor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e){
			    e.printStackTrace();
			}
		}

        System.out.println("Main thread finished.");
    }
}
