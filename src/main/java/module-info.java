module yuriymagus.elevators_management_lab {
    requires javafx.controls;
    requires javafx.fxml;

    opens yuriymagus.elevators_management_lab to javafx.fxml;
    opens yuriymagus.elevators_management_lab.backend to javafx.fxml;
    exports yuriymagus.elevators_management_lab.backend;
    exports yuriymagus.elevators_management_lab;
}