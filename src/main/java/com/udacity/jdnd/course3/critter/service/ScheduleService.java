package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.data.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;


    public Schedule createSchedule (Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        List<Pet> pets = petRepository.findAllById(petIds);
        schedule.setEmployee(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(long petId) {
      Pet pet =  petRepository.getOne(petId);
      return scheduleRepository.findByPets(pet);
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = scheduleRepository.findByEmployee(employee);
        return schedules;
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> schedules = scheduleRepository.findByPetsIn(customer.getPets());
        return schedules;
    }
}
