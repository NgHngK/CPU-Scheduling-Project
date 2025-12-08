package gui;

import process.Process;
import process.ProcessStats;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import algorithm.*;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestJavaFX extends Application {
	
	TableView<Process> tableView; //ProcessList
	TableView<ProcessStats> tableView2; //Result ProcessList
	ObservableList<Process> dataList;
	ObservableList<ProcessStats> dataList2; // for not contain IDLE
	ObservableList<ProcessStats> dataList3; // for contain IDLE
	Label avgWTResult = new Label("A");
	Label avgTATResult = new Label("A");
	Label cpuUtilizationResult = new Label();
	DecimalFormat df = new DecimalFormat("#.##");
	Random random = new Random();
	

	@SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
    	//========================= PROCESS TABLE ================================
    	// Create process table
    	tableView = new TableView<>();
    	tableView.setEditable(false);
    	int sizeOfCol1 = 100;
    	tableView.setPrefSize(sizeOfCol1 * 3, sizeOfCol1 * 3);
    	
    	dataList = FXCollections.observableArrayList();
    	tableView.setItems(dataList);
    	
    	// Create column pid
    	TableColumn<Process, String> pidColumn = new TableColumn<>("Process ID");
    	pidColumn.setCellValueFactory(c ->
    		new SimpleStringProperty(c.getValue().getPid())
    	);
    	
    	// Arrival Column
    	TableColumn<Process, Number> arrColumn = new TableColumn<>("Arrival Time");
    	arrColumn.setCellValueFactory(c -> 
    		new SimpleIntegerProperty(c.getValue().getArrivalTime())
    	);
    	
    	// Burst Column
    	TableColumn<Process, Number> burColumn = new TableColumn<>("Burst Time");
    	burColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getBurstTime())
    	);
    	
    	// Add Cols to table
    	tableView.getColumns().addAll(pidColumn, arrColumn, burColumn);
    	
    	pidColumn.setPrefWidth(sizeOfCol1);
    	arrColumn.setPrefWidth(sizeOfCol1);
    	burColumn.setPrefWidth(sizeOfCol1);
    	//========================================================================
    	
    	//=============================RESULT TABLE==================================
    	tableView2 = new TableView<>();
    	tableView2.setEditable(false);
    	int sizeOfCol2 = 100;
    	tableView2.setPrefSize(sizeOfCol2 * 5 + 20, sizeOfCol2 * 3);;
    	
    	dataList2 = FXCollections.observableArrayList();
    	dataList3 = FXCollections.observableArrayList();
    	tableView2.setItems(dataList2);
    	
    	TableColumn<ProcessStats, String> pidColumn2 = new TableColumn<ProcessStats, String>("Process ID");
    	pidColumn2.setCellValueFactory(c -> 
    		new SimpleStringProperty(c.getValue().getProcess().getPid())
    		);

    	TableColumn<ProcessStats, Number> arrColumn2 = new TableColumn<ProcessStats, Number>("Arrival Time");
    	arrColumn2.setCellValueFactory(c -> 
    		new SimpleIntegerProperty(c.getValue().getProcess().getArrivalTime())
    		);
    	
    	TableColumn<ProcessStats, Number> burColumn2 = new TableColumn<>("Burst Time");
    	burColumn2.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getProcess().getBurstTime())
    		);
    	
    	TableColumn<ProcessStats, Number> wColumn = new TableColumn<>("Waiting Time");
    	wColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getWaitingTime())
    		);
    	
    	TableColumn<ProcessStats, Number> tatColumn = new TableColumn<>("Turnaround Time");
    	tatColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getTurnaroundTime())
    		);
    	
    	tableView2.getColumns().addAll(pidColumn2, arrColumn2, burColumn2, wColumn, tatColumn);
  
    	pidColumn2.setPrefWidth(sizeOfCol2);
    	arrColumn2.setPrefWidth(sizeOfCol2);
    	burColumn2.setPrefWidth(sizeOfCol2);
    	wColumn.setPrefWidth(sizeOfCol2);
    	tatColumn.setPrefWidth(sizeOfCol2 + 20);
    	//==============================================================================
    	
    	// ADD PROCESS BUTTON
    	Button addBtn = new Button("Add process");
    	addBtn.setOnAction(e -> showAddProcessDialog());
    	
    	// ENTER QUANTUM FIELD
    	TextField quantum = new TextField();
    	// Must enter an integer.
    	quantum.setTextFormatter(new TextFormatter<>(change -> {
    		String text = change.getControlNewText();
    		if (text.matches("\\d*")) {
    			return change;
    		}
    		return null;
    	}));
    	quantum.setPromptText("Quantum Time");
    	quantum.setVisible(false);
    	
    	MenuItem fcfs = new MenuItem("First Come First Serve");
    	MenuItem sjn = new MenuItem("Shortest Job Next");
    	MenuItem rr = new MenuItem("Round Robin");
    	
    	MenuButton algoMenuButton = new MenuButton("Algorithms");
    	algoMenuButton.setPrefSize(250, 30);
    	
    	algoMenuButton.getItems().addAll(fcfs, sjn, rr);
    	
    	fcfs.setOnAction(e -> {
    		algoMenuButton.setText("First Come First Serve");
    		quantum.setVisible(false);
    		});
    	sjn.setOnAction(e -> {
    		algoMenuButton.setText("Shortest Job Next");
    		quantum.setVisible(false);
    	});
    	rr.setOnAction(e -> {
    		algoMenuButton.setText("Round Robin");
    		quantum.setVisible(true);
    		//quantum.requestFocus();
    	});
    	
    	//------------------------------------------------------------------------------
    	VBox avgWTBox = new VBox(10);
    	VBox avgTATBox = new VBox(10);
    	VBox cpuUtilizationBox = new VBox(10);
    	
    	Label avgWTLabel = new Label("AVG Waiting Time");
    	Label avgTATLabel= new Label("AVG Turnaround Time");
    	Label cpuULabel = new Label("CPU Utilization");
    	
    	
    	Label resultAvgWTLabel = new Label();
    	Label resultAvgTATLabel = new Label();
    	Label resultCpuULabel = new Label();
    	
    	avgWTBox.getChildren().addAll(avgWTLabel, resultAvgWTLabel);
    	avgTATBox.getChildren().addAll(avgTATLabel, resultAvgTATLabel);
    	cpuUtilizationBox.getChildren().addAll(cpuULabel, resultCpuULabel);
    	
    	avgWTBox.setAlignment(Pos.CENTER);
    	avgTATBox.setAlignment(Pos.CENTER);
    	cpuUtilizationBox.setAlignment(Pos.CENTER);
    	
    	resultAvgWTLabel.setVisible(false);
    	resultAvgTATLabel.setVisible(false);
    	resultCpuULabel.setVisible(false);
    	//------------------------------------------------------------------------------
    	AnchorPane centerPane = new AnchorPane(); 
    	
    	// CLEAR BUTTON
    	Button clearButton = new Button("Clear all processes");
    	clearButton.setOnAction(e -> {
    		dataList.clear();
    		dataList2.clear();
    		resultAvgWTLabel.setVisible(false);
        	resultAvgTATLabel.setVisible(false);
        	resultCpuULabel.setVisible(false);
        	centerPane.getChildren().clear();
    	});

    	
    	// Button to run algo 
    	Button submitButton = new Button("Submit");
    	 
    	submitButton.setOnAction(e -> {
    		if (algoMenuButton.getText().equals("First Come First Serve")) {
    			runFCFS();
    			resultAvgWTLabel.setText(avgWTResult.getText());
    			resultAvgTATLabel.setText(avgTATResult.getText());
    			resultCpuULabel.setText(cpuUtilizationResult.getText());
    			resultAvgWTLabel.setVisible(true);
    			resultAvgTATLabel.setVisible(true);
    			resultCpuULabel.setVisible(true);
    			
    			ganttChart(dataList3, centerPane);
    		} else if (algoMenuButton.getText().equals("Shortest Job Next")) {
				runSJN();
				resultAvgWTLabel.setText(avgWTResult.getText());
    			resultAvgTATLabel.setText(avgTATResult.getText());
    			resultCpuULabel.setText(cpuUtilizationResult.getText());
    			resultAvgWTLabel.setVisible(true);
    			resultAvgTATLabel.setVisible(true);
    			resultCpuULabel.setVisible(true);
    			
    			ganttChart(dataList3, centerPane);
			} else if (algoMenuButton.getText().equals("Round Robin")) {
				runRR(Integer.parseInt(quantum.getText()));
				resultAvgWTLabel.setText(avgWTResult.getText());
    			resultAvgTATLabel.setText(avgTATResult.getText());
    			resultCpuULabel.setText(cpuUtilizationResult.getText());
    			resultAvgWTLabel.setVisible(true);
    			resultAvgTATLabel.setVisible(true);
    			resultCpuULabel.setVisible(true);
    			
    			ganttChart(dataList3, centerPane);
			}
    	});

    	// ================== PROCESS LIST (RIGT OF BORDER) ==================
    	VBox vbox1 = new VBox(10);
    	vbox1.getChildren().addAll(tableView, addBtn, clearButton);
    	vbox1.setAlignment(Pos.TOP_CENTER);
    	
    	AnchorPane pcPane = new AnchorPane();
    	AnchorPane.setTopAnchor(vbox1, 10.0);
    	AnchorPane.setRightAnchor(vbox1, 10.0);

    	pcPane.getChildren().add(vbox1);
    	// ===================================================================
    	
    	// ====================== ALGO CHOOSE N SUBMIT (LEFT BORDER) ==============
    	VBox vbox2 = new VBox(10);
    	vbox2.getChildren().addAll(algoMenuButton, quantum, submitButton);
    	vbox2.setAlignment(Pos.TOP_LEFT);
    	
    	AnchorPane aPane = new AnchorPane();
    	
    	AnchorPane.setTopAnchor(vbox2, 10.0);
    	AnchorPane.setLeftAnchor(vbox2, 10.0);
    	
    	aPane.getChildren().add(vbox2);
    	//========================================================================
    	
    	// -------------------- PROCESS RESULT LIST --------------------  
    	HBox hbox1 = new HBox(170);
    	hbox1.getChildren().addAll(tableView2, avgWTBox, avgTATBox, cpuUtilizationBox);
    	hbox1.setAlignment(Pos.CENTER);
    	
    	AnchorPane pcrPane = new AnchorPane();
    	AnchorPane.setLeftAnchor(hbox1, 10.0);
    	AnchorPane.setBottomAnchor(hbox1, 10.0);
    	
    	pcrPane.getChildren().add(hbox1);
    	// ------------------------------------------------------- 
    	
    	// ============================= HELP MENU =============================
    	Button menuButton = new Button("Menu");
    	
    	AnchorPane.setTopAnchor(menuButton, 110.0);
    	AnchorPane.setLeftAnchor(menuButton, 10.0);
    	aPane.getChildren().add(menuButton);
    	
    	menuButton.setOnAction(e -> ShowMenuDialog());
    	// ====================================================================== 
    	
    	// ============================= EXIT BTN =============================
    	Button exitButton = new Button("EXIT");
    	AnchorPane.setTopAnchor(exitButton, 140.0);
    	AnchorPane.setLeftAnchor(exitButton, 10.0);
    	exitButton.setOnAction(e -> ShowExitDialog());
    	aPane.getChildren().add(exitButton);
    	
    	
    	// ====================================================================

    	//======================= ROOT =======================
        BorderPane layout = new BorderPane();
        layout.setRight(pcPane); // Process list
        layout.setCenter(centerPane); // Containing ganttchart
        layout.setLeft(aPane); // Algorithm list
        layout.setBottom(pcrPane);
        layout.setPadding(new Insets(20));
        
        //CSS
        layout.setStyle(
        	    "-fx-font-family: 'Menlo';" +
        	    "-fx-font-size: 12px;" +
        	    "-fx-font-weight: bold;"
        	);
        
        Scene scene = new Scene(layout, 1600, 900);

        primaryStage.setTitle("CPU Scheduling");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        //====================================================
    }
	// PROCESS DIALOG
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
    			return new Process("P" + Integer.toString(dataList.size() + 1), Integer.parseInt(arrField.getText()), Integer.parseInt(burField.getText()));
    		}
    		return null;
    	});
    	
    	dialog.showAndWait().ifPresent(p -> {
    		dataList.add(p);
    		});
    }
	
	// MENU DIALOG
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
	// EXIT DIALOG
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
	
	private void runFCFS() {
		dataList2.clear();
		dataList3.clear();
		
		FCFSScheduler fcfsScheduler = new FCFSScheduler();
		dataList2.addAll(fcfsScheduler.schedule(dataList));
		dataList3.addAll(fcfsScheduler.schedule(dataList));
		
		dataList2.removeIf(p ->
			p.getProcess().getPid().equals("IDLE")
		);
		
		double avgWT = fcfsScheduler.computeAverageWaitingTime(dataList2);
		double avgTAT = fcfsScheduler.computeAverageTurnaroundTime(dataList2);
		double cpuUtilization = fcfsScheduler.computeCpuUtilization(dataList2);
		
		avgWTResult.setText(df.format(avgWT));
		avgTATResult.setText(df.format(avgTAT));
		cpuUtilizationResult.setText(df.format(cpuUtilization) + "%");
	}
	
	private void runRR(int quantum) {
		dataList2.clear();
		dataList3.clear();
		
		RRScheduler rrScheduler = new RRScheduler(quantum);
		dataList2.addAll(rrScheduler.schedule(dataList));
		dataList3.addAll(rrScheduler.ganttList(dataList));
		
		double avgWT = rrScheduler.computeAverageWaitingTime(dataList2);
		double avgTAT = rrScheduler.computeAverageTurnaroundTime(dataList2);
		double cpuUtilization = rrScheduler.computeCpuUtilization(dataList2);
		
		avgWTResult.setText(df.format(avgWT));
		avgTATResult.setText(df.format(avgTAT));
		cpuUtilizationResult.setText(df.format(cpuUtilization) + "%");
	}

	private void runSJN() {
		dataList2.clear();
		dataList3.clear();
		
		SJNScheduler sjnScheduler = new SJNScheduler();
		dataList2.addAll(sjnScheduler.schedule(dataList));
		dataList3.addAll(sjnScheduler.schedule(dataList));
		
		dataList2.removeIf(p ->
			p.getProcess().getPid().equals("IDLE")
		);
		
		double avgWT = sjnScheduler.computeAverageWaitingTime(dataList2);
		double avgTAT = sjnScheduler.computeAverageTurnaroundTime(dataList2);
		double cpuUtilization = sjnScheduler.computeCpuUtilization(dataList2);

		avgWTResult.setText(df.format(avgWT));
		avgTATResult.setText(df.format(avgTAT));
		cpuUtilizationResult.setText(df.format(cpuUtilization) + "%");
	}
	
	//GANTT CHART
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

	public static void main(String[] args) {
        launch(args);
    }
}
