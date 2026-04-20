//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Create objects
        Customer customer = new Customer(1, "Aline", "0781234567");
        DeliveryAgent agent = new DeliveryAgent(2, "Eric", "0789876543");
        Restaurant restaurant = new Restaurant("Kigali Bites");

        Order order = new Order(101);

        System.out.println("Welcome to Food Delivery System!");

        restaurant.displayMenu();

        while (true) {
            System.out.print("\nEnter item number to order (0 to finish): ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            String item = restaurant.getItemByChoice(choice);

            if (item != null) {
                double price = restaurant.getPrice(item);
                order.addItem(item, price);
                System.out.println(item + " added to order.");
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }

        // Process order
        customer.placeOrder(order);
        restaurant.prepareOrder();
        agent.deliverOrder(order);

        // Show summary
        order.displayOrder();

        scanner.close();
    }
}