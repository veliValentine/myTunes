package group.demo.restControllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.Customer;

import group.demo.models.SpendingCustomer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ControllerHelper.API_BASE_URL_v1 + "/customers")
public class CustomerController {

    private final Logger logger = new Logger();
    private final CustomerRepository customerRepository = new CustomerRepository(logger);

    // all customers
    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    // add customer
    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean addCustomer(@RequestBody Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    // customer by ID
    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable String customerId) {
        return customerRepository.getCustomer(customerId);
    }

    // update customer
    @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
    public boolean updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(customerId, customer);
    }

    // customer top genres
    @RequestMapping(value = "/{customerId}/genres", method = RequestMethod.GET)
    public List<String> customerTopGenres(@PathVariable String customerId) {
        return customerRepository.customerTopGenres(customerId);
    }

    // customer count by countries
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Map<String, String> customersInCountry() {
        return customerRepository.customersInCountry();
    }

    // customers ordered by their spending
    @RequestMapping(value = "/spending", method = RequestMethod.GET)
    public List<SpendingCustomer> highestSpendingCustomers() {
        return customerRepository.highestSpenders();
    }
}
