package group.demo.dataAccess;

import group.demo.logger.Logger;
import group.demo.models.CountryCustomerCount;
import group.demo.models.Customer;
import group.demo.models.SpendingCustomer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerRepository extends Repository {
    // common customer fields to all queries
    private final String baseCustomerFields = "" +
            "FirstName, " +
            "LastName, " +
            "Country, " +
            "PostalCode, " +
            "Phone, " +
            "Email ";

    public CustomerRepository(Logger logger) {
        super(logger);
    }

    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("" +
                    "select " +
                    "customerId, " + baseCustomerFields +
                    "from Customer;");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Create new Customers list and add each customer to it
            customers = parseCustomersResultSetToArray(resultSet);
            logger.logToConsole("getCustomers successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return customers;
    }

    private ArrayList<Customer> parseCustomersResultSetToArray(ResultSet resultSet) throws Exception {
        ArrayList<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(parseCustomerResultSet(resultSet));
        }
        return customers;
    }

    public Customer getCustomer(String customerId) {
        Customer customer = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("" +
                    "select " +
                    "customerId, " + baseCustomerFields +
                    " from Customer where CustomerId = ?;");
            preparedStatement.setString(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            customer = parseCustomerResultSet(resultSet);
            logger.logToConsole("getCustomer successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return customer;
    }

    public boolean addCustomer(Customer inputCustomer) {
        boolean success = false;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("" +
                    "Insert Into Customer " +
                    "(FirstName, LastName, Country, PostalCode, Phone, Email)" +
                    " values (?,?,?,?,?,?);");
            setBaseCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("addCustomer successful: " + success);
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
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
            PreparedStatement preparedStatement = prepareQuery("" +
                    "Update Customer Set " +
                    "FirstName = ?, LastName = ?, Country = ?, PostalCode = ?, Phone = ?, Email = ? " +
                    "WHERE CustomerId = ?;");
            setBaseCustomerValuesToPreparedStatement(preparedStatement, inputCustomer);
            preparedStatement.setString(7, inputCustomer.getId());
            int result = preparedStatement.executeUpdate();
            success = (result != 0);
            logger.logToConsole("customer updated successful: " + success);
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return success;
    }

    public ArrayList<CountryCustomerCount> customerInCountry() {
        ArrayList<CountryCustomerCount> countryCustomerCounts = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement = prepareQuery("" +
                    "Select " +
                    "Country, count(country) " +
                    "From Customer " +
                    "group by Country " +
                    "order by COUNT(Country) DESC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            // Parse resultSet to country->count Map
            countryCustomerCounts = parseResultSetToCountyCountArray(resultSet);
            logger.logToConsole("customersInCountry successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return countryCustomerCounts;
    }

    private ArrayList<CountryCustomerCount> parseResultSetToCountyCountArray(ResultSet resultSet) throws Exception {
        ArrayList<CountryCustomerCount> counts = new ArrayList<>();
        while (resultSet.next()) {
            counts.add(new CountryCustomerCount(
                            resultSet.getString("Country"),
                            Integer.parseInt(resultSet.getString("count(country)"))
                    )
            );
        }
        return counts;
    }

    public ArrayList<SpendingCustomer> highestSpenders() {
        ArrayList<SpendingCustomer> customers = null;
        try {
            openConnectionAndLog();
            PreparedStatement preparedStatement =
                    prepareQuery("Select " +
                            "Customer.CustomerId, " + baseCustomerFields + ", sum(Total) " +
                            "From Invoice join Customer on Invoice.CustomerId = Customer.CustomerId " +
                            "group by Invoice.CustomerId " +
                            "order by sum(total) desc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            customers = parseSpendingCustomersResultSetToArray(resultSet);
            logger.logToConsole("getSpendingCustomers successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return customers;
    }

    private ArrayList<SpendingCustomer> parseSpendingCustomersResultSetToArray(ResultSet resultSet) throws Exception {
        ArrayList<SpendingCustomer> spendingCustomers = new ArrayList<>();
        while (resultSet.next()) {
            // get customer from result set
            Customer customer = parseCustomerResultSet(resultSet);
            // get total spending from result set
            double spending = Double.parseDouble(resultSet.getString("sum(Total)"));
            // create new SpendingCustomer
            spendingCustomers.add(new SpendingCustomer(customer, spending));
        }
        return spendingCustomers;
    }

    public ArrayList<String> customerTopGenres(String customerID) {
        ArrayList<String> genres = null;
        try {
            openConnectionAndLog();
            // Query gets genreName from table that contains customerId, genreName and total genreCount for wanted customer
            // from there it gets all genreCounts that equals to biggest genreCount (where genreCount = secondQuery).
            // Second query gets the biggest genre count value
            PreparedStatement preparedStatement =
                    prepareQuery("" +
                            "select genreName\n" +
                            "   from (\n" +
                            "       select Customer.CustomerId as id, Genre.Name as genreName, count(Genre.Name) as genreCount\n" +
                            "           from Customer\n" +
                            "           join Invoice on Customer.CustomerId = Invoice.CustomerId\n" +
                            "           join InvoiceLine on Invoice.InvoiceId = InvoiceLine.InvoiceId\n" +
                            "           join Track on InvoiceLine.TrackId = Track.TrackId\n" +
                            "           join Genre on Track.GenreId = Genre.GenreId\n" +
                            "       where Customer.CustomerId = ?\n" +
                            "       group by Genre.Name\n" +
                            "       order by count(Genre.Name) DESC\n" +
                            "   )\n" +
                            "   where genreCount = (\n" +
                            "       select count(genre.name)\n" +
                            "       from Invoice\n" +
                            "           join InvoiceLine on Invoice.InvoiceId = InvoiceLine.InvoiceId\n" +
                            "           join Track on InvoiceLine.TrackId = Track.TrackId\n" +
                            "           join Genre on Track.GenreId = Genre.GenreId\n" +
                            "       where Invoice.CustomerId = id\n" +
                            "       group by Genre.Name\n" +
                            "       order by count(Genre.Name) DESC\n" +
                            "       limit 1\n" +
                            "   )");
            preparedStatement.setString(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            genres = parseGenreResultSetToArray(resultSet);
            logger.logToConsole("getCustomerGenres successful");
        } catch (Exception e) {
            logger.errorToConsole(e.toString());
        } finally {
            closeConnectionAndLog();
        }
        return genres;
    }

    private ArrayList<String> parseGenreResultSetToArray(ResultSet resultSet) throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        while (resultSet.next()) {
            genres.add(resultSet.getString("genreName"));
        }
        return genres;
    }

    // helper methods used more than once
    private void setBaseCustomerValuesToPreparedStatement(PreparedStatement preparedStatement, Customer customer) throws Exception {
        preparedStatement.setString(1, customer.getFirstName());
        preparedStatement.setString(2, customer.getLastName());
        preparedStatement.setString(3, customer.getCountry());
        preparedStatement.setString(4, customer.getPostalCode());
        preparedStatement.setString(5, customer.getPhoneNumber());
        preparedStatement.setString(6, customer.getEmail());
    }

    private Customer parseCustomerResultSet(ResultSet resultSet) throws Exception {
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
}
