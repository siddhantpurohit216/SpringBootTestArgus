package com.example.employeeManagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeeManagement.Entities.Address;
import com.example.employeeManagement.Repos.AddressRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    @Transactional
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found with id " + id));

        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setZipCode(addressDetails.getZipCode());

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found with id " + id));

        addressRepository.delete(address);
    }
}
