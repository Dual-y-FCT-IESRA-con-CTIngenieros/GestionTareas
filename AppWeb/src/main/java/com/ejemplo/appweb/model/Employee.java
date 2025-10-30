package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmployee;

    private String nombre;
    private String apellidos;
    private String email;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeWO> employeeWOs = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeActivity> employeeActivities = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeWorkHours> workHours = new HashSet<>();

    public Employee() {}

    public Long getIdEmployee() { return idEmployee; }
    public void setIdEmployee(Long idEmployee) { this.idEmployee = idEmployee; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Set<EmployeeWO> getEmployeeWOs() { return employeeWOs; }
    public void setEmployeeWOs(Set<EmployeeWO> employeeWOs) { this.employeeWOs = employeeWOs; }

    public Set<EmployeeActivity> getEmployeeActivities() { return employeeActivities; }
    public void setEmployeeActivities(Set<EmployeeActivity> employeeActivities) { this.employeeActivities = employeeActivities; }

    public Set<EmployeeWorkHours> getWorkHours() { return workHours; }
    public void setWorkHours(Set<EmployeeWorkHours> workHours) { this.workHours = workHours; }

    @Override
    public String toString() {
        return "Employee{" + "idEmployee=" + idEmployee + ", nombre='" + nombre + '\'' + '}';
    }
}
