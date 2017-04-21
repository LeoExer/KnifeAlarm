package com.leo.knifealarm.sample;

import android.app.Application;

import com.leo.knifealarm.KnifeAlarm;

/**
 * Created by Leo on 2017/4/20.
 */

public class AlarmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KnifeAlarm.getInstance().init(getApplicationContext());
    }
}
