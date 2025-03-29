// Main TicketPool implementation with capability to switch between synchronization strategies
public class TicketPoolManager implements TicketPool{
    private TicketPool currentImplementation;
    private int capacity;

    public TicketPoolManager(int capacity) {
        this.capacity = capacity;
        // Default to synchronized implementation
        this.currentImplementation = new SynchronizedTicketPool(capacity);
    }

    @Override
    public boolean addTicket(String ticketInfo) {
        return currentImplementation.addTicket(ticketInfo);
    }

    @Override
    public String purchaseTicket() throws InterruptedException {
        return currentImplementation.purchaseTicket();
    }

    @Override
    public int getAvailableTickets() {
        return currentImplementation.getAvailableTickets();
    }

    @Override
    public String viewTicketInfo(int index) {
        return currentImplementation.viewTicketInfo(index);
    }

    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Create new implementation based on desired mechanism
        // Transfer any existing tickets if needed
        TicketPool oldImplementation = currentImplementation;
        int currentSize = oldImplementation.getAvailableTickets();

        switch (mechanism) {
            case SYNCHRONIZED:
                currentImplementation = new SynchronizedTicketPool(capacity);
                break;
            case REENTRANT_LOCK:
                currentImplementation = new ReentrantLockTicketPool(capacity);
                break;
            case BLOCKING_QUEUE:
                currentImplementation = new BlockingQueueTicketPool(capacity);
                break;
        }

        System.out.println("Switched to " + mechanism + " implementation");
    }
}
