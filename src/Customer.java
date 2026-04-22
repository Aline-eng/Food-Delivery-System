public class Customer extends User {

    public Customer(int id, String name, String phone) {
        super(id, name, phone);
    }

    public void placeOrder(Order order) {
        System.out.println(getName() + " placed order #" + order.getOrderId());
    }

    @Override
    public void displayDetails() {
        System.out.println("Customer       : " + getName() + " | Phone: " + getPhone());
    }
}
