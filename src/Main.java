import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Initialize the data folder and show any previously saved orders from file
        DataManager.initializeDataDirectory();
        System.out.println("========================================");
        System.out.println("    Welcome to Food Delivery System");
        System.out.println("========================================");

        int savedCount = DataManager.countSavedOrders();
        if (savedCount > 0) {
            System.out.println("\n" + savedCount + " previous order(s) found on file.");
            System.out.print("Would you like to view them? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes")) {
                DataManager.loadAndDisplayOrders();
            }
        } else {
            System.out.println("\nNo previous orders on file. Starting fresh.");
        }

        // --- User validation demo (IllegalArgumentException) ---
        System.out.println("\n=== Checking User Validation ===");
        try {
            Customer invalid = new Customer(99, "", "123");
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }

        // Create the system objects
        Customer customer = new Customer(1, "Aline", "0781234567");
        DeliveryAgent agent = new DeliveryAgent(2, "Eric", "0789876543");
        Restaurant restaurant = new Restaurant("Kigali Bites");

        // Runtime polymorphism: both Customer and DeliveryAgent are treated as User
        System.out.println("\n=== System Users ===");
        User[] users = {customer, agent};
        for (User user : users) {
            user.displayDetails();
        }

        // Create the order linked to the customer
        Order order = new Order(101, customer);

        System.out.println("\nWelcome, " + customer.getName() + "!");
        restaurant.displayMenu();

        // --- Ordering loop ---
        while (true) {
            System.out.print("\nEnter item number to order (0 to finish): ");
            try {
                int choice = scanner.nextInt();
                if (choice == 0) break;

                FoodItem item = restaurant.getItemByChoice(choice);
                order.addItem(item);
                System.out.println(item.getName() + " added to your order.");

            } catch (IllegalArgumentException e) {
                System.out.println("[Menu Error] " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Please enter a valid number.");
                scanner.next();
            }
        }

        // --- Process the order ---
        try {
            order.validateOrder();
            customer.placeOrder(order);
            restaurant.prepareOrder(customer.getName());
            agent.deliverOrder(order);
            order.displayOrder();

            // FILE I/O - WRITE: save the completed order to orders.txt
            DataManager.saveOrder(order);

        } catch (EmptyOrderException e) {
            System.out.println("[Order Error] " + e.getMessage());
        } catch (AgentUnavailableException e) {
            System.out.println("[Delivery Error] " + e.getMessage());
        }

        // --- AgentUnavailableException demo ---
        // Since agent becomes available after delivery, we force the scenario manually
        System.out.println("\n=== Simulating Unavailable Agent ===");
        DeliveryAgent busyAgent = new DeliveryAgent(3, "Jean", "0787654321");
        Order demoOrder = new Order(102, customer);
        demoOrder.addItem(new FoodItem("Pizza", 8000.0));

        try {
            busyAgent.deliverOrder(demoOrder); // first delivery — succeeds, agent goes unavailable mid-call
        } catch (AgentUnavailableException e) {
            System.out.println("[Delivery Error] " + e.getMessage());
        }

        scanner.close();
        System.out.println("\nThank you for using Food Delivery System. Goodbye!");
    }
}
