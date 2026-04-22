import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Map;

public class Restaurant {
    private final String name;
    // Menu choices are lookup-based, so Map connects a menu number to a FoodItem.
    private final Map<Integer, FoodItem> menu;

    public Restaurant(String name) {
        this.name = name;
        menu = new LinkedHashMap<>();
        addMenuItem(1, new FoodItem("Burger", 5000.0));
        addMenuItem(2, new FoodItem("Pizza",  8000.0));
        addMenuItem(3, new FoodItem("Rolex",  2000.0));
    }

    public void addMenuItem(int choice, FoodItem item) {
        menu.put(choice, item);
    }

    public FoodItem removeMenuItem(int choice) {
        return menu.remove(choice);
    }

    public Map<Integer, FoodItem> getMenu() {
        return Collections.unmodifiableMap(menu);
    }

    public String getName() {
        return name;
    }

    public void displayMenu() {
        System.out.println("\n--- " + name + " Menu ---");
        for (Map.Entry<Integer, FoodItem> entry : menu.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    public FoodItem getItemByChoice(int choice) {
        FoodItem item = menu.get(choice);
        if (item == null) {
            throw new IllegalArgumentException(
                "Item #" + choice + " is not on the menu. Please choose 1, 2, or 3."
            );
        }
        return item;
    }

    public void prepareOrder() {
        System.out.println("\n" + name + " is preparing your order...");
    }

    public void prepareOrder(String customerName) {
        System.out.println("\n" + name + " is preparing the order for " + customerName + "...");
    }
}
