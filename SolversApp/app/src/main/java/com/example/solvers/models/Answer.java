package com.example.solvers.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.HashMap;

public class Answer implements Serializable {
    private String id;
    private String text;
    private DocumentReference postRef;
    private DocumentReference authorRef;
    private Timestamp createdAt;

    public Answer(HashMap<String,Object> hash){
        this.id = hash.get("id").toString();
        this.authorRef = (DocumentReference) hash.get("author");
        this.text = hash.get("text").toString();
        this.postRef = (DocumentReference) hash.get("post");
        this.createdAt = (Timestamp) hash.get("createdAt");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getAuthorRef() {
        return authorRef;
    }

    public void setAuthorRef(DocumentReference author) {
        authorRef = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DocumentReference getPostRef() {
        return postRef;
    }

    public void setPostRef(DocumentReference post) {
        this.postRef = post;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
