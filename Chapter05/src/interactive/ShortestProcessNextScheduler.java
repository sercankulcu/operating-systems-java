package interactive;
import java.util.PriorityQueue;
import java.util.Comparator;

public class ShortestProcessNextScheduler {
	
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
	
    private PriorityQueue<Process> readyQueue;    // Priority queue for processes
    private int currentTime;                     // Current system time
    private int iterationCount;                  // Track number of iterations

    // Custom comparator for shortest process next (based on total time)
    private static final Comparator<Process> SPN_COMPARATOR = 
        (p1, p2) -> {
            // Compare by total time first
            int timeDiff = p1.totalTime - p2.totalTime;
            if (timeDiff == 0) {
                // If equal times, use arrival time as tiebreaker
                return p1.arrivalTime - p2.arrivalTime;
            }
            return timeDiff;
        };

    public ShortestProcessNextScheduler() {
        this.readyQueue = new PriorityQueue<>(SPN_COMPARATOR);
        this.currentTime = 0;
        this.iterationCount = 0;
    }

    // Add a process to the scheduler
    public void addProcess(String name, int totalTime, int arrivalTime) {
        Process p = new Process(name, totalTime, arrivalTime);
        readyQueue.add(p);
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Shortest Process Next Scheduling...");

        while (!readyQueue.isEmpty()) {
            iterationCount++;
            
            // Find the shortest process that has arrived
            Process shortest = null;
            for (Process p : readyQueue) {
                if (p.arrivalTime <= currentTime) {
                    shortest = p;
                    break; // First available process in priority queue is shortest
                }
            }
            
            currentTime++;

            if (shortest == null) {
                // No process has arrived yet
                continue;
            }

            shortest.executedTime += 1;
            shortest.remainingTime -= 1;
            
            // Remove the shortest process and execute it
            if(shortest.remainingTime <= 0)
            	readyQueue.remove(shortest);

            System.out.printf("Time %d: %s running (Total time: %d)\n",
                                currentTime, shortest.name, shortest.executedTime);
        }
        System.out.println("Scheduling completed at time " + currentTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        ShortestProcessNextScheduler scheduler = new ShortestProcessNextScheduler();
        
        // Add processes: name, total time, arrival time
        scheduler.addProcess("P1", 10, 0);  // Longest
        scheduler.addProcess("P2", 4, 1);   // Shortest
        scheduler.addProcess("P3", 7, 2);   // Medium
        
        // Run the scheduler
        scheduler.schedule();
    }
}