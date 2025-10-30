package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Calendar;
import java.util.List;
import java.util.Optional;

public interface CalendarService {
    List<Calendar> findAll();
    Optional<Calendar> findById(Long id);
    Calendar save(Calendar calendar);
    void deleteById(Long id);
}
