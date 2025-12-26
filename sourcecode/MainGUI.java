import gui.Controller;
import gui.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) {
        View view = new View();
        new Controller(view);
        view.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
