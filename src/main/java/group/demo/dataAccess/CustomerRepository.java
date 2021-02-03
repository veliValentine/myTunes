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

    private final String customerFields = "" +
            "CustomerId, " +
            "FirstName, " +
            "LastName, " +
            "Country, " +
            "PostalCode, " +
            "Phone, " +
            "Email ";

    public CustomerRepository(Logger logger) {
        this.logger = logger;
    }

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = null;
        try {
            // Open connection
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connection to database opened");

            PreparedStatement preparedStatement =
                    connection.prepareStatement("select " + customerFields + "from Customer");

            // Run statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create new Customers list and add each customer to it
            customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(parseCustomerResultSet(resultSet));
            }
            logger.logToConsole("\tgetCustomers successful");
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

    public Customer getCustomer(String customerId) {
        Customer customer = null;
        try {
            // open connection
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connection to database opened");

            // Prepare statement
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select " + customerFields + " from Customer where CustomerId = ?");
            preparedStatement.setString(1, customerId);

            // Execute
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parse data
            while (resultSet.next()) {
                customer = parseCustomerResultSet(resultSet);
            }
            logger.logToConsole("\tgetCustomer successful");
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
        return customer;
    }

    public boolean addCustomer(Customer inputCustomer) {
        boolean success = false;
        try {
            // Open connection
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connection to database opened");

            // Prepare statement
            PreparedStatement preparedStatement =
                    connection.prepareStatement("Insert Into Customer (FirstName, LastName, Country, PostalCode, Phone, Email) values (?,?,?,?,?,?)");

            preparedStatement.setString(1, inputCustomer.getFirstName());
            preparedStatement.setString(2, inputCustomer.getLastName());
            preparedStatement.setString(3, inputCustomer.getCountry());
            preparedStatement.setString(4, inputCustomer.getPostalCode());
            preparedStatement.setString(5, inputCustomer.getPhoneNumber());
            preparedStatement.setString(6, inputCustomer.getEmail());

            // run statement and get result
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("\taddCustomer successful: " + success);
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
        return success;
    }

    private Customer parseCustomerResultSet(ResultSet resultSet) {
        Customer customer = null;
        try {
            customer = new Customer(
                    resultSet.getString("customerId"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("Country"),
                    resultSet.getString("PostalCode"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Email")
            );
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        }
        return customer;
    }
}
