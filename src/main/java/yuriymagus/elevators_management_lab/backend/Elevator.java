package yuriymagus.elevators_management_lab.backend;

import yuriymagus.elevators_management_lab.MainController;

import java.util.*;

public class Elevator implements Runnable {
    private final Arbitrator arbitrator;
    private final MainController controller;
    private final Thread elevatorThread;
    private final int elevatorIndex;
    private final int passengersCapacity;
    private int currentFloor;
    private final int xCoordinate;
    private int yCoordinate;
    private Direction direction;
    private final SortedSet<Integer> floorsToVisit;
    private final HashMap<Integer, HashSet<Request>> requestsEndPoints;
    //                        ^           ^
    //                    to where  |   people

    public Elevator(int passengersCapacity, Arbitrator arbitrator, MainController controller, int elevatorIndex) {
        this.elevatorIndex = elevatorIndex;
        this.controller = controller;
        this.yCoordinate = BuildingProperties.getFloorYCoordinate(0);
        this.passengersCapacity = passengersCapacity;
        this.direction = Direction.STANDING;
        this.arbitrator = arbitrator;
        this.floorsToVisit = Collections.synchronizedSortedSet(new TreeSet<Integer>());
        this.requestsEndPoints = new HashMap<>();
        for (int endPoint = 0; endPoint < BuildingProperties.NUMBER_OF_FLOORS; endPoint++) {
            requestsEndPoints.put(endPoint, new HashSet<>());
        }
        this.xCoordinate = BuildingProperties.getElevatorShaftXCoordinate(elevatorIndex);
        this.elevatorThread = new Thread(this);
        this.elevatorThread.start();
    }

