package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class HeroProfile extends AbstractEntity {

    @OneToOne(mappedBy = "heroProfile")
    private Superhero superhero;

    @NotBlank(message = "Weakness is required")
    @Size(min = 2, max = 80, message = "Weakness must be 2-80 characters")
    private String weakness;

    @NotBlank(message = "Threat level is required")
    private String threatLvl;

    @NotBlank(message = "Mentor is required")
    private String mentor;

    @NotBlank(message = "Secret base is required")
    private String secretBase;

    @NotBlank(message = "First mission is required")
    private String firstMission;

    // GETTERS & SETTERS

    public Superhero getSuperhero() {
        return superhero;
    }

    public void setSuperhero(Superhero superhero) {
        this.superhero = superhero;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getThreatLvl() {
        return threatLvl;
    }

    public void setThreatLvl(String threatLvl) {
        this.threatLvl = threatLvl;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getSecretBase() {
        return secretBase;
    }

    public void setSecretBase(String secretBase) {
        this.secretBase = secretBase;
    }

    public String getFirstMission() {
        return firstMission;
    }

    public void setFirstMission(String firstMission) {
        this.firstMission = firstMission;
    }
}