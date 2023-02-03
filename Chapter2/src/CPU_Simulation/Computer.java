package CPU_Simulation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Matthew Segal
 * @author Laura Boivin
 *
 */
public class Computer
{
    static int numOfCPUs = 0;
    static Queue<CPU> readyQueue = new LinkedList<>();
    static Queue<Process> processQueue = new LinkedList<>();
    static Comparator<Process> processComparator = Comparator.comparing(Process::getTotalExecutionTime);
    static PriorityQueue<Process> processPriorityQueue = new PriorityQueue<>(processComparator);
    static Scanner sc;

    public static void main(String[] args) {
        ArrayList<Process> listOfProcessObjects;
        ArrayList<CPU> listOfCPUObjects = new ArrayList<>();

        sc = null;
        setScannerToFileMode();

        // Gets the list of all Processes
        listOfProcessObjects = readFile();

        // Fills up list of CPUs with new CPUs based on the numOfCPUs
        for (int i = 0; i < numOfCPUs; i++) {
            CPU cpu = new CPU();
            cpu.setCPUID(i);
            listOfCPUObjects.add(cpu);
        }

        // MAIN CODE
        System.out.println("Now performing a simulation based on FCFS...");
        firstComeFirstServe(listOfCPUObjects, listOfProcessObjects);
        System.out.println("FCFS simulation finished!");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        System.out.println("Now performing a simulation based on SJF...");
        shortestJobFirst(listOfCPUObjects, listOfProcessObjects);
        System.out.println("SJF simulation finished!");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        System.out.println("Now performing a simulation based on SRTF...");
        shortestRemainingTimeFirst(listOfCPUObjects, listOfProcessObjects);
        System.out.println("SRTF simulation finished!");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        //System.out.print("Now for Round-robin. Please choose an integer time-quantum: ");
        // Sets up Scanner for user input
        sc.close(); // closes Scanner to the file
        setScannerToUserMode();
        //int quantum = sc.nextInt();
        int quantum = 1;

        roundRobin(quantum, listOfCPUObjects, listOfProcessObjects);
        System.out.println("RR simulation finished!\n");

        sc.close(); // closes Scanner to user input
    }

    private static void firstComeFirstServe(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        int timeUnit = 0;

        calculateNumOfIORequests(processes);

        while (!checkIfAllTerminated(processes)) {
            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu : cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null) {
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().size() != 0 &&
                            currentProcess.getIORequestTime().get(0) == currentProcess.getExecutionTime()) {

                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }

            // Loops through the processList, if the ioRequest timer is done, add the process to the process queue and if there is one that has an arrival time of now,
            // add it to the processQueue otherwise if the process has reached its total execution time then change the status to terminated
            for (Process process : processes) {

                //optimal results when processes are added to the process queue at the start of this loop
                if (process.getArrivalTime() == timeUnit) {
                    process.setStatus(ProcessState.READY);
                    processQueue.add(process);
                }

                if (process.getIOTimer() == 2) {
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processQueue.add(process);
                    process.setStatus(ProcessState.READY); //L: just a logical swap of order
                }

                if (!readyQueue.isEmpty() && !processQueue.isEmpty()) {
                    if (process.getStatus().equals(ProcessState.READY)) {
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue

                        // Gets current Process from Queue
                        Process currentProcess = processQueue.remove();// Remove Process from processQueue

                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU

                        // Sets the statuses of the Process and CPU
                        currentCPU.setState(CPUState.BUSY);
                        currentProcess.setStatus(ProcessState.RUNNING);
                        currentProcess.setCpuResponse(true);
                        //adding the wait time timer to the wait time array list in currentProcess and setting the wait time timer back to 0
                        currentProcess.getWaitTimeArrayList().add(0,currentProcess.getWaitTimeTimer());
                        currentProcess.setWaitTimeTimer(0);
                    }
                }

                // If the execution time is over, set it to TERMINATED
                if (process.getTotalExecutionTime() == process.getExecutionTime()) {
                    process.setStatus(ProcessState.TERMINATED);

                    for (CPU cpu : cpus) {
                        if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)) {
                            cpu.setProcess(null);
                            cpu.setState(CPUState.READY);
                            readyQueue.add(cpu);
                        }
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro : processes) {
                if (pro.getStatus() != ProcessState.TERMINATED) {
                    if (pro.getIsWaiting()) {
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }
                    //Increment the wait time timer for each process with status "Ready"
                    if (pro.getStatus().equals(ProcessState.READY)){
                    pro.setWaitTimeTimer(pro.getWaitTimeTimer()+1);
                    }
                    if(pro.getStatus()== ProcessState.READY && !pro.getCpuResponse()){
                        pro.setCpuResponseTime(pro.getCpuResponseTime()+1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                // L: changed condition from "not terminated" to "is running" because the cpu
                // execution time should not increase if process status is "waiting"
                if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.RUNNING)) {
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);
                    cpu.setUtilization(cpu.getUtilization() + 1);
                }
            }
            timeUnit++;
        }

