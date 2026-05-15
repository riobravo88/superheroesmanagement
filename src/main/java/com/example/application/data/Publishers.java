package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Publishers extends AbstractEntity {

    @NotBlank(message = "Publisher name is required")
    @Size(min = 2, max = 80, message = "Publisher name must be 2-80 characters")
    private String publisherName;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be 2-50 characters")
    private String country;

    @NotBlank(message = "Founded year is required")
    @Size(min = 4, max = 4, message = "Founded year must be 4 characters")
    private String foundedYear;

    @NotBlank(message = "CEO is required")
    @Size(min = 2, max = 80, message = "CEO must be 2-80 characters")
    private String ceo;

    @NotBlank(message = "Universe is required")
    @Size(min = 2, max = 80, message = "Universe must be 2-80 characters")
    private String universe;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.EAGER)
    private List<Superhero> superheroes;

    public String getPublisherName() {
        return publisherName;
    }
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getFoundedYear() {
        return foundedYear;
    }
    public void setFoundedYear(String foundedYear) {
        this.foundedYear = foundedYear;
    }
    public String getCeo() {
        return ceo;
    }
    public void setCeo(String ceo) {
        this.ceo = ceo;
    }
    public String getUniverse() {
        return universe;
    }
    public void setUniverse(String universe) {
        this.universe = universe;
    }
    public List<Superhero> getSuperheroes() {
        return superheroes;
    }

    public void setSuperheroes(List<Superhero> superheroes) {
        this.superheroes = superheroes;
    }

}
