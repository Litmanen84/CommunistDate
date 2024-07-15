package com.example.CommunistDate.Users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Objects;


@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "sexual_orientation", nullable = true)
    private String sexualOrientation;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "country_of_residence", nullable = false)
    private String countryOfResidence;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "second_language", nullable = true)
    private String secondLanguage;

    @Column(name = "third_language", nullable = true)
    private String thirdLanguage;
    
    @Column(name = "fourth_language", nullable = true)
    private String fourthLanguage;

    @Column(name = "political_belief", nullable = false)
    private String politicalBelief;

    @Column(name = "communism_level", nullable = true)
    private Integer communismLevel;

    @Column(name = "partner_share", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean partnerShare;

    @Column(name = "biography", nullable = true)
    private String biography;

    @Column(name = "communism_relationship", nullable = true)
    private String communismRelationship;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @Column(name = "isadmin", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isAdmin;

    public User() {}

    public User(String username, String password, String email, String gender, String sexualOrientation, String city, String nationality, int age, String countryOfResidence, String language, String second_language, String third_language, String fourth_language, String politicalBelief, Integer communismLevel, boolean partnerShare, String biography, String communismRelationship, String profilePicture, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.sexualOrientation = sexualOrientation;
        this.city = city;
        this.nationality = nationality;
        this.age = age;
        this.countryOfResidence = countryOfResidence;
        this.language = language;
        this.secondLanguage = second_language;
        this.thirdLanguage = third_language;
        this.fourthLanguage = fourth_language;
        this.politicalBelief = politicalBelief;
        this.communismLevel = communismLevel;
        this.partnerShare = partnerShare;
        this.biography = biography;
        this.communismRelationship = communismRelationship;
        this.profilePicture = profilePicture;
        this.isAdmin = isAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(this.id, user.id) && Objects.equals(this.username, user.username) && Objects.equals(this.password, user.password) && Objects.equals(this.email, user.email)
                && Objects.equals(this.gender, user.gender) && Objects.equals(this.sexualOrientation, user.sexualOrientation) && Objects.equals(this.city, user.city) && Objects.equals(this.nationality, user.nationality)
                && Objects.equals(this.age, user.age) && Objects.equals(this.countryOfResidence, user.countryOfResidence) && Objects.equals(this.language, user.language)   
                && Objects.equals(this.secondLanguage, user.secondLanguage) && Objects.equals(this.thirdLanguage, user.thirdLanguage) && Objects.equals(this.fourthLanguage, user.fourthLanguage)   
                && Objects.equals(this.politicalBelief, user.politicalBelief) && Objects.equals(this.communismLevel, user.communismLevel) && Objects.equals(this.partnerShare, user.partnerShare)   
                && Objects.equals(this.biography, user.biography) && Objects.equals(this.communismRelationship, user.communismRelationship) && Objects.equals(this.profilePicture, user.profilePicture) 
                && Objects.equals(this.isAdmin, user.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.password, this.email, this.gender, this.sexualOrientation, this.city, this.nationality, this.age, this.countryOfResidence, this.language, this.secondLanguage, this.thirdLanguage, this.fourthLanguage, this.politicalBelief, this.communismLevel, this.partnerShare, this.biography, this.communismRelationship, this.profilePicture, this.isAdmin);   
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", username='" + this.username + '\'' + ", email='" + this.email + '\'' + ", gender='" + this.gender +'\'' + ", sexual orientation'" + this.sexualOrientation + + '\'' + ", password='" + this.password + '\'' + ", city='" + this.city + '\'' + ", nationaality='" + this.nationality + '\'' + ", age='" + this.age + '\'' + ", countryOfResidence='" + this.countryOfResidence + '\'' + ", language='" + this.language + '\'' + ", secondLanguage='" + this.secondLanguage + '\'' + ", thirdLanguage='" + this.thirdLanguage + '\'' + ", fourthLanguage='" + this.fourthLanguage + '\'' + ", politicalBelief='" + this.politicalBelief + '\'' + ", communismLevel='" + this.communismLevel + '\'' + ", partnerShare='" + this.partnerShare + '\'' + ", biography='" + this.biography + '\'' + ", communismRelationship='" + this.communismRelationship + '\'' + ", profilePicture='" + this.profilePicture + '\'' + ", isAdmin='" + this.isAdmin + '\'' + '}';
    }     

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
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

public String getSexualOrientation() {
    return sexualOrientation;
}

public void setSexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
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

public String getSecondLanguage() {
    return secondLanguage;
}   

public void setSecondLanguage(String secondLanguage) {
    this.secondLanguage = secondLanguage;
}

public String getThirdLanguage() {
    return thirdLanguage;
}

public void setThirdLanguage(String thirdLanguage) {
    this.thirdLanguage = thirdLanguage;
}

public String getFourthLanguage() {
    return fourthLanguage;
}

public void setFourthLanguage(String fourthLanguage) {
    this.fourthLanguage = fourthLanguage;
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

public String getBiography() {
    return biography;
}

public void setBiography(String biography) {
    this.biography = biography;
}

public String getCommunismRelationship() {
    return communismRelationship;
}

public void setCommunismRelationship(String communismRelationship) {
    this.communismRelationship = communismRelationship;
}

public String getProfilePicture() {
    return profilePicture;
}

public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
}

public boolean getIsAdmin() {
    return isAdmin;
}

public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
}
}

