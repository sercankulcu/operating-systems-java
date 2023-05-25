package interactive;



/*
 * Here's an implementation of Multiple Queues scheduling algorithm in Java:
 * 
 * This code creates a list of Process objects, each with an ID, burst time, and priority. The 
 * main method implements the Multiple Queues scheduling algorithm by dividing the processes into 
 * 3 separate queues based on their priority, with higher priority processes in a lower-indexed 
 * queue. The algorithm selects the first non-empty queue, removes the next process from the 
 * front of the queue, decrements its burst time
 * 
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MultipleQueuesScheduling {
	
   static class Process {
      int processId;
      int burstTime;
      int priority;

      public Process(int processId, int burstTime, int priority) {
         this.processId = processId;
         this.burstTime = burstTime;
         this.priority = priority;
      }
   }

   public static void main(String[] args) {
  	 
      List<Process> processList = new ArrayList<>();
      processList.add(new Process(1, 10, 3));
      processList.add(new Process(2, 5, 1));
      processList.add(new Process(3, 8, 2));
      processList.add(new Process(4, 4, 1));

      System.out.println("Process ID\tBurst Time\tPriority");
      for (Process process : processList) {
         System.out.println(process.processId + "\t\t" + process.burstTime + "\t\t" + process.priority);
      }

      int time = 0;
      Queue<Process> queue1 = new LinkedBlockingQueue<>();
      Queue<Process> queue2 = new LinkedBlockingQueue<>();
      Queue<Process> queue3 = new LinkedBlockingQueue<>();

      for (Process process : processList) {
         if (process.priority == 1) {
            queue1.offer(process);
         } else if (process.priority == 2) {
            queue2.offer(process);
         } else {
            queue3.offer(process);
         }
      }

      while (!queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()) {
         Process currProcess = null;
         if (!queue1.isEmpty()) {
            currProcess = queue1.poll();
         } else if (!queue2.isEmpty()) {
            currProcess = queue2.poll();
         } else if (!queue3.isEmpty()) {
            currProcess = queue3.poll();
         }

         currProcess.burstTime -= 1;
         time += 1;
         System.out.println("Time " + time + ": Process " + currProcess.processId + " running " + currProcess.burstTime);

         if (currProcess.burstTime > 0) {
            if (currProcess.priority == 1) {
               queue1.offer(currProcess);
            } else if (currProcess.priority == 2) {
               queue2.offer(currProcess);
            } else {
               queue3.offer(currProcess);
            }
         }
      }
   }
}
