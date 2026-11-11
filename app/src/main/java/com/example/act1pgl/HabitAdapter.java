package com.example.act1pgl;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    public interface OnHabitDeleteListener {
        void onDelete(Habit habit);
    }

    private List<Habit> habitList;
    private final OnHabitDeleteListener deleteListener;

    public HabitAdapter(List<Habit> habitList, OnHabitDeleteListener deleteListener) {
        this.habitList = habitList;
        this.deleteListener = deleteListener;
    }

    public void updateList(List<Habit> newList) {
        this.habitList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.tvTitle.setText(habit.getTitle());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(habit.isCompleted());
        updateStrikethrough(holder.tvTitle, habit.isCompleted());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            habit.setCompleted(isChecked);
            updateStrikethrough(holder.tvTitle, isChecked);
            HabitRepository.getInstance().updateHabitState();
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(habit);
        });
    }

    private void updateStrikethrough(TextView tv, boolean isCompleted) {
        if (isCompleted) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle;
        ImageButton btnDelete;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_habit);
            tvTitle = itemView.findViewById(R.id.tv_habit_title);
            btnDelete = itemView.findViewById(R.id.btn_delete_habit);
        }
    }
}