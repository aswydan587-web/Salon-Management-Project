// SalonApp.java
// البرنامج الرئيسي: إدارة الخدمات والعملاء والحجوزات
import java.util.Scanner;

public class SalonApp {

    // مصفوفات لتخزين السجلات (تعليمية وبسيطة)
    private static Service[] services = new Service[100];
    private static Customer[] customers = new Customer[200];
    private static Appointment[] appointments = new Appointment[500];

    private static int serviceCount = 0;
    private static int customerCount = 0;
    private static int appointmentCount = 0;

    private static int nextServiceId = 1;
    private static int nextCustomerId = 1;

    private static Scanner sc = new Scanner(System.in);

    // ======= التجريد/الوراثة/تعدد الأشكال (داخل نفس الملف للحفاظ على 3 كلاسات) =======
    // قاعدة تجريدية بسيطة تمثل سجل يمكن عرضه
    private static abstract class Record {
        public abstract String display(); // تجريد: كل نوع يسهل عرضه بطريقة مختلفة
    }

    // Appointment ترث من Record => مثال على الوراثة + override (تعدد الأشكال)
    private static class Appointment extends Record {
        private int id;
        private int customerId;
        private int serviceId;
        private String dateTime;
        private String notes;

        public Appointment(int id, int customerId, int serviceId, String dateTime, String notes) {
            this.id = id;
            this.customerId = customerId;
            this.serviceId = serviceId;
            this.dateTime = dateTime;
            this.notes = notes;
        }

        public int getId() { return id; }
        public int getCustomerId() { return customerId; }
        public int getServiceId() { return serviceId; }
        public String getDateTime() { return dateTime; }
        public String getNotes() { return notes; }

        @Override
        public String display() {
            String cust = findCustomerName(customerId);
            String serv = findServiceName(serviceId);
            return String.format("Appt[ID=%d | Customer=%s | Service=%s | at=%s | notes=%s]",
                    id, cust, serv, dateTime, notes == null ? "-" : notes);
        }
    }

