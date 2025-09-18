package splitwise;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Expense {
    private Double totalAmount;
    private String description;
    private List<Split> splits;

    public Expense(Double amount, String description, HashMap<String, Double> payerToAmountMap, List<String> participants, ISplitStrategy splitStrategy)
    {
        //participants contains the payers as well
        //In long interview using builder pattern to construct Expense object is correct approach
        this.totalAmount = amount;
        this.description = description;
        this.splits = splitStrategy.calculateSplits(amount, payerToAmountMap, participants);
    }

    public List<Split> getSplits()
    {
        return Collections.unmodifiableList(splits);        
    }
}
