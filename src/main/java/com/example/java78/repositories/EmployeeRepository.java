package com.example.java78.repositories;

import com.example.java78.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee getEmployeeById(long id);
}
