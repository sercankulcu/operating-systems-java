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
 *
 * Extended to include arrival times and burst times for more realistic FCFS simulation.
 * */

public class FCFSScheduler {

    static class Process {
        String name;
        int arrivalTime;
        int burstTime;

        public Process(String name, int arrivalTime, int burstTime) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
        }

        @Override
        public String toString() {
            return "Process{" +
                    "name='" + name + '\'' +
                    ", arrivalTime=" + arrivalTime +
                    ", burstTime=" + burstTime +
                    '}';
        }
    }

    public static void main(String[] args) {
        // Create a queue of processes with arrival and burst times
        Queue<Process> processes = new LinkedList<>();
        processes.add(new Process("P1", 0, 8));
        processes.add(new Process("P2", 1, 4));
        processes.add(new Process("P3", 2, 9));
        processes.add(new Process("P4", 3, 5));
        processes.add(new Process("P5", 4, 3));

        // Simulate FCFS scheduling
        int currentTime = 0;
        while (!processes.isEmpty()) {
            Process currentProcess = processes.poll();

            // Wait until the process arrives
            if (currentTime < currentProcess.arrivalTime) {
                currentTime = currentProcess.arrivalTime;
            }

            // Run the process
            System.out.println("Running " + currentProcess.name + " at time " + currentTime);

            try {
                Thread.sleep(currentProcess.burstTime * 100); // Simulate burst time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime += currentProcess.burstTime;

            System.out.println(currentProcess.name + " finished at time " + currentTime);
        }

        System.out.println("FCFS scheduling completed.");
    }
}