package com.example.kienycolin_csc372_assignment4_civiladvocacy;

import java.io.Serializable;

public class Official implements Serializable {
    private String title, name, party;
    private String address;
    private String phone, website, email;
    private String[] channels;
    private String photoURL;

    Official(String title, String name, String party){
        this.title = title;
        this.name = name;
        this.party = party;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
