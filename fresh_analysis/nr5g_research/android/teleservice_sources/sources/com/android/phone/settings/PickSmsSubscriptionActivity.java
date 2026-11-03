package com.android.phone.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.telephony.IIntegerConsumer;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PickSmsSubscriptionActivity extends Activity {
    private static final ComponentName SETTINGS_SUB_PICK_ACTIVITY = new ComponentName("com.android.settings", "com.android.settings.sim.SimDialogActivity");
    private static final List sSmsPickPendingList = new ArrayList();
    private boolean mPreviouslyStopped = false;

    public static void addPendingResult(IIntegerConsumer iIntegerConsumer) {
        List list = sSmsPickPendingList;
        synchronized (list) {
            list.add(iIntegerConsumer);
        }
        Log.i("PickSmsSubActivity", "queue pending result, token: " + iIntegerConsumer);
    }

    private static void sendResultAndClear(int i) {
        List<IIntegerConsumer> list = sSmsPickPendingList;
        synchronized (list) {
            for (IIntegerConsumer iIntegerConsumer : list) {
                try {
                    iIntegerConsumer.accept(i);
                    Log.i("PickSmsSubActivity", "Result received, token: " + iIntegerConsumer + ", result: " + i);
                } catch (RemoteException unused) {
                }
            }
            sSmsPickPendingList.clear();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        this.mPreviouslyStopped = false;
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        this.mPreviouslyStopped = false;
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.mPreviouslyStopped) {
            finishAndRemoveTask();
        } else {
            launchSmsPicker(new Intent(getIntent()));
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        this.mPreviouslyStopped = true;
        sendResultAndClear(-1);
        finishActivity(1);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            int intExtra = intent == null ? -1 : intent.getIntExtra("result_sub_id", -1);
            if (i2 == -1) {
                sendResultAndClear(intExtra);
            } else {
                sendResultAndClear(-1);
            }
        }
        if (this.mPreviouslyStopped) {
            return;
        }
        finishAndRemoveTask();
    }

    private void launchSmsPicker(Intent intent) {
        intent.setComponent(SETTINGS_SUB_PICK_ACTIVITY);
        intent.removeFlags(268435456);
        startActivityForResult(intent, 1);
    }
}
