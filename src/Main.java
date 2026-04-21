import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Checking User Validation ===");
        try {
            Customer invalid = new Customer(99, "", "123");
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }

        Customer customer = new Customer(1, "Aline", "0781234567");
        DeliveryAgent agent = new DeliveryAgent(2, "Eric", "0789876543");
        Restaurant restaurant = new Restaurant("Kigali Bites");

        System.out.println("\n=== System Users ===");
        User[] users = {customer, agent};
        for (User user : users) {
            user.displayDetails();
        }

        Order order = new Order(101, customer);

        System.out.println("\nWelcome to Food Delivery System!");
        restaurant.displayMenu();

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

        try {
            order.validateOrder();
            customer.placeOrder(order);
            restaurant.prepareOrder(customer.getName());
            agent.deliverOrder(order);
            order.displayOrder();

        } catch (EmptyOrderException e) {
            System.out.println("[Order Error] " + e.getMessage());
        } catch (AgentUnavailableException e) {
            System.out.println("[Delivery Error] " + e.getMessage());
        }

        System.out.println("\n=== Trying to Reassign the Same Agent ===");
        try {
            agent.deliverOrder(order);
        } catch (AgentUnavailableException e) {
            System.out.println("[Delivery Error] " + e.getMessage());
        }

        scanner.close();
    }
}
