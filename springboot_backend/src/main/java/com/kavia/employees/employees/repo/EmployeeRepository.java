package com.kavia.employees.employees.repo;

import com.kavia.employees.employees.model.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByEmail(String email);

  boolean existsByEmail(String email);
}
