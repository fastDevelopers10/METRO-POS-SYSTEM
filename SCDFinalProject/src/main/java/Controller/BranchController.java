package Controller;

import Service.BranchService;

import java.util.List;

public class BranchController {
    BranchService bs;

    public BranchController() {
        this.bs = new BranchService();
    }

    public List<Integer> getAllBranchIds() {
        return bs.getAllBranchIds();
    }
}
