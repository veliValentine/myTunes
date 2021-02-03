package group.demo.dataAccess;

import group.demo.logger.Logger;
import group.demo.models.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerRepository {
    private final String URL = ConnectionHelper.CONNECTION_URL;
    private Connection connection = null;

    private final Logger logger;

    public CustomerRepository(Logger logger){
        this.logger = logger;
    }

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = null;
        try {
            // Open connection
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connected to database successful");

            PreparedStatement preparedStatement =
                    connection.prepareStatement("select " +
                            "CustomerId, " +
                            "FirstName, " +
                            "LastName, " +
                            "Country, " +
                            "PostalCode, " +
                            "Phone, " +
                            "Email " +
                            "from Customer"
                    );
            // Run statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create new Customers list and add each customer to it
            customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(
                        new Customer(
                                resultSet.getString("customerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                resultSet.getString("Country"),
                                resultSet.getString("PostalCode"),
                                resultSet.getString("Phone"),
                                resultSet.getString("Email")
                        )
                );
            }
            logger.logToConsole("getCustomers successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            try {
                connection.close();
                logger.logToConsole("Connection to database closed");
            } catch (Exception e) {
                logger.errorToConsole(e.toString());
            }
        }
        return customers;
    }
}
