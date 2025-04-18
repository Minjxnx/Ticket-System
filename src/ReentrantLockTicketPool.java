import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// 2. ReentrantLock/ReentrantReadWriteLock implementation
class ReentrantLockTicketPool implements TicketPool {
    private final List<String> tickets;
    private final int capacity;
    private final ReentrantReadWriteLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public ReentrantLockTicketPool(int capacity) {
        this.tickets = new ArrayList<>();
        this.capacity = capacity;
        this.lock = new ReentrantReadWriteLock();
        ReentrantLock writeLock = new ReentrantLock();
        this.notEmpty = writeLock.newCondition();
        this.notFull = writeLock.newCondition();
    }

    @Override
    public boolean addTicket(String ticketInfo) {
        lock.writeLock().lock();
        try {
            if (tickets.size() < capacity) {
                tickets.add(ticketInfo);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String purchaseTicket() throws InterruptedException {
        while (true) {
            lock.writeLock().lock();
            try {
                if (!tickets.isEmpty()) {
                    return tickets.remove(0);
                }
            } finally {
                lock.writeLock().unlock();
            }

            // If no tickets are available, wait before trying again
            Thread.sleep(100);
        }
    }

    @Override
    public int getAvailableTickets() {
        lock.readLock().lock();
        try {
            return tickets.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String viewTicketInfo(int index) {
        lock.readLock().lock();
        try {
            if (index >= 0 && index < tickets.size()) {
                return tickets.get(index);
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void switchSynchronizationMechanism(SynchronizationMechanism mechanism) {
        // Not handled here, managed by TicketPoolManager
    }
}