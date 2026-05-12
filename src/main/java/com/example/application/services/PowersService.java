package com.example.application.services;

import com.example.application.data.Powers;
import com.example.application.data.PowersRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PowersService {

    private final PowersRepository repository;

    public PowersService(PowersRepository repository) {
        this.repository = repository;
    }

    public Optional<Powers> get(Long id) {
        return repository.findById(id);
    }

    public Powers save(Powers entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Powers> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Powers> list(Pageable pageable, Specification<Powers> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
