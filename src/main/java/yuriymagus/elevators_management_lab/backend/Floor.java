package yuriymagus.elevators_management_lab.backend;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Floor {
    private final ConcurrentLinkedQueue<Request> waitingPassengers;

    public Floor() {
        this.waitingPassengers = new ConcurrentLinkedQueue<>();
    }

    public void addWaitingPassenger(Request request) {
        this.waitingPassengers.add(request);
    }

    public ConcurrentLinkedQueue<Request> getWaitingPassengers() {
        return waitingPassengers;
    }

    public boolean removeWaitingPassenger(Request waitingPassenger) {
        return waitingPassengers.remove(waitingPassenger);
    }

    @Override
    public String toString() {
        return "Floor{" +
                "waitingPassengers=" + waitingPassengers +
                '}';
    }
}
