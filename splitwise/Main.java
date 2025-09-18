package splitwise;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args)
    {
        ISplitStrategy equalSplitStrategy = EqualSplitStrategy.getInstance();

        var service = SplitwiseService.getInstance();
        service.createUser("A");
        service.createUser("B");
        service.createUser("C");
        service.createUser("D");
        service.createUser("E");
        service.createUser("F");
        service.createUser("G");

        ArrayList<String> participants1 = new ArrayList<>();
        participants1.add("A");
        participants1.add("B");
        participants1.add("C");
        participants1.add("D");

        ArrayList<String> participants2 = new ArrayList<>();
        participants2.add("E");
        participants2.add("F");
        participants2.add("G");

        service.createGroup("FirstGroup", participants1);
        service.createGroup("SecondGroup", participants2);

        //1st Expense (In long interview use builder pattern to generate this)
        HashMap<String, Double> payerToAmountMap = new HashMap<>();
        payerToAmountMap.put("A", 150.0);
        payerToAmountMap.put("B", 30.0);
        service.addExpense("FirstGroup", new Expense(
            180.0,
            "IceCream",
            payerToAmountMap,
            participants1,
            equalSplitStrategy
        ));  
        printTransactions(service, "FirstGroup");


        //2nd Expense
        payerToAmountMap = new HashMap<>();
        payerToAmountMap.put("C", 10.0);
        payerToAmountMap.put("D", 10.0);
        service.addExpense("FirstGroup", new Expense(
            20.0,
            "Auto",
            payerToAmountMap,
            participants1,
            equalSplitStrategy
        )); 
        printTransactions(service, "FirstGroup");

        //Make a payment, B should now not be involved in any transaction post this
        service.recordPayment("FirstGroup", new Payment(20.0, "B", "C"));
        printTransactions(service, "FirstGroup");
    }

    
    private static void printTransactions(SplitwiseService service, String groupName)
    {
        var transactions = service.getTransactions(groupName);
        for(var txn: transactions)
        {
            System.out.println(txn);
        }
        System.out.println("-----------------------------------");
    }
}
