package com.kavia.employees.employees.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kavia.employees.common.errors.ConflictException;
import com.kavia.employees.common.errors.NotFoundException;
import com.kavia.employees.employees.api.dto.CreateEmployeeRequest;
import com.kavia.employees.employees.api.dto.UpdateEmployeeRequest;
import com.kavia.employees.employees.model.Employee;
import com.kavia.employees.employees.repo.EmployeeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {

  @Test
  void createEmployee_conflictOnDuplicateEmail() {
    EmployeeRepository repo = mock(EmployeeRepository.class);
    when(repo.existsByEmail("dup@example.com")).thenReturn(true);

    EmployeeService svc = new EmployeeService(repo);

    CreateEmployeeRequest req = new CreateEmployeeRequest();
    req.setFirstName("A");
    req.setLastName("B");
    req.setEmail("dup@example.com");

    assertThrows(ConflictException.class, () -> svc.createEmployee(req));
    verify(repo, never()).save(any());
  }

  @Test
  void getEmployee_notFound() {
    EmployeeRepository repo = mock(EmployeeRepository.class);
    when(repo.findById(42L)).thenReturn(Optional.empty());
    EmployeeService svc = new EmployeeService(repo);

    assertThrows(NotFoundException.class, () -> svc.getEmployee(42L));
  }

  @Test
  void updateEmployee_updatesEmailWhenUnique() {
    EmployeeRepository repo = mock(EmployeeRepository.class);

    Employee existing = new Employee();
    existing.setFirstName("Old");
    existing.setLastName("Name");
    existing.setEmail("old@example.com");

    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.existsByEmail("new@example.com")).thenReturn(false);
    when(repo.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

    EmployeeService svc = new EmployeeService(repo);

    UpdateEmployeeRequest req = new UpdateEmployeeRequest();
    req.setEmail("new@example.com");
    req.setFirstName("New");

    var res = svc.updateEmployee(1L, req);
    assertEquals("new@example.com", res.getEmail());
    assertEquals("New", res.getFirstName());
  }
}
