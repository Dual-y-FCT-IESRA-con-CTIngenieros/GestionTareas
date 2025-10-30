package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EmployeeWorkHours")
public class EmployeeWorkHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idWorkHours;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private Employee employee;

    private Double hours;

    public EmployeeWorkHours() {}

    public Long getIdWorkHours() { return idWorkHours; }
    public void setIdWorkHours(Long idWorkHours) { this.idWorkHours = idWorkHours; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Double getHours() { return hours; }
    public void setHours(Double hours) { this.hours = hours; }

    @Override
    public String toString() {
        return "EmployeeWorkHours{" + "idWorkHours=" + idWorkHours + ", hours=" + hours + '}';
    }
}
