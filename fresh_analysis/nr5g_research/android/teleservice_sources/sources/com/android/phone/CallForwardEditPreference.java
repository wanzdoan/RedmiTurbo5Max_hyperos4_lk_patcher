package com.android.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.SpannableString;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.settings.SuppServicesUiUtil;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.HashMap;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class CallForwardEditPreference extends EditPhoneNumberPreference {
    private static final String[] SRC_TAGS = {"{0}"};
    CallForwardInfo callForwardInfo;
    private int mAction;
    private int mButtonClicked;
    private boolean mCallForwardByUssd;
    private CarrierXmlParser mCarrierXmlParser;
    private HashMap mCfInfo;
    private Object mCommandException;
    private long mDelayMillisAfterUssdSet;
    private MyHandler mHandler;
    private Phone mPhone;
    private int mPreviousCommand;
    private boolean mReplaceInvalidCFNumber;
    private int mServiceClass;
    private CarrierXmlParser.SsEntry.SSAction mSsAction;
    private CharSequence mSummaryOnTemplate;
    private TimeConsumingPreferenceListener mTcpListener;
    int reason;

    public CallForwardEditPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new MyHandler();
        this.mReplaceInvalidCFNumber = false;
        this.mCallForwardByUssd = false;
        this.mPreviousCommand = 0;
        this.mSsAction = CarrierXmlParser.SsEntry.SSAction.UNKNOWN;
        this.mDelayMillisAfterUssdSet = 1000L;
        this.mSummaryOnTemplate = getSummaryOn();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CallForwardEditPreference, 0, R.style.EditPhoneNumberPreference);
        this.mServiceClass = typedArrayObtainStyledAttributes.getInt(R.styleable.CallForwardEditPreference_serviceClass, 1);
        this.reason = typedArrayObtainStyledAttributes.getInt(R.styleable.CallForwardEditPreference_reason, 0);
        typedArrayObtainStyledAttributes.recycle();
        Log.d("CallForwardEditPreference", "mServiceClass=" + this.mServiceClass + ", reason=" + this.reason);
    }

    public CallForwardEditPreference(Context context) {
        this(context, null);
    }

    void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, Phone phone, boolean z, boolean z2) {
        this.mPhone = phone;
        this.mTcpListener = timeConsumingPreferenceListener;
        this.mReplaceInvalidCFNumber = z;
        this.mCallForwardByUssd = z2;
        Log.d("CallForwardEditPreference", "init :mReplaceInvalidCFNumber " + this.mReplaceInvalidCFNumber + ", mCallForwardByUssd " + this.mCallForwardByUssd);
        if (this.mCallForwardByUssd) {
            this.mCfInfo = new HashMap();
            this.mCarrierXmlParser = new CarrierXmlParser(getContext(), new TelephonyManager(getContext(), phone.getSubId()).getSimCarrierId());
        }
    }

    void restoreCallForwardInfo(CallForwardInfo callForwardInfo) {
        handleCallForwardResult(callForwardInfo);
        updateSummaryText();
        if (callForwardInfo.status == 255) {
            Log.d("CallForwardEditPreference", "CallForwardInfo has unknown status");
            setSummaryOff("");
        }
    }

    @Override // com.android.phone.EditPhoneNumberPreference, android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onBindDialogView(View view) {
        this.mButtonClicked = -2;
        super.onBindDialogView(view);
    }

    @Override // com.android.phone.EditPhoneNumberPreference, android.preference.DialogPreference, android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i != -2 && PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId()).getBoolean("call_forwarding_over_ut_warning_bool", true) && SuppServicesUiUtil.isSsOverUtPrecautions(getContext(), this.mPhone)) {
            SuppServicesUiUtil.showBlockingSuppServicesDialog(getContext(), this.mPhone, "call_forwarding_key").show();
        } else {
            super.onClick(dialogInterface, i);
            this.mButtonClicked = i;
        }
    }

    /* JADX WARN: Code duplicated, block: B:28:0x00ae  */
    @Override // com.android.phone.EditPhoneNumberPreference, android.preference.EditTextPreference, android.preference.DialogPreference
    protected void onDialogClosed(boolean z) {
        int i;
        CallForwardInfo callForwardInfo;
        int i2;
        super.onDialogClosed(z);
        Log.d("CallForwardEditPreference", "mButtonClicked=" + this.mButtonClicked + ", positiveResult=" + z);
        if (isUnknownStatus() && (i2 = this.mButtonClicked) != -2) {
            int i3 = i2 == -1 ? 3 : 0;
            String phoneNumber = i3 == 0 ? "" : getPhoneNumber();
            Log.d("CallForwardEditPreference", "reason=" + this.reason + ", action=" + i3);
            setSummaryOff("");
            this.mPhone.setCallForwardingOption(i3, this.reason, phoneNumber, this.mServiceClass, 0, this.mHandler.obtainMessage(1, i3, 1));
            return;
        }
        if (this.mButtonClicked != -2) {
            int i4 = (isToggled() || this.mButtonClicked == -1) ? 3 : 0;
            if (this.reason == 2) {
                PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
                if (carrierConfigForSubId.getBoolean("support_no_reply_timer_for_cfnry_bool", true)) {
                    i = carrierConfigForSubId.getInt("no_reply_timer_for_cfnry_sec_int", 20);
                } else {
                    i = 0;
                }
            } else {
                i = 0;
            }
            String phoneNumber2 = getPhoneNumber();
            Log.d("CallForwardEditPreference", "callForwardInfo=" + this.callForwardInfo);
            if (i4 == 3 && (callForwardInfo = this.callForwardInfo) != null && callForwardInfo.status == 1 && phoneNumber2.equals(callForwardInfo.number)) {
                Log.d("CallForwardEditPreference", "no change, do nothing");
                return;
            }
            Log.d("CallForwardEditPreference", "reason=" + this.reason + ", action=" + i4);
            setSummaryOn("");
            if (!this.mCallForwardByUssd) {
                this.mPhone.setCallForwardingOption(i4, this.reason, phoneNumber2, this.mServiceClass, i, this.mHandler.obtainMessage(1, i4, 1));
            } else {
                HashMap map = this.mCfInfo;
                if (i4 == 3) {
                    map.put("tag_number", phoneNumber2);
                    this.mCfInfo.put("tag_time", Integer.toString(i));
                } else {
                    map.clear();
                }
                MyHandler myHandler = this.mHandler;
                myHandler.sendMessage(myHandler.obtainMessage(3, i4, 1));
            }
            TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
            if (timeConsumingPreferenceListener != null) {
                timeConsumingPreferenceListener.onStarted(this, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallForwardResult(CallForwardInfo callForwardInfo) {
        PersistableBundle carrierConfigForSubId;
        this.callForwardInfo = callForwardInfo;
        Log.d("CallForwardEditPreference", "handleGetCFResponse done, callForwardInfo=" + this.callForwardInfo);
        if (this.mReplaceInvalidCFNumber && !TextUtils.isEmpty(this.callForwardInfo.number) && PhoneNumberUtils.formatNumber(this.callForwardInfo.number, getCurrentCountryIso()) == null) {
            this.callForwardInfo.number = getContext().getString(R.string.voicemail);
            Log.i("CallForwardEditPreference", "handleGetCFResponse: Overridding CF number");
        }
        boolean z = false;
        setUnknownStatus(this.callForwardInfo.status == 255);
        setToggled(this.callForwardInfo.status == 1);
        if (this.mPhone != null && TextUtils.isEmpty(this.callForwardInfo.number) && (carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId())) != null) {
            z = carrierConfigForSubId.getBoolean("display_voicemail_number_as_default_call_forwarding_number");
            Log.d("CallForwardEditPreference", "display voicemail number as default");
        }
        Phone phone = this.mPhone;
        String voiceMailNumber = phone != null ? phone.getVoiceMailNumber() : null;
        if (!z) {
            voiceMailNumber = this.callForwardInfo.number;
        }
        setPhoneNumber(voiceMailNumber);
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onUpdated(this);
        }
    }

    void startCallForwardOptionsQuery() {
        if (!this.mCallForwardByUssd) {
            this.mPhone.getCallForwardingOption(this.reason, this.mServiceClass, this.mHandler.obtainMessage(0, 0, 0, null));
        } else {
            MyHandler myHandler = this.mHandler;
            myHandler.sendMessage(myHandler.obtainMessage(2, 0, 0, null));
        }
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSummaryText() {
        if (isToggled()) {
            String rawPhoneNumber = getRawPhoneNumber();
            if (rawPhoneNumber != null && rawPhoneNumber.length() > 0) {
                String strUnicodeWrap = BidiFormatter.getInstance().unicodeWrap(rawPhoneNumber, TextDirectionHeuristics.LTR);
                String strValueOf = String.valueOf(TextUtils.replace(this.mSummaryOnTemplate, SRC_TAGS, new String[]{strUnicodeWrap}));
                int iIndexOf = strValueOf.indexOf(strUnicodeWrap);
                SpannableString spannableString = new SpannableString(strValueOf);
                PhoneNumberUtils.addTtsSpan(spannableString, iIndexOf, strUnicodeWrap.length() + iIndexOf);
                setSummaryOn(spannableString);
                return;
            }
            setSummaryOn(getContext().getString(R.string.sum_cfu_enabled_no_number));
        }
    }

    private String getCurrentCountryIso() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService("phone");
        if (telephonyManager == null) {
            return "";
        }
        String upperCase = telephonyManager.getNetworkCountryIso().toUpperCase(Locale.ROOT);
        if (TextUtils.isEmpty(upperCase)) {
            upperCase = telephonyManager.getSimCountryIso().toUpperCase(Locale.US);
            if (TextUtils.isEmpty(upperCase)) {
                upperCase = Locale.getDefault().getCountry();
                Log.d("CallForwardEditPreference", "falling back to countryIso based on locale: " + upperCase);
            }
        }
        Log.d("CallForwardEditPreference", "getCurrentCountryIso: " + upperCase);
        return upperCase;
    }

    class MyHandler extends Handler {
        TelephonyManager.UssdResponseCallback mUssdCallback;

        private MyHandler() {
            this.mUssdCallback = new TelephonyManager.UssdResponseCallback() { // from class: com.android.phone.CallForwardEditPreference.MyHandler.1
                @Override // android.telephony.TelephonyManager.UssdResponseCallback
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String str, CharSequence charSequence) {
                    if (CallForwardEditPreference.this.mSsAction == CarrierXmlParser.SsEntry.SSAction.UNKNOWN) {
                        return;
                    }
                    HashMap responseSet = CallForwardEditPreference.this.mCarrierXmlParser.getFeature("callforwarding").getResponseSet(CallForwardEditPreference.this.mSsAction, charSequence.toString());
                    MyHandler.this.sendCfMessage(CallForwardEditPreference.this.mSsAction == CarrierXmlParser.SsEntry.SSAction.QUERY ? MyHandler.this.makeCallForwardInfo(responseSet) : null, responseSet.get("RESPONSE_ERROR") != null ? new CommandException(CommandException.Error.GENERIC_FAILURE) : null);
                }

                @Override // android.telephony.TelephonyManager.UssdResponseCallback
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String str, int i) {
                    Log.d("CallForwardEditPreference", "receive the ussd result failed");
                    MyHandler.this.sendCfMessage(null, new CommandException(CommandException.Error.GENERIC_FAILURE));
                }
            };
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleGetCFResponse(message);
                return;
            }
            if (i == 1) {
                handleSetCFResponse(message);
            } else if (i == 2) {
                prepareUssdCommand(message, CarrierXmlParser.SsEntry.SSAction.QUERY);
            } else {
                if (i != 3) {
                    return;
                }
                prepareUssdCommand(message, CarrierXmlParser.SsEntry.SSAction.UNKNOWN);
            }
        }

        private void handleGetCFResponse(Message message) {
            CharSequence text;
            Log.d("CallForwardEditPreference", "handleGetCFResponse: done");
            boolean z = false;
            CallForwardEditPreference.this.mTcpListener.onFinished(CallForwardEditPreference.this, message.arg2 != 1);
            AsyncResult asyncResult = (AsyncResult) message.obj;
            CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
            callForwardEditPreference.callForwardInfo = null;
            if (asyncResult.exception != null) {
                Log.d("CallForwardEditPreference", "handleGetCFResponse: ar.exception=" + asyncResult.exception);
                CommandException commandException = asyncResult.exception;
                boolean z2 = commandException instanceof CommandException;
                CallForwardEditPreference callForwardEditPreference2 = CallForwardEditPreference.this;
                if (z2) {
                    callForwardEditPreference2.handleCommandException(commandException);
                } else {
                    callForwardEditPreference2.mTcpListener.onError(CallForwardEditPreference.this, MiRilHook.HOOK_ID_START_URC_BY_AT);
                }
            } else {
                if (asyncResult.userObj instanceof Throwable) {
                    callForwardEditPreference.mTcpListener.onError(CallForwardEditPreference.this, 400);
                }
                CallForwardInfo[] callForwardInfoArr = (CallForwardInfo[]) asyncResult.result;
                if (callForwardInfoArr == null || callForwardInfoArr.length == 0) {
                    Log.d("CallForwardEditPreference", "handleGetCFResponse: cfInfoArray.length==0");
                    if (!(asyncResult.userObj instanceof Throwable)) {
                        CallForwardEditPreference.this.mTcpListener.onError(CallForwardEditPreference.this, 400);
                    }
                } else {
                    int length = callForwardInfoArr.length;
                    boolean z3 = false;
                    for (int i = 0; i < length; i++) {
                        Log.d("CallForwardEditPreference", "handleGetCFResponse, cfInfoArray[" + i + "]=" + callForwardInfoArr[i]);
                        int i2 = CallForwardEditPreference.this.mServiceClass;
                        CallForwardInfo callForwardInfo = callForwardInfoArr[i];
                        if ((i2 & callForwardInfo.serviceClass) != 0) {
                            CallForwardEditPreference.this.handleCallForwardResult(callForwardInfo);
                            int i3 = callForwardInfo.status;
                            boolean z4 = i3 == 255;
                            if (asyncResult.userObj instanceof Throwable) {
                                Log.d("CallForwardEditPreference", "Skipped duplicated error dialog");
                            } else {
                                int i4 = message.arg2;
                                if (i4 == 1 && message.arg1 == 0 && i3 == 1) {
                                    if (CallForwardEditPreference.this.isSkipCFFailToDisableDialog()) {
                                        Log.d("CallForwardEditPreference", "Skipped Callforwarding fail-to-disable dialog");
                                    } else {
                                        CallForwardEditPreference callForwardEditPreference3 = CallForwardEditPreference.this;
                                        int i5 = callForwardEditPreference3.reason;
                                        if (i5 == 1) {
                                            text = callForwardEditPreference3.getContext().getText(R.string.disable_cfb_forbidden);
                                        } else if (i5 == 2) {
                                            text = callForwardEditPreference3.getContext().getText(R.string.disable_cfnry_forbidden);
                                        } else if (i5 == 3) {
                                            text = callForwardEditPreference3.getContext().getText(R.string.disable_cfnrc_forbidden);
                                        } else {
                                            Log.d("CallForwardEditPreference", "handleGetCFResponse: fail to disable");
                                            text = null;
                                        }
                                        AlertDialog.Builder builderMakeAlertDialogBuilder = FrameworksUtils.makeAlertDialogBuilder(CallForwardEditPreference.this.getContext());
                                        builderMakeAlertDialogBuilder.setNeutralButton(R.string.close_dialog, (DialogInterface.OnClickListener) null);
                                        builderMakeAlertDialogBuilder.setTitle(CallForwardEditPreference.this.getContext().getText(R.string.error_updating_title));
                                        builderMakeAlertDialogBuilder.setMessage(text);
                                        builderMakeAlertDialogBuilder.setCancelable(true);
                                        if (!((Activity) CallForwardEditPreference.this.getContext()).isFinishing()) {
                                            builderMakeAlertDialogBuilder.create().show();
                                        }
                                    }
                                } else if (i4 == 1 && message.arg1 == 3 && i3 == 0) {
                                    CharSequence text2 = CallForwardEditPreference.this.getContext().getText(R.string.registration_cf_forbidden);
                                    AlertDialog.Builder builderMakeAlertDialogBuilder2 = FrameworksUtils.makeAlertDialogBuilder(CallForwardEditPreference.this.getContext());
                                    builderMakeAlertDialogBuilder2.setNeutralButton(R.string.close_dialog, (DialogInterface.OnClickListener) null);
                                    builderMakeAlertDialogBuilder2.setTitle(CallForwardEditPreference.this.getContext().getText(R.string.error_updating_title));
                                    builderMakeAlertDialogBuilder2.setMessage(text2);
                                    builderMakeAlertDialogBuilder2.setCancelable(true);
                                    if (!((Activity) CallForwardEditPreference.this.getContext()).isFinishing() && text2 != null) {
                                        builderMakeAlertDialogBuilder2.create().show();
                                    }
                                }
                            }
                            z3 = z4;
                        }
                    }
                    z = z3;
                }
            }
            CallForwardEditPreference callForwardEditPreference4 = CallForwardEditPreference.this;
            if (z) {
                callForwardEditPreference4.setSummaryOff("");
            } else {
                callForwardEditPreference4.updateSummaryText();
            }
        }

        private void handleSetCFResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("CallForwardEditPreference", "handleSetCFResponse: ar.exception=" + asyncResult.exception);
            }
            Object obj = asyncResult.result;
            if (obj != null && ((Integer) obj).intValue() == 255) {
                Log.d("CallForwardEditPreference", "handleSetCFResponse: no need to re get in CDMA");
                CallForwardEditPreference.this.mTcpListener.onFinished(CallForwardEditPreference.this, false);
                return;
            }
            Log.d("CallForwardEditPreference", "handleSetCFResponse: re get");
            if (!CallForwardEditPreference.this.mCallForwardByUssd) {
                postDelayed(new Runnable(message, message.arg1, asyncResult) { // from class: com.android.phone.CallForwardEditPreference.MyHandler.2
                    final Message msgCopy;
                    final /* synthetic */ AsyncResult val$ar;
                    final /* synthetic */ int val$arg1;
                    final /* synthetic */ Message val$msg;

                    {
                        this.val$msg = message;
                        this.val$arg1 = i;
                        this.val$ar = asyncResult;
                        this.msgCopy = Message.obtain(message);
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        Log.d("CallForwardEditPreference", "handleSetCFResponse: re get start");
                        Phone phone = CallForwardEditPreference.this.mPhone;
                        CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
                        phone.getCallForwardingOption(callForwardEditPreference.reason, callForwardEditPreference.mServiceClass, MyHandler.this.obtainMessage(0, this.val$arg1, 1, this.val$ar.exception));
                    }
                }, CallForwardEditPreference.this.getDelaytime());
                return;
            }
            MyHandler myHandler = CallForwardEditPreference.this.mHandler;
            MyHandler myHandler2 = CallForwardEditPreference.this.mHandler;
            MyHandler unused = CallForwardEditPreference.this.mHandler;
            myHandler.sendMessageDelayed(myHandler2.obtainMessage(2, message.arg1, 1, asyncResult.exception), CallForwardEditPreference.this.mDelayMillisAfterUssdSet);
        }

        private void prepareUssdCommand(Message message, CarrierXmlParser.SsEntry.SSAction sSAction) {
            CallForwardEditPreference.this.mAction = message.arg1;
            CallForwardEditPreference.this.mPreviousCommand = message.arg2;
            CallForwardEditPreference.this.mCommandException = message.obj;
            CallForwardEditPreference.this.mSsAction = sSAction;
            if (CallForwardEditPreference.this.mSsAction != CarrierXmlParser.SsEntry.SSAction.QUERY) {
                int i = CallForwardEditPreference.this.mAction;
                CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
                if (i == 3) {
                    callForwardEditPreference.mSsAction = CarrierXmlParser.SsEntry.SSAction.UPDATE_ACTIVATE;
                } else {
                    callForwardEditPreference.mSsAction = CarrierXmlParser.SsEntry.SSAction.UPDATE_DEACTIVATE;
                }
            }
            new Thread(new Runnable() { // from class: com.android.phone.CallForwardEditPreference.MyHandler.3
                @Override // java.lang.Runnable
                public void run() {
                    MyHandler myHandler = MyHandler.this;
                    myHandler.sendUssdCommand(myHandler.mUssdCallback, CallForwardEditPreference.this.mSsAction, CallForwardEditPreference.this.mCfInfo.isEmpty() ? null : CallForwardEditPreference.this.mCfInfo);
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendUssdCommand(TelephonyManager.UssdResponseCallback ussdResponseCallback, CarrierXmlParser.SsEntry.SSAction sSAction, HashMap map) {
            ((TelephonyManager) CallForwardEditPreference.this.getContext().getSystemService("phone")).sendUssdRequest(CallForwardEditPreference.this.mCarrierXmlParser.getFeature("callforwarding").makeCommand(sSAction, map), ussdResponseCallback, CallForwardEditPreference.this.mHandler);
        }

        private Message makeGetCfMessage(int i, int i2, Object obj) {
            return CallForwardEditPreference.this.mHandler.obtainMessage(i, CallForwardEditPreference.this.mAction, i2, obj);
        }

        private Message makeSetCfMessage(int i, int i2) {
            return CallForwardEditPreference.this.mHandler.obtainMessage(i, CallForwardEditPreference.this.mAction, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendCfMessage(Object obj, Throwable th) {
            Message messageMakeSetCfMessage;
            if (CallForwardEditPreference.this.mSsAction == CarrierXmlParser.SsEntry.SSAction.UNKNOWN) {
                return;
            }
            if (CallForwardEditPreference.this.mSsAction == CarrierXmlParser.SsEntry.SSAction.QUERY) {
                messageMakeSetCfMessage = makeGetCfMessage(0, CallForwardEditPreference.this.mPreviousCommand, CallForwardEditPreference.this.mCommandException);
            } else {
                messageMakeSetCfMessage = makeSetCfMessage(1, 1);
            }
            AsyncResult.forMessage(messageMakeSetCfMessage, obj, th);
            messageMakeSetCfMessage.sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CallForwardInfo[] makeCallForwardInfo(HashMap map) {
            String str;
            int iIntValue;
            int i;
            if (map != null && map.size() != 0) {
                String str2 = (String) map.get("status_code");
                String str3 = (String) map.get("time");
                if (TextUtils.isEmpty(str2)) {
                    i = 0;
                } else if (str2.equals("activate")) {
                    i = 1;
                } else {
                    if (!str2.equals("deactivate")) {
                        str2.equals("unregister");
                    }
                    i = 0;
                }
                str = (String) map.get("number");
                iIntValue = !TextUtils.isEmpty(str3) ? Integer.valueOf((String) map.get("time")).intValue() : 0;
            } else {
                str = "";
                iIntValue = 0;
                i = 0;
            }
            CallForwardInfo callForwardInfo = new CallForwardInfo();
            CallForwardInfo[] callForwardInfoArr = {callForwardInfo};
            callForwardInfo.status = i;
            CallForwardEditPreference callForwardEditPreference = CallForwardEditPreference.this;
            callForwardInfo.reason = callForwardEditPreference.reason;
            callForwardInfo.serviceClass = callForwardEditPreference.mServiceClass;
            CallForwardInfo callForwardInfo2 = callForwardInfoArr[0];
            callForwardInfo2.number = str;
            callForwardInfo2.timeSeconds = iIntValue;
            return callForwardInfoArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSkipCFFailToDisableDialog() {
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        if (carrierConfigForSubId != null) {
            return carrierConfigForSubId.getBoolean("skip_cf_fail_to_disable_dialog_bool");
        }
        return false;
    }

    public void setServiceClass(int i) {
        this.mServiceClass = i;
        Log.d("CallForwardEditPreference", "set service class to: " + this.mServiceClass);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCommandException(CommandException commandException) {
        if (commandException.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
            Log.d("CallForwardEditPreference", "receive REQUEST_NOT_SUPPORTED");
            setEnabled(false);
        } else {
            this.mTcpListener.onException(this, commandException);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDelaytime() {
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(this.mPhone.getSubId());
        if (carrierConfigForSubId != null) {
            return carrierConfigForSubId.getInt("mtk_carrier_ss_query_delay_time", 0);
        }
        return 0;
    }
}
