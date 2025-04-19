// Reader class to query ticket pool status
class Reader implements Runnable {
    private final TicketPool ticketPool;
    private final String readerId;
    private volatile boolean running;
    private final int delayMs;

    public Reader(TicketPool ticketPool, String readerId, int delayMs) {
        this.ticketPool = ticketPool;
        this.readerId = readerId;
        this.running = true;
        this.delayMs = delayMs;
    }

    // The main logic of the reader thread
    @Override
    public void run() {
        while (running) {
            try {
                // Read and print the number of available tickets
                int availableTickets = ticketPool.getAvailableTickets();
                System.out.println(readerId + " read: " + availableTickets + " tickets available");

                // Optionally peek at the first ticket if available
                if (availableTickets > 0) {
                    String ticketInfo = ticketPool.viewTicketInfo(0);
                    if (ticketInfo != null) {
                        System.out.println(readerId + " peeked at first ticket: " + ticketInfo);
                    }
                }

                // Wait for a specified delay before the next read
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                // Handle thread interruption and stop gracefully
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    // Stops the reader thread
    public void stop() {
        running = false;
    }
}