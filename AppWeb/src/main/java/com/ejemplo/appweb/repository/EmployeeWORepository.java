package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.EmployeeWO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWORepository extends JpaRepository<EmployeeWO, Long> {}
