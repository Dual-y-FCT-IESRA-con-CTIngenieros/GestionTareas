package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> findAll();
    Optional<Project> findById(Long id);
    Project save(Project project);
    void deleteById(Long id);
}
