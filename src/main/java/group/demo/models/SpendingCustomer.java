package group.demo.models;

public class SpendingCustomer extends Customer{
    private double totalSpending;

    public SpendingCustomer(String id, String firstName, String lastName, String country, String postalCode, String phoneNumber, String email, double totalSpending) {
        super(id, firstName, lastName, country, postalCode, phoneNumber, email);
        this.totalSpending = totalSpending;
    }

    public double getTotalSpending(){
        return totalSpending;
    }
}
