package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArea;

    private String desc;

    @OneToMany(mappedBy = "area")
    private Set<Project> projects = new HashSet<>();

    public Area() {}

    public Long getIdArea() { return idArea; }
    public void setIdArea(Long idArea) { this.idArea = idArea; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }

    @Override
    public String toString() {
        return "Area{" + "idArea=" + idArea + ", desc='" + desc + '\'' + '}';
    }
}
