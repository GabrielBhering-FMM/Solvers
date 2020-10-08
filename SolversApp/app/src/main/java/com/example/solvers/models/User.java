package com.example.solvers.models;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private String uid;
    private String name;

    public User(HashMap<String,Object> user){
        this.uid = user.get("uid").toString();
        this.name = user.get("name").toString();
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
}
