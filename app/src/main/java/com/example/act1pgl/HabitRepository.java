package com.example.act1pgl;

import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    private static HabitRepository instance;
    private final List<Habit> habitList;

    private HabitRepository() {
        habitList = new ArrayList<>();
        // 6 hábitos básicos del día por defecto
        habitList.add(new Habit("Hacer la cama"));
        habitList.add(new Habit("Beber 2L de agua"));
        habitList.add(new Habit("Hacer 30 min de ejercicio"));
        habitList.add(new Habit("Leer 15 minutos"));
        habitList.add(new Habit("Meditar 10 minutos"));
        habitList.add(new Habit("Planificar el día"));
    }

    public static synchronized HabitRepository getInstance() {
        if (instance == null) {
            instance = new HabitRepository();
        }
        return instance;
    }

    public List<Habit> getHabits() {
        return habitList;
    }

    public void addHabit(String title) {
        if (!title.trim().isEmpty()) {
            habitList.add(new Habit(title.trim()));
        }
    }

    public void deleteHabit(Habit habit) {
        habitList.removeIf(h -> h.getId().equals(habit.getId()));
    }
}