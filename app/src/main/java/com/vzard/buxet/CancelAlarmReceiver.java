package com.vzard.buxet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CancelAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationClass.managerCompat.cancelAll();
    }
}