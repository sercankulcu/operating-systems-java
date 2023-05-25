package interactive;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates priority scheduling:
 * 
 * In this example, a priority queue of processes is created using the PriorityQueue class. The 
 * PriorityQueue is constructed with a comparator that compares the priorities of the processes.
 * The add method is used to add processes to the queue, and the poll method is used to remove 
 * and return the next process in the queue. The processes are run in order of highest priority, 
 * demonstrating priority scheduling.
 * 
 * */

public class PriorityScheduling {
	
    private static final int NUM_PROCESSES = 5;

    public static void main(String[] args) {
    	
        // Create a priority queue of processes sorted by priority
        Queue<Process> processes = new PriorityQueue<>(NUM_PROCESSES, (p1, p2) -> p2.priority - p1.priority);
        for (int i = 0; i < NUM_PROCESSES; i++) {
            int priority = (int) (Math.random() * 10) + 1;
            processes.add(new Process("Process " + i, priority));
        }
        // Run the processes in order of highest priority
        while (!processes.isEmpty()) {
            Process process = processes.poll();
            System.out.println("Running " + process.name + " (priority: " + process.priority + ")");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Process {
        String name;
        int priority;

        Process(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }
}
