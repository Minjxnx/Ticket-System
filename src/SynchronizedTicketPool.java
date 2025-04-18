import java.util.ArrayList;
import java.util.List;

// 1. Synchronized implementation
class SynchronizedTicketPool implements TicketPool {
    private final List<String> tickets;
    private final int capacity;

    public SynchronizedTicketPool(int capacity) {
        this.tickets = new ArrayList<>();
        this.capacity = capacity;
    }

    @Override
    public synchronized boolean addTicket(String ticketInfo) {
        if (tickets.size() < capacity) {
            tickets.add(ticketInfo);
            notify(); // Notify waiting consumers
            return true;
        }
        return false;
    }

    @Override
    public synchronized String purchaseTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until a ticket is available
        }

        String ticket = tickets.remove(0);
        notify(); // Notify waiting producers
        return ticket;
    }

    @Override
    public synchronized int getAvailableTickets() {
        return tickets.size();
    }

    @Override
    public synchronized String viewTicketInfo(int index) {
        if (index >= 0 && index < tickets.size()) {
            return tickets.get(index);
        }
        return null;
    }

    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Not handled here, managed by TicketPoolManager
    }
}

