public class DeliveryAgent extends User {
    private boolean isAvailable;

    public DeliveryAgent(int id, String name, String phone) {
        super(id, name, phone);
        this.isAvailable = true;
    }

    public boolean isAvailable() { return isAvailable; }

    public void deliverOrder(Order order) {
        if (!isAvailable) {
            throw new AgentUnavailableException(getName());
        }
        isAvailable = false;
        System.out.println(getName() + " is delivering order #" + order.getOrderId()
                + " to " + order.getCustomer().getName());
    }

    @Override
    public void displayDetails() {
        System.out.println("Delivery Agent : " + getName()
                + " | Phone: " + getPhone()
                + " | Available: " + (isAvailable ? "Yes" : "No"));
    }
}
