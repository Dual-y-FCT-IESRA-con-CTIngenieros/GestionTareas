package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Calendar;
import com.ejemplo.appweb.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Override
    public List<Calendar> findAll() { return calendarRepository.findAll(); }

    @Override
    public Optional<Calendar> findById(Long id) { return calendarRepository.findById(id); }

    @Override
    public Calendar save(Calendar calendar) { return calendarRepository.save(calendar); }

    @Override
    public void deleteById(Long id) { calendarRepository.deleteById(id); }
}
