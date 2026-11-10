package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<Task> taskList;
    private final List<String> categoryList;

    private TaskRepository() {
        taskList = new ArrayList<>();
        categoryList = new ArrayList<>();

        // Categorías por defecto
        categoryList.add("Work");
        categoryList.add("Personal");

        // Datos iniciales de prueba
        Task t1 = new Task("Doctors appointment", "Work");
        t1.addSubtask(new Subtask("Confirm timing"));
        t1.addSubtask(new Subtask("Bring medical history"));

        Task t2 = new Task("Buy groceries", "Personal");
        t2.addSubtask(new Subtask("Milk"));
        t2.addSubtask(new Subtask("Bread"));

        Task t3 = new Task("Finish project proposal", "Work");

        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);
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
        taskList.add(0, task);
    }

    public void deleteTask(Task task) {
        taskList.removeIf(t -> t.getId().equals(task.getId()));
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId().equals(updatedTask.getId())) {
                taskList.set(i, updatedTask);
                break;
            }
        }
    }

    // --- GESTIÓN DE CATEGORÍAS ---
    public List<String> getCategories() {
        return categoryList;
    }

    public void addCategory(String category) {
        if (!categoryList.contains(category) && !category.trim().isEmpty()) {
            categoryList.add(category.trim());
        }
    }
    public void deleteCategory(String category) {
        categoryList.remove(category);
    }
}