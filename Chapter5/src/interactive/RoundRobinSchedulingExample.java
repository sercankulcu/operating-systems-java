package interactive;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates round-robin scheduling:
 * 
 * In this example, a queue of processes is created using the LinkedList class. The poll method 
 * is used to remove and return the next process in the queue. The processes are run using 
 * round-robin scheduling, where each process is given a fixed time slice to execute. If a 
 * process has not completed after its time slice, it is added back to the end of the queue 
 * to be run again later.
 * 
 * */

public class RoundRobinSchedulingExample {
    private static final int NUM_PROCESSES = 5;
    private static final int TIME_SLICE = 2;

    public static void main(String[] args) {
        // Create a queue of processes
        Queue<Process> processes = new LinkedList<>();
        for (int i = 0; i < NUM_PROCESSES; i++) {
            int executionTime = (int) (Math.random() * 10) + 1;
            processes.add(new Process("Process " + i, executionTime));
        }
        // Run the processes using round-robin scheduling
        while (!processes.isEmpty()) {
            Process process = processes.poll();
            System.out.println("Running " + process.name + " (remaining time: " + process.remainingTime + ")");
            try {
                Thread.sleep(TIME_SLICE * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            process.remainingTime -= TIME_SLICE;
            if (process.remainingTime > 0) {
                processes.add(process);
            }
        }
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
