import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final int orderId;
    private final Customer customer;
    // One order can contain many food items, so List preserves insertion order and duplicates.
    private final List<FoodItem> items;
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

    public FoodItem removeItem(int itemNumber) {
        if (itemNumber < 1 || itemNumber > items.size()) {
            throw new IllegalArgumentException("Invalid item number to remove.");
        }

        FoodItem removedItem = items.remove(itemNumber - 1);
        totalPrice -= removedItem.getPrice();
        return removedItem;
    }

    public void validateOrder() {
        if (items.isEmpty()) {
            throw new EmptyOrderException();
        }
    }

    public int getOrderId()           { return orderId; }
    public Customer getCustomer()     { return customer; }
    public double getTotalPrice()     { return totalPrice; }
    public List<FoodItem> getItems() { return Collections.unmodifiableList(items); }

    public void displayOrder() {
        System.out.println("\n========== Order Summary ==========");
        System.out.println("Order ID  : " + orderId);
        System.out.println("Customer  : " + customer.getName());
        System.out.println("Items ordered:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + items.get(i));
        }
        System.out.println("Total Price: " + totalPrice + " RWF");
        System.out.println("===================================");
    }
}
