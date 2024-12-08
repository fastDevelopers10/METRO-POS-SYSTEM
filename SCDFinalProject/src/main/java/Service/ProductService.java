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
    // Method to check if internet is available (by checking database connectivity)
    public boolean isInternetAvailable() {
        return productDAO.isInternetAvailable();
    }

    public List<String> getCategoriesByBranch(int branchId) {
        return productDAO.getUniqueCategories(branchId);

    }

    public int getVendorIdByName(String vendorName) {
        List<String> vendors = productDAO.fetchVendors();
        for (String vendor : vendors) {
            if (vendor.equalsIgnoreCase(vendorName)) {

                return vendors.indexOf(vendor) + 1;
            }
        }
        return -1;
    }

    public int getProductIdByName(String productName) {
        List<String> products = productDAO.fetchProductsByBranch(0);
        for (String product : products) {
            if (product.equalsIgnoreCase(productName)) {

                return products.indexOf(product) + 1;
            }
        }
        return -1;
    }

    public List<Product> fetchProductsByBranchForTable(int branchId, String searchQuery) {
        return productDAO.fetchProductsByBranchForTable(branchId, searchQuery);
    }


    public List<Product> fetchProductsByBranchForTable(int branchId) {
        return productDAO.fetchProductsByBranchForTable(branchId, "");
    }
    public boolean deleteProduct(int productId ) {
        return productDAO.deleteProductById(productId);
    }
    public int getProductCountByBranch(int branchId) {
        return productDAO.getProductCountByBranch(branchId);
    }

    public int getVendorCountByBranch() {
        return productDAO.getVendorCount();
    }

}
