package yuriymagus.elevators_management_lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import yuriymagus.elevators_management_lab.backend.Arbitrator;
import yuriymagus.elevators_management_lab.backend.BuildingProperties;
import yuriymagus.elevators_management_lab.backend.Request;
import yuriymagus.elevators_management_lab.backend.RequestsGenerator;

import java.util.HashSet;

public class MainController {
    @FXML
    public Pane mainPane;
    @FXML
    private ImageView firstElevator;

    @FXML
    private ImageView secondElevator;

    private HashSet<ImageView> requests;
    private Arbitrator arbitrator;

    @FXML
    public void initialize() {

        firstElevator.setLayoutX(69);
        firstElevator.setLayoutY(473);

        secondElevator.setLayoutX(147);
        secondElevator.setLayoutY(473); // 473 389 305 221 137 53 -31

        requests = new HashSet<>();
        startElevatorSimulation();
    }

    public void addPassenger(Request request) {
        Platform.runLater(() -> {
            ImageView passenger = new ImageView("passenger.png");
            passenger.setId("Request #" + request.getId());
            mainPane.getChildren().add(passenger);
            passenger.setVisible(false);
            passenger.setLayoutY(BuildingProperties.getFloorYCoordinateForPassenger(request.getStartFloor()));
            passenger.setLayoutX(
                    BuildingProperties.getElevatorShaftXCoordinate(1)
                            + 150
                            + arbitrator.getFloors().get(request.getStartFloor()).getWaitingPassengers().size() * 10
            );
            passenger.setVisible(true);
            request.setX(
                    BuildingProperties.getElevatorShaftXCoordinate(1)
                    + 150
                    + arbitrator.getFloors().get(request.getStartFloor()).getWaitingPassengers().size() * 10
            );
            request.setY(BuildingProperties.getFloorYCoordinateForPassenger(request.getStartFloor()));
            requests.add(passenger);
        });
    }

    public void setPassengerVisible(Request passenger, boolean toVisible) {
        Platform.runLater( () -> {
            ImageView passengerView = null;
            for (ImageView request : requests) {
                if (request.getId().equals("Request #" + passenger.getId())) {
                    passengerView = request;
                }
            }
            if (passengerView == null) {
                System.err.println("Error in MainController: passenger with id " + passenger.getId() + " not found.");
            } else {
                passengerView.setVisible(toVisible);
            }
        });
    }

    public void setPassengerOpacity(Request passenger, double opacity) {
        Platform.runLater( () -> {
            ImageView passengerView = null;
            for (ImageView request : requests) {
                if (request.getId().equals("Request #" + passenger.getId())) {
                    passengerView = request;
                }
            }
            if (passengerView == null) {
                System.err.println("Error in MainController: passenger with id " + passenger.getId() + " not found.");
            } else {
                passengerView.setOpacity(opacity);
            }
        });
    }

    public void movePassengerTo(Request passenger, double xCoordinate, double yCoordinate) {
        Platform.runLater( () -> {
            ImageView passengerView = null;
            for (ImageView request : requests) {
                if (request.getId().equals("Request #" + passenger.getId())) {
                    passengerView = request;
                }
            }
            if (passengerView == null) {
                System.err.println("Error in MainController: passenger with id " + passenger.getId() + " not found.");
            } else {
                passengerView.setLayoutX(xCoordinate);
                passengerView.setLayoutY(yCoordinate);
            }
        });
    }

    public void updateElevatorPosition(int elevatorIndex, int yCoordinate) {
        Platform.runLater(() -> {
            if (elevatorIndex == 0) {
                firstElevator.setLayoutY(yCoordinate);
            } else if (elevatorIndex == 1) {
                secondElevator.setLayoutY(yCoordinate);
            }
        });
    }

    public void startElevatorSimulation() {
        this.arbitrator = (new RequestsGenerator(this)).getArbitrator();
    }
}
