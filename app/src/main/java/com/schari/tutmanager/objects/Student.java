package com.schari.tutmanager.objects;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {

    private String id;
    private String name;
    private String school;
    private int standard;
    private String address;
    private String fatherName;
    private String motherName;
    private String dateOfBirth;
    private String dateOfJoining;
    private String personalContact;
    private String parentContact;
    private String email;
    private String previousScore;
    private boolean fresher;
    private ArrayList<String> difficultSubjects;
    private String profileImage;

    public Student() {

    }

    public Student(String name, String school, int  standard, String address,
                   String fatherName, String motherName, String dateOfBirth,
                   String dateOfJoining, String personalContact, String parentContact,
                   String email, String previousScore, boolean fresher,
                   ArrayList<String> difficultSubjects) {
        this.name = name;
        this.school = school;
        this.standard = standard;
        this.address = address;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoining = dateOfJoining;
        this.personalContact = personalContact;
        this.parentContact = parentContact;
        this.email = email;
        this.previousScore = previousScore;
        this.fresher = fresher;
        this.difficultSubjects = difficultSubjects;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public int getStandard() {
        return standard;
    }

    public String getAddress() {
        return address;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public String getPersonalContact() {
        return personalContact;
    }

    public String getParentContact() {
        return parentContact;
    }

    public String getEmail() {
        return email;
    }

    public String getPreviousScore() {
        return previousScore;
    }

    public boolean isFresher() {
        return fresher;
    }

    public ArrayList<String> getDifficultSubjects() {
        return difficultSubjects;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
