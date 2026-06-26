package com.kavia.employees.employees.service;

import com.kavia.employees.common.errors.ConflictException;
import com.kavia.employees.common.errors.NotFoundException;
import com.kavia.employees.employees.api.dto.CreateEmployeeRequest;
import com.kavia.employees.employees.api.dto.EmployeeResponse;
import com.kavia.employees.employees.api.dto.UpdateEmployeeRequest;
import com.kavia.employees.employees.model.Employee;
import com.kavia.employees.employees.repo.EmployeeRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

  private final EmployeeRepository repository;

  public EmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  public List<EmployeeResponse> listEmployees() {
    return repository.findAll().stream().map(EmployeeService::toResponse).collect(Collectors.toList());
  }

  public EmployeeResponse getEmployee(long id) {
    Employee e = repository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found: " + id));
    return toResponse(e);
  }

  @Transactional
  public EmployeeResponse createEmployee(CreateEmployeeRequest req) {
    if (repository.existsByEmail(req.getEmail())) {
      throw new ConflictException("Email already exists: " + req.getEmail());
    }
    Employee e = new Employee();
    e.setFirstName(req.getFirstName());
    e.setLastName(req.getLastName());
    e.setEmail(req.getEmail());
    e.setTitle(req.getTitle());
    e.setDepartment(req.getDepartment());
    e.setSalary(req.getSalary());
    e.setHireDate(req.getHireDate());
    if (req.getActive() != null) {
      e.setActive(req.getActive());
    }

    Employee saved = repository.save(e);
    return toResponse(saved);
  }

  @Transactional
  public EmployeeResponse updateEmployee(long id, UpdateEmployeeRequest req) {
    Employee e = repository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found: " + id));

    if (req.getEmail() != null && !Objects.equals(req.getEmail(), e.getEmail())) {
      if (repository.existsByEmail(req.getEmail())) {
        throw new ConflictException("Email already exists: " + req.getEmail());
      }
      e.setEmail(req.getEmail());
    }

    if (req.getFirstName() != null) e.setFirstName(req.getFirstName());
    if (req.getLastName() != null) e.setLastName(req.getLastName());
    if (req.getTitle() != null) e.setTitle(req.getTitle());
    if (req.getDepartment() != null) e.setDepartment(req.getDepartment());
    if (req.getSalary() != null) e.setSalary(req.getSalary());
    if (req.getHireDate() != null) e.setHireDate(req.getHireDate());
    if (req.getActive() != null) e.setActive(req.getActive());

    Employee saved = repository.save(e);
    return toResponse(saved);
  }

  @Transactional
  public void deleteEmployee(long id) {
    if (!repository.existsById(id)) {
      throw new NotFoundException("Employee not found: " + id);
    }
    repository.deleteById(id);
  }

  private static EmployeeResponse toResponse(Employee e) {
    EmployeeResponse r = new EmployeeResponse();
    r.setId(e.getId());
    r.setFirstName(e.getFirstName());
    r.setLastName(e.getLastName());
    r.setEmail(e.getEmail());
    r.setTitle(e.getTitle());
    r.setDepartment(e.getDepartment());
    r.setSalary(e.getSalary());
    r.setHireDate(e.getHireDate());
    r.setActive(e.isActive());
    r.setCreatedAt(e.getCreatedAt());
    r.setUpdatedAt(e.getUpdatedAt());
    return r;
  }
}
