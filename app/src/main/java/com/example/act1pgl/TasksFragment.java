package com.example.act1pgl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private List<Task> allTasks;
    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // 1. Datos de prueba iniciales
        allTasks = new ArrayList<>();
        allTasks.add(new Task("Doctors appointment", "Work"));
        allTasks.add(new Task("Buy groceries", "Personal"));
        allTasks.add(new Task("Finish project proposal", "Work"));

        // 2. Configurar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(new ArrayList<>(allTasks));
        recyclerView.setAdapter(adapter);

        // 3. Filtrado por Chips (All, Work, Personal)
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_filter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_all) {
                filterTasks("All");
            } else if (checkedId == R.id.chip_work) {
                filterTasks("Work");
            } else if (checkedId == R.id.chip_personal) {
                filterTasks("Personal");
            }
        });

        // 4. Botón Flotante (+)
        FloatingActionButton fab = view.findViewById(R.id.fab_add_task);
        fab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Agregar Tarea", Toast.LENGTH_SHORT).show();
            // Próximo paso: Abrir BottomSheetDialogFragment para crear tareas
        });

        return view;
    }

    private void filterTasks(String category) {
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
}