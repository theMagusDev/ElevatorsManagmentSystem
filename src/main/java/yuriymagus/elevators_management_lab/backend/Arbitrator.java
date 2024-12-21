package yuriymagus.elevators_management_lab.backend;

import yuriymagus.elevators_management_lab.MainController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Arbitrator implements Runnable {
    private final List<Floor> floors;
    private final ArrayList<Elevator> elevators;
    private final ConcurrentLinkedQueue<Request> waitingRequests;
    private final Thread waitingQueueThread;
    private final Thread arbitratorThread;

    public Arbitrator(MainController controller) {
        waitingRequests = new ConcurrentLinkedQueue<>();

        floors = Collections.synchronizedList(new ArrayList<>(BuildingProperties.NUMBER_OF_FLOORS));
        for (int i = 0; i < BuildingProperties.NUMBER_OF_FLOORS + 1; i++) {
            floors.add(new Floor());
        }

        elevators = new ArrayList<>(BuildingProperties.NUMBER_OF_ELEVATORS);
        for (int i = 0; i < BuildingProperties.NUMBER_OF_ELEVATORS; i++) {
            elevators.add(new Elevator(15, this, controller, i));
        }

        waitingQueueThread = new Thread(this::processQueue);
        waitingQueueThread.setDaemon(true);
        waitingQueueThread.start();

        arbitratorThread = new Thread(this);
        arbitratorThread.setPriority(Thread.MAX_PRIORITY);
        arbitratorThread.start();
    }

    public List<Floor> getFloors() {
        return floors;
    }

    private synchronized void processQueue() {
        while (true) {
            while (waitingRequests.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            Request request = waitingRequests.peek();
            boolean requestProcessed = false;

            for (Elevator elevator : elevators) {
                if (elevator.getPassengersInsideNumber() + 1 <= elevator.getPassengersCapacity()) {
                    requestProcessed = addRequest(request);
                    break;
                }
            }

            if (requestProcessed) {
                waitingRequests.poll();
            }
        }
    }

    private Elevator chooseClosestElevator(Request request) {
        Elevator chosenElevator = null;
        int minDistance = Integer.MAX_VALUE;
        boolean enoughCapacity;
        int distanceToRequest;
        for (Elevator elevator : elevators) {
            enoughCapacity = elevator.getPassengersInsideNumber() + 1 <= elevator.getPassengersCapacity();
            if (!enoughCapacity) {
                continue;
            }
            switch (elevator.getDirection()) {
                case STANDING -> {
                    if (request.getStartFloor() > elevator.getCurrentFloor()) {
                        distanceToRequest = request.getStartFloor() - elevator.getCurrentFloor();
                    } else {
                        distanceToRequest = elevator.getCurrentFloor() - request.getStartFloor();
                    }
                }
                case UP -> {
                    if (request.getStartFloor() > elevator.getCurrentFloor()) {
                        distanceToRequest = request.getStartFloor() - elevator.getCurrentFloor();
                    } else {
                        distanceToRequest = (elevator.getFloorsToVisit().last() - elevator.getCurrentFloor()) + (elevator.getFloorsToVisit().last() - request.getStartFloor());
                    }
                }
                case DOWN -> {
                    if (request.getStartFloor() < elevator.getCurrentFloor()) {
                        distanceToRequest = elevator.getCurrentFloor() - request.getStartFloor();
                    } else {
                        distanceToRequest = (elevator.getCurrentFloor() - elevator.getFloorsToVisit().first()) + (request.getStartFloor() - elevator.getFloorsToVisit().first());
                    }
                }
                default -> {
                    System.err.println("Unexpected elevator direction, failed to calculate distance to request. Integer.MAX_VALUE was set.");
                    distanceToRequest = Integer.MAX_VALUE;
                }
            }

            if (distanceToRequest < minDistance) {
                chosenElevator = elevator;
                minDistance = distanceToRequest;
            }
        }
        return chosenElevator;
    }

    public boolean addRequest(Request request) {
        System.out.println("Arbitrator got request " + request);
        Elevator chosenElevator = chooseClosestElevator(request);

        if (chosenElevator != null) {
            chosenElevator.addFloorsToVisit(request.getStartFloor());
            floors.get(request.getStartFloor()).addWaitingPassenger(request);
            return true;
        } else {
            waitingRequests.add(request);
            return false;
        }
    }

    public void notifyElevatorFreed() {
        synchronized (this) {
            notify(); // Notify arbitrator that some elevator is free (after it was completely busy)
        }
    }

    @Override
    public void run() {
        while (true) {
            if (arbitratorThread.isInterrupted()) {
                break;
            }
        }
    }
}
