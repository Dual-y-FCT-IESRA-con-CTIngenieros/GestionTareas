package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAircraft;

    private String desc;

    @OneToMany(mappedBy = "aircraft")
    private Set<WorkOrder> workOrders = new HashSet<>();

    public Aircraft() {}

    public Long getIdAircraft() { return idAircraft; }
    public void setIdAircraft(Long idAircraft) { this.idAircraft = idAircraft; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Set<WorkOrder> getWorkOrders() { return workOrders; }
    public void setWorkOrders(Set<WorkOrder> workOrders) { this.workOrders = workOrders; }

    @Override
    public String toString() {
        return "Aircraft{" + "idAircraft=" + idAircraft + ", desc='" + desc + '\'' + '}';
    }
}
