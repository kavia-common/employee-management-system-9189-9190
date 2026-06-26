package com.kavia.employees.employees.api;

import com.kavia.employees.employees.api.dto.CreateEmployeeRequest;
import com.kavia.employees.employees.api.dto.EmployeeResponse;
import com.kavia.employees.employees.api.dto.UpdateEmployeeRequest;
import com.kavia.employees.employees.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService service;

  public EmployeeController(EmployeeService service) {
    this.service = service;
  }

  @GetMapping
  public List<EmployeeResponse> list() {
    return service.listEmployees();
  }

  @GetMapping("/{id}")
  public EmployeeResponse get(@PathVariable long id) {
    return service.getEmployee(id);
  }

  @PostMapping
  public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody CreateEmployeeRequest req) {
    EmployeeResponse created = service.createEmployee(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public EmployeeResponse update(@PathVariable long id, @Valid @RequestBody UpdateEmployeeRequest req) {
    return service.updateEmployee(id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long id) {
    service.deleteEmployee(id);
  }
}
