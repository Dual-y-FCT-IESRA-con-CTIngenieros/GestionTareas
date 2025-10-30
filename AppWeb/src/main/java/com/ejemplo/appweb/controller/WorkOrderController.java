package com.ejemplo.appweb.controller;

import com.ejemplo.appweb.model.WorkOrder;
import com.ejemplo.appweb.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public List<WorkOrder> getAll() {
        return workOrderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrder> getById(@PathVariable Long id) {
        return workOrderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WorkOrder create(@RequestBody WorkOrder workOrder) {
        return workOrderService.save(workOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrder> update(@PathVariable Long id, @RequestBody WorkOrder details) {
        return workOrderService.findById(id)
                .map(order -> {
                    order.setDesc(details.getDesc());
                    order.setProjectManager(details.getProjectManager());
                    order.setProject(details.getProject());
                    order.setAircraft(details.getAircraft());
                    return ResponseEntity.ok(workOrderService.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (workOrderService.findById(id).isPresent()) {
            workOrderService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
