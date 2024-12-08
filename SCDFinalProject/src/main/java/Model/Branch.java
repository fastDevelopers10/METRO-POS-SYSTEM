package Model;

public class Branch {
    private int branchId;
    private String city;
    private String name;
    private String status;
    private String address;
    private String phone;
    private int numberOfEmployees;

    // Constructor
    public Branch(int branchId, String city, String name, String status, String address, String phone, int numberOfEmployees) {
        this.branchId = branchId;
        this.city = city;
        this.name = name;
        this.status = status;
        this.address = address;
        this.phone = phone;
        this.numberOfEmployees = numberOfEmployees;
    }

    public Branch() {

    }

    // Getters and Setters
    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    // toString() Method for debugging and logging
    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", numberOfEmployees=" + numberOfEmployees +
                '}';
    }
}
