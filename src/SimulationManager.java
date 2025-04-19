import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// SimulationManager to manage the entire simulation
public class SimulationManager {
    private final TicketPool ticketPool;
    private final int poolCapacity;
    private final Map<String, Thread> producerThreads;
    private final Map<String, Producer> producers;
    private final Map<String, Thread> consumerThreads;
    private final Map<String, Consumer> consumers;
    private final Map<String, Thread> readerThreads;
    private final Map<String, Reader> readers;

    private final Scanner scanner;
    private SynchronizationMechanism currentMechanism = SynchronizationMechanism.SYNCHRONIZED;

    // Constructor initializes the simulation manager with the given pool capacity
    public SimulationManager(int poolCapacity) {
        this.poolCapacity = poolCapacity;
        this.ticketPool = new TicketPoolManager(poolCapacity);
        this.producerThreads = new HashMap<>();
        this.producers = new HashMap<>();
        this.consumerThreads = new HashMap<>();
        this.consumers = new HashMap<>();
        this.readerThreads = new HashMap<>();
        this.readers = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    // Adds a new producer with the given ID and delay
    public void addProducer(String id, int delayMs) {
        Producer producer = new Producer(ticketPool, id, delayMs);
        Thread thread = new Thread(producer);

        producers.put(id, producer);
        producerThreads.put(id, thread);

        thread.start();
        System.out.println("Producer " + id + " added");
    }

    // Stops and removes a producer with the given ID
    public void removeProducer(String id) {
        if (producers.containsKey(id)) {
            producers.get(id).stop();
            producerThreads.get(id).interrupt();

            producers.remove(id);
            producerThreads.remove(id);

            System.out.println("Producer " + id + " removed");
        } else {
            System.out.println("Producer " + id + " not found");
        }
    }

    // Adds a new consumer with the given ID and delay
    public void addConsumer(String id, int delayMs) {
        Consumer consumer = new Consumer(ticketPool, id, delayMs);
        Thread thread = new Thread(consumer);

        consumers.put(id, consumer);
        consumerThreads.put(id, thread);

        thread.start();
        System.out.println("Consumer " + id + " added");
    }

    // Stops and removes a consumer with the given ID
    public void removeConsumer(String id) {
        if (consumers.containsKey(id)) {
            consumers.get(id).stop();
            consumerThreads.get(id).interrupt();

            consumers.remove(id);
            consumerThreads.remove(id);

            System.out.println("Consumer " + id + " removed");
        } else {
            System.out.println("Consumer " + id + " not found");
        }
    }

    // Adds a new reader with the given ID and delay
    public void addReader(String id, int delayMs) {
        Reader reader = new Reader(ticketPool, id, delayMs);
        Thread thread = new Thread(reader);

        readers.put(id, reader);
        readerThreads.put(id, thread);

        thread.start();
        System.out.println("Reader " + id + " added");
    }

    // Stops and removes a reader with the given ID
    public void removeReader(String id) {
        if (readers.containsKey(id)) {
            readers.get(id).stop();
            readerThreads.get(id).interrupt();

            readers.remove(id);
            readerThreads.remove(id);

            System.out.println("Reader " + id + " removed");
        } else {
            System.out.println("Reader " + id + " not found");
        }
    }

    // Switches the synchronization mechanism used by the ticket pool
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        ticketPool.switchSynchronizationMechanism(mechanism);
        currentMechanism = mechanism;
    }

    // Displays the current state of the ticket pool and simulation
    public void displayTicketPoolState() {
        System.out.println("--------- Ticket Pool State ---------");
        System.out.println("Current synchronization: " + currentMechanism);
        System.out.println("Pool capacity: " + poolCapacity);
        System.out.println("Available tickets: " + ticketPool.getAvailableTickets());
        System.out.println("Active producers: " + producers.size());
        System.out.println("Active consumers: " + consumers.size());
        System.out.println("Active readers: " + readers.size());
        System.out.println("------------------------------------");

        // Display a few sample tickets if available
        int availableTickets = ticketPool.getAvailableTickets();
        if (availableTickets > 0) {
            System.out.println("Sample tickets:");
            for (int i = 0; i < Math.min(3, availableTickets); i++) {
                System.out.println("- " + ticketPool.viewTicketInfo(i));
            }
            if (availableTickets > 3) {
                System.out.println("- ... and " + (availableTickets - 3) + " more");
            }
            System.out.println("------------------------------------");
        }
    }

