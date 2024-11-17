package SCDFinalProject.src.main.java.Model;

public class Employee {
    private String employeeId;
    private String name;
    private String role;
    private String email;
    private String password; // Password for login functionality

    // Constructor
    public Employee(String employeeId, String name, String role, String email, String password) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter for employeeId
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Method to validate login (email and password check)
    public boolean validateLogin(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Method to display employee details
    public void displayEmployeeInfo() {
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Employee Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Email: " + email);
    }
}
