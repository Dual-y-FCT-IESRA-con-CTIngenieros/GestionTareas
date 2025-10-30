package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Aircraft;
import java.util.List;
import java.util.Optional;

public interface AircraftService {
    List<Aircraft> findAll();
    Optional<Aircraft> findById(Long id);
    Aircraft save(Aircraft aircraft);
    void deleteById(Long id);
}
