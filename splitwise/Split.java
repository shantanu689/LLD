package splitwise;

public class Split {
    public String userName;
    public Double amount;

    public Split(String userName, Double amount)
    {
        this.userName = userName;
        this.amount = amount;
        // +ve amount =  user is owed that value
    }
}
