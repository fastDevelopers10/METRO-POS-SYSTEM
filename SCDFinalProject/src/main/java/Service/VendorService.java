package Service;

import Model.Vendor;

import java.util.ArrayList;
import java.util.List;

public class VendorService {
    private final List<Vendor> vendors;

    public VendorService() {
        this.vendors = new ArrayList<>();
    }

    public List<Vendor> getAllVendors() {
        return vendors;
    }

    public void addVendor(Vendor vendor) {
        vendors.add(vendor);
    }

    public void updateVendor(int vendorId, Vendor updatedVendor) {
        for (int i = 0; i < vendors.size(); i++) {
            if (vendors.get(i).getVendorId() == vendorId) {
                vendors.set(i, updatedVendor);
                return;
            }
        }
    }

    public void deleteVendor(int vendorId) {
        vendors.removeIf(vendor -> vendor.getVendorId() == vendorId);
    }
}
