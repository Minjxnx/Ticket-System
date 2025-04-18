import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 3. BlockingQueue implementation
class BlockingQueueTicketPool implements TicketPool {
    private final BlockingQueue<String> tickets;
    private final int capacity;

    public BlockingQueueTicketPool(int capacity) {
        this.tickets = new LinkedBlockingQueue<>(capacity);
        this.capacity = capacity;
    }

    @Override
    public boolean addTicket(String ticketInfo) {
        return tickets.offer(ticketInfo); // Non-blocking, returns false if full
    }

    @Override
    public String purchaseTicket() throws InterruptedException {
        return tickets.take(); // Blocks if queue is empty
    }

    @Override
    public int getAvailableTickets() {
        return tickets.size();
    }

    @Override
    public String viewTicketInfo(int index) {
        // BlockingQueue doesn't support direct indexing
        List<String> ticketList = new ArrayList<>(tickets);
        if (index >= 0 && index < ticketList.size()) {
            return ticketList.get(index);
        }
        return null;
    }

    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Not handled here, managed by TicketPoolManager
    }
}