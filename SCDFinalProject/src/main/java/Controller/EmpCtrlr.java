package SCDFinalProject.src.main.java.Controller;

import SCDFinalProject.src.main.java.Service.EmployeeDataService;
import SCDFinalProject.src.main.java.View.BranchManagerTableUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EmpCtrlr implements ActionListener {
    private BranchManagerTableUI ui;
    private EmployeeDataService service;

    public EmpCtrlr(BranchManagerTableUI ui) throws SQLException {
        this.ui = ui;
        this.service = new EmployeeDataService();
    }

    public void populateTable() {

        service.populateEmployeeTable(ui.getTableModel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(ui, "Button clicked!");
    }
}
