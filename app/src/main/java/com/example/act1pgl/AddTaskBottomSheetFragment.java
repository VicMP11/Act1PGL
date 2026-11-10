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

    public interface OnTaskSaveListener {
        void onTaskSave(Task task);
        void onTaskDelete(Task task);
    }

    private OnTaskSaveListener listener;
    private Task taskToEdit;
    private List<Subtask> subtasksList = new ArrayList<>();
    private String selectedCategory = "Work";

    public void setOnTaskSaveListener(OnTaskSaveListener listener) {
        this.listener = listener;
    }

    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;
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
        ImageButton btnDelete = view.findViewById(R.id.btn_delete_task);
        FloatingActionButton fabSave = view.findViewById(R.id.fab_save_task);

        // Si estamos editando, mostramos datos y botón de eliminar
        if (taskToEdit != null) {
            etTitle.setText(taskToEdit.getTitle());
            selectedCategory = taskToEdit.getCategory();
            tvCategory.setText(selectedCategory);
            btnDelete.setVisibility(View.VISIBLE);

            for (Subtask st : taskToEdit.getSubtasks()) {
                subtasksList.add(new Subtask(st.getTitle(), st.isCompleted()));
                addSubtaskRowView(inflater, containerSubtasks, st.getTitle());
            }
        }

        // Alternar categoría
        tvCategory.setOnClickListener(v -> {
            selectedCategory = selectedCategory.equals("Work") ? "Personal" : "Work";
            tvCategory.setText(selectedCategory);
        });

        // Botón añadir subtarea
        btnAddSubtask.setOnClickListener(v -> {
            String subtaskText = etSubtaskInput.getText().toString().trim();
            if (!TextUtils.isEmpty(subtaskText)) {
                subtasksList.add(new Subtask(subtaskText));
                addSubtaskRowView(inflater, containerSubtasks, subtaskText);
                etSubtaskInput.setText("");
            }
        });

        // Botón Eliminar
        btnDelete.setOnClickListener(v -> {
            if (listener != null && taskToEdit != null) {
                listener.onTaskDelete(taskToEdit);
            }
            dismiss();
        });

        // Botón Guardar
        fabSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), "Por favor escribe el título de la tarea", Toast.LENGTH_SHORT).show();
                return;
            }

            Task taskToReturn = (taskToEdit != null) ? taskToEdit : new Task(title, selectedCategory);
            taskToReturn.setTitle(title);
            taskToReturn.setCategory(selectedCategory);
            taskToReturn.setSubtasks(subtasksList);

            if (listener != null) {
                listener.onTaskSave(taskToReturn);
            }
            dismiss();
        });

        return view;
    }

    private void addSubtaskRowView(LayoutInflater inflater, LinearLayout containerSubtasks, String text) {
        View subtaskView = inflater.inflate(R.layout.item_subtask_row, containerSubtasks, false);
        TextView tvText = subtaskView.findViewById(R.id.tv_subtask_text);
        ImageButton btnRemove = subtaskView.findViewById(R.id.btn_remove_subtask);

        tvText.setText(text);
        btnRemove.setOnClickListener(subView -> {
            containerSubtasks.removeView(subtaskView);
            subtasksList.removeIf(st -> st.getTitle().equals(text));
        });

        containerSubtasks.addView(subtaskView);
    }
}