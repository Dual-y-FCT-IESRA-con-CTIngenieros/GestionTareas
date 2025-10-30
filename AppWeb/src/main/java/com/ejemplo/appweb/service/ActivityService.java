package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Activity;
import java.util.List;
import java.util.Optional;

public interface ActivityService {
    List<Activity> findAll();
    Optional<Activity> findById(Long id);
    Activity save(Activity activity);
    void deleteById(Long id);
} 