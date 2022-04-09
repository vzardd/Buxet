package com.vzard.buxet;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String label = intent.getStringExtra("label");
        String alarmDesp = intent.getStringExtra("alarmdesp");
        ApplicationClass.managerCompat = NotificationManagerCompat.from(context);
        int pos=-1;
        for(int i=0;i<ApplicationClass.applicationAlarm.size();++i)
        {
            if(ApplicationClass.applicationAlarm.get(i).getLabel().equals(label) && ApplicationClass.applicationAlarm.get(i).getAlarmDesp().equals(alarmDesp))
            {
                if(ApplicationClass.applicationAlarm.get(i).getRepeat()==0)
                {
                    ApplicationClass.applicationAlarm.get(i).setActive(false);
                    pos = i;
                    break;
                }
                else if(ApplicationClass.applicationAlarm.get(i).getRepeat()==1)
                {

                    pos = i;
                    break;
                }
                else
                {
                    pos = i;
                    break;
                }
            }
        }
        Intent intent1 = new Intent(context,AlarmIntent.class);
        intent1.putExtra("position",pos);
        PendingIntent pd = PendingIntent.getActivity(context,1,intent1,0);
        Intent intent2 = new Intent(context,CancelAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,2,intent2,0);
        Notification notification = new NotificationCompat.Builder(context,ApplicationClass.channelId)
                .setContentTitle(label)
                .setContentText(alarmDesp)
                .setSmallIcon(R.drawable.timer_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pd)
                .setOngoing(true)
                .addAction(R.drawable.thumbs_up_icon,"Got it!",pendingIntent )
                .build();
        ApplicationClass.managerCompat.notify(1,notification);
    }
}