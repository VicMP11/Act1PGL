package com.example.act1pgl;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HabitsFragment extends Fragment {

    private HabitAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        EditText etNewHabit = view.findViewById(R.id.et_new_habit);
        Button btnAddHabit = view.findViewById(R.id.btn_add_habit);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_habits);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HabitAdapter(new ArrayList<>(HabitRepository.getInstance().getHabits()), habit -> {
            HabitRepository.getInstance().deleteHabit(habit);
            adapter.updateList(new ArrayList<>(HabitRepository.getInstance().getHabits()));
        });
        recyclerView.setAdapter(adapter);

        btnAddHabit.setOnClickListener(v -> {
            String title = etNewHabit.getText().toString().trim();
            if (!TextUtils.isEmpty(title)) {
                HabitRepository.getInstance().addHabit(title);
                adapter.updateList(new ArrayList<>(HabitRepository.getInstance().getHabits()));
                etNewHabit.setText("");
            }
        });

        return view;
    }
}