package com.tasklist.android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    private static MainActivity instance;
    Chip all,personal,office,learn,others;
    ChipGroup chipGroup;
    LinearLayout linearLayout;
    FloatingActionButton addButton;
    ListView listView;
    static String loadedActivity="NOT NULL";
    static DatePickerDialog datePickerDialog;
    static TimePickerDialog timePickerDialog;
    MainAdapter mainAdapter;
    //Custom Dialog Elements
    static int rHour,rMinute;
    EditText taskDesc;
    Spinner spinner;
    static String state;

    ArrayList<String> listTitle = new ArrayList<String>();
    ArrayList<String> listTime = new ArrayList<String>();
    ArrayList<String> listStatus = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipGroup = findViewById(R.id.chip_group);
        all = findViewById(R.id.chip_all);
        personal = findViewById(R.id.chip_personal);
        office = findViewById(R.id.chip_office);
        learn = findViewById(R.id.chip_learn);
        others = findViewById(R.id.chip_others);
        addButton = findViewById(R.id.add_button);
        listView = findViewById(R.id.list);
        datePickerDialog = new DatePickerDialog(getApplicationContext());
        linearLayout = findViewById(R.id.linear_layout_list);

        mainAdapter = new MainAdapter(MainActivity.this,listTitle,listTime,listStatus);

        fetchCustomData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "get", Toast.LENGTH_SHORT).show();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fetchData("NOT NULL");
                loadedActivity = "NOT NULL";
                fetchCustomData();
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fetchData("='Personal'");
                loadedActivity = "='Personal'";
                fetchCustomData();
            }
        });

        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fetchData("='Office'");
                loadedActivity = "='Office'";
                fetchCustomData();
            }
        });

        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fetchData("='Learn'");
                loadedActivity = "='Learn'";
                fetchCustomData();
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fetchData("='Others'");
                loadedActivity = "='Others'";
                fetchCustomData();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog inputControl = new BottomSheetDialog(MainActivity.this);
                inputControl.setContentView(R.layout.custom_dialog);

                taskDesc = inputControl.findViewById(R.id.task);
                spinner = inputControl.findViewById(R.id.task_type);

                View f1 = getWindow().getDecorView();
                f1.setBackgroundResource(android.R.color.transparent);
                inputControl.show();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onDateButtonClick(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this);
        dialog.show();
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePickerDialog = dialog;
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                   {
                        rHour = hourOfDay;
                        rMinute = minute;
                   }
               },hour,minute,false);
                timePickerDialog.show();
            }
        });
    }

    public void onSendButtonClick(View view)
    {
        String description = taskDesc.getText().toString();
        String type = spinner.getSelectedItem().toString();
        String date = datePickerDialog.getDatePicker().getDayOfMonth()+"/"+datePickerDialog.getDatePicker().getMonth()+"/"+datePickerDialog.getDatePicker().getYear();

        int day = datePickerDialog.getDatePicker().getDayOfMonth();
        int month = datePickerDialog.getDatePicker().getMonth();
        String time = rHour+":"+rMinute;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        if(listTitle.contains(description))
        {
            Toast.makeText(this, "A task with specific name already exists", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (description.length()>=2) {
                dataBaseHelper.addTask(description, date, time, type);
                startAlarm(description);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please describe Task Properly", Toast.LENGTH_SHORT).show();
            }
            taskDesc.setText("");
        }
        fetchCustomData();
    }

    public void startAlarm(String taskName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.set(datePickerDialog.getDatePicker().getYear(),
                datePickerDialog.getDatePicker().getMonth(),
                datePickerDialog.getDatePicker().getDayOfMonth(),
                rHour, rMinute, 0);

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcast.class);
        intent.putExtra("taskName", taskName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    public void fetchCustomData()
    {
        listTitle.clear();
        listStatus.clear();
        listTime.clear();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        dataBaseHelper.getWritableDatabase();
        String selectQuery = String.format("select * from MyTaskList where TYPE %s",loadedActivity);
        Cursor cursor = dataBaseHelper.getWritableDatabase().rawQuery(selectQuery,null);
        while (cursor.moveToNext())
        {
            @SuppressLint("Range") String task = cursor.getString(cursor.getColumnIndex("TASK"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("TIME"));
            @SuppressLint("Range") String completed = cursor.getString(cursor.getColumnIndex("COMPLETED"));
            @SuppressLint("Range") String taskType = cursor.getString(cursor.getColumnIndex("TYPE"));
            listTitle.add(task);
            listTime.add(date+" "+time);
            listStatus.add(completed);
        }
        listView.setAdapter(mainAdapter);
    }

}