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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scheduling.Activity.Data.DataActivity;
import com.example.scheduling.Activity.Calendar.MainActivity;
import com.example.scheduling.Database.Task;
import com.example.scheduling.Database.TaskRepo;
import com.example.scheduling.R;
import com.example.scheduling.utils.Time;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ShortTaskActivity extends AppCompatActivity {
    private TaskRepo db;

    private Switch is_fullday;
    private Switch is_repeat;
    private EditText t_name;
    private EditText t_description;
    private EditText loop_times;
    private Button select_date;
    private Button select_from;
    private Button select_to;
    Spinner loop_repeat;
    Spinner loop_unit;

    final String[] unit_item = {"Day", "Week", "Month", "Year"};
    final String[] loop_item = {"Every Day", "Every Week", "Every Month", "Every Year", "Custom Period"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shorttask);

        db = new TaskRepo(getApplication());

        t_name = (EditText) findViewById(R.id.name);
        t_description = (EditText) findViewById(R.id.description);
        loop_times = (EditText) findViewById(R.id.loop_times);

        select_date = (Button) findViewById(R.id.select_date);

        Calendar calendar = Time.getDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        select_date.setText(Time.date2string(day, month+1, year));

        select_from = (Button) findViewById(R.id.select_from);
        select_to = (Button) findViewById(R.id.select_to);

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

        is_repeat = findViewById(R.id.is_repeat);
        is_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout looptime = (LinearLayout) findViewById(R.id.looptime);
                if(isChecked){
                    looptime.setVisibility(View.VISIBLE);
                }else{
                    looptime.setVisibility(View.GONE);
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

        Button tButton = (Button) findViewById(R.id.create_short);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t_name.getText().toString().isEmpty()) {
                    Toast.makeText(ShortTaskActivity.this, "Name is required!", Toast.LENGTH_LONG).show();
                    return;
                }
                Task nTask = new Task();
                nTask.id = UUID.randomUUID().toString();
                nTask.type = "short";
                nTask.status = "init";
                nTask.Name = t_name.getText().toString();
                nTask.Description = t_description.getText().toString();
                nTask.day_from = 0L;
                nTask.day_to = 0L;
                nTask.day = Time.string2format(select_date.getText().toString());
                nTask.day_from = nTask.day;
                nTask.day_to = nTask.day;
                nTask.time_from = "";
                nTask.time_to = "";
                if(is_fullday.isChecked()){
                    nTask.time_from = "0000";
                    nTask.time_to = "2400";
                }else{
                    nTask.time_from = select_from.getText().toString();
                    nTask.time_to = select_to.getText().toString();
                }
                nTask.repeat = -1;
                nTask.repeat_unit = -1;
                if(is_repeat.isChecked()){
                    if(loop_repeat.getSelectedItemPosition() == 4){
                        nTask.repeat = Integer.valueOf(loop_times.getText().toString());
                        nTask.repeat_unit = loop_unit.getSelectedItemPosition();
                    }else{
                        nTask.repeat = 1;
                        nTask.repeat_unit = loop_repeat.getSelectedItemPosition();
                    }
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
    public void datePicker(View v){
        Calendar calendar = Time.getDate();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), 16973948, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = Time.date2string(day, month, year);
                select_date.setText(dateTime);
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