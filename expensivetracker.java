import java.io.*;
import java.util.*;

class Expense implements Serializable {
    String category;
    double amount;
    String type; // INCOME or EXPENSE
    Date date;

    Expense(String category, double amount, String type) {
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = new Date();
    }
}

class User implements Serializable {
    String username;
    String password;
    ArrayList<Expense> expenses = new ArrayList<>();

    User(String u, String p) {
        username = u;
        password = p;
    }
}

public class ExpenseTracker {
    static Scanner sc = new Scanner(System.in);
    static HashMap<String, User> users = new HashMap<>();
    static final String FILE_NAME = "data.ser";

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int choice = sc.nextInt();

            if (choice == 1) register();
            else if (choice == 2) login();
            else break;
        }

        saveData();
        System.out.println("Thank you!");
    }

    static void register() {
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();

        if (users.containsKey(u)) {
            System.out.println("User already exists.");
        } else {
            users.put(u, new User(u, p));
            System.out.println("Registration successful.");
        }
    }

    static void login() {
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();

        User user = users.get(u);
        if (user != null && user.password.equals(p)) {
            userMenu(user);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    static void userMenu(User user) {
        while (true) {
            System.out.println("\n1. Add Income\n2. Add Expense\n3. View Summary\n4. Logout");
            int ch = sc.nextInt();

            if (ch == 1 || ch == 2) {
                System.out.print("Category: ");
                String cat = sc.next();
                System.out.print("Amount: ");
                double amt = sc.nextDouble();
                user.expenses.add(new Expense(cat, amt, ch == 1 ? "INCOME" : "EXPENSE"));
            }
            else if (ch == 3) {
                showSummary(user);
            }
            else break;
        }
    }

    static void showSummary(User user) {
        double income = 0, expense = 0;

        for (Expense e : user.expenses) {
            if (e.type.equals("INCOME")) income += e.amount;
            else expense += e.amount;
        }

        System.out.println("Total Income: " + income);
        System.out.println("Total Expense: " + expense);
        System.out.println("Balance: " + (income - expense));
    }

    static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }
}


