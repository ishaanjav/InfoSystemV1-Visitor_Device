package com.example.anany.informationsystemv1;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public  class Post {

    public String author;
    public String title;

    public Post(String author, String title) {
        // ...
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

    // Get a reference to our posts


