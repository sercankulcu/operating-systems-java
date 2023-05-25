package interactive;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates fair-share scheduling:
 * 
 * In this example, a queue of processes is created using the LinkedList class. The poll method is 
 * used to remove and return the next process in the queue. 
 * 
 * The processes are run using fair-share scheduling, where each process is given a fixed time 
 * slice to execute. If a process has not completed after its time slice, it is added back to the 
 * end of the queue to be run again later. The total execution time is tracked, and the processes 
 * are run until all processes have completed. 
 * 
 * In addition, the example keeps track of the wait time for each process. If a process has been 
 * waiting for more than half of the time slice, it is given a time slice to execute. This ensures 
 * that each process gets a fair share of the CPU time.
 * 
 * */

public class FairShareScheduling {
	
    private static final int NUM_PROCESSES = 5;
    private static final int TIME_SLICE = 2;

    public static void main(String[] args) {
    	
        // Create a queue of processes
        //Queue<Process> processes = new LinkedList<>();
        Queue<Process> processes = new PriorityQueue<>(NUM_PROCESSES, (p1, p2) -> p2.waitTime - p1.waitTime);
        for (int i = 0; i < NUM_PROCESSES; i++) {
            int executionTime = (int) (Math.random() * 20) + 1;
            processes.add(new Process("Process " + i, executionTime));
            System.out.println("Process " + i + " execution time " + executionTime);
        }
        // Run the processes using fair-share scheduling
        int time = 0;
        while (!processes.isEmpty()) {
            Process process = processes.poll();
            int executionTime = Math.min(process.remainingTime, TIME_SLICE);
            System.out.println("time: " + time + " Running " + process.name + " (remaining time: " + 
            		process.remainingTime + " wait time: " + process.waitTime + ") for " + 
            			executionTime + " sec");
            time += executionTime;
            process.remainingTime -= executionTime;
            process.waitTime = -executionTime;
            if (process.remainingTime > 0) {
                processes.add(process);
            }
            // Check if any other processes have been waiting for more than half of the time slice
            for (Iterator<Process> it = processes.iterator(); it.hasNext(); ) {
            		Process waitingProcess = it.next();
                waitingProcess.waitTime += executionTime;
            }
        }
        // Print the total execution time
        System.out.println("Total execution time: " + time + " seconds");
    }

    private static class Process {
        String name;
        int remainingTime;
        int waitTime;

        Process(String name, int remainingTime) {
            this.name = name;
            this.remainingTime = remainingTime;
            this.waitTime = 0;
        }
    }
}
