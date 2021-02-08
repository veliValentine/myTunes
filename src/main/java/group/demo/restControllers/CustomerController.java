package group.demo.restControllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ControllerHelper.API_BASE_URL_v1 + "/customers/{customerId}")
public class CustomerController {
    private final Logger logger = new Logger();
    private final CustomerRepository customerRepository = new CustomerRepository(logger);

    // customer by ID
    @RequestMapping(method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable String customerId) {
        return customerRepository.getCustomer(customerId);
    }

    // update customer
    @RequestMapping(method = RequestMethod.PUT)
    public boolean updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(customerId, customer);
    }

    // customer top genres
    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public List<String> customerTopGenres(@PathVariable String customerId) {
        return customerRepository.customerTopGenres(customerId);
    }
}
