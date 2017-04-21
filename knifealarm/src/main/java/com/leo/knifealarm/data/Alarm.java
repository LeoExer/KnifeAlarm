package com.leo.knifealarm.data;

import android.net.Uri;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Leo on 2017/4/17.
 */

public class Alarm extends DataSupport implements Serializable {

    public Alarm() {
    }

    public Alarm(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    private int id;

    private int hour;

    private int minute;

    private String tag;

    private String audioPath;

    private boolean playAudio = true;

    private float volume;

    private boolean isRepeat;

    private int[] repeatDays = new int[7];

    private boolean isVibrate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public boolean isPlayAudio() {
        return playAudio;
    }

    public void setPlayAudio(boolean playAudio) {
        this.playAudio = playAudio;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public int[] getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(int[] repeatDays) {
        this.repeatDays = repeatDays;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < repeatDays.length; i++) {
            if (repeatDays[i] != 1) {
                continue;
            }

            int day = i + 1;
            if (temp.length() > 0) {
                temp.append(", ").append(day);
            } else {
                temp.append(day);
            }
        }
        StringBuilder result = new StringBuilder();
        result.append("-------- alarm start --------")
            .append("\nhour           --> " + hour)
            .append("\nminute         --> " + minute)
            .append("\ntag            --> " + tag)
            .append("\nis play music  --> " + playAudio)
            .append("\naudio path     --> " + audioPath)
            .append("\nvolume         --> " + volume)
            .append("\nis vabrate     --> " + isVibrate)
            .append("\nis repeat      --> " + isRepeat)
            .append("\ndays           --> " + temp.toString())
            .append("\n-------- alarm end --------");

        return result.toString();
    }
}
