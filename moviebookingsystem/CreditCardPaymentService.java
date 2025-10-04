package moviebookingsystem;

public class CreditCardPaymentService implements PaymentService {
    public Boolean processPayment(Double amount)
    {
        System.out.println("Starting credit card payment of amount " + amount);
        
        //Fail 50% transactions
        return (Math.random() < 0.5);
    }
}
