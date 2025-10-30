package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.EmployeeWorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWorkHoursRepository extends JpaRepository<EmployeeWorkHours, Long> {}
