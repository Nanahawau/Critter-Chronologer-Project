package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.*;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;


    public Pet savePet (Pet pet, long ownerId) {
        System.out.println(ownerId + "ownerid");
        Customer customer = customerRepository.getOne(ownerId);
        pet.setCustomer(customer);
        Pet pet1 = petRepository.save(pet);
        customer.insertPet(pet1);
        customerRepository.save(customer);
        return pet1;
    }

    public Pet getPet (long id) {
        Optional<Pet> pet = petRepository.findById(id);
        Pet petObject = new Pet();

        if(pet.isPresent()) {
             petObject = petRepository.getOne(id);
            System.out.println(petObject + "petObject");
        }
        return petObject;
    }

    public List<Pet> getPets () {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner (long ownerId){
        return petRepository.getAllByCustomerId(ownerId);
    }
}
