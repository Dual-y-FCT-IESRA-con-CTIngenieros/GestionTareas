package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.WorkOrder;
import java.util.List;
import java.util.Optional;

public interface WorkOrderService {
    List<WorkOrder> findAll();
    Optional<WorkOrder> findById(Long id);
    WorkOrder save(WorkOrder workOrder);
    void deleteById(Long id);
}
