package com.android.phone.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MiuiSettings;
import android.text.BidiFormatter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneProxy;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import com.android.phone.utils.Utils;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miuix.appcompat.app.AlertDialog;
import miuix.preference.PreferenceFragment;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardEditPreference extends EditPhoneNumberPreference {
    private static final String[] SRC_TAGS = {"{0}"};
    private boolean DBG;
    public CallForwardInfo callForwardInfo;
    private int mButtonClicked;
    private MyHandler mHandler;
    private int mServiceClass;
    private AlertDialog mSmartCFTipDialog;
    private CharSequence mSummaryOnTemplate;
    private AlertDialog mTimeDialog;
    Phone phone;
    public int reason;
    TimeConsumingPreferenceListener tcpListener;

    public CallForwardEditPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.mTimeDialog = null;
        this.mSmartCFTipDialog = null;
        this.mSummaryOnTemplate = getSummaryOn();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CallForwardEditPreference, 0, R.style.EditPhoneNumberPreference);
        this.mServiceClass = typedArrayObtainStyledAttributes.getInt(R.styleable.CallForwardEditPreference_serviceClass, 1);
        this.reason = typedArrayObtainStyledAttributes.getInt(R.styleable.CallForwardEditPreference_reason, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (this.DBG) {
            Log.d("CallForwardEditPreference", "mServiceClass=" + this.mServiceClass + ", reason=" + this.reason);
        }
    }

    public void setServiceClass(int i) {
        this.mServiceClass = i;
    }

    public CallForwardEditPreference(Context context) {
        super(context);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
        this.mTimeDialog = null;
        this.mSmartCFTipDialog = null;
    }

    public void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z, int i) {
        if (this.DBG) {
            Log.d("CallForwardEditPreference", "Getting CallForwardEditPreference subscription =" + i);
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        this.phone = phone;
        this.tcpListener = timeConsumingPreferenceListener;
        if (z) {
            return;
        }
        PhoneProxy.getCallForwardingOption(phone, this.reason, this.mServiceClass, this.mHandler.obtainMessage(0, 0, 0, null));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener2 = this.tcpListener;
        if (timeConsumingPreferenceListener2 != null) {
            timeConsumingPreferenceListener2.onStarted(this, true);
        }
    }

    @Override // com.android.phone.settings.EditPhoneNumberPreference, com.android.phone.custompreferences.CustomEditTextPreference
    protected void onBindDialogView(View view) {
        this.mButtonClicked = -2;
        super.onBindDialogView(view);
    }

    @Override // com.android.phone.settings.EditPhoneNumberPreference, com.android.phone.custompreferences.CustomEditTextPreference, android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.reason == 3 && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportSmartCallForwardByDevice() && MiuiSettings.Telephony.isSmartCallForwardEnabled(getContext().getContentResolver()) && ((i == -3 && isToggled()) || i == -1)) {
            showSmartCallForwardDialog(dialogInterface, i);
        } else {
            super.onClick(dialogInterface, i);
            this.mButtonClicked = i;
        }
    }

    @Override // com.android.phone.settings.EditPhoneNumberPreference, com.android.phone.custompreferences.CustomEditTextPreference
    protected void onDialogClosed(boolean z) {
        CallForwardInfo callForwardInfo;
        super.onDialogClosed(z);
        if (this.DBG) {
            Log.d("CallForwardEditPreference", "mButtonClicked=" + this.mButtonClicked + ", positiveResult=" + z);
        }
        if (this.mButtonClicked != -2) {
            if (MiuiPhoneUtils.maybeShowDialogForSuppService(this.phone, 1)) {
                setToggled(false);
                return;
            }
            int i = (isToggled() || this.mButtonClicked == -1) ? 3 : 0;
            int lastCFTimeSeconds = this.reason == 2 ? getLastCFTimeSeconds() : 0;
            String phoneNumber = getPhoneNumber();
            if (this.DBG) {
                Log.d("CallForwardEditPreference", "callForwardInfo=" + this.callForwardInfo);
            }
            if (i == 3 && (callForwardInfo = this.callForwardInfo) != null && callForwardInfo.status == 1 && phoneNumber.equals(callForwardInfo.number)) {
                if (shouldShowCFTimeDialog(this.reason)) {
                    showCFTimeSecondsDialog(phoneNumber, i, lastCFTimeSeconds);
                    return;
                } else {
                    if (this.DBG) {
                        Log.d("CallForwardEditPreference", "no change, do nothing");
                        return;
                    }
                    return;
                }
            }
            if (i != 0 && shouldShowCFTimeDialog(this.reason)) {
                showCFTimeSecondsDialog(phoneNumber, i, lastCFTimeSeconds);
            } else {
                setCallForwardingOption(i, phoneNumber, lastCFTimeSeconds);
            }
        }
    }

    private boolean shouldShowCFTimeDialog(int i) {
        if (i == 2) {
            return MiuiPhoneUtils.supportCallForwardTimeDialog(this.phone) || MiuiJpOperatorCustimizedUtils.isCustForJpKd();
        }
        return false;
    }

    private void showCFTimeSecondsDialog(final String str, final int i, final int i2) {
        CharSequence[] charSequenceArr = {getContext().getResources().getString(R.string.cfnry_time_five_sec, 5), getContext().getResources().getString(R.string.cfnry_time_ten_sec, 10), getContext().getResources().getString(R.string.cfnry_time_fifteen_sec, 15), getContext().getResources().getString(R.string.cfnry_time_twenty_sec, 20), getContext().getResources().getString(R.string.cfnry_time_twenty_five_sec, 25), getContext().getResources().getString(R.string.cfnry_time_thirty_sec, 30)};
        final int[] intArray = getContext().getResources().getIntArray(R.array.cfnry_setting_time_entry_values);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(getContext()).setSingleChoiceItems(charSequenceArr, getLastCFTimeSecondsIndex(intArray), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallForwardEditPreference.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                dialogInterface.dismiss();
                CallForwardInfo callForwardInfo = CallForwardEditPreference.this.callForwardInfo;
                if (callForwardInfo != null && callForwardInfo.status == 1 && str.equals(callForwardInfo.number) && intArray[i3] == CallForwardEditPreference.this.callForwardInfo.timeSeconds) {
                    return;
                }
                Log.d("CallForwardEditPreference", "ChoiceItemsClicked time=" + intArray[i3]);
                CallForwardEditPreference.this.setCallForwardingOption(i, str, intArray[i3]);
            }
        }).setTitle(R.string.cfnry_time_setting_title).setNegativeButton(getContext().getString(android.R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallForwardEditPreference.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                dialogInterface.dismiss();
                CallForwardInfo callForwardInfo = CallForwardEditPreference.this.callForwardInfo;
                if (callForwardInfo != null && callForwardInfo.status == 1 && str.equals(callForwardInfo.number)) {
                    return;
                }
                Log.d("CallForwardEditPreference", "time canceled but number changed!");
                CallForwardEditPreference.this.setCallForwardingOption(i, str, i2);
            }
        }).setCancelable(false).create();
        this.mTimeDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCallForwardingOption(int i, String str, int i2) {
        if (this.DBG) {
            Log.d("CallForwardEditPreference", "setCallForwardingOption reason=" + this.reason + ", action=" + i + ", number=" + PhoneNumberUtils.toLogSafePhoneNumber(str) + ", mServiceClass=" + this.mServiceClass + ", time=" + i2);
        }
        setSummaryOn("");
        PhoneProxy.setCallForwardingOption(this.phone, i, this.reason, str, this.mServiceClass, i2, this.mHandler.obtainMessage(4, i, 4));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.tcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    public void handleCallForwardResult(CallForwardInfo callForwardInfo, boolean z) {
        handleCallForwardResult(callForwardInfo);
        setEnabled(z);
    }

    public void handleCallForwardResult(CallForwardInfo callForwardInfo) {
        this.callForwardInfo = callForwardInfo;
        if (this.DBG) {
            Log.d("CallForwardEditPreference", "handleGetCFResponse done, callForwardInfo=" + this.callForwardInfo);
        }
        setToggled(this.callForwardInfo.status == 1);
        setPhoneNumber(this.callForwardInfo.number);
        updateSummaryText();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSummaryText() {
        CharSequence string;
        if (isToggled()) {
            String rawPhoneNumber = getRawPhoneNumber();
            if (rawPhoneNumber != null && rawPhoneNumber.length() > 0) {
                string = TextUtils.replace(this.mSummaryOnTemplate, SRC_TAGS, new String[]{BidiFormatter.getInstance().unicodeWrap(Utils.localizeNumber(rawPhoneNumber))});
            } else {
                string = getContext().getString(R.string.sum_cfu_enabled_no_number);
            }
            setSummaryOn(string);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:53:0x00d1  */
        /* JADX WARN: Code duplicated, block: B:62:? A[RETURN, SYNTHETIC] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult;
            int i;
            int i2 = message.what;
            if (i2 == 0) {
                if (message.arg2 != 4) {
                    MiuiSuppServiceStatistic.getInstance(CallForwardEditPreference.this.getContext()).recordCountClickScene(message.what, 100, CallForwardEditPreference.this.phone);
                }
            } else {
                if (i2 != 1 && i2 != 2) {
                    if (i2 == 4) {
                        MiuiSuppServiceStatistic.getInstance(CallForwardEditPreference.this.getContext()).recordCountClickScene(message.what, 100, CallForwardEditPreference.this.phone);
                    } else if (i2 != 5 && i2 != 6) {
                        if (i2 == 12) {
                            AsyncResult asyncResult2 = (AsyncResult) message.obj;
                            Utils.Cfu cfu = (Utils.Cfu) asyncResult2.userObj;
                            if (asyncResult2.exception == null && (cfu.mServiceClass & 1) != 0) {
                                CallForwardEditPreference.this.phone.setVoiceCallForwardingFlag(1, message.arg1 == 1, cfu.mSetCfNumber);
                            }
                            Message message2 = cfu.mOnComplete;
                            if (message2 != null) {
                                AsyncResult.forMessage(message2, asyncResult2.result, asyncResult2.exception);
                                setCFResponse(cfu.mOnComplete);
                            }
                        } else if (i2 == 13) {
                            AsyncResult asyncResult3 = (AsyncResult) message.obj;
                            CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult3.result;
                            if (asyncResult3.exception == null) {
                                if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
                                    CallForwardEditPreference.this.phone.setVoiceCallForwardingFlag(1, false, (String) null);
                                } else {
                                    for (CallForwardInfo callForwardInfo : callForwardInfoArr) {
                                        if ((callForwardInfo.serviceClass & 1) != 0) {
                                            CallForwardEditPreference.this.phone.setVoiceCallForwardingFlag(1, callForwardInfo.status == 1, callForwardInfo.number);
                                            break;
                                        }
                                    }
                                }
                            }
                            Message message3 = (Message) asyncResult3.userObj;
                            if (message3 != null) {
                                AsyncResult.forMessage(message3, asyncResult3.result, asyncResult3.exception);
                                getCFResponse(message3);
                            }
                        }
                    }
                    setCFResponse(message);
                }
                asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    i = message.what;
                    if (i != 2 || i == 6 || i == 12 || i == 13) {
                        MiuiSuppServiceStatistic.getInstance(CallForwardEditPreference.this.getContext()).recordExceptionScene(message.what, 100, asyncResult.exception, CallForwardEditPreference.this.phone);
                    }
                    return;
                }
            }
            getCFResponse(message);
            asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                i = message.what;
                if (i != 2) {
                }
                MiuiSuppServiceStatistic.getInstance(CallForwardEditPreference.this.getContext()).recordExceptionScene(message.what, 100, asyncResult.exception, CallForwardEditPreference.this.phone);
            }
        }

        private void getCFResponse(Message message) {
            if (message.what != 2 && ((AsyncResult) message.obj).exception != null) {
                if (CallForwardEditPreference.this.phone.getPhoneType() == 2 && MiuiPhoneUtils.shouldSwitchToCdmaCallForward(CallForwardEditPreference.this.phone, ((AsyncResult) message.obj).exception)) {
                    Intent intent = new Intent();
                    SubscriptionManager.putSlotIdExtra(intent, CallForwardEditPreference.this.phone.getPhoneId());
                    PreferenceFragment preferenceFragment = ((EditPhoneNumberPreference) CallForwardEditPreference.this).mFragment;
                    if (preferenceFragment == null || preferenceFragment.getActivity() == null) {
                        return;
                    }
                    intent.setClass(((EditPhoneNumberPreference) CallForwardEditPreference.this).mFragment.getActivity(), CdmaCallForwardOptions.class);
                    ((EditPhoneNumberPreference) CallForwardEditPreference.this).mFragment.getActivity().startActivity(intent);
                    ((EditPhoneNumberPreference) CallForwardEditPreference.this).mFragment.getActivity().finish();
                    return;
                }
                ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
                iTelephonyModule.getCallForwardingOption(callForwardEditPreference.phone, callForwardEditPreference.reason, callForwardEditPreference.mServiceClass, CallForwardEditPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2, message.obj));
                return;
            }
            handleGetCFResponse(message);
        }

        private void setCFResponse(Message message) {
            if (message.what != 6 && ((AsyncResult) message.obj).exception != null) {
                ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
                CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
                Phone phone = callForwardEditPreference.phone;
                int i = (callForwardEditPreference.isToggled() || CallForwardEditPreference.this.mButtonClicked == -1) ? 3 : 0;
                CallForwardEditPreference callForwardEditPreference2 = CallForwardEditPreference.this;
                int i2 = callForwardEditPreference2.reason;
                String phoneNumber = callForwardEditPreference2.getPhoneNumber();
                int i3 = CallForwardEditPreference.this.mServiceClass;
                CallForwardEditPreference callForwardEditPreference3 = CallForwardEditPreference.this;
                iTelephonyModule.setCallForwardingOption(phone, i, i2, phoneNumber, i3, callForwardEditPreference3.reason == 2 ? callForwardEditPreference3.getLastCFTimeSeconds() : 0, CallForwardEditPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2));
                return;
            }
            handleSetCFResponse(message);
        }

        private void handleGetCFResponse(Message message) {
            CharSequence text;
            if (CallForwardEditPreference.this.DBG) {
                Log.d("CallForwardEditPreference", "handleGetCFResponse: done");
            }
            int i = message.arg2;
            CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
            if (i == 4) {
                callForwardEditPreference.tcpListener.onFinished(callForwardEditPreference, false);
            } else {
                callForwardEditPreference.tcpListener.onFinished(callForwardEditPreference, true);
            }
            AsyncResult asyncResult = (AsyncResult) message.obj;
            CallForwardEditPreference callForwardEditPreference2 = CallForwardEditPreference.this;
            callForwardEditPreference2.callForwardInfo = null;
            if (asyncResult.exception != null) {
                if (callForwardEditPreference2.DBG) {
                    Log.d("CallForwardEditPreference", "handleGetCFResponse: ar.exception=" + asyncResult.exception);
                }
                CallForwardEditPreference callForwardEditPreference3 = CallForwardEditPreference.this;
                callForwardEditPreference3.tcpListener.onException(callForwardEditPreference3, (CommandException) asyncResult.exception);
                return;
            }
            if (asyncResult.userObj instanceof Throwable) {
                callForwardEditPreference2.tcpListener.onError(callForwardEditPreference2, 400);
            }
            CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult.result;
            if (callForwardInfoArr.length == 0) {
                if (CallForwardEditPreference.this.DBG) {
                    Log.d("CallForwardEditPreference", "handleGetCFResponse: cfInfoArray.length==0");
                }
                CallForwardEditPreference.this.setEnabled(false);
                CallForwardEditPreference callForwardEditPreference4 = CallForwardEditPreference.this;
                callForwardEditPreference4.tcpListener.onError(callForwardEditPreference4, 400);
                return;
            }
            int length = callForwardInfoArr.length;
            int i2 = 0;
            while (true) {
                CallForwardEditPreference callForwardEditPreference5 = CallForwardEditPreference.this;
                if (i2 < length) {
                    if (callForwardEditPreference5.DBG) {
                        Log.d("CallForwardEditPreference", "handleGetCFResponse, cfInfoArray[" + i2 + "]=" + callForwardInfoArr[i2]);
                    }
                    int i3 = CallForwardEditPreference.this.mServiceClass;
                    CallForwardInfo callForwardInfo = callForwardInfoArr[i2];
                    if ((i3 & callForwardInfo.serviceClass) != 0) {
                        CallForwardEditPreference.this.handleCallForwardResult(callForwardInfo);
                        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING || ModemProvider.IS_SPRD) {
                            CallForwardEditPreference callForwardEditPreference6 = CallForwardEditPreference.this;
                            if (callForwardEditPreference6.reason == 0 && callForwardEditPreference6.mServiceClass == PhoneProxy.getVideoCFServiceClass()) {
                                Log.d("CallForwardEditPreference", "notify video call forward for MTK or SPRD");
                                try {
                                    ITelephonyRegistry iTelephonyRegistryAsInterface = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
                                    if (iTelephonyRegistryAsInterface != null) {
                                        int subId = CallForwardEditPreference.this.phone.getSubId();
                                        boolean z = callForwardInfo.status == 1;
                                        iTelephonyRegistryAsInterface.notifyCallForwardingChangedForSubscriber(subId, z);
                                        Log.d("CallForwardEditPreference", "notifyVideoCallForwardingChanged: subId=" + subId + ", isCFActive=" + z);
                                    }
                                } catch (RemoteException unused) {
                                }
                            }
                        }
                        if (message.arg2 == 4 && message.arg1 == 0 && callForwardInfo.status == 1 && !TelephonyManager.isCustForMxAt()) {
                            CallForwardEditPreference callForwardEditPreference7 = CallForwardEditPreference.this;
                            int i4 = callForwardEditPreference7.reason;
                            if (i4 == 1) {
                                text = callForwardEditPreference7.getContext().getText(R.string.disable_cfb_forbidden);
                            } else if (i4 == 2) {
                                text = callForwardEditPreference7.getContext().getText(R.string.disable_cfnry_forbidden);
                            } else {
                                text = callForwardEditPreference7.getContext().getText(R.string.disable_cfnrc_forbidden);
                            }
                            if ((CallForwardEditPreference.this.getContext() instanceof Activity) && ((Activity) CallForwardEditPreference.this.getContext()).isFinishing()) {
                                return;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(CallForwardEditPreference.this.getContext());
                            builder.setNeutralButton(R.string.close_dialog, (DialogInterface.OnClickListener) null);
                            builder.setTitle(CallForwardEditPreference.this.getContext().getText(R.string.error_updating_title));
                            builder.setMessage(text);
                            builder.setCancelable(true);
                            builder.create().show();
                        }
                    }
                    i2++;
                } else {
                    callForwardEditPreference5.updateSummaryText();
                    return;
                }
            }
        }

        private void handleSetCFResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null && CallForwardEditPreference.this.DBG) {
                Log.d("CallForwardEditPreference", "handleSetCFResponse: ar.exception=" + asyncResult.exception);
            }
            ITelephonyModule iTelephonyModule = (ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class);
            CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
            iTelephonyModule.updateRoamingCFUStatus(callForwardEditPreference.phone, callForwardEditPreference.reason, message.arg1, callForwardEditPreference.getPhoneNumber(), asyncResult.exception);
            if (CallForwardEditPreference.this.DBG) {
                Log.d("CallForwardEditPreference", "handleSetCFResponse: re get mServiceClass: " + CallForwardEditPreference.this.mServiceClass);
            }
            CallForwardEditPreference callForwardEditPreference2 = CallForwardEditPreference.this;
            PhoneProxy.getCallForwardingOption(callForwardEditPreference2.phone, callForwardEditPreference2.reason, callForwardEditPreference2.mServiceClass, obtainMessage(0, message.arg1, 4, asyncResult.exception));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLastCFTimeSeconds() {
        int i;
        CallForwardInfo callForwardInfo = this.callForwardInfo;
        if (callForwardInfo == null || (i = callForwardInfo.timeSeconds) <= 0) {
            return 20;
        }
        return i;
    }

    private int getLastCFTimeSecondsIndex(int[] iArr) {
        int lastCFTimeSeconds = getLastCFTimeSeconds();
        for (int i = 0; i < iArr.length; i++) {
            if (lastCFTimeSeconds == iArr[i]) {
                return i;
            }
        }
        return -1;
    }

    public void onDestroy() {
        AlertDialog alertDialog = this.mTimeDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mTimeDialog.dismiss();
            this.mTimeDialog = null;
        }
        AlertDialog alertDialog2 = this.mSmartCFTipDialog;
        if (alertDialog2 == null || !alertDialog2.isShowing()) {
            return;
        }
        this.mSmartCFTipDialog.dismiss();
        this.mSmartCFTipDialog = null;
    }

    private void showSmartCallForwardDialog(final DialogInterface dialogInterface, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.labelCFNRc_title);
        builder.setMessage(R.string.labelCFNRc_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.settings.CallForwardEditPreference.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface2, int i2) {
                dialogInterface2.dismiss();
                CallForwardEditPreference.this.performClick(dialogInterface, i);
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.settings.CallForwardEditPreference.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface2, int i2) {
                dialogInterface2.dismiss();
            }
        }).setCancelable(false);
        AlertDialog alertDialogCreate = builder.create();
        this.mSmartCFTipDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
        this.mButtonClicked = i;
        onDialogClosed(false);
    }
}
