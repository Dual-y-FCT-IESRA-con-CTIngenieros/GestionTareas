package com.ejemplo.appweb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ProjectTimeCode")
public class ProjectTimeCode {

    @EmbeddedId
    private ProjectTimeCodeId id = new ProjectTimeCodeId();

    @ManyToOne
    @MapsId("idProject")
    @JoinColumn(name = "idProject")
    private Project project;

    @ManyToOne
    @MapsId("idTimeCode")
    @JoinColumn(name = "idTimeCode")
    private TimeCode timeCode;

    public ProjectTimeCode() {}

    public ProjectTimeCodeId getId() { return id; }
    public void setId(ProjectTimeCodeId id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public TimeCode getTimeCode() { return timeCode; }
    public void setTimeCode(TimeCode timeCode) { this.timeCode = timeCode; }

    @Override
    public String toString() {
        return "ProjectTimeCode{" + "project=" + (project != null ? project.getIdProject() : null) +
               ", timeCode=" + (timeCode != null ? timeCode.getIdTimeCode() : null) + '}';
    }
}
