package com.example.act1pgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment implements TaskAdapter.OnTaskActionListener {

    private TaskAdapter adapter;
    private String currentCategoryFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        List<Task> allTasks = TaskRepository.getInstance().getTasks();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(new ArrayList<>(allTasks), this);
        recyclerView.setAdapter(adapter);

        filterTasks(currentCategoryFilter);

        ChipGroup chipGroup = view.findViewById(R.id.chip_group_filter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_all) {
                currentCategoryFilter = "All";
            } else if (checkedId == R.id.chip_work) {
                currentCategoryFilter = "Work";
            } else if (checkedId == R.id.chip_personal) {
                currentCategoryFilter = "Personal";
            }
            filterTasks(currentCategoryFilter);
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_task);
        fab.setOnClickListener(v -> openTaskModal(null));

        return view;
    }

    private void openTaskModal(@Nullable Task taskToEdit) {
        AddTaskBottomSheetFragment bottomSheet = new AddTaskBottomSheetFragment();
        if (taskToEdit != null) {
            bottomSheet.setTaskToEdit(taskToEdit);
        }
        bottomSheet.setOnTaskSaveListener(new AddTaskBottomSheetFragment.OnTaskSaveListener() {
            @Override
            public void onTaskSave(Task savedTask) {
                if (taskToEdit == null) {
                    TaskRepository.getInstance().addTask(savedTask);
                } else {
                    TaskRepository.getInstance().updateTask(savedTask);
                }
                filterTasks(currentCategoryFilter);
            }

            @Override
            public void onTaskDelete(Task taskToDelete) {
                TaskRepository.getInstance().deleteTask(taskToDelete);
                filterTasks(currentCategoryFilter);
            }
        });
        bottomSheet.show(getChildFragmentManager(), "AddTaskBottomSheet");
    }

    private void filterTasks(String category) {
        List<Task> allTasks = TaskRepository.getInstance().getTasks();
        if (category.equals("All")) {
            adapter.updateList(new ArrayList<>(allTasks));
        } else {
            List<Task> filtered = new ArrayList<>();
            for (Task t : allTasks) {
                if (t.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(t);
                }
            }
            adapter.updateList(filtered);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        openTaskModal(task);
    }

    @Override
    public void onTaskUpdated(Task task) {
        TaskRepository.getInstance().updateTask(task);
    }
}