package gui;

import algorithm.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import process.Process;
import process.ProcessStats;

import java.text.DecimalFormat;

public class MainController {

    private final MainView view;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public MainController(MainView view) {
        this.view = view;
        bindActions();
    }

    private void bindActions() {

        view.fcfsItem.setOnAction(e -> {
            view.algoMenu.setText("First Come First Serve");
            view.quantumField.setVisible(false);
        });

        view.sjnItem.setOnAction(e -> {
            view.algoMenu.setText("Shortest Job Next");
            view.quantumField.setVisible(false);
        });

        view.rrItem.setOnAction(e -> {
            view.algoMenu.setText("Round Robin");
            view.quantumField.setVisible(true);
        });

        view.clearBtn.setOnAction(e -> {
            view.processData.clear();
            view.resultData.clear();
            view.avgWTValue.setText("");
            view.avgTATValue.setText("");
            view.cpuUtilValue.setText("");
            view.ganttPane.getChildren().clear();
        });

        view.submitBtn.setOnAction(e -> runSelectedAlgorithm());
    }

    private void runSelectedAlgorithm() {
        if (view.processData.isEmpty()) {
            error("No processes to schedule");
            return;
        }

        String algo = view.algoMenu.getText();

        switch (algo) {
            case "First Come First Serve" -> runFCFS();
            case "Shortest Job Next" -> runSJN();
            case "Round Robin" -> runRR();
            default -> error("Choose a scheduling algorithm");
        }
    }

    private void runFCFS() {
        FCFSScheduler scheduler = new FCFSScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.processData));
    }

    private void runSJN() {
        SJNScheduler scheduler = new SJNScheduler();
        executeScheduler(scheduler, scheduler.schedule(view.processData));
    }

    private void runRR() {
        if (view.quantumField.getText().isEmpty()) {
            error("Quantum time required");
            return;
        }

        int q = Integer.parseInt(view.quantumField.getText());
        RRScheduler scheduler = new RRScheduler(q);
        executeScheduler(scheduler, scheduler.schedule(view.processData));
    }

    private void executeScheduler(CPUScheduler scheduler, Iterable<ProcessStats> stats) {
        view.resultData.clear();
        stats.forEach(ps -> {
            if (!ps.getProcess().getPid().equals("IDLE")) {
                view.resultData.add(ps);
            }
        });

        double avgWT = scheduler.computeAverageWaitingTime(view.resultData);
        double avgTAT = scheduler.computeAverageTurnaroundTime(view.resultData);
        double cpuU  = scheduler.computeCpuUtilization(view.resultData);

        view.avgWTValue.setText(df.format(avgWT));
        view.avgTATValue.setText(df.format(avgTAT));
        view.cpuUtilValue.setText(df.format(cpuU) + "%");
    }

    private void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }
}
