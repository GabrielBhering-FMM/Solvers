package com.example.solvers_login.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.HashMap;

public class Post implements Serializable {
    private String id;
    private String subject;
    private String description;
    private DocumentReference author;
    private boolean isAnswered;
    private Timestamp createdAt;
//    TODO: createdAt field (timestamp)

    public Post(HashMap<String,Object> hash){
        this.id = hash.get("id").toString();
        this.subject = hash.get("subject").toString();
        this.description = hash.get("description").toString();
        this.author = (DocumentReference) hash.get("author");
        this.isAnswered = Boolean.getBoolean(hash.get("isAnswered").toString());
        this.createdAt = (Timestamp) hash.get("createdAt");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentReference getAuthor() {
        return author;
    }

    public void setAuthor(DocumentReference author) {
        this.author = author;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
