package gui;

//processData = dataList
//resultData = dataList2 (or dataList3 idk lmao)

import java.text.DecimalFormat;

import algorithms.CPUScheduler;
import algorithms.FCFSScheduler;
import algorithms.RRScheduler;
import algorithms.SJNScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import process.Process;
import process.ProcessStats;

public class Controller {
	private final View view;
	private final DecimalFormat df = new DecimalFormat("#.##");
	
    // wallahi we're cooked    
	private ObservableList<Process> dataList = FXCollections.observableArrayList(); //this is processData, why are we using ObservableList?
	private ObservableList<ProcessStats> dataList2 = FXCollections.observableArrayList(); // contain processes that are not IDLE
	private ObservableList<ProcessStats> dataList3 = FXCollections.observableArrayList(); // for IDLE processes
	
	//HELPER FUNCTIONS FOR runSelectedAlgorithm() 
	
	//this function basically takes runFCFS() and runRR() and runSJN() and generalize them
	//uh maybe double check this shit if it doesnt work
	private void executeScheduler(CPUScheduler scheduler, Iterable<ProcessStats> stats) {
        dataList2.clear();
        dataList3.clear();
        
        stats.forEach(ps -> {
        	dataList3.add(ps);
            if (!ps.getProcess().getPid().equals("IDLE")) {
                dataList2.add(ps);
            }
        });

        double avgWT = scheduler.computeAverageWaitingTime(dataList2);
        double avgTAT = scheduler.computeAverageTurnaroundTime(dataList2);
        double cpuUtilization  = scheduler.computeCpuUtilization(dataList2);

        view.showStats(avgWT, avgTAT, cpuUtilization, df);
    }
	
	private void runFCFS() {
        FCFSScheduler scheduler = new FCFSScheduler();
        executeScheduler(scheduler, scheduler.schedule(dataList));
    }

    private void runSJN() {
        SJNScheduler scheduler = new SJNScheduler();
        executeScheduler(scheduler, scheduler.schedule(dataList));
    }

    private void runRR() {    	
    	dataList2.clear();
		dataList3.clear();
		
		RRScheduler rrScheduler = new RRScheduler(Integer.parseInt(view.quantum.getText()));
		dataList2.addAll(rrScheduler.schedule(dataList));
		dataList3.addAll(rrScheduler.ganttList(dataList));
		
		double avgWT = rrScheduler.computeAverageWaitingTime(dataList2);
		double avgTAT = rrScheduler.computeAverageTurnaroundTime(dataList2);
		double cpuUtilization = rrScheduler.computeCpuUtilization(dataList2);

		view.showStats(avgWT, avgTAT, cpuUtilization, df);
	}
    
    //END HELPTER SECTION FOR runSelectedAlgorithm()
	
	private void runSelectedAlgorithm() {
		if (dataList.isEmpty()) { //if processData empty
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
        view.clearButton.setOnAction(e -> {
            dataList.clear();
            dataList2.clear();
            dataList3.clear();
            view.resetUI();
        });
        
        view.submitButton.setOnAction(e -> {
        	runSelectedAlgorithm();
        	view.ganttChart(dataList3);
        });
        
        view.addBtn.setOnAction(e -> {
            view.showAddProcessDialog(dataList.size() + 1).ifPresent(p -> dataList.add(p));
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
		
		view.bindProcessTable(dataList);
	    view.bindResultTable(dataList2);
		
		bindActions();
	}
}