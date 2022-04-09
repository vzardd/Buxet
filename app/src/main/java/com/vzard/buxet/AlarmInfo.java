package com.vzard.buxet;

public class AlarmInfo {
    private String label;
    private String date;
    private String time;
    private String alarmDesp;
    private boolean active;
    private long timeInMillis;
    private int repeat; //0 Once, 1 Daily, 2 Weekly

    public AlarmInfo(String label, String date, String time, String alarmDesp, int repeat) {
        this.label = label;
        this.date = date;
        this.time = time;
        this.alarmDesp = alarmDesp;
        this.repeat = repeat;
        this.active=true;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmDesp() {
        return alarmDesp;
    }

    public void setAlarmDesp(String alarmDesp) {
        this.alarmDesp = alarmDesp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
