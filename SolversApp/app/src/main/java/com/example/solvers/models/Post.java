package com.example.solvers.models;

public class Post {
    private String title;
    private String description;

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return "Titulo: "+getTitle()+"\nDescricao: "+ getDescription();
    }
}
