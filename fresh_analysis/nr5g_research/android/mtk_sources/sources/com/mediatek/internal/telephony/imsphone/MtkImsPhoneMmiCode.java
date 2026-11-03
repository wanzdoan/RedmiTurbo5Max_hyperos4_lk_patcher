package com.mediatek.internal.telephony.imsphone;

import android.R;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.android.ims.ImsException;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.util.ArrayUtils;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class MtkImsPhoneMmiCode extends ImsPhoneMmiCode {
    private static final int DIALOG_DISPLAY_TIME = 1000;
    static final String LOG_TAG = "MtkImsPhoneMmiCode";
    private static final String SC_CFNotRegister = "68";
    private static final boolean SENLOG = TextUtils.equals(Build.TYPE, "user");
    static Pattern sFac = Pattern.compile("^\\#\\d+\\#[^*]+\\*$");

    public MtkImsPhoneMmiCode(ImsPhone phone, FeatureFlags featureFlags) {
        super(phone, featureFlags);
    }

    protected static int siToServiceClass(String si) {
        int sc = ImsPhoneMmiCode.siToServiceClass(si);
        if (sc == 16) {
            return sc + 512;
        }
        return sc;
    }

    public boolean isSupportedOverImsPhone() {
        if (isShortCode()) {
            return true;
        }
        if (!isServiceCodeCallForwarding(this.mSc) && !isServiceCodeCallBarring(this.mSc) && ((this.mSc == null || !this.mSc.equals("43")) && ((this.mSc == null || !this.mSc.equals("31")) && ((this.mSc == null || !this.mSc.equals("30")) && ((this.mSc == null || !this.mSc.equals("77")) && ((this.mSc == null || !this.mSc.equals("76")) && ((this.mSc == null || !this.mSc.equals("156")) && (this.mSc == null || !this.mSc.equals("157"))))))))) {
            return !isPinPukCommand() && (this.mSc == null || !(this.mSc.equals("03") || this.mSc.equals("30") || this.mSc.equals("31"))) && this.mPoundString != null;
        }
        if (supportMdAutoSetupIms()) {
            try {
                int serviceClass = siToServiceClass(this.mSib);
                if ((serviceClass & 1) == 0 && (serviceClass & 512) == 0 && serviceClass != 0) {
                    return false;
                }
                Rlog.d(LOG_TAG, "isSupportedOverImsPhone(), return true!");
                return true;
            } catch (RuntimeException exc) {
                Rlog.d(LOG_TAG, "Invalid service class " + exc);
            }
        }
        return false;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 4:
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception == null && msg.arg1 == 1) {
                    boolean cffEnabled = msg.arg2 == 1;
                    Rlog.i(LOG_TAG, "EVENT_SET_CFF_COMPLETE: cffEnabled:" + cffEnabled + ", mIccRecords=" + this.mIccRecords);
                    if (this.mIccRecords != null) {
                        this.mPhone.setVoiceCallForwardingFlag(1, cffEnabled, this.mDialingNumber);
                    }
                }
                onSetComplete(msg, ar);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private boolean supportMdAutoSetupIms() {
        if (!SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return true;
    }

    protected void onQueryCfComplete(AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception != null) {
            super.onQueryCfComplete(ar);
            return;
        }
        CallForwardInfo[] infos = (CallForwardInfo[]) ar.result;
        if (infos == null || infos.length == 0) {
            sb.append(this.mContext.getText(17042203));
            if (this.mIccRecords != null) {
                this.mPhone.setVoiceCallForwardingFlag(1, false, (String) null);
            }
        } else {
            SpannableStringBuilder tb = new SpannableStringBuilder();
            for (int serviceClassMask = 1; serviceClassMask <= 512; serviceClassMask <<= 1) {
                if (serviceClassMask != 256) {
                    int s = infos.length;
                    for (int i = 0; i < s; i++) {
                        if ((infos[i].serviceClass & serviceClassMask) != 0) {
                            tb.append(makeCFQueryResultMessage(infos[i], serviceClassMask));
                            tb.append((CharSequence) "\n");
                        }
                    }
                }
            }
            sb.append((CharSequence) tb);
        }
        this.mState = MmiCode.State.COMPLETE;
        this.mMessage = sb;
        Rlog.d(LOG_TAG, "onQueryCfComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onSuppSvcQueryComplete(AsyncResult ar) {
        if (isServiceCodeCallBarring(this.mSc) && ar.exception == null && !(ar.result instanceof Bundle)) {
            StringBuilder sb = new StringBuilder(getScString());
            sb.append("\n");
            Rlog.d(LOG_TAG, "onSuppSvcQueryComplete: Received Call Barring Response.");
            int[] cbInfos = (int[]) ar.result;
            if (cbInfos[0] == 0) {
                sb.append(this.mContext.getText(17042203));
            } else {
                sb.append(createQueryCallBarringResultMessage(cbInfos[0]));
            }
            this.mState = MmiCode.State.COMPLETE;
            this.mMessage = sb;
            Rlog.d(LOG_TAG, "onSuppSvcQueryComplete mmi=" + this);
            this.mPhone.onMMIDone(this);
            return;
        }
        super.onSuppSvcQueryComplete(ar);
    }

    private CharSequence createQueryCallBarringResultMessage(int serviceClass) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(17042205));
        for (int classMask = 1; classMask <= 512; classMask <<= 1) {
            if ((classMask & serviceClass) != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(classMask & serviceClass));
            }
        }
        return sb;
    }

    protected CharSequence serviceClassToCFString(int serviceClass) {
        Rlog.d(LOG_TAG, "serviceClassToCFString, serviceClass = " + serviceClass);
        switch (serviceClass) {
            case 256:
            case 512:
                return this.mContext.getText(17042201);
            default:
                return super.serviceClassToCFString(serviceClass);
        }
    }

    public CharSequence getMmiErrorMessage(AsyncResult ar) {
        CharSequence errorMessage = super.getMmiErrorMessage(ar);
        if (errorMessage == null) {
            CharSequence errorMessage2 = this.mContext.getText(R.string.permdesc_readPhoneState);
            if (ar.exception instanceof ImsException) {
                switch (ar.exception.getCode()) {
                    case 61449:
                        ImsException error = ar.exception;
                        String errorMsg = error.getMessage();
                        if (errorMsg != null && !errorMsg.isEmpty()) {
                            return errorMsg;
                        }
                        return errorMessage2;
                    default:
                        return errorMessage2;
                }
            }
            if (ar.exception instanceof CommandException) {
                CommandException err = ar.exception;
                switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$CommandException$Error[err.getCommandError().ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        String errorMsg2 = err.getMessage();
                        if (errorMsg2 != null && !errorMsg2.isEmpty()) {
                            return errorMsg2;
                        }
                        return errorMessage2;
                    default:
                        return errorMessage2;
                }
            }
            return errorMessage2;
        }
        return errorMessage;
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.imsphone.MtkImsPhoneMmiCode$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error = new int[CommandException.Error.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_6.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_23.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_24.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_25.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    protected CharSequence getImsErrorMessage(AsyncResult ar) {
        ImsException error = ar.exception;
        CharSequence errorMessage = getMmiErrorMessage(ar);
        if (errorMessage != null) {
            return errorMessage;
        }
        if (error.getMessage() != null) {
            String errorMsg = removeLastErrorCode(error.getMessage());
            Rlog.d(LOG_TAG, "getImsErrorMessage, errorMsg = " + errorMsg);
            return errorMsg;
        }
        return getErrorMessage(ar);
    }

    protected CharSequence getErrorMessage(AsyncResult ar) {
        if (ar.exception instanceof CommandException) {
            CommandException err = ar.exception;
            String errorMsg = err.getMessage();
            if (err.getCommandError() == CommandException.Error.OEM_ERROR_1 && errorMsg != null && !errorMsg.isEmpty()) {
                String errorMsg2 = removeLastErrorCode(errorMsg);
                Rlog.d(LOG_TAG, "getErrorMessage, errorMsg = " + errorMsg2);
                return errorMsg2;
            }
        }
        return super.getErrorMessage(ar);
    }

    private String removeLastErrorCode(CharSequence str) {
        String reverse = new StringBuilder(str).reverse().toString();
        String result = new StringBuilder(reverse.replaceFirst("\\)[0-9]{5}\\(", "")).reverse().toString();
        return result;
    }

    public static boolean isServiceCodeCallForwardingEx(String sc) {
        return sc != null && sc.equals(SC_CFNotRegister);
    }

    public static int scToCallForwardReasonEx(String sc) {
        if (sc.equals(SC_CFNotRegister)) {
            return 6;
        }
        throw new RuntimeException("scToCallForwardReasonEx: invalid call forward sc");
    }

    private void handleGeneralError() {
        this.mState = MmiCode.State.FAILED;
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        sb.append(this.mContext.getText(R.string.permdesc_readPhoneState));
        this.mMessage = sb;
        this.mPhone.onMMIDone(this);
    }

    private static boolean isFacFormatNumber(String dialString) {
        Matcher m = sFac.matcher(dialString);
        return m.matches();
    }

    protected boolean isFacToDial(String dialString) {
        if (!isFacFormatNumber(dialString)) {
            return false;
        }
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        PersistableBundle b = configManager.getConfigForSubId(this.mPhone.getSubId());
        if (b != null) {
            String[] dialFacList = b.getStringArray("feature_access_codes_string_array");
            if (!ArrayUtils.isEmpty(dialFacList)) {
                for (String fac : dialFacList) {
                    if (fac.equals(this.mSc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isVoiceUnconditionalForwarding(int reason, int serviceClass) {
        return GsmMmiCode.isVoiceUnconditionalForwarding(reason, serviceClass);
    }

    public String toString() {
        return "MtkImsPhoneMmiCode <" + super.toString() + ">";
    }
}
