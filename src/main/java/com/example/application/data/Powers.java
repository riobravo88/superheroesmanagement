package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import java.util.HashSet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Powers extends AbstractEntity {

    @NotBlank(message = "Power name is required")
    @Size(min = 2, max = 80, message = "Power name must be 2-80 characters")
    private String powerName;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 200, message = "Description must be 5-200 characters")
    private String description;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Strength level is required")
    private String strenghtLvl;

    @NotBlank(message = "Rare status is required")
    private String isRare;

    @ManyToMany(mappedBy = "powers")
    private Set<Superhero> superheroes = new HashSet<>();

    public String getPowerName() {
        return powerName;
    }
    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStrenghtLvl() {
        return strenghtLvl;
    }
    public void setStrenghtLvl(String strenghtLvl) {
        this.strenghtLvl = strenghtLvl;
    }
    public String getIsRare() {
        return isRare;
    }
    public void setIsRare(String isRare) {
        this.isRare = isRare;
    }
    public Set<Superhero> getSuperheroes() {
        return superheroes;
    }

    public void setSuperheroes(Set<Superhero> superheroes) {
        this.superheroes = superheroes;
    }

}
