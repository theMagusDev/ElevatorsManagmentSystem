package yuriymagus.elevators_management_lab;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    private final Stage stage;

    public Navigation(Stage stage) {
        this.stage = stage;
    }

    public Navigation load(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);

            stage.centerOnScreen();
            stage.setTitle("Hello!");
            stage.setResizable(false);
            stage.setScene(scene);

        } catch (Exception e) {
            System.err.println("Exception while loading fxml, message: " + e.getMessage());
        }
        return this;
    }

    public void show() {
        stage.show();
    }
}

