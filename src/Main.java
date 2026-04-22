import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean exitSystem = false;

    // The system manages many agents, so List represents this one-to-many relationship.
    private static final List<DeliveryAgent> agents = createAgents();
    // Restaurant selection is a lookup by number, so Map is the right fit for this relationship.
    private static final Map<Integer, Restaurant> restaurants = createRestaurants();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Welcome to Food Delivery System");
        System.out.println("========================================\n");

        try {
            while (!exitSystem) {
                boolean continueRunning = processOrderSession();
                if (!continueRunning) {
                    break;
                }

                if (!continueMenu("Place another order")) {
                    break;
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\n[Validation Error] " + e.getMessage());
        } catch (AgentUnavailableException e) {
            System.out.println("\n[Delivery Error] " + e.getMessage());
        } finally {
            scanner.close();
        }

        System.out.println("\nThank you for using Food Delivery System. Goodbye!");
    }

    private static boolean processOrderSession() {
        Customer customer = customerIdentificationMenu();
        if (customer == null) {
            return false;
        }

        Restaurant restaurant = selectRestaurant();
        if (restaurant == null) {
            return false;
        }

        Order order = new Order(generateOrderId(), customer);
        placeOrder(order, restaurant);

        if (order.getItems().isEmpty()) {
            System.out.println("\nNo items ordered.");
            return !exitSystem;
        }

        try {
            order.validateOrder();
        } catch (EmptyOrderException e) {
            System.out.println("\n[Order Error] " + e.getMessage());
            return !exitSystem;
        }

        customer.placeOrder(order);
        restaurant.prepareOrder(customer.getName());

        boolean deliveryAssigned = selectDeliveryAgent(order);

        if (deliveryAssigned && !exitSystem) {
            Order recentOrder = customer.getMostRecentOrder();
            if (recentOrder != null) {
                System.out.println("Latest order in history: #" + recentOrder.getOrderId());
            }
            order.displayOrder();
        }

        return !exitSystem;
    }

    private static Customer customerIdentificationMenu() {
        System.out.println("=== Customer Identification ===\n");

        while (true) {
            try {
                System.out.print("Enter your full name: ");
                String name = scanner.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println("[Input Error] Name cannot be empty. Please try again.\n");
                    continue;
                }

                System.out.print("Enter your phone number (10 digits, e.g., 0781234567): ");
                String phone = scanner.nextLine().trim();

                if (!isValidPhoneNumber(phone)) {
                    System.out.println("[Input Error] Phone number must be exactly 10 digits. Please try again.\n");
                    continue;
                }

                Customer customer = new Customer(1, name, phone);
                System.out.println("\nCustomer registered: " + customer.getName());
                customer.displayDetails();
                System.out.println();
                return customer;

            } catch (IllegalArgumentException e) {
                System.out.println("[Validation Error] " + e.getMessage() + "\n");
            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Invalid input. Please try again.\n");
                scanner.nextLine();
            }

            if (!continueMenu("Return to identification")) {
                return null;
            }
        }
    }

    private static Restaurant selectRestaurant() {
        System.out.println("=== Select Restaurant ===\n");
        for (Map.Entry<Integer, Restaurant> entry : restaurants.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getName());
        }
        System.out.println("0. Exit");
        System.out.print("\nEnter your choice: ");

        try {
            int choice = getIntInput();

            if (choice == 0) {
                return null;
            }

            Restaurant restaurant = restaurants.get(choice);
            if (restaurant == null) {
                System.out.println("[Input Error] Invalid choice. Please select an available restaurant.\n");
                return selectRestaurant();
            }
            return restaurant;
        } catch (InputMismatchException e) {
            System.out.println("[Input Error] Please enter a valid number.\n");
            return selectRestaurant();
        }
    }

    private static void placeOrder(Order order, Restaurant restaurant) {
        System.out.println("\n=== Place Your Order ===");
        restaurant.displayMenu();

        while (true) {
            System.out.print("\nEnter item number to order (0 finish, 8 remove last item, 9 menu): ");

            try {
                int choice = getIntInput();

                if (choice == 0) {
                    break;
                } else if (choice == 8) {
                    removeLastItem(order);
                } else if (choice == 9) {
                    restaurant.displayMenu();
                } else {
                    FoodItem item = restaurant.getItemByChoice(choice);
                    order.addItem(item);
                    System.out.println(item.getName() + " added to your order. (RWF " + item.getPrice() + ")");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("[Menu Error] " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static boolean selectDeliveryAgent(Order order) {
        while (true) {
            System.out.println("\n=== Select Delivery Agent ===\n");
            displayAvailableAgents(agents);

            System.out.print("\nEnter agent number to deliver your order (0 to cancel): ");

            try {
                int choice = getIntInput();

                if (choice == 0) {
                    order.getCustomer().cancelOrder(order.getOrderId());
                    System.out.println("\nOrder delivery cancelled.");
                    return false;
                }

                if (choice < 1 || choice > agents.size()) {
                    System.out.println("[Input Error] Invalid choice. Please select a valid agent number.\n");
                    continue;
                }

                DeliveryAgent selectedAgent = agents.get(choice - 1);

                try {
                    selectedAgent.deliverOrder(order);
                    System.out.println("\n[SUCCESS] Order #" + order.getOrderId() + " assigned to " + selectedAgent.getName());
                    System.out.println(selectedAgent.getName() + " has delivered "
                            + selectedAgent.getDeliveredOrderIds().size() + " order(s) so far.");
                    return true;

                } catch (AgentUnavailableException e) {
                    System.out.println("\n[Delivery Error] " + e.getMessage());

                    System.out.println("\nOptions:");
                    System.out.println("1. Select another agent");
                    System.out.println("2. Return to main menu");
                    System.out.println("3. Exit system");
                    System.out.print("\nEnter your choice: ");

                    int recoveryChoice = getIntInput();

                    switch (recoveryChoice) {
                        case 1:
                            continue;
                        case 2:
                            order.getCustomer().cancelOrder(order.getOrderId());
                            System.out.println("\nReturning to main menu...");
                            return false;
                        case 3:
                            order.getCustomer().cancelOrder(order.getOrderId());
                            System.out.println("\nExiting system...");
                            exitSystem = true;
                            return false;
                        default:
                            System.out.println("[Input Error] Invalid choice. Returning to agent selection.\n");
                    }
                }

            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void displayAvailableAgents(List<DeliveryAgent> agentList) {
        System.out.println("Available Delivery Agents:");
        for (int i = 0; i < agentList.size(); i++) {
            DeliveryAgent agent = agentList.get(i);
            String status = agent.isAvailable() ? "[AVAILABLE]" : "[UNAVAILABLE]";
            System.out.println((i + 1) + ". " + agent.getName() + " - " + status);
        }
    }

    private static void removeLastItem(Order order) {
        if (order.getItems().isEmpty()) {
            System.out.println("[Order Error] There is no item to remove.");
            return;
        }

        FoodItem removedItem = order.removeItem(order.getItems().size());
        System.out.println(removedItem.getName() + " removed from your order.");
    }

    private static boolean continueMenu(String context) {
        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. " + context);
            System.out.println("2. Exit system");
            System.out.print("\nEnter your choice (1 or 2): ");

            try {
                int choice = getIntInput();

                if (choice == 1) {
                    return true;
                }

                if (choice == 2) {
                    return false;
                }

                System.out.println("[Input Error] Please enter 1 or 2.");
            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Please enter 1 or 2.");
            }
        }
    }

    private static int getIntInput() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw e;
        }
    }

    private static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    private static int generateOrderId() {
        return 100 + (int) (Math.random() * 900);
    }

    private static List<DeliveryAgent> createAgents() {
        List<DeliveryAgent> agentList = new ArrayList<>();
        agentList.add(new DeliveryAgent(1, "Eric", "0789876543"));
        agentList.add(new DeliveryAgent(2, "Jean", "0787654321"));
        agentList.add(new DeliveryAgent(3, "Marie", "0785678901"));
        return agentList;
    }

    private static Map<Integer, Restaurant> createRestaurants() {
        Map<Integer, Restaurant> restaurantMap = new LinkedHashMap<>();

        Restaurant kigaliBites = new Restaurant("Kigali Bites");
        kigaliBites.addMenuItem(4, new FoodItem("Fresh Juice", 2500.0));
        kigaliBites.removeMenuItem(4);

        Restaurant pizzaPalace = new Restaurant("Pizza Palace");
        pizzaPalace.addMenuItem(4, new FoodItem("Garlic Bread", 3000.0));

        restaurantMap.put(1, kigaliBites);
        restaurantMap.put(2, pizzaPalace);
        return restaurantMap;
    }
}
