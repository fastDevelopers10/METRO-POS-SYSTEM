package SCDFinalProject.src.main.java.Controller;

public class DataOperator {
    private String name;
    private int employeeNumber;
    private String email;
    private String password;
    private String branchCode;
    private double salary;

    // Static counter to keep track of total employees and assign employee numbers
    private static int employeeCount = 0;

    public DataOperator(String name, String email, String branchCode, double salary) {
        this.name = name;
        this.employeeNumber = ++employeeCount;  // Increment counter and assign as Employee Number
        this.email = email;
        this.password = "123";                  // Default password
        this.branchCode = branchCode;
        this.salary = salary;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Method to display data operator information
    public void displayInfo() {
        System.out.println("Employee Name: " + name);
        System.out.println("Employee Number: " + employeeNumber);
        System.out.println("Email: " + email);
        System.out.println("Branch Code: " + branchCode);
        System.out.println("Salary: " + salary);
    }
}