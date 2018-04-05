package com.example.liuxuanchi.project.login;


import org.litepal.crud.DataSupport;

public class User extends DataSupport {
    private int id;
    private String account;
    private String phone;
    private String password;

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getAccount(){
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String setPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

