package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EmployeeActivity")
public class EmployeeActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "idWorkOrder")
    private WorkOrder workOrder;

    @ManyToOne
    @JoinColumn(name = "idTimeCode")
    private TimeCode timeCode;

    @ManyToOne
    @JoinColumn(name = "idActivity")
    private Activity activity;

    private Double time; // horas imputadas

    private LocalDate date;

    private String comment;

    public EmployeeActivity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public TimeCode getTimeCode() { return timeCode; }
    public void setTimeCode(TimeCode timeCode) { this.timeCode = timeCode; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    public Double getTime() { return time; }
    public void setTime(Double time) { this.time = time; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return "EmployeeActivity{" + "id=" + id + ", time=" + time + ", date=" + date + '}';
    }
}
