package com.example.act1pgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvTasks = view.findViewById(R.id.tv_stats_tasks);
        TextView tvHabits = view.findViewById(R.id.tv_stats_habits);
        TextView tvCategories = view.findViewById(R.id.tv_stats_categories);

        // 1. Calcular estadísticas de tareas
        List<Task> tasks = TaskRepository.getInstance().getTasks();
        int totalTasks = tasks.size();
        int completedTasks = 0;
        for (Task t : tasks) {
            if (t.isCompleted()) completedTasks++;
        }
        tvTasks.setText("Tareas completadas: " + completedTasks + " / " + totalTasks);

        // 2. Calcular estadísticas de hábitos
        List<Habit> habits = HabitRepository.getInstance().getHabits();
        int totalHabits = habits.size();
        int completedHabits = 0;
        for (Habit h : habits) {
            if (h.isCompleted()) completedHabits++;
        }
        tvHabits.setText("Hábitos cumplidos hoy: " + completedHabits + " / " + totalHabits);

        // 3. Cantidad de categorías
        int totalCategories = TaskRepository.getInstance().getCategories().size();
        tvCategories.setText("Categorías activas: " + totalCategories);

        return view;
    }
}