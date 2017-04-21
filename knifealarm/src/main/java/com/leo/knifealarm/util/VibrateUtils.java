package com.leo.knifealarm.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Leo on 2017/4/18.
 */

public class VibrateUtils {

    private static Vibrator sVibrator;

    private static final long[] DEFAULT_PATTERN = new long[]{200, 600, 200, 600};

    private static final int DEFAULT_REPEAT = 2;

    public static void vibrate(Context context, long[] pattern, int repeat) {
        sVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        sVibrator.vibrate(pattern, repeat);
    }

    public static void vibrate(Context context, int repeat) {
        vibrate(context, DEFAULT_PATTERN, repeat);
    }

    public static void vibrate(Context context) {
        vibrate(context, DEFAULT_REPEAT);
    }

    public static void stop() {
        if (sVibrator != null) {
            sVibrator.cancel();
            sVibrator = null;
        }
    }
}
