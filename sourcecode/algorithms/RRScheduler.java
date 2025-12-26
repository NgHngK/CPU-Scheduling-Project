
package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import process.ProcessStats;
import process.Process;

public class RRScheduler extends CPUScheduler {
	private int quantum;
	private final Process IDLE_PROCESS = new Process("IDLE", 0, 1);

    public RRScheduler(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be > 0");
        }
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    @Override
    public List<ProcessStats> schedule(List<Process> processes) {
        List<Process> list = new ArrayList<Process>();
        for (Process p : processes) {
            list.add(p);
        }

        // Sort by arrival time
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getArrivalTime() > list.get(j).getArrivalTime()) {
                    Process temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }

        int n = list.size();
        List<ProcessStats> result = new ArrayList<ProcessStats>();
        if (n == 0) {
            return result;
        }

        int[] remainingTime = new int[n];
        int[] firstStart = new int[n];
        int[] completionTime = new int[n];

        for (int i = 0; i < n; i++) {
            remainingTime[i] = list.get(i).getBurstTime();
            firstStart[i] = -1;      // not started yet
            completionTime[i] = 0;
        }

        // Ready queue stores indices of processes in "list"
        List<Integer> readyQueue = new ArrayList<Integer>();

        int currentTime = list.get(0).getArrivalTime();
        int index = 0; // next process in sorted list to arrive

        // Add processes that arrive at the initial currentTime
        while (index < n && list.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(index);
            index++;
        }

        while (!readyQueue.isEmpty() || index < n) {
            if (readyQueue.isEmpty()) {
                // CPU idle until next process arrives
                currentTime = list.get(index).getArrivalTime();
                while (index < n && list.get(index).getArrivalTime() <= currentTime) {
                    readyQueue.add(index);
                    index++;
                }
                continue;
            }

            // Get first process in the queue
            int procIndex = readyQueue.remove(0);
            @SuppressWarnings("unused")
			Process p = list.get(procIndex);

            if (firstStart[procIndex] == -1) {
                firstStart[procIndex] = currentTime; // first time it gets CPU
            }

            int rem = remainingTime[procIndex];
            int execTime = quantum;
            if (rem < quantum) {
                execTime = rem;
            }

            currentTime += execTime;
            rem -= execTime;
            remainingTime[procIndex] = rem;

            // Add processes that arrived during this time slice
            while (index < n && list.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(index);
                index++;
            }

            if (rem == 0) {
                completionTime[procIndex] = currentTime;
            } else {
                // still remaining -> go back to end of queue
                readyQueue.add(procIndex);
            }
        }

        // Build ProcessStats in the sorted order
        for (int i = 0; i < n; i++) {
            Process p = list.get(i);
            int start = firstStart[i];
            int completion = completionTime[i];
            ProcessStats stats = new ProcessStats(p, start, completion);
            result.add(stats);
        }

        return result;
    }
    
    
    public List<ProcessStats> ganttList(List<Process> processes) {
        // Sorted by Arri
        List<Process> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        // raw list
        List<ProcessStats> rawResult = new ArrayList<>();
        
        // queue
        List<Process> queue = new ArrayList<>();
        
        // remain time of each pc
        Map<Process, Integer> remainingTimeMap = new HashMap<>();
        // initial remain time
        for (Process p : sortedProcesses) {
            remainingTimeMap.put(p, p.getBurstTime());
        }

        int currentTime = 0;
        int index = 0; 

        while (!queue.isEmpty() || index < sortedProcesses.size()) {
            
            // Initial check
            while (index < sortedProcesses.size() && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                queue.add(sortedProcesses.get(index));
                index++;
            }

            
            if (queue.isEmpty()) {
                
                // get nextAri
                Process nextProcess = sortedProcesses.get(index);
                int gapDuration = nextProcess.getArrivalTime() - currentTime;

                if (gapDuration > 0) {
                    
                    rawResult.add(new ProcessStats(IDLE_PROCESS, currentTime, currentTime + gapDuration));
                    currentTime += gapDuration;
                } else {
                    // gap < 0
                    currentTime = nextProcess.getArrivalTime();
                }
        
                continue; 
            } else {

                Process currentProcess = queue.remove(0);
                int currentRemTime = remainingTimeMap.get(currentProcess);

                int executionTime = Math.min(currentRemTime, quantum);

                rawResult.add(new ProcessStats(currentProcess, currentTime, currentTime + executionTime));

                currentTime += executionTime;

                int newRemTime = currentRemTime - executionTime;
                remainingTimeMap.put(currentProcess, newRemTime);

                while (index < sortedProcesses.size() && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                    queue.add(sortedProcesses.get(index));
                    index++;
                }

                if (newRemTime > 0) {
                    queue.add(currentProcess);
                }
            }
        }

        // Merge
        return mergeConsecutiveProcess(rawResult);
    }

    // merge same pc
    private List<ProcessStats> mergeConsecutiveProcess(List<ProcessStats> rawList) {
        List<ProcessStats> mergedResult = new ArrayList<>();
        if (rawList.isEmpty()) return mergedResult;

        ProcessStats currentStats = rawList.get(0);

        for (int i = 1; i < rawList.size(); i++) {
            ProcessStats nextStats = rawList.get(i);

            // check same pc
            boolean isSameProcess = false;
            if (currentStats.getProcess() == null && nextStats.getProcess() == null) {
                isSameProcess = true; 
            } else if (currentStats.getProcess() != null && nextStats.getProcess() != null) {

                if (currentStats.getProcess().getPid().equals(nextStats.getProcess().getPid())) {
                    isSameProcess = true;
                }
            }

            if (isSameProcess) {

                currentStats = new ProcessStats(
                    currentStats.getProcess(), 
                    currentStats.getStartTime(), 
                    nextStats.getCompletionTime()
                );
            } else {

                mergedResult.add(currentStats);
                currentStats = nextStats;
            }
        }

        mergedResult.add(currentStats);

        return mergedResult;
    }
} 
