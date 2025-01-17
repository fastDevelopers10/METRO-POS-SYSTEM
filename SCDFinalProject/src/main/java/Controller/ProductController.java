package Controller;

import DAO.ProductDAO;
import Model.Product;
import Service.ProductService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductController {
    private final ProductService productService;


    public ProductController() {
        this.productService = new ProductService();
    }

    public boolean isInternetAvailable() {
        return productService.isInternetAvailable();
    }
    public List<String> getVendors() {
        return productService.getVendors();
    }

    public List<String> getProductsByBranch(int branchId) {
        return productService.getProductsByBranch(branchId);
    }

    Date purchaseDate = new java.sql.Date(System.currentTimeMillis());
    public List<Product> fetchProductsByBranchForTable(int branchId, String searchQuery) {
        return productService.fetchProductsByBranchForTable(branchId, searchQuery);
    }

    public List<Product> fetchProductsByBranchForTable(int branchId) {
        return productService.fetchProductsByBranchForTable(branchId);
    }

    public boolean deleteProduct(int productId) {
        return productService.deleteProduct(productId);
    }


    public boolean addOrUpdateProductAction(int branchId, String vendorName, String productName, String category,
                                            int cartons, int itemsPerCarton, double originalPrice, double salesPrice,
                                            Date purchaseDate) {
        int vendorId = productService.getVendorIdByName(vendorName);
        int productId = productService.getProductIdByName(productName);

        return productService.addOrUpdateProduct(branchId, vendorId, productId, productName.trim(), category.trim(), cartons, itemsPerCarton,
                originalPrice, salesPrice, purchaseDate);

    }
    public List<String> getCategories(int branchId) {
        return productService.getCategoriesByBranch(branchId);
    }
    public int getProductCountByBranch(int branchId) {
        return productService.getProductCountByBranch(branchId);
    }

    public int getVendorCountByBranch() {
        return productService.getVendorCountByBranch();
    }


    public List<Map<String, Object>> fetchVendorProductsByBranchforvendor(int branchId) {
        return productService.fetchVendorProductsByBranchforvendors(branchId);
    }
    public int getStockByBranch(int branchId) {
        return productService.getStockByBranch(branchId);
    }
    public List<Object[]> getProductIdAndQuantities() {
        return productService.getProductIdAndQuantities();
    }
    public int getProductRowCount() throws SQLException {
        return productService.getProductRowCount();
    }

    public int getTotalProductsSum() throws SQLException {
        return productService.getTotalProductsSum();
    }
    public Map<String,  Integer> getCategorySales(int branchId) {
        return productService.getCategorySales(branchId);
    }
}
