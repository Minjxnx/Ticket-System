// Base interface for TicketPool implementations
public interface TicketPool {

    /**
     * Attempts to add a ticket to the pool.
     *
     * @param ticketInfo the ticket information to add
     * @return true if the ticket was successfully added, false if the pool is full
     */
    boolean addTicket(String ticketInfo);

    /**
     * Purchases (removes and returns) a ticket from the pool.
     * If no ticket is available, this method may block until one becomes available.
     *
     * @return the ticket that was purchased
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    String purchaseTicket() throws InterruptedException;

    /**
     * Gets the number of available tickets in the pool.
     *
     * @return the current number of available tickets
     */
    int getAvailableTickets();

    /**
     * Views information of the ticket at a specified index without removing it.
     *
     * @param index the index of the ticket to view
     * @return the ticket info if the index is valid, or null otherwise
     */
    String viewTicketInfo(int index);

    /**
     * Switches the internal synchronization mechanism used by the ticket pool.
     * This is useful for testing different concurrency control strategies.
     *
     * @param mechanism the synchronization mechanism to switch to
     */
    void switchSynchronizationMechanism(SynchronizationMechanism mechanism);
}
