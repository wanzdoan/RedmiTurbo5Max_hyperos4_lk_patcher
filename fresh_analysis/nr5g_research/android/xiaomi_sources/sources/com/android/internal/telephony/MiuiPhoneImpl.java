package com.android.internal.telephony;

import android.app.ActivityThread;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.MiStubParam;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.cat.BindEsimService;
import com.android.internal.telephony.qcrilhook.GUTIController;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.util.TelephonyUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CommenUtils;
import miui.telephony.MiuiDeviceStateMonitor;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneImpl extends Handler implements MiuiPhoneStub.IMiuiPhone {
    private static final int EVENT_OEM_RAW_URC = 1005;
    private static final int EVENT_SET_PREFERRED_NETWORK_TYPE_DONE = 100;
    private static final String GOOGLE_LPA_PACKAGE_NAME = "com.google.android.euicc";
    private static final String IS_JUMP_MLPA = "is_jump_mLPA";
    private static final String LOG_TAG = "MiuiPhoneStubImpl";
    private static final String MIUI_LPA_PACKAGE_NAME = "com.miui.euicc";
    private static final String MIUI_LPA_TECH_VERSION = "lui";
    private static final String MIUI_LPA_TECH_VERSION_NAME = "mi_lpa_tech_version";
    private static final String OEM_RIL_STALL_INDICATION = "RIL_STALL_DETECTED";
    private BindEsimService bindEsimService;
    private MiuiDeviceStateMonitor mDeviceStateMonitor;
    private Phone mPhone;

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception != null) {
                    Rlog.d(LOG_TAG, "EVENT_SET_PREFERRED_NETWORK_TYPE_DONE, e=" + ar.exception);
                }
                break;
        }
    }

    public MiuiPhoneImpl() {
        super(Looper.getMainLooper());
    }

    public void init(Phone phone, Object... arg) {
        this.mPhone = phone;
        this.mDeviceStateMonitor = new MiuiDeviceStateMonitor(phone);
        Rlog.i(LOG_TAG, "MiuiPhoneImpl Create BindEsimService isSupportEuiccBinding = " + isSupportEuiccBinding());
        if (isSupportEuiccBinding()) {
            this.bindEsimService = new BindEsimService(this.mPhone.mCi, this.mPhone.mContext, this.mPhone.mPhoneId);
            Rlog.i(LOG_TAG, "MiuiPhoneImpl Create BindEsimService sub = " + this.mPhone.mPhoneId);
        }
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
    }

    public void dispose(Phone phone) {
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
    }

    public boolean forceUseCsForCall(Phone phone, String dialStr) {
        return MiuiCallControllerImpl.Processor.forceUseCsForCall(phone, dialStr);
    }

    public boolean needSendUssdWhenAPM(Phone phone, String dialStr) {
        return MiuiCallControllerImpl.Processor.needSendUssdWhenAPM(phone, dialStr);
    }

    public boolean isActiveSubId(int phoneSubId, Phone phone) {
        return InternalTelephonyUtils.isActiveSubId(phoneSubId, phone);
    }

    public boolean getVMNumberIgnoreSimRecords(Phone phone, String number) {
        return MiuiCallControllerImpl.Processor.getVMNumberIgnoreSimRecords(phone, number);
    }

    public boolean isUssdRelease(Phone phone, int ussdMode) {
        return MiuiCallControllerImpl.Processor.isUssdRelease(phone, ussdMode);
    }

    public void updateAllowedNetworkTypesFromDb(Phone phone) {
        Message msg = obtainMessage(100);
        InternalTelephonyUtils.updateAllowedNetworkTypesFromDb(phone, msg);
    }

    public String getLine1Number(String msisdn, Phone phone) {
        if (phone == null) {
            return "";
        }
        boolean isValidMobileNumber = InternalTelephonyUtils.isValidMobileNumber(phone, msisdn);
        if (!TextUtils.isEmpty(msisdn) && isValidMobileNumber) {
            return msisdn;
        }
        String number = phone.getImsPhone() != null ? phone.getImsPhone().getSubscriberUriNumber() : null;
        if (!TextUtils.isEmpty(number)) {
            return number;
        }
        return InternalTelephonyUtils.getImsSourceNumber(phone);
    }

    public int forceSetLteonlyAndIgnoreUIPrefer(int phoneId, int filteredRaf) {
        if (InternalTelephonyUtils.forceSetLteonlyAndIgnoreUIPrefer(phoneId)) {
            Rlog.i(LOG_TAG, "kddi special filteredRaf:" + filteredRaf);
            return 4096;
        }
        return filteredRaf;
    }

    public String getIccId(int phoneId) {
        UiccPort port;
        if (!PhoneFactory.getPhone(phoneId).getHalVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5) || (port = UiccController.getInstance().getUiccPort(phoneId)) == null) {
            return null;
        }
        return IccUtils.stripTrailingFs(port.getIccId());
    }

    public boolean disableSecondarySim(int phoneId, boolean hasRegistered) {
        if (TelephonyManager.isCustSingleSimDevice() && phoneId == 1) {
            if (hasRegistered) {
                MiuiNetworkControllerImpl.Processor.processForCustSingleSimDevice();
            }
            return true;
        }
        return false;
    }

    public void purgeGuti(Context context, String reason, int phoneId) {
        GUTIController.purgeGuti(context, reason, phoneId);
    }

    public boolean isRadioOffForced(int phoneId, boolean on, boolean forEmergencyCall, MiStubParam param) {
        if (!TelephonyCommonUtilsStub.get().isDemoBuild()) {
            if ((TelephonyCommonUtilsImpl.isSimLock(phoneId) && !forEmergencyCall) || MiuiTelephonyManagerStub.isSatelliteBox()) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void injectDeviceStateMonitor(Phone phone, DeviceStateMonitor monitor) {
        this.mDeviceStateMonitor.init(phone, monitor);
    }

    public MiuiDeviceStateMonitor getDeviceStateMonitor() {
        return this.mDeviceStateMonitor;
    }

    public void notifyDeviceStateListeners(Phone phone, boolean screenOn, boolean charging, boolean powerSaveOn) {
        this.mDeviceStateMonitor.notifyDeviceStateListeners(screenOn, charging, powerSaveOn);
    }

    public void notifyDeviceStateListeners(Phone phone, int eventType, boolean screenOn, boolean charging, boolean powerSaveOn) {
        this.mDeviceStateMonitor.notifyDeviceStateListeners(screenOn, charging, powerSaveOn);
        if (eventType == 2 || eventType == 4 || eventType == 3) {
            notifyDeviceStateListeners(phone, screenOn, charging, powerSaveOn);
        }
    }

    public boolean isNeedUpdateDeviceState(Phone phone, int param, MiStubParam extra) {
        return this.mDeviceStateMonitor.isNeedUpdateDeviceState(param);
    }

    public int generatorDeviceStateNewFilter(Phone phone, int param, int newFilter) {
        return this.mDeviceStateMonitor.generatorDeviceStateNewFilter(param, newFilter);
    }

    public void handleMessage(Phone phone, Message msg, MiStubParam extra) {
        AsyncResult ar;
        byte[] response;
        String[] splitStr;
        switch (msg.what) {
            case EVENT_OEM_RAW_URC /* 1005 */:
                if (MiuiTelephony.IS_XRING && (ar = (AsyncResult) msg.obj) != null && (response = (byte[]) ar.result) != null && (splitStr = new String(response).split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE)) != null && splitStr.length == 2 && OEM_RIL_STALL_INDICATION.equals(splitStr[0])) {
                    Intent intent = new Intent(CommenUtils.ACTION_RADIO_HEART_BEAT_IND);
                    intent.putExtra(CommenUtils.BROADCAST_SUB, phone.getPhoneId());
                    intent.putExtra(CommenUtils.BROADCAST_CONTEXT, splitStr[1].getBytes());
                    phone.getContext().sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
                }
                break;
        }
    }

    public void notifyHandoverProvider(final Context context, int action, int reason) {
        if (context == null) {
            return;
        }
        if (!isSupportHandoverProvider(context)) {
            Rlog.i(LOG_TAG, "HandoverProvider don't support");
            return;
        }
        Rlog.i(LOG_TAG, "reason=" + reason);
        if (reason == 0) {
            final ContentValues values = new ContentValues();
            values.put(ModemUtils.PROP_SMS_AUTO_REG_ON, Integer.valueOf(action == 3 ? 1 : 0));
            values.put("reason", Integer.valueOf(reason));
            values.put("timestamp", Long.valueOf(System.currentTimeMillis()));
            new Thread(new Runnable() { // from class: com.android.internal.telephony.MiuiPhoneImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MiuiPhoneImpl.lambda$notifyHandoverProvider$0(context, values);
                }
            }).start();
            return;
        }
        Rlog.i(LOG_TAG, "Skip notify: reason=" + reason + " != 0");
    }

    static /* synthetic */ void lambda$notifyHandoverProvider$0(Context context, ContentValues values) {
        try {
            Uri uri = Uri.parse("content://com.xiaomi.security.xsof.FraudDetectionProvider/call_forwarding");
            context.getContentResolver().insert(uri, values);
            Rlog.i(LOG_TAG, "Handover provider notified: enabled=" + values.getAsInteger(ModemUtils.PROP_SMS_AUTO_REG_ON) + ", timestamp=" + values.getAsLong("timestamp"));
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "Provider insert failed", e);
        }
    }

    private boolean isSupportEuiccBinding() {
        return HyperOSCustFeatureResolve.getBoolean("config_esim_euicc_binding_support", false);
    }

    public boolean notifyStkProCmdForEsimBinding(String proCmdData) {
        Rlog.i(LOG_TAG, "notifyStkProCmdForEsimBinding = " + proCmdData);
        if (this.bindEsimService == null || !BindEsimService.isNeedHandedByBindEsimService(proCmdData)) {
            return false;
        }
        this.bindEsimService.notifyStkProCmdForEsimBinding(proCmdData);
        return true;
    }

    public boolean isSupportHandoverProvider(Context context) {
        return context.getPackageManager().resolveContentProvider("com.xiaomi.security.xsof.FraudDetectionProvider", 0) != null;
    }

    public ActivityInfo overrideBestEuiccUiActivity(PackageManager pm, Intent euiccUiIntent, ActivityInfo defaultBest, List<ResolveInfo> resolveInfoList) {
        String str;
        Context ctx;
        ActivityInfo googleLpa;
        String techVersion;
        try {
            StringBuilder sbAppend = new StringBuilder().append("overrideBestEuiccUiActivity: defaultBest=");
            if (defaultBest == null) {
                str = "null";
            } else {
                str = defaultBest.packageName + "/" + defaultBest.name;
            }
            Rlog.d(LOG_TAG, sbAppend.append(str).append(" intent=").append(euiccUiIntent != null ? euiccUiIntent.getAction() : "null").toString());
            if (defaultBest == null || (ctx = ActivityThread.currentApplication()) == null) {
                return null;
            }
            int deviceProvisioned = Settings.Global.getInt(ctx.getContentResolver(), "device_provisioned", 0);
            Rlog.d(LOG_TAG, "overrideBestEuiccUiActivity: DEVICE_PROVISIONED=" + deviceProvisioned);
            if (deviceProvisioned != 0) {
                return null;
            }
            int jumpMlpa = Settings.System.getInt(ctx.getContentResolver(), IS_JUMP_MLPA, 0);
            Rlog.d(LOG_TAG, "overrideBestEuiccUiActivity: IS_JUMP_MLPA=" + jumpMlpa);
            if (jumpMlpa == 1 && MIUI_LPA_PACKAGE_NAME.equals(defaultBest.packageName)) {
                PackageInfo pi = pm.getPackageInfo(MIUI_LPA_PACKAGE_NAME, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                if (pi != null && pi.applicationInfo != null && pi.applicationInfo.metaData != null) {
                    techVersion = pi.applicationInfo.metaData.getString(MIUI_LPA_TECH_VERSION_NAME);
                } else {
                    techVersion = null;
                }
                Rlog.d(LOG_TAG, "overrideBestEuiccUiActivity: mi_lpa_tech_version=" + techVersion + " expected=" + MIUI_LPA_TECH_VERSION);
                if (MIUI_LPA_TECH_VERSION.equals(techVersion)) {
                    Rlog.i(LOG_TAG, "overrideBestEuiccUiActivity: Jump to LUI");
                    return defaultBest;
                }
            }
            if (!GOOGLE_LPA_PACKAGE_NAME.equals(defaultBest.packageName) && (googleLpa = getGoogleLpaFromList(pm, resolveInfoList)) != null) {
                Rlog.i(LOG_TAG, "overrideBestEuiccUiActivity: Replace with Google LPA");
                return googleLpa;
            }
            return null;
        } catch (Exception e) {
            Rlog.w(LOG_TAG, "overrideBestEuiccUiActivity failed", e);
        }
    }

    private ActivityInfo getGoogleLpaFromList(PackageManager pm, List<ResolveInfo> resolveInfoList) {
        if (resolveInfoList == null) {
            return null;
        }
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (isValidEuiccComponent(pm, resolveInfo)) {
                ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(resolveInfo);
                if ((componentInfo instanceof ActivityInfo) && GOOGLE_LPA_PACKAGE_NAME.equals(componentInfo.packageName)) {
                    return (ActivityInfo) componentInfo;
                }
            }
        }
        return null;
    }

    private static boolean isValidEuiccComponent(PackageManager packageManager, ResolveInfo resolveInfo) {
        String permission;
        ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(resolveInfo);
        if (componentInfo == null) {
            return false;
        }
        String packageName = componentInfo.packageName;
        if (packageManager.checkPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", packageName) != 0) {
            return false;
        }
        if (componentInfo instanceof ServiceInfo) {
            permission = ((ServiceInfo) componentInfo).permission;
        } else {
            if (!(componentInfo instanceof ActivityInfo)) {
                return false;
            }
            permission = ((ActivityInfo) componentInfo).permission;
        }
        if (!TextUtils.equals(permission, "android.permission.BIND_EUICC_SERVICE") || resolveInfo.filter == null || resolveInfo.filter.getPriority() == 0) {
            return false;
        }
        return true;
    }
}
