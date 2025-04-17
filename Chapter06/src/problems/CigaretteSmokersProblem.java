package problems;

import java.util.Random;
import java.util.concurrent.Semaphore;

/*
 * The Cigarette Smokers Problem is a classic synchronization problem in operating systems that illustrates the use of 
 * concurrency control mechanisms to manage shared resources and avoid deadlock or race conditions. The problem involves 
 * three smokers and an agent. Each smoker needs three ingredients to smoke (tobacco, paper, and matches), but each smoker 
 * has only one ingredient and needs the other two. The agent randomly provides two ingredients at a time, and the smokers 
 * must synchronize to ensure that the correct smoker (who has the missing third ingredient) picks up the provided ingredients 
 * to make and smoke a cigarette.
 * 
 * Agent: Randomly places two of the three ingredients (tobacco, paper, match) on the table.
 * Smokers: Three smokers, each with one ingredient:
 *   Tobacco smoker has tobacco, needs paper and match.
 *   Paper smoker has paper, needs tobacco and match.
 *   Match smoker has match, needs tobacco and paper.
 * The correct smoker must pick up the two provided ingredients, make a cigarette, smoke, and signal the agent to provide 
 * new ingredients.
 * */

// Class to demonstrate the Cigarette Smokers Problem using semaphores for synchronization
public class CigaretteSmokersProblem {
	// Semaphores for synchronization
	private static final Semaphore agent = new Semaphore(1); // Controls agent placing ingredients
	private static final Semaphore tobaccoSmoker = new Semaphore(0); // Signals tobacco smoker to proceed
	private static final Semaphore paperSmoker = new Semaphore(0); // Signals paper smoker to proceed
	private static final Semaphore matchSmoker = new Semaphore(0); // Signals match smoker to proceed
	private static final Semaphore mutex = new Semaphore(1); // Ensures mutual exclusion for shared state

	// Shared state to track ingredients on the table
	private static boolean isTobacco = false;
	private static boolean isPaper = false;
	private static boolean isMatch = false;

	// Random number generator for agent to select ingredients
	private static final Random random = new Random();

	// Agent thread: Randomly places two ingredients on the table
	static class Agent extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					// Agent waits to place ingredients
					agent.acquire();
					System.out.println("Agent is placing ingredients...");

					// Randomly select two ingredients to place
					int choice = random.nextInt(3);
					mutex.acquire(); // Lock shared state
					if (choice == 0) { // Place paper and match
						isPaper = true;
						isMatch = true;
						System.out.println("Agent places paper and match");
						tobaccoSmoker.release(); // Signal smoker with tobacco
					} else if (choice == 1) { // Place tobacco and match
						isTobacco = true;
						isMatch = true;
						System.out.println("Agent places tobacco and match");
						paperSmoker.release(); // Signal smoker with paper
					} else { // Place tobacco and paper
						isTobacco = true;
						isPaper = true;
						System.out.println("Agent places tobacco and paper");
						matchSmoker.release(); // Signal smoker with match
					}
					mutex.release(); // Unlock shared state
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
	}

	// Smoker with tobacco: Needs paper and match
	static class TobaccoSmoker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					// Wait for paper and match
					tobaccoSmoker.acquire();
					mutex.acquire(); // Lock shared state
					if (isPaper && isMatch) {
						// Take ingredients and smoke
						isPaper = false;
						isMatch = false;
						System.out.println("Tobacco smoker takes paper and match, makes cigarette, and smokes");
						mutex.release(); // Unlock shared state
						smoke();
						agent.release(); // Signal agent to place new ingredients
					} else {
						mutex.release(); // Unlock if ingredients not available
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		private void smoke() throws InterruptedException {
			System.out.println("Tobacco smoker is smoking...");
			Thread.sleep(1000); // Simulate smoking time
		}
	}

	// Smoker with paper: Needs tobacco and match
	static class PaperSmoker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					// Wait for tobacco and match
					paperSmoker.acquire();
					mutex.acquire(); // Lock shared state
					if (isTobacco && isMatch) {
						// Take ingredients and smoke
						isTobacco = false;
						isMatch = false;
						System.out.println("Paper smoker takes tobacco and match, makes cigarette, and smokes");
						mutex.release(); // Unlock shared state
						smoke();
						agent.release(); // Signal agent to place new ingredients
					} else {
						mutex.release(); // Unlock if ingredients not available
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		private void smoke() throws InterruptedException {
			System.out.println("Paper smoker is smoking...");
			Thread.sleep(1000); // Simulate smoking time
		}
	}

	// Smoker with match: Needs tobacco and paper
	static class MatchSmoker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					// Wait for tobacco and paper
					matchSmoker.acquire();
					mutex.acquire(); // Lock shared state
					if (isTobacco && isPaper) {
						// Take ingredients and smoke
						isTobacco = false;
						isPaper = false;
						System.out.println("Match smoker takes tobacco and paper, makes cigarette, and smokes");
						mutex.release(); // Unlock shared state
						smoke();
						agent.release(); // Signal agent to place new ingredients
					} else {
						mutex.release(); // Unlock if ingredients not available
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}

		private void smoke() throws InterruptedException {
			System.out.println("Match smoker is smoking...");
			Thread.sleep(1000); // Simulate smoking time
		}
	}

	// Main method to set up and run the simulation
	public static void main(String[] args) {
		System.out.println("Starting Cigarette Smokers Problem Simulation...");

		// Create and start threads
		Agent agentThread = new Agent();
		TobaccoSmoker tobaccoSmokerThread = new TobaccoSmoker();
		PaperSmoker paperSmokerThread = new PaperSmoker();
		MatchSmoker matchSmokerThread = new MatchSmoker();

		agentThread.start();
		tobaccoSmokerThread.start();
		paperSmokerThread.start();
		matchSmokerThread.start();

		// Run for a fixed duration, then interrupt threads
		try {
			Thread.sleep(10000); // Run for 10 seconds
			agentThread.interrupt();
			tobaccoSmokerThread.interrupt();
			paperSmokerThread.interrupt();
			matchSmokerThread.interrupt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Simulation ended.");
	}
}