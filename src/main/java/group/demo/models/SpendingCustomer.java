package group.demo.models;

public class SpendingCustomer extends Customer{
    private double totalSpending;

    public SpendingCustomer(Customer customer, double spending){
        super(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getCountry(), customer.getPostalCode(), customer.getPhoneNumber(), customer.getEmail());
        totalSpending = spending;
    }

    public double getTotalSpending(){
        return totalSpending;
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }
}
