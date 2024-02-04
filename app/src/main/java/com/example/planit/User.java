package com.example.planit;

import java.util.Date;

public class User {
    public String userid;
    public String name;
    public Integer age;
    public String email;
    public String location;
    //streak
    public Integer streak;
    public Integer longestStreak;
    public Date lastStreakDate;
    public User() {};
    public String profilePictureUrl;

    public User(String userid, String name, Integer age, String email, String location) {
        this.userid = userid;
        this.name = name;
        this.age = age;
        this.email = email;
        this.location = location;
        this.streak = 0;
        this.longestStreak = 6;
        this.lastStreakDate = new Date();
        this.profilePictureUrl="users/"+userid+"/profile.jpg";

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public Integer getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }

    public Date getLastStreakDate() {
        return lastStreakDate;
    }

    public void setLastStreakDate(Date lastStreakDate) {
        this.lastStreakDate = lastStreakDate;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}