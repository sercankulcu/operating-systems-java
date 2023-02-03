
import java.util.Scanner;

public class BankersAlgorithmMultipleResource {
    static int resources, processes;
    static int available[];
    static int max[][];
    static int allocation[][];
    static int need[][];

    static void inputData() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes:");
        //processes = sc.nextInt();
        processes = 5;
        System.out.println("Enter number of resources:");
        //resources = sc.nextInt();
        resources = 4;
        available = new int[resources];
        max = new int[processes][resources];
        allocation = new int[processes][resources];
        need = new int[processes][resources];
        System.out.println("Enter the available resources:");
        for (int i = 0; i < resources; i++) {
        //    available[i] = sc.nextInt();
        }
        available[0] = 5;
        available[1] = 3;
        available[2] = 2;
        available[3] = 2;
        System.out.println("Enter the maximum resources for each process:");
        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                //max[i][j] = sc.nextInt();
            }
        }
        max[0][0] = 4;
        max[0][1] = 1;
        max[0][2] = 1;
        max[0][3] = 1;
        max[1][1] = 2;
        max[1][2] = 1;
        max[1][3] = 2;
        max[2][0] = 4;
        max[2][1] = 2;
        max[2][2] = 1;
        max[3][0] = 1;
        max[3][1] = 1;
        max[3][2] = 1;
        max[3][3] = 1;
        max[4][0] = 2;
        max[4][1] = 1;
        max[4][2] = 1;
        
        
        allocation[0][0] = 3;
        allocation[0][2] = 1;
        allocation[0][3] = 1;
        allocation[1][1] = 1;
        allocation[2][0] = 1;
        allocation[2][1] = 1;
        allocation[2][2] = 1;
        allocation[3][0] = 1;
        allocation[3][1] = 1;
        allocation[3][3] = 1;
        
        System.out.println("Enter the allocated resources for each process:");
        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                //allocation[i][j] = sc.nextInt();
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    static boolean check(int process) {
        for (int i = 0; i < resources; i++) {
            if (need[process][i] > available[i]) {
                return false;
            }
        }
        return true;
    }

    static boolean safety() {
        int count = 0;
        boolean finished[] = new boolean[processes];
        int work[] = new int[resources];
        for (int i = 0; i < resources; i++) {
            work[i] = available[i];
        }
        while (count < processes) {
            boolean found = false;
            for (int i = 0; i < processes; i++) {
                if (!finished[i] && check(i)) {
                    for (int j = 0; j < resources; j++) {
                        work[j] += allocation[i][j];
                    }
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

