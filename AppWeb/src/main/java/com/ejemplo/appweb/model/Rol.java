package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    private String rol;

    @OneToMany(mappedBy = "rol")
    private Set<Employee> employees = new HashSet<>();

    public Rol() {}

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) { this.employees = employees; }

    @Override
    public String toString() {
        return "Rol{" + "idRol=" + idRol + ", rol='" + rol + '\'' + '}';
    }
}
