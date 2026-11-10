package com.example.act1pgl;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TasksFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_tasks);
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_menu) {
                    // Abre el menú lateral tipo hamburguesa
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.nav_tasks) {
                    selectedFragment = new TasksFragment();
                } else if (itemId == R.id.nav_calendar) {
                    selectedFragment = new TasksFragment(); // Reemplazar luego por CalendarFragment
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new TasksFragment(); // Reemplazar luego por ProfileFragment
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