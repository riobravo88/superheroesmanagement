package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Publishers extends AbstractEntity {

    private String publisherName;
    private String country;
    private String foundedYear;
    private String ceo;
    private String universe;

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

}
