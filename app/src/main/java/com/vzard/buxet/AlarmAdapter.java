package com.vzard.buxet;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    Context context;
    public AlarmAdapter(Context ctx) {
        this.context=ctx;
    }

    @NonNull
    @Override
    public AlarmAdapter.AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout_reminder,parent,false);
        return new AlarmHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.AlarmHolder holder, int position) {
        holder.tvLabel.setText(ApplicationClass.applicationAlarm.get(position).getLabel());
        holder.tvDate.setText(ApplicationClass.applicationAlarm.get(position).getDate()+" "+ ApplicationClass.applicationAlarm.get(position).getTime());
        if(ApplicationClass.applicationAlarm.get(position).getTimeInMillis() <= Calendar.getInstance().getTimeInMillis() && ApplicationClass.applicationAlarm.get(position).getRepeat()==0)
            ApplicationClass.applicationAlarm.get(position).setActive(false);
        if(!ApplicationClass.applicationAlarm.get(position).isActive())
        {
            holder.ivActive.setImageResource(R.drawable.inactive_alarm_icon);
        }
        else {
            holder.ivActive.setImageResource(R.drawable.active_alarm_icon);
        }
        holder.ivActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationClass.applicationAlarm.get(position).isActive())
                {
                    ApplicationClass.applicationAlarm.get(position).setActive(false);
                    holder.ivActive.setImageResource(R.drawable.inactive_alarm_icon);
                    //Alarm cancel code here
                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if(ApplicationClass.alarmListPd.get(position)!=null)
                    manager.cancel(ApplicationClass.alarmListPd.get(position));
                    Toast.makeText(context,"Reminder is set to inActive",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(ApplicationClass.applicationAlarm.get(position).getTimeInMillis() <= Calendar.getInstance().getTimeInMillis())
                    {
                        Toast.makeText(context,"Change the time and try again!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ApplicationClass.applicationAlarm.get(position).setActive(true);
                        holder.ivActive.setImageResource(R.drawable.active_alarm_icon);
                        //Alarm change code here
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        if(ApplicationClass.applicationAlarm.get(position).getRepeat()==0)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (alarmManager != null) {
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,ApplicationClass.applicationAlarm.get(position).getTimeInMillis(),ApplicationClass.alarmListPd.get(position));
                                }
                            }
                        }
                        else if(ApplicationClass.applicationAlarm.get(position).getRepeat()==1)
                        {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,ApplicationClass.applicationAlarm.get(position).getTimeInMillis(),AlarmManager.INTERVAL_DAY,ApplicationClass.alarmListPd.get(position));
                        }
                        else {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,ApplicationClass.applicationAlarm.get(position).getTimeInMillis(),7*24*60*60*1000,ApplicationClass.alarmListPd.get(position));
                        }
                        Toast.makeText(context,"Reminder is set to Active!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete it?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        if(ApplicationClass.applicationAlarm.get(position).isActive() && ApplicationClass.alarmListPd.get(position)!=null) {
                            alarmManager.cancel(ApplicationClass.alarmListPd.get(position));
                        }
                        ApplicationClass.alarmListPd.remove(position);
                        ApplicationClass.applicationAlarm.remove(position);
                        //Alarm Cancel code here
                        notifyDataSetChanged();
                        if (ApplicationClass.applicationAlarm.size()==0)
                        {
                            Reminder.tvAlarmEmpty.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SetReminder.class);
                intent.putExtra("pos",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ApplicationClass.applicationAlarm.size();
    }

    public class AlarmHolder extends RecyclerView.ViewHolder{
        ImageView ivActive,ivDelete;
        TextView tvLabel,tvDate;
        RelativeLayout rlItem;

        public AlarmHolder(@NonNull View itemView) {
            super(itemView);
            ivActive = itemView.findViewById(R.id.ivAlarmActive);
            ivDelete = itemView.findViewById(R.id.ivAlarmDelete);
            tvLabel = itemView.findViewById(R.id.tvAlarmLabel);
            tvDate = itemView.findViewById(R.id.tvAlarmTime);
            rlItem = itemView.findViewById(R.id.rlItem);
        }
    }
}
