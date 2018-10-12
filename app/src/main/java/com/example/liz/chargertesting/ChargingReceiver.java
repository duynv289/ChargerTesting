package com.example.liz.chargertesting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class ChargingReceiver extends BroadcastReceiver {
    private boolean isCharging;
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
    }

    public boolean isCharging() {
        return isCharging;
    }
}
