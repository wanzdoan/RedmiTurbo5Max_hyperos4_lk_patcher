package com.android.phone;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.UserHandle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telecom.PhoneAccountHandle;
import android.telephony.MiStubParam;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.settings.SuppServicesUiUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class PhoneUtils {
    private static final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
    private static StringBuilder sUssdMsg = new StringBuilder();
    private static AlertDialog sUssdDialog = null;
    private static final ComponentName PSTN_CONNECTION_SERVICE_COMPONENT = new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.services.telephony.TelephonyConnectionService");

    /* JADX WARN: Code duplicated, block: B:41:0x00e3  */
    /* JADX WARN: Code duplicated, block: B:56:0x0139  */
    /* JADX WARN: Code duplicated, block: B:59:0x0141  */
    /* JADX WARN: Code duplicated, block: B:61:0x0149  */
    /* JADX WARN: Code duplicated, block: B:70:0x01b5  */
    /* JADX WARN: Code duplicated, block: B:73:0x01bf  */
    /* JADX WARN: Code duplicated, block: B:75:0x01ce  */
    /* JADX WARN: Code duplicated, block: B:78:0x01e5  */
    /* JADX WARN: Code duplicated, block: B:81:0x027d  */
    /* JADX WARN: Code duplicated, block: B:83:? A[RETURN, SYNTHETIC] */
    static void displayMMIComplete(final Phone phone, Context context, final MmiCode mmiCode, Message message, AlertDialog alertDialog) {
        CharSequence message2;
        int i;
        int i2;
        int callForwardTitleResId;
        String operatorNumeric;
        PowerManager powerManager;
        PersistableBundle carrierConfig;
        if (MiPhoneAppStub.get().displayMMIComplete(phone, context, mmiCode, message, alertDialog, MiStubParam.obtain())) {
            return;
        }
        final PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        MmiCode.State state = mmiCode.getState();
        log("displayMMIComplete: state=" + state);
        int i3 = AnonymousClass4.$SwitchMap$com$android$internal$telephony$MmiCode$State[state.ordinal()];
        if (i3 == 1) {
            message2 = mmiCode.getMessage();
            StringBuilder sb = new StringBuilder("displayMMIComplete: using text from PENDING MMI message: '");
            sb.append(message2 != null ? Rlog.piiHandle(message2.toString()) : "null");
            sb.append("'");
            log(sb.toString());
        } else {
            if (i3 != 2) {
                if (i3 == 3) {
                    if (SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
                        carrierConfig = phoneGlobals.getCarrierConfigForSubId(phone.getSubId());
                    } else {
                        carrierConfig = phoneGlobals.getCarrierConfig();
                    }
                    if (carrierConfig.getBoolean("use_caller_id_ussd_bool")) {
                        message2 = SuppServicesUiUtil.handleCallerIdUssdResponse(phoneGlobals, context, phone, mmiCode);
                        if (mmiCode.getMessage() == null || message2.equals(mmiCode.getMessage())) {
                        }
                    }
                    if (phoneGlobals.getPUKEntryActivity() != null) {
                        message2 = context.getText(R.string.puk_unlocked);
                        i = android.R.string.SetupCallDefault;
                    }
                } else if (i3 != 4) {
                    OrtUtil$$ExternalSyntheticBUOutline2.m("Unexpected MmiCode state: ", state);
                    return;
                }
                message2 = mmiCode.getMessage();
                StringBuilder sb2 = new StringBuilder("displayMMIComplete (failed): using text from MMI message: '");
                sb2.append(message2 != null ? Rlog.piiHandle(message2.toString()) : "null");
                sb2.append("'");
                log(sb2.toString());
            } else {
                i = 0;
                message2 = null;
            }
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            if (phoneGlobals.getPUKEntryActivity() == null && state == MmiCode.State.COMPLETE) {
                log("displaying PUK unblocking progress dialog.");
                ProgressDialog progressDialog = new ProgressDialog(phoneGlobals, android.R.style.Theme.DeviceDefault.Dialog.Alert);
                progressDialog.setTitle(i);
                progressDialog.setMessage(message2);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.getWindow().setType(2008);
                progressDialog.getWindow().addFlags(2);
                progressDialog.show();
                phoneGlobals.setPukEntryProgressDialog(progressDialog);
                return;
            }
            if (phoneGlobals.getPUKEntryActivity() == null && state == MmiCode.State.FAILED) {
                createUssdDialog(phoneGlobals, context, message2, phone, 2009);
                phoneGlobals.setPukEntryActivity(null);
                return;
            }
            if (phoneGlobals.getPUKEntryActivity() != null) {
                phoneGlobals.setPukEntryActivity(null);
            }
            if (state != MmiCode.State.PENDING) {
                i2 = 2003;
                if (TelephonyBaseUtilsStub.isMiuiRom()) {
                    callForwardTitleResId = getCallForwardTitleResId(mmiCode.getDialString());
                    if (shouldShowCfTypeWhenMmi(mmiCode.getPhone()) && callForwardTitleResId != -1) {
                        StringBuilder sb3 = new StringBuilder(message2);
                        int iIndexOf = sb3.indexOf("\n");
                        log("displayMMIComplete: index=" + iIndexOf);
                        sb3.insert(iIndexOf + 1, phoneGlobals.getResources().getString(callForwardTitleResId) + "\n");
                        message2 = sb3.toString();
                    }
                    if (phone.getIccCard() == null && phone.getIccCard().getIccRecords() != null) {
                        operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
                    } else {
                        operatorNumeric = "";
                    }
                    if ("45403".equals(operatorNumeric)) {
                        powerManager = (PowerManager) context.getSystemService("power");
                        if (!powerManager.isScreenOn()) {
                            PowerManager.WakeLock wakeLockNewWakeLock = powerManager.newWakeLock(268435462, "phone");
                            wakeLockNewWakeLock.acquire();
                            wakeLockNewWakeLock.release();
                        }
                        i2 = 2010;
                    }
                }
                createUssdDialog(phoneGlobals, context, message2, phone, i2);
            } else {
                log("displayMMIComplete: USSD code has requested user input. Constructing input dialog.");
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.DialerAlertDialogTheme);
                View viewInflate = ((LayoutInflater) contextThemeWrapper.getSystemService("layout_inflater")).inflate(R.layout.dialog_ussd_response, (ViewGroup) null);
                final EditText editText = (EditText) viewInflate.findViewById(R.id.input_field);
                editText.setTextOperationUser(UserHandle.of(ActivityManager.getCurrentUser()));
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.phone.PhoneUtils.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i4) {
                        if (i4 == -2) {
                            if (mmiCode.isCancelable()) {
                                mmiCode.cancel();
                            }
                        } else {
                            if (i4 != -1) {
                                return;
                            }
                            if (editText.length() < 1 || editText.length() > 160) {
                                PhoneGlobals phoneGlobals2 = phoneGlobals;
                                Toast.makeText(phoneGlobals2, phoneGlobals2.getResources().getString(R.string.enter_input, 1, 160), 1).show();
                                if (mmiCode.isCancelable()) {
                                    mmiCode.cancel();
                                    return;
                                }
                                return;
                            }
                            phone.sendUssdResponse(editText.getText().toString());
                        }
                    }
                };
                final AlertDialog alertDialogCreate = new AlertDialog.Builder(contextThemeWrapper).setMessage(message2).setView(viewInflate).setPositiveButton(R.string.send_button, onClickListener).setNegativeButton(R.string.cancel, onClickListener).setCancelable(false).create();
                editText.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.phone.PhoneUtils.2
                    @Override // android.view.View.OnKeyListener
                    public boolean onKey(View view, int i4, KeyEvent keyEvent) {
                        if (i4 != 5 && i4 != 66) {
                            return false;
                        }
                        if (keyEvent.getAction() != 0) {
                            return true;
                        }
                        phone.sendUssdResponse(editText.getText().toString());
                        alertDialogCreate.dismiss();
                        return true;
                    }
                });
                editText.requestFocus();
                alertDialogCreate.getWindow().setType(2008);
                alertDialogCreate.getWindow().addFlags(2);
                alertDialogCreate.show();
                alertDialogCreate.getButton(-1).setTextColor(context.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color));
                alertDialogCreate.getButton(-2).setTextColor(context.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color));
            }
            if (mmiCode.isNetworkInitiatedUssd()) {
                playSound(context);
            }
        }
        i = 0;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (phoneGlobals.getPUKEntryActivity() == null) {
        }
        if (phoneGlobals.getPUKEntryActivity() == null) {
        }
        if (phoneGlobals.getPUKEntryActivity() != null) {
            phoneGlobals.setPukEntryActivity(null);
        }
        if (state != MmiCode.State.PENDING) {
            i2 = 2003;
            if (TelephonyBaseUtilsStub.isMiuiRom()) {
                callForwardTitleResId = getCallForwardTitleResId(mmiCode.getDialString());
                if (shouldShowCfTypeWhenMmi(mmiCode.getPhone())) {
                    StringBuilder sb4 = new StringBuilder(message2);
                    int iIndexOf2 = sb4.indexOf("\n");
                    log("displayMMIComplete: index=" + iIndexOf2);
                    sb4.insert(iIndexOf2 + 1, phoneGlobals.getResources().getString(callForwardTitleResId) + "\n");
                    message2 = sb4.toString();
                }
                if (phone.getIccCard() == null) {
                    operatorNumeric = "";
                } else {
                    operatorNumeric = "";
                }
                if ("45403".equals(operatorNumeric)) {
                    powerManager = (PowerManager) context.getSystemService("power");
                    if (!powerManager.isScreenOn()) {
                        PowerManager.WakeLock wakeLockNewWakeLock2 = powerManager.newWakeLock(268435462, "phone");
                        wakeLockNewWakeLock2.acquire();
                        wakeLockNewWakeLock2.release();
                    }
                    i2 = 2010;
                }
            }
            createUssdDialog(phoneGlobals, context, message2, phone, i2);
        } else {
            log("displayMMIComplete: USSD code has requested user input. Constructing input dialog.");
            ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, R.style.DialerAlertDialogTheme);
            View viewInflate2 = ((LayoutInflater) contextThemeWrapper2.getSystemService("layout_inflater")).inflate(R.layout.dialog_ussd_response, (ViewGroup) null);
            final EditText editText2 = (EditText) viewInflate2.findViewById(R.id.input_field);
            editText2.setTextOperationUser(UserHandle.of(ActivityManager.getCurrentUser()));
            DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.android.phone.PhoneUtils.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i4) {
                    if (i4 == -2) {
                        if (mmiCode.isCancelable()) {
                            mmiCode.cancel();
                        }
                    } else {
                        if (i4 != -1) {
                            return;
                        }
                        if (editText2.length() < 1 || editText2.length() > 160) {
                            PhoneGlobals phoneGlobals2 = phoneGlobals;
                            Toast.makeText(phoneGlobals2, phoneGlobals2.getResources().getString(R.string.enter_input, 1, 160), 1).show();
                            if (mmiCode.isCancelable()) {
                                mmiCode.cancel();
                                return;
                            }
                            return;
                        }
                        phone.sendUssdResponse(editText2.getText().toString());
                    }
                }
            };
            final AlertDialog alertDialogCreate2 = new AlertDialog.Builder(contextThemeWrapper2).setMessage(message2).setView(viewInflate2).setPositiveButton(R.string.send_button, onClickListener2).setNegativeButton(R.string.cancel, onClickListener2).setCancelable(false).create();
            editText2.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.phone.PhoneUtils.2
                @Override // android.view.View.OnKeyListener
                public boolean onKey(View view, int i4, KeyEvent keyEvent) {
                    if (i4 != 5 && i4 != 66) {
                        return false;
                    }
                    if (keyEvent.getAction() != 0) {
                        return true;
                    }
                    phone.sendUssdResponse(editText2.getText().toString());
                    alertDialogCreate2.dismiss();
                    return true;
                }
            });
            editText2.requestFocus();
            alertDialogCreate2.getWindow().setType(2008);
            alertDialogCreate2.getWindow().addFlags(2);
            alertDialogCreate2.show();
            alertDialogCreate2.getButton(-1).setTextColor(context.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color));
            alertDialogCreate2.getButton(-2).setTextColor(context.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color));
        }
        if (mmiCode.isNetworkInitiatedUssd()) {
            playSound(context);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.PhoneUtils$4, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$MmiCode$State;

        static {
            int[] iArr = new int[MmiCode.State.values().length];
            $SwitchMap$com$android$internal$telephony$MmiCode$State = iArr;
            try {
                iArr[MmiCode.State.PENDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$MmiCode$State[MmiCode.State.CANCELLED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$MmiCode$State[MmiCode.State.COMPLETE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$MmiCode$State[MmiCode.State.FAILED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private static void playSound(Context context) {
        int ringerMode = ((AudioManager) context.getSystemService("audio")).getRingerMode();
        if (ringerMode != 2) {
            if (ringerMode == 1) {
                log("playSound : RINGER_MODE_VIBRATE");
                ((Vibrator) context.getSystemService("vibrator")).vibrate(VibrationEffect.createWaveform(getLongArray(context.getResources(), R.array.config_defaultNotificationVibePattern, DEFAULT_VIBRATE_PATTERN), -1), new AudioAttributes.Builder().setUsage(5).build());
                return;
            }
            return;
        }
        log("playSound : RINGER_MODE_NORMAL");
        try {
            Uri defaultUri = RingtoneManager.getDefaultUri(2);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(context, defaultUri);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(5).setUsage(5).build());
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            log("playSound exception : " + e);
        }
    }

    private static long[] getLongArray(Resources resources, int i, long[] jArr) {
        int[] intArray = resources.getIntArray(i);
        if (intArray == null) {
            return jArr;
        }
        int length = intArray.length;
        long[] jArr2 = new long[length];
        for (int i2 = 0; i2 < length; i2++) {
            jArr2[i2] = intArray[i2];
        }
        return jArr2;
    }

    public static void createUssdDialog(PhoneGlobals phoneGlobals, Context context, CharSequence charSequence, Phone phone, int i) {
        String strPiiHandle;
        log("displayMMIComplete: MMI code has finished running.");
        if (MiPhoneAppStub.get().createUssdDialog(phoneGlobals, context, charSequence, phone, i, MiStubParam.obtain())) {
            return;
        }
        StringBuilder sb = new StringBuilder("displayMMIComplete: Extended NW displayMMIInitiate (");
        if (charSequence == null) {
            strPiiHandle = "null";
        } else {
            strPiiHandle = Rlog.piiHandle(charSequence.toString());
        }
        sb.append(strPiiHandle);
        sb.append(")");
        log(sb.toString());
        if (charSequence == null || charSequence.length() == 0) {
            return;
        }
        AlertDialog alertDialog = sUssdDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            sUssdDialog = null;
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context, android.R.style.Theme.DeviceDefault.Dialog.Alert).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.PhoneUtils.3
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                PhoneUtils.sUssdDialog = null;
                PhoneUtils.sUssdMsg.setLength(0);
            }
        }).create();
        sUssdDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(i);
        alertDialogCreate.getWindow().addFlags(2);
        if (charSequence.equals(sUssdMsg.toString())) {
            sUssdMsg = new StringBuilder();
        }
        if (sUssdMsg.length() != 0) {
            sUssdMsg.insert(0, "\n").insert(0, phoneGlobals.getResources().getString(R.string.ussd_dialog_sep)).insert(0, "\n");
        }
        if (phone != null) {
            String subDisplayName = getSubDisplayName(phone.getSubId());
            if (TextUtils.isEmpty(subDisplayName)) {
                subDisplayName = phone.getCarrierName();
            }
            log("displayMMIComplete: displayName = " + subDisplayName);
            if (subDisplayName != null) {
                alertDialogCreate.setTitle(phoneGlobals.getResources().getString(R.string.carrier_mmi_msg_title, subDisplayName));
            } else {
                alertDialogCreate.setTitle(phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title));
            }
        } else {
            alertDialogCreate.setTitle(phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title));
        }
        sUssdMsg.insert(0, charSequence);
        alertDialogCreate.setMessage(sUssdMsg.toString());
        alertDialogCreate.show();
    }

    static boolean cancelMmiCode(Phone phone) {
        List pendingMmiCodes = phone.getPendingMmiCodes();
        if (pendingMmiCodes.size() > 0) {
            MmiCode mmiCode = (MmiCode) pendingMmiCodes.get(0);
            if (mmiCode.isCancelable()) {
                mmiCode.cancel();
                return true;
            }
        }
        return false;
    }

    public static boolean isRealIncomingCall(Call.State state) {
        return state == Call.State.INCOMING && !PhoneGlobals.getInstance().mCM.hasActiveFgCall();
    }

    private static void log(String str) {
        Log.d("PhoneUtils", str);
    }

    public static PhoneAccountHandle makePstnPhoneAccountHandle(Phone phone) {
        if (phone == null) {
            return null;
        }
        return makePstnPhoneAccountHandleWithPrefix(phone, "", false, phone.getUserHandle());
    }

    public static PhoneAccountHandle makePstnPhoneAccountHandleWithPrefix(Phone phone, String str, boolean z, UserHandle userHandle) {
        String str2;
        if (z) {
            str2 = "E";
        } else {
            str2 = str + String.valueOf(phone.getSubId());
        }
        return makePstnPhoneAccountHandleWithId(str2, userHandle);
    }

    public static PhoneAccountHandle makePstnPhoneAccountHandleWithId(String str, UserHandle userHandle) {
        ComponentName pstnConnectionServiceName = getPstnConnectionServiceName();
        if (userHandle == null) {
            return new PhoneAccountHandle(pstnConnectionServiceName, str);
        }
        return new PhoneAccountHandle(pstnConnectionServiceName, str, userHandle);
    }

    public static int getSubIdForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
        Phone phoneForPhoneAccountHandle = getPhoneForPhoneAccountHandle(phoneAccountHandle);
        if (phoneForPhoneAccountHandle != null) {
            return phoneForPhoneAccountHandle.getSubId();
        }
        return -1;
    }

    public static Phone getPhoneForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
        if (phoneAccountHandle == null || !phoneAccountHandle.getComponentName().equals(getPstnConnectionServiceName())) {
            return null;
        }
        return getPhoneFromSubId(phoneAccountHandle.getId());
    }

    public static boolean isPhoneAccountActive(SubscriptionManager subscriptionManager, PhoneAccountHandle phoneAccountHandle) {
        return subscriptionManager.getActiveSubscriptionInfo(Integer.parseInt(phoneAccountHandle.getId())) != null;
    }

    private static ComponentName getPstnConnectionServiceName() {
        return PSTN_CONNECTION_SERVICE_COMPONENT;
    }

    private static Phone getPhoneFromSubId(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (str.equals(Integer.toString(phone.getSubId()))) {
                return phone;
            }
        }
        return null;
    }

    static final void registerIccStatus(Handler handler, int i) {
        for (Phone phone : PhoneFactory.getPhones()) {
            IccCard iccCard = phone.getIccCard();
            if (iccCard != null) {
                iccCard.registerForNetworkLocked(handler, i, phone);
            }
        }
    }

    static final void registerIccStatus(Handler handler, int i, int i2) {
        Phone[] phones = PhoneFactory.getPhones();
        IccCard iccCard = phones[i2].getIccCard();
        if (iccCard != null) {
            iccCard.registerForNetworkLocked(handler, i, phones[i2]);
        }
    }

    static final void unregisterIccStatus(Handler handler, int i) {
        IccCard iccCard = PhoneFactory.getPhones()[i].getIccCard();
        if (iccCard != null) {
            iccCard.unregisterForNetworkLocked(handler);
        }
    }

    static final void setRadioPower(boolean z) {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.setRadioPower(z);
        }
    }

    public static List getActiveSubInfoList() {
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(PhoneGlobals.getInstance()).getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return new ArrayList();
        }
        return new ArrayList(activeSubscriptionInfoList);
    }

    public static String getSubDisplayName(int i) {
        CharSequence displayName;
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(PhoneGlobals.getInstance()).getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo == null) {
            displayName = "";
        } else {
            displayName = activeSubscriptionInfo.getDisplayName();
        }
        return displayName.toString();
    }

    public static boolean isValidSubId(int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(PhoneGlobals.getInstance()).getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return false;
        }
        Iterator<SubscriptionInfo> it = activeSubscriptionInfoList.iterator();
        while (it.hasNext()) {
            if (it.next().getSubscriptionId() == i) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldShowCfTypeWhenMmi(Phone phone) {
        PersistableBundle carrierConfigForSubId;
        if (phone == null || (carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId())) == null) {
            return false;
        }
        return carrierConfigForSubId.getBoolean("config_show_cf_type_when_mmi", false);
    }

    private static int getCallForwardTitleResId(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (str.equalsIgnoreCase("*#21#")) {
            return R.string.labelCFU;
        }
        if (str.equalsIgnoreCase("*#67#")) {
            return R.string.labelCFB;
        }
        if (str.equalsIgnoreCase("*#61#")) {
            return R.string.labelCFNRy;
        }
        if (str.equalsIgnoreCase("*#62#")) {
            return R.string.labelCFNRc;
        }
        return -1;
    }
}
