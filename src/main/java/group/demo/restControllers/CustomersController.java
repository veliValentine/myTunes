package group.demo.restControllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.CountryCustomerCount;
import group.demo.models.Customer;

import group.demo.models.SpendingCustomer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ControllerHelper.API_BASE_URL_v1 + "/customers")
public class CustomersController {

    private final Logger logger = new Logger();
    private final CustomerRepository customerRepository = new CustomerRepository(logger);

    // all customers
    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    // add customer
    @RequestMapping(method = RequestMethod.POST)
    public boolean addCustomer(@RequestBody Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    // customer count by countries
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public List<CountryCustomerCount> customersInCountry() {
        return customerRepository.customerInCountry();
    }

    // customers ordered by their spending
    @RequestMapping(value = "/spending", method = RequestMethod.GET)
    public List<SpendingCustomer> highestSpendingCustomers() {
        return customerRepository.highestSpenders();
    }
}
