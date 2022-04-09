package com.vzard.buxet;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Calendar;

public class Reminder extends Fragment {

    public Reminder() {
        // Required empty public constructor
    }
    AlarmAdapter alarmAdapter;
    public static TextView tvAlarmEmpty;
    AlarmManager alarmManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reminder, container, false);
        if(ApplicationClass.applicationAlarm.size()==0 && Build.MANUFACTURER.equals("Xiaomi"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("For this feature to work, please enable the following options");
            builder.setMessage("- Floating Notification\n- LockScreen Notification\n- Enable Autostart\n- Allow Sound");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.show();
        }
        RecyclerView rvAlarm = v.findViewById(R.id.rvAlarm);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        setHasOptionsMenu(true);
        alarmAdapter = new AlarmAdapter(getActivity());
        tvAlarmEmpty = v.findViewById(R.id.tvReminderEmpty);
        if(ApplicationClass.applicationAlarm.isEmpty())
        {
            tvAlarmEmpty.setVisibility(View.VISIBLE);
        }
        else{
            tvAlarmEmpty.setVisibility(View.GONE);
        }
        rvAlarm.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAlarm.setAdapter(alarmAdapter);
        FloatingActionButton fabAddAlarm = v.findViewById(R.id.fabAddReminder);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ApplicationClass.applicationAlarm.add(new AlarmInfo("Label here","12/12/2020","12:43","Desp",3));
                alarmAdapter.notifyDataSetChanged();*/
                Intent intent = new Intent(getActivity(),SetReminder.class);
                intent.putExtra("pos",-1);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ApplicationClass.applicationAlarm.isEmpty())
        {
            tvAlarmEmpty.setVisibility(View.VISIBLE);
        }
        else{
            tvAlarmEmpty.setVisibility(View.GONE);
        }
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
            super.onStop();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MainActivity.REMINDER_FILE, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(ApplicationClass.applicationAlarm);
            String json2 = gson.toJson(ApplicationClass.alarmListPd);
            editor.putString("alarmlistpd",json2);
            editor.putString("alarminfo",json);
            editor.apply();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mn_reminder,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.itemInActive)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure?");
            builder.setMessage("All Inactive reminders will be lost.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(int i=0;i<ApplicationClass.applicationAlarm.size();++i)
                    {
                        if(!ApplicationClass.applicationAlarm.get(i).isActive())
                        {
                            ApplicationClass.applicationAlarm.remove(i);
                            ApplicationClass.alarmListPd.remove(i);
                            i--;
                        }
                    }
                    alarmAdapter.notifyDataSetChanged();
                    if(ApplicationClass.applicationAlarm.isEmpty())
                    {
                        tvAlarmEmpty.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(),"All Inactive reminders are cleared!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        else if(item.getItemId()==R.id.itemPast) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure?");
            builder.setMessage("All your past reminders will be lost.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(int i=0;i<ApplicationClass.applicationAlarm.size();++i)
                    {
                        if(ApplicationClass.applicationAlarm.get(i).getTimeInMillis() <= Calendar.getInstance().getTimeInMillis())
                        {
                            ApplicationClass.applicationAlarm.remove(i);
                            ApplicationClass.alarmListPd.remove(i);
                            i--;
                        }
                    }
                    alarmAdapter.notifyDataSetChanged();
                    if(ApplicationClass.applicationAlarm.isEmpty())
                    {
                        tvAlarmEmpty.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(),"All your past reminders are cleared!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        else if(item.getItemId()==R.id.itemAll)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure?");
            builder.setMessage("All your reminders will be lost.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ApplicationClass.applicationAlarm.clear();
                    for(int i=0;i<ApplicationClass.alarmListPd.size();++i)
                    {
                        if(ApplicationClass.alarmListPd.get(i)!=null)
                        alarmManager.cancel(ApplicationClass.alarmListPd.get(i));
                    }
                    ApplicationClass.alarmListPd.clear();
                    alarmAdapter.notifyDataSetChanged();
                    tvAlarmEmpty.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"All reminders are cleared!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}