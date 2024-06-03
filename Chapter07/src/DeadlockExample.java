/*
 * Here is an example of how you can create a deadlock in Java using two threads and two objects:
 * 
 * To cause a deadlock in Java, you can create multiple threads that try to acquire locks on 
 * multiple objects in different order, resulting in a circular wait.
 * 
 * This code creates two threads, thread1 and thread2, that try to acquire the lock on object1 
 * and object2 respectively. However, thread1 also tries to acquire the lock on object2 after 
 * acquiring the lock on object1, and thread2 tries to acquire the lock on object1 after 
 * acquiring the lock on object2, resulting in a circular wait.
 * 
 * */

public class DeadlockExample {

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
	}
}
