import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 3. BlockingQueue implementation
class BlockingQueueTicketPool implements TicketPool {
    private final BlockingQueue<String> tickets;
    private final int capacity;

    /**
     * Constructs a ticket pool backed by a thread-safe blocking queue.
     */
    public BlockingQueueTicketPool(int capacity) {
        this.tickets = new LinkedBlockingQueue<>(capacity);
        this.capacity = capacity;
    }

    /**
     * Attempts to add a ticket to the queue.
     * Returns false immediately if the queue is full.
     */
    @Override
    public boolean addTicket(String ticketInfo) {
        return tickets.offer(ticketInfo); // Non-blocking, returns false if full
    }

    /**
     * Retrieves and removes the head of the queue, waiting if necessary until a ticket becomes available.
     */
    @Override
    public String purchaseTicket() throws InterruptedException {
        return tickets.take(); // Blocks if queue is empty
    }

    /**
     * Returns the number of tickets currently in the queue.
     */
    @Override
    public int getAvailableTickets() {
        return tickets.size();
    }

    /**
     * Returns ticket info at the given index by copying to a list.
     * Returns null if index is out of bounds.
     */
    @Override
    public String viewTicketInfo(int index) {
        // BlockingQueue doesn't support direct indexing
        List<String> ticketList = new ArrayList<>(tickets);
        if (index >= 0 && index < ticketList.size()) {
            return ticketList.get(index);
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