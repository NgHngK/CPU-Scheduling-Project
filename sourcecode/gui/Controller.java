package gui;

//processData = dataList
//resultData = dataList2 (or dataList3 idk lmao)

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import algorithms.CPUScheduler;
import algorithms.FCFSScheduler;
import algorithms.RRScheduler;
import algorithms.SJNScheduler;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import process.Process;
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
        double cpuU  = scheduler.computeCpuUtilization(view.dataList2);

//      view.dataList2.addAll(scheduler.schedule(view.dataList));
//		view.dataList3.addAll(scheduler.schedule(view.dataList));
        
        //actual makes the shit visible??
        view.avgWTResult.setText(df.format(avgWT));
        view.avgTATResult.setText(df.format(avgTAT));
        view.cpuUtilizationResult.setText(df.format(cpuU) + "%");
        
        view.resultAvgWTLabel.setText(view.avgWTResult.getText());
        view.resultAvgTATLabel.setText(view.avgTATResult.getText());
        view.resultCpuULabel.setText(view.cpuUtilizationResult.getText());
        
        view.resultAvgWTLabel.setVisible(true);
		view.resultAvgTATLabel.setVisible(true);
		view.resultCpuULabel.setVisible(true);
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

//    private void runRR() {
//        if (view.quantum.getText().isEmpty()) {
//            error("Quantum time required");
//            return;
//        }
//
//        int q = Integer.parseInt(view.quantum.getText());
//        RRScheduler scheduler = new RRScheduler(q);
//        executeScheduler(scheduler, scheduler.schedule(view.dataList));
//    }
    
    private void runRR() {    	
    	view.dataList2.clear();
		view.dataList3.clear();
		
		RRScheduler rrScheduler = new RRScheduler(Integer.parseInt(view.quantum.getText()));
		view.dataList2.addAll(rrScheduler.schedule(view.dataList));
		view.dataList3.addAll(rrScheduler.ganttList(view.dataList));
		
		double avgWT = rrScheduler.computeAverageWaitingTime(view.dataList2);
		double avgTAT = rrScheduler.computeAverageTurnaroundTime(view.dataList2);
		double cpuUtilization = rrScheduler.computeCpuUtilization(view.dataList2);
		
		view.avgWTResult.setText(df.format(avgWT));
		view.avgTATResult.setText(df.format(avgTAT));
		view.cpuUtilizationResult.setText(df.format(cpuUtilization) + "%");
		
		view.resultAvgWTLabel.setText(view.avgWTResult.getText());
        view.resultAvgTATLabel.setText(view.avgTATResult.getText());
        view.resultCpuULabel.setText(view.cpuUtilizationResult.getText());
		
		view.resultAvgWTLabel.setVisible(true);
		view.resultAvgTATLabel.setVisible(true);
		view.resultCpuULabel.setVisible(true);
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
	
	private void ShowExitDialog() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm exit!");
		alert.setHeaderText("Are you sure you want to exit?");
		
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			System.exit(0);
		}	
	}
	
	//dude optimize this wtf is this shit lmao
	private void ShowMenuDialog() {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("MENU");
		
		int widthBtn = 210;
		int heigtBtn = 60;
		
		Button fcfsButton = new Button("Explain FCFS");
		fcfsButton.setOnAction(e -> {
			Dialog<TextArea> d = new Dialog<TextArea>();
			TextArea t = new TextArea("This is the simplest scheduling algorithm where processes are executed in the order they arrive in the ready queue. It does not consider process priority or burst time, which can lead to convoy effect, where long processes delay short ones.");
			t.setStyle("-fx-font-size: 18px;");
			t.setEditable(false);
			t.setWrapText(true);
			d.getDialogPane().setContent(t);
			d.getDialogPane().setPrefSize(300, 300);
			d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			d.showAndWait();
			});
		Button sjnButton = new Button("Explain SJN");
		sjnButton.setOnAction(e -> {
			Dialog<TextArea> d = new Dialog<TextArea>();
			TextArea t = new TextArea("SJN, also known as Shortest Job First (SJF), selects the process with the shortest burst time to execute next. This minimizes average waiting time but requires knowing the burst time of each process in advance, which is often impractical in real-world scenarios.");
			t.setStyle("-fx-font-size: 18px;");
			t.setEditable(false);
			t.setWrapText(true);
			d.getDialogPane().setContent(t);
			d.getDialogPane().setPrefSize(300, 300);
			d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			d.showAndWait();
			});
		Button rrButton = new Button("Explain RR");
		rrButton.setOnAction(e -> {
			Dialog<TextArea> d = new Dialog<TextArea>();
			TextArea t = new TextArea("RR allocates each process a fixed time slice (quantum) in a cyclic order. Once a process’s quantum expires, it is moved to the back of the queue, ensuring fairness. It is simple and ensures all processes get executed but can cause frequent context switching, adding overhead.");
			t.setStyle("-fx-font-size: 18px;");
			t.setEditable(false);
			t.setWrapText(true);
			d.getDialogPane().setContent(t);
			d.getDialogPane().setPrefSize(300, 300);
			d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			d.showAndWait();
			});
		
		fcfsButton.setPrefSize(widthBtn, heigtBtn);
		sjnButton.setPrefSize(widthBtn, heigtBtn);
		rrButton.setPrefSize(widthBtn, heigtBtn);
		
		VBox box = new VBox(10, fcfsButton, sjnButton, rrButton);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		dialog.getDialogPane().setContent(box);
    	dialog.getDialogPane().setPrefSize(210, 200);
    	dialog.showAndWait();
	}
	
	@SuppressWarnings("static-access")
	public void ganttChart(ObservableList<ProcessStats> dataList3, AnchorPane centerPane) {
		
		HashMap<String, Color> map = new HashMap<String, Color>();

    	centerPane.getChildren().clear();
    	int boxWidth = 800;
    	
    	Label ganttLabel = new Label(" GANTT CHART");
    	// Contain process pane
    	HBox hbox2 = new HBox(10);
    	hbox2.setPrefSize(boxWidth, 50);
    	hbox2.setAlignment(Pos.BOTTOM_LEFT);

    	 // Contain data
    	HBox hbox3 = new HBox();
    	hbox3.setPrefSize(boxWidth + 30, 10);
    	hbox3.setAlignment(Pos.TOP_LEFT);
    	//hbox3.setStyle("-fx-background-color: #ddd;");
    	
    	int sizeList3 = dataList3.size();
    	int scheduleCompletionTime = dataList3.get(sizeList3 - 1).getCompletionTime();
    	
    	AnchorPane.setBottomAnchor(ganttLabel, 170.0);
		AnchorPane.setLeftAnchor(ganttLabel, 0.0);
    	  	
    	AnchorPane.setBottomAnchor(hbox2, 110.0);
		AnchorPane.setLeftAnchor(hbox2, 20.0);
		
		AnchorPane.setBottomAnchor(hbox3, 100.0);
		AnchorPane.setLeftAnchor(hbox3, 10.0);
    	
    	for (ProcessStats ps : dataList3) {
    		
    		Label timeLabel = new Label(Integer.toString(ps.getStartTime())); 
    		timeLabel.setStyle("-fx-font-size: 9px;");
    		StackPane timeStackPane = new StackPane();
    		timeStackPane.setPrefSize(10, 20);
    		timeStackPane.getChildren().add(timeLabel);
    		
    		double width = (ps.getCompletionTime() - ps.getStartTime()) * ((boxWidth - 200) / scheduleCompletionTime);
    		String text = ps.getProcess().getPid();
    		
    		// Set color for each pc
    		int a = random.nextInt(255 - 100 + 1) + 100;
    		int b = random.nextInt(255 - 100 + 1) + 100;
    		int c = random.nextInt(255 - 100 + 1) + 100;
    		map.putIfAbsent(text , Color.rgb(a, b, c));
    		StackPaneRactangleWithText psRectangle = new StackPaneRactangleWithText(width, 50, text, map.get(text)); // process pane with text in center
    		
    		
    		if (psRectangle.getText() == "IDLE") {
    			psRectangle = new StackPaneRactangleWithText(width, 50, text, Color.rgb(255, 255, 255));   // White for IDLE pc
    		}
   
    		hbox2.getChildren().add(psRectangle.getStackPane());
    		hbox3.setMargin(timeStackPane, new Insets(0, width, 0, 0));
    		hbox3.getChildren().add(timeStackPane);
    		
    	}
    	Label compTimeLabel = new Label(Integer.toString(scheduleCompletionTime));
    	compTimeLabel.setStyle("-fx-font-size: 9px;");
    	hbox3.getChildren().add(compTimeLabel);
    	centerPane.getChildren().addAll(hbox2, hbox3, ganttLabel);
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
    		view.dataList.clear(); //clear processData
    		view.dataList2.clear(); //clear resultData
    		view.resultAvgWTLabel.setVisible(false);
        	view.resultAvgTATLabel.setVisible(false);
        	view.resultCpuULabel.setVisible(false);
        	view.centerPane.getChildren().clear(); //clear ganttPane
    	});
        
        view.submitButton.setOnAction(e -> {
        	runSelectedAlgorithm();
        	ganttChart(view.dataList3, view.centerPane);
        });
        
        view.addBtn.setOnAction(e -> showAddProcessDialog());
        
        view.exitButton.setOnAction(e -> ShowExitDialog());
        
        view.menuButton.setOnAction(e -> ShowMenuDialog());
	}
	
	public Controller(View view) {
		this.view = view;
		bindActions();
	}
}
