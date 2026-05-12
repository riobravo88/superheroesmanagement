package com.example.application.services;

import com.example.application.data.Superhero;
import com.example.application.data.SuperheroRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SuperheroService {

    private final SuperheroRepository repository;

    public SuperheroService(SuperheroRepository repository) {
        this.repository = repository;
    }

    public Optional<Superhero> get(Long id) {
        return repository.findById(id);
    }

    public Superhero save(Superhero entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Superhero> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Superhero> list(Pageable pageable, Specification<Superhero> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
