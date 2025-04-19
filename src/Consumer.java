// Consumer class to purchase tickets
class Consumer implements Runnable {
    private final TicketPool ticketPool;
    private final String consumerId;
    private volatile boolean running;
    private final int delayMs;

    public Consumer(TicketPool ticketPool, String consumerId, int delayMs) {
        this.ticketPool = ticketPool;
        this.consumerId = consumerId;
        this.running = true;
        this.delayMs = delayMs;
    }

    // The main logic of the consumer thread
    @Override
    public void run() {
        while (running) {
            try {
                // Attempt to purchase a ticket from the pool
                String ticket = ticketPool.purchaseTicket();
                System.out.println(consumerId + " purchased: " + ticket);

                // Wait for a specified delay before next purchase
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                // Handle thread interruption and stop gracefully
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    // Stops the consumer thread
    public void stop() {
        running = false;
    }
}