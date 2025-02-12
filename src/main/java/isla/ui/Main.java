package isla.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Isla using FXML.
 */
public class Main extends Application {
    private static final int WINDOW_HEIGHT = 220;
    private static final int WINDOW_WIDTH = 220;

    private final Isla isla = new Isla();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(WINDOW_HEIGHT);
            stage.setMinWidth(WINDOW_WIDTH);
            stage.setMaxWidth(WINDOW_WIDTH);
            fxmlLoader.<MainWindow>getController().setIsla(isla); // inject the Isla instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
