package com.vzard.buxet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetReminder extends AppCompatActivity {
    int savePos;
    RadioButton once,daily,weekly;
    AlarmManager alarmManager;
    EditText etLabel,etDesp;
    ImageView ivDatePicker,ivTimePicker;
    int yr,mon,day,hr,min,repeat;
    SimpleDateFormat dateFormat,timeFormat;
    TextView tvDate,tvTime;
    Button btnSaveChanges;
    Calendar calendar;
    String sdate,stime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        getSupportActionBar().setTitle("Add Reminder");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        savePos = getIntent().getIntExtra("pos",-1);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("HH:mm");
        sdate = new String();
        stime = new String();
        once = findViewById(R.id.once);
        daily = findViewById(R.id.daily);
        weekly = findViewById(R.id.weekly);
        etLabel = findViewById(R.id.etLabel);
        etDesp = findViewById(R.id.etAlarmDesp);
        ivDatePicker = findViewById(R.id.ivDatePicker);
        ivTimePicker = findViewById(R.id.ivTimePicker);
        tvDate = findViewById(R.id.tvSetAlarmDAte);
        tvTime = findViewById(R.id.tvSetAlarmTime);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        if(savePos!=-1)
        {
            etLabel.setText(ApplicationClass.applicationAlarm.get(savePos).getLabel());
            etDesp.setText(ApplicationClass.applicationAlarm.get(savePos).getAlarmDesp());
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ApplicationClass.applicationAlarm.get(savePos).getTimeInMillis());
            repeat = ApplicationClass.applicationAlarm.get(savePos).getRepeat();
            if(repeat==0)
            {
                once.setChecked(true);
            }
            else if(repeat==1)
            {
                daily.setChecked(true);
            }
            else{
                weekly.setChecked(true);
            }
            day = calendar.get(Calendar.DATE);
            mon = calendar.get(Calendar.MONTH);
            yr = calendar.get(Calendar.YEAR);
            hr = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
        }
        else {
            calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DATE);
            mon = calendar.get(Calendar.MONTH);
            yr = calendar.get(Calendar.YEAR);
            hr = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
        }
        sdate=dateFormat.format(new Date(calendar.getTimeInMillis()));
        tvDate.setText(sdate);
        stime=timeFormat.format(new Date(calendar.getTimeInMillis()));
        tvTime.setText(stime);
        ivDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SetReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DATE,dayOfMonth);
                        day =calendar.get(Calendar.DATE);
                        mon = calendar.get(Calendar.MONTH);
                        yr = calendar.get(Calendar.YEAR);
                        sdate=dateFormat.format(new Date(calendar.getTimeInMillis()));
                        tvDate.setText(sdate);
                    }
                },yr,mon,day);
                datePickerDialog.show();
            }
        });
        ivTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        hr = calendar.get(Calendar.HOUR_OF_DAY);
                        min = calendar.get(Calendar.MINUTE);
                        stime=timeFormat.format(new Date(calendar.getTimeInMillis()));
                        tvTime.setText(stime);
                    }
                },hr,min,true);
                timePickerDialog.show();
            }
        });
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLabel.getText().toString().trim().isEmpty() || etDesp.getText().toString().trim().isEmpty()){
                    Toast.makeText(SetReminder.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
                }
                else if(calendar.getTimeInMillis()<=Calendar.getInstance().getTimeInMillis())
                {
                    Toast.makeText(SetReminder.this,"Time travel is not allowed here!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(once.isChecked()){
                        repeat = 0;
                    }
                    else if(daily.isChecked())
                    {
                        repeat = 1;
                    }
                    else if(weekly.isChecked()) {
                        repeat = 2;
                    }
                    if(savePos==-1)
                    {
                        AlarmInfo obj = new AlarmInfo(etLabel.getText().toString().trim(),sdate,stime,etDesp.getText().toString().trim(),repeat);
                        obj.setTimeInMillis(calendar.getTimeInMillis());
                        //SetAlarm Here depending on repeat
                        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                        intent.putExtra("label",obj.getLabel());
                        intent.putExtra("alarmdesp",obj.getAlarmDesp());
                        PendingIntent pd = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                        if(repeat==0)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),pd);
                            }
                        }
                        else if(repeat==1)
                        {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pd);
                        }
                        else {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),7*24*60*60*1000,pd);
                        }
                        ApplicationClass.applicationAlarm.add(obj);//notifyDatasetChanged
                        ApplicationClass.alarmListPd.add(pd);
                    }
                    else {
                        AlarmInfo obj = new AlarmInfo(etLabel.getText().toString().trim(),sdate,stime,etDesp.getText().toString().trim(),repeat);
                        obj.setTimeInMillis(calendar.getTimeInMillis());
                        //Cancel old alarm and Change Alarm here
                        try {
                            alarmManager.cancel(ApplicationClass.alarmListPd.get(savePos));
                        }
                        catch (Exception e){}
                        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                        intent.putExtra("label",obj.getLabel());
                        intent.putExtra("alarmdesp",obj.getAlarmDesp());
                        PendingIntent pd = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                        if(repeat==0)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),pd);
                            }
                        }
                        else if(repeat==1)
                        {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pd);
                        }
                        else {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,obj.getTimeInMillis(),7*24*60*60*1000,pd);
                        }
                        ApplicationClass.applicationAlarm.set(savePos,obj);//notify dataset changed
                        ApplicationClass.alarmListPd.set(savePos,pd);

                    }
                    Toast.makeText(SetReminder.this, "Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.REMINDER_FILE,MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(ApplicationClass.applicationAlarm);
        String json2 = gson.toJson(ApplicationClass.alarmListPd);
        editor.putString("alarminfo",json);
        editor.putString("alarmlistpd",json2);
        editor.apply();
    }
}