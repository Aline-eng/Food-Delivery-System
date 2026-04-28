public class DeliveryAgent extends User {

    private static int nextId = 1;
    private boolean isAvailable;

    public DeliveryAgent(String name, String phone) {
        super(nextId++, name, phone);
        this.isAvailable = true;
    }

    public boolean isAvailable() { return isAvailable; }

    public void deliverOrder(Order order) {
        if (!isAvailable) {
            throw new AgentUnavailableException(getName());
        }
        isAvailable = false;
        System.out.println(getName() + " is delivering order #" + order.getOrderId()
                + " to " + order.getCustomer().getName() + "...");
        isAvailable = true;
        System.out.println(getName() + " completed the delivery and is now available again.");
    }

    @Override
    public void displayDetails() {
        System.out.println("Agent     [ID: " + getId() + "] " + getName()
                + " | Phone: " + getPhone()
                + " | Available: " + (isAvailable ? "Yes" : "No"));
    }
}
