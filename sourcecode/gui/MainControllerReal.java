package gui;

//processData = dataList
//resultData = dataList2 (or dataList3 idk lmao)

import java.text.DecimalFormat;

import algorithm.CPUScheduler;
import algorithm.FCFSScheduler;
import algorithm.RRScheduler;
import algorithm.SJNScheduler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import process.Process;
import process.ProcessStats;

public class MainControllerReal {
	private final MainViewReal view;
	private final DecimalFormat df = new DecimalFormat("#.##");
	
	
	//HELPER FUNCTIONS FOR runSelectedAlgorithm() 
	
	//this function basically takes runFCFS() and runRR() and runSJN() and generalize them
	//uh maybe double check this shit if it doesnt work
	private void executeScheduler(CPUScheduler scheduler, Iterable<ProcessStats> stats) {
        view.dataList2.clear();
        view.dataList3.clear();
        
        stats.forEach(ps -> {
            if (!ps.getProcess().getPid().equals("IDLE")) {
                view.dataList2.add(ps);
            }
        });

        double avgWT = scheduler.computeAverageWaitingTime(view.dataList2);
        double avgTAT = scheduler.computeAverageTurnaroundTime(view.dataList2);
        double cpuU  = scheduler.computeCpuUtilization(view.dataList2);

        //actual makes the shit visible??
        view.avgWTResult.setText(df.format(avgWT));
        view.avgTATResult.setText(df.format(avgTAT));
        view.cpuUtilizationResult.setText(df.format(cpuU) + "%");
    }
	
	private void runFCFS() {
        FCFSScheduler scheduler = new FCFSScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.dataList));
    }

    private void runSJN() {
        SJNScheduler scheduler = new SJNScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.dataList));
    }
    
    private void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void runRR() {
        if (view.quantum.getText().isEmpty()) {
            error("Quantum time required");
            return;
        }

        int q = Integer.parseInt(view.quantum.getText());
        RRScheduler scheduler = new RRScheduler(q);
        executeScheduler(scheduler, scheduler.schedule(view.dataList));
    }
    
    //END HELPTER SECTION FOR runSelectedAlgorithm()
	
	private void runSelectedAlgorithm() {
		if (view.dataList.isEmpty()) { //if processData empty
            error("No processes to schedule");
            return;
        }
		
		String algo = view.algoMenuButton.getText();
		
		switch (algo) {
	        case "First Come First Serve" -> runFCFS();
	        case "Shortest Job Next" -> runSJN();
	        case "Round Robin" -> runRR();
	        default -> error("Choose a scheduling algorithm");
		}
	}
	
	//doesnt update the table!!
	private void showAddProcessDialog() {
    	Dialog<Process> dialog = new Dialog<Process>();
    	dialog.setTitle("Add new process");
    	
    	//Button
    	ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
    	
    	//Input Fields
    	TextField arrField = new TextField();
    	arrField.setPromptText("Arrival Time (Enter an positive)");
    	// Must enter an integer.
    	arrField.setTextFormatter(new TextFormatter<>(change -> {
    		String text = change.getControlNewText();
    		if (text.matches("\\d*")) {
    			return change;
    		}
    		return null;
    	}));
    	
    	TextField burField = new TextField();
    	burField.setPromptText("Burst Time (Enter an positive)");
    	// Must enter an integer.
    	burField.setTextFormatter(new TextFormatter<>(change -> {
    		String text = change.getControlNewText();
    		if (text.matches("\\d*")) {
    			return change;
    		}
    		return null;
    	}));
    	
    	VBox box = new VBox(10, new Label("Arrival Time: "), arrField,
    							new Label("Burst Time: "), burField);
    	
    	dialog.getDialogPane().setContent(box);
    	dialog.getDialogPane().setPrefSize(210, 200);
    	
    	// get data
    	dialog.setResultConverter(btn -> {
    		if (btn == addButtonType) {
    			return new Process("P" + Integer.toString(view.dataList.size() + 1), Integer.parseInt(arrField.getText()), Integer.parseInt(burField.getText()));
    		}
    		return null;
    	});
    	
    	dialog.showAndWait().ifPresent(p -> {
    		view.dataList.add(p);
    		});
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
//        view.clearBtn.setOnAction(e -> {
//            view.processData.clear();
//            view.resultData.clear();
//            view.avgWTValue.setText("");
//            view.avgTATValue.setText("");
//            view.cpuUtilValue.setText("");
//            view.ganttPane.getChildren().clear();
//        });
        view.clearButton.setOnAction(e -> {
    		view.dataList.clear(); //clear processData
    		view.dataList2.clear(); //clear resultData
    		view.resultAvgWTLabel.setVisible(false);
        	view.resultAvgTATLabel.setVisible(false);
        	view.resultCpuULabel.setVisible(false);
        	view.centerPane.getChildren().clear(); //clear ganttPane
    	});
        
        view.submitButton.setOnAction(e -> runSelectedAlgorithm());
        
        view.addBtn.setOnAction(e -> showAddProcessDialog());
        
//        view.exitButton.setOnAction(e -> ShowExitDialog());
        
//        view.menuButton.setOnAction(e -> ShowMenuDialog());
	}
	
	public MainControllerReal(MainViewReal view) {
		this.view = view;
		bindActions();
	}
}
