package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<Task> taskList;

    private TaskRepository() {
        taskList = new ArrayList<>();

        // Datos iniciales de prueba con subtareas
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
}