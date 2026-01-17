package gui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import process.ProcessStats;
import process.Process;

public class View {
	private TableView<Process> tableView = new TableView<>(); //this is the processTable, naming is bad but it is legacy so what can i do about it :P
	private TableView<ProcessStats> tableView2 = new TableView<>(); //likewise, this is the resultTable, but the naming is like that to begin with
	
	protected Button addBtn = new Button("Add process"); //this is the addProcessBtn
	protected Button clearButton = new Button("Clear all processes"); //clearBtn
	protected Button submitButton = new Button("Submit"); //submitBtn
	protected Button exitButton = new Button("EXIT");
	protected Button menuButton = new Button("Menu");
	
	// algorithm menu setup
	protected MenuItem fcfs = new MenuItem("First Come First Serve");
	protected MenuItem sjn = new MenuItem("Shortest Job Next");
	protected MenuItem rr = new MenuItem("Round Robin");
	
	protected MenuButton algoMenuButton = new MenuButton("Algorithms", null, fcfs, sjn, rr); //this is the algoMenu button
	
	//gantt chart
	private AnchorPane centerPane = new AnchorPane(); //this is ganttPane
	
	// explain algos buttons	
	private Button fcfsButton = new Button("Explain FCFS");
	private Button sjnButton = new Button("Explain SJN");
	private Button rrButton = new Button("Explain RR");
	
	// quantum field for robin hood
	protected TextField quantum = new TextField(); //quantumField

	// cool stats 
	private Label avgWTResult = new Label(); //avgWTValue
	private Label avgTATResult = new Label(); //avgTATValue
	private Label cpuUtilizationResult = new Label(); //cpuUtilValue
    
	private Label resultAvgWTLabel = new Label();
	private Label resultAvgTATLabel = new Label();
	private Label resultCpuULabel = new Label();
	
	private Label avgWTLabel = new Label("AVG Waiting Time");
	private Label avgTATLabel= new Label("AVG Turnaround Time");
	private Label cpuULabel = new Label("CPU Utilization");

    private Random random = new Random();
    
    protected void bindProcessTable(ObservableList<Process> dataList) {
        tableView.setItems(dataList);
    }

    protected void bindResultTable(ObservableList<ProcessStats> dataList2) {
        tableView2.setItems(dataList2);
    }
    
