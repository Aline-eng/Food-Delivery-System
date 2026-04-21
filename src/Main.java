import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Customer customer = new Customer(1, "Aline", "0781234567");
        DeliveryAgent agent = new DeliveryAgent(2, "Eric", "0789876543");
        Restaurant restaurant = new Restaurant("Kigali Bites");

        // Runtime polymorphism: calling displayDetails() through parent User reference
        System.out.println("=== System Users ===");
        User[] users = {customer, agent};
        for (User user : users) {
            user.displayDetails(); // calls overridden method based on actual object type
        }

        Order order = new Order(101, customer);

        System.out.println("\nWelcome to Food Delivery System!");
        restaurant.displayMenu();

        while (true) {
            System.out.print("\nEnter item number to order (0 to finish): ");
            int choice = scanner.nextInt();

            if (choice == 0) break;

            FoodItem item = restaurant.getItemByChoice(choice);
            if (item != null) {
                order.addItem(item); // compile-time polymorphism: single item
                System.out.println(item.getName() + " added to order.");
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }

        // Process order
        customer.placeOrder(order);
        restaurant.prepareOrder(customer.getName()); // compile-time polymorphism: overloaded with name
        agent.deliverOrder(order);

        // Show summary
        order.displayOrder();

        scanner.close();
    }
}
