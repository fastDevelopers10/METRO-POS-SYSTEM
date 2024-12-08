package DAO;

import Model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDAO = new ProductDAO();
    }

    @Test
    void updateStock() throws SQLException {
        String productName = "Black Chocolate";
        int quantity = 10;
        int branchId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        boolean result = productDAO.updateStock(productName, quantity, branchId);
        assertTrue(result);
        verify(mockStatement).setInt(1, quantity);
        verify(mockStatement).setString(2, productName);
        verify(mockStatement).setInt(3, branchId);
        verify(mockStatement).setInt(4, quantity);
    }

    @Test
    void addProduct() throws SQLException {
        Product product = new Product(1, null, "Product1", "Category1",
                new BigDecimal("100.00"), new BigDecimal("120.00"), 50, true);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        productDAO.addProduct(product, 1);
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).setString(2, "Product1");
        verify(mockStatement).setString(3, "Category1");
        verify(mockStatement).setBigDecimal(4, new BigDecimal("100.00"));
        verify(mockStatement).setBigDecimal(5, new BigDecimal("120.00"));
        verify(mockStatement).setInt(6, 50);
        verify(mockStatement).setBoolean(7, true);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void getUniqueCategories() throws SQLException {
        int branchId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("product_category")).thenReturn("Cakes", "Chocolates","Category1");

        List<String> categories = productDAO.getUniqueCategories(branchId);

        assertEquals(3, categories.size());
        assertTrue(categories.contains("Cakes"));
        assertTrue(categories.contains("Chocolates"));
        assertTrue(categories.contains("Category1"));
    }

    @Test
    void getProductsByCategory() throws SQLException {
        // Arrange
        String category = "Category1";
        int branchId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("product_id")).thenReturn(1);
        when(mockResultSet.getString("product_name")).thenReturn("Product1");
        when(mockResultSet.getBigDecimal("original_price")).thenReturn(new BigDecimal("100.00"));
        when(mockResultSet.getBigDecimal("sales_price")).thenReturn(new BigDecimal("120.00"));
        when(mockResultSet.getInt("total_products")).thenReturn(50);
        when(mockResultSet.getBoolean("status")).thenReturn(true);

        List<Product> products = productDAO.getProductsByCategory(category, branchId);

        assertEquals(1, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    void getProductPriceByName() throws SQLException {
        // Arrange
        String productName = "Product1";
        int branchId = 1;
        BigDecimal expectedPrice = new BigDecimal("120.00");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getBigDecimal("sales_price")).thenReturn(expectedPrice);

        BigDecimal actualPrice = productDAO.getProductPriceByName(productName, branchId);
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void fetchVendors() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("name")).thenReturn("Electronics", "Bake House","Crockery", "Glass Suppliers","Frutico");

        List<String> vendors = productDAO.fetchVendors();

        assertEquals(5, vendors.size());
        assertTrue(vendors.contains("Electronics"));
        assertTrue(vendors.contains("Bake House"));
        assertTrue(vendors.contains("Crockery"));
        assertTrue(vendors.contains("Glass Suppliers"));
        assertTrue(vendors.contains("Frutico"));
    }

    @Test
    void deleteProductById() throws SQLException {
        int productId = 33;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        boolean result = productDAO.deleteProductById(productId);
        assertTrue(result);
        verify(mockStatement).setInt(1, productId);
        verify(mockStatement).executeUpdate();
    }

}
