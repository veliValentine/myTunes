package group.demo.models;

//select CustomerId, FirstName, LastName, Country, PostalCode, Phone, Email from Customer
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String email;


    public Customer(String id, String firstName, String lastName, String country, String postalCode, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
