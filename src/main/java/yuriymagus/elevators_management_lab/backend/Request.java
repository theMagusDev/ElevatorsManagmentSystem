package yuriymagus.elevators_management_lab.backend;

public class Request {
    private static int counter = 0;

    private int y;
    private int x;
    private final int startFloor;
    private final int endFloor;
    private final int id;

    public Request(int startFloor, int endFloor) {
        this.startFloor = startFloor;
        this.endFloor = endFloor;
        this.id = counter;
        counter++;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    @Override
    public String toString() {
        return "Request{" +
                "startFloor=" + startFloor +
                ", endFloor=" + endFloor +
                '}';
    }
}
