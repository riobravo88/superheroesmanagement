package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Powers extends AbstractEntity {

    private String powerName;
    private String description;
    private String type;
    private String strenghtLvl;
    private String isRare;

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

}
