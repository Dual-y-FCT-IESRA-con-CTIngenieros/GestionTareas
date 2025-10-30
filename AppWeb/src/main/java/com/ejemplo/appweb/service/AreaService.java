package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Area;
import java.util.List;
import java.util.Optional;

public interface AreaService {
    List<Area> findAll();
    Optional<Area> findById(Long id);
    Area save(Area area);
    void deleteById(Long id);
}
