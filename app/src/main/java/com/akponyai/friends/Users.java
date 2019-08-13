package com.akponyai.friends;

public class Users {

    String username;
    String password;

    public Users(){

    }

    public Users(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
