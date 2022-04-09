package com.vzard.buxet;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<ChatNoteInfo> applicationNotes;
    public static ArrayList<TodoInfo> applicationShortTerm;
    public static ArrayList<TodoInfo> applicationLongTerm;
    public static ArrayList<AlarmInfo> applicationAlarm;
    public static NotificationManagerCompat managerCompat;
    public static  ArrayList<PendingIntent> alarmListPd;
    public static MediaPlayer mp;
    final static String channelId = "Reminder";
    NotificationChannel channel;
    public static boolean splashShown;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationNotes = new ArrayList<ChatNoteInfo>();
        applicationShortTerm = new ArrayList<TodoInfo>();
        applicationLongTerm = new ArrayList<TodoInfo>();
        applicationAlarm = new ArrayList<AlarmInfo>();
        alarmListPd = new ArrayList<PendingIntent>();
        splashShown=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Notification channel
            channel = new NotificationChannel(channelId,"Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder Notification for Buxet");
            channel.enableLights(true);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        SharedPreferences pref = getSharedPreferences(MainActivity.NOTES_FILE,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("notes",null);
        Type type = new TypeToken<ArrayList<ChatNoteInfo>>(){}.getType();
        if(json==null)
        {
            //Toast.makeText(MainActivity.this,"Null",Toast.LENGTH_SHORT).show();
            ApplicationClass.applicationNotes=new ArrayList<ChatNoteInfo>();
        }
        else
        {
            ApplicationClass.applicationNotes=gson.fromJson(json,type);
        }
        SharedPreferences prefTodo = getSharedPreferences(MainActivity.TO_DO_FILE,MODE_PRIVATE);
        String jsonShort = prefTodo.getString("shortTerm",null);
        String jsonLong = prefTodo.getString("longTerm",null);
        Type type2 = new TypeToken<ArrayList<TodoInfo>>(){}.getType();
        if(jsonShort==null)
        {
            //Toast.makeText(MainActivity.this,"Null",Toast.LENGTH_SHORT).show();
            ApplicationClass.applicationShortTerm = new ArrayList<TodoInfo>();
        }
        else
        {
            ApplicationClass.applicationShortTerm = gson.fromJson(jsonShort,type2);
        }
        if(jsonLong==null)
        {
            //Toast.makeText(MainActivity.this,"Null",Toast.LENGTH_SHORT).show();
            ApplicationClass.applicationLongTerm=new ArrayList<TodoInfo>();
        }
        else
        {
            ApplicationClass.applicationLongTerm = gson.fromJson(jsonLong,type2);
        }
        SharedPreferences prefReminder = getSharedPreferences(MainActivity.REMINDER_FILE,MODE_PRIVATE);
        String jsonReminder = prefReminder.getString("alarminfo",null);
        Type type3 = new TypeToken<ArrayList<AlarmInfo>>(){}.getType();
        if(jsonReminder==null)
        {
            ApplicationClass.applicationAlarm = new ArrayList<AlarmInfo>();
        }
        else {
            ApplicationClass.applicationAlarm = gson.fromJson(jsonReminder,type3);
        }
        String jsonAlarmListPd = prefReminder.getString("alarmlistpd",null);
        Type type4 = new TypeToken<ArrayList<PendingIntent>>(){}.getType();
        if(jsonAlarmListPd==null)
        {
            alarmListPd = new ArrayList<PendingIntent>();
        }
        else {
            alarmListPd = gson.fromJson(jsonAlarmListPd,type4);
        }
    }
}
