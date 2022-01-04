package microservices.customer.api;

import lombok.extern.slf4j.Slf4j;
import microservices.customer.intercomm.AccountClient;
import microservices.customer.model.Account;
import microservices.customer.model.Customer;
import microservices.customer.model.CustomerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import microservices.customer.exceptions.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class Api {

    @Autowired
    private AccountClient accountClient;

    private List<Customer> customers;

    public Api() {
        customers = new ArrayList<>();
        customers.add(new Customer(1, "12345", "Adam Kowalski", CustomerType.INDIVIDUAL));
        customers.add(new Customer(2, "12346", "Anna Malinowska", CustomerType.INDIVIDUAL));
        customers.add(new Customer(3, "12347", "Paweł Michalski", CustomerType.INDIVIDUAL));
        customers.add(new Customer(4, "12348", "Karolina Lewandowska", CustomerType.INDIVIDUAL));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pesel/{pesel}")
    public Customer findByPesel(@PathVariable("pesel") String pesel) throws CustomerNotFoundException {
        log.info(String.format("Customer.findByPesel(%s)", pesel));
        return customers.stream()
                .filter(it -> it.getPesel().equals(pesel))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("pesel : " + pesel));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Customer> findAll() {
        log.info("Customer.findAll()");
        return customers;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Customer findById(@PathVariable("id") Integer id) throws CustomerNotFoundException {
        log.info(String.format("Customer.findById(%s)", id));
        Customer customer = customers.stream()
                .filter(it -> it.getId().intValue() == id.intValue())
                .findFirst().orElseThrow(() -> new CustomerNotFoundException("id : " + id));

        List<Account> accounts = accountClient.getAccounts(id);
        customer.setAccounts(accounts);
        return customer;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public Customer createNewCustomer(@RequestBody Customer customer) {
        log.info("Customer.createNewCustomer()");
        if (customer.getId() != null) {
            return null;
        }
        int size = customers.size();
        customer.setId(size + 1);
        customers.add(customer);
        return customer;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public boolean deleteCustomer(@PathVariable Integer id) {
        log.info("Customer.deleteCustomer()");
        try {
            Customer byId = findById(id);
            customers.remove(byId);
        } catch (CustomerNotFoundException e) {
            return false;
        }
        return true;
    }

}
