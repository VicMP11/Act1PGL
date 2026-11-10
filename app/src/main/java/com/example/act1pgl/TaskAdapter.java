package com.example.act1pgl;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskActionListener {
        void onTaskClick(Task task);
        void onTaskUpdated(Task task);
    }

    private List<Task> taskList;
    private final OnTaskActionListener listener;

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public void updateList(List<Task> newList) {
        this.taskList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvCategory.setText(task.getCategory());

        // Clic en la tarjeta abre edición
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTaskClick(task);
        });

        // Checkbox principal
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());
        updateStrikethrough(holder.tvTitle, task.isCompleted());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            updateStrikethrough(holder.tvTitle, isChecked);
            if (listener != null) listener.onTaskUpdated(task);
        });

        // Subtareas
        holder.containerSubtasks.removeAllViews();
        List<Subtask> subtasks = task.getSubtasks();

        if (subtasks != null && !subtasks.isEmpty()) {
            holder.containerSubtasks.setVisibility(View.VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());

            for (Subtask st : subtasks) {
                View subtaskView = inflater.inflate(R.layout.item_task_subtask_row, holder.containerSubtasks, false);
                CheckBox cbSubtask = subtaskView.findViewById(R.id.checkbox_subtask);
                TextView tvSubtaskTitle = subtaskView.findViewById(R.id.tv_subtask_title);

                tvSubtaskTitle.setText(st.getTitle());
                cbSubtask.setOnCheckedChangeListener(null);
                cbSubtask.setChecked(st.isCompleted());
                updateStrikethrough(tvSubtaskTitle, st.isCompleted());

                cbSubtask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    st.setCompleted(isChecked);
                    updateStrikethrough(tvSubtaskTitle, isChecked);
                    if (listener != null) listener.onTaskUpdated(task);
                });

                holder.containerSubtasks.addView(subtaskView);
            }
        } else {
            holder.containerSubtasks.setVisibility(View.GONE);
        }
    }

    private void updateStrikethrough(TextView textView, boolean isCompleted) {
        if (isCompleted) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle, tvCategory;
        LinearLayout containerSubtasks;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvCategory = itemView.findViewById(R.id.tv_task_category);
            containerSubtasks = itemView.findViewById(R.id.container_item_subtasks);
        }
    }
}