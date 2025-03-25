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
                String ticketInfo = "Ticket-" + producerId + "-" + ticketCounter++;
                boolean added = ticketPool.addTicket(ticketInfo);

                if (added) {
                    System.out.println(producerId + " produced: " + ticketInfo);
                } else {
                    System.out.println(producerId + " couldn't produce: Pool full");
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
