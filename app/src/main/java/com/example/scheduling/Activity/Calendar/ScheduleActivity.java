package com.example.scheduling.Activity.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.scheduling.Activity.Calendar.CalendarActivity;
import com.example.scheduling.Database.Task;
import com.example.scheduling.Database.TaskRepo;
import com.example.scheduling.R;
import com.example.scheduling.utils.Time;

import java.util.List;

public class ScheduleActivity extends Fragment {
    private TaskRepo db;
    final String[] unit_item = {"Day", "Week", "Month", "Year"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule, container, false);

        db = new TaskRepo(getActivity().getApplication());

        LinearLayout tButton = view.findViewById(R.id.goback1);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarActivity calendar_instance = (CalendarActivity) getActivity();
                calendar_instance.goMain();
            }
        });

        CalendarActivity calendar_instance = (CalendarActivity) getActivity();
        int[] time = calendar_instance.getTime();
        TextView textView = (TextView) view.findViewById(R.id.schedule_date);
        textView.setText(Time.date2string(time[0], time[1], time[2]));

        List<Task> day_task =  db.getByDay(Time.date2format(time[0], time[1], time[2]));

        LinearLayout schedule_view = (LinearLayout) view.findViewById(R.id.schedule_view);

        LinearLayout[] cardview = new LinearLayout[day_task.size()];
        for(int i = 0; i < day_task.size(); ++i) {
            cardview[i] = new LinearLayout(getContext());
            int margin_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams cardview_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            cardview_params.setMargins(margin_size, 0, margin_size, margin_size);
            cardview[i].setLayoutParams(cardview_params);

            View card_content = inflater.inflate(R.layout.timecard, container, false);
            LinearLayout card_background = card_content.findViewById(R.id.card_view);
            int card_background_height;
            if(day_task.get(i).Description.isEmpty()){
                card_background_height = 100;
            }else{
                card_background_height = 180;
                EditText card_description =  card_content.findViewById(R.id.card_description);
                card_description.setText(day_task.get(i).Description);
                card_description.setVisibility(View.VISIBLE);
            }
            card_background.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, card_background_height, getResources().getDisplayMetrics())));

            if(day_task.get(i).status.equals("passed")){
                card_background.setBackgroundColor(Color.parseColor("#dddddd"));
            } else if(day_task.get(i).type.equals("short")){
                card_background.setBackgroundColor(Color.parseColor("#2196f3"));
            } else {
                card_background.setBackgroundColor(Color.parseColor("#38d689"));
            }

            TextView card_title = card_content.findViewById(R.id.card_title);
            card_title.setText(day_task.get(i).Name);

            TextView card_status = card_content.findViewById(R.id.card_status);
            card_status.setText(day_task.get(i).status);

            TextView card_time = card_content.findViewById(R.id.card_time);
            String time_str = "";

            if(day_task.get(i).type.equals("short")){
                time_str += "From "+Time.format2string(day_task.get(i).day_from);
                time_str += "\n";
                time_str += "To " + Time.format2string(day_task.get(i).day_to);
                time_str += "\n";
            }

            if(day_task.get(i).time_to.isEmpty()){
                time_str += "NOT SET";
            } else if (day_task.get(i).time_to.equals("2400")){
                time_str += "FULL DAY";
            } else {
                time_str += day_task.get(i).time_from + " : " + day_task.get(i).time_to;
            }
            card_time.setText(time_str);


            TextView card_repeat = card_content.findViewById(R.id.card_repeat);
            if(day_task.get(i).repeat == -1){
                card_repeat.setText("No Repeat");
            } else {
                card_repeat.setText("Repeat Every " + unit_item[day_task.get(i).repeat_unit] + " " + day_task.get(i).repeat + " time(s)");
            }

            cardview[i].addView(card_content);

            schedule_view.addView(cardview[i]);
        }

        return view;
    }

}

