package com.leo.knifealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.leo.knifealarm.data.Alarm;
import com.leo.knifealarm.listener.OnAlarmListener;

import java.util.Calendar;

/**
 * Created by Leo on 2017/4/17.
 */

public class KnifeAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        test();
        String action = intent.getAction();
        if (KnifeAlarm.ALARM_ACTION.equalsIgnoreCase(action)) {
            OnAlarmListener listener = KnifeAlarm.getInstance().getOnAlarmListener();
            if (listener != null) {
                Alarm alarm = (Alarm)intent.getSerializableExtra("alarm");
                listener.onAlarm(alarm);
            }
        }
    }

    private void test() {
        Calendar c = Calendar.getInstance();
        Log.i("TEST", "--------- on alarm -----------");
        Log.i("TEST", "dayOfMonth: " + c.get(Calendar.DAY_OF_MONTH));
        Log.i("TEST", "dayOfWeek: " + c.get(Calendar.DAY_OF_WEEK));
        Log.i("TEST", "hourOfDay: " + c.get(Calendar.HOUR_OF_DAY));
        Log.i("TEST", "minute: " + c.get(Calendar.MINUTE));
        Log.i("TEST", "second: " + c.get(Calendar.SECOND));
    }
}
