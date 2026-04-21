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

    public int getOrderId()           { return orderId; }
    public Customer getCustomer()     { return customer; }
    public double getTotalPrice()     { return totalPrice; }
    public ArrayList<FoodItem> getItems() { return items; }

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
