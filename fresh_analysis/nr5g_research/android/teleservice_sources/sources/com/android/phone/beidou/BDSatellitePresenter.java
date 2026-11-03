package com.android.phone.beidou;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class BDSatellitePresenter {
    private static final String TAG = "BDSatellitePresenter";
    private static BDSatellitePresenter sBDSatellitePresenter;
    private BDSatelliteActivity mBDSatelliteActivity;
    private Context mContext;
    private boolean mRunningTask;
    private BDSatelliteMgr.SatInfoListener mSatInfoListener = new BDSatelliteMgr.SatInfoListener() { // from class: com.android.phone.beidou.BDSatellitePresenter.1
        @Override // com.android.phone.beidou.BDSatelliteMgr.SatInfoListener
        public void onSatInfoChanged(BDSatelliteDynInfo bDSatelliteDynInfo) {
            if (BDSatellitePresenter.this.mBDSatelliteActivity != null) {
                BDSatellitePresenter.this.mBDSatelliteActivity.refreshView(bDSatelliteDynInfo);
            }
        }

        @Override // com.android.phone.beidou.BDSatelliteMgr.SatInfoListener
        public void onAccuracyChanged(boolean z, int i, int i2, int i3) {
            if (BDSatellitePresenter.this.mBDSatelliteActivity != null) {
                BDSatellitePresenter.this.mBDSatelliteActivity.updateCalibrator(z, i, i2, i3);
            }
        }
    };
    private final BDSatelliteActivity.LifecycleCallback mLifecycleCallback = new BDSatelliteActivity.LifecycleCallback() { // from class: com.android.phone.beidou.BDSatellitePresenter.2
        @Override // com.android.phone.beidou.BDSatelliteActivity.LifecycleCallback
        public void onActivityResumed() {
            BDSatelliteMgr.getInstance().registerSatInfoListener(BDSatellitePresenter.this.mSatInfoListener);
        }

        @Override // com.android.phone.beidou.BDSatelliteActivity.LifecycleCallback
        public void onActivityPaused() {
            BDSatelliteMgr.getInstance().unregisterSatInfoListener(BDSatellitePresenter.this.mSatInfoListener);
        }
    };

    private BDSatellitePresenter() {
    }

    public static BDSatellitePresenter getInstance() {
        if (sBDSatellitePresenter == null) {
            sBDSatellitePresenter = new BDSatellitePresenter();
        }
        return sBDSatellitePresenter;
    }

    public void setUp(Context context) {
        this.mContext = context;
    }

    public void showAliSatellite() {
        Log.i(TAG, "showAliSatellite enter.");
        this.mRunningTask = true;
        Context context = this.mContext;
        if (context != null) {
            context.startActivity(getAliSateIntent());
        }
    }

    public Intent getAliSateIntent() {
        Intent intent = new Intent(this.mContext, (Class<?>) BDSatelliteActivity.class);
        intent.setFlags(268435456);
        return intent;
    }

    public BDSatelliteActivity.LifecycleCallback getLifecycleCallback() {
        return this.mLifecycleCallback;
    }

    public void closeAliSatellite() {
        String str = TAG;
        Log.i(str, "closeAliSatellite enter.");
        this.mRunningTask = false;
        if (this.mBDSatelliteActivity != null) {
            Log.i(str, "closeAliSatellite finish Activity.");
            this.mBDSatelliteActivity.finishAndRemoveTask();
        }
    }

    public void setActivity(BDSatelliteActivity bDSatelliteActivity) {
        if (bDSatelliteActivity == null) {
            Folme$$ExternalSyntheticBUOutline0.m("registerActivity cannot be called with null");
            return;
        }
        BDSatelliteActivity bDSatelliteActivity2 = this.mBDSatelliteActivity;
        if (bDSatelliteActivity2 != null && bDSatelliteActivity2 != bDSatelliteActivity) {
            Log.wtf(TAG, "Setting a second activity before destroying the first.");
        }
        this.mBDSatelliteActivity = bDSatelliteActivity;
    }

    public void unsetActivity(BDSatelliteActivity bDSatelliteActivity) {
        if (bDSatelliteActivity == null) {
            Folme$$ExternalSyntheticBUOutline0.m("unregisterActivity cannot be called with null");
            return;
        }
        BDSatelliteActivity bDSatelliteActivity2 = this.mBDSatelliteActivity;
        if (bDSatelliteActivity2 == null) {
            Log.i(TAG, "No BDSatelliteActivity currentlyset, no need to unset.");
        } else if (bDSatelliteActivity2 != bDSatelliteActivity) {
            Log.w(TAG, "Second instance of BDSatelliteActivity is trying to unregister when another instance is active. Ignoring.");
        } else {
            this.mBDSatelliteActivity = null;
        }
    }

    public void manualLeaveSatMode() {
        if (this.mRunningTask) {
            BdSmsController.getInstance().closeBdService();
        }
    }

    public BDSatelliteActivity getActivity() {
        return this.mBDSatelliteActivity;
    }
}
