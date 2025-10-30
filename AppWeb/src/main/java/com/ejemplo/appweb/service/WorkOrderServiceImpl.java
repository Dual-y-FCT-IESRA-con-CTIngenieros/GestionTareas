package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.WorkOrder;
import com.ejemplo.appweb.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Override
    public List<WorkOrder> findAll() {
        return workOrderRepository.findAll();
    }

    @Override
    public Optional<WorkOrder> findById(Long id) {
        return workOrderRepository.findById(id);
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    @Override
    public void deleteById(Long id) {
        workOrderRepository.deleteById(id);
    }
}
