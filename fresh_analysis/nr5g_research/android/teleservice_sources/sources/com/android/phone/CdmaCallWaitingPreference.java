package com.android.phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.SuppServicesUiUtil;

/* JADX INFO: loaded from: classes.dex */
public class CdmaCallWaitingPreference extends Preference {
    private Context mContext;
    private MyHandler mHandler;
    private boolean mIsActionAvailable;
    private Phone mPhone;
    private TimeConsumingPreferenceListener mTcpListener;

    public CdmaCallWaitingPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new MyHandler();
        this.mIsActionAvailable = true;
        this.mContext = context;
    }

    public CdmaCallWaitingPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.preferenceStyle);
    }

    public CdmaCallWaitingPreference(Context context) {
        this(context, null);
    }

    @Override // android.preference.Preference
    public void onClick() {
        final PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        if (carrierConfigForSubId.getBoolean("call_waiting_over_ut_warning_bool", true) && SuppServicesUiUtil.isSsOverUtPrecautions(getContext(), this.mPhone)) {
            SuppServicesUiUtil.showBlockingSuppServicesDialog(getContext(), this.mPhone, getKey()).show();
            return;
        }
        super.onClick();
        AlertDialog.Builder builderMakeAlertDialogBuilder = FrameworksUtils.makeAlertDialogBuilder(this.mContext);
        builderMakeAlertDialogBuilder.setTitle(this.mContext.getText(R.string.cdma_call_waiting));
        builderMakeAlertDialogBuilder.setMessage(this.mContext.getText(R.string.enable_cdma_call_waiting_setting));
        builderMakeAlertDialogBuilder.setPositiveButton(R.string.enable_cdma_cw, new DialogInterface.OnClickListener() { // from class: com.android.phone.CdmaCallWaitingPreference.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (carrierConfigForSubId.getBoolean("call_waiting_over_ut_warning_bool", true) && SuppServicesUiUtil.isSsOverUtPrecautions(CdmaCallWaitingPreference.this.getContext(), CdmaCallWaitingPreference.this.mPhone)) {
                    SuppServicesUiUtil.showBlockingSuppServicesDialog(CdmaCallWaitingPreference.this.getContext(), CdmaCallWaitingPreference.this.mPhone, "call_waiting_key").show();
                    return;
                }
                CdmaCallWaitingPreference.this.mPhone.setCallWaiting(true, CdmaCallWaitingPreference.this.mHandler.obtainMessage(1));
                if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                    CdmaCallWaitingPreference.this.mTcpListener.onStarted(CdmaCallWaitingPreference.this, false);
                }
            }
        });
        builderMakeAlertDialogBuilder.setNegativeButton(R.string.disable_cdma_cw, new DialogInterface.OnClickListener() { // from class: com.android.phone.CdmaCallWaitingPreference.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (carrierConfigForSubId.getBoolean("call_waiting_over_ut_warning_bool", true) && SuppServicesUiUtil.isSsOverUtPrecautions(CdmaCallWaitingPreference.this.getContext(), CdmaCallWaitingPreference.this.mPhone)) {
                    SuppServicesUiUtil.showBlockingSuppServicesDialog(CdmaCallWaitingPreference.this.getContext(), CdmaCallWaitingPreference.this.mPhone, "call_waiting_key").show();
                    return;
                }
                CdmaCallWaitingPreference.this.mPhone.setCallWaiting(false, CdmaCallWaitingPreference.this.mHandler.obtainMessage(1));
                if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                    CdmaCallWaitingPreference.this.mTcpListener.onStarted(CdmaCallWaitingPreference.this, false);
                }
            }
        });
        builderMakeAlertDialogBuilder.create().show();
    }

    @Override // android.preference.Preference
    public void setEnabled(boolean z) {
        if (this.mIsActionAvailable) {
            super.setEnabled(z);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleGetCallWaitingResponse(message);
            } else {
                if (i != 1) {
                    return;
                }
                handleSetCallWaitingResponse(message);
            }
        }

        private void handleGetCallWaitingResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                int i = message.arg2;
                CdmaCallWaitingPreference cdmaCallWaitingPreference = CdmaCallWaitingPreference.this;
                if (i == 1) {
                    cdmaCallWaitingPreference.mTcpListener.onFinished(CdmaCallWaitingPreference.this, false);
                } else {
                    cdmaCallWaitingPreference.mTcpListener.onFinished(CdmaCallWaitingPreference.this, true);
                }
            }
            Throwable th = asyncResult.exception;
            if (th instanceof CommandException) {
                Log.d("CdmaCallWaitingPreference", "handleGetCallWaitingResponse: CommandException=" + asyncResult.exception);
                if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                    CdmaCallWaitingPreference.this.mTcpListener.onException(CdmaCallWaitingPreference.this, (CommandException) asyncResult.exception);
                    return;
                }
                return;
            }
            if ((asyncResult.userObj instanceof Throwable) || th != null) {
                Log.d("CdmaCallWaitingPreference", "handleGetCallWaitingResponse: Exception" + asyncResult.exception);
                if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                    CdmaCallWaitingPreference.this.mTcpListener.onError(CdmaCallWaitingPreference.this, 400);
                    return;
                }
                return;
            }
            Log.d("CdmaCallWaitingPreference", "handleGetCallWaitingResponse: CW state successfully queried.");
            int[] iArr = (int[]) asyncResult.result;
            if (iArr == null) {
                if (CdmaCallWaitingPreference.this.mTcpListener != null) {
                    CdmaCallWaitingPreference.this.mTcpListener.onError(CdmaCallWaitingPreference.this, 400);
                    return;
                }
                return;
            }
            try {
                int i2 = iArr[0];
                if (i2 == 255) {
                    CdmaCallWaitingPreference.this.setSummary("");
                    return;
                }
                if (i2 == 1) {
                    CdmaCallWaitingPreference cdmaCallWaitingPreference2 = CdmaCallWaitingPreference.this;
                    cdmaCallWaitingPreference2.setSummary(cdmaCallWaitingPreference2.mContext.getString(R.string.cdma_call_waiting_in_ims_on));
                } else if (i2 == 0) {
                    CdmaCallWaitingPreference cdmaCallWaitingPreference3 = CdmaCallWaitingPreference.this;
                    cdmaCallWaitingPreference3.setSummary(cdmaCallWaitingPreference3.mContext.getString(R.string.cdma_call_waiting_in_ims_off));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                CdmaCallWaitingPreference.this.setSummary("");
                Log.e("CdmaCallWaitingPreference", "handleGetCallWaitingResponse: improper result: err =" + e.getMessage());
            }
        }

        private void handleSetCallWaitingResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("CdmaCallWaitingPreference", "handleSetCallWaitingResponse: ar.exception=" + asyncResult.exception);
            }
            Object obj = asyncResult.result;
            if (obj != null && ((Integer) obj).intValue() == 255) {
                Log.d("CdmaCallWaitingPreference", "handleSetCallWaitingResponse: no need to re get in CDMA");
                CdmaCallWaitingPreference.this.mTcpListener.onFinished(CdmaCallWaitingPreference.this, false);
            } else {
                Log.d("CdmaCallWaitingPreference", "handleSetCallWaitingResponse: re get");
                CdmaCallWaitingPreference.this.mPhone.getCallWaiting(obtainMessage(0, 1, 1, asyncResult.exception));
            }
        }
    }
}
