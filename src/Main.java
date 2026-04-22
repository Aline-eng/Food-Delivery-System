import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean exitSystem = false;
    
    // Static agents that maintain state throughout the program execution
    private static DeliveryAgent[] agents = {
            new DeliveryAgent(1, "Eric", "0789876543"),
            new DeliveryAgent(2, "Jean", "0787654321"),
            new DeliveryAgent(3, "Marie", "0785678901")
    };

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Welcome to Food Delivery System      ║");
        System.out.println("╚════════════════════════════════════════╝\n");

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
        // Step 1: Customer Identification
        Customer customer = customerIdentificationMenu();
        if (customer == null) {
            return false;
        }

        // Step 2: Restaurant & Food Ordering
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

        // Step 3: Validate Order
        try {
            order.validateOrder();
        } catch (EmptyOrderException e) {
            System.out.println("\n[Order Error] " + e.getMessage());
            return !exitSystem;
        }

        customer.placeOrder(order);
        restaurant.prepareOrder(customer.getName());

        // Step 4: Delivery Agent Selection with Error Handling
        boolean deliveryAssigned = selectDeliveryAgent(order);

        if (deliveryAssigned && !exitSystem) {
            order.displayOrder();
        }

        return !exitSystem;
    }

    /**
     * Customer Identification Menu - Collects name and phone number
     */
    private static Customer customerIdentificationMenu() {
        System.out.println("═══ Customer Identification ═══\n");

        while (true) {
            try {
                // Get Customer Name
                System.out.print("Enter your full name: ");
                String name = scanner.nextLine().trim();

                if (name.isEmpty()) {
                    System.out.println("[Input Error] Name cannot be empty. Please try again.\n");
                    continue;
                }

                // Get Phone Number with Validation
                System.out.print("Enter your phone number (10 digits, e.g., 0781234567): ");
                String phone = scanner.nextLine().trim();

                // Validate Phone Number
                if (!isValidPhoneNumber(phone)) {
                    System.out.println("[Input Error] Phone number must be exactly 10 digits. Please try again.\n");
                    continue;
                }

                // Create Customer
                Customer customer = new Customer(1, name, phone);
                System.out.println("\nCustomer registered: " + customer.getName());
                customer.displayDetails();
                System.out.println();
                return customer;

            } catch (IllegalArgumentException e) {
                System.out.println("[Validation Error] " + e.getMessage() + "\n");
            } catch (InputMismatchException e) {
                System.out.println("[Input Error] Invalid input. Please try again.\n");
                scanner.nextLine(); // Clear buffer
            }

            // Menu to Continue or Exit
            if (!continueMenu("Return to identification")) {
                return null;
            }
        }
    }

    /**
     * Restaurant Selection Menu
     */
    private static Restaurant selectRestaurant() {
        System.out.println("═══ Select Restaurant ═══\n");
        System.out.println("1. Kigali Bites");
        System.out.println("2. Pizza Palace");
        System.out.println("0. Exit");
        System.out.print("\nEnter your choice: ");

        try {
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    return new Restaurant("Kigali Bites");
                case 2:
                    return new Restaurant("Pizza Palace");
                case 0:
                    return null;
                default:
                    System.out.println("[Input Error] Invalid choice. Please select 1, 2, or 0.\n");
                    return selectRestaurant();
            }
        } catch (InputMismatchException e) {
            System.out.println("[Input Error] Please enter a valid number.\n");
            return selectRestaurant();
        }
    }

    /**
     * Place Order Menu - Allows user to select items from restaurant menu
     */
    private static void placeOrder(Order order, Restaurant restaurant) {
        System.out.println("\n═══ Place Your Order ═══");
        restaurant.displayMenu();

        while (true) {
            System.out.print("\nEnter item number to order (0 to finish, 9 for menu): ");

            try {
                int choice = getIntInput();

                if (choice == 0) {
                    break;
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

    /**
     * Delivery Agent Selection Menu - Handles agent selection and unavailability
     */
    private static boolean selectDeliveryAgent(Order order) {
        while (true) {
            System.out.println("\n═══ Select Delivery Agent ═══\n");
            displayAvailableAgents(agents);

            System.out.print("\nEnter agent number to deliver your order (0 to cancel): ");

            try {
                int choice = getIntInput();

                if (choice == 0) {
                    System.out.println("\nOrder delivery cancelled.");
                    return false;
                }

                if (choice < 1 || choice > agents.length) {
                    System.out.println("[Input Error] Invalid choice. Please select a valid agent number.\n");
                    continue;
                }

                DeliveryAgent selectedAgent = agents[choice - 1];

                try {
                    // Attempt to deliver order
                    selectedAgent.deliverOrder(order);
                    System.out.println("\n[SUCCESS] Order #" + order.getOrderId() + " assigned to " + selectedAgent.getName());
                    return true;

                } catch (AgentUnavailableException e) {
                    System.out.println("\n[Delivery Error] " + e.getMessage());
                    
                    // Show recovery options
                    System.out.println("\nOptions:");
                    System.out.println("1. Select another agent");
                    System.out.println("2. Return to main menu");
                    System.out.println("3. Exit system");
                    System.out.print("\nEnter your choice: ");

                    int recoveryChoice = getIntInput();

                    switch (recoveryChoice) {
                        case 1:
                            continue; // Try selecting another agent
                        case 2:
                            System.out.println("\nReturning to main menu...");
                            return false;
                        case 3:
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

    /**
     * Display available and unavailable agents
     */
    private static void displayAvailableAgents(DeliveryAgent[] agentList) {
        System.out.println("Available Delivery Agents:");
        for (int i = 0; i < agentList.length; i++) {
            DeliveryAgent agent = agentList[i];
            String status = agent.isAvailable() ? "[AVAILABLE]" : "[UNAVAILABLE]";
            System.out.println((i + 1) + ". " + agent.getName() + " - " + status);
        }
    }

    /**
     * Menu to continue or exit
     */
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

    /**
     * Get integer input from user
     */
    private static int getIntInput() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear buffer
            throw e;
        }
    }

    /**
     * Validate phone number format (10 digits)
     */
    private static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    /**
     * Generate unique order ID
     */
    private static int generateOrderId() {
        return 100 + (int) (Math.random() * 900);
    }
}
