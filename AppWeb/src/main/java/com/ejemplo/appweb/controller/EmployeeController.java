package com.ejemplo.appweb.controller;

import com.ejemplo.appweb.model.Employee;
import com.ejemplo.appweb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee details) {
        return employeeService.findById(id)
                .map(emp -> {
                    emp.setNombre(details.getNombre());
                    emp.setApellidos(details.getApellidos());
                    emp.setEmail(details.getEmail());
                    emp.setDateFrom(details.getDateFrom());
                    emp.setDateTo(details.getDateTo());
                    emp.setRol(details.getRol());
                    return ResponseEntity.ok(employeeService.save(emp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (employeeService.findById(id).isPresent()) {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
