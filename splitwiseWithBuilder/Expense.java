package splitwiseWithBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Expense {
    private Double totalAmount;
    private String description;
    private List<Split> splits;
    private HashMap<String, Double> paidBy;
    private ISplitStrategy splitStrategy;
    private List<String> participants;

    private Expense(ExpenseBuilder builder)
    {
        this.totalAmount = builder.totalAmount;
        this.description = builder.description;
        this.paidBy = builder.paidBy;
        this.participants = builder.participants;
        this.splitStrategy = builder.splitStrategy;
        this.splits = splitStrategy.calculateSplits(totalAmount, paidBy, participants);
    }

    public List<Split> getSplits()
    {
        return Collections.unmodifiableList(splits);        
    }


    public static class ExpenseBuilder
    {
        private Double totalAmount;
        private String description;
        private HashMap<String, Double> paidBy;
        private List<String> participants;
        private ISplitStrategy splitStrategy;

        public ExpenseBuilder()
        {
            paidBy = new HashMap<>();
            participants = new ArrayList<>();
        }

        public ExpenseBuilder setTotalAmount(Double amount)
        {
            this.totalAmount = amount;
            return this;
        }

        public ExpenseBuilder setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public ExpenseBuilder addPaidBy(String name, Double amount)
        {
            Double finalAmount = amount;
            if(paidBy.containsKey(name))
            {
                finalAmount += paidBy.get(name);
            }
            paidBy.put(name, finalAmount);
            return this;
        }

        public ExpenseBuilder addParticipants(List<String> names)
        {
            for(var name: names)
            {
                this.participants.add(name);
            }
            return this;
        }

        public ExpenseBuilder setSplitStrategy(ISplitStrategy splitStrategy)
        {
            this.splitStrategy = splitStrategy;
            return this;
        }

        public Expense build()
        {
            return new Expense(this);
        }
    }
}
