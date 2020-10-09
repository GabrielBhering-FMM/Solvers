package com.example.solvers.models;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private String uid;
    private String name;
    private String imageUrl;
    private String email;

    public User(HashMap<String,Object> user){
        this.uid = user.get("uid").toString();
        this.name = user.get("displayName").toString();
        this.email = user.get("email").toString();
        this.imageUrl = user.get("imageUrl").toString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
