package interactive;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryScheduler {
	
	// Process class to represent each process with its properties
	class Process {
	    String name;           // Process identifier
	    int totalTime;        // Total execution time required
	    int remainingTime;    // Remaining execution time
	    int tickets;          // Number of lottery tickets
	    int executedTime;     // Time already executed

	    public Process(String name, int totalTime, int tickets) {
	        this.name = name;
	        this.totalTime = totalTime;
	        this.remainingTime = totalTime;
	        this.tickets = tickets;
	        this.executedTime = 0;
	    }
	}
	
    private List<Process> processes;    // List of active processes
    private int timeQuantum;           // Time slice for each execution
    private Random random;             // Random number generator for lottery
    private int totalTickets;          // Total number of tickets in system

    public LotteryScheduler(int timeQuantum) {
        this.processes = new ArrayList<>();
        this.timeQuantum = timeQuantum;
        this.random = new Random();
        this.totalTickets = 0;
    }

    // Add a process to the scheduler
    public void addProcess(String name, int totalTime, int tickets) {
        processes.add(new Process(name, totalTime, tickets));
        totalTickets += tickets;
    }

    // Select a process based on lottery ticket system
    private Process drawWinner() {
        if (processes.isEmpty() || totalTickets <= 0) {
            return null;
        }

        // Draw a random ticket number
        int winningTicket = random.nextInt(totalTickets);
        int ticketSum = 0;

        // Find the process that owns the winning ticket
        for (Process p : processes) {
            if (p.remainingTime > 0) {
                ticketSum += p.tickets;
                if (ticketSum > winningTicket) {
                    return p;
                }
            }
        }
        return processes.get(0); // Fallback (shouldn't occur)
    }

    // Main scheduling algorithm
    public void schedule() {
        System.out.println("Starting Lottery Scheduling...");
        int currentTime = 0;

        while (!processes.isEmpty()) {
            Process winner = drawWinner();
            
            // If no winner found, all processes are completed
            if (winner == null) {
                break;
            }

            // Calculate execution time (minimum of quantum and remaining time)
            int slice = Math.min(timeQuantum, winner.remainingTime);
            
            // Execute the winning process
            System.out.printf("Time %d: Executing %s for %d units " +
                            "(Tickets: %d, Total Tickets: %d)\n",
                            currentTime, winner.name, slice, winner.tickets, totalTickets);

            // Update process state
            winner.executedTime += slice;
            winner.remainingTime -= slice;
            currentTime += slice;

            // Remove process if completed
            if (winner.remainingTime <= 0) {
                System.out.printf("Time %d: %s completed (Total time: %d)\n",
                                currentTime, winner.name, winner.executedTime);
                totalTickets -= winner.tickets;
                processes.remove(winner);
            }
        }
        System.out.println("Scheduling completed at time " + currentTime);
    }

    // Main method to test the scheduler
    public static void main(String[] args) {
        // Create scheduler with 5-unit time quantum
        LotteryScheduler scheduler = new LotteryScheduler(5);

        // Add processes with name, total time, and number of tickets
        scheduler.addProcess("P1", 20, 50);  // 50 tickets
        scheduler.addProcess("P2", 15, 30);  // 30 tickets
        scheduler.addProcess("P3", 10, 20);  // 20 tickets

        // Run the scheduler
        scheduler.schedule();
    }
}