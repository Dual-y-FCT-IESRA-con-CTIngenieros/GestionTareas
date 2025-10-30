package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Area;
import com.ejemplo.appweb.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public List<Area> findAll() { return areaRepository.findAll(); }

    @Override
    public Optional<Area> findById(Long id) { return areaRepository.findById(id); }

    @Override
    public Area save(Area area) { return areaRepository.save(area); }

    @Override
    public void deleteById(Long id) { areaRepository.deleteById(id); }
}
