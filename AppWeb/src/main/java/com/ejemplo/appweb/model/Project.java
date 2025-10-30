package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProject;

    private String desc;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idArea")
    private Area area;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkOrder> workOrders = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectTimeCode> projectTimeCodes = new HashSet<>();

    public Project() {}

    public Long getIdProject() { return idProject; }
    public void setIdProject(Long idProject) { this.idProject = idProject; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public Set<WorkOrder> getWorkOrders() { return workOrders; }
    public void setWorkOrders(Set<WorkOrder> workOrders) { this.workOrders = workOrders; }

    public Set<ProjectTimeCode> getProjectTimeCodes() { return projectTimeCodes; }
    public void setProjectTimeCodes(Set<ProjectTimeCode> projectTimeCodes) { this.projectTimeCodes = projectTimeCodes; }

    @Override
    public String toString() {
        return "Project{" + "idProject=" + idProject + ", desc='" + desc + '\'' + '}';
    }
}
