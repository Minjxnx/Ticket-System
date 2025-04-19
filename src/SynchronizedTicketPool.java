import java.util.ArrayList;
import java.util.List;

// 1. Synchronized implementation
class SynchronizedTicketPool implements TicketPool {
    private final List<String> tickets;
    private final int capacity;

    /**
     * Constructs a synchronized ticket pool with specified capacity.
     */
    public SynchronizedTicketPool(int capacity) {
        this.tickets = new ArrayList<>();
        this.capacity = capacity;
    }

    /**
     * Adds a ticket to the pool if there is available capacity.
     * Notifies a waiting consumer if the ticket is successfully added.
     */
    @Override
    public synchronized boolean addTicket(String ticketInfo) {
        if (tickets.size() < capacity) {
            tickets.add(ticketInfo);
            notify(); // Notify waiting consumers
            return true;
        }
        return false;
    }

    /**
     * Waits until at least one ticket is available, then removes and returns it.
     * Notifies a waiting producer after removal.
     */
    @Override
    public synchronized String purchaseTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until a ticket is available
        }

        String ticket = tickets.remove(0);
        notify(); // Notify waiting producers
        return ticket;
    }

    /**
     * Returns the number of available tickets in the pool.
     */
    @Override
    public synchronized int getAvailableTickets() {
        return tickets.size();
    }

    /**
     * Returns the ticket info at the specified index, or null if index is invalid.
     */
    @Override
    public synchronized String viewTicketInfo(int index) {
        if (index >= 0 && index < tickets.size()) {
            return tickets.get(index);
        }
        return null;
    }

    /**
     * No-op for this implementation; switching is handled by TicketPoolManager.
     */
    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Not handled here, managed by TicketPoolManager
    }
}

