package com.example.scheduling.Activity.Create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.scheduling.Activity.Data.DataActivity;
import com.example.scheduling.Activity.Calendar.MainActivity;
import com.example.scheduling.Database.Task;
import com.example.scheduling.Database.TaskDB;
import com.example.scheduling.Database.TaskRepo;
import com.example.scheduling.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        LinearLayout to_long = findViewById(R.id.to_long);
        to_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLong();
            }
        });

        LinearLayout to_short = findViewById(R.id.to_short);
        to_short.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goShort();
            }
        });

        BottomNavigationView mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setSelectedItemId(R.id.menu_new);
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
                        return true;
                    case R.id.menu_analytics:
                        goData();
                        return true;
                }
                return false;
            }
        });
    }

    public void goLong() {
        Intent intent = new Intent(this, LongTaskActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.empty, R.anim.empty);
    }

    public void goShort() {
        Intent intent = new Intent(this, ShortTaskActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.empty, R.anim.empty);
    }

    public void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.l2r, R.anim.empty);
    }

    public void goData() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.r2l, R.anim.empty);
    }
}