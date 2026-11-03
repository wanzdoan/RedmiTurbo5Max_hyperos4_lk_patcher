package com.android.phone.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import com.android.internal.telephony.CommandException;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public abstract class TimeConsumingPreferenceActivity extends BaseActivity implements DialogInterface.OnCancelListener {
    private boolean mEnterAnimationComplete = false;
    private Integer mPendingDialogId = null;
    private TimeConsumingPreferenceFragment mTimeConsumingPreferenceFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.settings.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        TimeConsumingPreferenceFragment timeConsumingPreferenceFragment = (TimeConsumingPreferenceFragment) getSupportFragmentManager().findFragmentByTag("miuix.preference.PreferenceFragment.MAIN_FRAGMENT_TAG");
        this.mTimeConsumingPreferenceFragment = timeConsumingPreferenceFragment;
        if (bundle == null && timeConsumingPreferenceFragment == null) {
            TimeConsumingPreferenceFragment timeConsumingPreferenceFragment2 = new TimeConsumingPreferenceFragment();
            this.mTimeConsumingPreferenceFragment = timeConsumingPreferenceFragment2;
            loadFragment(timeConsumingPreferenceFragment2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.mEnterAnimationComplete = false;
        this.mPendingDialogId = null;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle == null || !bundle.containsKey("android:savedDialogs")) {
            return;
        }
        bundle.remove("android:savedDialogs");
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        int i2;
        int i3;
        if (i == 100 || i == 200) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            if (i == 100) {
                i2 = R.string.reading_settings;
                progressDialog.setOnCancelListener(this);
            } else {
                i2 = R.string.updating_settings;
            }
            progressDialog.setMessage(getText(i2));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setEnableImmersive(false);
            progressDialog.setCanceledOnTouchOutside(false);
            return progressDialog;
        }
        if (i != 400 && i != 500 && i != 300 && i != 600 && i != 700 && i != 800 && i != 900) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int i4 = R.string.error_updating_title;
        if (i == 300) {
            i3 = R.string.exception_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else if (i == 400) {
            i3 = R.string.response_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else if (i == 500) {
            i3 = R.string.radio_off_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismissAndFinish);
        } else if (i == 600) {
            i3 = R.string.fdn_check_failure;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else if (i == 700) {
            i3 = R.string.stk_cc_ss_to_dial_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else if (i == 800) {
            i3 = R.string.stk_cc_ss_to_ussd_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else if (i == 900) {
            i3 = R.string.stk_cc_ss_to_ss_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        } else {
            i3 = R.string.exception_error;
            builder.setPositiveButton(R.string.close_dialog, this.mTimeConsumingPreferenceFragment.mDismiss);
        }
        builder.setTitle(getText(i4));
        builder.setMessage(getText(i3));
        builder.setCancelable(false);
        builder.setEnableDialogImmersive(false);
        return builder.create();
    }

    @Override // android.app.Activity
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        this.mEnterAnimationComplete = true;
        Integer num = this.mPendingDialogId;
        if (num != null) {
            showDialog(num.intValue());
            this.mPendingDialogId = null;
        }
    }

    void showDialogSafely(int i) {
        if (this.mEnterAnimationComplete || !isInFloatingWindowMode()) {
            showDialog(i);
        } else {
            this.mPendingDialogId = Integer.valueOf(i);
        }
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        if (this.mTimeConsumingPreferenceFragment.DBG) {
            this.mTimeConsumingPreferenceFragment.dumpState();
        }
        finish();
    }

    public class TimeConsumingPreferenceFragment extends BaseActivity.BaseFragment implements TimeConsumingPreferenceListener {
        private boolean DBG;
        private final ArrayList mBusyList;
        public final DialogInterface.OnClickListener mDismiss;
        public final DialogInterface.OnClickListener mDismissAndFinish;
        private Handler mHandler = new Handler() { // from class: com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                TimeConsumingPreferenceFragment.this.dismissDialogSafely(((Boolean) message.obj).booleanValue());
            }
        };
        protected boolean mIsForeground;
        private final BroadcastReceiver mReceiver;
        private int mSlotId;
        private long startedTimeMillis;

        public TimeConsumingPreferenceFragment() {
            this.mDismiss = new DismissOnClickListener();
            this.mDismissAndFinish = new DismissAndFinishOnClickListener();
            this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
            this.mBusyList = new ArrayList();
            this.mIsForeground = false;
            this.mSlotId = 0;
            this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.settings.TimeConsumingPreferenceActivity.TimeConsumingPreferenceFragment.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                        TimeConsumingPreferenceFragment.this.setScreenEnabled();
                    }
                    if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction()) && TimeConsumingPreferenceFragment.this.mSlotId == SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.getDefault().getDefaultSlotId()) && "ABSENT".equals(intent.getStringExtra("ss")) && TimeConsumingPreferenceFragment.this.getActivity() != null) {
                        TimeConsumingPreferenceFragment.this.getActivity().finish();
                    }
                }
            };
        }

        class DismissOnClickListener implements DialogInterface.OnClickListener {
            private DismissOnClickListener(TimeConsumingPreferenceFragment timeConsumingPreferenceFragment) {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }

        class DismissAndFinishOnClickListener implements DialogInterface.OnClickListener {
            private DismissAndFinishOnClickListener() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (TimeConsumingPreferenceFragment.this.getActivity() != null) {
                    TimeConsumingPreferenceFragment.this.getActivity().finish();
                }
            }
        }

        protected void setScreenEnabled() {
            getPreferenceScreen().setEnabled(!isAirplaneOn());
        }

        protected boolean isAirplaneOn() {
            return Settings.Global.getInt(getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
        }

        @Override // com.android.phone.settings.BaseActivity.BaseFragment, androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mSlotId = SubscriptionManager.getSlotIdExtra(getActivity().getIntent(), SubscriptionManager.getDefault().getDefaultSlotId());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            getContext().registerReceiver(this.mReceiver, intentFilter, 2);
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            this.mIsForeground = true;
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            this.mIsForeground = false;
        }

        @Override // androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            getContext().unregisterReceiver(this.mReceiver);
        }

        @Override // com.android.phone.settings.TimeConsumingPreferenceListener
        public void onStarted(Preference preference, boolean z) {
            if (this.DBG) {
                dumpState();
            }
            if (this.DBG) {
                Log.d("TimeConsumingPreferenceActivity", "onStarted, preference=" + preference.getKey() + ", reading=" + z);
            }
            this.mBusyList.add(preference.getKey());
            if (!this.mIsForeground || getActivity() == null) {
                return;
            }
            this.startedTimeMillis = System.currentTimeMillis();
            ((TimeConsumingPreferenceActivity) getActivity()).showDialogSafely(z ? 100 : 200);
        }

        public void onFinished(Preference preference, boolean z) {
            if (this.DBG) {
                dumpState();
            }
            if (this.DBG) {
                Log.d("TimeConsumingPreferenceActivity", "onFinished, preference=" + preference.getKey() + ", reading=" + z);
            }
            this.mBusyList.remove(preference.getKey());
            if (this.mBusyList.isEmpty()) {
                if (System.currentTimeMillis() - this.startedTimeMillis < 1000) {
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(handler.obtainMessage(1, Boolean.valueOf(z)), 1000L);
                } else {
                    dismissDialogSafely(z);
                }
            }
            preference.setEnabled(true);
        }

        public void onError(Preference preference, int i) {
            if (this.DBG) {
                dumpState();
            }
            if (this.DBG) {
                Log.d("TimeConsumingPreferenceActivity", "onError, preference=" + preference.getKey() + ", error=" + i);
            }
            if (this.mIsForeground && getActivity() != null) {
                getActivity().showDialog(i);
            }
            preference.setEnabled(false);
        }

        public void onException(Preference preference, CommandException commandException) {
            if (commandException.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
                onError(preference, 600);
            } else {
                preference.setEnabled(false);
                onError(preference, MiRilHook.HOOK_ID_START_URC_BY_AT);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dismissDialogSafely(boolean z) {
            try {
                if (getActivity() == null) {
                    return;
                }
                if (z) {
                    getActivity().dismissDialog(100);
                    getActivity().removeDialog(100);
                } else {
                    getActivity().dismissDialog(200);
                    getActivity().removeDialog(200);
                }
            } catch (IllegalArgumentException unused) {
            }
        }

        void dumpState() {
            Log.d("TimeConsumingPreferenceActivity", "dumpState begin");
            ArrayList arrayList = this.mBusyList;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                Log.d("TimeConsumingPreferenceActivity", "mBusyList: key=" + ((String) obj));
            }
            Log.d("TimeConsumingPreferenceActivity", "dumpState end");
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
        }
    }
}
