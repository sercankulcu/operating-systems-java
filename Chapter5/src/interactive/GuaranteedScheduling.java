package interactive;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates guaranteed scheduling:
 * 
 * In this example, a queue of processes is created using the LinkedList class. The poll method 
 * is used to remove and return the next process in the queue. The processes are run using 
 * guaranteed scheduling, where each process is given a fixed time slice to execute. If a process 
 * has not completed after its time slice, it is added back to the end of the queue to be run 
 * again later. The total execution time is tracked, and the processes are run until all processes 
 * have completed.
 * */

public class GuaranteedScheduling {

	private static final int NUM_PROCESSES = 5;
	private static final int TIME_SLICE = 2;

	public static void main(String[] args) {
		// Create a queue of processes
		Queue<Process> processes = new LinkedList<>();
		for (int i = 0; i < NUM_PROCESSES; i++) {
			int executionTime = (int) (Math.random() * 10) + 1;
			processes.add(new Process("Process " + i, executionTime));
		}
		// Run the processes using guaranteed scheduling
		int time = 0;
		while (!processes.isEmpty()) {
			Process process = processes.poll();
			int executionTime = Math.min(process.remainingTime, TIME_SLICE);
			System.out.println("Running " + process.name + " (remaining time: " + process.remainingTime + ") for " + executionTime + " seconds");
			time += executionTime;
			process.remainingTime -= executionTime;
			if (process.remainingTime > 0) {
				processes.add(process);
			}
		}
		// Print the total execution time
		System.out.println("Total execution time: " + time + " seconds");
	}

	private static class Process {
		String name;
		int remainingTime;

		Process(String name, int remainingTime) {
			this.name = name;
			this.remainingTime = remainingTime;
		}
	}
}
