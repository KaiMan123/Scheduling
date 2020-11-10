package com.example.scheduling.Activity.Data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.scheduling.Activity.Create.CreateActivity;
import com.example.scheduling.Activity.Calendar.MainActivity;
import com.example.scheduling.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        BottomNavigationView mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setSelectedItemId(R.id.menu_analytics);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.menu_calendar:
                        goMain();
                        return true;
                    case R.id.menu_new:
                        goCreate();
                        return true;
                    case R.id.menu_analytics:
                        return true;
                }
                return false;
            }
        });
    }

    public void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.l2r, R.anim.empty);
    }

    public void goCreate() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.l2r, R.anim.empty);
    }
}