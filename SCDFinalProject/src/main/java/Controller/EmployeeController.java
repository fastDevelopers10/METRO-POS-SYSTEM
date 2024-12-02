package Controller;

import Model.Employee;
import Service.EmployeeService;

import java.util.List;

public class EmployeeController {
    private  EmployeeService employeeService;

    public  EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    public List<Employee> getEmployeesByBranch(int branchId) {
        return employeeService.getEmployeesByBranch(branchId);
    }
}
