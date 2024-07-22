package com.example.CommunistDate.UserPreferences;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.example.CommunistDate.Users.User;

@Entity
@Table(name = "userpreferences")
public class UserPreferences {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "political_belief")
    private String politicalBelief;

    @Column(name = "gender")
    private String gender;

    @Column(name = "partner_share")
    private Boolean partnerShare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getPoliticalBelief() {
        return politicalBelief;
    }

    public void setPoliticalBelief(String politicalBelief) {
        this.politicalBelief = politicalBelief;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getPartnerShare() {
        return partnerShare;
    }

    public void setPartnerShare(Boolean partnerShare) {
        this.partnerShare = partnerShare;
    }
}
    

