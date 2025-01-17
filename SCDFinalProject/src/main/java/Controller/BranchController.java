package Controller;

import Model.Branch;
import Service.BranchService;

import java.util.List;

public class BranchController {
    private final BranchService branchService;

    public BranchController() {
        this.branchService = new BranchService();
    }

    public List<Branch> fetchAllBranches() {
        return branchService.getAllBranches();
    }

    public boolean addBranch(Branch branch) {
        return branchService.addBranch(branch);
    }

    public boolean updateBranch(Branch branch) {
        return branchService.updateBranch(branch);
    }

    public List<Branch> fetchActiveBranches() {
        return branchService.getActiveBranches();
    }

    public List<Branch> fetchClosedBranches() {
        return branchService.getClosedBranches();
    }

    public List<Integer> getAllBranchIds() {
        return branchService.getAllBranchIds();
    }
}
