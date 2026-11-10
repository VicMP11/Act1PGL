package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String title;
    private String category;
    private boolean isCompleted;
    private List<String> subtasks;

    public Task(String title, String category) {
        this.title = title;
        this.category = category;
        this.isCompleted = false;
        this.subtasks = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public List<String> getSubtasks() { return subtasks; }
    public void addSubtask(String subtask) { this.subtasks.add(subtask); }
}
