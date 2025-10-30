package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.ProjectTimeCode;
import com.ejemplo.appweb.model.ProjectTimeCodeId;
import com.ejemplo.appweb.repository.ProjectTimeCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectTimeCodeServiceImpl implements ProjectTimeCodeService {

    @Autowired
    private ProjectTimeCodeRepository projectTimeCodeRepository;

    @Override
    public List<ProjectTimeCode> findAll() { return projectTimeCodeRepository.findAll(); }

    @Override
    public Optional<ProjectTimeCode> findById(ProjectTimeCodeId id) { return projectTimeCodeRepository.findById(id); }

    @Override
    public ProjectTimeCode save(ProjectTimeCode projectTimeCode) { return projectTimeCodeRepository.save(projectTimeCode); }

    @Override
    public void deleteById(ProjectTimeCodeId id) { projectTimeCodeRepository.deleteById(id); }

    @Override
    public Optional<ProjectTimeCode> findById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
