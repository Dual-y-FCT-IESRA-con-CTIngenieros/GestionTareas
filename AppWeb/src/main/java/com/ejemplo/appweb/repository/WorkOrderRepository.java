package com.ejemplo.appweb.repository;

import com.ejemplo.appweb.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {}
