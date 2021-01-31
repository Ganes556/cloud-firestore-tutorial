package com.example.firebase_learning_queris;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String idDocument;
    private String title;
    private String description;
    private int priority;

    public Note(){

    }
    public Note(String title, String description, int priority){
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Exclude
    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

}
