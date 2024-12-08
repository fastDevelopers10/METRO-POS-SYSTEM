package SCDFinalProject.src.main.java.Controller;

import SCDFinalProject.src.main.java.Service.EmployeeService;
import SCDFinalProject.src.main.java.View.BranchManagerTableUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EmpCtrlr implements ActionListener {
    private EmployeeService service;
    private BranchManagerTableUI ui;


    public EmpCtrlr(BranchManagerTableUI ui) throws SQLException {
        this.ui = ui;
        this.service = new EmployeeService();
    }

    public void populateTable() {

        service.populateBranchManagerTable(ui.getTableModel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(ui, "Button clicked!");
    }
}
