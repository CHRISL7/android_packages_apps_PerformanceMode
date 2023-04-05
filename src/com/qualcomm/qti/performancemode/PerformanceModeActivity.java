package com.qualcomm.qti.performancemode;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.qualcomm.qti.performancemode.api.PerformanceModeManager;
import com.qualcomm.qti.performancemode.utils.LogUtils;
import com.qualcomm.qti.performancemode.utils.SharedPreferenceUtils;

public class PerformanceModeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = PerformanceModeActivity.class.getSimpleName();
    private Context mAppContext;
    private Button mButtonOff;
    private Button mButtonOn;
    private boolean mIsPerformanceModeOn;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0000R.layout.activity_performance_mode);
        this.mAppContext = getApplicationContext();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Button button = (Button) findViewById(C0000R.C0001id.button_on);
        this.mButtonOn = button;
        button.setOnClickListener(this);
        Button button2 = (Button) findViewById(C0000R.C0001id.button_off);
        this.mButtonOff = button2;
        button2.setOnClickListener(this);
        boolean performanceModeStatus = SharedPreferenceUtils.getPerformanceModeStatus(this.mAppContext);
        this.mIsPerformanceModeOn = performanceModeStatus;
        setVisibility(performanceModeStatus);
        if (!PerformanceModeManager.getInstance().isPerformanceModeSupport()) {
            LogUtils.m0d(TAG, "onCreate: button is disabled");
            this.mButtonOn.setEnabled(false);
            this.mButtonOff.setEnabled(false);
            return;
        }
        tryTurnOnPerformanceMode(this.mAppContext);
    }

    public boolean onNavigateUp() {
        if (super.onNavigateUp()) {
            return true;
        }
        finish();
        return true;
    }

    public void onClick(View v) {
        if (v.getId() == C0000R.C0001id.button_on) {
            int handle = PerformanceModeManager.getInstance().turnOnPerformanceMode();
            if (-1 != handle) {
                setVisibility(true);
                SharedPreferenceUtils.setSessionHandle(this.mAppContext, handle);
                SharedPreferenceUtils.setPerformanceModeStatus(this.mAppContext, true);
                return;
            }
            Toast.makeText(this.mAppContext, C0000R.string.turn_on_fail, 0).show();
            return;
        }
        int handle2 = SharedPreferenceUtils.getSessionHandle(this.mAppContext);
        if (-1 == handle2) {
            Toast.makeText(this.mAppContext, C0000R.string.turn_off_invalid_handle, 0).show();
        } else if (-1 != PerformanceModeManager.getInstance().turnOffPerformanceMode(handle2)) {
            setVisibility(false);
            SharedPreferenceUtils.setSessionHandle(this.mAppContext, -1);
            SharedPreferenceUtils.setPerformanceModeStatus(this.mAppContext, false);
        } else {
            Toast.makeText(this.mAppContext, C0000R.string.turn_off_fail, 0).show();
        }
    }

    public boolean isPerformanceModeOn() {
        return this.mIsPerformanceModeOn;
    }

    public void setVisibility(boolean isOn) {
        this.mIsPerformanceModeOn = isOn;
        if (isOn) {
            this.mButtonOn.setVisibility(8);
            this.mButtonOff.setVisibility(0);
            return;
        }
        this.mButtonOn.setVisibility(0);
        this.mButtonOff.setVisibility(8);
    }

    private void tryTurnOnPerformanceMode(Context appContext) {
        int lastHandle = SharedPreferenceUtils.getSessionHandle(appContext);
        if (isPerformanceModeOn() && -1 == lastHandle) {
            int handle = PerformanceModeManager.getInstance().turnOnPerformanceMode();
            LogUtils.m0d(TAG, "tryTurnOnPerformanceMode: turn on result handle = " + handle);
            if (-1 != handle) {
                SharedPreferenceUtils.setSessionHandle(appContext, handle);
            }
        }
    }
}
