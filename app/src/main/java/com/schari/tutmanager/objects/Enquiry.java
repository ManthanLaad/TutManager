package com.schari.tutmanager.objects;

import com.google.firebase.database.Exclude;

public class Enquiry {

    private String name;
    private String school;
    private String address;
    private String standard;
    private String id;

    public Enquiry() {

    }

    public Enquiry(String name, String school, String address, String standard) {
        this.name = name;
        this.school = school;
        this.address = address;
        this.standard = standard;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public String getAddress() {
        return address;
    }

    public String getStandard() {
        return standard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
