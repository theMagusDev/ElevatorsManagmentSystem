package yuriymagus.elevators_management_lab.backend;

import java.util.HashMap;
import java.util.Map;

public class BuildingProperties {
    public static final int NUMBER_OF_FLOORS;
    public static final int NUMBER_OF_ELEVATORS;

    private static final HashMap<Integer, Integer> floorsYCoordinates; // 473 389 305 221 137 53 -31
    private static final HashMap<Integer, Integer> elevatorsShaftsXCoordinates; // 147 69
    private static final HashMap<Integer, Integer> passengersShaftsXCoordinates; // 137 216
    private static final HashMap<Integer, Integer> floorsYCoordinatesForPassengers;

    static {
        NUMBER_OF_FLOORS = 7;
        NUMBER_OF_ELEVATORS = 2;
        floorsYCoordinates = new HashMap<>();
        floorsYCoordinates.put(0, 473);
        floorsYCoordinates.put(1, 389);
        floorsYCoordinates.put(2, 305);
        floorsYCoordinates.put(3, 221);
        floorsYCoordinates.put(4, 137);
        floorsYCoordinates.put(5, 53);
        floorsYCoordinates.put(6, -31);
        elevatorsShaftsXCoordinates = new HashMap<>();
        elevatorsShaftsXCoordinates.put(0, 69);
        elevatorsShaftsXCoordinates.put(1, 147);
        floorsYCoordinatesForPassengers = new HashMap<>();
        floorsYCoordinatesForPassengers.put(0, 553);
        floorsYCoordinatesForPassengers.put(1, 469);
        floorsYCoordinatesForPassengers.put(2, 385);
        floorsYCoordinatesForPassengers.put(3, 301);
        floorsYCoordinatesForPassengers.put(4, 217);
        floorsYCoordinatesForPassengers.put(5, 133);
        floorsYCoordinatesForPassengers.put(6, 49);
        passengersShaftsXCoordinates = new HashMap<>();
        passengersShaftsXCoordinates.put(0, 137);
        passengersShaftsXCoordinates.put(1, 216);
    }

    public static int getPassengersShaftsXCoordinates(int shaftNumber) {
        return passengersShaftsXCoordinates.get(shaftNumber);
    }

    public static int getExactFloorFromCoordinates(int yCoordinate) {
        for (Map.Entry<Integer, Integer> entry : floorsYCoordinates.entrySet()) {
            if (entry.getValue() == yCoordinate) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static int getFloorYCoordinate(int floor) {
        return floorsYCoordinates.getOrDefault(floor, -1);
    }

    public static int getFloorYCoordinateForPassenger(int floor) {
        return floorsYCoordinatesForPassengers.getOrDefault(floor, -1);
    }

    public static int getElevatorShaftXCoordinate(int elevatorNumber) {
        return elevatorsShaftsXCoordinates.getOrDefault(elevatorNumber, -1);
    }
}
