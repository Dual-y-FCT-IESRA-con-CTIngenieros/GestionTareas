package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Activity;
import com.ejemplo.appweb.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<Activity> findAll() { return activityRepository.findAll(); }

    @Override
    public Optional<Activity> findById(Long id) { return activityRepository.findById(id); }

    @Override
    public Activity save(Activity activity) { return activityRepository.save(activity); }

    @Override
    public void deleteById(Long id) { activityRepository.deleteById(id); }
}
