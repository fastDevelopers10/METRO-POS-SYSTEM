package SCDFinalProject.src.main.java.Controller;

import java.time.LocalDate;
import java.util.List;

public class BranchManager {
    private static int idCounter = 1000;
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String status;
    private double salary;
    private int branchId;
    private String username;
    private String password;
    private LocalDate joiningDate;

    // Constructor without setting password or joining date
    private BranchManager( String name, String address, String phoneNumber, String email, String status, double salary, int branchId) {

        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.salary = salary;
        this.branchId = branchId;
        this.username = null;  // Set later in addBranchManager
        this.password = null;
        this.joiningDate = null;
    }

    public static BranchManager addBranchManager(List<BranchManager> branchManagers, String name, String address, String phoneNumber, String email, String status, double salary, int branchId) {
        BranchManager manager = new BranchManager( name, address, phoneNumber, email, status, salary, branchId);
        manager.username = manager.name.toLowerCase() + manager.id;  // Set username as name + id
        manager.password = "123";  // Default password
        manager.joiningDate = LocalDate.now();  // Set joining date to today
        manager.id = idCounter++;
        branchManagers.add(manager);
        return manager;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public double getSalary() {
        return salary;
    }

    public int getBranchId() {
        return branchId;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void displayInfo() {
        System.out.println("Branch Manager Info:");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Status: " + status);
        System.out.println("Salary: " + salary);
        System.out.println("Branch ID: " + branchId);
        System.out.println("Joining Date: " + joiningDate);
    }
}

