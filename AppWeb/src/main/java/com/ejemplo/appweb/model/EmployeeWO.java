package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EmployeeWO")
public class EmployeeWO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String desc;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "idWorkOrder")
    private WorkOrder workOrder;

    public EmployeeWO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    @Override
    public String toString() {
        return "EmployeeWO{" + "id=" + id + ", desc='" + desc + '\'' + '}';
    }
}
