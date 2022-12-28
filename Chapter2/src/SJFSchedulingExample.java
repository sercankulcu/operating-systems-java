import java.util.PriorityQueue;
import java.util.Queue;

/*
 * Here is an example of a Java program that demonstrates shortest job first (SJF) scheduling:
 * 
 * In this example, a priority queue of processes is created using the PriorityQueue class. The 
 * PriorityQueue is constructed with a comparator that compares the execution time of the 
 * processes. The add method is used to add processes to the queue, and the poll method is used 
 * to remove and return the next process in the queue. The processes are run in order of shortest 
 * execution time, demonstrating SJF scheduling.
 * 
 * */

public class SJFSchedulingExample {
    private static final int NUM_PROCESSES = 5;

    public static void main(String[] args) {
        // Create a priority queue of processes sorted by execution time
        Queue<Process> processes = new PriorityQueue<>(NUM_PROCESSES, (p1, p2) -> p1.executionTime - p2.executionTime);
        for (int i = 0; i < NUM_PROCESSES; i++) {
            int executionTime = (int) (Math.random() * 10) + 1;
            processes.add(new Process("Process " + i, executionTime));
        }
        // Run the processes in order of shortest execution time
        while (!processes.isEmpty()) {
            Process process = processes.poll();
            System.out.println("Running " + process.name + " (execution time: " + process.executionTime + ")");
            try {
                Thread.sleep(process.executionTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Process {
        String name;
        int executionTime;

        Process(String name, int executionTime) {
            this.name = name;
            this.executionTime = executionTime;
        }
    }
}
