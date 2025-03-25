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

    @Override
    public void run() {
        while (running) {
            try {
                String ticket = ticketPool.purchaseTicket();
                System.out.println(consumerId + " purchased: " + ticket);

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