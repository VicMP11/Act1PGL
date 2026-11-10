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

public class CategoriesFragment extends Fragment {

    private CategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        EditText etCategory = view.findViewById(R.id.et_new_category);
        Button btnAdd = view.findViewById(R.id.btn_add_category);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_categories);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(new ArrayList<>(TaskRepository.getInstance().getCategories()), category -> {
            TaskRepository.getInstance().deleteCategory(category);
            adapter.updateList(new ArrayList<>(TaskRepository.getInstance().getCategories()));
        });
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String newCat = etCategory.getText().toString().trim();
            if (!TextUtils.isEmpty(newCat)) {
                TaskRepository.getInstance().addCategory(newCat);
                adapter.updateList(new ArrayList<>(TaskRepository.getInstance().getCategories()));
                etCategory.setText("");
            }
        });

        return view;
    }
}