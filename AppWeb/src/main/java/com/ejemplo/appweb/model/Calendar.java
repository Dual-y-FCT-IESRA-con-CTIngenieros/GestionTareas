package com.ejemplo.appweb.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCalendar;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "idActivity")
    private Activity activity;

    public Calendar() {}

    public Long getIdCalendar() { return idCalendar; }
    public void setIdCalendar(Long idCalendar) { this.idCalendar = idCalendar; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    @Override
    public String toString() {
        return "Calendar{" + "idCalendar=" + idCalendar + ", date=" + date + '}';
    }
}
