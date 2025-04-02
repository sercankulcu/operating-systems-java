package interactive;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler {
	
	// Process class to represent each process with its properties
	class Process {
	    String name;           // Process identifier
	    int totalTime;        // Total execution time required
	    int remainingTime;    // Remaining execution time
	    int arrivalTime;      // Time when process arrives
	    int executedTime;     // Time already executed

	    public Process(String name, int totalTime, int arrivalTime) {
	        this.name = name;
	        this.totalTime = totalTime;
	        this.remainingTime = totalTime;
	        this.arrivalTime = arrivalTime;
	        this.executedTime = 0;
	    }
	}
	
    private Queue<Process> readyQueue;    // Queue for ready processes
    private int timeQuantum;             // Fixed time slice for each process
    private int currentTime;             // Current system time
    
    public RoundRobinScheduler(int timeQuantum) {
        this.readyQueue = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        this.currentTime = 0;
    }

    // Add a process to the scheduler
    public void addProcess(String name, int totalTime, int arrivalTime) {
        Process p = new Process(name, totalTime, arrivalTime);
        readyQueue.add(p);
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Round Robin Scheduling with Quantum " + timeQuantum + "...");

        while (!readyQueue.isEmpty()) {
            Process current = readyQueue.poll();

            // Skip if process hasn't arrived yet
            if (current.arrivalTime > currentTime) {
                readyQueue.add(current);
                currentTime++;
                continue;
            }

            // Calculate execution time for this round
            int executionTime = Math.min(timeQuantum, current.remainingTime);

            // Execute the process
            current.executedTime += executionTime;
            current.remainingTime -= executionTime;
            currentTime += executionTime;

            // Check if process completed
            if (current.remainingTime > 0) {
                // Return to queue if not finished
                readyQueue.add(current);
                System.out.printf("Time %d: %s preempted (Remaining: %d)\n",
                                currentTime, current.name, current.remainingTime);
            } else {
                System.out.printf("Time %d: %s completed (Total time: %d)\n",
                                currentTime, current.name, current.executedTime);
            }
        }
        System.out.println("Scheduling completed at time " + currentTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        // Create scheduler with 4-unit time quantum
    	int timeQuantum = 2;
        RoundRobinScheduler scheduler = new RoundRobinScheduler(timeQuantum);

        // Add processes: name, total time, arrival time
        scheduler.addProcess("P1", 10, 0);
        scheduler.addProcess("P2", 6, 1);
        scheduler.addProcess("P3", 8, 2);
        scheduler.addProcess("P4", 7, 3);

        // Run the scheduler
        scheduler.schedule();
    }
}