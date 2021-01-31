package com.example.firestore1;

public class Note {
    private String title;
    private String description;

    public Note(){
        // public no-arg constructor needed
    }

    public Note(String title, String description ){
        this.title = title;
        this. description = description;
    }

    // setelah get namanya harus sama dengan variable yg akan di return
    // agar data dapat mengambil data yang seharusnya
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }

}
