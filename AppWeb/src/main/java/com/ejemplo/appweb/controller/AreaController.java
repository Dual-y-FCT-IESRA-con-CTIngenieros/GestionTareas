package com.ejemplo.appweb.controller;

import com.ejemplo.appweb.model.Area;
import com.ejemplo.appweb.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping
    public List<Area> getAll() {
        return areaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Area> getById(@PathVariable Long id) {
        return areaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Area create(@RequestBody Area area) {
        return areaService.save(area);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Area> update(@PathVariable Long id, @RequestBody Area details) {
        return areaService.findById(id)
                .map(a -> {
                    a.setDesc(details.getDesc());
                    return ResponseEntity.ok(areaService.save(a));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (areaService.findById(id).isPresent()) {
            areaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
