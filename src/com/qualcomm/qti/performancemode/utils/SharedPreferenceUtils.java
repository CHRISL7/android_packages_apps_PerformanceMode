package com.qualcomm.qti.performancemode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {
    private static final String PERFORMANCE_MODE_STATUS = "performance_mode_status";
    private static final String SESSION_HANDLE = "session_handle";
    private static final String TAG = SharedPreferenceUtils.class.getSimpleName();

    private static boolean isAtLeastN() {
        return Build.VERSION.SDK_INT >= 24;
    }

    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        Context storageContext;
        if (isAtLeastN()) {
            String name = PreferenceManager.getDefaultSharedPreferencesName(context);
            storageContext = context.createDeviceProtectedStorageContext();
            if (!storageContext.moveSharedPreferencesFrom(context, name)) {
                LogUtils.m1e(TAG, "Failed to migrate shared preferences");
            }
        } else {
            storageContext = context;
        }
        return PreferenceManager.getDefaultSharedPreferences(storageContext);
    }

    public static boolean getPerformanceModeStatus(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PERFORMANCE_MODE_STATUS, false);
    }

    public static void setPerformanceModeStatus(Context context, boolean isOn) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putBoolean(PERFORMANCE_MODE_STATUS, isOn);
        editor.apply();
    }

    public static int getSessionHandle(Context context) {
        return getDefaultSharedPreferences(context).getInt(SESSION_HANDLE, -1);
    }

    public static void setSessionHandle(Context context, int handle) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putInt(SESSION_HANDLE, handle);
        editor.apply();
    }
}
