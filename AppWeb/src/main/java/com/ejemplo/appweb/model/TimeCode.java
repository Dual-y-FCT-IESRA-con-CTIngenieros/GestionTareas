package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "TimeCode")
public class TimeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTimeCode;

    private String desc;
    private String color;
    private Boolean chkProd;

    @OneToMany(mappedBy = "timeCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities = new HashSet<>();

    @OneToMany(mappedBy = "timeCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectTimeCode> projectTimeCodes = new HashSet<>();

    public TimeCode() {}

    public Long getIdTimeCode() { return idTimeCode; }
    public void setIdTimeCode(Long idTimeCode) { this.idTimeCode = idTimeCode; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Boolean getChkProd() { return chkProd; }
    public void setChkProd(Boolean chkProd) { this.chkProd = chkProd; }

    public Set<Activity> getActivities() { return activities; }
    public void setActivities(Set<Activity> activities) { this.activities = activities; }

    public Set<ProjectTimeCode> getProjectTimeCodes() { return projectTimeCodes; }
    public void setProjectTimeCodes(Set<ProjectTimeCode> projectTimeCodes) { this.projectTimeCodes = projectTimeCodes; }

    @Override
    public String toString() {
        return "TimeCode{" + "idTimeCode=" + idTimeCode + ", desc='" + desc + '\'' + '}';
    }
}
