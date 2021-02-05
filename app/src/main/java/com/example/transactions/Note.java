package com.example.transactions;

import com.google.firebase.firestore.Exclude;

import java.util.List;
import java.util.Map;

public class Note {
    // TODO TRANSACTION
//    private String idDocument;
//    private String title;
//    private String description;
//    private int priority;
//
//    public Note(){
//        // public no-arg constructor needed
//    }
//
//    public Note(String title, String description, int priority){
//        this.title = title;
//        this. description = description;
//        this.priority = priority;
//    }
//    @Exclude
//    public String getIdDocument() {
//        return idDocument;
//    }
//
//    public void setIdDocument(String idDocument) {
//        this.idDocument = idDocument;
//    }
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public String getTitle(){
//        return title;
//    }
//    public String getDescription(){
//        return description;
//    }

    // TODO ARRAYS Firebase
//    private String idDocument;
//    private String title;
//    private String description;
//    private int priority;
//    private List<String> tags;
//
//    public Note(){
//        // public no-arg constructor needed
//    }
//
//    public Note(String title, String description, int priority, List<String> tags){
//        this.title = title;
//        this. description = description;
//        this.priority = priority;
//        this.tags = tags;
//    }
//    @Exclude
//    public String getIdDocument() {
//        return idDocument;
//    }
//
//    public void setIdDocument(String idDocument) {
//        this.idDocument = idDocument;
//    }
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public String getTitle(){
//        return title;
//    }
//    public String getDescription(){
//        return description;
//    }
//
//    public List<String> getTags() {
//        return tags;
//    }

    // TODO NESTED OBJECT
    private String idDocument;
    private String title;
    private String description;
    private int priority;
    private Map<String,Boolean> tags;

    // cara membuat map bersarang
//    private Map<String,Map<String,Map<String,Boolean>>> tags;

    public Note(){
        // public no-arg constructor needed
    }

    public Note(String title, String description, int priority,Map<String,Boolean> tags){
        this.title = title;
        this. description = description;
        this.priority = priority;
        this.tags = tags;
    }
    @Exclude
    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public int getPriority() {
        return priority;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }

    public Map<String,Boolean> getTags() {
        return tags;
    }
}