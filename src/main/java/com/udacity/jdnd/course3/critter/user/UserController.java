package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        List<Long> petIds = customerDTO.getPetIds();
        return getCustomerDTO(userService.saveCustomer(customer, petIds));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
      return userService.getAllCustomers().stream().map(this::getCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return getCustomerDTO(userService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = userService.saveEmployee(convertEmployeeDTOToUser(employeeDTO));
        return convertEntityToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEntityToEmployeeDTO(userService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = userService.findEmployeesForService(employeeDTO);
        return employees.stream().map(this::getEmployeeDTO).collect(Collectors.toList());
    }


    // DTO Conversions
    private static CustomerDTO convertEntityToCustomerDTO (Customer user) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(user, customerDTO);
        return customerDTO;
    }

    private static EmployeeDTO convertEntityToEmployeeDTO (Employee user) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(user, employeeDTO);
        return employeeDTO;
    }

    private static EmployeeRequestDTO convertEntityToEmployeeRequestDTO (Employee user) {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        BeanUtils.copyProperties(user, employeeRequestDTO);
        return employeeRequestDTO;
    }


    private static Customer convertCustomerDTOToUser (CustomerDTO customerDTO) {
        Customer user = new Customer();
        BeanUtils.copyProperties(customerDTO, user);
        return user;
    }


    private static Employee convertEmployeeDTOToUser (EmployeeDTO employeeDTO) {
        Employee user = new Employee();
        BeanUtils.copyProperties(employeeDTO, user);
        return user;
    }

    private static Employee convertEmployeeRequestDTOToUser (EmployeeRequestDTO employeeRequestDTO) {
        Employee user = new Employee();
        BeanUtils.copyProperties(employeeRequestDTO, user);
        return user;
    }

    private CustomerDTO getCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }
}
