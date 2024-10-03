package com.example.java78.controllers;

import com.example.java78.entities.Employee;
import com.example.java78.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        var newEmployee = employeeRepository.save(employee);

        return ResponseEntity.ok(newEmployee);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(){
        var employees = employeeRepository.findAll();
        var employee = employees.get(0);
        return ResponseEntity.ok(employees);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        var employee = employeeRepository.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }
}
