package Scheduling_Interactive;

/*
 * Here's an implementation of the Shortest Process Next (SPN) scheduling algorithm in Java:
 * 
 * This code creates a list of Process objects, each with an ID and burst time. The main method 
 * implements the SPN scheduling algorithm by selecting the process with the shortest burst time 
 * and removing it from the list. The selected process is executed for 1 unit of time and its 
 * burst time is decremented. If the burst time is still greater than 0, the process is added 
 * back to the list and the algorithm repeats until all processes have completed.
 * 
 * */

import java.util.ArrayList;
import java.util.List;

public class ShortestProcessNextScheduling {
   static class Process {
      int processId;
      int burstTime;

      public Process(int processId, int burstTime) {
         this.processId = processId;
         this.burstTime = burstTime;
      }
   }

   public static void main(String[] args) {
      List<Process> processList = new ArrayList<>();
      processList.add(new Process(1, 10));
      processList.add(new Process(2, 5));
      processList.add(new Process(3, 8));

      System.out.println("Process ID\tBurst Time");
      for (Process process : processList) {
         System.out.println(process.processId + "\t\t" + process.burstTime);
      }

      int time = 0;
      while (!processList.isEmpty()) {
         int shortestProcessIndex = 0;
         for (int i = 1; i < processList.size(); i++) {
            if (processList.get(i).burstTime < processList.get(shortestProcessIndex).burstTime) {
               shortestProcessIndex = i;
            }
         }

         Process currProcess = processList.get(shortestProcessIndex);
         processList.remove(shortestProcessIndex);

         currProcess.burstTime -= 1;
         time += 1;
         System.out.println("Time " + time + ": Process " + currProcess.processId + " running");

         if (currProcess.burstTime > 0) {
            processList.add(currProcess);
         }
      }
   }
}
