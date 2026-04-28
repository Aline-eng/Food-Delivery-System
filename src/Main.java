import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<Customer> customers = new ArrayList<>();
    static List<DeliveryAgent> agents = new ArrayList<>();
    static Restaurant restaurant = new Restaurant("Kigali Bites");
    static int orderCounter = 1;

    public static void main(String[] args) {

        DataManager.initializeDataDirectory();

        // Load previously registered customers and agents from file on startup
        customers = DataManager.loadCustomers();
        agents = DataManager.loadAgents();

        System.out.println("========================================");
        System.out.println("     Welcome to Food Delivery System    ");
        System.out.println("========================================");
        System.out.println("Loaded " + customers.size() + " customer(s) and "
                + agents.size() + " agent(s) from file.");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1: registerCustomer();  break;
                case 2: registerAgent();     break;
                case 3: placeOrder();        break;
                case 4: viewCustomers();     break;
                case 5: viewAgents();        break;
                case 6: DataManager.loadAndDisplayOrders(); break;
                case 7:
                    running = false;
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("[Error] Please enter a number between 1 and 7.");
            }
        }
        scanner.close();
    }

    // ---------------------------------------------------------------
    // MAIN MENU
    // ---------------------------------------------------------------

    static void printMainMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1. Register new customer");
        System.out.println("2. Register new delivery agent");
        System.out.println("3. Place an order");
        System.out.println("4. View all customers");
        System.out.println("5. View all delivery agents");
        System.out.println("6. View all saved orders");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    // ---------------------------------------------------------------
    // 1. REGISTER CUSTOMER
    // ---------------------------------------------------------------

    static void registerCustomer() {
        System.out.println("\n--- Register New Customer ---");
        try {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter phone number (10 digits): ");
            String phone = scanner.nextLine().trim();

            // Customer constructor calls User constructor which validates name and phone.
            // If invalid, IllegalArgumentException is thrown and caught below.
            Customer customer = new Customer(name, phone);
            customers.add(customer);

            // FILE I/O - WRITE: save the new customer to customers.txt
            DataManager.saveCustomer(customer);

            System.out.println("Customer registered successfully!");
            customer.displayDetails();

        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 2. REGISTER DELIVERY AGENT
    // ---------------------------------------------------------------

    static void registerAgent() {
        System.out.println("\n--- Register New Delivery Agent ---");
        try {
            System.out.print("Enter agent name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter phone number (10 digits): ");
            String phone = scanner.nextLine().trim();

            DeliveryAgent agent = new DeliveryAgent(name, phone);
            agents.add(agent);

            // FILE I/O - WRITE: save the new agent to agents.txt
            DataManager.saveAgent(agent);

            System.out.println("Delivery agent registered successfully!");
            agent.displayDetails();

        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 3. PLACE AN ORDER
    // ---------------------------------------------------------------

    static void placeOrder() {
        System.out.println("\n--- Place an Order ---");

        // Step 1: pick a customer
        if (customers.isEmpty()) {
            System.out.println("[Error] No customers registered yet. Please register a customer first.");
            return;
        }
        System.out.println("\nSelect a customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.print((i + 1) + ". ");
            customers.get(i).displayDetails();
        }
        System.out.print("Enter customer number: ");
        int customerChoice = readInt();
        if (customerChoice < 1 || customerChoice > customers.size()) {
            System.out.println("[Error] Invalid customer selection.");
            return;
        }
        Customer customer = customers.get(customerChoice - 1);

        // Step 2: pick a delivery agent
        if (agents.isEmpty()) {
            System.out.println("[Error] No delivery agents registered yet. Please register an agent first.");
            return;
        }
        System.out.println("\nSelect a delivery agent:");
        for (int i = 0; i < agents.size(); i++) {
            System.out.print((i + 1) + ". ");
            agents.get(i).displayDetails();
        }
        System.out.print("Enter agent number: ");
        int agentChoice = readInt();
        if (agentChoice < 1 || agentChoice > agents.size()) {
            System.out.println("[Error] Invalid agent selection.");
            return;
        }
        DeliveryAgent agent = agents.get(agentChoice - 1);

        // Step 3: select items from the menu
        Order order = new Order(orderCounter++, customer);
        restaurant.displayMenu();

        while (true) {
            System.out.print("\nEnter item number to add (0 to finish): ");
            try {
                int itemChoice = readInt();
                if (itemChoice == 0) break;

                // getItemByChoice throws IllegalArgumentException if item not on menu
                FoodItem item = restaurant.getItemByChoice(itemChoice);
                order.addItem(item);
                System.out.println(item.getName() + " added. Running total: " + order.getTotalPrice() + " RWF");

            } catch (IllegalArgumentException e) {
                System.out.println("[Menu Error] " + e.getMessage());
            }
        }

        // Step 4: process the order
        try {
            // validateOrder throws EmptyOrderException if no items were added
            order.validateOrder();

            customer.placeOrder(order);
            restaurant.prepareOrder(customer.getName());

            // deliverOrder throws AgentUnavailableException if agent is busy
            agent.deliverOrder(order);

            order.displayOrder();

            // FILE I/O - WRITE: save the completed order to orders.txt
            DataManager.saveOrder(order);

        } catch (EmptyOrderException e) {
            System.out.println("[Order Error] " + e.getMessage());
        } catch (AgentUnavailableException e) {
            System.out.println("[Delivery Error] " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // 4. VIEW ALL CUSTOMERS
    // ---------------------------------------------------------------

    static void viewCustomers() {
        System.out.println("\n--- Registered Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }
        // Runtime polymorphism: each User reference calls the correct displayDetails()
        for (User user : customers) {
            user.displayDetails();
        }
    }

    // ---------------------------------------------------------------
    // 5. VIEW ALL AGENTS
    // ---------------------------------------------------------------

    static void viewAgents() {
        System.out.println("\n--- Registered Delivery Agents ---");
        if (agents.isEmpty()) {
            System.out.println("No delivery agents registered yet.");
            return;
        }
        for (User user : agents) {
            user.displayDetails();
        }
    }

    // ---------------------------------------------------------------
    // HELPER: read an integer safely
    // ---------------------------------------------------------------

    static int readInt() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.print("[Input Error] Please enter a valid number: ");
            }
        }
    }
}