    // ======= مساعدة بحث (search helpers) =======
    private static String findCustomerName(int cid) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getId() == cid) return customers[i].getName();
        }
        return "UnknownCustomer";
    }

    private static String findServiceName(int sid) {
        for (int i = 0; i < serviceCount; i++) {
            if (services[i].getId() == sid) return services[i].getName();
        }
        return "UnknownService";
    }

    // ======= main =======
    public static void main(String[] args) {
        seedData();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("اختر رقم: ");
            switch (choice) {
                case 1: addService(); break;
                case 2: listServices(); break;
                case 3: updateService(); break;
                case 4: removeService(); break;

                case 5: addCustomer(); break;
                case 6: listCustomers(); break;
                case 7: updateCustomer(); break;
                case 8: removeCustomer(); break;

                case 9: bookAppointment(); break;
                case 10: listAppointments(); break;
                case 11: cancelAppointment(); break;

                case 12: System.out.println("خروج. مع السلامة!"); running = false; break;
                default: System.out.println("خيار غير صحيح، حاول مرة أخرى."); break;
            }
        }
        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\n===== نظام إدارة الصالون =====");
        System.out.println("1. إضافة خدمة");
        System.out.println("2. عرض الخدمات");
        System.out.println("3. تعديل خدمة");
        System.out.println("4. حذف خدمة");

        System.out.println("5. إضافة عميل");
        System.out.println("6. عرض العملاء");
        System.out.println("7. تعديل عميل");
        System.out.println("8. حذف عميل");

        System.out.println("9. حجز موعد");
        System.out.println("10. عرض الحجوزات");
        System.out.println("11. إلغاء حجز");

        System.out.println("12. خروج");
    }

    // ======= أدوات قراءة مدخلات =======
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("أدخل رقمًا صحيحًا: ");
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("أدخل رقمًا: ");
        }
        double v = sc.nextDouble();
        sc.nextLine();
        return v;
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // ======= خدمات CRUD للخدمات (Service) =======
    private static void addService() {
        System.out.println("\n-- إضافة خدمة جديدة --");
        String name = readLine("اسم الخدمة: ");
        int dur = readInt("المدة (دقائق): ");
        double price = readDouble("السعر: ");
        Service s = new Service(nextServiceId++, name, dur, price);
        if (serviceCount < services.length) {
            services[serviceCount++] = s;
            System.out.println("تمت إضافة: " + s);
        } else {
            System.out.println("قائمة الخدمات ممتلئة.");
        }
    }

    private static void listServices() {
        System.out.println("\n-- قائمة الخدمات --");
        if (serviceCount == 0) { System.out.println("لا توجد خدمات."); return; }
        for (int i = 0; i < serviceCount; i++) {
            System.out.println(services[i].toString());
        }
    }

    private static void updateService() {
        System.out.println("\n-- تعديل خدمة --");
        listServices();
        int id = readInt("أدخل ID الخدمة للتعديل: ");
        int idx = findServiceIndexById(id);
        if (idx == -1) { System.out.println("لم أجد الخدمة."); return; }

        String newName = readLine("الاسم الجديد (اترك فارغًا للاحتفاظ): ");
        if (!newName.isEmpty()) services[idx].setName(newName);

        String durStr = readLine("المدة الجديدة (دقائق) (اترك فارغًا للاحتفاظ): ");
        if (!durStr.isEmpty()) {
            try { services[idx].setDuration(Integer.parseInt(durStr)); } catch (Exception e) { System.out.println("قيمة غير صحيحة."); }
        }

        String priceStr = readLine("السعر الجديد (اترك فارغًا للاحتفاظ): ");
        if (!priceStr.isEmpty()) {
            try { services[idx].setPrice(Double.parseDouble(priceStr)); } catch (Exception e) { System.out.println("قيمة غير صحيحة."); }
        }

        System.out.println("تم التحديث: " + services[idx]);
    }

    private static void removeService() {
        System.out.println("\n-- حذف خدمة --");
        listServices();
        int id = readInt("أدخل ID الخدمة للحذف: ");
        int idx = findServiceIndexById(id);
        if (idx == -1) { System.out.println("لم أجد الخدمة."); return; }
        // نحذف بتحريك العناصر لليسار
        for (int i = idx; i < serviceCount - 1; i++) services[i] = services[i + 1];
        services[--serviceCount] = null;
        System.out.println("تم حذف الخدمة.");
    }

    private static int findServiceIndexById(int id) {
        for (int i = 0; i < serviceCount; i++) if (services[i].getId() == id) return i;
        return -1;
    }

    // ======= CRUD للعملاء (Customer) =======
    private static void addCustomer() {
        System.out.println("\n-- إضافة عميل جديد --");
        String name = readLine("اسم العميل: ");
        String phone = readLine("هاتف العميل: ");
        Customer c = new Customer(nextCustomerId++, name, phone);
        if (customerCount < customers.length) {
            customers[customerCount++] = c;
            System.out.println("تمت الإضافة: " + c);
        } else {
            System.out.println("قائمة العملاء ممتلئة.");
        }
    }

    private static void listCustomers() {
        System.out.println("\n-- قائمة العملاء --");
        if (customerCount == 0) { System.out.println("لا يوجد عملاء."); return; }
        for (int i = 0; i < customerCount; i++) System.out.println(customers[i].toString());
    }

    private static void updateCustomer() {
        System.out.println("\n-- تعديل عميل --");
        listCustomers();
        int id = readInt("أدخل ID العميل للتعديل: ");
        int idx = findCustomerIndexById(id);
        if (idx == -1) { System.out.println("لم أجد العميل."); return; }

        String newName = readLine("الاسم الجديد (اترك فارغًا للاحتفاظ): ");
        if (!newName.isEmpty()) customers[idx].setName(newName);

        String newPhone = readLine("الهاتف الجديد (اترك فارغًا للاحتفاظ): ");
        if (!newPhone.isEmpty()) customers[idx].setPhone(newPhone);

        System.out.println("تم التحديث: " + customers[idx]);
    }

    private static void removeCustomer() {
        System.out.println("\n-- حذف عميل --");
        listCustomers();
        int id = readInt("أدخل ID العميل للحذف: ");
        int idx = findCustomerIndexById(id);
        if (idx == -1) { System.out.println("لم أجد العميل."); return; }
        for (int i = idx; i < customerCount - 1; i++) customers[i] = customers[i + 1];
        customers[--customerCount] = null;
        System.out.println("تم حذف العميل.");
    }

    private static int findCustomerIndexById(int id) {
        for (int i = 0; i < customerCount; i++) if (customers[i].getId() == id) return i;
        return -1;
    }

    // ======= الحجوزات =======
    private static void bookAppointment() {
        System.out.println("\n-- حجز موعد جديد --");
        if (customerCount == 0 || serviceCount == 0) {
            System.out.println("تأكد من وجود عملاء وخدمات أولاً.");
            return;
        }
        listCustomers();
        int cid = readInt("أدخل ID العميل: ");
        if (findCustomerIndexById(cid) == -1) { System.out.println("العميل غير موجود."); return; }

        listServices();
        int sid = readInt("أدخل ID الخدمة: ");
        if (findServiceIndexById(sid) == -1) { System.out.println("الخدمة غير موجودة."); return; }

        String dt = readLine("التاريخ والوقت (مثال: 2025-11-20 15:00): ");
        String notes = readLine("ملاحظات (اختياري): ");
        Appointment a = new Appointment(appointmentCount + 1, cid, sid, dt, notes);
        if (appointmentCount < appointments.length) {
            appointments[appointmentCount++] = a;
            System.out.println("تم الحجز: " + a.display());
        } else {
            System.out.println("قاعدة البيانات ممتلئة للحجوزات.");
        }
    }

    private static void listAppointments() {
        System.out.println("\n-- قائمة الحجوزات --");
        if (appointmentCount == 0) { System.out.println("لا توجد حجوزات."); return; }
        for (int i = 0; i < appointmentCount; i++) {
            // polymorphism: display() معرف في Record و overridden في Appointment
            System.out.println(appointments[i].display());
        }
    }

    private static void cancelAppointment() {
        System.out.println("\n-- إلغاء حجز --");
        listAppointments();
        int id = readInt("أدخل ID الحجز للإلغاء: ");
        int idx = -1;
        for (int i = 0; i < appointmentCount; i++) if (appointments[i].getId() == id) { idx = i; break; }
        if (idx == -1) { System.out.println("لم أجد الحجز."); return; }
        for (int i = idx; i < appointmentCount - 1; i++) appointments[i] = appointments[i + 1];
        appointments[--appointmentCount] = null;
        System.out.println("تم إلغاء الحجز.");
    }

    // ======= بيانات عيّنة عند البداية =======
    private static void seedData() {
        services[serviceCount++] = new Service(nextServiceId++, "قص شعر", 30, 8.0);
        services[serviceCount++] = new Service(nextServiceId++, "تصفيف", 45, 12.5);
        services[serviceCount++] = new Service(nextServiceId++, "تلوين", 90, 40.0);

        customers[customerCount++] = new Customer(nextCustomerId++, "أحمد سويدان", "777-000-111");
        customers[customerCount++] = new Customer(nextCustomerId++, "سارة علي", "777-111-222");
    }
}