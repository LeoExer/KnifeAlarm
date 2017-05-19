package com.leo.knifealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.leo.knifealarm.data.Alarm;
import com.leo.knifealarm.data.DataRespository;
import com.leo.knifealarm.listener.OnAlarmListener;
import com.leo.knifealarm.listener.OnAudioPlayingListener;
import com.leo.knifealarm.util.AudioPlayer;
import com.leo.knifealarm.util.UriValidator;
import com.leo.knifealarm.util.VibrateUtils;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Leo on 2017/4/17.
 */

public class KnifeAlarm {

    public static final long MILLIS_OF_DAY = 24 * 3600 * 1000;
    public static final long MILLIS_OF_WEEK = 7 * MILLIS_OF_DAY;

    public static final String ALARM_ACTION = "com.leo.knifealarm.ALARM_ACTION";

    private Context mContext;

    private AlarmManager mAlarmManager = null;

    private OnAlarmListener mListener = null;

    private static KnifeAlarm sInstance = null;

    private AudioPlayer mAudioPlayer;

    private KnifeAlarm() {
    }

    public static KnifeAlarm getInstance() {
        if (sInstance == null) {
            sInstance = new KnifeAlarm();
        }
        return sInstance;
    }

    /**
     * 初始化
     * @param context Application context
     */
    public void init(@NonNull Context context) {
        mContext = context;
        LitePal.initialize(context); // init litepal
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(@NonNull Alarm alarm) {
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("alarm", alarm);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, alarm.getId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long curTime = System.currentTimeMillis();
        Log.i("TEST", "------ sys cur time: " + curTime + " -------");
        test(curTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0); // set second to 0
        long time = calendar.getTimeInMillis();
        Log.i("TEST", "------ set time: " + time + " -------");
        test(time);

        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(curTime);

        boolean alarmAllDay = true;
        for (int i : alarm.getRepeatDays()) {
            if (i == 0) {
                alarmAllDay = false;
                break;
            }
        }

        long finalTime = time;
        if (alarm.isRepeat()) {
            Log.i("TEST", "repeat");
            if (alarmAllDay) { // alarm every day
                Log.i("TEST", "repeat all day");
                if (time <= curTime) {
                    finalTime += MILLIS_OF_DAY; // delay one day;
                }
                test(finalTime);
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, finalTime, MILLIS_OF_DAY, sender);
            } else { // alarm custom days
                Log.i("TEST", "repeat custom days");
                int[] days = alarm.getRepeatDays();
                int len = days.length;
                int curDayOfWeek = curCalendar.get(Calendar.DAY_OF_WEEK);
                for (int i = 0; i < len; i++) {
                    long newFinalTime = finalTime;
                    int nowDay = days[i];
                    if (nowDay == 1) { // set alarm
                        int dayCount = (i + 1) - curDayOfWeek; // sun = 1; mon = 2 ...
                        Log.i("TEST", "pos: " + i + ", day count: " + dayCount);
                        if (dayCount < 0) {
                            newFinalTime += (dayCount + 7) * MILLIS_OF_DAY;
                        } else if (dayCount == 0) { // today
                            if (time <= curTime) {
                                newFinalTime += MILLIS_OF_WEEK; // delay one week;
                            }
                        } else {
                            newFinalTime += dayCount * MILLIS_OF_DAY;
                        }
                        test(newFinalTime);
                        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, newFinalTime,
                                MILLIS_OF_WEEK, sender);
                    }
                }
            }
        } else { // alarm once
            Log.i("TEST", "no repeat");
            if (time <= curTime) {
                finalTime += MILLIS_OF_DAY; // delay one day;
            }
            test(finalTime);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, finalTime, sender);
        }

        // save alarm
        DataRespository.getInstance().save(alarm);
    }

    public void cancelAlarm(int alarmId) {
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, alarmId,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.cancel(pi);

        DataRespository.getInstance().delete(alarmId);
    }

    public List<Alarm> getAllAlarms() {
        return DataRespository.getInstance().loadAlarms();
    }

    public void playAudio(Alarm alarm) {
        playAudio(alarm, null);
    }

    public void playAudio(Alarm alarm, OnAudioPlayingListener listener) {
        releaseAudioPlayer();
        if (alarm.isPlayAudio()) {
            String audioPath = alarm.getAudioPath();
            if (audioPath == null || audioPath.length() == 0) {
                return;
            }

            mAudioPlayer = new AudioPlayer(mContext);
            Log.i("TEST", "play audioPath: " + audioPath);
            UriValidator uriValidator = new UriValidator(audioPath);
            if (uriValidator.isVaild()) { // uri
                Log.i("TEST", "play uri");
                mAudioPlayer.play(Uri.parse(audioPath), true, alarm.getVolume(), listener);
            } else { // file or http url
                mAudioPlayer.play(audioPath, true, alarm.getVolume(), listener);
            }
        }
    }

    public void stopAudio() {
        releaseAudioPlayer();
    }

    private void releaseAudioPlayer() {
        if (mAudioPlayer != null) {
            if (mAudioPlayer.isPlaying()) {
                mAudioPlayer.stop();
            }
            mAudioPlayer = null;
        }
    }

    public void vibrate(Context context, Alarm alarm) {
        if (alarm.isVibrate()) {
            VibrateUtils.vibrate(context);
        }
    }

    public void stopVibrate() {
        VibrateUtils.stop();
    }

    public void setOnAlarmListener(OnAlarmListener listener) {
        this.mListener = listener;
    }

    public OnAlarmListener getOnAlarmListener() {
        return mListener;
    }


    private void test(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Log.i("TEST", "dayOfMonth: " + calendar.get(Calendar.DAY_OF_MONTH));
        Log.i("TEST", "dayOfWeek: " + calendar.get(Calendar.DAY_OF_WEEK));
        Log.i("TEST", "hourOfDay: " + calendar.get(Calendar.HOUR_OF_DAY));
        Log.i("TEST", "minute: " + calendar.get(Calendar.MINUTE));
        Log.i("TEST", "second: " + calendar.get(Calendar.SECOND));
    }
}
