package CPU_Simulation;
import java.util.ArrayList;

enum ProcessState {
  NEW, RUNNING, WAITING, READY, TERMINATED
}

/**
 * A class for defining a Process
 */
public class Process implements Comparable{
    private String PID;
    private int arrivalTime;
    private int totalExecutionTime;
    private ArrayList<Integer> IORequestTime;
    private ProcessState status;
    private int IOTimer;
    private boolean isWaiting;
    private int executionTime;
    private int waitTimeTimer;
    private ArrayList<Integer> waitTimeArrayList;
    private int turnaroundPerProcess;
    private int nbIORequests;
    private boolean CpuResponse;
    private int CpuResponseTime;
    private int remainingTime;

    public Process() {
        PID = "NULL";
        arrivalTime = -1;
        totalExecutionTime = -1;
        IORequestTime = null;
        status = ProcessState.NEW;
        IOTimer = 0;
        isWaiting = false;
        executionTime = 0;
        waitTimeTimer = 0;
        waitTimeArrayList = new ArrayList<>();
        turnaroundPerProcess = 0;
        nbIORequests =0;
        CpuResponse =false;
        CpuResponseTime = 0;
        remainingTime = totalExecutionTime - executionTime;
    }

    public Process(String PID, int arrivalTime, int totalExecutionTime, ArrayList<Integer> IORequestTime) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.totalExecutionTime = totalExecutionTime;
        this.IORequestTime = IORequestTime;
        status = ProcessState.NEW;
        IOTimer = 0;
        isWaiting = false;
        executionTime = 0;
        waitTimeTimer = 0;
        waitTimeArrayList = new ArrayList<>();
        turnaroundPerProcess = 0;
        nbIORequests =0;
        CpuResponse = false;
        CpuResponseTime = 0;
        remainingTime = totalExecutionTime - executionTime;
    }
    public int getCpuResponseTime() { return CpuResponseTime; }

    public void setCpuResponseTime(int cpuResponseTime) { CpuResponseTime = cpuResponseTime; }

    public boolean getCpuResponse() { return CpuResponse; }

    public void setCpuResponse(boolean cpuResponse) { CpuResponse = cpuResponse; }

    public int getTurnaroundPerProcess() { return turnaroundPerProcess; }

    public void setTurnaroundPerProcess(int turnaroundPerProcess) { this.turnaroundPerProcess = turnaroundPerProcess; }

    public int getNbIORequests() { return nbIORequests; }

    public void setNbIORequests(int nbIORequests) { this.nbIORequests = nbIORequests; }

    public int getWaitTimeTimer() { return waitTimeTimer; }

    public void setWaitTimeTimer(int waitTimeTimer) { this.waitTimeTimer = waitTimeTimer; }

    public ArrayList<Integer> getWaitTimeArrayList() { return waitTimeArrayList; }

    public void setWaitTimeArrayList(ArrayList<Integer> waitTimeArrayList) { this.waitTimeArrayList = waitTimeArrayList; }

    public String getPID() {
        return PID;
    }

    public int getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTotalExecutionTime() { return totalExecutionTime; }

    public ArrayList<Integer> getIORequestTime() { return IORequestTime; }

    public ProcessState getStatus() {
        return status;
    }

    public void setStatus(ProcessState status) { this.status = status; }

    public int getIOTimer() { return IOTimer; }

    public void setIOTimer(int IOTimer) { this.IOTimer = IOTimer; }

    public boolean getIsWaiting() { return isWaiting; }

    public void setIsWaiting(boolean waiting) { isWaiting = waiting; }

    public int getExecutionTime() { return executionTime; }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getRemainingTime() { return remainingTime; }

    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }

    /*@Override
    public String toString() {
        return "Process{" +
                "PID='" + PID + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", totalExecutionTime=" + totalExecutionTime +
                ", IORequestTime=" + IORequestTime +
                ", status=" + status +
                '}';
    }*/

    @Override
    public String toString() {
        return "Process{" +
                "PID='" + PID + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", totalExecutionTime=" + totalExecutionTime +
                ", IORequestTime=" + IORequestTime +
                ", status=" + status +
                ", IOTimer=" + IOTimer +
                ", isWaiting=" + isWaiting +
                ", executionTime=" + executionTime +
                ", waitTimeTimer=" + waitTimeTimer +
                ", waitTimeArrayList=" + waitTimeArrayList +
                ", turnaroundPerProcess=" + turnaroundPerProcess +
                ", nbIORequests=" + nbIORequests +
                ", CpuResponse=" + CpuResponse +
                ", CpuResponseTime=" + CpuResponseTime +
                ", remainingTime=" + remainingTime +
                '}';
    }

    @Override
    public int compareTo(Object process) {
        int arrivalTimeToCompare = ((Process)process).arrivalTime;
        return this.arrivalTime - arrivalTimeToCompare;
    }

}
