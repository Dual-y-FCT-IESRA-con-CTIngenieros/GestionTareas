package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.EmployeeWorkHours;
import java.util.List;
import java.util.Optional;

public interface EmployeeWorkHoursService {
    List<EmployeeWorkHours> findAll();
    Optional<EmployeeWorkHours> findById(Long id);
    EmployeeWorkHours save(EmployeeWorkHours ewh);
    void deleteById(Long id);
}
