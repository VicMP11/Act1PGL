package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<Task> taskList;

    private TaskRepository() {
        taskList = new ArrayList<>();
        // Datos iniciales de prueba
        taskList.add(new Task("Doctors appointment", "Work"));
        taskList.add(new Task("Buy groceries", "Personal"));
        taskList.add(new Task("Finish project proposal", "Work"));
    }

    public static synchronized TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public List<Task> getTasks() {
        return taskList;
    }

    public void addTask(Task task) {
        taskList.add(0, task); // Añadir al inicio
    }
}