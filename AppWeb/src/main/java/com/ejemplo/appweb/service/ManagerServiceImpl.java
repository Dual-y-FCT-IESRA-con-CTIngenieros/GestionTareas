package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Manager;
import com.ejemplo.appweb.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Optional<Manager> findById(Long id) {
        return managerRepository.findById(id);
    }

    @Override
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public void deleteById(Long id) {
        managerRepository.deleteById(id);
    }
}
