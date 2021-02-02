package group.demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/health")
    public String healthCheck(){
        return "ok";
    }

    @RequestMapping("/customers")
    public String getCustomers() {
        return "Return all customers";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public String addCustomer() {
        return "Add new customer";
    }

    @RequestMapping(value = "/customers/{customerId}")
    public String getCustomer(@PathVariable String customerId) {
        return "Return customer with id: " + customerId;
    }

    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.PUT)
    public String updateCustomer(@PathVariable String customerId) {
        return "Modify customer with id: " + customerId;
    }
}
