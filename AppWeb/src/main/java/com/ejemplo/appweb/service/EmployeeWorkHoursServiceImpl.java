package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.EmployeeWorkHours;
import com.ejemplo.appweb.repository.EmployeeWorkHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeWorkHoursServiceImpl implements EmployeeWorkHoursService {

    @Autowired
    private EmployeeWorkHoursRepository employeeWorkHoursRepository;

    @Override
    public List<EmployeeWorkHours> findAll() {
        return employeeWorkHoursRepository.findAll();
    }

    @Override
    public Optional<EmployeeWorkHours> findById(Long id) {
        return employeeWorkHoursRepository.findById(id);
    }

    @Override
    public EmployeeWorkHours save(EmployeeWorkHours employeeWorkHours) {
        return employeeWorkHoursRepository.save(employeeWorkHours);
    }

    @Override
    public void deleteById(Long id) {
        employeeWorkHoursRepository.deleteById(id);
    }
}
