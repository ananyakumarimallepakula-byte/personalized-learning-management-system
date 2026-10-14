package com.personalizedlearning.dto;

import java.time.LocalDateTime;

public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
    private String collegeName;
    private String course;
    private Integer yearOfStudy;
    private String profileImage;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, String role, String phone, Boolean active,
                   LocalDateTime createdAt, String collegeName, String course, Integer yearOfStudy, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
        this.collegeName = collegeName;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.profileImage = profileImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
