package com.vzard.buxet;

import android.app.AlarmManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        for(int i=0;i<ApplicationClass.alarmListPd.size();++i)
        {
            if(ApplicationClass.applicationAlarm.get(i).isActive() && ApplicationClass.applicationAlarm.get(i).getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
            {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                try {
                    if (ApplicationClass.applicationAlarm.get(i).getRepeat() == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ApplicationClass.applicationAlarm.get(i).getTimeInMillis(), ApplicationClass.alarmListPd.get(i));
                            }
                        }
                    } else if (ApplicationClass.applicationAlarm.get(i).getRepeat() == 1) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, ApplicationClass.applicationAlarm.get(i).getTimeInMillis(), AlarmManager.INTERVAL_DAY, ApplicationClass.alarmListPd.get(i));
                    } else {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, ApplicationClass.applicationAlarm.get(i).getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, ApplicationClass.alarmListPd.get(i));
                    }
                }
                catch (Exception e) { }
            }
        }
    }
}