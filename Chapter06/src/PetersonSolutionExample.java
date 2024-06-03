/*
 * Here is an example of a Java program that demonstrates Peterson's solution for achieving 
 * mutual exclusion:
 * 
 * In this example, the incrementCounter method increments a shared counter variable. This method 
 * is called by two separate threads, which can potentially access the counter variable 
 * simultaneously. To prevent this, the method uses Peterson's solution to create a critical 
 * region around the code that modifies the counter variable. 
 * 
 * Peterson's solution uses two flags and a turn variable to implement mutual exclusion. The flag 
 * variables are used to indicate that a thread is entering the critical region, and the turn 
 * variable is used to determine which thread should enter the critical region next. The threads 
 * enter a spin loop while waiting for their turn to enter the critical region.
 * */

public class PetersonSolutionExample {

	private static final int N = 2; // Number of threads
	private static volatile boolean[] flag = new boolean[N];
	private static volatile int turn = 0;
	private static int counter;

	public static void main(String[] args) {
		// Create two threads that increment a shared counter
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 1000000; i++) {
				incrementCounter();
			}
		});
		
		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 1000000; i++) {
				incrementCounter();
			}
		});
		// Start the threads
		t1.start();
		t2.start();
		// Wait for the threads to finish
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Print the final value of the counter
		System.out.println("Final value of counter: " + counter);
	}

	private static void incrementCounter() {
		int i = (int) (Thread.currentThread().getId() % N);
		int j = (i + 1) % N;
		flag[i] = true;
		turn = j;
		while (flag[j] && turn == j) {} // Spin loop
		// Critical region
		counter++;
		System.out.println("Counter: " + counter + " i: " + i + " j: " + j);
		flag[i] = false;
	}
}
