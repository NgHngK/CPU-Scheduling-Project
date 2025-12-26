import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import algorithms.CPUScheduler;
import algorithms.FCFSScheduler;
import algorithms.RRScheduler;
import algorithms.SJNScheduler;
import process.Process;
import process.ProcessStats;

public class MainCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            System.out.print("- PID (e.g., P1): ");
            String pid = sc.next();
            System.out.print("- Arrival time: ");
            int at = sc.nextInt();
            System.out.print("- Burst time: ");
            int bt = sc.nextInt();

            processes.add(new Process(pid, at, bt));
        }

        System.out.println("\n=== FCFS Scheduling ===");
        CPUScheduler fcfs = new FCFSScheduler();
        runAndPrint(fcfs, processes);

        System.out.println("\n=== SJN (SJF non-preemptive) Scheduling ===");
        CPUScheduler sjn = new SJNScheduler();
        runAndPrint(sjn, processes);

        System.out.println("\n=== Round Robin Scheduling ===");
        System.out.print("Enter time quantum for RR: ");

        int q = sc.nextInt();
        CPUScheduler rr = new RRScheduler(q);
        runAndPrint(rr, processes);

        sc.close();
    }

    private static void runAndPrint(CPUScheduler scheduler, List<Process> processes) {
        // Use a copy so algorithms don't interfere
        List<Process> copy = new ArrayList<>(processes);
        List<ProcessStats> stats = scheduler.schedule(copy);

        System.out.println("ProcessID\tArrivalTime\tBurstTime\tStartTime\tCompletionTime\tWaitingTime\tTurnaroundTime");

        for (ProcessStats ps : stats) {
            Process p = ps.getProcess();
            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d%n",
                    p.getPid(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    ps.getStartTime(),
                    ps.getCompletionTime(),
                    ps.getWaitingTime(),
                    ps.getTurnaroundTime());
        }

        double avgWt = scheduler.computeAverageWaitingTime(stats);
        double avgTat = scheduler.computeAverageTurnaroundTime(stats);
        System.out.printf("Average Waiting Time: %.2f%n", avgWt);
        System.out.printf("Average Turnaround Time: %.2f%n", avgTat);
    }
}
