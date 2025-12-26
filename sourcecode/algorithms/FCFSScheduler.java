package algorithms;
import java.util.ArrayList;
import java.util.List;

import process.Process;
import process.ProcessStats;

public class FCFSScheduler extends CPUScheduler {
	
	private final Process IDLE_PROCESS = new Process("IDLE", 0, 1);
	
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

        List<ProcessStats> result = new ArrayList<ProcessStats>();
        int currentTime = 0;

        for (int i = 0; i < list.size(); i++) {
            Process p = list.get(i);

            if (currentTime < p.getArrivalTime()) {
            	
            	int idleStart = currentTime;
            	int idleCompletion = p.getArrivalTime();
            	
            	if (idleCompletion > idleStart) {
            		ProcessStats idleStats = new ProcessStats(IDLE_PROCESS, idleStart, idleCompletion);
            		result.add(idleStats);
            	}
            	
                currentTime = p.getArrivalTime();
            }

            int start = currentTime;
            int completion = start + p.getBurstTime();
            
            ProcessStats stats = new ProcessStats(p, start, completion);
            result.add(stats);

            currentTime = completion;
        }

        return result;
    }
}
