package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainAppTest extends Application {

    @Override
    public void start(Stage stage) {
        MainViewReal view = new MainViewReal();
        new MainControllerReal(view);
        view.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
