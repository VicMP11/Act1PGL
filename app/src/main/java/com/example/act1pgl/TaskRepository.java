package com.example.act1pgl;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<Task> taskList;
    private final List<String> categoryList;
    private SharedPreferences prefs;

    private static final String PREF_NAME = "task_app_prefs";
    private static final String KEY_TASKS = "key_tasks";
    private static final String KEY_CATEGORIES = "key_categories";

    private TaskRepository(Context context) {
        taskList = new ArrayList<>();
        categoryList = new ArrayList<>();
        if (context != null) {
            prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            loadData();
        }
    }

    public static synchronized TaskRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TaskRepository(context);
        }
        return instance;
    }

    public static synchronized TaskRepository getInstance() {
        return getInstance(null);
    }

    public List<Task> getTasks() { return taskList; }

    public void addTask(Task task) {
        taskList.add(0, task);
        saveData();
    }

    public void deleteTask(Task task) {
        taskList.removeIf(t -> t.getId().equals(task.getId()));
        saveData();
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId().equals(updatedTask.getId())) {
                taskList.set(i, updatedTask);
                break;
            }
        }
        saveData();
    }

    public List<String> getCategories() { return categoryList; }

    public void addCategory(String category) {
        if (!categoryList.contains(category) && !category.trim().isEmpty()) {
            categoryList.add(category.trim());
            saveData();
        }
    }

    public void deleteCategory(String category) {
        categoryList.remove(category);
        saveData();
    }

    // --- GUARDADO Y CARGA EN DISCO ---
    private void saveData() {
        if (prefs == null) return;
        try {
            // Guardar Tareas
            JSONArray tasksArray = new JSONArray();
            for (Task t : taskList) {
                JSONObject tObj = new JSONObject();
                tObj.put("id", t.getId());
                tObj.put("title", t.getTitle());
                tObj.put("category", t.getCategory());
                tObj.put("dueDate", t.getDueDate());
                tObj.put("isCompleted", t.isCompleted());

                JSONArray subArray = new JSONArray();
                for (Subtask st : t.getSubtasks()) {
                    JSONObject stObj = new JSONObject();
                    stObj.put("title", st.getTitle());
                    stObj.put("isCompleted", st.isCompleted());
                    subArray.put(stObj);
                }
                tObj.put("subtasks", subArray);
                tasksArray.put(tObj);
            }

            // Guardar Categorías
            JSONArray catArray = new JSONArray();
            for (String cat : categoryList) {
                catArray.put(cat);
            }

            prefs.edit()
                    .putString(KEY_TASKS, tasksArray.toString())
                    .putString(KEY_CATEGORIES, catArray.toString())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            String catString = prefs.getString(KEY_CATEGORIES, null);
            if (catString != null) {
                JSONArray catArray = new JSONArray(catString);
                for (int i = 0; i < catArray.length(); i++) {
                    categoryList.add(catArray.getString(i));
                }
            } else {
                categoryList.add("Work");
                categoryList.add("Personal");
            }

            String taskString = prefs.getString(KEY_TASKS, null);
            if (taskString != null) {
                JSONArray tasksArray = new JSONArray(taskString);
                for (int i = 0; i < tasksArray.length(); i++) {
                    JSONObject tObj = tasksArray.getJSONObject(i);
                    Task t = new Task(tObj.getString("title"), tObj.getString("category"));
                    t.setDueDate(tObj.optString("dueDate", ""));
                    t.setCompleted(tObj.getBoolean("isCompleted"));

                    JSONArray subArray = tObj.optJSONArray("subtasks");
                    if (subArray != null) {
                        for (int j = 0; j < subArray.length(); j++) {
                            JSONObject stObj = subArray.getJSONObject(j);
                            t.addSubtask(new Subtask(stObj.getString("title"), stObj.getBoolean("isCompleted")));
                        }
                    }
                    taskList.add(t);
                }
            } else {
                // Datos de prueba iniciales la primera vez que se abre la app
                Task t1 = new Task("Doctors appointment", "Work");
                t1.addSubtask(new Subtask("Confirm timing"));
                Task t2 = new Task("Buy groceries", "Personal");
                t2.addSubtask(new Subtask("Milk"));
                taskList.add(t1);
                taskList.add(t2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}