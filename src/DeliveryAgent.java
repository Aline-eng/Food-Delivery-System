public class DeliveryAgent extends User{
    public DeliveryAgent(int id, String name, String phone) {
        super(id, name, phone);
    }

    public void deliverOrder(Order order) {
        System.out.println(getName() + " is delivering order " + order.getOrderId());
    }

    @Override
    public void displayDetails() {
        System.out.println("Delivery Agent: " + getName());
    }

}
