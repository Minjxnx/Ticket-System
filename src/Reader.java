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

    @Override
    public void run() {
        while (running) {
            try {
                int availableTickets = ticketPool.getAvailableTickets();
                System.out.println(readerId + " read: " + availableTickets + " tickets available");

                // Optionally look at ticket info for a specific index if tickets are available
                if (availableTickets > 0) {
                    String ticketInfo = ticketPool.viewTicketInfo(0);
                    if (ticketInfo != null) {
                        System.out.println(readerId + " peeked at first ticket: " + ticketInfo);
                    }
                }

                Thread.sleep(delayMs);
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