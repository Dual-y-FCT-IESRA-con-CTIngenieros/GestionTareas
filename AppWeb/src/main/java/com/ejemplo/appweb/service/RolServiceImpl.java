package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Rol;
import com.ejemplo.appweb.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> findAll() { return rolRepository.findAll(); }

    @Override
    public Optional<Rol> findById(Long id) { return rolRepository.findById(id); }

    @Override
    public Rol save(Rol rol) { return rolRepository.save(rol); }

    @Override
    public void deleteById(Long id) { rolRepository.deleteById(id); }
}
