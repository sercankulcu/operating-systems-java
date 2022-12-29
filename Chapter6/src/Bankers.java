import java.util.Arrays;

/*
 * Here is an example of a Java program that implements the Banker's algorithm for 
 * deadlock prevention:
 * 
 * */

public class Bankers {
  private int[][] need;
  private int[][] allocation;
  private int[] available;
  private int[] work;
  private boolean[] finished;
  private int numProcesses;
  private int numResources;

  public Bankers(int numProcesses, int numResources, int[][] allocation, int[][] max) {
    this.numProcesses = numProcesses;
    this.numResources = numResources;
    this.allocation = allocation;
    this.need = new int[numProcesses][numResources];
    this.available = new int[numResources];
    this.work = new int[numResources];
    this.finished = new boolean[numProcesses];
    // Calculate the need matrix
    for (int i = 0; i < numProcesses; i++) {
      for (int j = 0; j < numResources; j++) {
        need[i][j] = max[i][j] - allocation[i][j];
      }
    }
    // Calculate the available vector
    for (int j = 0; j < numResources; j++) {
      int sum = 0;
      for (int i = 0; i < numProcesses; i++) {
        sum += allocation[i][j];
      }
      available[j] = sum;
    }
    // Initialize the work vector
    work = Arrays.copyOf(available, available.length);
  }

  public boolean isSafe() {
    // Check if there is a need that is less than or equal to the available resources
    for (int i = 0; i < numProcesses; i++) {
      if (!finished[i]) {
        boolean canAllocate = true;
        for (int j = 0; j < numResources; j++) {
          if (need[i][j] > work[j]) {
            canAllocate = false;
            break;
          }
        }
        if (canAllocate) {
          // If the process can be allocated, update the work and finished vectors
          finished[i] = true;
          for (int j = 0; j < numResources; j++) {
            work[j] += allocation[i][j];
          }
          i = -1;
        }
      }
    }
    // Check if all processes have been finished
    for (boolean b : finished) {
      if (!b) {
        return false;
      }
    }
    return true;
  }
}
