package yuriymagus.elevators_management_lab;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ElevatorsApplication extends Application {
    private static Navigation navigation;

    public static Navigation getNavigation() {
        return navigation;
    }

    @Override
    public void start(Stage stage) throws IOException {
        navigation = new Navigation(stage);
        navigation.load("hello-view.fxml").show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}