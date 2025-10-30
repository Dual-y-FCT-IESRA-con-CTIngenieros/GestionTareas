package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActivity;

    @ManyToOne
    @JoinColumn(name = "idTimeCode")
    private TimeCode timeCode;

    private String desc;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeActivity> employeeActivities = new HashSet<>();

    public Activity() {}

    public Long getIdActivity() { return idActivity; }
    public void setIdActivity(Long idActivity) { this.idActivity = idActivity; }

    public TimeCode getTimeCode() { return timeCode; }
    public void setTimeCode(TimeCode timeCode) { this.timeCode = timeCode; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

    public Set<EmployeeActivity> getEmployeeActivities() { return employeeActivities; }
    public void setEmployeeActivities(Set<EmployeeActivity> employeeActivities) { this.employeeActivities = employeeActivities; }

    @Override
    public String toString() {
        return "Activity{" + "idActivity=" + idActivity + ", desc='" + desc + '\'' + '}';
    }
}
