package Controller;

public class Vendor {
    // Attributes
    private int vendorId;          // Auto-generated SCDFinalProject.src.main.java.Controller.Vendor ID
    private String vendorName;
    private String address;
    private String phoneNumber;
    private String productId;      // Product brought from vendor (using ID)
    private double amountPaid;
    private double amountDue;
    private String month;

    // Static counter to auto-generate SCDFinalProject.src.main.java.Controller.Vendor ID
    private static int vendorCount = 0;

    // Constructor
    public Vendor(String vendorName, String address, String phoneNumber, String productId, double amountPaid, double amountDue, String month) {
        this.vendorId = ++vendorCount;       // Auto-generate SCDFinalProject.src.main.java.Controller.Vendor ID
        this.vendorName = vendorName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.productId = productId;
        this.amountPaid = amountPaid;
        this.amountDue = amountDue;
        this.month = month;
    }

    // Getters and Setters
    public int getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    // Method to display SCDFinalProject.src.main.java.Controller.Vendor information
    public void displayInfo() {
        System.out.println("SCDFinalProject.src.main.java.Controller.Vendor ID: " + vendorId);
        System.out.println("SCDFinalProject.src.main.java.Controller.Vendor Name: " + vendorName);
        System.out.println("Address: " + address);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Product ID: " + productId);
        System.out.println("Amount Paid: " + amountPaid);
        System.out.println("Amount Due: " + amountDue);
        System.out.println("Month: " + month);
    }
}