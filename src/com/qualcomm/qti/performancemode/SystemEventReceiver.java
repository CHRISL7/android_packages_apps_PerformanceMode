package com.qualcomm.qti.performancemode;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.qualcomm.qti.performancemode.api.PerformanceModeManager;
import com.qualcomm.qti.performancemode.utils.LogUtils;
import com.qualcomm.qti.performancemode.utils.SharedPreferenceUtils;

public class SystemEventReceiver extends BroadcastReceiver {
    private static final String TAG = SystemEventReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        Context appContext = context.getApplicationContext();
        boolean isOnMode = SharedPreferenceUtils.getPerformanceModeStatus(appContext);
        String str = TAG;
        LogUtils.m0d(str, "onReceive: intentAction = " + intentAction + " isOnMode = " + isOnMode);
        if ("android.intent.action.BOOT_COMPLETED".equals(intentAction) || "android.intent.action.LOCKED_BOOT_COMPLETED".equals(intentAction)) {
            boolean isSupport = PerformanceModeManager.getInstance().isPerformanceModeSupport();
            if (!isSupport || !isOnMode) {
                SharedPreferenceUtils.setSessionHandle(appContext, -1);
            } else {
                tryTurnOnPerformanceMode(appContext);
            }
            enablePerformanceModeActivity(appContext, isSupport);
        } else if ("android.intent.action.ACTION_SHUTDOWN".equals(intentAction) && isOnMode) {
            LogUtils.m0d(str, "onReceive: shutdown set session handle to -1");
            SharedPreferenceUtils.setSessionHandle(appContext, -1);
        }
    }

    private void enablePerformanceModeActivity(Context context, boolean enable) {
        LogUtils.m0d(TAG, "enablePerformanceModeActivity: enable = " + enable);
        PackageManager pm = context.getPackageManager();
        ComponentName name = new ComponentName(context, PerformanceModeActivity.class);
        if (enable) {
            pm.setComponentEnabledSetting(name, 1, 1);
        } else {
            pm.setComponentEnabledSetting(name, 2, 0);
        }
    }

    private void tryTurnOnPerformanceMode(Context context) {
        PerformanceModeManager manager = PerformanceModeManager.getInstance();
        Context appContext = context.getApplicationContext();
        int lastHandle = SharedPreferenceUtils.getSessionHandle(appContext);
        if (-1 != lastHandle) {
            LogUtils.m0d(TAG, "onReceive:tryTurnOnPerformanceMode: lastHandle = " + lastHandle + " turn off result = " + manager.turnOffPerformanceMode(lastHandle));
        }
        int currentHandle = manager.turnOnPerformanceMode();
        LogUtils.m0d(TAG, "onReceive:tryTurnOnPerformanceMode: currentHandle = " + currentHandle);
        if (-1 != currentHandle) {
            SharedPreferenceUtils.setSessionHandle(appContext, currentHandle);
        } else {
            SharedPreferenceUtils.setSessionHandle(appContext, -1);
        }
    }
}
