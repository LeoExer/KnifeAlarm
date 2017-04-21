package com.leo.knifealarm.util;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2017/4/21.
 */

public class RingtoneUtils {

    public static List<String> getAllRingtoneTitle(Context context, int type) {
        List<String> titles = new ArrayList<>();
        RingtoneManager rm = new RingtoneManager(context);
        rm.setType(type);
        Cursor cursor = rm.getCursor();
        if (cursor.moveToFirst()) {
            do {
                titles.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }
        return titles;
    }

    public static Ringtone getRingtoneByPosition(Context context, int type, int position) {
        RingtoneManager rm = new RingtoneManager(context);
        rm.setType(type);
        rm.getCursor(); // 不能缺少
        return rm.getRingtone(position);
    }

    public static Uri getRingtoneUriByPosition(Context context, int type, int position) {
        RingtoneManager rm = new RingtoneManager(context);
        rm.setType(type);
        rm.getCursor(); // 不能缺少
        return rm.getRingtoneUri(position);
    }

    public static Uri getDefaultRingtoneUri(Context context, int type) {
        return RingtoneManager.getActualDefaultRingtoneUri(context, type);
    }

    public static void setDefaultRingtoneUri(Context context, int type, Uri ringtoneUri) {
        RingtoneManager.setActualDefaultRingtoneUri(context, type, ringtoneUri);
    }

    public static int getRingtonePositionByUri(Context context, int type, Uri ringtoneUri) {
        RingtoneManager rm = new RingtoneManager(context);
        rm.setType(type);
        rm.getCursor(); // 不能缺少
        return rm.getRingtonePosition(ringtoneUri);
    }
}
