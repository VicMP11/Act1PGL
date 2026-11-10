package com.example.act1pgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements TaskAdapter.OnTaskActionListener {

    private TaskAdapter adapter;
    private String selectedDateFormatted = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendar_view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_calendar_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Fecha actual por defecto
        Calendar today = Calendar.getInstance();
        selectedDateFormatted = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        filterTasksByDate(selectedDateFormatted);

        // Al cambiar de fecha en el calendario
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDateFormatted = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            filterTasksByDate(selectedDateFormatted);
        });

        return view;
    }

    private void filterTasksByDate(String date) {
        List<Task> allTasks = TaskRepository.getInstance().getTasks();
        List<Task> filtered = new ArrayList<>();
        for (Task t : allTasks) {
            if (date.equals(t.getDueDate())) {
                filtered.add(t);
            }
        }
        adapter.updateList(filtered);
    }

    @Override
    public void onTaskClick(Task task) {
        AddTaskBottomSheetFragment bottomSheet = new AddTaskBottomSheetFragment();
        bottomSheet.setTaskToEdit(task);
        bottomSheet.setOnTaskSaveListener(new AddTaskBottomSheetFragment.OnTaskSaveListener() {
            @Override
            public void onTaskSave(Task savedTask) {
                TaskRepository.getInstance().updateTask(savedTask);
                filterTasksByDate(selectedDateFormatted);
            }

            @Override
            public void onTaskDelete(Task taskToDelete) {
                TaskRepository.getInstance().deleteTask(taskToDelete);
                filterTasksByDate(selectedDateFormatted);
            }
        });
        bottomSheet.show(getChildFragmentManager(), "AddTaskBottomSheet");
    }

    @Override
    public void onTaskUpdated(Task task) {
        TaskRepository.getInstance().updateTask(task);
    }
}