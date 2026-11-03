package com.mediatek.internal.telephony;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.telephony.data.DataProfile;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DefaultPhoneNotifier;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.ImsSmsDispatcher;
import com.android.internal.telephony.InboundSmsTracker;
import com.android.internal.telephony.MultiSimSettingController;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.SignalStrengthController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.SmsUsageMonitor;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.WapPushOverSms;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cat.CommandParamsFactory;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.data.AutoDataSwitchController;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.mediatek.internal.telephony.carrierexpress.CarrierExpressFwkHandler;
import com.mediatek.internal.telephony.cat.MtkCatLog;
import com.mediatek.internal.telephony.cat.MtkCatService;
import com.mediatek.internal.telephony.cat.MtkCommandParamsFactory;
import com.mediatek.internal.telephony.cat.MtkRilMessageDecoder;
import com.mediatek.internal.telephony.cdma.MtkCdmaInboundSmsHandler;
import com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher;
import com.mediatek.internal.telephony.data.MtkAutoDataSwitchController;
import com.mediatek.internal.telephony.data.MtkDataConfigManager;
import com.mediatek.internal.telephony.data.MtkDataHelper;
import com.mediatek.internal.telephony.data.MtkDataNetwork;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.data.MtkDataProfileManager;
import com.mediatek.internal.telephony.data.MtkPhoneSwitcher;
import com.mediatek.internal.telephony.datasub.SmartDataSwitchAssistant;
import com.mediatek.internal.telephony.gsm.MtkGsmInboundSmsHandler;
import com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneMmiCode;
import com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.internal.telephony.uicc.MtkUiccProfile;
import com.mediatek.internal.telephony.uicc.MtkUiccSlot;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class MtkTelephonyComponentFactory extends TelephonyComponentFactory {
    private static final String LOG_TAG = "MtkTelephonyComponentFactory";
    private static MtkTelephonyComponentFactory sInstance;

    public static MtkTelephonyComponentFactory getInstance() {
        if (sInstance == null) {
            sInstance = new MtkTelephonyComponentFactory();
        }
        return sInstance;
    }

    /* JADX INFO: renamed from: makePhone, reason: merged with bridge method [inline-methods] */
    public GsmCdmaPhone m222makePhone(Context context, CommandsInterface ci, PhoneNotifier notifier, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        return new MtkGsmCdmaPhone(context, ci, notifier, phoneId, precisePhoneType, telephonyComponentFactory, featureFlags);
    }

    public RIL makeRil(Context context, int allowedNetworkTypes, Integer instanceId, FeatureFlags featureFlags) {
        return new MtkRIL(context, allowedNetworkTypes, instanceId, featureFlags);
    }

    public ServiceStateTracker makeServiceStateTracker(GsmCdmaPhone phone, CommandsInterface ci, FeatureFlags featureFlags) {
        return new MtkServiceStateTracker(phone, ci, featureFlags);
    }

    public MultiSimSettingController initMultiSimSettingController(Context c, FeatureFlags featureFlags) {
        return MtkMultiSimSettingController.init(c, featureFlags);
    }

    public GsmCdmaCallTracker makeGsmCdmaCallTracker(GsmCdmaPhone phone, FeatureFlags featureFlags) {
        return new MtkGsmCdmaCallTracker(phone, featureFlags);
    }

    public SubscriptionManagerService makeSubscriptionManagerService(Context context, Looper looper, FeatureFlags featureFlags) {
        return new MtkSubscriptionManagerService(context, looper, featureFlags);
    }

    public DefaultPhoneNotifier makeDefaultPhoneNotifier(Context context, FeatureFlags featureFlags) {
        Rlog.d(LOG_TAG, "makeDefaultPhoneNotifier mtk");
        return new MtkPhoneNotifier(context, featureFlags);
    }

    public UiccController makeUiccController(Context c, FeatureFlags featureFlags) {
        Rlog.d(LOG_TAG, "makeUiccController mtk");
        return new MtkUiccController(c, featureFlags);
    }

    public UiccProfile makeUiccProfile(Context context, CommandsInterface ci, IccCardStatus ics, int phoneId, UiccCard uiccCard, Object lock, FeatureFlags featureFlags) {
        return new MtkUiccProfile(context, ci, ics, phoneId, uiccCard, lock, featureFlags);
    }

    public UiccSlot makeUiccSlot(Context context, boolean isActive, FeatureFlags featureFlags) {
        return new MtkUiccSlot(context, isActive, featureFlags);
    }

    public void initRadioManager(Context context, int numPhones, CommandsInterface[] sCommandsInterfaces) {
        if (context == null) {
            Rlog.i(LOG_TAG, "XiaomiTelephonyComponentFactory.makeMtkLegacy failed, context is null");
        } else {
            try {
                String tmpPath = context.getDir("Jar", 0).getAbsolutePath();
                DexClassLoader miTeleJarLoader = new DexClassLoader("/system_ext/framework/xiaomi-telephony-common.jar", tmpPath, null, MtkTelephonyComponentFactory.class.getClassLoader());
                Rlog.i(LOG_TAG, "miTeleJarLoader = " + miTeleJarLoader);
                Class miTeleClass = miTeleJarLoader.loadClass("com.xiaomi.internal.telephony.XiaomiTelephonyComponentFactory");
                Rlog.i(LOG_TAG, "miTeleClass = " + miTeleClass);
                Method mtdMake = miTeleClass.getMethod("makeMtkLegacy", Context.class, Integer.TYPE, CommandsInterface[].class);
                Rlog.i(LOG_TAG, "makeMtkLegacy = " + mtdMake);
                mtdMake.invoke(null, context, Integer.valueOf(numPhones), sCommandsInterfaces);
                Rlog.i(LOG_TAG, "XiaomiTelephonyComponentFactory.makeMtkLegacy ok");
            } catch (Exception e) {
                Rlog.i(LOG_TAG, "XiaomiTelephonyComponentFactory.makeMtkLegacy failed, error:" + e.toString());
                e.printStackTrace();
            }
        }
        RadioManager.init(context, numPhones, sCommandsInterfaces);
    }

    public CatService makeCatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir, Context context, IccFileHandler fh, UiccProfile uiccProfile, int slotId, Looper looper, FeatureFlags featureFlags) {
        int phoneType;
        if (slotId == -1) {
            phoneType = 1;
        } else {
            int phoneType2 = TelephonyManager.getDefault().getCurrentPhoneType();
            MtkCatLog.d("MtkCatService", "makeCatService phoneType : " + phoneType2 + " slotId: " + slotId);
            phoneType = phoneType2;
        }
        if (uiccProfile != null) {
            if (phoneType != 2) {
                ca = uiccProfile.getApplicationIndex(0);
            } else {
                ca = uiccProfile.getApplication(2);
            }
        }
        MtkCatLog.d("MtkCatService", "makeCatService  ca = " + ca);
        if (ci != null && ca != null && ir != null && context != null && fh != null && uiccProfile != null) {
            return new MtkCatService(ci, ca, ir, context, fh, uiccProfile, slotId, looper, featureFlags);
        }
        MtkCatLog.e("MtkCatService", "makeCatService exception, will not create MtkCatservice!!!!");
        return null;
    }

    public RilMessageDecoder makeRilMessageDecoder(Handler caller, IccFileHandler fh, Context context, int slotId) {
        return new MtkRilMessageDecoder(caller, fh, context, slotId);
    }

    public CommandParamsFactory makeCommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh, Context context) {
        return new MtkCommandParamsFactory(caller, fh, context);
    }

    public DisplayInfoController makeDisplayInfoController(Phone phone, FeatureFlags featureFlags) {
        return new MtkDisplayInfoController(phone, featureFlags);
    }

    public void makeSmartDataSwitchAssistant(Context context, Phone[] phones) {
        SmartDataSwitchAssistant.makeSmartDataSwitchAssistant(context, phones);
    }

    public void makeSuppServManager(Context context, Phone[] phones) {
        MtkSuppServManager ssManager = MtkSuppServManager.makeSuppServManager(context, phones);
        ssManager.init();
    }

    public PhoneSwitcher makePhoneSwitcher(int maxDataAttachModemCount, Context context, Looper looper, FeatureFlags featureFlags) {
        if (context != null) {
            if (WifiSarController.isNeeded()) {
                new WifiSarController(context);
            }
            if (BluetoothSarController.isNeeded()) {
                new BluetoothSarController(context);
            }
        } else {
            Rlog.i(LOG_TAG, "context is null");
        }
        return MtkPhoneSwitcher.mtkMake(maxDataAttachModemCount, context, looper, featureFlags);
    }

    public SmsStorageMonitor makeSmsStorageMonitor(Phone phone, FeatureFlags featureFlags) {
        return new MtkSmsStorageMonitor(phone, featureFlags);
    }

    public SmsUsageMonitor makeSmsUsageMonitor(Context context, FeatureFlags featureFlags) {
        return new MtkSmsUsageMonitor(context, featureFlags);
    }

    public IccSmsInterfaceManager makeIccSmsInterfaceManager(Phone phone, FeatureFlags featureFlags) {
        return new MtkIccSmsInterfaceManager(phone, featureFlags);
    }

    public ImsSmsDispatcher makeImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        return new MtkImsSmsDispatcher(phone, smsDispatchersController, featureFlags);
    }

    public GsmSMSDispatcher makeGsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler, FeatureFlags featureFlags) {
        return new MtkGsmSMSDispatcher(phone, smsDispatchersController, gsmInboundSmsHandler, featureFlags);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, boolean is3gpp2WapPdu, String address, String displayAddr, String messageBody, boolean isClass0, int subId, int smsSource) {
        return new MtkInboundSmsTracker(context, pdu, timestamp, destPort, is3gpp2, is3gpp2WapPdu, address, displayAddr, messageBody, isClass0, subId, smsSource);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, String address, String displayAddr, int referenceNumber, int sequenceNumber, int messageCount, boolean is3gpp2WapPdu, String messageBody, boolean isClass0, int subId, int smsSource) {
        return new MtkInboundSmsTracker(context, pdu, timestamp, destPort, is3gpp2, address, displayAddr, referenceNumber, sequenceNumber, messageCount, is3gpp2WapPdu, messageBody, isClass0, subId, smsSource);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, Cursor cursor, boolean isCurrentFormat3gpp2) {
        return new MtkInboundSmsTracker(context, cursor, isCurrentFormat3gpp2);
    }

    public void makeSmsBroadcastUndelivered(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler, FeatureFlags featureFlags) {
        MtkSmsBroadcastUndelivered.initialize(context, gsmInboundSmsHandler, cdmaInboundSmsHandler, featureFlags);
    }

    public WapPushOverSms makeWapPushOverSms(Context context, FeatureFlags featureFlags) {
        return new MtkWapPushOverSms(context, featureFlags);
    }

    public GsmInboundSmsHandler makeGsmInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, Looper looper, FeatureFlags featureFlags) {
        return MtkGsmInboundSmsHandler.makeInboundSmsHandler(context, storageMonitor, phone, looper, featureFlags);
    }

    public MtkSmsHeader makeSmsHeader() {
        return new MtkSmsHeader();
    }

    public SmsDispatchersController makeSmsDispatchersController(Phone phone, SmsStorageMonitor storageMonitor, SmsUsageMonitor usageMonitor, FeatureFlags featureFlags) {
        return new MtkSmsDispatchersController(phone, phone.mSmsStorageMonitor, phone.mSmsUsageMonitor, featureFlags);
    }

    public ProxyController makeProxyController(Context context, FeatureFlags flags) {
        return new MtkProxyController(context, flags);
    }

    public CdmaInboundSmsHandler makeCdmaInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, CdmaSMSDispatcher smsDispatcher, Looper looper, FeatureFlags featureFlags) {
        return new MtkCdmaInboundSmsHandler(context, storageMonitor, phone, smsDispatcher, looper, featureFlags);
    }

    public CdmaSMSDispatcher makeCdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        return new MtkCdmaSMSDispatcher(phone, smsDispatchersController, featureFlags);
    }

    public IccPhoneBookInterfaceManager makeIccPhoneBookInterfaceManager(Phone phone) {
        Rlog.d(LOG_TAG, "makeIccPhoneBookInterfaceManager mtk");
        return new MtkIccPhoneBookInterfaceManager(phone);
    }

    public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone imsPhone) {
        return makeImsPhoneCallTracker(imsPhone, new FeatureFlagsImpl());
    }

    public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone imsPhone, FeatureFlags featureFlags) {
        return new MtkImsPhoneCallTracker(imsPhone, featureFlags);
    }

    public ImsPhone makeImsPhone(Context context, PhoneNotifier phoneNotifier, Phone defaultPhone, FeatureFlags featureFlags) {
        try {
            return new MtkImsPhone(context, phoneNotifier, defaultPhone, featureFlags);
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "makeImsPhone", e);
            return null;
        }
    }

    public DeviceStateMonitor makeDeviceStateMonitor(Phone phone, FeatureFlags featureFlags) {
        return new MtkDeviceStateMonitor(phone, featureFlags);
    }

    public DataNetworkController makeDataNetworkController(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new MtkDataNetworkController(phone, looper, featureFlags);
    }

    public DataNetwork makeDataNetwork(Phone phone, FeatureFlags featureFlags, Looper looper, SparseArray<DataServiceManager> dataServiceManagers, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int transport, boolean isSatellite, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetwork.DataNetworkCallback callback) {
        return new MtkDataNetwork(phone, featureFlags, looper, dataServiceManagers, dataProfile, networkRequestList, transport, isSatellite, dataAllowedReason, callback);
    }

    public DataConfigManager makeDataConfigManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new MtkDataConfigManager(phone, looper, featureFlags);
    }

    public DataProfileManager makeDataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, FeatureFlags featureFlags, DataProfileManager.DataProfileManagerCallback callback) {
        return new MtkDataProfileManager(phone, dataNetworkController, dataServiceManager, looper, featureFlags, callback);
    }

    public AutoDataSwitchController makeAutoDataSwitchController(Context context, Looper looper, PhoneSwitcher phoneSwitcher, FeatureFlags featureFlags, AutoDataSwitchController.AutoDataSwitchControllerCallback phoneSwitcherCallback) {
        return new MtkAutoDataSwitchController(context, looper, phoneSwitcher, featureFlags, phoneSwitcherCallback);
    }

    public void makeDataHelper(Context context, Phone[] phones, FeatureFlags featureFlags) {
        MtkDataHelper.makeMtkDataHelper(context, phones, featureFlags);
    }

    public void initCarrierExpress() {
        Rlog.d(LOG_TAG, "Creating CarrierExpress");
        CarrierExpressFwkHandler.init();
    }

    public void initGwsdService(Context context) {
        if (SystemProperties.get("ro.vendor.mtk_protocol1_rat_config", "").equals("no")) {
            Rlog.d(LOG_TAG, "initGwsdService wifi only project rat = no, return directly!");
            return;
        }
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0") && SystemProperties.get("ro.vendor.mtk_gwsd_support", "0").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            String classPackage = "";
            try {
                if (SystemProperties.get("ro.vendor.mtk_gwsd_capability", "0").equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) {
                    classPackage = "/system_ext/framework/mediatek-gwsdv2.jar";
                }
                if (SystemProperties.get("ro.vendor.mtk_gwsd_capability", "0").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                    classPackage = "/system_ext/framework/mediatek-gwsd.jar";
                }
                PathClassLoader classLoader = new PathClassLoader(classPackage, ClassLoader.getSystemClassLoader());
                Class<?> clazz = Class.forName("com.mediatek.gwsd.service.GwsdService", false, classLoader);
                Rlog.d(LOG_TAG, "class = " + clazz);
                Method method = clazz.getMethod("getInstance", Context.class);
                method.invoke(clazz, context);
            } catch (Exception e) {
                Rlog.e(LOG_TAG, Log.getStackTraceString(e));
            }
        }
    }

    public SignalStrengthController makeSignalStrengthController(GsmCdmaPhone phone) {
        return new MtkSignalStrengthController(phone);
    }

    public ImsPhoneMmiCode makeImsPhoneMmiCode(ImsPhone phone, FeatureFlags featureFlags) {
        return new MtkImsPhoneMmiCode(phone, featureFlags);
    }

    public PhoneConfigurationManager makePhoneConfigurationManager(Context context, FeatureFlags featureFlags) {
        return new MtkPhoneConfigurationManager(context, featureFlags);
    }
}
