package group.demo.controllers;

import group.demo.dataAccess.CustomerRepository;
import group.demo.logger.Logger;
import group.demo.models.Customer;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private Logger logger = new Logger();
    private CustomerRepository customerRepository = new CustomerRepository(logger);

    // server health check
    @RequestMapping("/health")
    public String healthCheck(){
        return "ok";
    }

    // all customers
    @RequestMapping("/customers")
    public ArrayList<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public String addCustomer() {
        return "Add new customer";
    }

    @RequestMapping(value = "/customers/{customerId}")
    public Customer getCustomer(@PathVariable String customerId) {
        return customerRepository.getCustomer(customerId);
    }

    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.PUT)
    public String updateCustomer(@PathVariable String customerId) {
        return "Modify customer with id: " + customerId;
    }
}
