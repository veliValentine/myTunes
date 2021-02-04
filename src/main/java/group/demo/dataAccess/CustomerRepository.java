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
    private Connection connection = null;

    private final Logger logger;

    private final String baseCustomerFields = "" +
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
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("select " + baseCustomerFields + "from Customer;");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Create new Customers list and add each customer to it
            customers = parseCustomersResultSet(resultSet);
            logger.logToConsole("\tgetCustomers successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return customers;
    }

    private ArrayList<Customer> parseCustomersResultSet(ResultSet resultSet) throws Exception {
        ArrayList<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(parseBaseCustomerResultSet(resultSet));
        }
        return customers;
    }

    public Customer getCustomer(String customerId) {
        Customer customer = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("select " + baseCustomerFields + " from Customer where CustomerId = ?;");
            preparedStatement.setString(1, customerId);
            // Execute
            ResultSet resultSet = preparedStatement.executeQuery();
            // Parse data
            while (resultSet.next()) {
                customer = parseBaseCustomerResultSet(resultSet);
            }
            logger.logToConsole("\tgetCustomer successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return customer;
    }

    public boolean addCustomer(Customer inputCustomer) {
        boolean success = false;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("Insert Into Customer (FirstName, LastName, Country, PostalCode, Phone, Email) values (?,?,?,?,?,?);");
            setBasicCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);
            // run statement and get result
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("\taddCustomer successful: " + success);

        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return success;
    }

    public boolean updateCustomer(String inputId, Customer inputCustomer) {
        boolean success = false;
        try {
            if (!inputId.equals(inputCustomer.getId())) {
                logger.errorToConsole("updateCustomer ID mismatch: path variable id different than customer.id");
                return false;
            }
            openConnectionAndLog();

            PreparedStatement preparedStatement = prepareQuery("Update Customer Set FirstName = ?, LastName = ?, Country = ?, PostalCode = ?, Phone = ?, Email = ? WHERE CustomerId = ?;");
            // set basic Customer inputs and customer ID
            setBasicCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);
            preparedStatement.setString(7, inputCustomer.getId());
            // run statement and get result
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("\tcustomer updated successful: " + success);
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return success;
    }

    public Map<String, String> customersInCountry() {
        Map<String, String> countryMap = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("Select Country, count(country) From Customer group by Country order by COUNT(Country) DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Parse resultSet to country->count Map
            countryMap = parseResultToCountyCountLinkedHashMap(resultSet);
            logger.logToConsole("\tcustomersInCountry successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            finallyCloseConnectionAndLog();
        }
        return countryMap;
    }

    private LinkedHashMap<String, String> parseResultToCountyCountLinkedHashMap(ResultSet resultSet) throws Exception {
        LinkedHashMap<String, String> countryMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            countryMap.put(
                    resultSet.getString("Country"),
                    resultSet.getString("count(country)")
            );
        }
        return countryMap;
    }

    // helper methods used more than once
    private void openConnectionAndLog() throws Exception {
        connection = DriverManager.getConnection(ConnectionHelper.CONNECTION_URL);
        logger.logToConsole("Connection to database opened");
    }

    private PreparedStatement prepareQuery(String query) throws Exception {
        return connection.prepareStatement(query);
    }

    private void setBasicCustomerValuesToPreparedStatement(PreparedStatement preparedStatement, Customer customer) throws Exception {
        preparedStatement.setString(1, customer.getFirstName());
        preparedStatement.setString(2, customer.getLastName());
        preparedStatement.setString(3, customer.getCountry());
        preparedStatement.setString(4, customer.getPostalCode());
        preparedStatement.setString(5, customer.getPhoneNumber());
        preparedStatement.setString(6, customer.getEmail());
    }

    private Customer parseBaseCustomerResultSet(ResultSet resultSet) throws Exception {
        return new Customer(
                resultSet.getString("customerId"),
                resultSet.getString("FirstName"),
                resultSet.getString("LastName"),
                resultSet.getString("Country"),
                resultSet.getString("PostalCode"),
                resultSet.getString("Phone"),
                resultSet.getString("Email")
        );
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
