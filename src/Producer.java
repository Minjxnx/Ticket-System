// Producer class to add tickets
class Producer implements Runnable {
    private final TicketPool ticketPool;
    private final String producerId;
    private volatile boolean running;
    private final int delayMs;

    public Producer(TicketPool ticketPool, String producerId, int delayMs) {
        this.ticketPool = ticketPool;
        this.producerId = producerId;
        this.running = true;
        this.delayMs = delayMs;
    }

    // The main logic of the producer thread
    @Override
    public void run() {
        int ticketCounter = 0;

        while (running) {
            try {
                // Generate new ticket info
                String ticketInfo = "Ticket-" + producerId + "-" + ticketCounter;

                // Try to add the ticket to the pool
                boolean added = ticketPool.addTicket(ticketInfo);

                if (added) {
                    // Ticket added successfully
                    System.out.println(producerId + " produced: " + ticketInfo);
                    ticketCounter++;

                    // Wait for specified delay before producing next ticket
                    Thread.sleep(delayMs);
                } else {
                    // Pool is full, wait longer before retrying
                    System.out.println(producerId + ": Pool full, waiting before retry");
                    Thread.sleep(delayMs * 2); // Wait longer when pool is full
                }
            } catch (InterruptedException e) {
                // Handle thread interruption and stop gracefully
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    // Stops the producer thread
    public void stop() {
        running = false;
    }
}
