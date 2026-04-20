import java.util.ArrayList;

public class Order {
    private int orderId;
    private ArrayList<String> items;
    private double totalPrice;

    public Order(int orderId) {
        this.orderId = orderId;
        items = new ArrayList<>();
        totalPrice = 0;
    }

    public void addItem(String item, double price) {
        items.add(item);
        totalPrice += price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void displayOrder() {
        System.out.println("\n--- Order Summary ---");
        System.out.println("Order ID: " + orderId);
        System.out.println("Items: " + items);
        System.out.println("Total Price: " + totalPrice + " RWF");
    }
}