package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Config;
import java.util.List;
import java.util.Optional;

public interface ConfigService {
    List<Config> findAll();
    Optional<Config> findById(String key);
    Config save(Config config);
    void deleteById(String key);
}
