package com.example.scheduling.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.example.scheduling.Database.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class OneDayDecorator  implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private final List<Long> dates;
    private final int[] color;

    public OneDayDecorator(List<Long> dates, int[] color) {
        this.dates = dates;
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay date) {
        int year = date.getYear();
        int month = date.getMonth()-1;
        int day = date.getDay();
        if(this.dates.contains(Time.date2format(day, month, year))){
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CustmMultipleDotSpan(9, this.color));
    }
}
