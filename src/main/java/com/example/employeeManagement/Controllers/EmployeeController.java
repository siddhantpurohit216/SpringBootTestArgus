// package com.example.employeeManagement.Controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.employeeManagement.EmployeeDTO.EmployeeDto;
// import com.example.employeeManagement.Entities.Employee;
// import com.example.employeeManagement.Services.EmployeeService;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/employees")
// public class EmployeeController {

//     private final EmployeeService employeeService;

//     @Autowired
//     public EmployeeController(EmployeeService employeeService) {
//         this.employeeService = employeeService;
//     }

//     @GetMapping
//     public List<EmployeeDto> getAllEmployees() {
//         return employeeService.getAllEmployees();
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
//         Optional<Employee> employee = employeeService.getEmployeeById(id);
//         return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public Employee createEmployee(@RequestBody Employee employee) {
//         return employeeService.createEmployee(employee);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
//         Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
//         return ResponseEntity.ok(updatedEmployee);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
//         employeeService.deleteEmployee(id);
//         return ResponseEntity.noContent().build();
//     }
// }
package com.example.employeeManagement.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeeManagement.EmployeeDTO.EmployeeDto;
import com.example.employeeManagement.Entities.Employee;
import com.example.employeeManagement.Entities.Address;
import com.example.employeeManagement.Services.EmployeeService;

import jakarta.transaction.Transactional;

import com.example.employeeManagement.Services.AddressService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    // keeping the fileds as final to promote immutability through constructor
    // injection
    private final EmployeeService employeeService;
    private final AddressService addressService;

    // constructor injection to promote immutability
    @Autowired
    public EmployeeController(EmployeeService employeeService, AddressService addressService) {
        this.employeeService = employeeService;
        this.addressService = addressService;
    }

    @GetMapping("/dto")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeesDTO() {
        List<Employee> employees = employeeService.getAllEmployeesDTO();
//     //hibernate runs another query to fetch the address entity
    //     // System.out.println(employees.get(0).getAddress().getCity());
        List<EmployeeDto> employeeDtos = employees.stream().map(employee -> {
            EmployeeDto dto = new EmployeeDto();
            dto.setName(employee.getFirstName());
            dto.setEmail(employee.getEmail());
            //hibernate runs another query to fetch the address entity
            dto.setCity(employee.getAddress().getCity());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(employeeDtos);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/dto/{id}")
    public ResponseEntity<EmployeeDto> createEmployeeDTO(@RequestBody EmployeeDto empDto) {

        // Manually map DTO to Address
        Address address = new Address();
        address.setCity(empDto.getCity());
        // Set other address fields if present in the DTO

        // Save Address entity
        Address savedAddress = addressService.createAddress(address);

        // Manually map DTO to Employee
        Employee employee = new Employee();
        employee.setFirstName(empDto.getName());
        employee.setEmail(empDto.getEmail());
        employee.setAddress(savedAddress);

        // Save Employee entity
        Employee savedEmployee = employeeService.createEmployee(employee);

        // Manually map the saved Employee to EmployeeDto
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(savedEmployee.getFirstName());
        employeeDto.setEmail(savedEmployee.getEmail());
        employeeDto.setCity(savedEmployee.getAddress().getCity());

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDto);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    // Optional<Employee> employee = employeeService.getEmployeeById(id);
    // return employee.map(ResponseEntity::ok).orElseGet(() ->
    // ResponseEntity.notFound().build());
    // }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeAndAddress(@PathVariable Long id) {
        try {
            Employee employee = employeeService.fetchEmployee(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if employee is not found
        }
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    // employeeService.deleteEmployee(id);
    // return ResponseEntity.noContent().build();
    // }

    // Address related endpoints
    @Transactional
    @GetMapping("/{employeeId}/address")
    public ResponseEntity<Address> getEmployeeAddress(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
        if (employee.isPresent() && employee.get().getAddress() != null) {
            return ResponseEntity.ok(employee.get().getAddress());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{employeeId}/address")
    public ResponseEntity<Address> addAddressToEmployee(@PathVariable Long employeeId, @RequestBody Address address) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
        if (employee.isPresent()) {
            employee.get().setAddress(address);
            Address savedAddress = addressService.createAddress(address);
            employeeService.updateEmployee(employeeId, employee.get());
            return ResponseEntity.ok(savedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{employeeId}/address")
    public ResponseEntity<Address> updateEmployeeAddress(@PathVariable Long employeeId, @RequestBody Address address) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
        if (employee.isPresent() && employee.get().getAddress() != null) {
            address.setId(employee.get().getAddress().getId());
            Address updatedAddress = addressService.updateAddress(address.getId(), address);
            employee.get().setAddress(updatedAddress);
            employeeService.updateEmployee(employeeId, employee.get());
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{employeeId}/address")
    public ResponseEntity<Void> deleteEmployeeAddress(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
        if (employee.isPresent() && employee.get().getAddress() != null) {
            addressService.deleteAddress(employee.get().getAddress().getId());
            employee.get().setAddress(null);
            employeeService.updateEmployee(employeeId, employee.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
