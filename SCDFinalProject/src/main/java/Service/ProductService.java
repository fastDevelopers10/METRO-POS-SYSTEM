package Service;

import DAO.ProductDAO;
import Model.Product;

import java.util.Date;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getProductsByBranch(int branchId, String searchQuery) {
        return productDAO.getProductsByBranchWithSearch(branchId, searchQuery);
    }
    public List<String> getVendors() {
        return productDAO.fetchVendors();
    }
    public List<String> getProductsByBranch(int branchId) {
        return productDAO.fetchProductsByBranch(branchId);
    }

    public boolean addOrUpdateProduct(int branchId, int vendorId, int productId, String name, String category,
                                      int cartons, int itemsPerCarton, double originalPrice, double salesPrice,
                                      Date purchaseDate) {
        return productDAO.addOrUpdateProduct(branchId, vendorId, productId, name, category, cartons, itemsPerCarton, originalPrice, salesPrice, purchaseDate);
    }

    public List<String> getCategoriesByBranch(int branchId) {
        System.out.println(productDAO.getUniqueCategories(branchId));
        return productDAO.getUniqueCategories(branchId);  // Call the DAO method to fetch categories

    }
    // New method to get Vendor ID by Vendor Name
    public int getVendorIdByName(String vendorName) {
        List<String> vendors = productDAO.fetchVendors();
        for (String vendor : vendors) {
            if (vendor.equalsIgnoreCase(vendorName)) {
                // Assuming the vendor name is unique and matches one of the list entries
                // In this case, let's return the index (or you can modify it to return a proper ID from your DB)
                return vendors.indexOf(vendor) + 1;  // Assuming IDs start from 1
            }
        }
        return -1;  // Return -1 if vendor not found
    }

    // New method to get Product ID by Product Name
    public int getProductIdByName(String productName) {
        List<String> products = productDAO.fetchProductsByBranch(0); // Fetch all products (or you can specify a branch ID)
        for (String product : products) {
            if (product.equalsIgnoreCase(productName)) {
                // Assuming product names are unique, return index (or modify to return ID from DB)
                return products.indexOf(product) + 1;  // Assuming IDs start from 1
            }
        }
        return -1;  // Return -1 if product not found
    }

}
