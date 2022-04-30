package com.example.ahand;

public class User {
    String usrImage;
    String fullName;
    String location;
    String phone;
    String email;
    String password;
    String repeatPassword;

    public User(String usrImage, String fullName, String location, String phone, String email, String password, String repeatPassword) {
        this.usrImage = usrImage;
        this.fullName = fullName;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public User(String usrImage, String fullName, String location, String phone, String email) {
        this.usrImage = usrImage;
        this.fullName = fullName;
        this.location = location;
        this.phone = phone;
        this.email = email;
    }

    public User() {
    }

    public String getUsrImage() {
        return usrImage;
    }

    public void setUsrImage(String usrImage) {
        this.usrImage = usrImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
