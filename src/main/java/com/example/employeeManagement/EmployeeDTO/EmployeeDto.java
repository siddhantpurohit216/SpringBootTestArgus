package com.example.employeeManagement.EmployeeDTO;

public class EmployeeDto {
    
    // private Long id;
    private String name;
    private String email;

    // Constructor
    public EmployeeDto(String name, String email) {
        // this.id = id;
        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
