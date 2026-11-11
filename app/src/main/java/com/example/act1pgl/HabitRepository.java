package com.example.act1pgl;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    private static HabitRepository instance;
    private final List<Habit> habitList;
    private SharedPreferences prefs;

    private static final String PREF_NAME = "habit_app_prefs";
    private static final String KEY_HABITS = "key_habits";

    private HabitRepository(Context context) {
        habitList = new ArrayList<>();
        if (context != null) {
            prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            loadHabits();
        }
    }

    public static synchronized HabitRepository getInstance(Context context) {
        if (instance == null) {
            instance = new HabitRepository(context);
        }
        return instance;
    }

    public static synchronized HabitRepository getInstance() {
        return getInstance(null);
    }

    public List<Habit> getHabits() { return habitList; }

    public void addHabit(String title) {
        if (!title.trim().isEmpty()) {
            habitList.add(new Habit(title.trim()));
            saveHabits();
        }
    }

    public void deleteHabit(Habit habit) {
        habitList.removeIf(h -> h.getId().equals(habit.getId()));
        saveHabits();
    }

    public void updateHabitState() {
        saveHabits();
    }

    private void saveHabits() {
        if (prefs == null) return;
        try {
            JSONArray array = new JSONArray();
            for (Habit h : habitList) {
                JSONObject obj = new JSONObject();
                obj.put("title", h.getTitle());
                obj.put("isCompleted", h.isCompleted());
                array.put(obj);
            }
            prefs.edit().putString(KEY_HABITS, array.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHabits() {
        try {
            String habitString = prefs.getString(KEY_HABITS, null);
            if (habitString != null) {
                JSONArray array = new JSONArray(habitString);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Habit h = new Habit(obj.getString("title"));
                    h.setCompleted(obj.getBoolean("isCompleted"));
                    habitList.add(h);
                }
            } else {
                habitList.add(new Habit("Hacer la cama"));
                habitList.add(new Habit("Beber 2L de agua"));
                habitList.add(new Habit("Hacer 30 min de ejercicio"));
                habitList.add(new Habit("Leer 15 minutos"));
                habitList.add(new Habit("Meditar 10 minutos"));
                habitList.add(new Habit("Planificar el día"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}