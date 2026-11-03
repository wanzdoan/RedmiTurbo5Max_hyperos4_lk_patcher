package com.android.phone;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class IllegalMeDialogHelper extends Handler {
    private static final boolean DBG;
    private static IllegalMeDialogHelper sInstance;
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final CarrierConfigManager mConfigManager;
    private Context mContext;
    private AlertDialog mErrorDialog;
    private final ArrayMap mRegistrationFailedListeners;
    private final TelephonyManager mTelephonyManager;

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigChanged(i, i2);
    }

    final class RegistrationFailedListener extends TelephonyCallback implements TelephonyCallback.RegistrationFailedListener {
        private final Handler mHandler;
        private final int mSlotIndex;
        private final int mSubId;
        private final TelephonyManager mTelephonyManager;

        RegistrationFailedListener(Handler handler, Context context, int i, int i2) {
            this.mHandler = handler;
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            this.mSubId = i;
            this.mSlotIndex = i2;
        }

        @Override // android.telephony.TelephonyCallback.RegistrationFailedListener
        public void onRegistrationFailed(CellIdentity cellIdentity, String str, int i, int i2, int i3) {
            Log.d("IllegalMeDialogHelper", "onRegistrationFailed: cellIdentity = " + cellIdentity + ", chosenPlmn = " + str + ", domain = " + i + ", causeCode = " + i2 + ", additionalCauseCode = " + i3);
            String strSubstring = (str == null || str.length() < 3) ? "" : str.substring(0, 3);
            if (MiuiPhoneUtils.isSupportShowDialogWhenIllegalME(i2, this.mSlotIndex, strSubstring)) {
                IllegalMeDialogHelper.this.showCustomizedRejectCauseDialog(i2, this.mSlotIndex, strSubstring);
            }
        }

        public void registerTelephonyCallback() {
            TelephonyManager telephonyManagerCreateForSubscriptionId;
            TelephonyManager telephonyManager = this.mTelephonyManager;
            if (telephonyManager == null || (telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(this.mSubId)) == null) {
                return;
            }
            Log.d("IllegalMeDialogHelper", "registerTelephonyCallback subId = " + this.mSubId);
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this);
        }

        public void unregisterTelephonyCallback() {
            if (this.mTelephonyManager == null) {
                return;
            }
            Log.d("IllegalMeDialogHelper", "unregisterTelephonyCallback subId = " + this.mSubId);
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }

        public int getSubId() {
            return this.mSubId;
        }
    }

    public static void init() {
        if (sInstance == null) {
            HandlerThread handlerThread = new HandlerThread("IllegalMeDialogHelperThread");
            handlerThread.start();
            sInstance = new IllegalMeDialogHelper(handlerThread.getLooper());
        }
    }

    public IllegalMeDialogHelper(Looper looper) {
        super(looper);
        this.mRegistrationFailedListeners = new ArrayMap();
        CarrierConfigManager.CarrierConfigChangeListener carrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.phone.IllegalMeDialogHelper$$ExternalSyntheticLambda0
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mCarrierConfigChangeListener = carrierConfigChangeListener;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        if (phoneGlobals == null) {
            this.mTelephonyManager = null;
            this.mConfigManager = null;
            return;
        }
        this.mTelephonyManager = (TelephonyManager) phoneGlobals.getSystemService(TelephonyManager.class);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        this.mConfigManager = carrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.phone.IllegalMeDialogHelper$$ExternalSyntheticLambda1
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, carrierConfigChangeListener);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        super.handleMessage(message);
    }

    private void onCarrierConfigChanged(int i, int i2) {
        Log.i("IllegalMeDialogHelper", "onCarrierConfigChanged slotIndex=" + i + ", subId=" + i2);
        if (i < 0) {
            return;
        }
        RegistrationFailedListener registrationFailedListener = (RegistrationFailedListener) this.mRegistrationFailedListeners.get(Integer.valueOf(i));
        if (registrationFailedListener != null && registrationFailedListener.getSubId() != i2) {
            registrationFailedListener.unregisterTelephonyCallback();
            this.mRegistrationFailedListeners.remove(Integer.valueOf(i));
            registrationFailedListener = null;
        }
        if (registrationFailedListener == null && SubscriptionManager.isValidSubscriptionId(i2)) {
            RegistrationFailedListener registrationFailedListener2 = new RegistrationFailedListener(this, this.mContext, i2, i);
            registrationFailedListener2.registerTelephonyCallback();
            this.mRegistrationFailedListeners.put(Integer.valueOf(i), registrationFailedListener2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCustomizedRejectCauseDialog(int i, int i2, String str) {
        String strSelectTitleResourceForIllegalME = selectTitleResourceForIllegalME(this.mContext, i);
        String strSelectMessageResourceForIllegalME = selectMessageResourceForIllegalME(this.mContext, i);
        Log.d("IllegalMeDialogHelper", "showCustomizedRejectCauseDialog titleString = " + strSelectTitleResourceForIllegalME + ", messageString = " + strSelectMessageResourceForIllegalME);
        if (TextUtils.isEmpty(strSelectTitleResourceForIllegalME) || TextUtils.isEmpty(strSelectMessageResourceForIllegalME)) {
            dismissErrorDialog();
            return;
        }
        MiuiPhoneUtils.setShowDialogWhenIllegalMEDetails(str, System.currentTimeMillis());
        recordCustomizedErrorDialogScene(i2, str);
        AlertDialog alertDialog = this.mErrorDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            Log.d("IllegalMeDialogHelper", "showCustomizedRejectCauseDialog: Dialog already showing");
        } else {
            showErrorDialog(strSelectTitleResourceForIllegalME, strSelectMessageResourceForIllegalME);
        }
    }

    private void showErrorDialog(CharSequence charSequence, CharSequence charSequence2) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setTitle(charSequence).setMessage(charSequence2).setNegativeButton(R.string.got_it, (DialogInterface.OnClickListener) null).create();
        this.mErrorDialog = alertDialogCreate;
        alertDialogCreate.setCanceledOnTouchOutside(false);
        this.mErrorDialog.getWindow().setType(2003);
        this.mErrorDialog.show();
    }

    private void dismissErrorDialog() {
        if (this.mErrorDialog != null) {
            Log.e("IllegalMeDialogHelper", "dismissErrorDialog = ");
            this.mErrorDialog.dismiss();
            this.mErrorDialog = null;
        }
    }

    private String selectTitleResourceForIllegalME(Context context, int i) {
        if (i == 6) {
            return context.getString(R.string.customized_title_emm_cause_6);
        }
        return "";
    }

    private String selectMessageResourceForIllegalME(Context context, int i) {
        if (i == 6) {
            return context.getString(R.string.customized_message_emm_cause_6);
        }
        return "";
    }

    private void recordCustomizedErrorDialogScene(int i, String str) {
        HashMap map = new HashMap();
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(i);
        map.put("simMcc", (simOperatorNumeric == null || simOperatorNumeric.length() < 3) ? "" : simOperatorNumeric.substring(0, 3));
        map.put("networkMcc", str);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_customized_error_alert_dialog", map);
    }
}
