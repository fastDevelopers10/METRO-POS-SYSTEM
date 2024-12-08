package Controller;

import DAO.VendorDAO;
import Model.Vendor;



import java.util.List;

public class VendorController {

    private VendorDAO vendorDAO;

    public VendorController() {
        vendorDAO = new VendorDAO();
    }

    // Fetch all vendors from the database
    public List<Vendor> fetchAllVendors() {
        return vendorDAO.getAllVendors();
    }

    // Update a vendor's information in the database
    public boolean modifyVendor(Vendor vendor) {
        return vendorDAO.updateVendor(vendor);
    }
    public boolean addVendor(Vendor vendor) {
        return vendorDAO.addVendor(vendor);
    }

}

