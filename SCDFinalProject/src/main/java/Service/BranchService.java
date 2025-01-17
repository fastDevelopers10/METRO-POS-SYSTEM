package Service;

import DAO.BranchDAO;

import java.util.List;
import Model.Branch;


public class BranchService {

    private final BranchDAO branchDAO;

    public BranchService() {
        this.branchDAO = new BranchDAO();
    }

    public List<Branch> getAllBranches() {
        return branchDAO.getAllBranches();
    }

    public boolean addBranch(Branch branch) {
        return branchDAO.addBranch(branch);
    }

    public boolean updateBranch(Branch branch) {
        return branchDAO.updateBranch(branch);
    }
    public List<Branch> getActiveBranches() {
        return branchDAO.fetchBranchesByStatus("Active");
    }

    public List<Branch> getClosedBranches() {
        return branchDAO.fetchBranchesByStatus("Closed");
    }

    public List<Integer> getAllBranchIds()
    {
        return branchDAO.getAllBranchIds();
    }
}