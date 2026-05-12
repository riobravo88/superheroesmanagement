package com.example.application.services;

import com.example.application.data.Publishers;
import com.example.application.data.PublishersRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PublishersService {

    private final PublishersRepository repository;

    public PublishersService(PublishersRepository repository) {
        this.repository = repository;
    }

    public Optional<Publishers> get(Long id) {
        return repository.findById(id);
    }

    public Publishers save(Publishers entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Publishers> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Publishers> list(Pageable pageable, Specification<Publishers> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
