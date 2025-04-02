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

public class SRTNScheduler {
	
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

		int time = 0;
		while (!processList.isEmpty()) {

			Collections.sort(processList);
			Process currProcess = processList.get(0);

			time += currProcess.remainingTime;
			System.out.println("Time " + time + ": Process " + currProcess.processId + " running. Remaining time: " + currProcess.remainingTime);
			processList.remove(0);

			if(time == 5) {
				processList.add(new Process(4, 2));
			}
			
			if(time == 15) {
				processList.add(new Process(5, 7));
			}
		}
	}
}

