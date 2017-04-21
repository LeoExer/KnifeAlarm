package com.leo.knifealarm.data;

import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2017/4/17.
 */

public class DataRespository {

    private static DataRespository sInstance;

    private Map<Integer, Alarm> mCache = new HashMap<>();

    private DataRespository() {
    }

    public static DataRespository getInstance() {
        if (sInstance == null) {
            sInstance = new DataRespository();
        }

        return sInstance;
    }

    public Alarm getAlarm(int alarmId) {
        if (mCache.containsKey(alarmId)) {
            return mCache.get(alarmId);
        }
        Alarm alarm = DataSupport.find(Alarm.class, alarmId);
        mCache.put(alarmId, alarm);
        return alarm;
    }

    public List<Alarm> loadAlarms() {
        List<Alarm> alarms = DataSupport.findAll(Alarm.class);
        mCache.clear();
        for (Alarm alarm: alarms) {
            mCache.put(alarm.getId(), alarm);
        }
        return alarms;
    }

    public void save(@NonNull Alarm alarm) {
        alarm.save();
        mCache.put(alarm.getId(), alarm);
    }

    public void delete(@NonNull Alarm alarm) {
        DataSupport.delete(Alarm.class, alarm.getId());
        mCache.remove(alarm.getId());
    }

    public void delete(int alarmId) {
        DataSupport.delete(Alarm.class, alarmId);
        mCache.remove(alarmId);
    }

    public void update(@NonNull Alarm alarm) {
        alarm.update(alarm.getId());
        mCache.put(alarm.getId(), alarm);
    }

}
