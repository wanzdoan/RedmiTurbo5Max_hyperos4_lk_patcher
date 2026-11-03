package com.android.phone.beidou;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import com.android.phone.R;
import com.xiaomi.internal.telephony.DynamicSarService;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class BDSatelliteActivity extends Activity {
    private static final String TAG = "BDSatelliteActivity";
    private Dialog mAliSatDialog;
    private LifecycleCallback mLifecycleCallback;
    private BDSatelliteLayout mSatLayout;
    private int mSatMode;

    public interface LifecycleCallback {
        void onActivityPaused();

        void onActivityResumed();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate...");
        super.onCreate(bundle);
        this.mLifecycleCallback = BDSatellitePresenter.getInstance().getLifecycleCallback();
        this.mSatMode = BDSatelliteMgr.getInstance().mSatMode;
    }

    @Override // android.app.Activity
    protected void onStart() {
        Log.i(TAG, "onStart...");
        super.onStart();
        BDSatellitePresenter.getInstance().setActivity(this);
    }

    @Override // android.app.Activity
    protected void onResume() {
        Log.i(TAG, "onResume...");
        super.onResume();
        if (BdSmsController.getInstance().getPhoneBdServiceState() == 0) {
            finishAndRemoveTask();
            return;
        }
        getWindow().addFlags(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        showAliSatDialog();
        LifecycleCallback lifecycleCallback = this.mLifecycleCallback;
        if (lifecycleCallback != null) {
            lifecycleCallback.onActivityResumed();
        }
    }

    public void refreshView(BDSatelliteDynInfo bDSatelliteDynInfo) {
        BDSatelliteLayout bDSatelliteLayout;
        if (bDSatelliteDynInfo == null || (bDSatelliteLayout = this.mSatLayout) == null) {
            return;
        }
        bDSatelliteLayout.updateGuidanceViews(bDSatelliteDynInfo);
    }

    @Override // android.app.Activity
    protected void onPause() {
        Log.i(TAG, "onPause...");
        super.onPause();
        LifecycleCallback lifecycleCallback = this.mLifecycleCallback;
        if (lifecycleCallback != null) {
            lifecycleCallback.onActivityPaused();
        }
        Dialog dialog = this.mAliSatDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mAliSatDialog.dismiss();
        }
        BDSatelliteLayout bDSatelliteLayout = this.mSatLayout;
        if (bDSatelliteLayout != null) {
            bDSatelliteLayout.stopTimer();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        Log.i(TAG, "onStop -> manual leave satellite!");
        super.onStop();
        getWindow().clearFlags(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
        BDSatellitePresenter.getInstance().manualLeaveSatMode();
        BDSatellitePresenter.getInstance().unsetActivity(this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.i(TAG, "onDestroy...");
        super.onDestroy();
    }

    private void showAliSatDialog() {
        if (this.mAliSatDialog != null) {
            return;
        }
        AlertDialog alertDialogCreate = initDialogBuilder().setNegativeButton(getButtonRes(this.mSatMode, -2), createClickRunnable()).create();
        this.mAliSatDialog = alertDialogCreate;
        Window window = alertDialogCreate.getWindow();
        if (window != null) {
            window.setGravity(80);
        }
        this.mAliSatDialog.show();
        this.mSatLayout = (BDSatelliteLayout) this.mAliSatDialog.findViewById(R.id.align_satellite_layout);
    }

    private DialogInterface.OnClickListener createClickRunnable() {
        return new DialogInterface.OnClickListener() { // from class: com.android.phone.beidou.BDSatelliteActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.lambda$createClickRunnable$0(dialogInterface, i);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createClickRunnable$0(DialogInterface dialogInterface, int i) {
        Log.i(TAG, "click -> manual leave satellite!");
        finish();
    }

    private AlertDialog.Builder initDialogBuilder() {
        return new AlertDialog.Builder(this, R$style.AlertDialog_Theme_Dark).setCancelable(false).setView(getLayoutInflater().inflate(R.layout.satellite_layout, (ViewGroup) null));
    }

    public int getButtonRes(int i, int i2) {
        if (i2 == -2) {
            return R.string.satellite_dialog_cancel;
        }
        Folme$$ExternalSyntheticBUOutline0.m("unknown button id.");
        return 0;
    }

    public void updateCalibrator(boolean z, int i, int i2, int i3) {
        BDSatelliteLayout bDSatelliteLayout = this.mSatLayout;
        if (bDSatelliteLayout != null) {
            bDSatelliteLayout.updateMagneticCalibrate(z, i, i2, i3);
        }
    }
}
