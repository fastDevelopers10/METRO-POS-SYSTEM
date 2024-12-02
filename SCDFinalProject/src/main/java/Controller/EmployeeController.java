package Controller;

import Model.Employee;
import Service.EmployeeService;

import java.util.List;

public class EmployeeController {
    private  EmployeeService employeeService;

    public void EmployeeService() {
        this.employeeService = new EmployeeService();
    }

    public List<Employee> getEmployeesByBranch(int branchId) {
        return employeeService.getEmployeesByBranch(branchId);
    }
}
