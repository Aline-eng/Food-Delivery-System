import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {
    // A customer can place many orders over time, so List models this one-to-many relationship.
    private final List<Order> orderHistory;

    public Customer(int id, String name, String phone) {
        super(id, name, phone);
        this.orderHistory = new ArrayList<>();
    }

    public void placeOrder(Order order) {
        orderHistory.add(order);
        System.out.println(getName() + " placed order #" + order.getOrderId());
    }

    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    public Order getMostRecentOrder() {
        if (orderHistory.isEmpty()) {
            return null;
        }
        return orderHistory.get(orderHistory.size() - 1);
    }

    public boolean cancelOrder(int orderId) {
        return orderHistory.removeIf(order -> order.getOrderId() == orderId);
    }

    @Override
    public void displayDetails() {
        System.out.println("Customer       : " + getName()
                + " | Phone: " + getPhone()
                + " | Orders: " + orderHistory.size());
    }
}
