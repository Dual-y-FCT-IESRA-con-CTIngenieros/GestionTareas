package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.EmployeeActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeActivityRepository extends JpaRepository<EmployeeActivity, Long> {}
