package batch;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates first come, first served (FCFS) scheduling:
 * 
 * In this example, a queue of processes is created using the LinkedList class. The add method is 
 * used to add processes to the queue, and the poll method is used to remove and return the next 
 * process in the queue. The processes are run in the order they were added to the queue, 
 * demonstrating first come, first served scheduling.
 * */

public class FCFSScheduling {
    private static final int NUM_PROCESSES = 5;

    public static void main(String[] args) {
        // Create a queue of processes
        Queue<String> processes = new LinkedList<>();
        for (int i = 0; i < NUM_PROCESSES; i++) {
            processes.add("Process " + i);
        }
        // Run the processes in the order they arrived
        while (!processes.isEmpty()) {
            String process = processes.poll();
            System.out.println("Running " + process);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
