package com.android.phone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.view.Display;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MiuiCallControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.ims.MiuiImsRegControllerBase;
import com.android.phone.ims.MiuiImsRegControllerImpl;
import com.android.phone.module.IMiConnectModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.roaming.CallCapabilityManager;
import com.android.phone.roaming.RoamingCFManager;
import com.android.phone.roaming.RoamingManager;
import com.android.phone.roaming.RoamingParametersManager;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.phone.statistics.FiveGAStatistics;
import com.android.phone.statistics.MiuiNetworkInfoStats;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.ModemUtils;
import com.android.phone.utils.NrSceneConstants$Scene;
import com.android.phone.utils.Utils;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.util.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import com.xiaomi.internal.telephony.ModemSarController;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiHeDuoHaoUtil;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miui.util.ITouchFeature;
import miuix.appcompat.app.AlertDialog;
import xiaomi.hypercomm.HyperCommManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneReceiver extends BroadcastReceiver {
    private static String TAG = "PhoneReceiver";
    private static MiuiPhoneReceiver sInstance;
    private Context mContext;
    private boolean[] mDisableWfcByAP;
    private final DisplayManager.DisplayListener mDisplayListener;
    private MiuiPhoneReceiverHandler mHandler;
    private boolean mIsNeedCloseSatellite;
    public String mLastCountryIso = TelephonyManager.getDefault().getNetworkCountryIso();
    private boolean[] mMiSimActivateDialogShowed;
    private boolean mMiSimActivateDialogShowedAgain;
    private MiuiImsRegControllerBase[] mMiuiImsRegistrationControllers;
    private boolean mNeedRegistedProvisionCompleteReceiver;
    private final NotificationManager mNotificationManager;
    private ServiceState[] mServiceState;
    private TeleCallBack mTeleCallBack;
    private int mTouchMode;
    private boolean[] mVolteRegState;
    private boolean[] mVowifiRegState;
    private WifiCallHelper[] mWifiCallHelper;

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enableTouchModeSwitch() {
        return false;
    }

    public static void register(Context context) {
        if (sInstance == null) {
            sInstance = new MiuiPhoneReceiver(context);
        }
    }

    public static MiuiPhoneReceiver getInstance() {
        return sInstance;
    }

    private MiuiPhoneReceiver(Context context) {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mVowifiRegState = new boolean[i];
        this.mVolteRegState = new boolean[i];
        this.mDisableWfcByAP = new boolean[i];
        this.mTouchMode = -1;
        this.mNeedRegistedProvisionCompleteReceiver = true;
        this.mIsNeedCloseSatellite = false;
        this.mMiSimActivateDialogShowedAgain = false;
        this.mMiSimActivateDialogShowed = new boolean[i];
        this.mWifiCallHelper = new WifiCallHelper[i];
        this.mMiuiImsRegistrationControllers = new MiuiImsRegControllerBase[i];
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.phone.MiuiPhoneReceiver.2
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i2) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i2) {
                if (i2 == 0) {
                    Display display = ((DisplayManager) MiuiPhoneReceiver.this.mContext.getSystemService("display")).getDisplay(0);
                    if (display.getState() == 2 && MiuiPhoneReceiver.this.enableTouchModeSwitch()) {
                        MiuiPhoneReceiver miuiPhoneReceiver = MiuiPhoneReceiver.this;
                        boolean zIsGsmRatExist = miuiPhoneReceiver.isGsmRatExist(miuiPhoneReceiver.mServiceState);
                        Rlog.d(MiuiPhoneReceiver.TAG, "Screen on, set touch mode to " + (zIsGsmRatExist ? 1 : 0));
                        MiuiPhoneReceiver.this.mHandler.sendMessage(MiuiPhoneReceiver.this.mHandler.obtainMessage(1, zIsGsmRatExist ? 1 : 0, 0));
                    }
                    MiuiNetworkInfoStats.getInstance().onDisplayChanged(display);
                }
            }
        };
        this.mContext = context;
        this.mHandler = new MiuiPhoneReceiverHandler(BackgroundThread.get().getLooper());
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mServiceState[i2] = new ServiceState();
        }
        initMiuiImsRegisterState(context);
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        if (ImsAdapterBase.getInstance().isVolteSupportedByDevice(context) && MiuiPhoneUtils.isVolteRegionFeatureEnabled()) {
            intentFilter.addAction("miui.intent.action.MIUI_REGION_CHANGED");
            checkSystemUpdate();
        }
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_IMS_REGISTED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("miui.intent.action.FIRST_DATA_USAGE");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("miui.intent.action.ACTION_SA_ENABLE_OR_DISABLE");
        intentFilter.addAction("com.android.phone.intent.action.SATELLITE_DSI_CHANGED");
        intentFilter.addAction("com.android.satellite.action.NTNGPS");
        if (Build.IS_DEBUGGABLE) {
            intentFilter.addAction("miui.intent.action.ACTION_SILENT_DISABLE_SA");
            intentFilter.addAction("miui.intent.action.ACTION_SILENT_DISABLE_ENDC");
            intentFilter.addAction("miui.intent.action.ACTION_DEBUG_CDRX_CONFIG");
            intentFilter.addAction("miui.intent.action.ACTION_DEBUG_NR_SCENE");
        }
        boolean z = Build.IS_INTERNATIONAL_BUILD;
        if (z) {
            intentFilter.addAction("miui.intent.action.OMACP_VOWIFI_ENABLED");
        }
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            intentFilter.addAction(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        }
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            intentFilter.addAction("com.android.phone.intent.action.ENTER_DEPOPULATED_ZONE");
        }
        intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        intentFilter.addAction("android.location.MODE_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEBUG_ROAMING_NOTIFICATION");
        intentFilter.addAction("com.android.updater.action.COTA_CARRIER");
        context.registerReceiver(this, intentFilter, 2);
        ((DisplayManager) this.mContext.getSystemService("display")).registerDisplayListener(this.mDisplayListener, null);
        Arrays.fill(this.mMiSimActivateDialogShowed, false);
        if (!z) {
            registerMiShowDbChangeObserver();
        }
        registerTelephonyInfoCallBack();
    }

    private void registerMiShowDbChangeObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("disable_security_by_mishow"), true, new ContentObserver(this.mHandler) { // from class: com.android.phone.MiuiPhoneReceiver.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (Settings.Secure.getInt(MiuiPhoneReceiver.this.mContext.getContentResolver(), "disable_security_by_mishow", 0) == 1 || Settings.System.getInt(MiuiPhoneReceiver.this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
                    MiuiPhoneReceiver.setRadioPower(false);
                    Rlog.d(MiuiPhoneReceiver.TAG, "MiShowDB change,setRadioPower false");
                } else {
                    MiuiPhoneReceiver.setRadioPower(true);
                    Rlog.d(MiuiPhoneReceiver.TAG, "MiShowDB change,setRadioPower true");
                }
            }
        });
    }

    static final void setRadioPower(boolean z) {
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.mCi.setRadioPower(z, (Message) null);
        }
    }

    public class MiuiPhoneReceiverHandler extends Handler {
        public MiuiPhoneReceiverHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiPhoneReceiver miuiPhoneReceiver;
            int i = message.what;
            if (i == 1) {
                int i2 = message.arg1;
                try {
                    ITouchFeature iTouchFeature = ITouchFeature.getInstance();
                    if (iTouchFeature != null) {
                        iTouchFeature.setTouchMode(12, i2);
                        MiuiPhoneReceiver.this.mTouchMode = i2;
                        Rlog.d(MiuiPhoneReceiver.TAG, "setTouchMode, value= " + i2);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    Rlog.d(MiuiPhoneReceiver.TAG, "setTouchMode, value= " + i2 + " Exception:" + e);
                    return;
                }
            }
            if (i == 2) {
                Rlog.d(MiuiPhoneReceiver.TAG, "service state changed");
                ServiceState serviceState = (ServiceState) message.obj;
                int i3 = message.arg1;
                if (RoamingManager.getInstance() != null) {
                    RoamingManager.getInstance().processServiceState(i3, serviceState);
                }
                if (RoamingUtils.isRoamingOptimizationSupported(MiuiPhoneReceiver.this.mContext)) {
                    CallCapabilityManager.getInstance().processServiceState(i3, serviceState);
                    RoamingCFManager.getInstance().processServiceState(i3, serviceState);
                }
                MiuiNetworkInfoStats.getInstance().processServiceState(i3, serviceState);
                MiuiPhoneReceiver.this.showEsimRoamingDialogIfNeed(i3, serviceState);
                return;
            }
            if (i == 3) {
                Rlog.d(MiuiPhoneReceiver.TAG, "signal strength changed");
                MiuiNetworkInfoStats.getInstance().processSignalStrength(message.arg1, (SignalStrength) message.obj);
                return;
            }
            if (i == 20) {
                MiuiPhoneReceiver.this.mMiSimActivateDialogShowedAgain = true;
                int i4 = 0;
                while (true) {
                    int i5 = MiuiPhoneUtils.PHONE_COUNT;
                    miuiPhoneReceiver = MiuiPhoneReceiver.this;
                    if (i4 >= i5) {
                        break;
                    }
                    miuiPhoneReceiver.mMiSimActivateDialogShowed[i4] = false;
                    MiuiPhoneReceiver.this.showMiSimActivateDialogIfNeed(i4, null);
                    i4++;
                }
                if (miuiPhoneReceiver.mHandler.hasMessages(30)) {
                    return;
                }
                MiuiPhoneReceiver.this.mHandler.sendEmptyMessageDelayed(30, 3600000L);
                return;
            }
            if (i != 30) {
                if (i != 147) {
                    return;
                }
                ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getSarMode();
                Rlog.d(MiuiPhoneReceiver.TAG, "getSarMode");
                return;
            }
            for (int i6 = 0; i6 < MiuiPhoneUtils.PHONE_COUNT; i6++) {
                String imsi = MiuiPhoneUtils.getImsi(i6);
                if (!TextUtils.isEmpty(imsi) && DefaultSimManager.getDefaultDataSlotId() != i6 && MiuiPhoneReceiver.this.isNeedShowActivateDialog(imsi)) {
                    MiuiPhoneReceiver.this.enterMiSimNotActiveTips(i6);
                }
            }
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Rlog.d(TAG, "Received action:" + action);
        boolean z = true;
        int i = 0;
        if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
            int slotIdExtra = SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID);
            String stringExtra = intent.getStringExtra("ss");
            Rlog.d(TAG, "ACTION_SIM_STATE_CHANGED ,simState = " + stringExtra + " ; slotId = " + slotIdExtra);
            onIccStatusChanged(context, intent);
            if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() && ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled()) {
                Rlog.d(TAG, "satellite sim absent  mIsNeedCloseSatellite = " + this.mIsNeedCloseSatellite + ", simState = " + stringExtra);
                if (TelephonyCommonUtils.isNtnChipVendor_54() || ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isHardSimSwapForTT()) {
                    if (("READY".equals(stringExtra) || "LOCKED".equals(stringExtra)) && !this.mIsNeedCloseSatellite) {
                        this.mIsNeedCloseSatellite = true;
                        return;
                    } else {
                        if ("ABSENT".equals(stringExtra) && this.mIsNeedCloseSatellite) {
                            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).closeSatellite(0, slotIdExtra);
                            this.mIsNeedCloseSatellite = false;
                            return;
                        }
                        return;
                    }
                }
                if ("ABSENT".equals(stringExtra)) {
                    ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).closeSatellite(0, slotIdExtra);
                    return;
                }
                return;
            }
            if ("LOADED".equals(stringExtra) && MiuiPhoneUtils.isDeO2Sim(PhoneFactory.getPhone(slotIdExtra))) {
                if (this.mNeedRegistedProvisionCompleteReceiver && !MiuiPhoneUtils.isDevicesProvisioned(context)) {
                    handleRegistProvisionCompleteReceiver();
                    return;
                } else {
                    if (MiuiPhoneUtils.isDevicesProvisioned(context) && ImsAdapterBase.getInstance().isWfcEnabledByUser(this.mContext, slotIdExtra)) {
                        showDeO2VowifiNotifyDialog(context);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if ("miui.intent.action.MIUI_REGION_CHANGED".equals(action)) {
            String stringExtra2 = intent.getStringExtra(a.d);
            Rlog.d(TAG, "Region change to " + stringExtra2);
            updateVolteConfig(stringExtra2, false);
            return;
        }
        if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
            if (MiuiPhoneUtils.isFiveGCapable()) {
                FiveGManagerBase.getInstance().onCarrierConfigChanged(intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1), intent.getIntExtra(SubscriptionManager.SUBSCRIPTION_KEY, SubscriptionManager.INVALID_SUBSCRIPTION_ID));
            }
            int intExtra = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
            if (SubscriptionManager.isRealSlotId(intExtra)) {
                if (MiuiImsPhoneUtils.getPrefBoolean("show_wifi_calling_dialog", true) && MiuiImsPhoneUtils.shouldShowWifiCallingDialogOrNotification(MiuiPhoneUtils.getPhone(intExtra))) {
                    if (this.mNeedRegistedProvisionCompleteReceiver && !MiuiPhoneUtils.isDevicesProvisioned(context)) {
                        handleRegistProvisionCompleteReceiver();
                    } else if (MiuiPhoneUtils.isDevicesProvisioned(context)) {
                        if (MiuiImsPhoneUtils.shouldShowWifiCallingDialog(MiuiPhoneUtils.getPhone(intExtra))) {
                            showWifiCallingDialog(context, intExtra);
                        } else if (MiuiImsPhoneUtils.shouldShowWifiCallingNotification(MiuiPhoneUtils.getPhone(intExtra))) {
                            showWifiCallingNotification(context, intExtra);
                        }
                    }
                }
                if (MiuiPhoneUtils.isVolteEnabledByPlatform(MiuiPhoneUtils.getPhone(intExtra))) {
                    MiuiPhoneUtils.sendEnhanced4GLteModeChangeBroadcast(intExtra, -1);
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).trigerNoCallServiceDialogChecker(false);
                }
                int intExtra2 = intent.getIntExtra(SubscriptionManagerEx.SUBSCRIPTION_KEY, -1);
                Rlog.d(TAG, "ACTION_CARRIER_CONFIG_CHANGED phoneId:" + intExtra + " subId:" + intExtra2);
                if (SubscriptionManager.isValidSubscriptionId(intExtra2)) {
                    PhoneGlobals.getInstance().refreshMwiIndicator(intExtra2);
                    this.mDisableWfcByAP[intExtra] = getDisableWfcByApStateToSp(intExtra2);
                    handleWifiApStateChanged(TelephonyManagerEx.getDefault().getWifiApState(), intExtra);
                }
                boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(this.mContext, intExtra2, "support_ses_provision", false);
                boolean booleanConfigForSubId2 = CarrierConfigManagerEx.getBooleanConfigForSubId(this.mContext, intExtra2, "rtt_supported_bool", false);
                if (MiuiPhoneUtils.isSendATcommand()) {
                    if (booleanConfigForSubId2) {
                        ATCommandUtils.getInstance().atCmdRunSync(intExtra, new String[]{"AT+ECFGSET=\"tty_enable\",\"1\"", "+ECFGSET"}, null);
                        ATCommandUtils.getInstance().atCmdRunSync(intExtra, new String[]{"AT+ECFGSET=\"text_feature_tag\",\"1\"", "+ECFGSET"}, null);
                    } else {
                        ATCommandUtils.getInstance().atCmdRunSync(intExtra, new String[]{"AT+ECFGSET=\"tty_enable\",\"0\"", "+ECFGSET"}, null);
                        ATCommandUtils.getInstance().atCmdRunSync(intExtra, new String[]{"AT+ECFGSET=\"text_feature_tag\",\"0\"", "+ECFGSET"}, null);
                    }
                }
                if (MiuiPhoneUtils.isUkO2Sim(PhoneFactory.getPhone(intExtra)) && booleanConfigForSubId) {
                    Intent intent2 = new Intent("miui.intent.action.ACTION_O2_SES_PROVISION");
                    intent2.setPackage("com.xiaomi.entitlement.o2");
                    intent2.putExtras(intent);
                    context.sendBroadcast(intent2);
                }
                boolean booleanConfigForSubId3 = CarrierConfigManagerEx.getBooleanConfigForSubId(context, intExtra2, "config_wifi_disconnect_delay_for_vowifi_detach", false);
                if (ModemProvider.IS_QCOM && ((TelephonyManager.isCustForHkH3g() || booleanConfigForSubId3) && Settings.Secure.getInt(context.getContentResolver(), "wifi_disconnect_delay_duration", 0) == 0)) {
                    Settings.Secure.putInt(context.getContentResolver(), "wifi_disconnect_delay_duration", 3);
                }
                if (MiuiPhoneUtils.isFastDormancySupported()) {
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).setFastDormancyConfig(context, intExtra);
                }
                MiuiImsRegControllerBase miuiImsRegControllerBase = this.mMiuiImsRegistrationControllers[intExtra];
                if (miuiImsRegControllerBase != null) {
                    miuiImsRegControllerBase.updateImsCarrierConfig();
                }
                if (MiuiPhoneUtils.is5gSaSupported()) {
                    boolean zIsWifiCallHelperSupported = MiuiPhoneUtils.isWifiCallHelperSupported(MiuiPhoneUtils.getPhone(intExtra));
                    WifiCallHelper[] wifiCallHelperArr = this.mWifiCallHelper;
                    if (zIsWifiCallHelperSupported) {
                        if (wifiCallHelperArr[intExtra] == null) {
                            wifiCallHelperArr[intExtra] = new WifiCallHelper(PhoneGlobals.getInstance().getApplicationContext(), intExtra);
                        }
                        this.mWifiCallHelper[intExtra].init();
                    } else {
                        WifiCallHelper wifiCallHelper = wifiCallHelperArr[intExtra];
                        if (wifiCallHelper != null) {
                            wifiCallHelper.dispose();
                        }
                    }
                }
                int intConfigForSubId = CarrierConfigManagerEx.getIntConfigForSubId(context, intExtra2, "config_set_volte_roaming_state", -1);
                if (intConfigForSubId != -1) {
                    setVoLTERoamingStatue(intExtra, intConfigForSubId);
                    return;
                }
                return;
            }
            return;
        }
        if ("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED".equals(action)) {
            int intExtra3 = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
            if (SubscriptionManager.isRealSlotId(intExtra3)) {
                Rlog.d(TAG, "ACTION_DEFAULT_DATA_SLOT_CHANGED phoneId:" + intExtra3);
                if (!MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED && MiuiPhoneUtils.isVolteEnabledByPlatform(MiuiPhoneUtils.getPhone(intExtra3))) {
                    MiuiPhoneUtils.sendEnhanced4GLteModeChangeBroadcast(intExtra3, -1);
                }
                if (MiuiPhoneUtils.isFastDormancySupported() && (TelephonyManager.isCustForHkH3g() || TelephonyManager.isCustForEsTelefonica())) {
                    ((IModemModule) ModuleDelegate.get(IModemModule.class)).setFastDormancyConfig(context, intExtra3);
                }
                openDual4GIfNeed("LOADED");
                if (TelephonyManager.isCustForMxAt()) {
                    setOperatorPropDependIccid(intExtra3);
                    return;
                }
                return;
            }
            return;
        }
        if ("miui.intent.action.OMACP_VOWIFI_ENABLED".equals(action)) {
            int i2 = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
            int intExtra4 = intent.getIntExtra("subscription", i2);
            if (intExtra4 == i2) {
                Rlog.d(TAG, "ACTION_OMACP_VOWIFI_ENABLED sub is invalid return!!!");
                return;
            }
            boolean booleanExtra = intent.getBooleanExtra("vowifi_enabled", false);
            MiuiImsPhoneUtils.saveOmacpWifiState(intExtra4, booleanExtra);
            Rlog.d(TAG, "ACTION_OMACP_VOWIFI_ENABLED subId:" + intExtra4 + " enabled:" + booleanExtra);
            return;
        }
        if ("android.intent.action.ACTION_IMS_REGISTED".equals(action)) {
            int intExtra5 = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
            if (SubscriptionManager.isRealSlotId(intExtra5)) {
                MiuiImsPhoneUtils.sendWifiEmergencyCallNotification(intExtra5);
                this.mVolteRegState[intExtra5] = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                this.mVowifiRegState[intExtra5] = intent.getBooleanExtra("wfc_state", false);
                MiuiImsRegControllerBase miuiImsRegControllerBase2 = this.mMiuiImsRegistrationControllers[intExtra5];
                if (miuiImsRegControllerBase2 != null) {
                    miuiImsRegControllerBase2.receiveIMSRegistedStateBroadcast(this.mVolteRegState[intExtra5], this.mVowifiRegState[intExtra5]);
                }
                if (this.mVolteRegState[intExtra5]) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).trigerNoCallServiceDialogChecker(true);
                    return;
                }
                return;
            }
            return;
        }
        if ("android.provision.action.PROVISION_COMPLETE".equals(action)) {
            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
            while (i < phoneCount) {
                if (!showMiSimActivateDialogIfNeed(i, null)) {
                    Phone phone = MiuiPhoneUtils.getPhone(i);
                    Phone phone2 = MiuiPhoneUtils.getPhone(i + 1);
                    if (MiuiImsPhoneUtils.shouldShowWifiCallingDialogOrNotification(phone)) {
                        if (MiuiImsPhoneUtils.shouldShowWifiCallingDialogOrNotification(phone2)) {
                            phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
                        }
                        if (MiuiImsPhoneUtils.shouldShowWifiCallingDialog(phone)) {
                            showWifiCallingDialogDelayed(context, phone.getPhoneId());
                            return;
                        } else {
                            if (MiuiImsPhoneUtils.shouldShowWifiCallingNotification(phone)) {
                                showWifiCallingNotification(context, phone.getPhoneId());
                                return;
                            }
                            return;
                        }
                    }
                    if (MiuiPhoneUtils.isDeO2Sim(phone) && ImsAdapterBase.getInstance().isWfcEnabledByUser(this.mContext, i)) {
                        showDeO2VowifiNotifyDelay(context);
                        return;
                    }
                }
                i++;
            }
            return;
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            Rlog.d(TAG, "ACTION_BOOT_COMPLETED");
            MiuiPhoneUtils.setHealthStatusInDb(context, 1);
            if (Build.IS_DEMO_BUILD) {
                this.mNotificationManager.notifyAsUser(null, 1, new Notification.Builder(context).setSmallIcon(android.R.drawable.stat_sys_warning).setContentTitle(context.getString(R.string.notification_demo_version_title)).setContentText(context.getString(R.string.notification_demo_version_text)).setShowWhen(false).setOngoing(true).setChannel("demo_Version").build(), UserHandle.ALL);
            }
            Rlog.d(TAG, "device boot complete, resetShowDialogWhenIllegalME.");
            MiuiPhoneUtils.resetShowDialogWhenIllegalME();
            return;
        }
        if ("android.intent.action.SERVICE_STATE".equals(action)) {
            int intExtra6 = intent.getIntExtra("slot", 0);
            Bundle extras = intent.getExtras();
            ServiceState serviceStateNewFromBundle = extras != null ? ServiceState.newFromBundle(extras) : null;
            if (serviceStateNewFromBundle != null) {
                MiuiPhoneUtils.switchCellBroadCastEnabled(this.mContext, serviceStateNewFromBundle);
            }
            if (SubscriptionManager.isRealSlotId(intExtra6)) {
                MiuiImsRegControllerBase miuiImsRegControllerBase3 = this.mMiuiImsRegistrationControllers[intExtra6];
                if (miuiImsRegControllerBase3 != null) {
                    miuiImsRegControllerBase3.updateServiceState(false);
                }
                if (enableTouchModeSwitch()) {
                    boolean zIsGsmRatExist = isGsmRatExist(this.mServiceState);
                    ServiceState[] serviceStateArr = this.mServiceState;
                    serviceStateArr[intExtra6] = serviceStateNewFromBundle;
                    boolean zIsGsmRatExist2 = isGsmRatExist(serviceStateArr);
                    Rlog.d(TAG, "isGsmExist= " + zIsGsmRatExist + ", isGsmExistNow=" + zIsGsmRatExist2);
                    if (zIsGsmRatExist != zIsGsmRatExist2) {
                        MiuiPhoneReceiverHandler miuiPhoneReceiverHandler = this.mHandler;
                        miuiPhoneReceiverHandler.sendMessage(miuiPhoneReceiverHandler.obtainMessage(1, zIsGsmRatExist2 ? 1 : 0, 0));
                    }
                }
                if (MiuiKoreaOperatorCustimizedUtils.isCustForKrOps()) {
                    int csRejectCause = PhoneProxy.getCsRejectCause(serviceStateNewFromBundle);
                    Rlog.e(TAG, "slotId= " + intExtra6 + " mCsRejectCause=" + csRejectCause);
                    MiuiEmmRejectCauseManager.getInstance().showRejectCauseDialogIfNeeded(intExtra6, csRejectCause);
                    if (MiuiKoreaOperatorCustimizedUtils.isCustForKrLgu()) {
                        switchNetworkTypeInRoaming(intExtra6);
                    }
                }
                this.mServiceState[intExtra6] = serviceStateNewFromBundle;
                return;
            }
            return;
        }
        if ("miui.intent.action.ACTION_SILENT_DISABLE_SA".equals(action)) {
            int intExtra7 = intent.getIntExtra("sa_state", -1);
            if (intExtra7 == -1) {
                Rlog.e(TAG, "invalid extra.");
                return;
            }
            if (FiveGUtils.isXmRadioSupported()) {
                NrSceneConstants$Scene nrSceneConstants$Scene = NrSceneConstants$Scene.DEPRI_SA_TEST;
                int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$Scene);
                final String strValueOf = String.valueOf(nrSceneConstants$Scene.getValue());
                HyperCommManager.getDefault().requestNrOperation(DefaultSimManager.getDefaultDataSlotId(), strValueOf, typeByScene, intExtra7 != 1, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.MiuiPhoneReceiver.3
                    @Override // android.os.ResultReceiver
                    protected void onReceiveResult(int i3, Bundle bundle) {
                        Rlog.d(MiuiPhoneReceiver.TAG, "requestNrOperation result=" + i3 + "; featureId=" + strValueOf + "; slotId=" + DefaultSimManager.getDefaultDataSlotId());
                    }
                });
                return;
            }
            if (FiveGPowerController.getInstance() != null) {
                Rlog.d(TAG, "silent disable sa, sa_state=" + intExtra7);
                FiveGPowerController.getInstance().enableSaAsync(intExtra7 == 1, NrSceneConstants$Scene.DEPRI_SA_TEST);
                return;
            }
            return;
        }
        if ("miui.intent.action.ACTION_SA_ENABLE_OR_DISABLE".equals(action)) {
            int intExtra8 = intent.getIntExtra("sa_state", -1);
            int intExtra9 = intent.getIntExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, -1);
            if (intExtra8 == -1) {
                Rlog.e(TAG, "invalid extra.");
                return;
            }
            if (FiveGUtils.isXmRadioSupported()) {
                NrSceneConstants$Scene nrSceneConstants$Scene2 = NrSceneConstants$Scene.SA_SMS;
                int typeByScene2 = FiveGUtils.getTypeByScene(nrSceneConstants$Scene2);
                final String strValueOf2 = String.valueOf(nrSceneConstants$Scene2.getValue());
                HyperCommManager.getDefault().requestNrOperation(DefaultSimManager.getDefaultDataSlotId(), strValueOf2, typeByScene2, intExtra8 != 1, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.MiuiPhoneReceiver.4
                    @Override // android.os.ResultReceiver
                    protected void onReceiveResult(int i3, Bundle bundle) {
                        Rlog.d(MiuiPhoneReceiver.TAG, "requestNrOperation result=" + i3 + "; featureId=" + strValueOf2 + "; slotId=" + DefaultSimManager.getDefaultDataSlotId());
                    }
                });
                return;
            }
            if (FiveGPowerController.getInstance() != null) {
                FiveGPowerController.getInstance().enableSaAsync(intExtra8 == 1, intExtra9, NrSceneConstants$Scene.SA_SMS);
                return;
            }
            return;
        }
        if ("miui.intent.action.ACTION_SILENT_DISABLE_ENDC".equals(action)) {
            int intExtra10 = intent.getIntExtra("endc_state", -1);
            if (intExtra10 == -1) {
                Rlog.e(TAG, "invalid extra.");
                return;
            }
            Rlog.d(TAG, "silent disable endc, endcState=" + intExtra10);
            try {
                if (FiveGUtils.isXmRadioSupported()) {
                    NrSceneConstants$Scene nrSceneConstants$Scene3 = NrSceneConstants$Scene.ENDC_TEST;
                    int typeByScene3 = FiveGUtils.getTypeByScene(nrSceneConstants$Scene3);
                    final String strValueOf3 = String.valueOf(nrSceneConstants$Scene3.getValue());
                    HyperCommManager.getDefault().requestNrOperation(DefaultSimManager.getDefaultDataSlotId(), strValueOf3, typeByScene3, intExtra10 != 1, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.MiuiPhoneReceiver.5
                        @Override // android.os.ResultReceiver
                        protected void onReceiveResult(int i3, Bundle bundle) {
                            Rlog.d(MiuiPhoneReceiver.TAG, "requestNrOperation result=" + i3 + "; featureId=" + strValueOf3 + "; slotId=" + DefaultSimManager.getDefaultDataSlotId());
                        }
                    });
                } else if (FiveGPowerController.getInstance() != null) {
                    FiveGPowerController fiveGPowerController = FiveGPowerController.getInstance();
                    if (intExtra10 != 1) {
                        z = false;
                    }
                    fiveGPowerController.bindServiceAndEnableEndc(z);
                }
                return;
            } catch (Exception e) {
                Rlog.d(TAG, "silent disable endc: Exception = " + e);
                return;
            }
        }
        if ("miui.intent.action.ACTION_DEBUG_CDRX_CONFIG".equals(action)) {
            int intExtra11 = intent.getIntExtra("cdrx_type", -1);
            if (intExtra11 == -1) {
                Rlog.e(TAG, "invalid extra.");
                return;
            }
            Intent intent3 = new Intent("qualcomm.intent.action.ACTION_NR5G_DRX_CONFIG_CHANGED");
            if (intExtra11 == 1) {
                Rlog.d(TAG, "send debug enable cdrx config");
                intent3.putExtra("BROADCAST_CONTEXT", IccUtils.hexStringToBytes("010000005000000001000000400100000000000000000000"));
            } else {
                Rlog.d(TAG, "send debug empty cdrx config");
                intent3.putExtra("BROADCAST_CONTEXT", IccUtils.hexStringToBytes("000000000000000000000000000000000000000000000000"));
            }
            this.mContext.sendBroadcast(intent3);
            return;
        }
        if ("miui.intent.action.FIRST_DATA_USAGE".equals(action)) {
            Rlog.d(TAG, "popupDialog: show dialog data usage for first time");
            AlertDialog.Builder builder = new AlertDialog.Builder(context, miuix.appcompat.R$style.Theme_Light_Dialog_Alert);
            builder.setCancelable(false);
            builder.setMessage(286196135);
            builder.setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null);
            AlertDialog alertDialogCreate = builder.create();
            alertDialogCreate.getWindow().setType(2008);
            alertDialogCreate.show();
            Settings.Global.putInt(context.getContentResolver(), "device_first_using_data", 1);
            return;
        }
        if ("android.intent.action.PHONE_STATE".equals(action)) {
            String stringExtra3 = intent.getStringExtra(MEModuleSignalT1s.EXTRA_STATE);
            int intExtra12 = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", SubscriptionManager.INVALID_PHONE_ID);
            Rlog.d(TAG, "ACTION_PHONE_STATE_CHANGED phoneid: " + intExtra12 + " state: " + stringExtra3);
            if (TextUtils.isEmpty(stringExtra3) || !stringExtra3.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                return;
            }
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                MiuiCallControllerImpl.Processor.clearSpeedHDIconForPhoneId();
                i++;
            }
            if (Utils.isBCLStrategyEnable()) {
                FlashManager.getInstance().unregisterListener();
            }
            ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).responsePhoneStateIdle(context);
            return;
        }
        if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
            boolean z2 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
            if (MiuiPhoneUtils.isARDOptimizationSupported() && !MiuiPhoneUtils.isARDOptimizationDisabledByCloud() && z2 && FiveGPowerController.getInstance() != null) {
                FiveGPowerController.getInstance().resetARDOptimizationToDefault();
            }
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                MiuiImsRegControllerBase miuiImsRegControllerBase4 = this.mMiuiImsRegistrationControllers[i];
                if (miuiImsRegControllerBase4 != null) {
                    miuiImsRegControllerBase4.updateServiceState(true);
                }
                i++;
            }
            MiuiNetworkInfoStats.getInstance().onAirplaneModeChanged(z2);
            return;
        }
        if ("com.android.phone.intent.action.SATELLITE_DSI_CHANGED".equals(action)) {
            int intExtra13 = intent.getIntExtra(ModemSarController.BC_INTENT_EXTRA_DSI_VALUE, -1);
            Rlog.d(TAG, "receive ACTION_DSI_VALUE_CHANGED_STATE: " + intExtra13);
            if (Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "satellite_state", 0) == 0 || ModuleDelegate.get(ISatelliteModule.class) == null) {
                return;
            }
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSarMode(intExtra13);
            MiuiPhoneReceiverHandler miuiPhoneReceiverHandler2 = this.mHandler;
            miuiPhoneReceiverHandler2.sendMessageDelayed(miuiPhoneReceiverHandler2.obtainMessage(147), 1000L);
            return;
        }
        if (SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(action)) {
            boolean booleanExtra2 = intent.getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
            Rlog.d(TAG, "receive ACTION_SATELLITE_STATE_CHANGE,isNtnEnabled: " + booleanExtra2);
            if (booleanExtra2) {
                ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).updateCurrentLocation("gps");
                return;
            } else {
                ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).updateCurrentLocation("network");
                this.mIsNeedCloseSatellite = false;
                return;
            }
        }
        if ("com.android.phone.intent.action.ENTER_DEPOPULATED_ZONE".equals(action)) {
            Rlog.d(TAG, "Received ACTION_ENTER_DEPOPULATED_ZONE_NOTIFY!");
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).updateCurrentLocation("network");
            return;
        }
        if ("com.android.satellite.action.NTNGPS".equals(action)) {
            double doubleExtra = intent.getDoubleExtra("longitude", 0.0d);
            double doubleExtra2 = intent.getDoubleExtra("latitude", 0.0d);
            double doubleExtra3 = intent.getDoubleExtra("altitude", 0.0d);
            if (doubleExtra == 0.0d && doubleExtra2 == 0.0d) {
                return;
            }
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).receiveNtnGps(doubleExtra, doubleExtra2, doubleExtra3);
            return;
        }
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
            handleWifiApStateChanged(intent.getIntExtra("wifi_state", 0), SubscriptionManager.INVALID_PHONE_ID);
            return;
        }
        if ("android.location.MODE_CHANGED".equals(action)) {
            handleLocaltionSwitchChanged(this.mContext);
            return;
        }
        if ("miui.intent.action.ACTION_DEBUG_ROAMING_NOTIFICATION".equals(action)) {
            RoamingCFManager.getInstance().setDebug(intent.getBooleanExtra("debug", false));
            return;
        }
        if ("com.android.updater.action.COTA_CARRIER".equals(action)) {
            if (TelephonyManager.isCustForMxAt()) {
                setOperatorPropDependIccid(DefaultSimManager.getDefaultDataSlotId());
            }
        } else if ("miui.intent.action.ACTION_DEBUG_NR_SCENE".equals(action)) {
            handleDebugNrScene(intent);
        }
    }

    private void handleDebugNrScene(Intent intent) {
        String stringExtra = intent.getStringExtra("scene_name");
        if (TextUtils.isEmpty(stringExtra)) {
            Rlog.e(TAG, "DEBUG_NR_SCENE: missing scene_name, available: " + Arrays.toString(NrSceneConstants$Scene.values()));
            return;
        }
        try {
            final NrSceneConstants$Scene nrSceneConstants$SceneValueOf = NrSceneConstants$Scene.valueOf(stringExtra.toUpperCase());
            boolean booleanExtra = intent.getBooleanExtra("depri_nr", true);
            final int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, DefaultSimManager.getDefaultDataSlotId());
            int typeByScene = FiveGUtils.getTypeByScene(nrSceneConstants$SceneValueOf);
            String strValueOf = String.valueOf(nrSceneConstants$SceneValueOf.getValue());
            String str = TAG;
            StringBuilder sb = new StringBuilder("DEBUG_NR_SCENE: scene=");
            sb.append(nrSceneConstants$SceneValueOf);
            sb.append(", featureId=");
            sb.append(strValueOf);
            sb.append(", featureType=");
            sb.append(typeByScene == 1 ? "SA" : "NSA");
            sb.append(", depriNr=");
            sb.append(booleanExtra);
            sb.append(", slotId=");
            sb.append(intExtra);
            Rlog.d(str, sb.toString());
            if (FiveGUtils.isXmRadioSupported()) {
                HyperCommManager.getDefault().requestNrOperation(intExtra, strValueOf, typeByScene, booleanExtra, new ResultReceiver(this, this.mHandler) { // from class: com.android.phone.MiuiPhoneReceiver.6
                    @Override // android.os.ResultReceiver
                    protected void onReceiveResult(int i, Bundle bundle) {
                        Rlog.d(MiuiPhoneReceiver.TAG, "DEBUG_NR_SCENE: requestNrOperation result=" + i + "; scene=" + nrSceneConstants$SceneValueOf + "; slotId=" + intExtra);
                    }
                });
            } else {
                Rlog.e(TAG, "DEBUG_NR_SCENE: XmRadio not supported, cannot execute");
            }
        } catch (IllegalArgumentException unused) {
            Rlog.e(TAG, "DEBUG_NR_SCENE: unknown scene '" + stringExtra + "', available: " + Arrays.toString(NrSceneConstants$Scene.values()));
        }
    }

    private void handleWifiApStateChanged(int i, int i2) {
        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
            Phone phone = PhoneFactory.getPhone(i3);
            if (phone != null && SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
                int subId = phone.getSubId();
                boolean zDisableWfcWhenEnableWifiAp = MiuiImsPhoneUtils.disableWfcWhenEnableWifiAp(subId);
                if (!zDisableWfcWhenEnableWifiAp || (i2 != SubscriptionManager.INVALID_PHONE_ID && i3 != i2)) {
                    Rlog.i(TAG, "handleWifiApStateChanged continue + " + i3);
                } else {
                    Rlog.d(TAG, "handleWifiApStateChanged: wifiApState:" + i + " phoneId: " + i2);
                    PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(phone.getSubId());
                    if (zDisableWfcWhenEnableWifiAp && configForSubId != null) {
                        boolean zIsWfcEnabledByPlatform = ImsAdapterBase.getInstance().isWfcEnabledByPlatform(this.mContext, i3);
                        boolean z = configForSubId.getBoolean("carrier_wfc_ims_available_bool");
                        Rlog.i(TAG, "handleWifiApStateChanged wfcEnabledByPlatform = " + zIsWfcEnabledByPlatform + " wfcCarrierConfig = " + z);
                        if ((i == 13 && zIsWfcEnabledByPlatform) || (i == 11 && !z)) {
                            PhoneProxy.updateConfigForPhoneId(this.mContext, i3, "LOADED");
                        }
                    }
                    boolean zIsWfcEnabledByUser = ImsAdapterBase.getInstance().isWfcEnabledByUser(this.mContext, i3);
                    if (i == 13 && !this.mDisableWfcByAP[i3] && zIsWfcEnabledByUser) {
                        Rlog.d(TAG, "Received WIFI_AP_STATE_CHANGED_ACTION hotspot on, disable slot" + i3 + " vowifi");
                        ImsAdapterBase.getInstance().setWfcSetting(this.mContext, i3, false);
                        this.mDisableWfcByAP[i3] = true;
                        setDisableWfcByApStateToSp(subId, true);
                    } else if (i == 11 && this.mDisableWfcByAP[i3] && !zIsWfcEnabledByUser) {
                        Rlog.d(TAG, "Received WIFI_AP_STATE_CHANGED_ACTION hotspot off, enable slot" + i3 + " vowifi");
                        ImsAdapterBase.getInstance().setWfcSetting(this.mContext, i3, true);
                        this.mDisableWfcByAP[i3] = false;
                        setDisableWfcByApStateToSp(subId, false);
                    }
                }
            }
        }
    }

    private void clearDisableWfcByApState(int i) {
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            this.mDisableWfcByAP[i] = false;
        }
    }

    private void setDisableWfcByApStateToSp(int i, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putBoolean("key_disble_wfc_when_wifAP_enabled_" + i, z);
        editorEdit.apply();
    }

    private boolean getDisableWfcByApStateToSp(int i) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("key_disble_wfc_when_wifAP_enabled_" + i, false);
    }

    private void handleLocaltionSwitchChanged(Context context) {
        if (context == null) {
            return;
        }
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (locationManager != null && !locationManager.isLocationEnabled()) {
                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                    Phone phone = PhoneFactory.getPhone(i);
                    if (phone != null) {
                        int subId = phone.getSubId();
                        if (android.telephony.SubscriptionManager.from(context).isActiveSubId(subId) && !ImsAdapterBase.getInstance().hideWfc(context, subId) && ImsAdapterBase.getInstance().isWfcEnabledByUser(context, i) && CarrierConfigManagerEx.getBooleanConfigForSubId(context, subId, "config_disable_wfc_when_disable_location_switch", false)) {
                            Rlog.i(TAG, "handleLocaltionSwitchChanged, disable wfc, phone " + i);
                            ImsAdapterBase.getInstance().setWfcSetting(context, i, false);
                        }
                    }
                }
            }
        } catch (IllegalStateException e) {
            Rlog.e(TAG, "handleLocaltionSwitchChanged," + e);
        } catch (Exception e2) {
            Rlog.e(TAG, "handleLocaltionSwitchChanged," + e2);
        }
    }

    private void setOperatorPropDependIccid(int i) {
        String simSerialNumberForSlot;
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        final boolean z = PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("key_att_ringtone_done", false);
        Rlog.d(TAG, "setOperatorPropDependIccid: isAttRingRoneDone:" + z);
        if (defaultDataSlotId != i) {
            Rlog.d(TAG, "setOperatorPropDependIccid: dataSlotId:" + defaultDataSlotId + "; phoneId:" + i);
            return;
        }
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        Rlog.d(TAG, "setOperatorPropDependIccid: simOperator:" + simOperatorForSlot);
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return;
        }
        if (!TextUtils.isEmpty(simOperatorForSlot) && (("334050".equals(simOperatorForSlot) || "334090".equals(simOperatorForSlot) || "334070".equals(simOperatorForSlot)) && (simSerialNumberForSlot = TelephonyManager.getDefault().getSimSerialNumberForSlot(i)) != null && simSerialNumberForSlot.length() >= 9)) {
            String strSubstring = simSerialNumberForSlot.substring(8, 9);
            Rlog.d(TAG, "setOperatorPropDependIccid: iccid9Pos:".concat(strSubstring));
            if (!"1".equals(strSubstring) && !"3".equals(strSubstring)) {
                SystemProperties.set("persist.radio.op.name", "AT");
                new Thread(new Runnable() { // from class: com.android.phone.MiuiPhoneReceiver.7
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            return;
                        }
                        MiuiPhoneReceiver.this.handleATTSimChanged();
                    }
                }).start();
                return;
            }
        }
        SystemProperties.set("persist.radio.op.name", "MI");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGsmRatExist(ServiceState[] serviceStateArr) {
        for (ServiceState serviceState : serviceStateArr) {
            if (serviceState.getRilDataRadioTechnology() == 1 || serviceState.getRilDataRadioTechnology() == 2 || serviceState.getRilVoiceRadioTechnology() == 16) {
                return true;
            }
        }
        return false;
    }

    public int getTouchMode() {
        return this.mTouchMode;
    }

    private void switchNetworkTypeInRoaming(int i) {
        String networkCountryIso = TelephonyManager.getDefault().getNetworkCountryIso();
        if (TextUtils.isEmpty(this.mLastCountryIso) && !TextUtils.isEmpty(networkCountryIso)) {
            this.mLastCountryIso = networkCountryIso;
        }
        if (TextUtils.isEmpty(this.mLastCountryIso) || TextUtils.isEmpty(networkCountryIso) || Objects.equals(this.mLastCountryIso, networkCountryIso)) {
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        Rlog.d(TAG, "switchNetworkTypeInRoaming mLastCountryIso=" + this.mLastCountryIso + "; currentCountryIso=" + networkCountryIso + ";phoneId=" + i + "; subId=" + subscriptionIdForSlot);
        if (subscriptionIdForSlot != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
            if (this.mLastCountryIso.equals("kr")) {
                switchNetworkType(subscriptionIdForSlot, NetworkMode.getInstance().getLteNetworkModeValue(i));
            } else if (networkCountryIso.equals("kr")) {
                switchNetworkType(subscriptionIdForSlot, NetworkMode.getInstance().getNrNetworkModeValue(i));
            }
        }
        this.mLastCountryIso = networkCountryIso;
    }

    private void switchNetworkType(final int i, final int i2) {
        new Thread(this) { // from class: com.android.phone.MiuiPhoneReceiver.8
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                android.telephony.TelephonyManager telephonyManagerCreateForSubscriptionId;
                android.telephony.TelephonyManager telephonyManagerFrom = android.telephony.TelephonyManager.from(PhoneGlobals.getInstance());
                if (telephonyManagerFrom == null || (telephonyManagerCreateForSubscriptionId = telephonyManagerFrom.createForSubscriptionId(i)) == null) {
                    return;
                }
                telephonyManagerCreateForSubscriptionId.setAllowedNetworkTypesForReason(2, RadioAccessFamily.getRafFromNetworkType(i2));
            }
        }.start();
    }

    private void onIccStatusChanged(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("ss");
        int intExtra = intent.getIntExtra("phone", -1);
        clearHeDuoHao(context, stringExtra);
        clearDisableWfcByApState(intExtra);
        if ("LOADED".equals(stringExtra)) {
            showDataUsagePromptDialog(context);
            if (TelephonyManager.isCustForMxAt()) {
                setOperatorPropDependIccid(intExtra);
            }
            notifySimStateChanged(intExtra, FiveGManagerBase.CARRIER_CONFIG_STATE_LOADED);
            RoamingParametersManager.getInstance();
        } else {
            notifySimStateChanged(intExtra, FiveGManagerBase.CARRIER_CONFIG_STATE_CLEAR);
        }
        if ("ABSENT".equals(stringExtra) && isOtherCardsAbsent(intExtra)) {
            this.mMiSimActivateDialogShowedAgain = false;
            if (this.mHandler.hasMessages(20)) {
                this.mHandler.removeMessages(20);
            }
            if (this.mHandler.hasMessages(30)) {
                this.mHandler.removeMessages(30);
            }
            MiuiImsPhoneUtils.forceDismissWifiEmergencyCallNotification(intExtra);
        }
        showChangeDataPreferenceDialogIfNeed(stringExtra);
        showSimPickDialogIfNeed(stringExtra);
        openDual4GIfNeed(stringExtra);
        if (SubscriptionManager.isValidPhoneId(intExtra)) {
            showMiSimActivateDialogIfNeed(intExtra, stringExtra);
        }
        if (RoamingUtils.isRoamingOptimizationSupported(this.mContext)) {
            CallCapabilityManager.getInstance().onIccStatusChanged(intExtra, stringExtra);
            RoamingCFManager.getInstance().onIccStatusChanged(intExtra, stringExtra);
        }
        if (FiveGAStatistics.getInstance() != null) {
            FiveGAStatistics.getInstance().notifySimStateChanged(intExtra, intent);
        }
        MiuiNetworkInfoStats.getInstance().onSimCardStateChanged(intExtra, stringExtra);
    }

    private void notifySimStateChanged(int i, int i2) {
        FiveGManagerBase fiveGManagerBase;
        if (!MiuiPhoneUtils.isFiveGCapable() || (fiveGManagerBase = FiveGManagerBase.getInstance()) == null) {
            return;
        }
        fiveGManagerBase.onSimStateChanged(i, i2);
    }

    private void clearHeDuoHao(Context context, String str) {
        if (MiuiHeDuoHaoUtil.isHeDuoHaoEnable() && "ABSENT".equals(str)) {
            context.getContentResolver().delete(MiuiHeDuoHaoUtil.URI, null, null);
        }
    }

    private void openDual4G() {
        Rlog.i(TAG, "openDual4G");
        if (Dual4GManager.getInstance() == null) {
            Rlog.w(TAG, "openDual4G: Dual4GManager not initialized");
        } else {
            Dual4GManager.getInstance().setDual4GMode(true, new Utils.Dual4GCallBack(this) { // from class: com.android.phone.MiuiPhoneReceiver.9
                @Override // com.android.phone.utils.Utils.Dual4GCallBack
                public void onSetDual4GResult(boolean z, int i) {
                    Rlog.i(MiuiPhoneReceiver.TAG, "onSetDual4GResult enabled=" + z + ", resultCode=" + i);
                    if (i != 0) {
                        Toast.makeText(PhoneGlobals.getInstance().getApplicationContext(), z ? R.string.turn_on_dual_4g_failed : R.string.turn_off_dual_4g_failed, 0).show();
                    } else {
                        MiuiPhoneUtils.openedDual4GConfirmPrompt();
                    }
                }
            });
        }
    }

    private void openDual4GIfNeed(String str) {
        if (!MiuiPhoneUtils.isMultiSimEnabled() || !"LOADED".equals(str) || TelephonyManager.getDefault().getIccCardCount() == 1 || !TelephonyManager.getDefault().isDualVolteSupported() || Dual4GManager.isDual4GEnabled()) {
            Rlog.d(TAG, "openDual4GIfNeed, do nothing, return");
        } else {
            if (!MiuiPhoneUtils.cannotDisableDual4G() || Dual4GManager.getInstance() == null) {
                return;
            }
            Dual4GManager.getInstance().setDual4GMode(true, null);
        }
    }

    private void showChangeDataPreferenceDialogIfNeed(String str) {
        if (!Build.IS_INTERNATIONAL_BUILD || !MiuiPhoneUtils.isMultiSimEnabled() || !"LOADED".equals(str) || TelephonyManager.getDefault().getIccCardCount() == 1) {
            Rlog.d(TAG, "showChangeDataPreferenceDialogIfNeed, not international build or single verson,return");
            return;
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        boolean zIsDualVolteSupported = TelephonyManager.getDefault().isDualVolteSupported();
        Rlog.i(TAG, "showChangeDataPreferenceDialogIfNeed,isDualVolteSupported " + zIsDualVolteSupported + " ,getDefaultDataSlotId = " + defaultDataSlotId);
        if (!MiuiPhoneUtils.is4GOnlySim(TelephonyManager.getDefault().getSimOperatorForSlot(defaultDataSlotId)) || zIsDualVolteSupported) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                Rlog.d(TAG, "showChangeDataPreferenceDialogIfNeed i = " + i + "getSimInsertStates = " + DefaultSimManager.getInstance().getSimInsertStates(i));
                if (i != defaultDataSlotId && ((DefaultSimManager.getInstance().getSimInsertStates(i) == 2 || DefaultSimManager.getInstance().getSimInsertStates(i) == 4) && MiuiPhoneUtils.is4GOnlySim(TelephonyManager.getDefault().getSimOperatorForSlot(i)))) {
                    if (!zIsDualVolteSupported) {
                        showChangeDataPreferenceDialog(i);
                        return;
                    } else {
                        if (Dual4GManager.isDual4GEnabled()) {
                            return;
                        }
                        openDual4G();
                        return;
                    }
                }
            }
        }
    }

    public void showChangeDataPreferenceDialog(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 6);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
        Rlog.d(TAG, "showChangeDataPreferenceDialog, vice slot is " + i);
    }

    private void showMiSimActivateDialog(int i) {
        this.mMiSimActivateDialogShowed[i] = true;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.MiSimActivateDialogActivity"));
        intent.setFlags(402653184);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
        if (!this.mHandler.hasMessages(20) && !this.mMiSimActivateDialogShowedAgain) {
            this.mHandler.sendEmptyMessageDelayed(20, 3600000L);
        }
        Rlog.d(TAG, "showMiSimActivateDialog start, phoneId=" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean showMiSimActivateDialogIfNeed(int i, String str) {
        if (!Build.IS_INTERNATIONAL_BUILD && isMiSimActivateIntentAvailable(this.mContext)) {
            if (str == null) {
                str = MiuiPhoneUtils.getIccStateIntentString(IccCardConstants.State.intToState(android.telephony.TelephonyManager.getSimStateForSlotIndex(i)));
            }
            if ("LOADED".equals(str) && !this.mMiSimActivateDialogShowed[i]) {
                String simOperatorName = MiuiPhoneUtils.getSimOperatorName(i);
                String iccId = MiuiPhoneUtils.getIccId(i);
                String imsi = MiuiPhoneUtils.getImsi(i);
                if ("小米移动".equals(simOperatorName) && !TextUtils.isEmpty(iccId) && !TextUtils.isEmpty(imsi) && isNeedShowActivateDialog(imsi)) {
                    if (!MiuiPhoneUtils.isDevicesProvisioned(this.mContext)) {
                        if (this.mNeedRegistedProvisionCompleteReceiver) {
                            handleRegistProvisionCompleteReceiver();
                        }
                        return false;
                    }
                    showMiSimActivateDialog(i);
                    return true;
                }
            } else if ("ABSENT".equals(str)) {
                this.mMiSimActivateDialogShowed[i] = false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedShowActivateDialog(String str) {
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.pop_up_activation_box_imsi_entries);
        if (stringArray == null) {
            return false;
        }
        for (String str2 : stringArray) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMiSimActivateIntentAvailable(Context context) {
        List<ResolveInfo> listQueryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("com.miui.virtualsim.action.CARD_ACTIVATE"), 0);
        Rlog.d(TAG, "isMiSimActivateIntentAvailable list=" + listQueryIntentActivities);
        return listQueryIntentActivities.size() > 0;
    }

    private void showSimPickDialogIfNeed(String str) {
        int i;
        if (Build.IS_INTERNATIONAL_BUILD || !Build.IS_CM_CUSTOMIZATION) {
            return;
        }
        if (MiuiPhoneUtils.isMultiSimEnabled() && "LOADED".equals(str)) {
            boolean z = true;
            if (TelephonyManager.getDefault().getIccCardCount() != 1) {
                int[] iArr = new int[MiuiPhoneUtils.PHONE_COUNT];
                for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                    iArr[i2] = DefaultSimManager.getInstance().getSimInsertStates(i2);
                }
                if (isAnyCardStateChanged(iArr)) {
                    List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
                    Iterator it = subscriptionInfoList.iterator();
                    boolean z2 = true;
                    while (it.hasNext()) {
                        if (!((SubscriptionInfo) it.next()).isActivated()) {
                            z2 = false;
                        }
                    }
                    Rlog.d(TAG, "CardStateChange start isAllInfoActive " + z2);
                    int i3 = 0;
                    while (true) {
                        i = MiuiPhoneUtils.PHONE_COUNT;
                        if (i3 >= i) {
                            break;
                        }
                        Rlog.d(TAG, "sim state for " + i3 + ":" + android.telephony.TelephonyManager.getSimStateForSlotIndex(i3));
                        if (android.telephony.TelephonyManager.getSimStateForSlotIndex(i3) != 10) {
                            z = false;
                        }
                        i3++;
                    }
                    if (subscriptionInfoList.size() == i && z2 && z) {
                        if (!NetworkModeManager.isDeviceLockViceNetworkModeForCmcc() || SubscriptionManager.INVALID_SUBSCRIPTION_ID == DefaultSlotSelectorImpl.getDataSlotForCmcc()) {
                            showSimPickDialog();
                            Rlog.d(TAG, "showSimPickDialog start");
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
        }
        Rlog.d(TAG, "showSimPickDialogIfNeed, not cmcc build or single verson,return");
    }

    private void showSimPickDialog() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 8);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, DefaultSimManager.getDefaultDataSlotId());
        phoneGlobals.startActivity(intent);
    }

    private boolean isAnyCardStateChanged(int[] iArr) {
        if (iArr == null) {
            return false;
        }
        for (int i : iArr) {
            if (i == 2 || i == 4) {
                return true;
            }
        }
        return false;
    }

    private void checkSystemUpdate() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(phoneGlobals);
        String string = defaultSharedPreferences.getString("build_miui_fingerprint", null);
        String str = android.os.Build.FINGERPRINT;
        if (str.equals(string)) {
            return;
        }
        Rlog.d(TAG, "checkSystemUpdate Build fingerprint changed. old: " + string + " new: " + str);
        defaultSharedPreferences.edit().putString("build_miui_fingerprint", str).apply();
        if (Build.IS_INTERNATIONAL_BUILD && ImsAdapterBase.getInstance().isVolteSupportedByDevice(phoneGlobals) && !MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            updateVolteConfig(Build.getRegion(), true);
        }
    }

    private void updateVolteConfig(String str, boolean z) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (!MiuiPhoneUtils.isRegionSupportVoLTE(phoneGlobals, str)) {
            ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(phoneGlobals, false);
            return;
        }
        if (z) {
            return;
        }
        if (MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (TelephonyManager.getDefault().isVolteEnabledByPlatform(i)) {
                    ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(phoneGlobals, true, i);
                }
            }
            return;
        }
        int i2 = Settings.Global.getInt(phoneGlobals.getContentResolver(), "volte_vt_enabled", -1);
        if (i2 == -1 || i2 == 0) {
            if (TelephonyManager.getDefault().isVolteEnabledByPlatform()) {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(phoneGlobals, true);
            } else {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(phoneGlobals, 1);
            }
        }
    }

    private void showWifiCallingNotification(Context context, int i) {
        String str;
        if (ImsAdapterBase.getInstance().isWfcEnabledByPlatform(context, i)) {
            boolean z = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
            if (z) {
                str = "com.android.phone.settings.MultiSimInfoEditorActivity";
            } else {
                str = "com.android.phone.settings.MobileNetworkSettings";
            }
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, str));
            if (z) {
                Bundle bundle = new Bundle();
                SubscriptionManager.putSlotId(bundle, i);
                intent.putExtras(bundle);
            }
            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 1140850688);
            Resources resourcesForSubId = android.telephony.SubscriptionManager.getResourcesForSubId(context, MiuiPhoneUtils.getPhone(i).getSubId());
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(resourcesForSubId.getString(R.string.wifi_call_welcome_settings)).setSmallIcon(R.drawable.icon_volte_provision_notfication_black).setAutoCancel(true).setShowWhen(false).setContentIntent(activity).setStyle(new Notification.BigTextStyle(builder).bigText(resourcesForSubId.getString(R.string.wifi_call_notification_message))).setChannel("wfc");
            this.mNotificationManager.notify(i, builder.build());
            MiuiImsPhoneUtils.setPrefBoolean("show_wifi_calling_dialog", false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWifiCallingDialog(Context context, int i) {
        if (ImsAdapterBase.getInstance().isWfcEnabledByPlatform(context, i)) {
            Rlog.d(TAG, "show wifi calling welcome Dialog. ");
            Intent intent = new Intent(context, (Class<?>) MiuiErrorDialogActivity.class);
            intent.putExtra("dialog_type", 9);
            intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
            intent.setFlags(268435456);
            context.startActivity(intent);
            MiuiImsPhoneUtils.setPrefBoolean("show_wifi_calling_dialog", false);
        }
    }

    private void initMiuiImsRegisterState(Context context) {
        if (!MiuiImsRegControllerBase.isSupportImsRegOptimization(context)) {
            Rlog.d(TAG, "not support monitor ims register state");
            return;
        }
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null || phone.getImsPhone() == null) {
                Rlog.d(TAG, "imsPhone is null");
            } else {
                this.mMiuiImsRegistrationControllers[i] = new MiuiImsRegControllerImpl(context, phone.getImsPhone());
            }
        }
    }

    private void showWifiCallingDialogDelayed(final Context context, final int i) {
        new Handler().postDelayed(new Runnable() { // from class: com.android.phone.MiuiPhoneReceiver.10
            @Override // java.lang.Runnable
            public void run() {
                MiuiPhoneReceiver.this.showWifiCallingDialog(context, i);
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private boolean isOtherCardsAbsent(int i) {
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            if (i2 != i && "ABSENT".equals(MiuiPhoneUtils.getIccStateIntentString(IccCardConstants.State.intToState(TelephonyManager.getDefault().getSimStateForSlot(i2))))) {
                return true;
            }
        }
        return false;
    }

    public void enterMiSimNotActiveTips(int i) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("dialog_type", 18);
        intent.putExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, i);
        phoneGlobals.startActivity(intent);
        Rlog.d(TAG, "enterMiSimNotActiveTips, vice slot is " + i);
    }

    static void showDataUsagePromptDialog(Context context) {
        if (TelephonyManager.isCustForKrOps() && Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0 && MiuiPhoneUtils.isMobileDataEnabled(context) && Settings.Global.getInt(context.getContentResolver(), "always_alert_data_enable", 1) == 1 && !SystemProperties.getBoolean("gsm.data.data_prompt", false)) {
            android.telephony.TelephonyManager.from(PhoneGlobals.getInstance()).setDataEnabled(false);
            SystemProperties.set("gsm.data.data_prompt", "true");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileDataUsageWarningActivity"));
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleATTSimChanged() {
        Rlog.d(TAG, "handleATTSimChanged");
        try {
            try {
                if (this.mContext.getPackageManager().getApplicationInfo("com.android.thememanager", 0) != null) {
                    this.mContext.getContentResolver().call(Uri.parse("content://com.android.thememanager.theme_provider_glim"), "bootComplete", (String) null, (Bundle) null);
                } else {
                    Rlog.e(TAG, "handleATTSimChanged: appinfo is null");
                }
            } catch (Exception e) {
                Rlog.e(TAG, "handleATTSimChanged: e" + e);
            }
        } finally {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
            editorEdit.putBoolean("key_att_ringtone_done", true);
            editorEdit.apply();
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MiuiPhoneReceiver: ".concat(" touchMode=" + getTouchMode()));
    }

    private void registerTelephonyInfoCallBack() {
        Rlog.d(TAG, "registerTelephonyInfoCallBack");
        if (this.mTeleCallBack == null) {
            Rlog.d(TAG, "new TeleCallBack");
            TeleCallBack teleCallBack = new TeleCallBack();
            this.mTeleCallBack = teleCallBack;
            TelephonyStatAdapter.registerCallBack(teleCallBack);
        }
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        private TeleCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            MiuiPhoneReceiver.this.mHandler.obtainMessage(3, i, 0, signalStrength).sendToTarget();
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            MiuiPhoneReceiver.this.mHandler.obtainMessage(2, i, 0, serviceState).sendToTarget();
        }
    }

    private void handleRegistProvisionCompleteReceiver() {
        Rlog.d(TAG, "handleRegistProvisionCompleteReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provision.action.PROVISION_COMPLETE");
        this.mContext.registerReceiver(this, intentFilter, 2);
        this.mNeedRegistedProvisionCompleteReceiver = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeO2VowifiNotifyDialog(Context context) {
        Intent intent = new Intent(context, (Class<?>) MiuiErrorDialogActivity.class);
        intent.putExtra("dialog_type", 23);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private void showDeO2VowifiNotifyDelay(final Context context) {
        new Handler().postDelayed(new Runnable() { // from class: com.android.phone.MiuiPhoneReceiver.11
            @Override // java.lang.Runnable
            public void run() {
                MiuiPhoneReceiver.this.showDeO2VowifiNotifyDialog(context);
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    private void setVoLTERoamingStatue(int i, int i2) {
        byte[] bArr = new byte[4];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        byteBufferWrap.putInt(i2);
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            modemUtilsInit.callHook("onHookNVCMD", 2, Integer.valueOf(i), bArr);
            Rlog.d(TAG, "set VoLTE roaming status: " + i2 + " for phoneId" + i);
            return;
        }
        Rlog.d(TAG, "modemUtils is null,fail to set VoLTE roaming status");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEsimRoamingDialogIfNeed(int i, ServiceState serviceState) {
        if (!HyperOSCustFeatureResolve.getBoolean("config_esim_roaming_assistant", false)) {
            Rlog.d(TAG, "not support esim roaming assistant feature");
            return;
        }
        if (serviceState == null || !SubscriptionManager.isValidPhoneId(i)) {
            Rlog.d(TAG, "ss is null or phoneId is invalid");
            return;
        }
        if (!serviceState.getRoaming()) {
            Rlog.d(TAG, "showEsimRoamingDialogIfNeed: not roaming, skip");
            resetEsimRoamingDialogState();
        } else if (shouldShowEsimRoamingDialog(serviceState)) {
            Rlog.d(TAG, "showEsimRoamingDialog");
            showEsimRoamingDialog(this.mContext.getApplicationContext());
        }
    }

    private String getOperatorMcc(ServiceState serviceState) {
        String operatorNumeric = serviceState.getOperatorNumeric();
        if (!TextUtils.isEmpty(operatorNumeric) && operatorNumeric.length() >= 3) {
            try {
                return Integer.toString(Integer.parseInt(operatorNumeric.substring(0, 3)));
            } catch (Exception e) {
                Rlog.e(TAG, "getOperatorMcc error: " + e);
            }
        }
        return null;
    }

    private boolean shouldShowEsimRoamingDialog(ServiceState serviceState) {
        List allSubscriptionInfoList = SubscriptionManager.getDefault().getAllSubscriptionInfoList();
        if (allSubscriptionInfoList == null || allSubscriptionInfoList.isEmpty()) {
            Rlog.d(TAG, "subscriptionInfoList is null or empty");
            return false;
        }
        Rlog.d(TAG, "subscriptionInfoList size: " + allSubscriptionInfoList.size());
        return hasMatchingEmbeddedSubscription(allSubscriptionInfoList, serviceState);
    }

    private boolean hasMatchingEmbeddedSubscription(List list, ServiceState serviceState) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (isMatchingSubscription((SubscriptionInfo) it.next(), serviceState)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchingSubscription(SubscriptionInfo subscriptionInfo, ServiceState serviceState) {
        if (subscriptionInfo == null) {
            return false;
        }
        if (!subscriptionInfo.isEmbedded()) {
            Rlog.d(TAG, "isMatchingSubscription: subscriptionInfo is not embedded");
            return false;
        }
        int mcc = subscriptionInfo.getMcc();
        Rlog.d(TAG, "isMatchingSubscription: simMcc = " + mcc);
        if (mcc == 0) {
            Rlog.d(TAG, "isMatchingSubscription: simMcc is 0, skip");
            return false;
        }
        boolean z = subscriptionInfo.getSlotId() >= 0;
        Rlog.d(TAG, "isMatchingSubscription: isActivated = " + z);
        if (z) {
            Rlog.d(TAG, "isMatchingSubscription: is activated, skip");
            return false;
        }
        String operatorMcc = getOperatorMcc(serviceState);
        Rlog.d(TAG, "isMatchingSubscription: operatorMcc = " + operatorMcc);
        if (operatorMcc != null) {
            return operatorMcc.equals(Integer.toString(mcc)) && !z;
        }
        Rlog.d(TAG, "isMatchingSubscription: operatorMcc is null, skip");
        return false;
    }

    private void resetEsimRoamingDialogState() {
        try {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("esim_roaming_prefs", 0);
            boolean z = sharedPreferences.getBoolean("dialog_disabled", false);
            int i = sharedPreferences.getInt("last_show_stage", 0);
            if (!z && i <= 0) {
                return;
            }
            sharedPreferences.edit().clear().apply();
            cancelEsimRoamingAlarm();
            Rlog.d(TAG, "resetEsimRoamingDialogState: cleared prefs and canceled alarm (wasDisabled=" + z + ", lastStage=" + i + ")");
        } catch (Exception e) {
            Rlog.e(TAG, "resetEsimRoamingDialogState error: " + e);
        }
    }

    private void cancelEsimRoamingAlarm() {
        try {
            AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
            if (alarmManager == null) {
                return;
            }
            Intent intent = new Intent(this.mContext, (Class<?>) MiuiEsimRoamingCheckReceiver.class);
            intent.setAction("miui.intent.action.ESIM_ROAMING_CHECK");
            PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592);
            alarmManager.cancel(broadcast);
            broadcast.cancel();
            Rlog.d(TAG, "cancelEsimRoamingAlarm: alarm canceled");
        } catch (Exception e) {
            Rlog.e(TAG, "cancelEsimRoamingAlarm error: " + e);
        }
    }

    private void showEsimRoamingDialog(Context context) {
        Rlog.d(TAG, "showEsimRoamingDialog");
        Intent intent = new Intent(context, (Class<?>) MiuiEsimRoamingDialogActivity.class);
        intent.setFlags(335544320);
        context.startActivity(intent);
    }
}
