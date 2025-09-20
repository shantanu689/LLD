package splitwiseWithBuilder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SplitwiseService {
    private static SplitwiseService instance = null;
    private ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private SplitwiseService() {}

    public synchronized static SplitwiseService getInstance()
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
