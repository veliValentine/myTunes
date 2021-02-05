package group.demo.restControllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.Customer;

import group.demo.models.SpendingCustomer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(ControllerHelper.API_BASE_URL_v1 + "/customers")
public class CustomerController {

    private final Logger logger = new Logger();
    private final CustomerRepository customerRepository = new CustomerRepository(logger);

    // all customers
    @GetMapping
    public ArrayList<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    // customer by ID
    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable String customerId) {
        return customerRepository.getCustomer(customerId);
    }

    // add customer
    @RequestMapping(value = "", method = RequestMethod.POST)
    public boolean addCustomer(@RequestBody Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    // update customer
    @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
    public boolean updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(customerId, customer);
    }

    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Map<String, String> customersInCountry() {
        return customerRepository.customersInCountry();
    }

    @RequestMapping(value = "/spending", method = RequestMethod.GET)
    public ArrayList<SpendingCustomer> highestSpendingCustomers() {
        return customerRepository.highestSpenders();
    }
}
