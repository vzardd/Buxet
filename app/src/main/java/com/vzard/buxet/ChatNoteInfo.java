package com.vzard.buxet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ChatNoteInfo implements Parcelable {
    private String title;
    private String lastMsg;
    private String lastMsgTime;
    private boolean despVisibility;
    private String desp;
    private ArrayList<String> messages;
    private ArrayList<String> msgTimes;

    public ChatNoteInfo(String title, String desp , String msg, ArrayList<String> messages,String lastMsgTime,ArrayList<String> msgTimes) { // Message Times must be initialized
        this.title = title;
        this.lastMsg = msg;
        this.lastMsgTime = lastMsgTime;
        this.despVisibility = false;
        this.desp = desp;
        this.messages=messages;
        this.msgTimes=msgTimes;
    }

    protected ChatNoteInfo(Parcel in) {
        title = in.readString();
        lastMsg = in.readString();
        lastMsgTime = in.readString();
        despVisibility = in.readByte() != 0;
        desp = in.readString();
        messages = in.createStringArrayList();
        msgTimes = in.createStringArrayList();
    }

    public static final Creator<ChatNoteInfo> CREATOR = new Creator<ChatNoteInfo>() {
        @Override
        public ChatNoteInfo createFromParcel(Parcel in) {
            return new ChatNoteInfo(in);
        }

        @Override
        public ChatNoteInfo[] newArray(int size) {
            return new ChatNoteInfo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public boolean isDespVisibility() {
        return despVisibility;
    }

    public void setDespVisibility(boolean despVisibility) {
        this.despVisibility = despVisibility;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getMsgTimes() {
        return msgTimes;
    }

    public void setMsgTimes(ArrayList<String> msgTimes) {
        this.msgTimes = msgTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(lastMsg);
        dest.writeString(lastMsgTime);
        dest.writeByte((byte) (despVisibility ? 1 : 0));
        dest.writeString(desp);
        dest.writeStringList(messages);
        dest.writeStringList(msgTimes);
    }
}
