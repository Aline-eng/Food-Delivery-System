import java.util.ArrayList;

public class Order {
    private int orderId;
    private Customer customer;
    private ArrayList<FoodItem> items;
    private double totalPrice;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalPrice = 0;
    }

    // Compile-time polymorphism: method overloading
    public void addItem(FoodItem item) {
        items.add(item);
        totalPrice += item.getPrice();
    }

    public void addItem(FoodItem item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
        totalPrice += item.getPrice() * quantity;
    }

    public void validateOrder() {
        if (items.isEmpty()) {
            throw new EmptyOrderException();
        }
    }

    // Returns all item names as a single comma-separated string.
    // Used by DataManager when writing the order to the text file.
    public String getItemsSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).getName());
            if (i < items.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public int getOrderId()       { return orderId; }
    public Customer getCustomer() { return customer; }
    public double getTotalPrice() { return totalPrice; }

    public void displayOrder() {
        System.out.println("\n========== Order Summary ==========");
        System.out.println("Order ID  : " + orderId);
        System.out.println("Customer  : " + customer.getName());
        System.out.println("Items ordered:");
        for (FoodItem item : items) {
            System.out.println("   - " + item);
        }
        System.out.println("Total Price: " + totalPrice + " RWF");
        System.out.println("===================================");
    }
}
