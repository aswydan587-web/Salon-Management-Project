// Customer.java
// نموذج يمثل عميل: id, name, phone
// يحتوي على تقنيات بسيطة (constructor, getters/setters, toString)
public class Customer {
    private int id;
    private String name;
    private String phone;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // getters & setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("Customer[ID=%d, Name=%s, Phone=%s]", id, name, phone);
    }
}