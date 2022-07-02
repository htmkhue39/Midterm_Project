package com.example.midterm_project.Domain;

import java.io.Serializable;

public class Profile implements Serializable {
    private String avatar, name, address, phone, email;

    public Profile() {

    }

    public Profile(String avatar, String name, String address, String phone, String email) {
        this.avatar = avatar;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
