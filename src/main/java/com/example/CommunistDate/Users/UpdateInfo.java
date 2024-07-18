package com.example.CommunistDate.Users;

import jakarta.annotation.Nullable;

public class UpdateInfo {
    @Nullable
    private String gender;

    @Nullable
    private String city;

    @Nullable
    private String countryOfResidence;

    @Nullable
    private String language;

    @Nullable
    private String politicalBelief;

    @Nullable
    private Integer communismLevel;

    @Nullable
    private boolean partnerShare;

    @Nullable
    private String description;

    public UpdateInfo() {}

    public UpdateInfo(String gender, String city, String countryOfResidence, String language, String politicalBelief, Integer communismLevel, boolean partnerShare, String description) {
        this.gender = gender;
        this.city = city;        
        this.countryOfResidence = countryOfResidence;
        this.language = language;
        this.politicalBelief = politicalBelief;
        this.communismLevel = communismLevel;
        this.partnerShare = partnerShare;
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPoliticalBelief() {
        return politicalBelief;
    }

    public void setPoliticalBelief(String politicalBelief) {
        this.politicalBelief = politicalBelief;
    }

    public Integer getCommunismLevel() {
        return communismLevel;
    }

    public void setCommunismLevel(Integer communismLevel) {
        this.communismLevel = communismLevel;
    }

    public boolean getPartnerShare() {
        return partnerShare;
    }

    public void setPartnerShare(boolean partnerShare) {
        this.partnerShare = partnerShare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
