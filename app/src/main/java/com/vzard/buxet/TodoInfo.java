package com.vzard.buxet;

public class TodoInfo {
    private String taskTitle;
    private String taskDesp;
    private boolean accomplsished;

    public TodoInfo(String taskTitle, String taskDesp) {
        this.taskTitle = taskTitle;
        this.taskDesp = taskDesp;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDesp() {
        return taskDesp;
    }

    public void setTaskDesp(String taskDesp) {
        this.taskDesp = taskDesp;
    }

    public boolean isAccomplsished() {
        return accomplsished;
    }

    public void setAccomplsished(boolean accomplsished) {
        this.accomplsished = accomplsished;
    }
}
