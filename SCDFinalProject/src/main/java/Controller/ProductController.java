package Controller;

import DAO.ProductDAO;
import Model.Product;
import Service.ProductService;

import java.util.Date;
import java.util.List;

public class ProductController {
    private final ProductService productService;


    public ProductController() {
        this.productService = new ProductService();
    }

    public List<Product> fetchProductsByBranch(int branchId, String searchQuery) {
        return productService.getProductsByBranch(branchId, searchQuery);
    }

    // Fetch list of vendors (vendor ID and name as ComboItem objects)
    public List<String> getVendors() {
        return productService.getVendors();
    }

    // Fetch list of products for a given branch (product ID, name, and category as ComboItem objects)
    public List<String> getProductsByBranch(int branchId) {
        return productService.getProductsByBranch(branchId);
    }

    Date purchaseDate = new java.sql.Date(System.currentTimeMillis());  // Current date
    // Fetch products by branch with a search query
    public List<Product> fetchProductsByBranchForTable(int branchId, String searchQuery) {
        return productService.fetchProductsByBranchForTable(branchId, searchQuery);
    }

    // Fetch all products by branch (no search query)
    public List<Product> fetchProductsByBranchForTable(int branchId) {
        return productService.fetchProductsByBranchForTable(branchId);
    }

    public boolean deleteProduct(int productId) {
        return productService.deleteProduct(productId);
    }


    public boolean addOrUpdateProductAction(int branchId, String vendorName, String productName, String category,
                                             int cartons, int itemsPerCarton, double originalPrice, double salesPrice,
                                             Date purchaseDate) {
        // Fetch vendorId and productId
        int vendorId = productService.getVendorIdByName(vendorName);
        int productId = productService.getProductIdByName(productName);

        // Delegate to the service layer
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


//    public static void main(String[] args) {
//        ProductController ctr=new ProductController();
//        System.out.println(ctr.getCategories(1));
//    }

}
