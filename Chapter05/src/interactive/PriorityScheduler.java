package interactive;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityScheduler {
	
	// Process class to represent each process with its properties
	class Process {
	    String name;           // Process identifier
	    int totalTime;        // Total execution time required
	    int remainingTime;    // Remaining execution time
	    int priority;         // Priority level (lower number = higher priority)
	    int executedTime;     // Time already executed
	    int arrivalTime;      // Time when process arrives

	    public Process(String name, int totalTime, int priority, int arrivalTime) {
	        this.name = name;
	        this.totalTime = totalTime;
	        this.remainingTime = totalTime;
	        this.priority = priority;
	        this.executedTime = 0;
	        this.arrivalTime = arrivalTime;
	    }
	}
	
    private PriorityQueue<Process> readyQueue;    // Priority queue for processes
    private int currentTime;                     // Current system time

    // Custom comparator for priority queue (lower priority number = higher priority)
    private static final Comparator<Process> PRIORITY_COMPARATOR = 
        (p1, p2) -> {
            // If priorities are equal, use arrival time as tiebreaker
            if (p1.priority == p2.priority) {
                return p1.arrivalTime - p2.arrivalTime;
            }
            return p1.priority - p2.priority;
        };

    public PriorityScheduler() {
        this.readyQueue = new PriorityQueue<>(PRIORITY_COMPARATOR);
        this.currentTime = 0;
    }

    // Add a process to the scheduler
    public void addProcess(String name, int totalTime, int priority, int arrivalTime) {
        Process p = new Process(name, totalTime, priority, arrivalTime);
        readyQueue.add(p);
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Priority Scheduling...");

        while (!readyQueue.isEmpty()) {
            // Get highest priority process (lowest priority number)
            Process current = readyQueue.poll();
            List<Process> toMove = new ArrayList<>();

            // Skip if process hasn't arrived yet
            while (current.arrivalTime > currentTime) {
            	toMove.add(current);
            	current = readyQueue.poll();
            }
            
            readyQueue.addAll(toMove);
            toMove.clear();
            
            currentTime++;

            // Execute process for its full duration (preemptive version could use quantum)
            int executionTime = current.remainingTime;
            
            System.out.printf("Time %d: Executing %s for %d units " +
                            "(Priority: %d, Arrival: %d)\n",
                            currentTime, current.name, 1, 
                            current.priority, current.arrivalTime);

            // Update process state
            current.executedTime += 1;
            current.remainingTime -= 1;
            if(current.remainingTime > 0)
            	readyQueue.add(current);

            // Process is completed
            if (current.remainingTime <= 0) {
                System.out.printf("Time %d: %s completed (Total time: %d)\n",
                                currentTime, current.name, current.executedTime);
            }
        }
        System.out.println("Scheduling completed at time " + currentTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        PriorityScheduler scheduler = new PriorityScheduler();

        // Add processes: name, total time, priority (lower = higher), arrival time
        scheduler.addProcess("P1", 10, 2, 0);  // Medium priority
        scheduler.addProcess("P2", 5, 1, 2);   // High priority
        scheduler.addProcess("P3", 8, 3, 1);   // Low priority
        scheduler.addProcess("P4", 6, 0, 3);   // Highest priority

        // Run the scheduler
        scheduler.schedule();
    }
}