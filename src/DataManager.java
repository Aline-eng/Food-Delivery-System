import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String ORDERS_FILE = "data/orders.txt";

    public static void initializeDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Writes a completed order to orders.txt as plain readable text.
    // FileWriter(file, true) means append mode — each new order is added
    // at the bottom without erasing previous ones.
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

    // Reads all saved orders from orders.txt and prints them line by line.
    // This demonstrates the READ side of file I/O.
    public static void loadAndDisplayOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            System.out.println("No previous orders found.");
            return;
        }

        System.out.println("\n========== Previous Orders ==========");
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("[File Error] Could not read orders: " + e.getMessage());
        }
        System.out.println("=====================================");
    }

    // Returns how many orders are saved in the file by counting separator lines.
    public static int countSavedOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) return 0;

        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID")) count++;
            }
        } catch (IOException e) {
            System.out.println("[File Error] Could not count orders: " + e.getMessage());
        }
        return count;
    }
}
