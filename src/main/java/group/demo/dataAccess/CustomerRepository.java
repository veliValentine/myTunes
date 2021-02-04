package group.demo.dataAccess;

import group.demo.logger.Logger;
import group.demo.models.Customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
            finallyCloseConnectionAndLog();
        }
        return customers;
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

            setBasicCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);

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

    private void setBasicCustomerValuesToPreparedStatement(PreparedStatement preparedStatement, Customer customer) {
        try {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getCountry());
            preparedStatement.setString(4, customer.getPostalCode());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getEmail());
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        }
    }

    public boolean updateCustomer(String inputId, Customer inputCustomer) {
        boolean success = false;
        try {
            if (!inputId.equals(inputCustomer.getId())) {
                logger.errorToConsole("updateCustomer ID mismatch: path variable id different than customer.id");
                return false;
            }
            // Open connection
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connection to database opened");

            PreparedStatement preparedStatement =
                    connection.prepareStatement("Update Customer Set FirstName = ?, LastName = ?, Country = ?, PostalCode = ?, Phone = ?, Email = ? WHERE CustomerId = ?;");

            setBasicCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);
            preparedStatement.setString(7, inputCustomer.getId());

            // run statement and get result
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("\tcustomer updated successful: " + success);

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

    public Map<String, String> customersInCountry() {
        Map<String, String> countryMap = null;
        try {
            connection = DriverManager.getConnection(URL);
            logger.logToConsole("Connection to database opened");

            PreparedStatement preparedStatement =
                    connection.prepareStatement("Select Country, count(country) From Customer group by Country order by COUNT(Country) DESC;");

            ResultSet resultSet = preparedStatement.executeQuery();
            countryMap = new LinkedHashMap<>();
            while (resultSet.next()) {
                countryMap.put(
                        resultSet.getString("Country"),
                        resultSet.getString("count(country)")
                );
            }
            logger.logToConsole("\tcustomersInCountry successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return countryMap;
    }

    private void finallyCloseConnectionAndLog() {
        try {
            connection.close();
            logger.logToConsole("Connection to database closed");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        }
    }
}
