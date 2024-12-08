package Model;


public class Vendor {

    private int vendorId;
    private String name;
    private String phone;
    private boolean status;

    public Vendor(int vendorId, String name, String phone, boolean status) {
        this.vendorId = vendorId;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
