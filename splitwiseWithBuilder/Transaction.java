package splitwiseWithBuilder;

public class Transaction {
    double amount;
    String from;
    String to; 

    public Transaction(double amount, String from, String to)
    {
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString()
    {
        return "User " + from + " owes " + to + " an amount: " + amount;
    }
}
