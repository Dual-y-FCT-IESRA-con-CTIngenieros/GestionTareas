package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Aircraft;
import com.ejemplo.appweb.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AircraftServiceImpl implements AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Override
    public List<Aircraft> findAll() { return aircraftRepository.findAll(); }

    @Override
    public Optional<Aircraft> findById(Long id) { return aircraftRepository.findById(id); }

    @Override
    public Aircraft save(Aircraft aircraft) { return aircraftRepository.save(aircraft); }

    @Override
    public void deleteById(Long id) { aircraftRepository.deleteById(id); }
}
