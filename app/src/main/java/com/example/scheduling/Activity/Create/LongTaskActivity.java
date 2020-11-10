package com.example.scheduling.Activity.Create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scheduling.Activity.Data.DataActivity;
import com.example.scheduling.Activity.Calendar.MainActivity;
import com.example.scheduling.Database.Task;
import com.example.scheduling.Database.TaskRepo;
import com.example.scheduling.R;
import com.example.scheduling.utils.Time;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class LongTaskActivity extends AppCompatActivity {
    private TaskRepo db;
    private Switch is_specific;
    private Switch is_fullday;
    private EditText t_name;
    private EditText t_description;
    private EditText loop_times;
    private Button select_date_from;
    private Button select_date_to;
    private Button select_from;
    private Button select_to;
    Spinner loop_repeat;
    Spinner loop_unit;
    final String[] unit_item = {"Day", "Week", "Month", "Year"};
    final String[] loop_item = {"Every Day", "Every Week", "Every Month", "Every Year", "Custom Period"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.longtask);

        db = new TaskRepo(getApplication());

        t_name = (EditText) findViewById(R.id.name);
        t_description = (EditText) findViewById(R.id.description);
        loop_times = (EditText) findViewById(R.id.loop_times);

        select_date_from = (Button) findViewById(R.id.select_date_from);
        select_date_to = (Button) findViewById(R.id.select_date_to);
        select_from = (Button) findViewById(R.id.select_from);
        select_to = (Button) findViewById(R.id.select_to);

        is_specific = findViewById(R.id.is_specific);
        is_specific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout timeframe = (LinearLayout) findViewById(R.id.timeframe);
                if(isChecked){
                    timeframe.setVisibility(View.VISIBLE);
                }else{
                    timeframe.setVisibility(View.GONE);
                }
            }
        });

        is_fullday = findViewById(R.id.is_fullday);
        is_fullday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout fullday = (LinearLayout) findViewById(R.id.fullday);
                if(isChecked){
                    fullday.setVisibility(View.GONE);
                }else{
                    fullday.setVisibility(View.VISIBLE);
                }
            }
        });

        loop_repeat = (Spinner) findViewById(R.id.loop_repeat);
        ArrayAdapter<String> item_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loop_item);
        loop_repeat.setAdapter(item_list);

        loop_repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout custom_repeat = (LinearLayout) findViewById(R.id.custom_repeat);
                if(position == 4){
                    custom_repeat.setVisibility(View.VISIBLE);
                }else{
                    custom_repeat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loop_unit = (Spinner) findViewById(R.id.loop_unit);
        ArrayAdapter<String> unit_list = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unit_item);
        loop_unit.setAdapter(unit_list);

        Button to_create = findViewById(R.id.to_create);
        to_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCreate();
            }
        });

        Button tButton = (Button) findViewById(R.id.create_long);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t_name.getText().toString().isEmpty()) {
                    Toast.makeText(LongTaskActivity.this, "Name is required!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(select_date_from.getText().toString().isEmpty()) {
                    Toast.makeText(LongTaskActivity.this, "From Date is required!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(select_date_to.getText().toString().isEmpty()) {
                    Toast.makeText(LongTaskActivity.this, "To Date is required!", Toast.LENGTH_LONG).show();
                    return;
                }
                Task nTask = new Task();
                nTask.id = UUID.randomUUID().toString();
                nTask.type = "long";
                nTask.status = "init";
                nTask.Name = t_name.getText().toString();
                nTask.Description = t_description.getText().toString();
                nTask.day = 0L;
                nTask.day_from = Time.string2format(select_date_from.getText().toString());
                nTask.day_to = Time.string2format(select_date_to.getText().toString());
                nTask.time_from = -1;
                nTask.time_to = -1;
                if(is_fullday.isChecked()){
                    nTask.time_from = 0;
                    nTask.time_to = 2400;
                }else{
                    nTask.time_from = Time.string2time(select_from.getText().toString());
                    nTask.time_to = Time.string2time(select_to.getText().toString());
                }
                nTask.repeat = -1;
                nTask.repeat_unit = -1;
                if(loop_repeat.getSelectedItemPosition() == 4){
                    nTask.repeat = Integer.valueOf(loop_times.getText().toString());
                    nTask.repeat_unit = loop_unit.getSelectedItemPosition();
                }else{
                    nTask.repeat = 1;
                    nTask.repeat_unit = loop_repeat.getSelectedItemPosition();
                }
                db.insert(nTask);
                goMain();
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

    @SuppressLint("ResourceType")
    public void dateFromPicker(View v){
        Calendar calendar = Time.getDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), 16973948, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = Time.date2string(day, month, year);
                select_date_from.setText(dateTime);
            }

        }, year, month, day).show();
    }

    @SuppressLint("ResourceType")
    public void dateToPicker(View v){
        Calendar calendar = Time.getDate();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), 16973948, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = Time.date2string(day, month, year);
                select_date_to.setText(dateTime);
            }

        }, year, month, day).show();
    }

    public void fromPicker(View v){
        Calendar initCal = Time.getTime();
        new TimePickerDialog(v.getContext(), 16973948, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Time.getTime();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                select_from.setText(time);
            }

        }, initCal.get(Calendar.HOUR_OF_DAY), initCal.get(Calendar.MINUTE), true).show();
    }

    public void toPicker(View v){
        Calendar initCal = Time.getTime();
        new TimePickerDialog(v.getContext(), 16973948, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Time.getTime();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                System.out.println(calendar.getTimeZone());
                System.out.println(calendar.getTime());
                System.out.println("===========================================");
                select_to.setText(time);
            }

        }, initCal.get(Calendar.HOUR_OF_DAY), initCal.get(Calendar.MINUTE), true).show();
    }

    public void goCreate() {
        Intent intent = new Intent(this, CreateActivity.class);
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