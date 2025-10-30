package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.TimeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeCodeRepository extends JpaRepository<TimeCode, Long> {}
