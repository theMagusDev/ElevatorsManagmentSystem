package yuriymagus.elevators_management_lab;

import javafx.fxml.FXML;

import java.io.IOException;

public class GreetingController {
    @FXML
    protected void onHelloButtonClick() throws IOException {
        ElevatorsApplication.getNavigation().load("main-view.fxml").show();
    }
}