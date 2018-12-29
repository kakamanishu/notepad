package com.example.admin.saveourlife;


import org.litepal.crud.LitePalSupport;

public class UserList extends LitePalSupport {
    private String account;
    private String password;
    public String getAccount(){
        return account;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
