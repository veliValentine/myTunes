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
public class CustomersController {

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
