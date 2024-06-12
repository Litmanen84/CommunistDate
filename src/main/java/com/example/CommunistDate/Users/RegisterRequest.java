package com.example.CommunistDate.Users;
import java.util.Optional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @Size(min = 5, max = 25)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter and one number")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    private String gender;

    @NotBlank
    private String city;

    @NotBlank
    private String nationality;

    @Min(value=18, message="You must be at least 18 years old to be a true communist and join our community")
    private int age;

    @NotBlank
    private String countryOfResidence;

    @NotBlank
    private String language;

    @NotBlank
    private String politicalBelief;

    @Nullable
    private Integer communismLevel;

    private boolean partnerShare;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String email, String gender, String city, String nationality, int age, String countryOfResidence, String language, String politicalBelief, Integer communismLevel, boolean partnerShare) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.city = city;
        this.nationality = nationality;
        this.age = age;
        this.countryOfResidence = countryOfResidence;
        this.language = language;
        this.politicalBelief = politicalBelief;
        this.communismLevel = communismLevel;
        this.partnerShare = partnerShare;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;               
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public Optional<Integer> getCommunismLevel() {
        return Optional.ofNullable(communismLevel);
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
}
