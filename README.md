# Многопоточная система управления лифтами

## 1. Обзор
Система имитирует работу лифтов в здании. Она включает в себя графический интерфейс (JavaFX) и бэкэнд, который обрабатывает множество потоков и логику для управления движением лифта, запросами пассажиров и операциями на конкретном этаже.

---

## 2. Ключевые особенности
- **Симуляция в реальном времени**: Графический интерфейс динамически обновляет движение лифта и пассажиров.
- **Многопоточный бэкэнд**: Лифты и обработка запросов работают параллельно для реалистичного моделирования.
- **Расширяемая архитектура**: Можно легко добавлять новые лифты, этажи или функции, изменяя константы и расширяя классы.

---

## 3. Логика обработки запросов
+ Когда арбитр получает запрос, он выбирает ближайший лифт (с учетом направления движения лифта).
+ Лифты могут собирать пассажиров по пути следования.
+ Лифт может не принять пассажира, если ему нужно ехать в противоположном направлении. Он примет его позже, или другой лифт заберет этого пассажира.

---

## 4. Классы и их функционал

### 4.1 **Классы JavaFX**

#### **ElevatorsApplication**
- **Назначение**: Точка входа в приложение. Инициализирует навигацию и отображает начальный вид.
- **Ключевые методы**:
  - `start(Stage stage)`: Загружает начальный файл «hello-view.fxml».
  - `getNavigation()`: Предоставляет доступ к объекту `Navigation`.

#### **GreetingController**.
- **Назначение**: Обрабатывает взаимодействие пользователя с экраном приветствия.
- **Ключевые методы**:
  - `onHelloButtonClick()`: Переход к главному представлению (`main-view.fxml`).

#### **MainController**.
- **Назначение**: Управляет главным экраном приложения, включая симуляцию лифтов и анимацию пассажиров.
- **Атрибуты**:
  - `mainPane`: Корневая панель, содержащая элементы пользовательского интерфейса.
  - `Первый лифт`, `Второй лифт`: Графические представления лифтов.
  - `requests`: Набор иконок пассажиров.
  - `arbitrator`: Управляет решениями и логикой работы лифта.
- **Ключевые методы**:
  - `initialize()`: Устанавливает начальные позиции лифтов и запускает симуляцию.
  - `addPassenger(Request request)`: Добавляет графическое представление пассажира.
  - `setPassengerVisible(Request passenger, boolean toVisible)`: Управляет видимостью пассажира.
  - `movePassengerTo(Request passenger, double x, double y)`: Перемещает пассажира в указанные координаты.
  - `updateElevatorPosition(int elevatorIndex, int yCoordinate)`: Обновляет положение лифта на экране.
    - `startElevatorSimulation()`: Инициализирует логику бэкэнда.

## 4. Classes and Responsibilities

### 4.1 **JavaFX Classes**

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

### 4.2 **Бэкэнд-классы**.

#### **Арбитр**
- **Назначение**: Центральное лицо, принимающее решения о распределении заявок на лифты.
- **Атрибуты**:
  - `floors`: Список всех этажей и их состояния.
  - `elevators`: Список управляемых лифтов.
  - `waitingRequests`: Очередь необработанных запросов.
- **Ключевые методы**:
  - `chooseClosestElevator(Request request)`: Определяет оптимальный лифт для запроса.
  - `addRequest(Request request)`: Назначает заявки лифтам или добавляет их в очередь.
  - `notifyElevatorFreed()`: Сигнализирует, когда лифт становится свободным.
  - `processQueue()`: Обрабатывает поставленные в очередь запросы.

#### **Лифт**.
- **Назначение**: Представляет отдельный лифт.
- **Атрибуты**:
  - `currentFloor`, `direction`: Текущее состояние лифта.
  - `floorsToVisit`: Этажи, которые посетит лифт.
  - `requestsEndPoints`: Карта пассажиров и мест их назначения.
- **Ключевые методы**:
  - `addFloorsToVisit(Integer floor)`: Добавляет этаж в очередь лифта.
  - `releasePassengers()`: Обрабатывает высадку пассажиров.
  - `pickupPassengers()`: Занимается подбором пассажиров.
  - `moveElevator()`: Перемещает лифт в следующую позицию.

#### **RequestsGenerator**.
- **Назначение**: Случайным образом генерирует запросы пассажиров.
- **Ключевые методы**:
  - `generateRequests()`: Непрерывно генерирует запросы со случайными начальным и конечным этажами.

#### **Request**
- **Назначение**: Представляет запрос пассажира.
- **Атрибуты**:
  - `startFloor`, `endFloor`: Место отправления и назначения пассажира.
  - `x`, `y`: Графические координаты для анимации.

#### **Пол**
- **Назначение**: Представляет этаж в здании.
- **Атрибуты**:
  - `waitingPassengers`: Очередь пассажиров, ожидающих лифт.
- **Ключевые методы**:
  - `addWaitingPassenger(Request request)`: Добавляет пассажира в очередь.
  - `removeWaitingPassenger(Request request)`: Удаляет пассажира из очереди.

#### **BuildingProperties**.
- **Назначение**: Хранит статические свойства здания.
- **Атрибуты**:
  - `КОЛИЧЕСТВО_ЭТАЖЕЙ`, `КОЛИЧЕСТВО_ЛИФТОВ`: Настраиваемые константы.
  - Координатные отображения для этажей и лифтовых шахт.
- **Ключевые методы**:
  - `getFloorYCoordinate(int floor)`: Возвращает координату Y для этажа.
  - `getElevatorShaftXCoordinate(int elevatorNumber)`: Возвращает X-координату шахты лифта.

#### **Direction (Enum)**.
- **Назначение**: Представляет направление движения лифта.
- **Значения**: `ВВЕРХ`, `ВНИЗ`, `СТОЯТЬ`.

---

## 5. Рабочий процесс системы
1. **Инициализация**:
  - `ElevatorsApplication` запускает JavaFX-приложение и загружает представление приветствия.
  - При взаимодействии с пользователем `GreetingController` переключается на основной вид.
  - `MainController` инициализирует позиции лифтов и запускает симуляцию.

2. **Генерация запросов**:
  - `RequestsGenerator` постоянно генерирует запросы пассажиров.
  - Запросы обрабатываются `Arbitrator`, который распределяет их по лифтам или ставит в очередь.

3. **Эксплуатация лифтов**:
  - Лифты, управляемые классом `Elevator`, перемещаются между этажами, забирая и высаживая пассажиров.
  - Позиции лифтов и перемещения пассажиров обновляются в реальном времени в пользовательском интерфейсе через `MainController`.

4. **Динамические обновления**:
  - Состояния лифта и пассажиров обновляются с помощью методов JavaFX (`Platform.runLater` обеспечивает безопасность потоков).
