package Model;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {
    private String employeeId;
    private String username;  // Added username field
    private String email;
    private String password;  // Password for login functionality
    private int branchCode;  // Added branchCode field
    private String address;  // Added address field
    private BigDecimal salary;  // Added salary field (using BigDecimal for precision)
    private String phone;  // Added phone number field
    private String status;  // Added status field (active, inactive, etc.)
    private Date joiningDate;  // Added joining date field
    private String employeeType;  // Added employee type (Cashier, Manager, etc.)

    // Constructor to initialize all fields
    public Employee(String employeeId, String username, String email, String password, int branchCode, String address,
                    BigDecimal salary, String phone, String status, Date joiningDate, String employeeType) {
        this.employeeId = employeeId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.branchCode = branchCode;
        this.address = address;
        this.salary = salary;
        this.phone = phone;
        this.status = status;
        this.joiningDate = joiningDate;
        this.employeeType = employeeType;
    }

    // Getters and setters for all fields
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    // Method to validate login (email and password check)
    public boolean validateLogin(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Method to display employee details
    public void displayEmployeeInfo() {
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Branch Code: " + branchCode);
        System.out.println("Address: " + address);
        System.out.println("Salary: " + salary);
        System.out.println("Phone: " + phone);
        System.out.println("Status: " + status);
        System.out.println("Joining Date: " + joiningDate);
        System.out.println("Employee Type: " + employeeType);
    }
}
