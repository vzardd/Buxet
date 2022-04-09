package com.vzard.buxet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AlarmIntent extends AppCompatActivity {
    TextView label,desp,date,repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_intent);
        getSupportActionBar().hide();
        label = findViewById(R.id.tvIntentTitle);
        desp = findViewById(R.id.tvIntentDesp);
        date = findViewById(R.id.tvIntentTime);
        repeat = findViewById(R.id.tvIntentRepeat);
        int pos = getIntent().getIntExtra("position",-1);
        if(pos!=-1)
        {
            label.setText(ApplicationClass.applicationAlarm.get(pos).getLabel());
            desp.setText(ApplicationClass.applicationAlarm.get(pos).getAlarmDesp());
            date.setText(ApplicationClass.applicationAlarm.get(pos).getDate());
            if(ApplicationClass.applicationAlarm.get(pos).getRepeat()==0)
            {
                repeat.setText("Repeat Once");
            }
            else if(ApplicationClass.applicationAlarm.get(pos).getRepeat()==1)
            {
                repeat.setText("Repeat Daily");
            }
            else {
                repeat.setText("Repeat Weekly");
            }
        }
    }
}