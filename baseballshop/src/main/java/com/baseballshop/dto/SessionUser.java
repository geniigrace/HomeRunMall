package com.baseballshop.dto;

import com.baseballshop.entity.Member;

import java.io.Serializable;

public class SessionUser implements Serializable { //Serializable : 이걸 상속받았기 때문에 http에서 사용가능
    private String name;

    private String email;

    private String picture;

    public SessionUser(Member member){
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }

    public SessionUser(){

    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPicture(){
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }


}
