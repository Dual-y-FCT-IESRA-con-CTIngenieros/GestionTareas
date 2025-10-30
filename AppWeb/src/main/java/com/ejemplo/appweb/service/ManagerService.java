package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Manager;
import java.util.List;
import java.util.Optional;

public interface ManagerService {
    List<Manager> findAll();
    Optional<Manager> findById(Long id);
    Manager save(Manager manager);
    void deleteById(Long id);
}
