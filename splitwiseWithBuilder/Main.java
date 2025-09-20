package splitwiseWithBuilder;

import java.util.ArrayList;
import java.util.Arrays;
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

        //1st Expense
        service.addExpense("FirstGroup", new Expense.ExpenseBuilder()
            .setTotalAmount(180.0)
            .setDescription("Icecream")
            .addPaidBy("A", 150.0)
            .addPaidBy("B", 30.0)
            .addParticipants(participants1)
            .setSplitStrategy(equalSplitStrategy)
            .build()); 
        printTransactions(service, "FirstGroup");


        //2nd Expense
        service.addExpense("FirstGroup", new Expense.ExpenseBuilder()
            .setTotalAmount(20.0)
            .setDescription("Auto")
            .addPaidBy("C", 10.0)
            .addPaidBy("D", 10.0)
            .addParticipants(participants1)
            .setSplitStrategy(equalSplitStrategy)
            .build()); 
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
