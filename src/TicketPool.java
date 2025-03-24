// Base interface for TicketPool implementations
public interface TicketPool {
    boolean addTicket(String ticketInfo);
    String purchaseTicket() throws InterruptedException;
    int getAvailableTickets();
    String viewTicketInfo(int index);
    void switchSynchronizationMechanism(SynchronizationMechanism mechanism);
}
