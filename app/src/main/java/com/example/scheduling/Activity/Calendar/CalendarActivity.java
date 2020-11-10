package com.example.scheduling.Activity.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.scheduling.R;
import com.example.scheduling.utils.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class CalendarActivity extends AppCompatActivity {
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private int[] time = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        Intent intent = getIntent();
        time = intent.getIntArrayExtra(MainActivity.EXTRA_MESSAGE);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.empty, R.anim.empty);
    }

    public int[] getTime() {
        return time;
    }
}