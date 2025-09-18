package splitwise;

import java.util.HashMap;
import java.util.List;

public class SplitwiseService {
    private static SplitwiseService instance = null;
    private HashMap<String, Group> groups = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();

    private SplitwiseService() {}

    public static synchronized SplitwiseService getInstance()
    {
        if(instance == null)
        {
            instance = new SplitwiseService();
        }
        return instance;
    }

    public void addExpense(String groupName, Expense expense)
    {
        if(!groups.containsKey(groupName))
        {
            System.out.println("ERROR: Invalid group name");
            return;
        }

        groups.get(groupName).addExpense(expense);
    }

    public List<Transaction> getTransactions(String groupName)
    {
        if(!groups.containsKey(groupName))
        {
            System.out.println("ERROR: Invalid group name");
            return null;
        }

        return groups.get(groupName).getPendingTransactions();
    }

    public void recordPayment(String groupName, Payment payment)
    {
        groups.get(groupName).recordPayment(payment);
    }

    public void createUser(String name)
    {
        users.put(name, new User(name));
    }

    public void createGroup(String groupName, List<String> userList)
    {
        groups.put(groupName, new Group(groupName));
        Group group = groups.get(groupName);
        for(var user: userList)
        {
            group.addUser(users.get(user));
        }        
    }
}
