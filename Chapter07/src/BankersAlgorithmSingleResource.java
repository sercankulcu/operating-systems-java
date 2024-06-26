

/*
 * Here's an example Java code that implements the Banker's Algorithm for a single resource:
 * 
 * */

public class BankersAlgorithmSingleResource {
	static int processes;
	static int available;
	static int[] max;
	static int[] allocation;
	static int[] need;

	static void inputData() {

		processes = 4;
		max = new int[processes];
		allocation = new int[processes];
		need = new int[processes];
		available = 2;

		max[0] = 6;
		max[1] = 5;
		max[2] = 4;
		max[3] = 7;

		allocation[0] = 1;
		allocation[1] = 1;
		allocation[2] = 2;
		allocation[3] = 4;

		for (int i = 0; i < processes; i++) {
			need[i] = max[i] - allocation[i];
		}
	}

	static boolean check(int process) {
		if(need[process] > available) {
			return false;
		}
		return true;
	}

	static boolean safety() {
		int count = 0;
		boolean[] finished = new boolean[processes];
		while (count < processes) {
			boolean found = false;
			for (int i = 0; i < processes; i++) {
				if (!finished[i] && check(i)) {
					available += allocation[i];
					finished[i] = true;
					count++;
					found = true;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		inputData();
		if (safety()) {
			System.out.println("The system is in a safe state.");
		} else {
			System.out.println("The system is in an unsafe state.");
		}
	}
}
