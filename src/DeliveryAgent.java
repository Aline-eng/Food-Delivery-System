import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class DeliveryAgent extends User {
    private boolean isAvailable;
    // A delivery agent should not store duplicate completed order IDs, so Set keeps them unique.
    private final Set<Integer> deliveredOrderIds;

    public DeliveryAgent(int id, String name, String phone) {
        super(id, name, phone);
        this.isAvailable = true;
        this.deliveredOrderIds = new LinkedHashSet<>();
    }

    public boolean isAvailable() { return isAvailable; }

    public void deliverOrder(Order order) {
        if (!isAvailable) {
            throw new AgentUnavailableException(getName());
        }
        isAvailable = false;
        deliveredOrderIds.add(order.getOrderId());
        System.out.println(getName() + " is delivering order #" + order.getOrderId()
                + " to " + order.getCustomer().getName());
    }

    public Set<Integer> getDeliveredOrderIds() {
        return Collections.unmodifiableSet(deliveredOrderIds);
    }

    public boolean hasDeliveredOrder(int orderId) {
        return deliveredOrderIds.contains(orderId);
    }

    public boolean removeDeliveredOrderRecord(int orderId) {
        return deliveredOrderIds.remove(orderId);
    }

    @Override
    public void displayDetails() {
        System.out.println("Delivery Agent : " + getName()
                + " | Phone: " + getPhone()
                + " | Available: " + (isAvailable ? "Yes" : "No")
                + " | Delivered Orders: " + deliveredOrderIds.size());
    }
}
