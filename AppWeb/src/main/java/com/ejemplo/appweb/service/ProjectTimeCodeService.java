package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.ProjectTimeCode;
import com.ejemplo.appweb.model.ProjectTimeCodeId;
import java.util.List;
import java.util.Optional;

public interface ProjectTimeCodeService {
    List<ProjectTimeCode> findAll();
    Optional<ProjectTimeCode> findById(Long id);
    ProjectTimeCode save(ProjectTimeCode projectTimeCode);
    void deleteById(ProjectTimeCodeId id);
    Optional<ProjectTimeCode> findById(ProjectTimeCodeId id);
}
