package Service;

import DAO.BranchDAO;

import java.util.List;

public class BranchService {
    BranchDAO bd;

    public BranchService() {
        this.bd = new BranchDAO();
    }
    public List<Integer> getAllBranchIds()
    {
        return bd.getAllBranchIds();
    }
}