        // Clears out readyQueue
        while (!readyQueue.isEmpty()){
            readyQueue.remove();
        }

        // HANDLES DISPLAYING CPU UTILIZATION
        displayCPUUtilization(cpus, timeUnit);

        System.out.println();

        displayAverageWaitTime(processes);

        System.out.println();

        displayTurnaroundTime(processes);

        System.out.println();

        displayCpuResponseTime(processes);

        System.out.println();
    }

    private static void roundRobin(int timeQuantum, ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        int timeUnit = 0;

        calculateNumOfIORequests(processes);

        while (!checkIfAllTerminated(processes)) {
            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu : cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null) {
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().size() != 0 &&
                            currentProcess.getIORequestTime().get(0) == currentProcess.getExecutionTime()) {

                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }

            // Loops through CPUs to see if any have reached the timeQuantum, if so,
            // kick it off and reset timeQuantumTimer
            for (CPU cpu : cpus) {
                if (cpu.getProcess() != null && cpu.getTimeQuantumTimer() == timeQuantum){
                    Process currentProcess = cpu.getProcess();
                    cpu.setProcess(null);
                    cpu.setState(CPUState.READY);
                    readyQueue.add(cpu);
                    cpu.setTimeQuantumTimer(0);
                    currentProcess.setStatus(ProcessState.READY);
                    processQueue.add(currentProcess);
                }
            }

            // Loops through the processList, if the ioRequest timer is done, add the process to the process queue and if there is one that has an arrival time of now,
            // add it to the processQueue otherwise if the process has reached its total execution time then change the status to terminated
            for (Process process : processes) {

                //optimal results when processes are added to the process queue at the start of this loop
                if (process.getArrivalTime() == timeUnit) {
                    process.setStatus(ProcessState.READY);
                    processQueue.add(process);
                }

                if (process.getIOTimer() == 2) {
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processQueue.add(process);
                    process.setStatus(ProcessState.READY); //L: just a logical swap of order
                }

                if (!readyQueue.isEmpty() && !processQueue.isEmpty()) {
                    if (process.getStatus().equals(ProcessState.READY)) {
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue

                        // Gets current Process from Queue
                        Process currentProcess = processQueue.remove();// Remove Process from processQueue

                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU

                        // Sets the statuses of the Process and CPU
                        currentCPU.setState(CPUState.BUSY);
                        currentProcess.setStatus(ProcessState.RUNNING);
                        currentProcess.setCpuResponse(true);
                        //adding the wait time timer to the wait time array list in currentProcess and setting the wait time timer back to 0
                        currentProcess.getWaitTimeArrayList().add(0,currentProcess.getWaitTimeTimer());
                        currentProcess.setWaitTimeTimer(0);
                    }
                }

                // If the execution time is over, set it to TERMINATED
                if (process.getTotalExecutionTime() == process.getExecutionTime()) {
                    process.setStatus(ProcessState.TERMINATED);

                    for (CPU cpu : cpus) {
                        if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)) {
                            cpu.setProcess(null);
                            cpu.setState(CPUState.READY);
                            readyQueue.add(cpu);
                        }
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro : processes) {
                if (pro.getStatus() != ProcessState.TERMINATED) {
                    if (pro.getIsWaiting()) {
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }

                    //Increment the wait time timer for each process with status "Ready"
                    if (pro.getStatus().equals(ProcessState.READY)){
                        pro.setWaitTimeTimer(pro.getWaitTimeTimer()+1);
                    }

                    if(pro.getStatus()== ProcessState.READY && !pro.getCpuResponse()){
                        pro.setCpuResponseTime(pro.getCpuResponseTime()+1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                // L: changed condition from "not terminated" to "is running" because the cpu
                // execution time should not increase if process status is "waiting"
                if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.RUNNING)) {
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);

                    cpu.setUtilization(cpu.getUtilization() + 1);

                    // Increments the CPUs timeQuantumTimer
                    cpu.setTimeQuantumTimer(cpu.getTimeQuantumTimer() + 1);
                }
            }
            timeUnit++;
        }

        // Clears out readyQueue
        while (!readyQueue.isEmpty()){
            readyQueue.remove();
        }

        // HANDLES DISPLAYING CPU UTILIZATION
        displayCPUUtilization(cpus, timeUnit);

        System.out.println();

        displayAverageWaitTime(processes);

        System.out.println();

        displayTurnaroundTime(processes);

        System.out.println();

        displayCpuResponseTime(processes);

        System.out.println();
    }

    private static void shortestJobFirst(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        int timeUnit = 0;

        calculateNumOfIORequests(processes);

        while (!checkIfAllTerminated(processes)) {
            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu : cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null) {
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().size() != 0 &&
                            currentProcess.getIORequestTime().get(0) == currentProcess.getExecutionTime()) {

                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }

            for (Process pr : processes) {
                //optimal results when processes are added to the process queue at the start of this loop
                if (pr.getArrivalTime() == timeUnit) {
                    pr.setStatus(ProcessState.READY);
                    processPriorityQueue.add(pr);
                }
            }

            // Loops through the processList, if the ioRequest timer is done, add the process to the process queue and
            // if there is one that has an arrival time of now, add it to the processPriorityQueue otherwise if the
            // process has reached its total execution time then change the status to terminated
            for (Process process : processes) {
                if (process.getIOTimer() == 2) {
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processPriorityQueue.add(process);
                    process.setStatus(ProcessState.READY); //L: just a logical swap of order
                }

                if (!readyQueue.isEmpty() && !processPriorityQueue.isEmpty()) {
                    if (process.getStatus().equals(ProcessState.READY)) {
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue
                        // Gets current Process from Queue
                        Process currentProcess = processPriorityQueue.remove();// Remove Process from processPriorityQueue

                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU

                        // Sets the statuses of the Process and CPU
                        currentCPU.setState(CPUState.BUSY);
                        currentProcess.setStatus(ProcessState.RUNNING);
                        currentProcess.setCpuResponse(true);
                        //adding the wait time timer to the wait time array list in currentProcess and setting
                        // the wait time timer back to 0
                        currentProcess.getWaitTimeArrayList().add(0,currentProcess.getWaitTimeTimer());
                        currentProcess.setWaitTimeTimer(0);
                    }
                }

                // If the execution time is over, set it to TERMINATED
                if (process.getTotalExecutionTime() == process.getExecutionTime()) {
                    process.setStatus(ProcessState.TERMINATED);

                    for (CPU cpu : cpus) {
                        if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)) {
                            cpu.setProcess(null);
                            cpu.setState(CPUState.READY);
                            readyQueue.add(cpu);
                        }
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro : processes) {
                if (pro.getStatus() != ProcessState.TERMINATED) {
                    if (pro.getIsWaiting()) {
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }
                    //Increment the wait time timer for each process with status "Ready"
                    if (pro.getStatus().equals(ProcessState.READY)){
                        pro.setWaitTimeTimer(pro.getWaitTimeTimer()+1);
                    }
                    if(pro.getStatus()== ProcessState.READY && !pro.getCpuResponse()){
                        pro.setCpuResponseTime(pro.getCpuResponseTime()+1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                // L: changed condition from "not terminated" to "is running" because the cpu execution
                // time should not increase if process status is "waiting"
                if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.RUNNING)) {
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);
                    cpu.setUtilization(cpu.getUtilization() + 1);
                }
            }
            timeUnit++;
        }

        // Clears out readyQueue
        while (!readyQueue.isEmpty()){
            readyQueue.remove();
        }

        // HANDLES DISPLAYING CPU UTILIZATION
        displayCPUUtilization(cpus, timeUnit);

        System.out.println();

        displayAverageWaitTime(processes);

        System.out.println();

        displayTurnaroundTime(processes);

        System.out.println();

        displayCpuResponseTime(processes);

        System.out.println();
    }

    private static void shortestRemainingTimeFirst(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        Comparator<Process> SRTFComparator = Comparator.comparing(Process::getRemainingTime);
        PriorityQueue<Process> SRTFPriorityQueue = new PriorityQueue<>(SRTFComparator);

        int timeUnit = 0;

        calculateNumOfIORequests(processes);

        while (!checkIfAllTerminated(processes)) {
            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu : cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null) {
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().size() != 0 &&
                            currentProcess.getIORequestTime().get(0) == currentProcess.getExecutionTime()) {

                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }

            for (Process pr : processes) {
                //optimal results when processes are added to the process queue at the start of this loop
                if (pr.getArrivalTime() == timeUnit) {
                    pr.setStatus(ProcessState.READY);
                    SRTFPriorityQueue.add(pr);
                }
            }

            // Loops through the processList, if the ioRequest timer is done, add the process to the process queue and
            // if there is one that has an arrival time of now, add it to the SRTFPriorityQueue otherwise if the
            // process has reached its total execution time then change the status to terminated
            for (Process process : processes) {
                if (process.getIOTimer() == 2) {
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    SRTFPriorityQueue.add(process);
                    process.setStatus(ProcessState.READY); //L: just a logical swap of order
                }

                if (!readyQueue.isEmpty() && !SRTFPriorityQueue.isEmpty()) {
                    if (process.getStatus().equals(ProcessState.READY)) {
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue

                        // Gets current Process from Queue
                        Process currentProcess = SRTFPriorityQueue.remove();// Remove Process from SRTFPriorityQueue

                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU

                        // Sets the statuses of the Process and CPU
                        currentCPU.setState(CPUState.BUSY);
                        currentProcess.setStatus(ProcessState.RUNNING);
                        currentProcess.setCpuResponse(true);
                        // adding the wait time timer to the wait time array list in currentProcess and
                        // setting the wait time timer back to 0
                        currentProcess.getWaitTimeArrayList().add(0,currentProcess.getWaitTimeTimer());
                        currentProcess.setWaitTimeTimer(0);
                    }
                }

                // If the execution time is over, set it to TERMINATED
                if (process.getTotalExecutionTime() == process.getExecutionTime() && process.getIORequestTime() == null) {
                    process.setStatus(ProcessState.TERMINATED);

                    for (CPU cpu : cpus) {
                        if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)) {
                            cpu.setProcess(null);
                            cpu.setState(CPUState.READY);
                            readyQueue.add(cpu);
                        }
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro : processes) {
                if (pro.getStatus() != ProcessState.TERMINATED) {
                    if (pro.getIsWaiting()) {
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }
                    //Increment the wait time timer for each process with status "Ready"
                    if (pro.getStatus().equals(ProcessState.READY)){
                        pro.setWaitTimeTimer(pro.getWaitTimeTimer()+1);
                    }
                    if(pro.getStatus()== ProcessState.READY && !pro.getCpuResponse()){
                        pro.setCpuResponseTime(pro.getCpuResponseTime()+1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                // L: changed condition from "not terminated" to "is running" because the cpu execution
                // time should not increase if process status is "waiting"
                if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.RUNNING)) {
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);
                    cpu.setUtilization(cpu.getUtilization() + 1);
                }
            }

            for (CPU cpu : cpus){
                Process currentProcess = cpu.getProcess();
                if (!SRTFPriorityQueue.isEmpty() && currentProcess != null &&
                        SRTFPriorityQueue.peek().getRemainingTime() < currentProcess.getRemainingTime() &&
                        currentProcess.getRemainingTime() > 0) {
                    cpu.setProcess(null);
                    cpu.setState(CPUState.READY);

                    readyQueue.add(cpu);

                    Process temp = SRTFPriorityQueue.remove();
                    SRTFPriorityQueue.add(currentProcess);
                    currentProcess.setStatus(ProcessState.READY);
                    cpu.setProcess(temp);
                    cpu.setState(CPUState.BUSY);
                    temp.setStatus(ProcessState.RUNNING);
                    readyQueue.remove();
                }
            }

            for (Process p : processes) {
                if (p.getRemainingTime() == 0){
                    p.setStatus(ProcessState.TERMINATED);
                }else{
                    p.setRemainingTime(p.getTotalExecutionTime() - p.getExecutionTime());
                }
            }
            timeUnit++;
        }

        // Clears out readyQueue
        while (!readyQueue.isEmpty()){
            readyQueue.remove();
        }

        // HANDLES DISPLAYING CPU UTILIZATION
        displayCPUUtilization(cpus, timeUnit);

        System.out.println();

        displayAverageWaitTime(processes);

        System.out.println();

        displayTurnaroundTime(processes);

        System.out.println();

        displayCpuResponseTime(processes);

        System.out.println();
    }

    private static ArrayList<Process> readFile() {
        int processNbr = 0;
        ArrayList<Process> listOfProcessObjects = new ArrayList<>();
        String processID;
        int arrivalTime;
        int totalExecTime;

        String str;

        while (sc.hasNextLine())
        {
            str = sc.nextLine();

            if (str.contains("numOfCPUs:")) {
                String[] cpus = str.split("\\s+");
                numOfCPUs = Integer.parseInt(cpus[cpus.length-1]);
            }

            if (str.contains("p" + processNbr)) {

                String[] lineArray = str.split("\\s+");

                processID = lineArray[0];
                arrivalTime = Integer.parseInt(lineArray[1]);
                totalExecTime = Integer.parseInt(lineArray[2]);
                ArrayList<Integer> IO_RequestAtTime = new ArrayList<>();

                if(lineArray.length>3)
                {
                    for (int j = 3; j < lineArray.length; j++)
                    {
                        IO_RequestAtTime.add(Integer.parseInt(lineArray[j]));
                    }
                }
                else
                {
                    IO_RequestAtTime = null;
                }

                listOfProcessObjects.add(new Process(processID, arrivalTime, totalExecTime, IO_RequestAtTime));
                IO_RequestAtTime = null;
                processNbr++;
            }
        }
        return listOfProcessObjects;
    }

    private static void calculateNumOfIORequests(ArrayList<Process> processes){
        for(Process process : processes){
            if (process.getIORequestTime()!=null)
                process.setNbIORequests(process.getIORequestTime().size());
        }
    }

    private static void resetScanner(){
        sc.close();
        setScannerToFileMode();
    }

    private static void setScannerToFileMode(){
        try {
            sc = new Scanner(new FileInputStream("cpu_input.txt"));
        }catch (FileNotFoundException e) {
            System.out.println("Error in the path to the text file.");
            System.exit(0);
        }
    }

    private static void setScannerToUserMode(){
        sc = new Scanner(System.in);
    }

    private static void resetAllCPUs(ArrayList<CPU> cpus){
        for (CPU cpu: cpus) {
            cpu.setState(CPUState.READY);
            cpu.setUtilization(0);
            readyQueue.add(cpu);
        }
    }

    private static boolean checkIfAllTerminated(ArrayList<Process> processes){
        int terminatedCounter = 0;
        for (Process process : processes) {
            if (process.getStatus().equals(ProcessState.TERMINATED)){
                terminatedCounter++;
            }
        }
        return terminatedCounter == processes.size();
    }

    private static void displayCPUUtilization(ArrayList<CPU> cpus, int timeUnit){
        for (CPU cpu : cpus) {
            System.out.println("CPU:" + cpu.getCPUID() + " has been used for " + cpu.getUtilization() + "/"
                    + timeUnit + " time");
        }
    }

    private static void displayAverageWaitTime(ArrayList<Process> processes){
        double waitSum;
        ArrayList<Double> averagePerProcess = new ArrayList<>();
        double average =0;

        for (Process process : processes){
            waitSum = 0;
            for(int i = 0; i<process.getWaitTimeArrayList().size(); i++){
                waitSum += process.getWaitTimeArrayList().get(i);
            }
            waitSum = waitSum/process.getWaitTimeArrayList().size();
            averagePerProcess.add(0,waitSum);
        }

        for (Double perProcess : averagePerProcess) {
            average += perProcess;
        }

        average = average/averagePerProcess.size();
        System.out.println("The average wait time is "+ average + " time units.");
    }

    private static void displayTurnaroundTime(ArrayList<Process> processes) {
        int turnaroundTime;
        int waitSum;
        for (Process process : processes){
            waitSum = 0;
            for (int i = 0; i<process.getWaitTimeArrayList().size();i++){
                waitSum = waitSum + process.getWaitTimeArrayList().get(i);
            }
            waitSum += process.getTotalExecutionTime();
            if (process.getIORequestTime()!=null)
                waitSum += (process.getNbIORequests()*2);
            turnaroundTime = waitSum; // - process.getArrivalTime();
            process.setTurnaroundPerProcess(turnaroundTime);
            System.out.println("Process "+ process.getPID()+" has a turnaround time of "+process.getTurnaroundPerProcess()+" time unit(s).");
        }
    }

    private static void displayCpuResponseTime(ArrayList<Process> processes){
        for (Process process : processes){
            System.out.println("The CPU response time for process "+ process.getPID()+ " was: "+process.getCpuResponseTime());
        }
    }
}

