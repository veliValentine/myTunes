package group.demo.controllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.Customer;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(ControllerHelper.BASE_URL_v1)
public class CustomerController {

    private Logger logger = new Logger();
    private CustomerRepository customerRepository = new CustomerRepository(logger);

    // all customers
    @RequestMapping("/customers")
    public ArrayList<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    // customer by ID
    @RequestMapping(value = "/customers/{customerId}")
    public Customer getCustomer(@PathVariable String customerId) {
        return customerRepository.getCustomer(customerId);
    }

    // add customer
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public boolean addCustomer(@RequestBody Customer customer) {
        return customerRepository.addCustomer(customer);
    }

    // update customer
    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.PUT)
    public boolean updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(customerId, customer);
    }
}
