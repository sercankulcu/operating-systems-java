package interactive;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MultiLevelQueueScheduler {
	
	// Process class to represent each process with its properties
	class Process {
	    String name;           // Process identifier
	    int totalTime;        // Total execution time required
	    int remainingTime;    // Remaining execution time
	    int priority;         // Priority level (0 = highest, increases for lower priority)
	    int executedTime;     // Time already executed

	    public Process(String name, int totalTime, int priority) {
	        this.name = name;
	        this.totalTime = totalTime;
	        this.remainingTime = totalTime;
	        this.priority = priority;
	        this.executedTime = 0;
	    }
	}
	
    private List<Queue<Process>> queues;    // List of queues for different priority levels
    private int[] timeQuanta;              // Time quantum for each priority level
    private int maxPriorityLevels;         // Number of priority levels
    private int currentTime;               // Current system time

    public MultiLevelQueueScheduler(int maxPriorityLevels, int[] timeQuanta) {
        this.maxPriorityLevels = maxPriorityLevels;
        this.timeQuanta = timeQuanta;
        this.queues = new ArrayList<>(maxPriorityLevels);
        this.currentTime = 0;

        // Initialize queues for each priority level
        for (int i = 0; i < maxPriorityLevels; i++) {
            queues.add(new LinkedList<>());
        }
    }

    // Add a process to appropriate priority queue
    public void addProcess(String name, int totalTime, int priority) {
        if (priority < 0 || priority >= maxPriorityLevels) {
            throw new IllegalArgumentException("Invalid priority level");
        }
        Process p = new Process(name, totalTime, priority);
        queues.get(priority).add(p);
    }

    // Check if all queues are empty
    private boolean allQueuesEmpty() {
        for (Queue<Process> queue : queues) {
            if (!queue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Multiple Queue Scheduling...");

        while (!allQueuesEmpty()) {
            // Process each priority level from highest (0) to lowest
            for (int priority = 0; priority < maxPriorityLevels; priority++) {
                Queue<Process> currentQueue = queues.get(priority);
                int quantum = timeQuanta[priority];

                // Process all tasks in current queue
                while (!currentQueue.isEmpty()) {
                    Process p = currentQueue.poll();
                    
                    // Calculate execution time
                    int slice = Math.min(quantum, p.remainingTime);
                    
                    // Execute the process
                    System.out.printf("Time %d: Executing %s for %d units " +
                                    "(Priority: %d, Quantum: %d)\n",
                                    currentTime, p.name, slice, priority, quantum);

                    // Update process state
                    p.executedTime += slice;
                    p.remainingTime -= slice;
                    currentTime += slice;

                    // Check if process completed
                    if (p.remainingTime > 0) {
                        // Return to queue if not finished
                        currentQueue.add(p);
                    } else {
                        System.out.printf("Time %d: %s completed (Total time: %d)\n",
                                        currentTime, p.name, p.executedTime);
                    }
                }
            }
        }
        System.out.println("Scheduling completed at time " + currentTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        // Define 3 priority levels with different time quanta
        int[] timeQuanta = {5, 3, 2};  // Higher priority gets larger quantum
        MultiLevelQueueScheduler scheduler = new MultiLevelQueueScheduler(3, timeQuanta);

        // Add processes: name, total time, priority (0 = highest)
        scheduler.addProcess("P1", 15, 0);  // High priority
        scheduler.addProcess("P2", 10, 0);  // High priority
        scheduler.addProcess("P3", 20, 1);  // Medium priority
        scheduler.addProcess("P4", 12, 2);  // Low priority
        scheduler.addProcess("P5", 15, 2);  // Low priority

        // Run the scheduler
        scheduler.schedule();
    }
}