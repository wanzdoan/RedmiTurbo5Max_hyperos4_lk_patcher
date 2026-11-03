package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class CLIRListPreference extends ListPreference {
    private final boolean DBG;
    public int[] clirArray;
    private boolean mConfigSupportNetworkDefault;
    private final String[] mEntries;
    private final MyHandler mHandler;
    private Phone mPhone;
    private TimeConsumingPreferenceListener mTcpListener;
    private final String[] mValues;

    public CLIRListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = true;
        this.mHandler = new MyHandler();
        this.mEntries = getContext().getResources().getStringArray(R.array.clir_display_values);
        this.mValues = getContext().getResources().getStringArray(R.array.clir_values);
    }

    public CLIRListPreference(Context context) {
        this(context, null);
    }

    @Override // android.preference.ListPreference, android.preference.DialogPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z && getValue() != null) {
            this.mPhone.setOutgoingCallerIdDisplay(convertValueToCLIRMode(getValue()), this.mHandler.obtainMessage(1));
            TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
            if (timeConsumingPreferenceListener != null) {
                timeConsumingPreferenceListener.onStarted(this, false);
                return;
            }
            return;
        }
        Log.d("CLIRListPreference", String.format("onDialogClosed: positiveResult=%b value=%s -- do nothing", Boolean.valueOf(z), getValue()));
    }

    public void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z, Phone phone) {
        this.mPhone = phone;
        this.mTcpListener = timeConsumingPreferenceListener;
        boolean z2 = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId()).getBoolean("support_clir_network_default_bool");
        this.mConfigSupportNetworkDefault = z2;
        if (!z2) {
            String[] strArr = this.mEntries;
            String[] strArr2 = {strArr[1], strArr[2]};
            String[] strArr3 = this.mValues;
            String[] strArr4 = {strArr3[1], strArr3[2]};
            setEntries(strArr2);
            setEntryValues(strArr4);
        }
        if (z) {
            return;
        }
        Log.i("CLIRListPreference", "init: requesting CLIR");
        this.mPhone.getOutgoingCallerIdDisplay(this.mHandler.obtainMessage(0, 0, 0));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener2 = this.mTcpListener;
        if (timeConsumingPreferenceListener2 != null) {
            timeConsumingPreferenceListener2.onStarted(this, true);
        }
    }

    public void handleGetCLIRResult(int[] iArr) {
        this.clirArray = iArr;
        int i = iArr[1];
        char c = 0;
        setEnabled(i == 1 || i == 3 || i == 4);
        int i2 = iArr[1];
        if (i2 == 1 || i2 == 3 || i2 == 4) {
            int i3 = iArr[0];
            if (i3 == 1) {
                c = 1;
            } else if (i3 == 2) {
                c = 2;
            }
        }
        if (!this.mConfigSupportNetworkDefault && c == 0) {
            c = 2;
        }
        setValue(this.mValues[c]);
        int i4 = R.string.sum_default_caller_id;
        if (c != 0) {
            if (c == 1) {
                i4 = R.string.sum_hide_caller_id;
            } else if (c == 2) {
                i4 = R.string.sum_show_caller_id;
            }
        }
        setSummary(i4);
    }

    private int convertValueToCLIRMode(String str) {
        if (this.mValues[1].equals(str)) {
            return 1;
        }
        return (!this.mValues[2].equals(str) && this.mConfigSupportNetworkDefault) ? 0 : 2;
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleGetCLIRResponse(message);
            } else {
                if (i != 1) {
                    return;
                }
                handleSetCLIRResponse(message);
            }
        }

        private void handleGetCLIRResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i = message.arg2;
            CLIRListPreference cLIRListPreference = CLIRListPreference.this;
            if (i == 1) {
                cLIRListPreference.mTcpListener.onFinished(CLIRListPreference.this, false);
            } else {
                cLIRListPreference.mTcpListener.onFinished(CLIRListPreference.this, true);
            }
            CLIRListPreference cLIRListPreference2 = CLIRListPreference.this;
            cLIRListPreference2.clirArray = null;
            if (asyncResult.exception != null) {
                Log.i("CLIRListPreference", "handleGetCLIRResponse: ar.exception=" + asyncResult.exception);
                CommandException commandException = asyncResult.exception;
                boolean z = commandException instanceof CommandException;
                CLIRListPreference cLIRListPreference3 = CLIRListPreference.this;
                if (z) {
                    cLIRListPreference3.handleCommandException(commandException);
                    return;
                } else {
                    cLIRListPreference3.mTcpListener.onError(CLIRListPreference.this, 400);
                    return;
                }
            }
            if (asyncResult.userObj instanceof Throwable) {
                Log.i("CLIRListPreference", "handleGetCLIRResponse: ar.throwable=" + asyncResult.userObj);
                CLIRListPreference.this.mTcpListener.onError(CLIRListPreference.this, 400);
                return;
            }
            int[] iArr = (int[]) asyncResult.result;
            if (iArr.length != 2) {
                cLIRListPreference2.mTcpListener.onError(CLIRListPreference.this, 400);
                return;
            }
            Log.i("CLIRListPreference", "handleGetCLIRResponse: CLIR successfully queried, clirArray[0]=" + iArr[0] + ", clirArray[1]=" + iArr[1]);
            CLIRListPreference.this.handleGetCLIRResult(iArr);
        }

        private void handleSetCLIRResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("CLIRListPreference", "handleSetCLIRResponse: ar.exception=" + asyncResult.exception);
            }
            Log.d("CLIRListPreference", "handleSetCLIRResponse: re get");
            CLIRListPreference.this.mPhone.getOutgoingCallerIdDisplay(obtainMessage(0, 1, 1, asyncResult.exception));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCommandException(CommandException commandException) {
        if (commandException.getCommandError() != CommandException.Error.REQUEST_NOT_SUPPORTED) {
            this.mTcpListener.onException(this, commandException);
            return;
        }
        Log.d("CLIRListPreference", "receive REQUEST_NOT_SUPPORTED CLIR !!");
        setEnabled(false);
        setSummary("");
    }
}
