package batch;


/*
 * Here's an implementation of the Shortest Remaining Time Next (SRTN) scheduling algorithm in 
 * Java:
 * 
 * This code creates a list of Process objects, each with an ID, burst time, and remaining time. 
 * The compareTo method sorts the processes based on the remaining time. The main method 
 * implements the SRTN scheduling algorithm by continually selecting the process with the 
 * shortest remaining time, decrementing its remaining time, and printing out the status at 
 * each time unit. When a process's remaining time reaches 0, it is removed from the list.
 * 
 * */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SRTNScheduling {
   static class Process implements Comparable<Process> {
      int processId;
      int burstTime;
      int remainingTime;

      public Process(int processId, int burstTime) {
         this.processId = processId;
         this.burstTime = burstTime;
         this.remainingTime = burstTime;
      }

      @Override
      public int compareTo(Process o) {
         if (this.remainingTime == o.remainingTime) {
            return Integer.compare(this.processId, o.processId);
         }
         return Integer.compare(this.remainingTime, o.remainingTime);
      }
   }

   public static void main(String[] args) {
      List<Process> processList = new ArrayList<>();
      processList.add(new Process(1, 10));
      processList.add(new Process(2, 5));
      processList.add(new Process(3, 8));

      System.out.println("Process ID\tBurst Time\tRemaining Time");
      for (Process process : processList) {
         System.out.println(process.processId + "\t\t" + process.burstTime + "\t\t" + process.remainingTime);
      }

      int time = 0;
      while (!processList.isEmpty()) {
         Collections.sort(processList);
         Process currProcess = processList.get(0);
         currProcess.remainingTime -= 1;
         time += 1;
         System.out.println("Time " + time + ": Process " + currProcess.processId + " running");
         if (currProcess.remainingTime == 0) {
            processList.remove(0);
         }
         
         if(time == 10) {
        	 processList.add(new Process(4, 2));
         }
      }
   }
}

