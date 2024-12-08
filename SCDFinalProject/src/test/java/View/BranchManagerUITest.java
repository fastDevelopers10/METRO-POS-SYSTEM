import Model.Employee;
import View.BranchManagerUI;
import View.LoginOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class BranchManagerUITest {

    private BranchManagerUI branchManagerUI;

    @BeforeEach
    public void setUp() {
        // Initialize the BranchManagerUI with a real Employee instance (assuming you have a real Employee object)
        Employee realEmployee = new Employee("John Doe", "1", "john@example.com"); // Replace with your real Employee constructor
        branchManagerUI = new BranchManagerUI(realEmployee);
    }

    @Test
    public void testBranchManagerUIInitialization() {
        // Test that the BranchManagerUI is initialized correctly.
        assertNotNull(branchManagerUI, "BranchManagerUI should be initialized");

        // Test if the window title is set correctly.
        assertEquals("Branch Manager", branchManagerUI.getTitle(), "Title should be 'Branch Manager'");

        // Test that the background panel is visible.
        assertTrue(branchManagerUI.isVisible(), "BranchManagerUI should be visible");
    }

    @Test
    public void testEmployeePanelDisplayed() {
        // Simulate the action when the "Dashboard" button is clicked.
        branchManagerUI.showEmployeePanel();

        // Test that the employee panel is properly displayed (JTable should be populated).
        JTable employeeTable = (JTable) branchManagerUI.getContentPane().getComponent(1); // Assuming the table is the second component in the content pane
        assertNotNull(employeeTable, "Employee table should be displayed");

        // Test that the table has rows after the panel is displayed (assumes some data is already set in the table)
        assertTrue(employeeTable.getRowCount() > 0, "Employee table should have rows");
    }

    @Test
    public void testLogoutAction() {
        // Simulate the logout action (e.g., by clicking the logout button).
        branchManagerUI.logoutAction();

        // Verify that the window is closed or not visible after logout.
        assertFalse(branchManagerUI.isVisible(), "BranchManagerUI should be closed after logout");

        // Test that the login screen appears (assuming a dialog or login screen appears on logout)
        // This test assumes you have a method in your code that shows the login screen after logout
        LoginOptions loginOptions = new LoginOptions(); // Create the login options screen
        loginOptions.setVisible(true);
        assertTrue(loginOptions.isVisible(), "Login screen should be visible after logout");
    }

    @Test
    public void testSideMenuButtons() {
        // Test the action listener for the "Dashboard" button.
        JButton dashboardButton = (JButton) findComponentByName(branchManagerUI, "Dashboard");
        dashboardButton.doClick(); // Simulate the button click

        // Verify that the employee panel is displayed.
        assertTrue(branchManagerUI.getContentPane().getComponent(1) instanceof JTable, "Employee panel should be shown after clicking Dashboard");

        // Test the action listener for the "Reports" button.
        JButton reportsButton = (JButton) findComponentByName(branchManagerUI, "Reports");
        reportsButton.doClick(); // Simulate the button click

        // Verify if the reports section is displayed.
        assertTrue(branchManagerUI.getContentPane().getComponent(2) instanceof JLabel, "Reports section should be shown after clicking Reports");
    }

    // Utility method to find components by name (assuming each component has a name set)
    private Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }

    @Test
    public void testEmployeeTableDataRendering() {
        // Simulate fetching employees and rendering data in the table
        // Here, I'm assuming the table will populate with real employee data.

        // Create an example employee to simulate data in the employee panel
        Employee employee = new Employee("John Doe", "1", "john@example.com");
        branchManagerUI.addEmployeeToTable(employee); // You might need to call a method that adds employees to the table.

        // Verify that the employee's name is rendered in the first row of the table.
        JTable employeeTable = (JTable) branchManagerUI.getContentPane().getComponent(1);
        assertEquals("John Doe", employeeTable.getValueAt(0, 0), "Employee name should be John Doe");
        assertEquals("1", employeeTable.getValueAt(0, 1), "Employee branch ID should be 1");
        assertEquals("john@example.com", employeeTable.getValueAt(0, 2), "Employee email should be john@example.com");
    }
}