    public int getPassengersCapacity() {
        return passengersCapacity;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public SortedSet<Integer> getFloorsToVisit() {
        return floorsToVisit;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getPassengersInsideNumber() {
        return requestsEndPoints
                .values()
                .stream()
                .mapToInt(HashSet::size)
                .sum();
    }

    public synchronized void addFloorsToVisit(Integer floor) {
        System.out.println("Elevator " + this + " got floor " + floor + " to visit from arbitrator.");
        floorsToVisit.add(floor);
        if (direction == Direction.STANDING) {
            if (floor > currentFloor) {
                direction = Direction.UP;
                System.out.println("Now elevator " + this + " has direction UP");
            } else {
                direction = Direction.DOWN;
                System.out.println("Now elevator " + this + " has direction DOWN");
            }
        }
        this.notify();
    }

    private void releasePassengers() throws InterruptedException {
        floorsToVisit.remove(currentFloor);
        if (floorsToVisit.isEmpty()) {
            direction = Direction.STANDING;
        }
        boolean releasedPassenger = false;
        for (Map.Entry<Integer, HashSet<Request>> requestsOnFloor : requestsEndPoints.entrySet()) {
            if (requestsOnFloor.getKey() == currentFloor && !requestsOnFloor.getValue().isEmpty()) {
                for (Request request : requestsOnFloor.getValue()) {
                    releasedPassenger = true;

                    controller.setPassengerVisible(request, false);
                    request.setX(BuildingProperties.getPassengersShaftsXCoordinates(elevatorIndex));
                    request.setY(BuildingProperties.getFloorYCoordinateForPassenger(currentFloor));
                    controller.movePassengerTo(request, request.getX(), request.getY());
                    controller.setPassengerVisible(request, true);
                    for (int i = 0; i < 25; i++) {
                        Thread.sleep(25);
                        controller.movePassengerTo(request, request.getX(), request.getY());
                        controller.setPassengerOpacity(request, (double) (25 - i) / 25);
                        request.setX(request.getX() + 10);
                    }
                    controller.setPassengerVisible(request, false);
                }
            }
        }
        requestsEndPoints.get(currentFloor).clear();
        if (releasedPassenger) {
            System.out.println("Elevator " + this + " released passengers on floor " + currentFloor);
            Thread.sleep(1000);
        }
        arbitrator.notifyElevatorFreed();
    }

    private void pickupPassengers() throws InterruptedException {
        if (currentFloor == -1 || getPassengersInsideNumber() + 1 > passengersCapacity) {
            return;
        }
        HashSet<Request> pickedPassengers = new HashSet<>();
        synchronized (arbitrator.getFloors().get(currentFloor).getWaitingPassengers()) {
            Request acceptingPassenger;
            for (Request request : arbitrator.getFloors().get(currentFloor).getWaitingPassengers()) {
                acceptingPassenger = request;
                if (acceptingPassenger != null && getPassengersInsideNumber() + 1 <= passengersCapacity) {
                    if (
                            (direction == Direction.STANDING)
                                    || (direction == Direction.DOWN && currentFloor > acceptingPassenger.getEndFloor())
                                    || (direction == Direction.UP && currentFloor < acceptingPassenger.getEndFloor())
                    ) {
                        synchronized (floorsToVisit) {
                            addFloorsToVisit(acceptingPassenger.getEndFloor());
                        }
                        pickedPassengers.add(acceptingPassenger);
                        requestsEndPoints.get(acceptingPassenger.getEndFloor()).add(acceptingPassenger);
                        controller.setPassengerVisible(acceptingPassenger, true);
                        while (acceptingPassenger.getX() > BuildingProperties.getPassengersShaftsXCoordinates(elevatorIndex)) {
                            controller.movePassengerTo(acceptingPassenger, acceptingPassenger.getX() - 10, acceptingPassenger.getY());
                            acceptingPassenger.setX(acceptingPassenger.getX() - 10);
                            Thread.sleep(15);
                        }
                        controller.setPassengerVisible(acceptingPassenger, false);
                        System.out.println("Picked up passenger from floor " + currentFloor + ", elevator " + this);
                    }
                } else {
                    break;
                }
            }
            System.out.println("Elevator " + this + " has picked up " + pickedPassengers.size() + " passengers.");
            for (Request pickedPassenger : pickedPassengers) {
                if (!(arbitrator.getFloors().get(currentFloor).removeWaitingPassenger(pickedPassenger))) {
                    System.err.println("Error while picking up passengers in elevator " + this + ": failed to remove picked passenger from waiting queue.");
                }
            }
        }
        if (!pickedPassengers.isEmpty()) {
            Thread.sleep(1000);
        }
    }

    private void moveElevator() throws InterruptedException {
        if (yCoordinate > 500 || yCoordinate < -500) {
            this.elevatorThread.interrupt();
            throw new InterruptedException("Elevator has flown out of the building, interrupting its thread.");
        }
//        System.out.println("Elevator " + this + " does his work on coordinates " + xCoordinate + ";" + yCoordinate);
//        System.out.println("Elevator " + this + " floors to visit are " + floorsToVisit + ", having passengers " + getPassengersInsideNumber() + " who needs to " + requestsEndPoints);

        int exactFloor = BuildingProperties.getExactFloorFromCoordinates(yCoordinate);
        if (exactFloor != -1) { // exactFloor can be -1 if elevator is behind the floors
            System.out.println("Elevator " + this + " is now on floor " + exactFloor);
            currentFloor = exactFloor;
            releasePassengers();
            if (direction == Direction.UP && getPassengersInsideNumber() == 0 && (floorsToVisit.isEmpty() || floorsToVisit.last() <= currentFloor)
            || direction == Direction.DOWN && getPassengersInsideNumber() == 0 && (floorsToVisit.isEmpty() || floorsToVisit.first() >= currentFloor)
            ) {
                direction = Direction.STANDING;
            }
            pickupPassengers();
        }

        switch (direction) {
            case UP -> yCoordinate -= 7;
            case DOWN -> yCoordinate += 7;
        }
        controller.updateElevatorPosition(elevatorIndex, yCoordinate);
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    while (floorsToVisit.isEmpty()) {
                        System.out.println("Elevator " + this + " waits for new orders");
                        this.wait();
                    }
                }
                moveElevator();
                Thread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "elevatorIndex=" + elevatorIndex +
                '}';
    }
}
