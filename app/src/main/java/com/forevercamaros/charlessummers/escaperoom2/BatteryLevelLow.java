package com.forevercamaros.charlessummers.escaperoom2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class BatteryLevelLow extends BroadcastReceiver {
    private List<BatterLevelLowListener> listeners = new ArrayList<BatterLevelLowListener>();
    interface BatterLevelLowListener {
        void onBatteryLevelLow();
    }

    public void addBatteryLevelLowListener(BatterLevelLowListener listener){
        listeners.add(listener);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        for (BatterLevelLowListener bl: listeners){
            bl.onBatteryLevelLow();
        }
    }
}
