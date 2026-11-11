package com.example.act1pgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarFragment extends Fragment implements TaskAdapter.OnTaskActionListener {

    private TaskAdapter taskAdapter;
    private CalendarAdapter calendarAdapter;

    private TextView tvMonthYear;
    private Calendar currentCalendar;
    private String selectedDateFormatted = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvMonthYear = view.findViewById(R.id.tv_month_year);
        RecyclerView recyclerGrid = view.findViewById(R.id.recycler_calendar_grid);
        RecyclerView recyclerTasks = view.findViewById(R.id.recycler_calendar_tasks);

        currentCalendar = Calendar.getInstance();
        selectedDateFormatted = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));

        recyclerTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new ArrayList<>(), this);
        recyclerTasks.setAdapter(taskAdapter);

        recyclerGrid.setLayoutManager(new GridLayoutManager(getContext(), 7));

        setupCalendarGrid(recyclerGrid);
        filterTasksByDate(selectedDateFormatted);

        return view;
    }

    private void setupCalendarGrid(RecyclerView recyclerGrid) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        tvMonthYear.setText(monthYearFormat.format(currentCalendar.getTime()));

        List<String> daysInMonth = new ArrayList<>();
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add("");
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        for (int day = 1; day <= maxDays; day++) {
            daysInMonth.add(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day));
        }

        Set<String> datesWithTasks = getDatesWithTasks();

        calendarAdapter = new CalendarAdapter(daysInMonth, datesWithTasks, selectedDateFormatted, fullDate -> {
            selectedDateFormatted = fullDate;
            filterTasksByDate(fullDate);
        });
        recyclerGrid.setAdapter(calendarAdapter);
    }

    private Set<String> getDatesWithTasks() {
        List<Task> allTasks = TaskRepository.getInstance().getTasks();
        Set<String> dates = new HashSet<>();
        for (Task t : allTasks) {
            if (t.getDueDate() != null && !t.getDueDate().isEmpty()) {
                dates.add(t.getDueDate());
            }
        }
        return dates;
    }

    private void filterTasksByDate(String date) {
        List<Task> allTasks = TaskRepository.getInstance().getTasks();
        List<Task> filtered = new ArrayList<>();
        for (Task t : allTasks) {
            if (date.equals(t.getDueDate())) {
                filtered.add(t);
            }
        }
        taskAdapter.updateList(filtered);
    }

    @Override
    public void onTaskClick(Task task) {
        AddTaskBottomSheetFragment bottomSheet = new AddTaskBottomSheetFragment();
        bottomSheet.setTaskToEdit(task);
        bottomSheet.setOnTaskSaveListener(new AddTaskBottomSheetFragment.OnTaskSaveListener() {
            @Override
            public void onTaskSave(Task savedTask) {
                TaskRepository.getInstance().updateTask(savedTask);
                calendarAdapter.updateData(getDatesWithTasks(), selectedDateFormatted);
                filterTasksByDate(selectedDateFormatted);
            }

            @Override
            public void onTaskDelete(Task taskToDelete) {
                TaskRepository.getInstance().deleteTask(taskToDelete);
                calendarAdapter.updateData(getDatesWithTasks(), selectedDateFormatted);
                filterTasksByDate(selectedDateFormatted);
            }
        });
        bottomSheet.show(getChildFragmentManager(), "AddTaskBottomSheet");
    }

    @Override
    public void onTaskUpdated(Task task) {
        TaskRepository.getInstance().updateTask(task);
        calendarAdapter.updateData(getDatesWithTasks(), selectedDateFormatted);
    }
}