    // Starts the CLI interface for interactive simulation control
    public void startCLI() {
        boolean running = true;

        System.out.println("Welcome to Ticket System Simulation");
        System.out.println("Pool capacity: " + poolCapacity);
        System.out.println("Available commands:");
        printHelp();

        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                running = false;
            } else {
                processCommand(command);
            }
        }

        // Cleanup
        shutdownAll();
    }

    // Prints help message with available commands
    private void printHelp() {
        System.out.println("help - Display available commands");
        System.out.println("add-producer <id> <delayMs> - Add a new producer");
        System.out.println("remove-producer <id> - Remove a producer");
        System.out.println("add-consumer <id> <delayMs> - Add a new consumer");
        System.out.println("remove-consumer <id> - Remove a consumer");
        System.out.println("add-reader <id> <delayMs> - Add a new reader");
        System.out.println("remove-reader <id> - Remove a reader");
        System.out.println("switch-sync <mechanism> - Switch synchronization mechanism (SYNCHRONIZED, REENTRANT_LOCK, BLOCKING_QUEUE)");
        System.out.println("state - Display ticket pool state");
        System.out.println("exit - Exit the simulation");
    }

    // Processes user input commands
    private void processCommand(String command) {
        String[] parts = command.split("\\s+");

        try {
            switch (parts[0]) {
                case "help":
                    printHelp();
                    break;

                case "add-producer":
                    if (parts.length >= 3) {
                        addProducer(parts[1], Integer.parseInt(parts[2]));
                    } else {
                        System.out.println("Invalid command format. Use: add-producer <id> <delayMs>");
                    }
                    break;

                case "remove-producer":
                    if (parts.length >= 2) {
                        removeProducer(parts[1]);
                    } else {
                        System.out.println("Invalid command format. Use: remove-producer <id>");
                    }
                    break;

                case "add-consumer":
                    if (parts.length >= 3) {
                        addConsumer(parts[1], Integer.parseInt(parts[2]));
                    } else {
                        System.out.println("Invalid command format. Use: add-consumer <id> <delayMs>");
                    }
                    break;

                case "remove-consumer":
                    if (parts.length >= 2) {
                        removeConsumer(parts[1]);
                    } else {
                        System.out.println("Invalid command format. Use: remove-consumer <id>");
                    }
                    break;

                case "add-reader":
                    if (parts.length >= 3) {
                        addReader(parts[1], Integer.parseInt(parts[2]));
                    } else {
                        System.out.println("Invalid command format. Use: add-reader <id> <delayMs>");
                    }
                    break;

                case "remove-reader":
                    if (parts.length >= 2) {
                        removeReader(parts[1]);
                    } else {
                        System.out.println("Invalid command format. Use: remove-reader <id>");
                    }
                    break;

                case "switch-sync":
                    if (parts.length >= 2) {
                        try {
                            SynchronizationMechanism mechanism = SynchronizationMechanism.valueOf(parts[1]);
                            switchSynchronizationMechanism(mechanism);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid mechanism. Use: SYNCHRONIZED, REENTRANT_LOCK, or BLOCKING_QUEUE");
                        }
                    } else {
                        System.out.println("Invalid command format. Use: switch-sync <mechanism>");
                    }
                    break;

                case "state":
                    displayTicketPoolState();
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Gracefully stops all running threads and closes scanner
    private void shutdownAll() {
        // Stop all producers
        for (String id : new ArrayList<>(producers.keySet())) {
            removeProducer(id);
        }

        // Stop all consumers
        for (String id : new ArrayList<>(consumers.keySet())) {
            removeConsumer(id);
        }

        // Stop all readers
        for (String id : new ArrayList<>(readers.keySet())) {
            removeReader(id);
        }

        scanner.close();
    }
}
