package com.android.phone;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline2;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CellIdentity;
import android.telephony.MiStubParam;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.settings.SuppServicesUiUtil;
import com.android.phone.settings.VoicemailSettingsActivity;
import com.android.services.telephony.MiTelephonyServiceStub;
import com.android.services.telephony.MiuiTelephonyGlobals;
import com.android.services.telephony.ModulesLoader;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import com.miui.enterprise.PhoneHelper;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.security.SecurityManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyUtils;
import miui.telephony.VirtualSimUtils;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;
import miuix.autodensity.AutoDensityConfig;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneApp implements MiPhoneAppStub.IMiPhoneApp {
    private static final int[] BIG_SIM_SLOT_ICON;
    private static final long[] DEFAULT_VIBRATE_PATTERN;
    private static final boolean IS_INTERNATIONAL_BUILD;
    private static int THEME;
    private static int mOpenChannelFailCounts;
    private static TelecomManager sTelecomManager;
    private static TelephonyManager sTelephonyManager;
    private static StringBuilder sUssdMsg;
    private AlertDialog mAd;
    private Context mContext;
    private PhoneApp mOwner;
    private ProgressDialog mPd;
    PhoneGlobals mPhoneGlobals;
    private int mPrevRoamingNotification = 0;
    MiuiTelephonyGlobals mTelephonyGlobals;
    private static int MIUI_THEME = PhoneGlobals.getInstance().getResources().getIdentifier("Theme.DayNight.Dialog.Alert", "style", "miui");
    private static final String CUSTOMIZED_REGION = SystemProperties.get("ro.miui.customized.region", "");

    static {
        int i = MIUI_THEME;
        if (i <= 0) {
            i = android.R.style.Theme.DeviceDefault.Dialog.Alert;
        }
        THEME = i;
        sUssdMsg = new StringBuilder();
        BIG_SIM_SLOT_ICON = new int[]{R.drawable.ic_sim_1_big, R.drawable.ic_sim_2_big};
        DEFAULT_VIBRATE_PATTERN = new long[]{0, 250, 250, 250};
        IS_INTERNATIONAL_BUILD = Build.IS_INTERNATIONAL_BUILD;
        mOpenChannelFailCounts = 0;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void init(PhoneApp phoneApp, PhoneGlobals phoneGlobals, Object... objArr) {
        this.mOwner = phoneApp;
        this.mPhoneGlobals = phoneGlobals;
        AutoDensityConfig.init(phoneApp);
        internalInit();
    }

    private void internalInit() {
        if (UserHandle.myUserId() == 0) {
            this.mContext = PhoneGlobals.getInstance();
            MiuiTelephonyGlobals miuiTelephonyGlobals = new MiuiTelephonyGlobals(this.mOwner);
            this.mTelephonyGlobals = miuiTelephonyGlobals;
            miuiTelephonyGlobals.onCreate();
            EventHubManager.getInstance().init(this.mContext);
            ModulesLoader.getInstance().initialize();
            MiuiPhoneUtils.setCellBroadCastEnabled(this.mContext, false);
            MiuiNotificationMgrUtils.createNotificationChannelsList(this.mOwner);
            if (MiuiPhoneUtils.isDualDataSupported()) {
                MiuiPhoneUtils.intelligentDualSimDbInit(this.mContext);
            } else if (MiuiPhoneUtils.isSmartSwitchDualSimSupported() && (!IS_INTERNATIONAL_BUILD || MiuiPhoneUtils.isCustForInternational())) {
                MiuiPhoneUtils.smartSwitchDualSimDbInit(this.mContext);
            }
            if (MiuiPhoneUtils.isHideAlwaysReceiveMms()) {
                autoReceiveMMSForDomesticVersion();
            }
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void syncPhoneAccounts() {
        DefaultSimManager.getInstance().syncPhoneAccount();
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isDataOnlySlot(Context context, Phone phone) {
        if (phone == null) {
            return false;
        }
        int phoneId = phone.getPhoneId();
        if ((!MiuiTelephonyManagerStub.get().isEsimActive() && MiuiTelephonyManagerStub.get().getEsimGPIOState() != 0) || !MiuiTelephonyManagerStub.get().isSupportEuiccBinding()) {
            return phoneId == (PhoneFactory.getPhones().length > 1 ? VirtualSimUtils.getVirtualSimSlot(context, true, 1) : -1);
        }
        Rlog.d("MiuiPhoneApp", "isDataOnlySlot");
        return phoneId == MiuiPhoneUtils.getEsimSlotId(context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5 */
    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean setAllPhonesDataEnabled(PhoneGlobals phoneGlobals, boolean z, String str) {
        boolean zCheckEntDataRestriction = PhoneHelper.checkEntDataRestriction(phoneGlobals, z);
        ?? IsCellularDataRestriction = zCheckEntDataRestriction;
        if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED) {
            IsCellularDataRestriction = PhoneHelperStub.getInstance().isCellularDataRestriction(zCheckEntDataRestriction);
        }
        localLog("setAllPhonesDataEnabled", "callingPackage = " + str + ", enable = " + ((boolean) IsCellularDataRestriction));
        Rlog.d("MiuiPhoneApp", "callingPackage = " + str + ", enable = " + ((boolean) IsCellularDataRestriction));
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < miui.telephony.TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                PhoneFactory.getPhone(i).getDataSettingsManager().obtainMessage(5, 0, (int) IsCellularDataRestriction, str).sendToTarget();
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return true;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void recordCallerInfo(int i, String str, String str2) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).recordCallerInfo(1, str, str2);
        localLog("getCellLocation", " callingPackage = " + str + ", callingFeatureId = " + str2);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isNotReadyForApplication(Context context) {
        if (!Build.IS_INTERNATIONAL_BUILD || Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0) {
            return false;
        }
        for (int i = 0; i < miui.telephony.TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (miui.telephony.TelephonyManager.getDefault().getSimStateForSlot(i) != 1) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean enforceCallPermission() {
        return miui.telephony.TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid());
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public int getPreferredVoiceSubscription() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int defaultVoiceSubscriptionId = SubscriptionManager.getDefaultVoiceSubscriptionId();
            if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubscriptionId)) {
                defaultVoiceSubscriptionId = miui.telephony.SubscriptionManager.getDefault().getDefaultSubscriptionId();
            }
            return defaultVoiceSubscriptionId;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void setAllowedNetworkTypesForReason(int i, int i2, long j) {
        DefaultSimManager.setNetworkModeInDb(SubscriptionManager.getPhoneId(i), RadioAccessFamily.getNetworkTypeFromRaf((int) j));
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void cleanUpAllowedNetworkTypes(Phone phone, int i) {
        int[][] supportModes = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportModes();
        int phoneId = SubscriptionManager.getPhoneId(i);
        int[] iArr = supportModes[phoneId];
        if (iArr.length == 0) {
            return;
        }
        DefaultSimManager.setNetworkModeInDb(phoneId, iArr[0]);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean factoryResetByFiveGCapable(Phone phone) {
        MiCarrierConfigLoaderStub.get().resetCarrierConfig();
        if (!FiveGManagerBase.isFiveGCapable() || FiveGManagerBase.getInstance() == null) {
            return false;
        }
        FiveGManagerBase.getInstance().factoryReset(phone.getPhoneId());
        return true;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isUserDataEnabled(Context context, int i) {
        if (!miui.telephony.TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            try {
                context.enforceCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE", null);
            } catch (Exception unused) {
                TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(context, i, "isUserDataEnabled");
            }
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PhoneFactory.getPhone(DefaultSimManager.getDefaultDataSlotId()).isUserDataEnabled();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isDataEnabled(Context context, int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PhoneFactory.getPhone(DefaultSimManager.getDefaultDataSlotId()).getDataSettingsManager().isDataEnabled();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean setDataEnabledForReason(Context context, int i, boolean z, String str) {
        if (PhoneHelperStub.getInstance().isMobileDataRestriction()) {
            Rlog.d("MiuiPhoneApp", "Device is in enterprise mode, mobile data is restricted by enterprise!");
            return true;
        }
        if (i == 0) {
            return setAllPhonesDataEnabled(context, z, str);
        }
        return false;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void dump(Context context, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (miui.telephony.TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid()) || context.checkCallingOrSelfPermission("android.permission.DUMP") == 0) {
            return;
        }
        printWriter.println("Permission Denial: can't dump Phone from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + "without permission android.permission.DUMP");
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void dumpDebugService(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        MiuiPhoneUtils.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isIgnoreEmergencyNumber(Phone phone, String str) {
        return MiuiPhoneUtils.isIgnoreEmergencyNumber(phone, str);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public String getMmsUserAgent(Context context, int i, String str) {
        String string = context.getString(android.R.string.factorytest_failed);
        Rlog.d("MiuiPhoneApp", "defaultSmsPkg = " + string);
        if (((string != null && string.equals("com.google.android.apps.messaging")) || miui.telephony.TelephonyManager.isGoogleCsp()) && MiuiPhoneUtils.isCustMmsUserAgentSupport()) {
            String string2 = context.getResources().getString(android.R.string.day);
            StringBuilder sb = new StringBuilder();
            sb.append(SystemProperties.get("ro.product.marketname", android.os.Build.BRAND + "-" + android.os.Build.MODEL));
            sb.append("/");
            sb.append(string2);
            String string3 = sb.toString();
            Rlog.d("MiuiPhoneApp", "userAgent =".concat(string3));
            return string3;
        }
        return SubscriptionManager.getResourcesForSubId(context, i).getString(android.R.string.day);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public List getBlurCellInfo(Context context, AppOpsManager appOpsManager, List list, String str, int i) {
        if (list == null || list.isEmpty() || isPreciseLocationMode(appOpsManager, str, i)) {
            return list;
        }
        SecurityManager securityManager = (SecurityManager) context.getSystemService("security");
        Rlog.d("MiuiPhoneApp", "MIUILOG- Blur location for CellInfo, caller" + str);
        return securityManager.getBlurryCellInfos(list);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public CellIdentity getBlurCellLocation(Context context, AppOpsManager appOpsManager, CellIdentity cellIdentity, String str, int i) {
        if (cellIdentity == null || isPreciseLocationMode(appOpsManager, str, i)) {
            return cellIdentity;
        }
        SecurityManager securityManager = (SecurityManager) context.getSystemService("security");
        Rlog.d("MiuiPhoneApp", "MIUILOG- Blur location for CellLocation, caller" + str);
        return securityManager.getBlurryCellLocation(cellIdentity);
    }

    private void localLog(String str, String str2) {
        StringBuilder sb;
        String str3 = "MiuiPhoneApp." + str;
        try {
            PhoneFactory.addLocalLog(str3, 30);
            sb = new StringBuilder("callingUid = ");
        } catch (IllegalArgumentException unused) {
            sb = new StringBuilder("callingUid = ");
        } finally {
            PhoneFactory.localLog(str3, "callingUid = " + Binder.getCallingUid() + " callingPid = " + Binder.getCallingPid() + str2);
        }
        sb.append(Binder.getCallingUid());
        sb.append(" callingPid = ");
        sb.append(Binder.getCallingPid());
        sb.append(str2);
        String string = sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5 */
    private boolean setAllPhonesDataEnabled(Context context, boolean z, String str) {
        boolean zCheckEntDataRestriction = PhoneHelper.checkEntDataRestriction(context, z);
        ?? IsCellularDataRestriction = zCheckEntDataRestriction;
        if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED) {
            IsCellularDataRestriction = PhoneHelperStub.getInstance().isCellularDataRestriction(zCheckEntDataRestriction);
        }
        localLog("setAllPhonesDataEnabled", "callingPackage = " + str + ", enable = " + ((boolean) IsCellularDataRestriction));
        Rlog.d("MiuiPhoneApp", "callingPackage = " + str + ", enable = " + ((boolean) IsCellularDataRestriction));
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < miui.telephony.TelephonyManager.getDefault().getPhoneCount(); i++) {
            try {
                PhoneFactory.getPhone(i).getDataSettingsManager().obtainMessage(5, 0, (int) IsCellularDataRestriction, str).sendToTarget();
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
        return true;
    }

    private boolean isPreciseLocationMode(AppOpsManager appOpsManager, String str, int i) {
        return Build.IS_INTERNATIONAL_BUILD || UserHandle.getAppId(i) < 10000 || appOpsManager.noteOpNoThrow(10036, i, str) == 0;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isLocalEmergencyNumber(Context context, String str, boolean z) {
        return TelephonyManagerEx.isLocalEmergencyNumber(context, str);
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean startVoicemailSettingsActivity(VoicemailSettingsActivity voicemailSettingsActivity) {
        if (!miui.telephony.TelephonyManager.isGoogleCsp()) {
            return false;
        }
        Intent intent = voicemailSettingsActivity.getIntent();
        int intExtra = intent.getIntExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", -1);
        if (intExtra != -1) {
            miui.telephony.SubscriptionManager.putSubscriptionIdExtra(intent, intExtra);
        }
        intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.VoicemailSetting");
        voicemailSettingsActivity.startActivity(intent);
        voicemailSettingsActivity.finish();
        return true;
    }

    public void autoReceiveMMSForDomesticVersion() {
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        if (Settings.System.getInt(contentResolver, "always_enable_mms", 1) != 1) {
            Settings.System.putInt(contentResolver, "always_enable_mms", 1);
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean isActivityInfoOptimizeSupported() {
        return ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isActivityInfoOptimizeSupported();
    }

    /* JADX WARN: Code duplicated, block: B:100:0x02db  */
    /* JADX WARN: Code duplicated, block: B:102:0x02fa  */
    /* JADX WARN: Code duplicated, block: B:105:0x0305  */
    /* JADX WARN: Code duplicated, block: B:108:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:38:0x00dc  */
    /* JADX WARN: Code duplicated, block: B:41:0x00e5  */
    /* JADX WARN: Code duplicated, block: B:47:0x0103  */
    /* JADX WARN: Code duplicated, block: B:52:0x011e  */
    /* JADX WARN: Code duplicated, block: B:55:0x0126  */
    /* JADX WARN: Code duplicated, block: B:57:0x012c  */
    /* JADX WARN: Code duplicated, block: B:63:0x014b A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:65:0x0189  */
    /* JADX WARN: Code duplicated, block: B:68:0x0190  */
    /* JADX WARN: Code duplicated, block: B:73:0x01ac  */
    /* JADX WARN: Code duplicated, block: B:74:0x01c9  */
    /* JADX WARN: Code duplicated, block: B:76:0x01d1  */
    /* JADX WARN: Code duplicated, block: B:78:0x01e0  */
    /* JADX WARN: Code duplicated, block: B:81:0x01fa  */
    /* JADX WARN: Code duplicated, block: B:83:0x0205  */
    /* JADX WARN: Code duplicated, block: B:85:0x0264 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:88:0x0273  */
    /* JADX WARN: Code duplicated, block: B:90:0x028a  */
    /* JADX WARN: Code duplicated, block: B:93:0x02b6  */
    /* JADX WARN: Code duplicated, block: B:95:0x02ba  */
    /* JADX WARN: Code duplicated, block: B:97:0x02c8  */
    /* JADX WARN: Instruction removed from duplicated block: B:73:0x01ac, please report this as an issue */
    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean displayMMIComplete(final Phone phone, Context context, MmiCode mmiCode, Message message, android.app.AlertDialog alertDialog, MiStubParam miStubParam) {
        MmiCode mmiCode2;
        CharSequence message2;
        int i;
        CharSequence charSequence;
        int i2;
        View viewInflate;
        DialogInterface.OnClickListener onClickListener;
        final AlertDialog alertDialogCreate;
        String str;
        int i3;
        CharSequence string;
        boolean zEquals;
        int i4;
        PowerManager powerManager;
        PersistableBundle carrierConfig;
        final PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        MmiCode.State state = mmiCode.getState();
        Rlog.d("MiuiPhoneApp", "displayMMIComplete: state=" + state);
        int i5 = AnonymousClass7.$SwitchMap$com$android$internal$telephony$MmiCode$State[state.ordinal()];
        String operatorNumeric = "";
        String string2 = null;
        if (i5 == 1) {
            mmiCode2 = mmiCode;
            message2 = mmiCode2.getMessage();
            StringBuilder sb = new StringBuilder("displayMMIComplete: using text from PENDING MMI message: '");
            sb.append(TelephonyUtils.pii(message2 == null ? "" : message2.toString()));
            sb.append("'");
            Rlog.d("MiuiPhoneApp", sb.toString());
        } else if (i5 != 2) {
            if (i5 == 3) {
                if (SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
                    carrierConfig = phoneGlobals.getCarrierConfigForSubId(phone.getSubId());
                } else {
                    carrierConfig = phoneGlobals.getCarrierConfig();
                }
                mmiCode2 = mmiCode;
                if (carrierConfig.getBoolean("use_caller_id_ussd_bool")) {
                    message2 = SuppServicesUiUtil.handleCallerIdUssdResponse(phoneGlobals, context, phone, mmiCode2);
                    if (mmiCode2.getMessage() == null || message2.equals(mmiCode2.getMessage())) {
                    }
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    if (phoneGlobals.getPUKEntryActivity() == null && state == MmiCode.State.COMPLETE) {
                        Rlog.d("MiuiPhoneApp", "displaying PUK unblocking progress dialog.");
                        setUssdDialog(phoneGlobals, i, null, message2, null, false, true, 2008, -1);
                        return true;
                    }
                    if (phoneGlobals.getPUKEntryActivity() == null && state == MmiCode.State.FAILED) {
                        createUssdDialog(phoneGlobals, context, message2, phone, 2009, miStubParam);
                        phoneGlobals.setPukEntryActivity(null);
                        return true;
                    }
                    charSequence = message2;
                    if (phoneGlobals.getPUKEntryActivity() != null) {
                        phoneGlobals.setPukEntryActivity(null);
                    }
                    i2 = -1;
                    if (state != MmiCode.State.PENDING) {
                        if (!MiuiImsPhoneUtils.isCustForEsVodafone() && state == MmiCode.State.FAILED) {
                            Rlog.d("MiuiPhoneApp", "Failed when USSD input timeout, dismiss input dialog and quit directly.");
                            dismissUssdDialog();
                            return true;
                        }
                        int callForwardTitleResId = getCallForwardTitleResId(mmiCode2.getDialString());
                        if (shouldShowCfTypeWhenMmi(mmiCode2.getPhone()) || callForwardTitleResId == -1) {
                            string = charSequence;
                        } else {
                            StringBuilder sb2 = new StringBuilder(charSequence);
                            int iIndexOf = sb2.indexOf("\n");
                            Rlog.d("MiuiPhoneApp", "displayMMIComplete: index=" + iIndexOf);
                            sb2.insert(iIndexOf + 1, phoneGlobals.getResources().getString(callForwardTitleResId) + "\n");
                            string = sb2.toString();
                        }
                        if (phone.getIccCard() != null && phone.getIccCard().getIccRecords() != null) {
                            operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
                        }
                        if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
                            zEquals = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "ussd_screen_on", false);
                            Rlog.d("MiuiPhoneApp", "isUssdScreenOn =  " + zEquals);
                        } else {
                            zEquals = "45403".equals(operatorNumeric);
                        }
                        if (zEquals) {
                            powerManager = (PowerManager) context.getSystemService("power");
                            if (!powerManager.isScreenOn()) {
                                PowerManager.WakeLock wakeLockNewWakeLock = powerManager.newWakeLock(268435462, "phone");
                                wakeLockNewWakeLock.acquire();
                                wakeLockNewWakeLock.release();
                            }
                            i4 = 2010;
                        } else {
                            i4 = 2003;
                        }
                        createUssdDialog(phoneGlobals, context, string, phone, i4, miStubParam);
                    } else {
                        Rlog.d("MiuiPhoneApp", "displayMMIComplete: USSD code has requested user input. Constructing input dialog.");
                        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                        viewInflate = ((LayoutInflater) contextThemeWrapper.getSystemService("layout_inflater")).inflate(R.layout.miui_dialog_ussd_response, (ViewGroup) null);
                        final EditText editText = (EditText) viewInflate.findViewById(R.id.input_field);
                        editText.setTextOperationUser(UserHandle.of(ActivityManager.getCurrentUser()));
                        final MmiCode mmiCode3 = mmiCode2;
                        onClickListener = new DialogInterface.OnClickListener(this) { // from class: com.android.phone.MiuiPhoneApp.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i6) {
                                if (i6 == -2) {
                                    if (mmiCode3.isCancelable()) {
                                        mmiCode3.cancel();
                                    }
                                } else {
                                    if (i6 != -1) {
                                        return;
                                    }
                                    if (editText.length() < 1 || editText.length() > 160) {
                                        PhoneGlobals phoneGlobals2 = phoneGlobals;
                                        Toast.makeText(phoneGlobals2, phoneGlobals2.getResources().getString(R.string.enter_input, 1, 160), 1).show();
                                        if (mmiCode3.isCancelable()) {
                                            mmiCode3.cancel();
                                            return;
                                        }
                                        return;
                                    }
                                    phone.sendUssdResponse(editText.getText().toString());
                                }
                            }
                        };
                        alertDialogCreate = new AlertDialog.Builder(contextThemeWrapper).setView(viewInflate).setPositiveButton(R.string.send_button, onClickListener).setNegativeButton(R.string.cancel, onClickListener).setCancelable(false).create();
                        if (isMultiSimMode() && phone != null) {
                            getPhoneAccount(phone.getSubId());
                            if (phone.getCarrierName() != null) {
                                string2 = phoneGlobals.getResources().getString(R.string.carrier_mmi_msg_title, phone.getCarrierName());
                                alertDialogCreate.setTitle(string2);
                            } else {
                                string2 = phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title);
                                alertDialogCreate.setTitle(string2);
                            }
                        }
                        str = string2;
                        editText.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.phone.MiuiPhoneApp.2
                            @Override // android.view.View.OnKeyListener
                            public boolean onKey(View view, int i6, KeyEvent keyEvent) {
                                if (i6 != 5 && i6 != 66) {
                                    return false;
                                }
                                if (keyEvent.getAction() != 0) {
                                    return true;
                                }
                                phone.sendUssdResponse(editText.getText().toString());
                                if (MiuiPhoneApp.this.mAd != null && MiuiPhoneApp.this.mAd.isShowing()) {
                                    MiuiPhoneApp.this.mAd.dismiss();
                                }
                                alertDialogCreate.dismiss();
                                MiuiPhoneApp.this.dismissUssdDialog();
                                return true;
                            }
                        });
                        editText.requestFocus();
                        ((TextView) viewInflate.findViewById(R.id.ussd_message)).setText(charSequence);
                        if (miui.telephony.TelephonyManager.isCustForKrOps()) {
                            i3 = -1;
                        } else {
                            i2 = R.string.carrier_info;
                            if (i2 > 0) {
                                alertDialogCreate.setTitle(i2);
                            }
                            alertDialogCreate.getWindow().addFlags(2097152);
                            i3 = 2097152;
                        }
                        alertDialogCreate.getWindow().setType(2008);
                        alertDialogCreate.getWindow().addFlags(2);
                        if (phone != null) {
                            setAlertDialog(context, viewInflate, null, i2, str, null, null, false, 2008, i3, R.string.send_button, R.string.cancel, onClickListener, onClickListener, phone.getSubId());
                            return true;
                        }
                        alertDialogCreate.show();
                    }
                    if (!mmiCode.isNetworkInitiatedUssd()) {
                        return r11;
                    }
                    playSound(context);
                    return 1;
                }
                if (phoneGlobals.getPUKEntryActivity() != null) {
                    message2 = context.getText(R.string.puk_unlocked);
                    i = android.R.string.SetupCallDefault;
                }
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                if (phoneGlobals.getPUKEntryActivity() == null) {
                }
                if (phoneGlobals.getPUKEntryActivity() == null) {
                }
                charSequence = message2;
                if (phoneGlobals.getPUKEntryActivity() != null) {
                    phoneGlobals.setPukEntryActivity(null);
                }
                i2 = -1;
                if (state != MmiCode.State.PENDING) {
                    if (!MiuiImsPhoneUtils.isCustForEsVodafone()) {
                    }
                    int callForwardTitleResId2 = getCallForwardTitleResId(mmiCode2.getDialString());
                    if (shouldShowCfTypeWhenMmi(mmiCode2.getPhone())) {
                        string = charSequence;
                    } else {
                        string = charSequence;
                    }
                    if (phone.getIccCard() != null) {
                        operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
                    }
                    if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
                        zEquals = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "ussd_screen_on", false);
                        Rlog.d("MiuiPhoneApp", "isUssdScreenOn =  " + zEquals);
                    } else {
                        zEquals = "45403".equals(operatorNumeric);
                    }
                    if (zEquals) {
                        powerManager = (PowerManager) context.getSystemService("power");
                        if (!powerManager.isScreenOn()) {
                            PowerManager.WakeLock wakeLockNewWakeLock2 = powerManager.newWakeLock(268435462, "phone");
                            wakeLockNewWakeLock2.acquire();
                            wakeLockNewWakeLock2.release();
                        }
                        i4 = 2010;
                    } else {
                        i4 = 2003;
                    }
                    createUssdDialog(phoneGlobals, context, string, phone, i4, miStubParam);
                } else {
                    Rlog.d("MiuiPhoneApp", "displayMMIComplete: USSD code has requested user input. Constructing input dialog.");
                    ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
                    viewInflate = ((LayoutInflater) contextThemeWrapper2.getSystemService("layout_inflater")).inflate(R.layout.miui_dialog_ussd_response, (ViewGroup) null);
                    final EditText editText2 = (EditText) viewInflate.findViewById(R.id.input_field);
                    editText2.setTextOperationUser(UserHandle.of(ActivityManager.getCurrentUser()));
                    final MmiCode mmiCode4 = mmiCode2;
                    onClickListener = new DialogInterface.OnClickListener(this) { // from class: com.android.phone.MiuiPhoneApp.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i6) {
                            if (i6 == -2) {
                                if (mmiCode4.isCancelable()) {
                                    mmiCode4.cancel();
                                }
                            } else {
                                if (i6 != -1) {
                                    return;
                                }
                                if (editText2.length() < 1 || editText2.length() > 160) {
                                    PhoneGlobals phoneGlobals2 = phoneGlobals;
                                    Toast.makeText(phoneGlobals2, phoneGlobals2.getResources().getString(R.string.enter_input, 1, 160), 1).show();
                                    if (mmiCode4.isCancelable()) {
                                        mmiCode4.cancel();
                                        return;
                                    }
                                    return;
                                }
                                phone.sendUssdResponse(editText2.getText().toString());
                            }
                        }
                    };
                    alertDialogCreate = new AlertDialog.Builder(contextThemeWrapper2).setView(viewInflate).setPositiveButton(R.string.send_button, onClickListener).setNegativeButton(R.string.cancel, onClickListener).setCancelable(false).create();
                    if (isMultiSimMode()) {
                        getPhoneAccount(phone.getSubId());
                        if (phone.getCarrierName() != null) {
                            string2 = phoneGlobals.getResources().getString(R.string.carrier_mmi_msg_title, phone.getCarrierName());
                            alertDialogCreate.setTitle(string2);
                        } else {
                            string2 = phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title);
                            alertDialogCreate.setTitle(string2);
                        }
                    }
                    str = string2;
                    editText2.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.phone.MiuiPhoneApp.2
                        @Override // android.view.View.OnKeyListener
                        public boolean onKey(View view, int i6, KeyEvent keyEvent) {
                            if (i6 != 5 && i6 != 66) {
                                return false;
                            }
                            if (keyEvent.getAction() != 0) {
                                return true;
                            }
                            phone.sendUssdResponse(editText2.getText().toString());
                            if (MiuiPhoneApp.this.mAd != null && MiuiPhoneApp.this.mAd.isShowing()) {
                                MiuiPhoneApp.this.mAd.dismiss();
                            }
                            alertDialogCreate.dismiss();
                            MiuiPhoneApp.this.dismissUssdDialog();
                            return true;
                        }
                    });
                    editText2.requestFocus();
                    ((TextView) viewInflate.findViewById(R.id.ussd_message)).setText(charSequence);
                    if (miui.telephony.TelephonyManager.isCustForKrOps()) {
                        i2 = R.string.carrier_info;
                        if (i2 > 0) {
                            alertDialogCreate.setTitle(i2);
                        }
                        alertDialogCreate.getWindow().addFlags(2097152);
                        i3 = 2097152;
                    } else {
                        i3 = -1;
                    }
                    alertDialogCreate.getWindow().setType(2008);
                    alertDialogCreate.getWindow().addFlags(2);
                    if (phone != null) {
                        setAlertDialog(context, viewInflate, null, i2, str, null, null, false, 2008, i3, R.string.send_button, R.string.cancel, onClickListener, onClickListener, phone.getSubId());
                        return true;
                    }
                    alertDialogCreate.show();
                }
                if (!mmiCode.isNetworkInitiatedUssd()) {
                    return r11;
                }
                playSound(context);
                return 1;
            }
            if (i5 != 4) {
                OrtUtil$$ExternalSyntheticBUOutline2.m("Unexpected MmiCode state: ", state);
                return false;
            }
            mmiCode2 = mmiCode;
            message2 = mmiCode2.getMessage();
            StringBuilder sb3 = new StringBuilder("displayMMIComplete (failed): using text from MMI message: '");
            sb3.append(TelephonyUtils.pii(message2 == null ? "" : message2.toString()));
            sb3.append("'");
            Rlog.d("MiuiPhoneApp", sb3.toString());
        } else {
            mmiCode2 = mmiCode;
            message2 = null;
        }
        i = 0;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (phoneGlobals.getPUKEntryActivity() == null) {
        }
        if (phoneGlobals.getPUKEntryActivity() == null) {
        }
        charSequence = message2;
        if (phoneGlobals.getPUKEntryActivity() != null) {
            phoneGlobals.setPukEntryActivity(null);
        }
        i2 = -1;
        if (state != MmiCode.State.PENDING) {
            if (!MiuiImsPhoneUtils.isCustForEsVodafone()) {
            }
            int callForwardTitleResId3 = getCallForwardTitleResId(mmiCode2.getDialString());
            if (shouldShowCfTypeWhenMmi(mmiCode2.getPhone())) {
                string = charSequence;
            } else {
                string = charSequence;
            }
            if (phone.getIccCard() != null) {
                operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
            }
            if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
                zEquals = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "ussd_screen_on", false);
                Rlog.d("MiuiPhoneApp", "isUssdScreenOn =  " + zEquals);
            } else {
                zEquals = "45403".equals(operatorNumeric);
            }
            if (zEquals) {
                powerManager = (PowerManager) context.getSystemService("power");
                if (!powerManager.isScreenOn()) {
                    PowerManager.WakeLock wakeLockNewWakeLock3 = powerManager.newWakeLock(268435462, "phone");
                    wakeLockNewWakeLock3.acquire();
                    wakeLockNewWakeLock3.release();
                }
                i4 = 2010;
            } else {
                i4 = 2003;
            }
            createUssdDialog(phoneGlobals, context, string, phone, i4, miStubParam);
        } else {
            Rlog.d("MiuiPhoneApp", "displayMMIComplete: USSD code has requested user input. Constructing input dialog.");
            ContextThemeWrapper contextThemeWrapper3 = new ContextThemeWrapper(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
            viewInflate = ((LayoutInflater) contextThemeWrapper3.getSystemService("layout_inflater")).inflate(R.layout.miui_dialog_ussd_response, (ViewGroup) null);
            final EditText editText3 = (EditText) viewInflate.findViewById(R.id.input_field);
            editText3.setTextOperationUser(UserHandle.of(ActivityManager.getCurrentUser()));
            final MmiCode mmiCode5 = mmiCode2;
            onClickListener = new DialogInterface.OnClickListener(this) { // from class: com.android.phone.MiuiPhoneApp.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i6) {
                    if (i6 == -2) {
                        if (mmiCode5.isCancelable()) {
                            mmiCode5.cancel();
                        }
                    } else {
                        if (i6 != -1) {
                            return;
                        }
                        if (editText3.length() < 1 || editText3.length() > 160) {
                            PhoneGlobals phoneGlobals2 = phoneGlobals;
                            Toast.makeText(phoneGlobals2, phoneGlobals2.getResources().getString(R.string.enter_input, 1, 160), 1).show();
                            if (mmiCode5.isCancelable()) {
                                mmiCode5.cancel();
                                return;
                            }
                            return;
                        }
                        phone.sendUssdResponse(editText3.getText().toString());
                    }
                }
            };
            alertDialogCreate = new AlertDialog.Builder(contextThemeWrapper3).setView(viewInflate).setPositiveButton(R.string.send_button, onClickListener).setNegativeButton(R.string.cancel, onClickListener).setCancelable(false).create();
            if (isMultiSimMode()) {
                getPhoneAccount(phone.getSubId());
                if (phone.getCarrierName() != null) {
                    string2 = phoneGlobals.getResources().getString(R.string.carrier_mmi_msg_title, phone.getCarrierName());
                    alertDialogCreate.setTitle(string2);
                } else {
                    string2 = phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title);
                    alertDialogCreate.setTitle(string2);
                }
            }
            str = string2;
            editText3.setOnKeyListener(new View.OnKeyListener() { // from class: com.android.phone.MiuiPhoneApp.2
                @Override // android.view.View.OnKeyListener
                public boolean onKey(View view, int i6, KeyEvent keyEvent) {
                    if (i6 != 5 && i6 != 66) {
                        return false;
                    }
                    if (keyEvent.getAction() != 0) {
                        return true;
                    }
                    phone.sendUssdResponse(editText3.getText().toString());
                    if (MiuiPhoneApp.this.mAd != null && MiuiPhoneApp.this.mAd.isShowing()) {
                        MiuiPhoneApp.this.mAd.dismiss();
                    }
                    alertDialogCreate.dismiss();
                    MiuiPhoneApp.this.dismissUssdDialog();
                    return true;
                }
            });
            editText3.requestFocus();
            ((TextView) viewInflate.findViewById(R.id.ussd_message)).setText(charSequence);
            if (miui.telephony.TelephonyManager.isCustForKrOps()) {
                i2 = R.string.carrier_info;
                if (i2 > 0) {
                    alertDialogCreate.setTitle(i2);
                }
                alertDialogCreate.getWindow().addFlags(2097152);
                i3 = 2097152;
            } else {
                i3 = -1;
            }
            alertDialogCreate.getWindow().setType(2008);
            alertDialogCreate.getWindow().addFlags(2);
            if (phone != null) {
                setAlertDialog(context, viewInflate, null, i2, str, null, null, false, 2008, i3, R.string.send_button, R.string.cancel, onClickListener, onClickListener, phone.getSubId());
                return true;
            }
            alertDialogCreate.show();
        }
        if (!mmiCode.isNetworkInitiatedUssd()) {
            return r11;
        }
        playSound(context);
        return 1;
    }

    /* JADX INFO: renamed from: com.android.phone.MiuiPhoneApp$7, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass7 {
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

    private static boolean shouldShowCfTypeWhenMmi(Phone phone) {
        PersistableBundle carrierConfigForSubId;
        if (phone == null || (carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId())) == null) {
            return false;
        }
        return carrierConfigForSubId.getBoolean("config_show_cf_type_when_mmi", false);
    }

    private static void playSound(Context context) {
        int ringerMode = ((AudioManager) context.getSystemService("audio")).getRingerMode();
        if (ringerMode != 2) {
            if (ringerMode == 1) {
                Rlog.d("MiuiPhoneApp", "playSound : RINGER_MODE_VIBRATE");
                ((Vibrator) context.getSystemService("vibrator")).vibrate(VibrationEffect.createWaveform(getLongArray(context.getResources(), R.array.config_defaultNotificationVibePattern, DEFAULT_VIBRATE_PATTERN), -1), new AudioAttributes.Builder().setUsage(5).build());
                return;
            }
            return;
        }
        Rlog.d("MiuiPhoneApp", "playSound : RINGER_MODE_NORMAL");
        MediaPlayer mediaPlayer = null;
        try {
            Uri defaultUri = RingtoneManager.getDefaultUri(2);
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            try {
                mediaPlayer2.setDataSource(context, defaultUri);
                mediaPlayer2.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(5).setUsage(5).build());
                mediaPlayer2.setLooping(false);
                mediaPlayer2.prepareAsync();
                mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.android.phone.MiuiPhoneApp.3
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mediaPlayer3) {
                        mediaPlayer3.start();
                        Rlog.d("MiuiPhoneApp", "playSound : onPrepared");
                    }
                });
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.android.phone.MiuiPhoneApp.4
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mediaPlayer3) {
                        mediaPlayer3.release();
                        Rlog.d("MiuiPhoneApp", "playSound : onCompletion");
                    }
                });
                mediaPlayer2.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.android.phone.MiuiPhoneApp$$ExternalSyntheticLambda0
                    @Override // android.media.MediaPlayer.OnErrorListener
                    public final boolean onError(MediaPlayer mediaPlayer3, int i, int i2) {
                        return MiuiPhoneApp.$r8$lambda$gLk4b5ZHcmAzsfOPhx7IpNe_X6U(mediaPlayer3, i, i2);
                    }
                });
            } catch (IOException e) {
                e = e;
                mediaPlayer = mediaPlayer2;
                Rlog.d("MiuiPhoneApp", "playSound exception : " + e);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    public static /* synthetic */ boolean $r8$lambda$gLk4b5ZHcmAzsfOPhx7IpNe_X6U(MediaPlayer mediaPlayer, int i, int i2) {
        Rlog.e("MiuiPhoneApp", "playSound error: what=" + i + " extra=" + i2);
        mediaPlayer.release();
        return true;
    }

    private static boolean isMultiSimMode() {
        return getTelephonyManager() != null && getTelephonyManager().getActiveModemCount() > 1;
    }

    private static TelephonyManager getTelephonyManager() {
        if (sTelephonyManager == null) {
            sTelephonyManager = (TelephonyManager) PhoneGlobals.getInstance().getSystemService(TelephonyManager.class);
        }
        return sTelephonyManager;
    }

    public void setUssdDialog(Context context, int i, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, boolean z, boolean z2, int i2, int i3) {
        this.mPd = (ProgressDialog) showMiuiUSSDDialog(context, i, charSequence, charSequence2, drawable, z, z2, i2, i3);
    }

    private static PhoneAccount getPhoneAccount(int i) {
        PhoneAccountHandle phoneAccountHandleForSubscriptionId = getTelephonyManager() != null ? getTelephonyManager().getPhoneAccountHandleForSubscriptionId(i) : null;
        if (getTelecomManager() == null || phoneAccountHandleForSubscriptionId == null) {
            return null;
        }
        return getTelecomManager().getPhoneAccount(phoneAccountHandleForSubscriptionId);
    }

    private static TelecomManager getTelecomManager() {
        if (sTelecomManager == null) {
            sTelecomManager = (TelecomManager) PhoneGlobals.getInstance().getSystemService(TelecomManager.class);
        }
        return sTelecomManager;
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

    public Dialog showMiuiUSSDDialog(Context context, int i, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, boolean z, boolean z2, int i2, int i3) {
        int slotIndex;
        if (context == null) {
            return null;
        }
        ProgressDialog progressDialog = new ProgressDialog(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
        if (charSequence != null) {
            progressDialog.setTitle(charSequence);
        }
        if (i != -1) {
            progressDialog.setTitle(i);
        }
        if (drawable != null) {
            progressDialog.setIcon(drawable);
        } else if ((MiuiPhoneUtils.isMultiSimEnabled() || !miui.telephony.TelephonyManager.isCustSingleSimDevice()) && (slotIndex = SubscriptionManager.getSlotIndex(i3)) != -1) {
            int i4 = MiuiPhoneUtils.isVirtualSim(slotIndex) ? R.drawable.mi_big_sim_icon : BIG_SIM_SLOT_ICON[slotIndex];
            View viewInflate = View.inflate(this.mContext, R.layout.miui_dialog_cust_title, null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.title);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.icon);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            textView.setPaddingRelative(this.mContext.getResources().getDimensionPixelSize(miuix.appcompat.R$dimen.miuix_appcompat_dialog_progress_horizontal_margin), 0, 0, 0);
            if (charSequence != null) {
                textView.setText(charSequence);
            }
            if (i != -1) {
                textView.setText(i);
            }
            imageView.setImageResource(i4);
            progressDialog.setCustomTitle(viewInflate);
        }
        progressDialog.setMessage(charSequence2);
        progressDialog.setCancelable(z);
        progressDialog.setIndeterminate(z2);
        progressDialog.getWindow().addFlags(2);
        if (i2 != -1) {
            progressDialog.getWindow().setType(i2);
        }
        progressDialog.getWindow().setGravity(80);
        progressDialog.show();
        return progressDialog;
    }

    public void setAlertDialog(Context context, View view, StringBuilder sb, int i, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, boolean z, int i2, int i3, int i4, int i5, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, int i6) {
        this.mAd = (AlertDialog) showMiuiAlertDialog(context, view, sb, i, charSequence, charSequence2, drawable, z, i2, i3, i4, i5, onClickListener, onClickListener2, i6);
    }

    public Dialog showMiuiAlertDialog(Context context, View view, final StringBuilder sb, int i, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, boolean z, int i2, int i3, int i4, int i5, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, int i6) {
        int slotIndex;
        if (context == null) {
            return null;
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setPositiveButton(i4, onClickListener).setCancelable(z).create();
        alertDialogCreate.getWindow().addFlags(2);
        if (sb != null) {
            alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.phone.MiuiPhoneApp.5
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    MiuiPhoneApp.this.mAd = null;
                    sb.setLength(0);
                }
            });
        }
        if (charSequence2 != null) {
            alertDialogCreate.setMessage(charSequence2);
        }
        if (charSequence != null) {
            alertDialogCreate.setTitle(charSequence);
        }
        if (i != -1) {
            alertDialogCreate.setTitle(i);
        }
        if (view != null) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.appcompat_dialog_pannel_horizontal_margin);
            view.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
            alertDialogCreate.setView(view);
        }
        if (drawable != null) {
            alertDialogCreate.setIcon(drawable);
        } else if ((MiuiPhoneUtils.isMultiSimEnabled() || !miui.telephony.TelephonyManager.isCustSingleSimDevice()) && (slotIndex = SubscriptionManager.getSlotIndex(i6)) != -1) {
            int i7 = MiuiPhoneUtils.isVirtualSim(slotIndex) ? R.drawable.mi_big_sim_icon : BIG_SIM_SLOT_ICON[slotIndex];
            View viewInflate = View.inflate(this.mContext, R.layout.miui_dialog_cust_title, null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.title);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.icon);
            if (charSequence != null) {
                textView.setText(charSequence);
            }
            if (i != -1) {
                textView.setText(i);
            }
            imageView.setImageResource(i7);
            alertDialogCreate.setCustomTitle(viewInflate);
        }
        if (i5 != -1) {
            alertDialogCreate.setButton(-2, this.mContext.getText(i5), onClickListener2);
        }
        if (i2 != -1) {
            alertDialogCreate.getWindow().setType(i2);
        }
        if (i3 != -1) {
            alertDialogCreate.getWindow().addFlags(i3);
        }
        alertDialogCreate.getWindow().setGravity(80);
        alertDialogCreate.show();
        WindowInsetsController insetsController = alertDialogCreate.getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.setSystemBarsAppearance(8, 8);
        }
        return alertDialogCreate;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean displayMMIInitiate(Phone phone, Context context, MmiCode mmiCode, Message message, Dialog dialog, MiStubParam miStubParam) {
        Rlog.d("MiuiPhoneApp", "displayMMIInitiate: " + Rlog.pii("MiuiPhoneApp", mmiCode.toString()));
        if (dialog != null) {
            dialog.dismiss();
        }
        if (!mmiCode.isCancelable()) {
            Rlog.d("MiuiPhoneApp", "displayMMIInitiate: not a USSD code, displaying status toast.");
            Toast.makeText(context, context.getText(R.string.mmiStarted), 0).show();
            return true;
        }
        Rlog.d("MiuiPhoneApp", "displayMMIInitiate: running USSD code, displaying intermediate progress.");
        if (phone != null) {
            miStubParam.obj1 = showMiuiUSSDDialog(context, -1, context.getText(R.string.ussdinitiated_title), context.getText(R.string.ussdRunning), null, false, true, -1, phone.getSubId());
        }
        return true;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public boolean createUssdDialog(PhoneGlobals phoneGlobals, Context context, CharSequence charSequence, Phone phone, int i, MiStubParam miStubParam) {
        CharSequence charSequenceSubstring;
        boolean zEquals;
        String string;
        int i2;
        StringBuilder sb = new StringBuilder("displayMMIComplete: Extended NW displayMMIInitiate (");
        sb.append(TelephonyUtils.pii(charSequence == null ? "" : charSequence.toString()));
        sb.append(")");
        Rlog.d("MiuiPhoneApp", sb.toString());
        if (charSequence == null || charSequence.length() == 0) {
            return true;
        }
        boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "remove_error_string_on_ussd_dialog", false);
        if (charSequence.toString().contains("![CDATA[") && booleanForPhoneId) {
            charSequenceSubstring = charSequence.toString().substring(charSequence.toString().indexOf("![CDATA[") + 8);
        } else {
            charSequenceSubstring = charSequence;
        }
        if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
            zEquals = HyperOSCustFeatureResolve.getBoolean("ignore_mmi_msg", false);
            Rlog.d("MiuiPhoneApp", "ignoreMmiMsg =  " + zEquals);
        } else {
            zEquals = "mx_telcel".equals(CUSTOMIZED_REGION);
        }
        if (zEquals) {
            CharSequence text = context.getText(android.R.string.permdesc_readPhoneNumbers);
            CharSequence text2 = context.getText(android.R.string.permdesc_readPhoneState);
            if (TextUtils.equals(charSequenceSubstring, text) || TextUtils.equals(charSequenceSubstring, text2)) {
                Rlog.d("MiuiPhoneApp", "Ignore error and complete mmicode message for Telcel.");
                return true;
            }
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context, THEME).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setCancelable(true).setOnDismissListener(new DialogInterface.OnDismissListener(this) { // from class: com.android.phone.MiuiPhoneApp.6
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                MiuiPhoneApp.sUssdMsg.setLength(0);
            }
        }).create();
        alertDialogCreate.getWindow().setType(i);
        alertDialogCreate.getWindow().addFlags(2);
        if (sUssdMsg.length() != 0) {
            sUssdMsg.insert(0, "\n").insert(0, phoneGlobals.getResources().getString(R.string.ussd_dialog_sep)).insert(0, "\n");
        }
        if (phone.getCarrierName() != null) {
            string = phoneGlobals.getResources().getString(R.string.carrier_mmi_msg_title, phone.getCarrierName());
            alertDialogCreate.setTitle(string);
        } else {
            string = phoneGlobals.getResources().getString(R.string.default_carrier_mmi_msg_title);
            alertDialogCreate.setTitle(string);
        }
        sUssdMsg.insert(0, charSequenceSubstring);
        alertDialogCreate.setMessage(sUssdMsg.toString());
        if (miui.telephony.TelephonyManager.isCustForKrOps()) {
            i2 = -1;
        } else {
            i2 = R.string.carrier_info;
            if (i2 > 0) {
                alertDialogCreate.setTitle(i2);
            }
        }
        StringBuilder sb2 = sUssdMsg;
        showMiuiAlertDialog(context, null, sb2, i2, string, sb2.toString(), null, true, i, -1, R.string.ok, -1, null, null, phone.getSubId());
        return true;
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void dismissUssdDialog() {
        ProgressDialog progressDialog = this.mPd;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mPd = null;
        }
    }

    @Override // com.android.phone.MiPhoneAppStub.IMiPhoneApp
    public void updateDataRoamingStatus(Phone phone, String str, int i) {
        List internetDataDisallowedReasons = phone.getDataNetworkController().getInternetDataDisallowedReasons();
        boolean zIsEmpty = internetDataDisallowedReasons.isEmpty();
        boolean z = internetDataDisallowedReasons.size() == 1 && internetDataDisallowedReasons.contains(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
        Rlog.d("MiuiPhoneApp", "dataAllowed=" + zIsEmpty + ", reasons=" + internetDataDisallowedReasons + ", roamingOperatorNumeric=" + str);
        if (internetDataDisallowedReasons.contains(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED) && internetDataDisallowedReasons.contains(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE) && MiTelephonyServiceStub.get().isSupportDomesticRoaming() && dataIsNowRoaming(i)) {
            zIsEmpty = false;
            z = true;
        }
        if (!zIsEmpty && z) {
            if (this.mPrevRoamingNotification == 2) {
                return;
            }
            this.mPrevRoamingNotification = 2;
            Rlog.d("MiuiPhoneApp", "Show roaming disconnected notification");
            showDataRoamingNotification(i, false);
            return;
        }
        if (zIsEmpty && dataIsNowRoaming(i)) {
            PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(i);
            if ((carrierConfigForSubId == null || carrierConfigForSubId.getBoolean("show_data_connected_roaming_notification")) && this.mPrevRoamingNotification != 1) {
                this.mPrevRoamingNotification = 1;
                Rlog.d("MiuiPhoneApp", "Show roaming connected notification");
                showDataRoamingNotification(i, true);
                return;
            }
            return;
        }
        if (this.mPrevRoamingNotification != 0) {
            this.mPrevRoamingNotification = 0;
            Rlog.d("MiuiPhoneApp", "Dismiss roaming notification");
            hideDataRoamingNotification();
        }
    }

    public void showDataRoamingNotification(int i, boolean z) {
        int i2;
        int i3;
        StringBuilder sb = new StringBuilder("showDataRoamingNotification() roaming ");
        sb.append(z ? SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON : SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_OFF);
        sb.append(" on subId ");
        sb.append(i);
        Rlog.d("MiuiPhoneApp", sb.toString());
        if (MiTelephonyServiceStub.get().isNoNotificationOnDomesticRoaming() && MiTelephonyServiceStub.get().isDomesticRoamingEnable(this.mContext)) {
            return;
        }
        Intent intent = new Intent();
        try {
            if (miui.telephony.TelephonyManager.isCustForKrOps()) {
                intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MiuiDataRoamingSettings");
            } else {
                intent.setClassName("com.miui.securitycenter", "com.miui.networkassistant.ui.activity.InternationalRoamingSettingActivity");
            }
        } catch (Exception e) {
            Rlog.d("MiuiPhoneApp", "Exception: " + e);
        }
        intent.putExtra("android.provider.extra.SUB_ID", i);
        PendingIntent activity = PendingIntent.getActivity(this.mContext, i, intent, 67108864);
        Context context = this.mContext;
        if (z) {
            i2 = R.string.roaming_on_notification_title;
        } else {
            i2 = R.string.roaming;
        }
        CharSequence text = context.getText(i2);
        Context context2 = this.mContext;
        if (z) {
            i3 = R.string.roaming_enabled_message;
        } else {
            i3 = R.string.roaming_reenable_message;
        }
        CharSequence text2 = context2.getText(i3);
        notifyAsUser(null, 5, new Notification.BigTextStyle(new Notification.Builder(this.mContext, "mobileRoamingDataAlertNew").setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(text).setColor(this.mContext.getResources().getColor(com.android.phone.common.R$color.dialer_theme_color)).setContentText(text2).setChannelId("mobileRoamingDataAlertNew").setContentIntent(activity).setAutoCancel(true)).bigText(text2).build(), UserHandle.ALL);
    }

    void hideDataRoamingNotification() {
        Rlog.d("MiuiPhoneApp", "hideDataRoamingNotification()...");
        cancelAsUser(null, 5, UserHandle.ALL);
    }

    private void notifyAsUser(String str, int i, Notification notification, UserHandle userHandle) {
        try {
            Context context = this.mContext;
            NotificationManager notificationManager = (NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService("notification");
            notificationManager.createNotificationChannel(new NotificationChannel("mobileRoamingDataAlertNew", this.mContext.getText(R.string.mobile_data_settings_title), 4));
            notificationManager.notify(str, i, notification);
        } catch (PackageManager.NameNotFoundException e) {
            Rlog.e("MiuiPhoneApp", "unable to notify for user " + userHandle);
            e.printStackTrace();
        }
    }

    private void cancelAsUser(String str, int i, UserHandle userHandle) {
        try {
            Context context = this.mContext;
            ((NotificationManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService("notification")).cancel(str, i);
        } catch (PackageManager.NameNotFoundException e) {
            Rlog.e("MiuiPhoneApp", "unable to cancel for user " + userHandle);
            e.printStackTrace();
        }
    }

    private boolean dataIsNowRoaming(int i) {
        Phone phone = getPhone(i);
        ServiceState serviceState = phone != null ? phone.getServiceState() : null;
        return serviceState != null && serviceState.getDataRoaming();
    }

    public static Phone getPhone(int i) {
        return PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
    }
}
