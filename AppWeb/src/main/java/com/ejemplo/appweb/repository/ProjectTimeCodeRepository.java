package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.ProjectTimeCode;
import com.ejemplo.appweb.model.ProjectTimeCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTimeCodeRepository extends JpaRepository<ProjectTimeCode, ProjectTimeCodeId> {}
