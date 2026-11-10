package com.example.act1pgl;

public class Subtask {
    private String title;
    private boolean isCompleted;

    public Subtask(String title) {
        this.title = title;
        this.isCompleted = false;
    }

    public Subtask(String title, boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}