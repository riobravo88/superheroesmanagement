package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Superhero extends AbstractEntity {

    private String name;
    private String spname;
    private String occupation;
    private String homeCity;
    private String universe;
    private String firstApperiance;

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

}
