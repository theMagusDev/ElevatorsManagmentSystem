package yuriymagus.elevators_management_lab.backend;

import yuriymagus.elevators_management_lab.MainController;

import java.util.Random;

public class RequestsGenerator implements Runnable {
    Thread requestGeneratorThread;
    MainController mainController;
    Arbitrator arbitrator;

    public RequestsGenerator(MainController mainController) {
        this.mainController = mainController;
        this.arbitrator = new Arbitrator(mainController);
        requestGeneratorThread = new Thread(this);
        requestGeneratorThread.start();
    }

    public Arbitrator getArbitrator() {
        return arbitrator;
    }

    private void generateRequests() {
        Random random = new Random();
        Request generatedRequest;
        int from = -1, to = -2;
        while (true) {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                break;
            }
            do {
                from = random.nextInt(7);
                to = random.nextInt(7);
            } while (from == to);
            generatedRequest = new Request(from, to);
            arbitrator.addRequest(generatedRequest);
            mainController.addPassenger(generatedRequest);
        }
    }


    @Override
    public void run() {
        generateRequests();
    }
}
