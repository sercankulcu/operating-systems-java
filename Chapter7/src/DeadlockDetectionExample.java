import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/*
 * Here is an example of how you can use the ThreadMXBean class to detect deadlocks in Java:
 * 
 * A deadlock is a situation in which two or more threads are blocked and unable to make progress, 
 * because they are waiting for each other to release a resource. Deadlocks can occur in Java when 
 * multiple threads try to acquire locks on multiple objects in different order, resulting in a 
 * circular wait. 
 * 
 * To detect deadlocks in Java, you can use the java.lang.management.ThreadMXBean class and its 
 * findDeadlockedThreads() method, which returns an array of thread IDs for the threads that are 
 * involved in a deadlock.
 * */

public class DeadlockDetectionExample {
	public static void main(String[] args) {

		// Create the two objects
		Object object1 = new Object();
		Object object2 = new Object();

		// Create the two threads
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				// Acquire the lock on object1
				synchronized (object1) {
					try {
						// Sleep for a moment
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Try to acquire the lock on object2
					synchronized (object2) {
						// Do something with object1 and object2
					}
				}
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// Acquire the lock on object2
				synchronized (object2) {
					try {
						// Sleep for a moment
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Try to acquire the lock on object1
					synchronized (object1) {
						// Do something with object1 and object2
					}
				}
			}
		});

		// Start the threads
		thread1.start();
		thread2.start();
		// Get the thread MX bean
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		// Check if the thread contention monitoring is supported
		if (threadMXBean.isThreadContentionMonitoringSupported()) {
			// Enable the thread contention monitoring
			threadMXBean.setThreadContentionMonitoringEnabled(true);

			// Check for deadlocks every second
			while (true) {
				// Find the threads that are involved in a deadlock
				long[] threadIds = threadMXBean.findDeadlockedThreads();
				if (threadIds != null) {
					// Print the deadlock information
					ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);
					System.out.println("Deadlock detected!");
					for (ThreadInfo threadInfo : threadInfos) {
						System.out.println(threadInfo);
					}
				}

				// Sleep for a second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
