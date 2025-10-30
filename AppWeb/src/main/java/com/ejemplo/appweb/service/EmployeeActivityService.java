package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.EmployeeActivity;
import java.util.List;
import java.util.Optional;

public interface EmployeeActivityService {
    List<EmployeeActivity> findAll();
    Optional<EmployeeActivity> findById(Long id);
    EmployeeActivity save(EmployeeActivity employeeActivity);
    void deleteById(Long id);
}
