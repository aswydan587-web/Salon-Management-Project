// Service.java
// نموذج يمثل خدمة في الصالون (id, name, duration, price)
// تغليف (encapsulation) + طريقة toString
public class Service {
    private int id;
    private String name;
    private int duration; // minutes
    private double price;

    public Service(int id, String name, int duration, double price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    // getters & setters (encapsulation)
    public int getId() { return id; }
    public String getName() { return name; }
    public int getDuration() { return duration; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return String.format("Service[ID=%d, Name=%s, Duration=%d min, Price=%.2f]",
                id, name, duration, price);
    }
}