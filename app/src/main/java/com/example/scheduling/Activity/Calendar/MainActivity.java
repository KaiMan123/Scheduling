package com.example.scheduling.Activity.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.scheduling.Activity.Create.CreateActivity;
import com.example.scheduling.Activity.Data.DataActivity;
import com.example.scheduling.Database.Task;
import com.example.scheduling.Database.TaskRepo;
import com.example.scheduling.R;
import com.example.scheduling.utils.OneDayDecorator;
import com.example.scheduling.utils.Time;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.scheduling.MESSAGE";
    private int[] time = new int[3];
    private MaterialCalendarView  calendarView;
    private LinearLayout dailyView;
    private TaskRepo db;
    List<Task> day_tasks;
    List<Long> short_date;
    List<Long> long_date;
    List<Long> two_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new TaskRepo(getApplication());
        List<List<Long>> tTask = db.getAllTasks();

        short_date = tTask.get(0);
        long_date = tTask.get(1);
        two_date = tTask.get(2);
        System.out.println("=====================================================");
        System.out.println(short_date.size());
        System.out.println(long_date.size());
        System.out.println(two_date.size());
//        if(long_date.size() > 0){
//            Calendar cc = Time.getTime();
//            for(Long dd : long_date){
//                cc.setTimeInMillis(dd);
//                System.out.println(dd);
//                System.out.println(cc.getTime());
//            }
//        }
        System.out.println("=====================================================");

        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());

        calendarView.addDecorator(new OneDayDecorator(short_date, new int[]{Color.parseColor("#2196f3")}));
        calendarView.addDecorator(new OneDayDecorator(long_date, new int[]{Color.parseColor("#38d689")}));
        calendarView.addDecorator(new OneDayDecorator(two_date, new int[]{Color.parseColor("#2196f3"), Color.parseColor("#38d689")}));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView view, CalendarDay date, boolean selected) {
                OnSelectFunc(date.getDay(), date.getMonth()-1, date.getYear());
            }
        });
        firstInit();

        BottomNavigationView mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setSelectedItemId(R.id.menu_calendar);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                switch (item.getItemId()) {
                    case R.id.menu_calendar:
                        return true;
                    case R.id.menu_new:
                        goCreate();
                        return true;
                    case R.id.menu_analytics:
                        goData();
                        return true;
                }
                return false;
            }
        });
    }

    private void OnSelectFunc(int dayOfMonth, int month, int year) {
        TextView tTextView = findViewById(R.id.dailyDate);
        tTextView.setText(Time.date2string(dayOfMonth, month, year));
        time[0] = dayOfMonth;
        time[1] = month;
        time[2] = year;

        day_tasks =  db.getByDay(Time.date2format(dayOfMonth, month, year));
        changeContent(day_tasks);
    }

    private void firstInit() {
        Calendar calendar = Time.getDate();

        TextView tTextView = findViewById(R.id.dailyDate);
        tTextView.setText(Time.date2string(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
        time[0] = calendar.get(Calendar.DAY_OF_MONTH);
        time[1] = calendar.get(Calendar.MONTH);
        time[2] = calendar.get(Calendar.YEAR);

        ImageButton tButton = findViewById(R.id.dailyButton);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCalendar();
            }
        });

        day_tasks =  db.getByDay(Time.date2format(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
        changeContent(day_tasks);
    }

    public void changeContent(List<Task> tasks) {
        LinearLayout quick_view = findViewById(R.id.quick_view);
        quick_view.removeAllViews();
        TextView[] cardview = new TextView[tasks.size()];
        for(int i = 0; i < tasks.size(); ++i) {
            cardview[i] = new TextView(getApplication());
            cardview[i].setText(tasks.get(i).Name);
            int cardview_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams cardview_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, cardview_height);
            cardview_params.setMargins(10, 10, 10, 10);
            cardview[i].setLayoutParams(cardview_params);
            cardview[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            cardview[i].setGravity(Gravity.CENTER);
            if(tasks.get(i).status.equals("passed")){
                cardview[i].setBackgroundColor(Color.parseColor("#dddddd"));
            } else if(tasks.get(i).type.equals("short")){
                cardview[i].setBackgroundColor(Color.parseColor("#2196f3"));
            }else{
                cardview[i].setBackgroundColor(Color.parseColor("#38d689"));
            }
            quick_view.addView(cardview[i]);
        }


//        TextView coming = findViewById(R.id.coming_task);
//        coming.setText("You have finish all the task in this day!");
//
//        TextView total = findViewById(R.id.total_task);
//        total.setText("Total task you have: (0/0)");
//
//        TextView onging_long = findViewById(R.id.ongoing_long_task);
//        onging_long.setText("No of long task you have: (0/0)");
//
//        TextView onging_short = findViewById(R.id.ongoing_short_task);
//        onging_short.setText("No of short task you have: (0/0)");
//
//        TextView finish = findViewById(R.id.finish_task);
//        finish.setText("No of task you have done: (0/0)");
//
//        TextView giveup = findViewById(R.id.giveup_task);
//        giveup.setText("No of task you have given up: (0/0)");
    }

    public void goCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        int[] message = time;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        overridePendingTransition(R.anim.empty, R.anim.empty);
    }

    public void goCreate() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.r2l, R.anim.empty);
    }

    public void goData() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.r2l, R.anim.empty);
    }
}
