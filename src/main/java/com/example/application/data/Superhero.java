package com.example.application.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Superhero extends AbstractEntity {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    @NotBlank(message = "Superhero name is required")
    @Size(min = 2, max = 50, message = "Superhero name must be 2-50 characters")
    private String spname;

    @NotBlank(message = "Occupation is required")
    private String occupation;

    @NotBlank(message = "Home city is required")
    private String homeCity;

    @NotBlank(message = "Universe is required")
    private String universe;

    @NotBlank(message = "First appearance is required")
    private String firstApperiance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_profile_id")
    private HeroProfile heroProfile;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publishers publisher;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "superhero_powers",
            joinColumns = @JoinColumn(name = "superhero_id"),
            inverseJoinColumns = @JoinColumn(name = "power_id")
    )
    private Set<Powers> powers = new HashSet<>();

    // GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

    public String getFirstApperiance() {
        return firstApperiance;
    }

    public void setFirstApperiance(String firstApperiance) {
        this.firstApperiance = firstApperiance;
    }

    public HeroProfile getHeroProfile() {
        return heroProfile;
    }

    public void setHeroProfile(HeroProfile heroProfile) {
        this.heroProfile = heroProfile;
    }

    public Publishers getPublisher() {
        return publisher;
    }

    public void setPublisher(Publishers publisher) {
        this.publisher = publisher;
    }
    public Set<Powers> getPowers() {
        return powers;
    }

    public void setPowers(Set<Powers> powers) {
        this.powers = powers;
    }
}