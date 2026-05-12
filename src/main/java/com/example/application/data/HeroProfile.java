package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class HeroProfile extends AbstractEntity {

    private String weakness;
    private String threatLvl;
    private String mentor;
    private String secretBase;
    private String firstMission;

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
