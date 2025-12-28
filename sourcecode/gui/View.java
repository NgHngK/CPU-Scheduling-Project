package gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import process.ProcessStats;
import process.Process;

public class View {
	public TableView<Process> tableView = new TableView<>(); //this is the processTable, naming is bad but it is legacy so what can i do about it :P
	public TableView<ProcessStats> tableView2 = new TableView<>(); //likewise, this is the resultTable, but the naming is like that to begin with
	
	public Button addBtn = new Button("Add process"); //this is the addProcessBtn
	public Button clearButton = new Button("Clear all processes"); //clearBtn
	public Button submitButton = new Button("Submit"); //submitBtn
	public Button exitButton = new Button("EXIT");
	public Button menuButton = new Button("Menu");
	
	// algorithm menu setup
	public MenuItem fcfs = new MenuItem("First Come First Serve");
	public MenuItem sjn = new MenuItem("Shortest Job Next");
	public MenuItem rr = new MenuItem("Round Robin");
	
	public MenuButton algoMenuButton = new MenuButton("Algorithms", null, fcfs, sjn, rr); //this is the algoMenu button
	
	//gantt chart
	public AnchorPane centerPane = new AnchorPane(); //this is ganttPane
	
	// explain algos buttons	
	public Button fcfsButton = new Button("Explain FCFS");
	public Button sjnButton = new Button("Explain SJN");
	public Button rrButton = new Button("Explain RR");
	
	// quantum field for robin hood
	public TextField quantum = new TextField(); //quantumField

	// cool stats 
    public Label avgWTResult = new Label(); //avgWTValue
    public Label avgTATResult = new Label(); //avgTATValue
    public Label cpuUtilizationResult = new Label(); //cpuUtilValue
    
    public Label resultAvgWTLabel = new Label();
    public Label resultAvgTATLabel = new Label();
    public Label resultCpuULabel = new Label();
	
	public Label avgWTLabel = new Label("AVG Waiting Time");
	public Label avgTATLabel= new Label("AVG Turnaround Time");
	public Label cpuULabel = new Label("CPU Utilization");
    
    // wallahi we're cooked    
    public ObservableList<Process> dataList = FXCollections.observableArrayList(); //this is processData, why are we using ObservableList?
    public ObservableList<ProcessStats> dataList2 = FXCollections.observableArrayList(); // contain processes that are not IDLE
    public ObservableList<ProcessStats> dataList3 = FXCollections.observableArrayList(); // for IDLE processes

    @SuppressWarnings("unchecked")
	private void buildProcessTable() {
    	tableView.setItems(dataList); //this table displays what is in processData (aka dataList)
    	
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
    	tableView2.setItems(dataList2); //displays not IDLE processes
    	
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
