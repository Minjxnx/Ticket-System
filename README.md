# Ticket-System

Requirements:

System Design:

1. Shared Ticket Pool:
    o Implement a TicketPool class to manage tickets.
    o Ensure thread-safe access to tickets using synchronization techniques.
   
2. Producers and Consumers:
    o Producers (vendors) add tickets to the pool.
    o Consumers (customers) purchase tickets concurrently from the pool.
   
3. Readers and Writers:
    o Readers query the ticket pool status without blocking others.
    o Writers (vendors) update the ticket pool with exclusive access.
   
4. Synchronization Mechanisms:
    o Implement and compare the following approaches:
        ▪ Intrinsic Locks (synchronized): Ensure thread safety using synchronized methods/blocks.
        ▪ ReentrantReadWriteLock / ReentrantLock: Use explicit locks for fine-grained control.
        ▪ BlockingQueue: Implement producer-consumer using thread-safe queues (e.g., ArrayBlockingQueue, LinkedBlockingQueue).
   
5. CLI Features:
    o Configure system parameters (e.g., ticket pool size, producer/consumer rates).
    o Dynamically add/remove producers, consumers, readers, and writers.
    o Display the ticket pool’s real-time state.
