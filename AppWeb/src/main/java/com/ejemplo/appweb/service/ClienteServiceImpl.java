package com.ejemplo.appweb.service;

import com.ejemplo.appweb.model.Cliente;
import com.ejemplo.appweb.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() { return clienteRepository.findAll(); }

    @Override
    public Optional<Cliente> findById(Long id) { return clienteRepository.findById(id); }

    @Override
    public Cliente save(Cliente cliente) { return clienteRepository.save(cliente); }

    @Override
    public void deleteById(Long id) { clienteRepository.deleteById(id); }
}
