package com.example.scheduling.Activity.Calendar;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.scheduling.Activity.Calendar.CalendarActivity;
import com.example.scheduling.R;
import com.example.scheduling.utils.Time;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class TimeTableActivity extends Fragment {
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable, container, false);

        LinearLayout tButton = view.findViewById(R.id.goback2);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarActivity calendar_instance = (CalendarActivity) getActivity();
                calendar_instance.goMain();
            }
        });

        CalendarActivity calendar_instance = (CalendarActivity) getActivity();
        int[] time = calendar_instance.getTime();
        TextView textView = (TextView) view.findViewById(R.id.timetable_date);
        textView.setText(Time.date2string(time[0], time[1], time[2]));

        LinearLayout timetable_view = (LinearLayout) view.findViewById(R.id.timetable_view);

        generateTimetable(timetable_view);

        return view;
    }

    public void generateTimetable(LinearLayout timetable) {
        String[] timeset = {
                "0  mn", "1  am", "2  am", "3  am", "4  am", "5  am", "6  am", "7  am", "8  am", "9  am", "10 am", "11 am",
                "12 mn", "1  pm", "2  pm", "3  pm", "4  pm", "p  am", "6  pm", "p  am", "8  pm", "9  pm", "10 pm", "11 pm"
        };
        for(int i = 0; i < 24; ++i){
            LinearLayout timeframe = new LinearLayout(getContext());
            timeframe.setOrientation(LinearLayout.HORIZONTAL);
            int timeframe_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams timeframe_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, timeframe_height);
            timeframe.setLayoutParams(timeframe_params);

            TextView time = new TextView(getContext());
            time.setText(timeset[i]);
            int time_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams time_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, time_height);
            time.setLayoutParams(time_params);

            LinearLayout timebox = new LinearLayout(getContext());
            timebox.setOrientation(LinearLayout.VERTICAL);
            int timebox_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams timebox_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, timebox_height);
            timebox.setLayoutParams(timebox_params);

            timeframe.addView(time);
            timeframe.addView(timebox);
            timetable.addView(timeframe);
        }
    }
}