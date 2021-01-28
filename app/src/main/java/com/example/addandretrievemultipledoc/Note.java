package com.example.addandretrievemultipledoc;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String idDocument;
    private String title;
    private String description;

    public Note(){

    }
    public Note(String title, String description){
        this.title = title;
        this.description = description;
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
}
