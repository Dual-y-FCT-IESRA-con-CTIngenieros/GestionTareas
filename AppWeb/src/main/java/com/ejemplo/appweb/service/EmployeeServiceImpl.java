package com.ejemplo.appweb.service;
import com.ejemplo.appweb.model.Employee;
import com.ejemplo.appweb.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() { return employeeRepository.findAll(); }

    @Override
    public Optional<Employee> findById(Long id) { return employeeRepository.findById(id); }

    @Override
    public Employee save(Employee employee) { return employeeRepository.save(employee); }

    @Override
    public void deleteById(Long id) { employeeRepository.deleteById(id); }
}
