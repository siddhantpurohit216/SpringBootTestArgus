package com.example.employeeManagement.EmployeeDTO;

public class EmployeeDto {
    
    // private Long id;
    private String name;
    private String email;
    private String city;

    // Constructor
    public EmployeeDto(String name, String email,String city) {
        // this.id = id;
        this.name = name;
        this.email = email;
        this.city=city;
    }


    public EmployeeDto() {
        //TODO Auto-generated constructor stub
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
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
