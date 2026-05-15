package com.example.application.services;

import com.example.application.data.HeroProfile;
import com.example.application.data.HeroProfileRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class HeroProfileService {

    private final HeroProfileRepository repository;

    public HeroProfileService(HeroProfileRepository repository) {
        this.repository = repository;
    }

    public Optional<HeroProfile> get(Long id) {
        return repository.findById(id);
    }

    public HeroProfile save(HeroProfile entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<HeroProfile> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<HeroProfile> list(Pageable pageable, Specification<HeroProfile> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<HeroProfile> listAll() {
        return repository.findAll();
    }
}
