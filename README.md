# Multithreaded Elevator Management System

## 1. Overview
The system simulates the operation of elevators within a building. It includes a graphical interface (JavaFX) and a backend that handles multiple threads and logic for managing elevator movement, passenger requests, and floor-specific operations.

---

## 2. Classes and Responsibilities

### 2.1 **JavaFX Classes**

#### **ElevatorsApplication**
- **Purpose**: Entry point of the application. Initializes navigation and displays the initial view.
- **Key Methods**:
    - `start(Stage stage)`: Loads the initial "hello-view.fxml".
    - `getNavigation()`: Provides access to the `Navigation` object.

#### **GreetingController**
- **Purpose**: Handles user interactions on the greeting screen.
- **Key Methods**:
    - `onHelloButtonClick()`: Navigates to the main view (`main-view.fxml`).

#### **MainController**
- **Purpose**: Manages the main application screen, including the simulation of elevators and passenger animations.
- **Attributes**:
    - `mainPane`: The root pane containing UI elements.
    - `firstElevator`, `secondElevator`: Graphical representations of elevators.
    - `requests`: A set of passenger icons.
    - `arbitrator`: Manages elevator decisions and logic.
- **Key Methods**:
    - `initialize()`: Sets initial positions of elevators and starts the simulation.
    - `addPassenger(Request request)`: Adds a graphical representation of a passenger.
    - `setPassengerVisible(Request passenger, boolean toVisible)`: Controls passenger visibility.
    - `movePassengerTo(Request passenger, double x, double y)`: Moves a passenger to specified coordinates.
    - `updateElevatorPosition(int elevatorIndex, int yCoordinate)`: Updates elevator position on the screen.
    - `startElevatorSimulation()`: Initializes the backend logic.

---

### 2.2 **Backend Classes**

#### **Arbitrator**
- **Purpose**: Central decision-maker for assigning requests to elevators.
- **Attributes**:
    - `floors`: List of all floors and their states.
    - `elevators`: List of managed elevators.
    - `waitingRequests`: Queue of unprocessed requests.
- **Key Methods**:
    - `chooseClosestElevator(Request request)`: Determines the optimal elevator for a request.
    - `addRequest(Request request)`: Assigns requests to elevators or adds them to the queue.
    - `notifyElevatorFreed()`: Signals when an elevator becomes available.
    - `processQueue()`: Processes queued requests.

#### **Elevator**
- **Purpose**: Represents an individual elevator.
- **Attributes**:
    - `currentFloor`, `direction`: Current state of the elevator.
    - `floorsToVisit`: Floors the elevator will visit.
    - `requestsEndPoints`: Map of passengers and their destinations.
- **Key Methods**:
    - `addFloorsToVisit(Integer floor)`: Adds a floor to the elevator’s queue.
    - `releasePassengers()`: Handles passenger drop-offs.
    - `pickupPassengers()`: Handles passenger pickups.
    - `moveElevator()`: Moves the elevator to its next position.

#### **RequestsGenerator**
- **Purpose**: Randomly generates passenger requests.
- **Key Methods**:
    - `generateRequests()`: Continuously generates requests with random start and end floors.

#### **Request**
- **Purpose**: Represents a passenger’s request.
- **Attributes**:
    - `startFloor`, `endFloor`: Passenger’s origin and destination.
    - `x`, `y`: Graphical coordinates for animation.

#### **Floor**
- **Purpose**: Represents a floor in the building.
- **Attributes**:
    - `waitingPassengers`: Queue of passengers waiting for the elevator.
- **Key Methods**:
    - `addWaitingPassenger(Request request)`: Adds a passenger to the queue.
    - `removeWaitingPassenger(Request request)`: Removes a passenger from the queue.

#### **BuildingProperties**
- **Purpose**: Stores static properties of the building.
- **Attributes**:
    - `NUMBER_OF_FLOORS`, `NUMBER_OF_ELEVATORS`: Configurable constants.
    - Coordinate mappings for floors and elevator shafts.
- **Key Methods**:
    - `getFloorYCoordinate(int floor)`: Returns the Y-coordinate for a floor.
    - `getElevatorShaftXCoordinate(int elevatorNumber)`: Returns the X-coordinate of an elevator shaft.

#### **Direction (Enum)**
- **Purpose**: Represents elevator direction.
- **Values**: `UP`, `DOWN`, `STANDING`.

---

## 3. System Workflow
1. **Initialization**:
    - `ElevatorsApplication` starts the JavaFX application and loads the greeting view.
    - Upon user interaction, `GreetingController` switches to the main view.
    - `MainController` initializes elevator positions and starts the simulation.

2. **Request Generation**:
    - `RequestsGenerator` continuously generates passenger requests.
    - Requests are processed by the `Arbitrator`, which assigns them to elevators or queues them.

3. **Elevator Operations**:
    - Elevators, managed by the `Elevator` class, move between floors to pick up and drop off passengers.
    - Elevator positions and passenger movements are updated in real-time on the UI via `MainController`.

4. **Dynamic Updates**:
    - Elevator and passenger states are updated through JavaFX methods (`Platform.runLater` ensures thread safety).

---

## 4. Key Features
- **Real-Time Simulation**: The graphical interface dynamically updates elevator and passenger movements.
- **Multithreaded Backend**: Elevators and request processing operate in parallel for realistic simulation.
- **Extensible Architecture**: Easy to add more elevators, floors, or features by modifying constants and extending classes.
