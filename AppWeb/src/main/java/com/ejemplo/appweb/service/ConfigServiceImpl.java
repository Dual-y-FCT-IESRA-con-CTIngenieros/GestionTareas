package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Config;
import com.ejemplo.appweb.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public List<Config> findAll() { return configRepository.findAll(); }

    @Override
    public Optional<Config> findById(String key) { return configRepository.findById(key); }

    @Override
    public Config save(Config config) { return configRepository.save(config); }

    @Override
    public void deleteById(String key) { configRepository.deleteById(key); }
}
