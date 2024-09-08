package com.example.employeeManagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeeManagement.EmployeeDTO.EmployeeDto;
import com.example.employeeManagement.Entities.Employee;
import com.example.employeeManagement.Entities.Address;
import com.example.employeeManagement.Repos.EmployeeRepository;
import com.example.employeeManagement.Repos.AddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, AddressRepository addressRepository) {
        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;
    }



    public List<Employee> getAllEmployeesDTO() {
        return employeeRepository.findAll();
    }
    

    public Employee createEmployeeDTO(EmployeeDto empDto)
    {
        Address a =new Address();
        a.setCity(empDto.getCity());
        
        Address savedAdd=addressRepository.save(a);

        Employee newEmp=new Employee();
        newEmp.setFirstName(empDto.getName());
        newEmp.setEmail(empDto.getEmail());
        newEmp.setAddress(a); // Associate with Address

        // Save Employee entity
        Employee savedEmployee= employeeRepository.save(newEmp);

        //make a employeeDTO to return:

        EmployeeDto empDtoRes=new EmployeeDto();
        empDto.setName(savedEmployee.getFirstName());
        empDto.setEmail(savedEmployee.getEmail());
        empDto.setCity(savedEmployee.getAddress().getCity());

        return savedEmployee;

    }

    // @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        List<Employee> emp = employeeRepository.findAll();

        return emp;
    }

    @Transactional(readOnly = true)
    public Employee fetchEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
        // Access the address to ensure it's loaded
        // This is a no-op if it's already loaded
        employee.getAddress(); 
        return employee;
    }
    
    // @Transactional(readOnly = true)
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    //createEmployeeWithAddress:
    // @Transactional
    public Employee createEmployee(Employee employee) {
        // Address address = employee.getAddress();
        // if (address != null) {
        //     addressRepository.save(address);
        // }
        return employeeRepository.save(employee);
    }

    // @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setPassword(employeeDetails.getPassword());

        Address address = employeeDetails.getAddress();
        if (address != null) {
            if (employee.getAddress() != null) {
                address.setId(employee.getAddress().getId());
            }
            addressRepository.save(address);
            employee.setAddress(address);
        }

        return employeeRepository.save(employee);
    }

    // @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
        
        if (employee.getAddress() != null) {
            addressRepository.delete(employee.getAddress());
        }
        employeeRepository.delete(employee);
    }
}
