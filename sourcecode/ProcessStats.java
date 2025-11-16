public class ProcessStats {
    private final Process process;
    private final int startTime;
    private final int completionTime;
    private final int waitingTime;
    private final int turnaroundTime;

    public ProcessStats(Process process, int startTime, int completionTime) {
        this.process = process;
        this.startTime = startTime;
        this.completionTime = completionTime;

        int arrival = process.getArrivalTime();
        int burst = process.getBurstTime();

        this.turnaroundTime = completionTime - arrival;
        this.waitingTime = turnaroundTime - burst;
    }

    public Process getProcess() {
        return process;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }
}
