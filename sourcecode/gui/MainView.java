package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import process.Process;
import process.ProcessStats;

public class MainView {

    public TableView<Process> processTable = new TableView<>();
    public TableView<ProcessStats> resultTable = new TableView<>();

    public Button addProcessBtn = new Button("Add process");
    public Button clearBtn = new Button("Clear all processes");
    public Button submitBtn = new Button("Submit");

    public MenuItem fcfsItem = new MenuItem("First Come First Serve");
    public MenuItem sjnItem  = new MenuItem("Shortest Job Next");
    public MenuItem rrItem   = new MenuItem("Round Robin");

    public MenuButton algoMenu = new MenuButton("Algorithms", null, fcfsItem, sjnItem, rrItem);

    public TextField quantumField = new TextField();

    public Label avgWTValue = new Label();
    public Label avgTATValue = new Label();
    public Label cpuUtilValue = new Label();

    public AnchorPane ganttPane = new AnchorPane();

    public ObservableList<Process> processData =
            FXCollections.observableArrayList();

    public ObservableList<ProcessStats> resultData =
            FXCollections.observableArrayList();
    

    private void buildProcessTable() {
        processTable.setItems(processData);

        TableColumn<Process, String> pid = new TableColumn<>("Process ID");
        TableColumn<Process, Number> at  = new TableColumn<>("Arrival");
        TableColumn<Process, Number> bt  = new TableColumn<>("Burst");

        processTable.getColumns().addAll(pid, at, bt);
    }

    private void buildResultTable() {
        resultTable.setItems(resultData);

        TableColumn<ProcessStats, String> pid = new TableColumn<>("Process ID");
        TableColumn<ProcessStats, Number> at  = new TableColumn<>("Arrival");
        TableColumn<ProcessStats, Number> bt  = new TableColumn<>("Burst");
        TableColumn<ProcessStats, Number> wt  = new TableColumn<>("Waiting");
        TableColumn<ProcessStats, Number> tat = new TableColumn<>("Turnaround");

        resultTable.getColumns().addAll(pid, at, bt, wt, tat);
    }

    private VBox statBox(String title, Label value) {
        Label label = new Label(title);
        VBox box = new VBox(5, label, value);
        box.setAlignment(Pos.CENTER);
        return box;
    }
    
    public void start(Stage stage) {
        buildProcessTable();
        buildResultTable();

        quantumField.setPromptText("Quantum Time");
        quantumField.setVisible(false);

        VBox right = new VBox(10, processTable, addProcessBtn, clearBtn);
        right.setPadding(new Insets(10));
        right.setAlignment(Pos.TOP_CENTER);
        
        VBox left = new VBox(10, algoMenu, quantumField, submitBtn);
        left.setPadding(new Insets(10));

        HBox stats = new HBox(
                statBox("AVG Waiting Time", avgWTValue),
                statBox("AVG Turnaround Time", avgTATValue),
                statBox("CPU Utilization", cpuUtilValue)
        );
        stats.setSpacing(50);
        stats.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(10, resultTable, stats);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(left);
        root.setRight(right);
        root.setCenter(ganttPane);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 1600, 900);
        stage.setScene(scene);
        stage.setTitle("CPU Scheduling");
        stage.setResizable(false);
        stage.show();
    }
}
