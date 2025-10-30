package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idManager;

    private String nombre;
    private String apellidos;

    @OneToMany(mappedBy = "projectManager")
    private Set<WorkOrder> workOrders = new HashSet<>();

    public Manager() {}

    public Long getIdManager() { return idManager; }
    public void setIdManager(Long idManager) { this.idManager = idManager; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public Set<WorkOrder> getWorkOrders() { return workOrders; }
    public void setWorkOrders(Set<WorkOrder> workOrders) { this.workOrders = workOrders; }

    @Override
    public String toString() {
        return "Manager{" + "idManager=" + idManager + ", nombre='" + nombre + '\'' + '}';
    }
}
