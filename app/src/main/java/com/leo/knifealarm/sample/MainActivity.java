package com.leo.knifealarm.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leo.knifealarm.KnifeAlarm;
import com.leo.knifealarm.OnAlarmListener;
import com.leo.knifealarm.data.Alarm;
import com.leo.knifealarm.view.AlarmSettingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KnifeAlarm.getInstance().setOnAlarmListener(new OnAlarmListener() {
            @Override
            public void onAlarm(Alarm alarm) {
                Log.i("TEST", "on listener");
                Log.i("TEST", alarm.toString());

                KnifeAlarm.getInstance().playAudio(alarm);
                KnifeAlarm.getInstance().vibrate(MainActivity.this, alarm);
            }
        });
    }

    public void test(View view) {
        startActivity(new Intent(this, AlarmSettingActivity.class));
    }
}
