package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String category;
    private boolean isCompleted;
    private List<Subtask> subtasks;

    public Task(String title, String category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.category = category;
        this.isCompleted = false;
        this.subtasks = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public List<Subtask> getSubtasks() { return subtasks; }
    public void setSubtasks(List<Subtask> subtasks) { this.subtasks = subtasks; }
    public void addSubtask(Subtask subtask) { this.subtasks.add(subtask); }
}