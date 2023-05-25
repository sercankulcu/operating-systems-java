package interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * This code first creates a list of processes. Each process has an ID, a burst time, and a number of 
 * lottery tickets. The lottery tickets are used to determine which process is scheduled to run. The 
 * lottery scheduling algorithm then draws a random number from 1 to totalTickets. The process with 
 * the highest number of lottery tickets that is equal to or greater than the random number is 
 * scheduled to run.
 * */

public class LotteryScheduling {

	private static final Random random = new Random();

	public static void main(String[] args) {
		// Create a list of processes
		List<Process> processes = new ArrayList<>();
		processes.add(new Process(1, 10));
		processes.add(new Process(2, 20));
		processes.add(new Process(3, 30));
		processes.add(new Process(4, 20));

		while(!processes.isEmpty()) {

			// Run the lottery scheduling algorithm
			lotteryScheduling(processes);

			// Print the results
			for (Process process : processes) {
				System.out.println(process);
			}
		}
	}

	private static void lotteryScheduling(List<Process> processes) {

		int totalTickets = 0;
		for (Process process : processes) {
			totalTickets += process.getBurstTime();
			process.setTickets(totalTickets);
		}
		// Draw a random number from 1 to 100
		int lotteryNumber = random.nextInt(totalTickets);
		System.out.println("lotteryNumber " + lotteryNumber);

		// Iterate over the processes
		for (Process process : processes) {
			// If the process's lottery tickets are greater than or equal to the lottery number,
			// then the process is scheduled to run
			if (process.getTickets() >= lotteryNumber) {
				process.run();
				processes.remove(process);
				return;
			}
		}
	}
}

class Process {

	private int id;
	private int burstTime;
	private int tickets;

	public Process(int id, int burstTime) {
		this.id = id;
		this.burstTime = burstTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "Process{" +
				"id=" + id +
				", burstTime=" + burstTime +
				", tickets=" + tickets +
				'}';
	}

	public void run() {
		System.out.println("Process " + id + " is running for " + burstTime + " milliseconds");
	}
}
