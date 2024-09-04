package com.example.employeeManagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeeManagement.EmployeeDTO.EmployeeDto;
import com.example.employeeManagement.Entities.Employee;
import com.example.employeeManagement.Repos.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDto> getAllEmployees() {
        
        List<Employee>emp= employeeRepository.findAll();
        List<EmployeeDto> empDTO=new ArrayList();

        for(Employee e: emp)
        {
            EmployeeDto ans=new EmployeeDto(e.getFirstName()+ " "+ e.getLastName(), e.getEmail());
            empDTO.add(ans);
        }

        return empDTO;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setPassword(employeeDetails.getPassword());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
        
        employeeRepository.delete(employee);
    }
}
