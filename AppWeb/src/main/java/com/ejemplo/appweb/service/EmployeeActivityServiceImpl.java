package com.ejemplo.appweb.service;
import com.ejemplo.appweb.model.EmployeeActivity;
import com.ejemplo.appweb.repository.EmployeeActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeActivityServiceImpl implements EmployeeActivityService {

    @Autowired
    private EmployeeActivityRepository employeeActivityRepository;

    @Override
    public List<EmployeeActivity> findAll() { return employeeActivityRepository.findAll(); }

    @Override
    public Optional<EmployeeActivity> findById(Long id) { return employeeActivityRepository.findById(id); }

    @Override
    public EmployeeActivity save(EmployeeActivity employeeActivity) { return employeeActivityRepository.save(employeeActivity); }

    @Override
    public void deleteById(Long id) { employeeActivityRepository.deleteById(id); }
}
