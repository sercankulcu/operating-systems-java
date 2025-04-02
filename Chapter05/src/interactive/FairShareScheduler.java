package interactive;
import java.util.ArrayList;
import java.util.List;

public class FairShareScheduler {

	// Process class to represent each process with its properties
	class Process {
		String name;           // Process identifier
		int totalTime;         // Total execution time required
		int remainingTime;     // Remaining execution time
		double share;          // Fair share percentage
		int executedTime;      // Time already executed

		public Process(String name, int totalTime, double share) {
			this.name = name;
			this.totalTime = totalTime;
			this.remainingTime = totalTime;
			this.share = share;
			this.executedTime = 0;
		}
	}

	private List<Process> processes;    // List of all processes
	private int timeQuantum;           // Time slice for each execution
	
	public FairShareScheduler(int timeQuantum) {
		this.processes = new ArrayList<>();
		this.timeQuantum = timeQuantum;
	}

	// Add a process to the scheduler
	public void addProcess(String name, int totalTime, double share) {
		processes.add(new Process(name, totalTime, share));
		totalTime += totalTime;
	}

	// Calculate the time slice for each process based on its share
	private int getProcessTimeSlice(Process p) {
		return (int) (timeQuantum * p.share);
	}

	// Main scheduling algorithm
	public void schedule() {
		System.out.println("Starting Fair Share Scheduling...");
		int currentTime = 0;

		// Continue until all processes are completed
		while (!processes.isEmpty()) {
			List<Process> completed = new ArrayList<>();

			// Process each active process in one round
			for (Process p : processes) {
				int slice = Math.min(getProcessTimeSlice(p), p.remainingTime);

				// Simulate process execution
				System.out.printf("Time %d: Executing %s for %d units (Share: %.2f)\n", 
						currentTime, p.name, slice, p.share);

				p.executedTime += slice;
				p.remainingTime -= slice;
				currentTime += slice;

				// Check if process is completed
				if (p.remainingTime <= 0) {
					System.out.printf("Time %d: %s completed (Total time: %d)\n", 
							currentTime, p.name, p.executedTime);
					completed.add(p);
				}
			}

			// Remove completed processes
			processes.removeAll(completed);

			// If no processes completed and some remain, continue
			if (completed.isEmpty() && !processes.isEmpty()) {
				System.out.println("Time " + currentTime + ": Continuing next round...");
			}
		}
		System.out.println("Scheduling completed at time " + currentTime);
	}

	// Main method to test the scheduler
	public static void main(String[] args) {
		// Create scheduler with 10-unit base time quantum
		FairShareScheduler scheduler = new FairShareScheduler(10);

		// Add processes with name, total time, and fair share percentage
		scheduler.addProcess("P1", 30, 0.5);  // 50% share
		scheduler.addProcess("P2", 20, 0.3);  // 30% share
		scheduler.addProcess("P3", 10, 0.2);  // 20% share

		// Run the scheduler
		scheduler.schedule();
	}
}