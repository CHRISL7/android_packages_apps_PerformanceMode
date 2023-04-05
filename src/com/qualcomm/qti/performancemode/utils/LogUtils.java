package com.qualcomm.qti.performancemode.utils;

import android.util.Log;

public final class LogUtils {
    private static final String APP_TAG = "PerformanceMode";
    private static final int DEBUG = 3;
    private static final int ERROR = 6;

    private static boolean isEnabledLogging(int level) {
        return Log.isLoggable(APP_TAG, level);
    }

    private static String combineLogMessage(String tag, String message) {
        return tag + ": " + message;
    }

    /* renamed from: d */
    public static void m0d(String tag, String msg) {
        if (isEnabledLogging(DEBUG)) {
            Log.d(APP_TAG, combineLogMessage(tag, msg));
        }
    }

    /* renamed from: e */
    public static void m1e(String tag, String msg) {
        if (isEnabledLogging(ERROR)) {
            Log.e(APP_TAG, combineLogMessage(tag, msg));
        }
    }
}
