package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProjectTimeCodeId implements Serializable {

    private Long idProject;
    private Long idTimeCode;

    public ProjectTimeCodeId() {}

    public ProjectTimeCodeId(Long idProject, Long idTimeCode) {
        this.idProject = idProject;
        this.idTimeCode = idTimeCode;
    }

    public Long getIdProject() { return idProject; }
    public void setIdProject(Long idProject) { this.idProject = idProject; }

    public Long getIdTimeCode() { return idTimeCode; }
    public void setIdTimeCode(Long idTimeCode) { this.idTimeCode = idTimeCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTimeCodeId)) return false;
        ProjectTimeCodeId that = (ProjectTimeCodeId) o;
        return Objects.equals(getIdProject(), that.getIdProject()) &&
               Objects.equals(getIdTimeCode(), that.getIdTimeCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdProject(), getIdTimeCode());
    }
}
