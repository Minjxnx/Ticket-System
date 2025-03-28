public class TicketSystemApplication {

    public static void main(String[] args) {
        // Default pool capacity
        int poolCapacity = 10;

        // Check for command line arguments
        if (args.length > 0) {
            try {
                poolCapacity = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid pool capacity. Using default: " + poolCapacity);
            }
        }

        SimulationManager manager = new SimulationManager(poolCapacity);

        // Start the CLI
        manager.startCLI();
    }
}
