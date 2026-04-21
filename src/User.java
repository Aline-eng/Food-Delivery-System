public abstract class User {
    private int id;
    private String name;
    private String phone;

    public User(int id, String name, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                "Invalid phone number: '" + phone + "'. Must be exactly 10 digits."
            );
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId()       { return id; }
    public String getName()  { return name; }
    public String getPhone() { return phone; }

    public abstract void displayDetails();
}
