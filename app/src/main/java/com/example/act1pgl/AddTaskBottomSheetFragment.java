package com.example.act1pgl;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskBottomSheetFragment extends BottomSheetDialogFragment {

    public interface OnTaskSaveListener {
        void onTaskSave(Task task);
        void onTaskDelete(Task task);
    }

    private OnTaskSaveListener listener;
    private Task taskToEdit;
    private List<Subtask> subtasksList = new ArrayList<>();
    private String selectedCategory = "Work";
    private String selectedDate = "";

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
        ImageView btnPickDate = view.findViewById(R.id.btn_pick_date);
        ImageButton btnDelete = view.findViewById(R.id.btn_delete_task);
        FloatingActionButton fabSave = view.findViewById(R.id.fab_save_task);

        if (taskToEdit != null) {
            etTitle.setText(taskToEdit.getTitle());
            selectedCategory = taskToEdit.getCategory();
            selectedDate = taskToEdit.getDueDate();
            tvCategory.setText(selectedCategory);
            btnDelete.setVisibility(View.VISIBLE);

            for (Subtask st : taskToEdit.getSubtasks()) {
                subtasksList.add(new Subtask(st.getTitle(), st.isCompleted()));
                addSubtaskRowView(inflater, containerSubtasks, st.getTitle());
            }
        } else {
            List<String> categories = TaskRepository.getInstance().getCategories();
            if (!categories.isEmpty()) {
                selectedCategory = categories.get(0);
            }
            tvCategory.setText(selectedCategory);
        }

        // Seleccionar o crear categoría
        tvCategory.setOnClickListener(v -> showCategoryPickerDialog(tvCategory));

        // Selector de fecha
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(requireContext(),
                    (datePickerView, year, month, dayOfMonth) -> {
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        Toast.makeText(getContext(), "Fecha: " + selectedDate, Toast.LENGTH_SHORT).show();
                    },
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
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
            taskToReturn.setDueDate(selectedDate);
            taskToReturn.setSubtasks(subtasksList);

            if (listener != null) {
                listener.onTaskSave(taskToReturn);
            }
            dismiss();
        });

        return view;
    }

    private void showCategoryPickerDialog(TextView tvCategory) {
        List<String> categories = TaskRepository.getInstance().getCategories();
        List<String> options = new ArrayList<>(categories);
        options.add("+ Crear nueva categoría");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona una Categoría");
        builder.setItems(options.toArray(new String[0]), (dialog, which) -> {
            if (which == options.size() - 1) {
                // Diálogo para crear nueva categoría
                showNewCategoryInputDialog(tvCategory);
            } else {
                selectedCategory = options.get(which);
                tvCategory.setText(selectedCategory);
            }
        });
        builder.show();
    }

    private void showNewCategoryInputDialog(TextView tvCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nueva Categoría");

        final EditText input = new EditText(requireContext());
        input.setHint("Ej. Personal, Universidad, Compras");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newCat = input.getText().toString().trim();
            if (!TextUtils.isEmpty(newCat)) {
                TaskRepository.getInstance().addCategory(newCat);
                selectedCategory = newCat;
                tvCategory.setText(selectedCategory);
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
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