    @SuppressWarnings("unchecked")
	private void buildProcessTable() {
//    	tableView.setItems(dataList); //this table displays what is in processData (aka dataList)
    	
    	//set up table
    	TableColumn<Process, String> pidColumn = new TableColumn<>("Process ID"); //pid
        TableColumn<Process, Number> arrColumn  = new TableColumn<>("Arrival"); //at
        TableColumn<Process, Number> burColumn  = new TableColumn<>("Burst"); //bt

        //this apparently makes the data shows up, no clue what the factory thing is
    	pidColumn.setCellValueFactory(c ->
    		new SimpleStringProperty(c.getValue().getPid())
    	);

    	arrColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getArrivalTime())
    	);

    	burColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getBurstTime())
    	);
        
        tableView.getColumns().addAll(pidColumn, arrColumn, burColumn);
        
      //set size
    	int sizeOfCol1 = 100;
    	tableView.setPrefSize(sizeOfCol1 * 3, sizeOfCol1 * 3);
    	
        pidColumn.setPrefWidth(sizeOfCol1);
    	arrColumn.setPrefWidth(sizeOfCol1);
    	burColumn.setPrefWidth(sizeOfCol1);
    }
    
    @SuppressWarnings("unchecked")
	private void buildResultTable() {
//    	tableView2.setItems(dataList2); //displays not IDLE processes
    	
    	//set up table
    	TableColumn<ProcessStats, String> pidColumn2 = new TableColumn<>("Process ID"); //pid
        TableColumn<ProcessStats, Number> arrColumn2  = new TableColumn<>("Arrival"); //at
        TableColumn<ProcessStats, Number> burColumn2  = new TableColumn<>("Burst"); //bt
        TableColumn<ProcessStats, Number> wColumn  = new TableColumn<>("Waiting"); //wt
        TableColumn<ProcessStats, Number> tatColumn = new TableColumn<>("Turnaround"); //tat

        pidColumn2.setCellValueFactory(c ->
    		new SimpleStringProperty(c.getValue().getProcess().getPid())
    		);

		arrColumn2.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getProcess().getArrivalTime())
    		);

		burColumn2.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getProcess().getBurstTime())
    		);

		wColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getWaitingTime())
    		);

		tatColumn.setCellValueFactory(c ->
    		new SimpleIntegerProperty(c.getValue().getTurnaroundTime())
    		);
        
        tableView2.getColumns().addAll(pidColumn2, arrColumn2, burColumn2, wColumn, tatColumn);
        
    	//set size
    	int sizeOfCol2 = 100;
    	tableView2.setPrefSize(sizeOfCol2 * 5 + 20, sizeOfCol2 * 3);
    	
        pidColumn2.setPrefWidth(sizeOfCol2);
    	arrColumn2.setPrefWidth(sizeOfCol2);
    	burColumn2.setPrefWidth(sizeOfCol2);
    	wColumn.setPrefWidth(sizeOfCol2);
    	tatColumn.setPrefWidth(sizeOfCol2 + 20);
    }
    
  //dude optimize this wtf is this shit lmao
	protected void showMenuDialog() {
  		Dialog<String> dialog = new Dialog<String>();
  		dialog.setTitle("MENU");
  		
  		int widthBtn = 210;
  		int heigtBtn = 60;
  		
//  		Button fcfsButton = new Button("Explain FCFS");
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
//  		Button sjnButton = new Button("Explain SJN");
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
//  		Button rrButton = new Button("Explain RR");
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

  	protected void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }
  	
  	protected Optional<Process> showAddProcessDialog(int nextPid) {
  	    Dialog<Process> dialog = new Dialog<>();
  	    dialog.setTitle("Add new process");

  	    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
  	    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

  	    TextField arrField = new TextField();
  	    arrField.setPromptText("Arrival Time");
  	    arrField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));

  	    TextField burField = new TextField();
  	    burField.setPromptText("Burst Time");
  	    burField.setTextFormatter(new TextFormatter<>(c ->c.getControlNewText().matches("\\d*") ? c : null));

  	    VBox box = new VBox(10, new Label("Arrival Time:"), arrField, new Label("Burst Time:"), burField);

  	    dialog.getDialogPane().setContent(box);
  	    dialog.getDialogPane().setPrefSize(210, 200);

  	    dialog.setResultConverter(btn -> {
  	        if (btn == addButtonType && !arrField.getText().isEmpty() && !burField.getText().isEmpty()) {
  	            return new Process("P" + nextPid, Integer.parseInt(arrField.getText()), Integer.parseInt(burField.getText()));
  	        }
  	        return null;
  	    });

  	    return dialog.showAndWait();
  	}

  	protected void resetUI() {
		resultAvgWTLabel.setVisible(false);
		resultAvgTATLabel.setVisible(false);
		resultCpuULabel.setVisible(false);
  		centerPane.getChildren().clear(); //clear ganttPane
  	}
  	
  	protected void showStats(double avgWT, double avgTAT, double cpuU, DecimalFormat df) {
  	    avgWTResult.setText(df.format(avgWT));
  	    avgTATResult.setText(df.format(avgTAT));
  	    cpuUtilizationResult.setText(df.format(cpuU) + "%");

  	    resultAvgWTLabel.setText(avgWTResult.getText());
  	    resultAvgTATLabel.setText(avgTATResult.getText());
  	    resultCpuULabel.setText(cpuUtilizationResult.getText());

  	    resultAvgWTLabel.setVisible(true);
  	    resultAvgTATLabel.setVisible(true);
  	    resultCpuULabel.setVisible(true);
  	}
  	
  	protected boolean confirmExit() {
  	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
  	    alert.setTitle("Confirm exit!");
  	    alert.setHeaderText("Are you sure you want to exit?");
  	    
  	    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

  	    Optional<ButtonType> result = alert.showAndWait();
  	    return result.isPresent() && result.get() == ButtonType.YES;
  	}
  	
  	@SuppressWarnings("static-access")
  	protected void ganttChart(ObservableList<ProcessStats> dataList3) {
		
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
    		GanttChartBlock psRectangle = new GanttChartBlock(width, 50, text, map.get(text)); // process pane with text in center
    		
    		
    		if (psRectangle.getText() == "IDLE") {
    			psRectangle = new GanttChartBlock(width, 50, text, Color.rgb(255, 255, 255));   // White for IDLE pc
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
  	
    public void start(Stage primaryStage) {
    	buildProcessTable();
        buildResultTable();
        
        //quantum button
        quantum.setPromptText("Quantum Time");
        quantum.setVisible(false);
        
        //RIGHT Vbox
        VBox vbox1 = new VBox(10);
        
        vbox1.getChildren().addAll(tableView, addBtn, clearButton);
    	vbox1.setAlignment(Pos.TOP_CENTER);
    	
    	AnchorPane pcPane = new AnchorPane();
    	AnchorPane.setTopAnchor(vbox1, 10.0);
    	AnchorPane.setRightAnchor(vbox1, 10.0);

    	pcPane.getChildren().add(vbox1);
    	
    	//LEFT Vbox
    	VBox vbox2 = new VBox(10);
    	vbox2.getChildren().addAll(algoMenuButton, quantum, submitButton);
    	vbox2.setAlignment(Pos.TOP_LEFT);
    	
    	AnchorPane aPane = new AnchorPane();
    	
    	AnchorPane.setTopAnchor(vbox2, 10.0);
    	AnchorPane.setLeftAnchor(vbox2, 10.0);
    	
    	aPane.getChildren().add(vbox2);
    	
    	//HBOX part (Process result list)
    	VBox avgWTBox = new VBox(10);
    	VBox avgTATBox = new VBox(10);
    	VBox cpuUtilizationBox = new VBox(10); 
    	
    	avgWTBox.getChildren().addAll(avgWTLabel, resultAvgWTLabel);
    	avgTATBox.getChildren().addAll(avgTATLabel, resultAvgTATLabel);
    	cpuUtilizationBox.getChildren().addAll(cpuULabel, resultCpuULabel);
    	
    	avgWTBox.setAlignment(Pos.CENTER);
    	avgTATBox.setAlignment(Pos.CENTER);
    	cpuUtilizationBox.setAlignment(Pos.CENTER);
    	
    	resultAvgWTLabel.setVisible(false);
    	resultAvgTATLabel.setVisible(false);
    	resultCpuULabel.setVisible(false);
    	
    	HBox hbox1 = new HBox(170);
    	hbox1.getChildren().addAll(tableView2, avgWTBox, avgTATBox, cpuUtilizationBox);
    	hbox1.setAlignment(Pos.CENTER);
    	
    	AnchorPane pcrPane = new AnchorPane();
    	AnchorPane.setLeftAnchor(hbox1, 10.0);
    	AnchorPane.setBottomAnchor(hbox1, 10.0);
    	
    	pcrPane.getChildren().add(hbox1);
    	
    	//help menu    	
    	AnchorPane.setTopAnchor(menuButton, 110.0);
    	AnchorPane.setLeftAnchor(menuButton, 10.0);
    	aPane.getChildren().add(menuButton);
    	
    	//exit button
    	AnchorPane.setTopAnchor(exitButton, 140.0);
    	AnchorPane.setLeftAnchor(exitButton, 10.0);
    	aPane.getChildren().add(exitButton);
    	
    	//root??
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
    }
}
