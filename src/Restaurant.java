import java.util.HashMap;

public class Restaurant {
    private String name;
    private HashMap<Integer, String> menuItems;
    private HashMap<String, Double> prices;

    public Restaurant(String name) {
        this.name = name;
        menuItems = new HashMap<>();
        prices = new HashMap<>();

        // Menu setup
        menuItems.put(1, "Burger");
        menuItems.put(2, "Pizza");
        menuItems.put(3, "Rolex");

        prices.put("Burger", 5000.0);
        prices.put("Pizza", 8000.0);
        prices.put("Rolex", 2000.0);
    }

    public void displayMenu() {
        System.out.println("\n--- " + name + " Menu ---");
        for (int key : menuItems.keySet()) {
            String item = menuItems.get(key);
            System.out.println(key + ". " + item + " - " + prices.get(item) + " RWF");
        }
    }

    public String getItemByChoice(int choice) {
        return menuItems.get(choice);
    }

    public double getPrice(String item) {
        return prices.get(item);
    }

    public void prepareOrder() {
        System.out.println("\nRestaurant is preparing your order...");
    }
}