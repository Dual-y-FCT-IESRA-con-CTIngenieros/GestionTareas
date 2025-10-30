package com.ejemplo.appweb.controller;

import com.ejemplo.appweb.model.Manager;
import com.ejemplo.appweb.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping
    public List<Manager> getAll() {
        return managerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getById(@PathVariable Long id) {
        return managerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Manager create(@RequestBody Manager manager) {
        return managerService.save(manager);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manager> update(@PathVariable Long id, @RequestBody Manager details) {
        return managerService.findById(id)
                .map(manager -> {
                    manager.setNombre(details.getNombre());
                    manager.setApellidos(details.getApellidos());
                    return ResponseEntity.ok(managerService.save(manager));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (managerService.findById(id).isPresent()) {
            managerService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
