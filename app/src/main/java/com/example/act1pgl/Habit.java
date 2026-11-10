package com.example.act1pgl;

import java.util.UUID;

public class Habit {
    private String id;
    private String title;
    private boolean isCompleted;

    public Habit(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.isCompleted = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}