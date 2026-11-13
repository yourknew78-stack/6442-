public class PaymentProcessor {

    public void processPayment(String method, double amount) {
        System.out.println("Processing payment of $" + amount);

        if (method.equals("CREDIT_CARD")) {
            System.out.println("Connecting to credit card gateway...");
            System.out.println("Charging card: $" + amount);
        } else if (method.equals("PAYPAL")) {
            System.out.println("Connecting to PayPal...");
            System.out.println("Transferring funds: $" + amount);
        } else if (method.equals("BANK_TRANSFER")) {
            System.out.println("Connecting to bank...");
            System.out.println("Initiating bank transfer of $" + amount);
        } else {
            System.out.println("Unknown payment method: " + method);
        }

        System.out.println("Payment completed.");
    }
}