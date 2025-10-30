package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "WorkOrder")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idWorkOrder;

    private String desc;

    @ManyToOne
    @JoinColumn(name = "projectManager")
    private Manager projectManager;

    @ManyToOne
    @JoinColumn(name = "idProject")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "idAircraft")
    private Aircraft aircraft;

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeWO> employeeWOs = new HashSet<>();

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeActivity> employeeActivities = new HashSet<>();

    public WorkOrder() {}

    public Long getIdWorkOrder() { return idWorkOrder; }
    public void setIdWorkOrder(Long idWorkOrder) { this.idWorkOrder = idWorkOrder; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Manager getProjectManager() { return projectManager; }
    public void setProjectManager(Manager projectManager) { this.projectManager = projectManager; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }

    public Set<EmployeeWO> getEmployeeWOs() { return employeeWOs; }
    public void setEmployeeWOs(Set<EmployeeWO> employeeWOs) { this.employeeWOs = employeeWOs; }

    public Set<EmployeeActivity> getEmployeeActivities() { return employeeActivities; }
    public void setEmployeeActivities(Set<EmployeeActivity> employeeActivities) { this.employeeActivities = employeeActivities; }

    @Override
    public String toString() {
        return "WorkOrder{" + "idWorkOrder=" + idWorkOrder + ", desc='" + desc + '\'' + '}';
    }
}
