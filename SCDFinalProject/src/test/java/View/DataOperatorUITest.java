//package View;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//import javax.swing.*;
//
//import Controller.ProductController;
//import Model.Employee;
//import View.DataOperatorUI;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//public class DataOperatorUITest {
//
//    private ByteArrayOutputStream outContent;
//    private PrintStream originalOut;
//
//    @Before
//    public void setUpStreams() {
//        outContent = new ByteArrayOutputStream();
//        originalOut = System.out;
//        System.setOut(new PrintStream(outContent));
//    }
//
//    @After
//    public void restoreStreams() {
//        System.setOut(originalOut);
//    }
//
//    @Test
//    public void testConstructorInitialization() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123);
//        ProductController mockController = mock(ProductController.class);
//        when(mockController.getProductCountByBranch(123)).thenReturn(50);
//        when(mockController.getVendorCountByBranch()).thenReturn(20);
//
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        assertEquals("Name: Alina1", ((JLabel) ui.getContentPane().getComponent(0)).getText());
//        assertEquals("Position: Data Operator", ((JLabel) ui.getContentPane().getComponent(1)).getText());
//        assertEquals(" # 123456", ((JLabel) ui.getContentPane().getComponent(2)).getText());
//        assertEquals("50", ui.TotalProducts.getText());
//        assertEquals("20", ui.TotalVendors.getText());
//    }
//
//    @Test
//    public void testDashboardButtonAction() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton dashboardButton = (JButton) ui.sideMenuPanel.getComponent(0);
//        dashboardButton.doClick();
//
//        String output = outContent.toString().trim();
//        assertTrue(output.contains("Dashboard clicked"));
//    }
//
//    @Test
//    public void testChangePasswordButtonAction() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton changePasswordButton = (JButton) ui.sideMenuPanel.getComponent(1);
//        changePasswordButton.doClick();
//
//        String output = outContent.toString().trim();
//        assertTrue(output.contains("Change Password clicked"));
//    }
//
//    @Test
//    public void testUpdateVendorLabel() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        ProductController mockController = mock(ProductController.class);
//        when(mockController.getVendorCountByBranch()).thenReturn(25);
//
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        ui.updateLabelVendor();
//
//        assertEquals("25", ui.TotalVendors.getText());
//    }
//
//    @Test
//    public void testUpdateProductLabel() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        ProductController mockController = mock(ProductController.class);
//        when(mockController.getProductCountByBranch(101)).thenReturn(75);
//
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        ui.updateLabelProduct();
//
//        assertEquals("75", ui.TotalProducts.getText());
//    }
//
//    @Test
//    public void testResetActiveButtonToDashboard() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton otherButton = (JButton) ui.sideMenuPanel.getComponent(2);
//        otherButton.doClick();
//
//        ui.resetActiveButtonToDashboard();
//
//        JButton dashboardButton = (JButton) ui.sideMenuPanel.getComponent(0);
//        assertTrue(dashboardButton.isEnabled());
//    }
//
//    @Test
//    public void testVendorViewButton() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton vendorButton = findButtonByText(ui, "View", "Vendors");
//        assertNotNull(vendorButton);
//
//        vendorButton.doClick();
//
//        JDialog[] dialogs = findVisibleDialogs();
//        assertEquals(1, dialogs.length);
//
//        JDialog vendorDialog = dialogs[0];
//        assertEquals("Vendors", vendorDialog.getTitle());
//        assertTrue(vendorDialog.isVisible());
//
//        vendorDialog.dispose();
//    }
//
//    @Test
//    public void testCategoryViewButton() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton categoryButton = findButtonByText(ui, "view", "Categories");
//        assertNotNull(categoryButton);
//
//        categoryButton.doClick();
//
//        JDialog[] dialogs = findVisibleDialogs();
//        assertEquals(1, dialogs.length);
//
//        JDialog categoryDialog = dialogs[0];
//        assertEquals("Categories", categoryDialog.getTitle());
//        assertTrue(categoryDialog.isVisible());
//
//        categoryDialog.dispose();
//    }
//
//    @Test
//    public void testProductViewButton() {
//        Employee mockEmployee = new Employee("Alina1", "Data Operator", 123456);
//        DataOperatorUI ui = new DataOperatorUI(mockEmployee);
//
//        JButton productButton = findButtonByText(ui, "View", "Products");
//        assertNotNull(productButton);
//
//        productButton.doClick();
//
//        JFrame[] frames = findVisibleFrames();
//        assertEquals(1, frames.length);
//
//        JFrame productFrame = frames[0];
//        assertEquals("Product Table101", productFrame.getTitle());
//        assertTrue(productFrame.isVisible());
//
//        productFrame.dispose();
//    }
//
//}
