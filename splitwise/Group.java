package splitwise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Group {
    private String name;
    private List<Expense> expenses;
    private List<User> members;
    private HashMap<String, Double> userToBalanceMap;
    private List<Transaction> transactions;
    private List<Payment> payments;

    public Group(String name)
    {
        this.name = name;
        this.expenses = new ArrayList<>();
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
        userToBalanceMap = new HashMap<>();
        this.payments = new ArrayList<>();
    }

    public synchronized void addExpense(Expense expense)
    {
        expenses.add(expense);      
        simplifyTransactions(expense);
    }

    public synchronized void recordPayment(Payment payment)
    {
        payments.add(payment);
        Double senderBalance = userToBalanceMap.get(payment.from);
        userToBalanceMap.put(payment.from, senderBalance+payment.amount);
        Double receiverBalance = userToBalanceMap.get(payment.to);
        userToBalanceMap.put(payment.to, receiverBalance-payment.amount);
        updateTransactions();
    }

    public List<Transaction> getPendingTransactions()
    {
        return transactions;
    }

    public synchronized void addUser(User user)
    {
        members.add(user);
        userToBalanceMap.put(user.name, 0.0);
    }

    private void simplifyTransactions(Expense expense)
    {
        for(Split split: expense.getSplits())
        {
            String userName = split.userName;
            if(!userToBalanceMap.containsKey(userName))
            {
                System.out.println("Error: User " + userName + " doesn't exist in the group " + name);
                return;
            }

            var currBalance = userToBalanceMap.get(userName);
            userToBalanceMap.put(userName, currBalance + split.amount);
        }
        updateTransactions();
    }

    private void updateTransactions()
    {
        transactions.clear();
        PriorityQueue<Pair> minHeap = new PriorityQueue<>();
        PriorityQueue<Pair> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        for(var entry : userToBalanceMap.entrySet())
        {
            if(entry.getValue() < 0.0)
            {
                minHeap.add(new Pair(entry.getValue(), entry.getKey()));
            }
            else if(entry.getValue() > 0.0)
            {
                maxHeap.add(new Pair(entry.getValue(), entry.getKey()));
            }
        }

        while(minHeap.size() != 0)
        {
            Pair minElement = minHeap.poll();
            Pair maxElement = maxHeap.poll();
            //System.out.println(minElement.user + " " + maxElement.user);
            if(Math.abs(minElement.amount) <= maxElement.amount)
            {
                var oldAmount = maxElement.amount;
                maxElement.amount += minElement.amount;                
                transactions.add(new Transaction(Math.abs(minElement.amount), minElement.user, maxElement.user));
                if(Math.abs(minElement.amount) != oldAmount)
                {
                    maxHeap.add(maxElement);
                }
            }
            else
            {
                minElement.amount += maxElement.amount;
                transactions.add(new Transaction(maxElement.amount, minElement.user, maxElement.user));
                minHeap.add(minElement);
            }
        }
    }

    private class Pair implements Comparable<Pair>
    {
        Double amount;
        String user;

        public Pair(Double amount, String user)
        {
            this.amount = amount;
            this.user = user;
        }

        @Override
        public int compareTo(Pair obj)
        {
            return Double.compare(amount, obj.amount);
        }
    }
}
