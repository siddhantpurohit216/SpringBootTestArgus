package com.example.employeeManagement.Repos;

import com.example.employeeManagement.Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
