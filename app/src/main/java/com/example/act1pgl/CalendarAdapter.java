package com.example.act1pgl;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClick(String fullDate);
    }

    private final List<String> daysOfMonth;
    private final Set<String> datesWithTasks;
    private String selectedDate;
    private final OnDayClickListener listener;

    public CalendarAdapter(List<String> daysOfMonth, Set<String> datesWithTasks, String selectedDate, OnDayClickListener listener) {
        this.daysOfMonth = daysOfMonth;
        this.datesWithTasks = datesWithTasks;
        this.selectedDate = selectedDate;
        this.listener = listener;
    }

    public void updateData(Set<String> newDatesWithTasks, String newSelectedDate) {
        this.datesWithTasks.clear();
        this.datesWithTasks.addAll(newDatesWithTasks);
        this.selectedDate = newSelectedDate;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        String dateStr = daysOfMonth.get(position);

        if (dateStr == null || dateStr.isEmpty()) {
            holder.tvDayNumber.setText("");
            holder.dotIndicator.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(null);
            return;
        }

        // Obtener solo el número del día (ej. "2026-11-12" -> "12")
        String dayNumber = dateStr.substring(8);
        holder.tvDayNumber.setText(String.valueOf(Integer.parseInt(dayNumber)));

        // Mostrar punto rojo si hay tareas ese día
        if (datesWithTasks.contains(dateStr)) {
            holder.dotIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.dotIndicator.setVisibility(View.INVISIBLE);
        }

        // Estilo del día seleccionado
        if (dateStr.equals(selectedDate)) {
            holder.tvDayNumber.setBackgroundResource(R.drawable.ic_profile);
            holder.tvDayNumber.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2B5B75")));
            holder.tvDayNumber.setTextColor(Color.WHITE);
            holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvDayNumber.setBackground(null);
            holder.tvDayNumber.setTextColor(Color.parseColor("#1A1A1A"));
            holder.tvDayNumber.setTypeface(null, Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedDate = dateStr;
            notifyDataSetChanged();
            if (listener != null) listener.onDayClick(dateStr);
        });
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayNumber;
        View dotIndicator;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
            dotIndicator = itemView.findViewById(R.id.dot_indicator);
        }
    }
}