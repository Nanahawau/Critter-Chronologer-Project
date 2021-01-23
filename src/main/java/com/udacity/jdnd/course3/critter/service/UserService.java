package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public Customer saveCustomer (Customer customer, List<Long> petIds) {
        List<Pet> customerPets = new ArrayList<>();
        if (petIds != null && !petIds.isEmpty()) {
            customerPets = petIds.stream().map((petId) -> petRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(customerPets);
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers () {
      return customerRepository.findAll();
    }

    public Employee saveEmployee (Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(long id) {
        return employeeRepository.getOne(id);
    }

    public Customer getOwnerByPet (long id) {
        return petRepository.getOne(id).getCustomer();
    }

    public void setAvailability (Set<DayOfWeek> daysAvailable, long id) {
       Optional<Employee> user = employeeRepository.findById(id);
       if (user.isPresent()){
           Employee userObject = employeeRepository.getOne(id);
           userObject.setDaysAvailable(daysAvailable);
       }
    }

    public List<Employee> findEmployeesForService (EmployeeRequestDTO user) {
        List<Employee> employees = employeeRepository
                .getAllByDaysAvailableContains(user.getDate().getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(user.getSkills()))
                .collect(Collectors.toList());
        return employees;
    }


}
