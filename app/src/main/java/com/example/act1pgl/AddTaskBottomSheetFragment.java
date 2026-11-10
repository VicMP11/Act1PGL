package com.example.act1pgl;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    // Interface para enviar la nueva tarea de vuelta a TasksFragment
    public interface OnTaskAddedListener {
        void onTaskAdded(Task task);
    }

    private OnTaskAddedListener listener;
    private List<String> subtasksList = new ArrayList<>();
    private String selectedCategory = "Work";

    public void setOnTaskAddedListener(OnTaskAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_task, container, false);

        EditText etTitle = view.findViewById(R.id.et_task_title);
        EditText etSubtaskInput = view.findViewById(R.id.et_subtask_input);
        ImageButton btnAddSubtask = view.findViewById(R.id.btn_add_subtask);
        LinearLayout containerSubtasks = view.findViewById(R.id.container_subtasks);
        TextView tvCategory = view.findViewById(R.id.tv_category_selector);
        FloatingActionButton fabSave = view.findViewById(R.id.fab_save_task);

        // Alternar categoría al hacer clic (Work <-> Personal)
        tvCategory.setOnClickListener(v -> {
            if (selectedCategory.equals("Work")) {
                selectedCategory = "Personal";
            } else {
                selectedCategory = "Work";
            }
            tvCategory.setText(selectedCategory);
        });

        // Botón para agregar subtarea a la vista
        // Fragmento dentro de btnAddSubtask.setOnClickListener en AddTaskBottomSheetFragment.java
        btnAddSubtask.setOnClickListener(v -> {
            String subtaskText = etSubtaskInput.getText().toString().trim();
            if (!TextUtils.isEmpty(subtaskText)) {
                subtasksList.add(subtaskText);

                View subtaskView = inflater.inflate(R.layout.item_subtask_row, containerSubtasks, false);
                TextView tvText = subtaskView.findViewById(R.id.tv_subtask_text);
                ImageButton btnRemove = subtaskView.findViewById(R.id.btn_remove_subtask);

                tvText.setText(subtaskText);

                // Guardar referencia al texto correspondiente
                btnRemove.setOnClickListener(subView -> {
                    containerSubtasks.removeView(subtaskView);
                    subtasksList.remove(subtaskText);
                });

                containerSubtasks.addView(subtaskView);
                etSubtaskInput.setText("");
            }
        });

        // Guardar la tarea completa
        fabSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), "Por favor escribe el título de la tarea", Toast.LENGTH_SHORT).show();
                return;
            }

            Task newTask = new Task(title, selectedCategory);
            for (String st : subtasksList) {
                newTask.addSubtask(st);
            }

            if (listener != null) {
                listener.onTaskAdded(newTask);
            }
            dismiss(); // Cerrar el modal
        });

        return view;
    }
}