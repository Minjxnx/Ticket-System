// Main TicketPool implementation with capability to switch between synchronization strategies
public class TicketPoolManager implements TicketPool{
    private TicketPool currentImplementation;
    private int capacity;

    /**
     * Constructs a TicketPoolManager with the specified ticket pool capacity.
     * Initializes the default implementation using the synchronized approach.
     *
     * @param capacity the maximum number of tickets allowed in the pool
     */
    public TicketPoolManager(int capacity) {
        this.capacity = capacity;
        // Default to synchronized implementation
        this.currentImplementation = new SynchronizedTicketPool(capacity);
    }

    /**
     * Adds a ticket to the current ticket pool implementation.
     *
     * @param ticketInfo the ticket information to add
     * @return true if the ticket was successfully added, false if the pool is full
     */
    @Override
    public boolean addTicket(String ticketInfo) {
        return currentImplementation.addTicket(ticketInfo);
    }

    /**
     * Purchases a ticket from the current ticket pool implementation.
     *
     * @return the ticket information that was purchased
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Override
    public String purchaseTicket() throws InterruptedException {
        return currentImplementation.purchaseTicket();
    }

    /**
     * Retrieves the number of available tickets from the current implementation.
     *
     * @return the number of tickets currently available in the pool
     */
    @Override
    public int getAvailableTickets() {
        return currentImplementation.getAvailableTickets();
    }

    /**
     * Views the ticket information at a specific index from the current implementation.
     *
     * @param index the index of the ticket to view
     * @return the ticket info if the index is valid, otherwise null
     */
    @Override
    public String viewTicketInfo(int index) {
        return currentImplementation.viewTicketInfo(index);
    }

    /**
     * Switches the synchronization mechanism used by the ticket pool.
     * This does not transfer existing tickets between implementations.
     *
     * @param mechanism the new synchronization mechanism to use
     */
    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Store reference to the old implementation (optional for future transfer logic)
        TicketPool oldImplementation = currentImplementation;

        // You could potentially transfer tickets here if needed
        int currentSize = oldImplementation.getAvailableTickets();

        // Create a new pool instance based on the selected synchronization mechanism
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
