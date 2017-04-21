package com.leo.knifealarm;

import com.leo.knifealarm.data.Alarm;

/**
 * Created by Leo on 2017/4/17.
 */

public class AlarmBuilder {

    private static final float DEFAULT_VOLUME = 0.5f;

    private Alarm mAlarm = null;

    public AlarmBuilder create() {
        mAlarm = new Alarm();
        return this;
    }

    public AlarmBuilder hour(int hour) {
        mAlarm.setHour(hour);
        return this;
    }

    public AlarmBuilder minute(int minute) {
        mAlarm.setMinute(minute);
        return this;
    }

    public AlarmBuilder tag(String tag) {
        mAlarm.setTag(tag);
        return this;
    }

    public AlarmBuilder audio(boolean enable, String musicPath, float volume) {
        mAlarm.setPlayAudio(enable);
        if (enable) {
            mAlarm.setAudioPath(musicPath);
            mAlarm.setVolume(volume);
        }
        return this;
    }

    public AlarmBuilder audio(boolean enable, String musicPath) {
        return audio(enable, musicPath, DEFAULT_VOLUME);
    }

    public AlarmBuilder vibrate(boolean enable) {
        mAlarm.setVibrate(enable);
        return this;
    }

    public AlarmBuilder repeat(boolean enable, int[] days) {
        mAlarm.setRepeat(enable);
        if (enable) {
            mAlarm.setRepeatDays(days);
        }
        return this;
    }

    public Alarm build() {
        return mAlarm;
    }
}
