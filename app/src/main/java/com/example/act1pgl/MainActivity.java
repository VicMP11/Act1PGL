package com.example.act1pgl;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Cargar el fragmento de tareas por defecto al abrir la app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TasksFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_tasks);
        }

        // Listener para cambiar entre pestañas
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_tasks) {
                    selectedFragment = new TasksFragment();
                } else if (itemId == R.id.nav_calendar) {
                    // TODO: Reemplazar por tu CalendarFragment
                    selectedFragment = new TasksFragment();
                } else if (itemId == R.id.nav_profile) {
                    // TODO: Reemplazar por tu ProfileFragment
                    selectedFragment = new TasksFragment();
                } else if (itemId == R.id.nav_menu) {
                    // Acción para el menú lateral o desplegable
                    return true;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }
}