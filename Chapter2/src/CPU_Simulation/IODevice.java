package CPU_Simulation;

enum IODeviceState {
    READY, BUSY
}


public class IODevice {
    private int IOID;
    private Process process;
    private IODeviceState status;

    public IODevice() {
        IOID = -1;
        process = null;
        status = IODeviceState.READY;
    }

    public IODevice(int IOID, Process process) {
        this.IOID = IOID;
        this.process = process;
        status = IODeviceState.READY;
    }

    public int getIOID() {
        return IOID;
    }

    public void setIOID(int IOID) {
        this.IOID = IOID;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public IODeviceState getStatus() { return status; }

    public void setStatus(IODeviceState status) { this.status = status; }
}
