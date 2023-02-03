package CPU_Simulation;

enum CPUState {
    BUSY, READY
}


public class CPU  {
    private Process process;
    private int CPUID;
    private CPUState state;
    private int utilization;
    private int timeQuantumTimer;

    public CPU() {
        process = null;
        CPUID = -1;
        state = CPUState.BUSY;
        utilization = 0;
        timeQuantumTimer = 0;
    }

    public CPU(Process process) {
        this.process = process;
    }

    public Process getProcess() { return process; }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getCPUID() {
        return CPUID;
    }

    public void setCPUID(int CPUID) {
        this.CPUID = CPUID;
    }

    public CPUState getState() {
        return state;
    }

    public void setState(CPUState state) {
        this.state = state;
    }

    public int getUtilization() { return utilization; }

    public void setUtilization(int utilization) { this.utilization = utilization; }

    public int getTimeQuantumTimer() { return timeQuantumTimer; }

    public void setTimeQuantumTimer(int timeQuantum) { this.timeQuantumTimer = timeQuantum; }

    @Override
    public String toString() {
        return "CPU{" +
                "process=" + process +
                '}';
    }

}
