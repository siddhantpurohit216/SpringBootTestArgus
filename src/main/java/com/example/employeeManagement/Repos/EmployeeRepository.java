package com.example.employeeManagement.Repos;


// package com.example.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeeManagement.Entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
