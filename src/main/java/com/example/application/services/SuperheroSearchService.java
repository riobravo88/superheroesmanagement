package com.example.application.services;

import com.example.application.data.Publishers;
import com.example.application.data.Superhero;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperheroSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Superhero> search(String keyword, String universe, Publishers publisher) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Superhero> cq = cb.createQuery(Superhero.class);
        Root<Superhero> hero = cq.from(Superhero.class);

        List<Predicate> predicates = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            Predicate nameLike = cb.like(cb.lower(hero.get("name")), "%" + keyword.toLowerCase() + "%");
            Predicate spnameLike = cb.like(cb.lower(hero.get("spname")), "%" + keyword.toLowerCase() + "%");
            predicates.add(cb.or(nameLike, spnameLike));
        }

        if (universe != null && !universe.isBlank()) {
            predicates.add(cb.like(cb.lower(hero.get("universe")), "%" + universe.toLowerCase() + "%"));
        }

        if (publisher != null) {
            Join<Object, Object> publisherJoin = hero.join("publisher");
            predicates.add(cb.equal(publisherJoin.get("id"), publisher.getId()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
