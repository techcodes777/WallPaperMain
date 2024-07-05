package com.hdlight.wallpaperapps.model;

public class SignUp {

    String mobile;
    String password;
    String username;

    public SignUp() {

    }

    public SignUp(String mobile, String password, String username) {
        this.mobile = mobile;
        this.password = password;
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
