public class Customer extends User {

    private static int nextId = 1;

    public Customer(String name, String phone) {
        super(nextId++, name, phone);
    }

    public void placeOrder(Order order) {
        System.out.println(getName() + " placed order #" + order.getOrderId());
    }

    @Override
    public void displayDetails() {
        System.out.println("Customer [ID: " + getId() + "] " + getName() + " | Phone: " + getPhone());
    }
}
