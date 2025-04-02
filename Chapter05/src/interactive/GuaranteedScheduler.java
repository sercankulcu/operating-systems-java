package interactive;
import java.util.ArrayList;
import java.util.List;

public class GuaranteedScheduler {
	
	// Process class to represent each process with its properties
	class Process {
	    String name;           // Process identifier
	    int totalTime;        // Total execution time required
	    int remainingTime;    // Remaining execution time
	    double guaranteedRate; // Guaranteed service rate (fraction of CPU)
	    double virtualTime;   // Virtual time for scheduling
	    int executedTime;     // Time already executed

	    public Process(String name, int totalTime, double guaranteedRate) {
	        this.name = name;
	        this.totalTime = totalTime;
	        this.remainingTime = totalTime;
	        this.guaranteedRate = guaranteedRate;
	        this.virtualTime = 0.0;
	        this.executedTime = 0;
	    }
	}
	
    private List<Process> processes;    // List of active processes
    private int timeQuantum;           // Base time slice for execution
    private double currentRealTime;    // Current real time in the system

    public GuaranteedScheduler(int timeQuantum) {
        this.processes = new ArrayList<>();
        this.timeQuantum = timeQuantum;
        this.currentRealTime = 0.0;
    }

    // Add a process to the scheduler
    public void addProcess(String name, int totalTime, double guaranteedRate) {
        processes.add(new Process(name, totalTime, guaranteedRate));
    }

    // Find process with minimum virtual time
    private Process getNextProcess() {
        Process next = null;
        double minVirtualTime = Double.MAX_VALUE;

        for (Process p : processes) {
            if (p.remainingTime > 0 && p.virtualTime < minVirtualTime) {
                minVirtualTime = p.virtualTime;
                next = p;
            }
        }
        return next;
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Guaranteed Scheduling...");
        
        while (!processes.isEmpty()) {
            Process p = getNextProcess();
            
            // If no process found, all are completed
            if (p == null) {
                break;
            }

            // Calculate actual time slice (minimum of quantum and remaining time)
            int slice = Math.min(timeQuantum, p.remainingTime);
            
            // Execute the process
            System.out.printf("Real Time %.1f: Executing %s for %d units " +
                            "(Guaranteed Rate: %.2f, Virtual Time: %.1f)\n",
                            currentRealTime, p.name, slice, p.guaranteedRate, p.virtualTime);

            // Update process state
            p.executedTime += slice;
            p.remainingTime -= slice;
            currentRealTime += slice;
            
            // Update virtual time based on guaranteed rate
            p.virtualTime += (double) slice / p.guaranteedRate;

            // Remove process if completed
            if (p.remainingTime <= 0) {
                System.out.printf("Real Time %.1f: %s completed (Total time: %d)\n",
                                currentRealTime, p.name, p.executedTime);
                processes.remove(p);
            }
        }
        System.out.printf("Scheduling completed at real time %.1f\n", currentRealTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        // Create scheduler with 5-unit time quantum
        GuaranteedScheduler scheduler = new GuaranteedScheduler(5);

        // Add processes with name, total time, and guaranteed rate
        scheduler.addProcess("P1", 20, 0.5);  // 50% of CPU guaranteed
        scheduler.addProcess("P2", 14, 0.3);  // 30% of CPU guaranteed
        scheduler.addProcess("P3", 11, 0.2);  // 20% of CPU guaranteed

        // Run the scheduler
        scheduler.schedule();
    }
}