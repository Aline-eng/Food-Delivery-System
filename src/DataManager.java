import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String ORDERS_FILE    = "data/orders.txt";
    private static final String CUSTOMERS_FILE = "data/customers.txt";
    private static final String AGENTS_FILE    = "data/agents.txt";

    public static void initializeDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // ---------------------------------------------------------------
    // ORDERS — write and read
    // ---------------------------------------------------------------

    public static void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.write("Order ID  : " + order.getOrderId());
            writer.newLine();
            writer.write("Customer  : " + order.getCustomer().getName()
                    + " | Phone: " + order.getCustomer().getPhone());
            writer.newLine();
            writer.write("Items     : " + order.getItemsSummary());
            writer.newLine();
            writer.write("Total     : " + order.getTotalPrice() + " RWF");
            writer.newLine();
            writer.write("------------------------------------");
            writer.newLine();
            System.out.println("Order #" + order.getOrderId() + " saved to " + ORDERS_FILE);
        } catch (IOException e) {
            System.out.println("[File Error] Could not save order: " + e.getMessage());
        }
    }

    public static void loadAndDisplayOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No orders on file yet.");
            return;
        }
        System.out.println("\n========== Saved Orders ==========");
        printFile(ORDERS_FILE);
        System.out.println("==================================");
    }

    public static int countSavedOrders() {
        return countLinesStartingWith(ORDERS_FILE, "Order ID");
    }

    // ---------------------------------------------------------------
    // CUSTOMERS — write and read
    // ---------------------------------------------------------------

    // Saves one customer as a single line: ID,Name,Phone
    public static void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE, true))) {
            writer.write(customer.getId() + "," + customer.getName() + "," + customer.getPhone());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[File Error] Could not save customer: " + e.getMessage());
        }
    }

    // Reads customers.txt and rebuilds the list of Customer objects
    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return customers;

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    customers.add(new Customer(parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("[File Error] Could not load customers: " + e.getMessage());
        }
        return customers;
    }

    // ---------------------------------------------------------------
    // AGENTS — write and read
    // ---------------------------------------------------------------

    public static void saveAgent(DeliveryAgent agent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AGENTS_FILE, true))) {
            writer.write(agent.getId() + "," + agent.getName() + "," + agent.getPhone());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[File Error] Could not save agent: " + e.getMessage());
        }
    }

    public static List<DeliveryAgent> loadAgents() {
        List<DeliveryAgent> agents = new ArrayList<>();
        File file = new File(AGENTS_FILE);
        if (!file.exists()) return agents;

        try (BufferedReader reader = new BufferedReader(new FileReader(AGENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    agents.add(new DeliveryAgent(parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("[File Error] Could not load agents: " + e.getMessage());
        }
        return agents;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static void printFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("[File Error] Could not read file: " + e.getMessage());
        }
    }

    private static int countLinesStartingWith(String filePath, String prefix) {
        int count = 0;
        File file = new File(filePath);
        if (!file.exists()) return 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prefix)) count++;
            }
        } catch (IOException e) {
            System.out.println("[File Error] " + e.getMessage());
        }
        return count;
    }
}
