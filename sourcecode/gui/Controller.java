package gui;

//processData = dataList
//resultData = dataList2 (or dataList3 idk lmao)

import java.text.DecimalFormat;
import java.util.Random;

import algorithms.CPUScheduler;
import algorithms.FCFSScheduler;
import algorithms.RRScheduler;
import algorithms.SJNScheduler;
import process.ProcessStats;

public class Controller {
	private final View view;
	private final DecimalFormat df = new DecimalFormat("#.##");
	Random random = new Random();
	
	//HELPER FUNCTIONS FOR runSelectedAlgorithm() 
	
	//this function basically takes runFCFS() and runRR() and runSJN() and generalize them
	//uh maybe double check this shit if it doesnt work
	private void executeScheduler(CPUScheduler scheduler, Iterable<ProcessStats> stats) {
        view.dataList2.clear();
        view.dataList3.clear();
        
        stats.forEach(ps -> {
        	view.dataList3.add(ps);
            if (!ps.getProcess().getPid().equals("IDLE")) {
                view.dataList2.add(ps);
            }
        });

        double avgWT = scheduler.computeAverageWaitingTime(view.dataList2);
        double avgTAT = scheduler.computeAverageTurnaroundTime(view.dataList2);
        double cpuUtilization  = scheduler.computeCpuUtilization(view.dataList2);

        view.showStats(avgWT, avgTAT, cpuUtilization, df);
    }
	
	private void runFCFS() {
        FCFSScheduler scheduler = new FCFSScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.dataList));
    }

    private void runSJN() {
        SJNScheduler scheduler = new SJNScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.dataList));
    }

    private void runRR() {    	
    	view.dataList2.clear();
		view.dataList3.clear();
		
		RRScheduler rrScheduler = new RRScheduler(Integer.parseInt(view.quantum.getText()));
		view.dataList2.addAll(rrScheduler.schedule(view.dataList));
		view.dataList3.addAll(rrScheduler.ganttList(view.dataList));
		
		double avgWT = rrScheduler.computeAverageWaitingTime(view.dataList2);
		double avgTAT = rrScheduler.computeAverageTurnaroundTime(view.dataList2);
		double cpuUtilization = rrScheduler.computeCpuUtilization(view.dataList2);

		view.showStats(avgWT, avgTAT, cpuUtilization, df);
	}
    
    //END HELPTER SECTION FOR runSelectedAlgorithm()
	
	private void runSelectedAlgorithm() {
		if (view.dataList.isEmpty()) { //if processData empty
            view.error("No processes to schedule");
            return;
        }
		
		String algo = view.algoMenuButton.getText();
		
		switch (algo) {
	        case "First Come First Serve" -> runFCFS();
	        case "Shortest Job Next" -> runSJN();
	        case "Round Robin" -> runRR();
	        default -> view.error("Choose a scheduling algorithm");
		}
	}
	
	private void bindActions() {		
		//binding the algo buttons in the algo selection drop down
		view.fcfs.setOnAction(e -> {
    		view.algoMenuButton.setText("First Come First Serve");
    		view.quantum.setVisible(false);
    	});
		
		view.sjn.setOnAction(e -> {
            view.algoMenuButton.setText("Shortest Job Next");
            view.quantum.setVisible(false);
        });

        view.rr.setOnAction(e -> {
            view.algoMenuButton.setText("Round Robin");
            view.quantum.setVisible(true);
        });
        
        //clear button        
        view.clearButton.setOnAction(e -> view.resetUI());
        
        view.submitButton.setOnAction(e -> {
        	runSelectedAlgorithm();
        	view.ganttChart(view.dataList3);
        });
        
        view.addBtn.setOnAction(e -> {
            view.showAddProcessDialog(view.dataList.size() + 1).ifPresent(p -> view.dataList.add(p));
        });
        
        view.exitButton.setOnAction(e -> {
            if (view.confirmExit()) {
                System.exit(0);
            }
        });
        
        view.menuButton.setOnAction(e -> view.showMenuDialog());
	}
	
	public Controller(View view) {
		this.view = view;
		bindActions();
	}
}