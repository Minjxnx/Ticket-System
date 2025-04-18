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

    @Override
    public void run() {
        int ticketCounter = 0;

        while (running) {
            try {
                String ticketInfo = "Ticket-" + producerId + "-" + ticketCounter;
                boolean added = ticketPool.addTicket(ticketInfo);

                if (added) {
                    System.out.println(producerId + " produced: " + ticketInfo);
                    ticketCounter++;
                    Thread.sleep(delayMs);
                } else {
                    // If pool is full, wait a bit before trying again
                    System.out.println(producerId + ": Pool full, waiting before retry");
                    Thread.sleep(delayMs * 2); // Wait longer when pool is full
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
