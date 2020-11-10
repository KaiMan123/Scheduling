package com.example.scheduling.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.scheduling.Activity.Calendar.ScheduleActivity;
import com.example.scheduling.Activity.Calendar.TimeTableActivity;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private final String[] PAGE_TITLES = new String[] {
        "Schedule",
        "TimeTable",
    };

    private final Fragment[] PAGES = new Fragment[] {
        new ScheduleActivity(),
        new TimeTableActivity(),
    };

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return PAGES[position];
    }

    @Override
    public int getCount() {
        return PAGES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES[position];
    }

}