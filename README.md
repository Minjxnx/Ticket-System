# Ticket-System

## Requirements

### System Design

1. **Shared Ticket Pool:**
   - Implement a `TicketPool` class to manage tickets.
   - Ensure thread-safe access to tickets using synchronization techniques.
   
2. **Producers and Consumers:**
   - **Producers (vendors)** add tickets to the pool.
   - **Consumers (customers)** purchase tickets concurrently from the pool.
   
3. **Readers and Writers:**
   - **Readers** query the ticket pool status without blocking others.
   - **Writers (vendors)** update the ticket pool with exclusive access.
   
4. **Synchronization Mechanisms:**
   - Implement and compare the following approaches:
     - **Intrinsic Locks (`synchronized`)**: Ensure thread safety using synchronized methods/blocks.
     - **ReentrantReadWriteLock / ReentrantLock**: Use explicit locks for fine-grained control.
     - **BlockingQueue**: Implement producer-consumer using thread-safe queues (e.g., `ArrayBlockingQueue`, `LinkedBlockingQueue`).

5. **CLI Features:**
   - Configure system parameters (e.g., ticket pool size, producer/consumer rates).
   - Dynamically add/remove producers, consumers, readers, and writers.
   - Display the ticket pool’s real-time state.

### Concurrency Mechanisms
- Use multi-threading concepts (`Thread`, `Runnable`) to simulate operations.
- Implement proper exception handling for thread interruptions.

### Testing
- Test each synchronization technique for correctness, thread safety, and performance.
- Include scenarios such as:
  - Empty ticket pools
  - Maximum capacity limits
  - High concurrency conditions

