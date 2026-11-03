package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.net.LocalServerSocket;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.AnomalyReporter;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.data.CellularNetworkValidator;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.data.TelephonyNetworkProvider;
import com.android.internal.telephony.euicc.EuiccCardController;
import com.android.internal.telephony.euicc.EuiccController;
import com.android.internal.telephony.imsphone.ImsPhoneFactory;
import com.android.internal.telephony.metrics.MetricsCollector;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class PhoneFactory {
    private static CellularNetworkValidator sCellularNetworkValidator = null;
    private static CommandsInterface[] sCommandsInterfaces = null;
    private static Context sContext = null;
    private static EuiccCardController sEuiccCardController = null;
    private static EuiccController sEuiccController = null;
    private static IntentBroadcaster sIntentBroadcaster = null;
    private static boolean sMadeDefaults = false;
    private static MetricsCollector sMetricsCollector;
    private static NotificationChannelController sNotificationChannelController;
    private static Phone sPhone;
    private static PhoneConfigurationManager sPhoneConfigurationManager;
    private static PhoneNotifier sPhoneNotifier;
    private static PhoneSwitcher sPhoneSwitcher;
    private static Phone[] sPhones;
    private static ProxyController sProxyController;
    private static RadioInterfaceCapabilityController sRadioHalCapabilities;
    private static SimultaneousCallingTracker sSimultaneousCallingTracker;
    private static SubscriptionManagerService sSubscriptionManagerService;
    private static TelephonyNetworkProvider sTelephonyNetworkProvider;
    private static UiccController sUiccController;
    static final Object sLockProxyPhones = new Object();
    private static HashMap sLocalLogs = new HashMap();
    private static FeatureFlags sFeatureFlags = new FeatureFlagsImpl();

    public static void makeDefaultPhones(Context context, FeatureFlags featureFlags) {
        sFeatureFlags = featureFlags;
        makeDefaultPhone(context, featureFlags);
    }

    public static FeatureFlags getFeatureFlags() {
        return sFeatureFlags;
    }

    public static void makeDefaultPhone(Context context, FeatureFlags featureFlags) {
        synchronized (sLockProxyPhones) {
            try {
                if (!sMadeDefaults) {
                    sContext = context;
                    int i = 0;
                    while (true) {
                        i++;
                        try {
                            new LocalServerSocket("com.android.internal.telephony");
                            TelephonyComponentFactory telephonyComponentFactoryInject = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName());
                            sMetricsCollector = new MetricsCollector(context, sFeatureFlags);
                            sPhoneNotifier = telephonyComponentFactoryInject.makeDefaultPhoneNotifier(context, featureFlags);
                            int activeModemCount = TelephonyManager.getDefault().getActiveModemCount();
                            int[] iArr = new int[activeModemCount];
                            sPhones = new Phone[activeModemCount];
                            sCommandsInterfaces = new RIL[activeModemCount];
                            for (int i2 = 0; i2 < activeModemCount; i2++) {
                                iArr[i2] = RILConstants.PREFERRED_NETWORK_MODE;
                                Rlog.i("PhoneFactory", "Network Mode set to " + Integer.toString(iArr[i2]));
                                sCommandsInterfaces[i2] = telephonyComponentFactoryInject.makeRil(context, RadioAccessFamily.getRafFromNetworkType(iArr[i2]), Integer.valueOf(i2), featureFlags);
                            }
                            telephonyComponentFactoryInject.initRadioManager(context, activeModemCount, sCommandsInterfaces);
                            if (activeModemCount > 0) {
                                sRadioHalCapabilities = RadioInterfaceCapabilityController.init(RadioConfig.make(context, sCommandsInterfaces[0].getHalVersion(0)), sCommandsInterfaces[0]);
                            } else {
                                sRadioHalCapabilities = RadioInterfaceCapabilityController.init(RadioConfig.make(context, HalVersion.UNKNOWN), null);
                            }
                            sUiccController = UiccController.make(context, featureFlags);
                            Rlog.i("PhoneFactory", "Creating SubscriptionManagerService");
                            sSubscriptionManagerService = SubscriptionManagerService.init(context, Looper.myLooper(), featureFlags);
                            TelephonyComponentFactory.getInstance().inject(MultiSimSettingController.class.getName()).initMultiSimSettingController(context, featureFlags);
                            if (context.getPackageManager().hasSystemFeature("android.hardware.telephony.euicc")) {
                                sEuiccController = EuiccController.init(context, sFeatureFlags);
                                sEuiccCardController = EuiccCardController.init(context, sFeatureFlags);
                            }
                            for (int i3 = 0; i3 < activeModemCount; i3++) {
                                sPhones[i3] = createPhone(context, i3);
                            }
                            if (activeModemCount > 0) {
                                sPhone = sPhones[0];
                            }
                            ComponentName defaultSmsApplication = SmsApplication.getDefaultSmsApplication(context, true);
                            String packageName = "NONE";
                            if (defaultSmsApplication != null) {
                                packageName = defaultSmsApplication.getPackageName();
                            }
                            Rlog.i("PhoneFactory", "defaultSmsApplication: " + packageName);
                            SmsApplication.grantPermissionsToSystemApps(context);
                            SmsApplication.initSmsPackageMonitor(context);
                            sMadeDefaults = true;
                            TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeSuppServManager(sContext, sPhones);
                            telephonyComponentFactoryInject.makeSmartDataSwitchAssistant(sContext, sPhones);
                            telephonyComponentFactoryInject.initGwsdService(context);
                            if (context.getPackageManager().hasSystemFeature("android.hardware.telephony.ims")) {
                                for (int i4 = 0; i4 < activeModemCount; i4++) {
                                    sPhones[i4].createImsPhone();
                                }
                            } else {
                                Rlog.i("PhoneFactory", "IMS is not supported on this device, skipping ImsResolver.");
                            }
                            telephonyComponentFactoryInject.makeDataHelper(sContext, sPhones, featureFlags);
                            sPhoneConfigurationManager = PhoneConfigurationManager.init(sContext, featureFlags);
                            sSimultaneousCallingTracker = SimultaneousCallingTracker.init(sContext);
                            sCellularNetworkValidator = CellularNetworkValidator.make(sContext, sFeatureFlags);
                            sPhoneSwitcher = TelephonyComponentFactory.getInstance().inject(PhoneSwitcher.class.getName()).makePhoneSwitcher(sPhoneConfigurationManager.getNumberOfModemsWithSimultaneousDataConnections(), sContext, Looper.myLooper(), featureFlags);
                            sProxyController = ProxyController.getInstance(context, featureFlags);
                            sIntentBroadcaster = IntentBroadcaster.getInstance(context);
                            sNotificationChannelController = new NotificationChannelController(context);
                            sTelephonyNetworkProvider = new TelephonyNetworkProvider(Looper.myLooper(), context, featureFlags);
                            telephonyComponentFactoryInject.initCarrierExpress();
                            TelephonyCommonUtilsStub.get().initMiuiTelephony(context, featureFlags, new Object[0]);
                            TelephonyCommonUtilsStub.get().init(context);
                            telephonyComponentFactoryInject.makeExtTelephonyClasses(context, sPhones, sCommandsInterfaces);
                        } catch (IOException unused) {
                            if (i > 3) {
                                throw new RuntimeException("PhoneFactory probably already running");
                            }
                            try {
                                Thread.sleep(2000L);
                            } catch (InterruptedException unused2) {
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void onMultiSimConfigChanged(Context context, int i) {
        synchronized (sLockProxyPhones) {
            try {
                Phone[] phoneArr = sPhones;
                int length = phoneArr.length;
                if (length == i) {
                    return;
                }
                if (length > i) {
                    return;
                }
                sPhones = (Phone[]) Arrays.copyOf(phoneArr, i);
                sCommandsInterfaces = (CommandsInterface[]) Arrays.copyOf(sCommandsInterfaces, i);
                TelephonyComponentFactory telephonyComponentFactoryInject = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName());
                while (length < i) {
                    sCommandsInterfaces[length] = telephonyComponentFactoryInject.makeRil(context, RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE), Integer.valueOf(length), sFeatureFlags);
                    sPhones[length] = createPhone(context, length);
                    if (context.getPackageManager().hasSystemFeature("android.hardware.telephony.ims")) {
                        sPhones[length].createImsPhone();
                    }
                    length++;
                }
                telephonyComponentFactoryInject.makeSmartDataSwitchAssistant(sContext, sPhones);
                telephonyComponentFactoryInject.makeDataHelper(sContext, sPhones, sFeatureFlags);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static Phone createPhone(Context context, int i) {
        Rlog.i("PhoneFactory", "Creating Phone phoneId = " + i);
        return TelephonyComponentFactory.getInstance().inject(GsmCdmaPhone.class.getName()).makePhone(context, sCommandsInterfaces[i], sPhoneNotifier, i, 1, TelephonyComponentFactory.getInstance(), sFeatureFlags);
    }

    public static Phone getDefaultPhone() {
        Phone phone;
        synchronized (sLockProxyPhones) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                phone = sPhone;
            } catch (Throwable th) {
                throw th;
            }
        }
        return phone;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x001b  */
    public static Phone getPhone(int i) {
        Phone phone;
        synchronized (sLockProxyPhones) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                if (i == Integer.MAX_VALUE) {
                    phone = sPhone;
                } else if (i >= 0) {
                    Phone[] phoneArr = sPhones;
                    if (i < phoneArr.length) {
                        phone = phoneArr[i];
                    } else {
                        phone = null;
                    }
                } else {
                    phone = null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return phone;
    }

    public static Phone[] getPhones() {
        Phone[] phoneArr;
        synchronized (sLockProxyPhones) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                phoneArr = sPhones;
            } catch (Throwable th) {
                throw th;
            }
        }
        return phoneArr;
    }

    public static TelephonyNetworkProvider getNetworkProvider() {
        return sTelephonyNetworkProvider;
    }

    public static int calculatePreferredNetworkType(int i) {
        if (getPhone(i) == null) {
            Rlog.d("PhoneFactory", "Invalid phoneId return default network mode ");
            return RadioAccessFamily.getRafFromNetworkType(RILConstants.PREFERRED_NETWORK_MODE);
        }
        int allowedNetworkTypes = (int) getPhone(i).getAllowedNetworkTypes(0);
        Rlog.d("PhoneFactory", "calculatePreferredNetworkType: phoneId = " + i + " networkType = " + allowedNetworkTypes);
        return allowedNetworkTypes;
    }

    public static int getDefaultSubscription() {
        return SubscriptionManagerService.getInstance().getDefaultSubId();
    }

    public static boolean isSMSPromptEnabled() {
        int i;
        try {
            i = Settings.Global.getInt(sContext.getContentResolver(), "multi_sim_sms_prompt");
        } catch (Settings.SettingNotFoundException unused) {
            Rlog.e("PhoneFactory", "Settings Exception Reading Dual Sim SMS Prompt Values");
            i = 0;
        }
        boolean z = i != 0;
        Rlog.d("PhoneFactory", "SMS Prompt option:" + z);
        return z;
    }

    public static Phone makeImsPhone(PhoneNotifier phoneNotifier, Phone phone) {
        return ImsPhoneFactory.makePhone(sContext, phoneNotifier, phone, sFeatureFlags);
    }

    public static SmsController getSmsController() {
        SmsController smsController;
        synchronized (sLockProxyPhones) {
            try {
                if (!sMadeDefaults) {
                    throw new IllegalStateException("Default phones haven't been made yet!");
                }
                smsController = sProxyController.getSmsController();
            } catch (Throwable th) {
                throw th;
            }
        }
        return smsController;
    }

    public static CommandsInterface[] getCommandsInterfaces() {
        CommandsInterface[] commandsInterfaceArr;
        synchronized (sLockProxyPhones) {
            commandsInterfaceArr = sCommandsInterfaces;
        }
        return commandsInterfaceArr;
    }

    public static void addLocalLog(String str, int i) {
        synchronized (sLocalLogs) {
            try {
                if (sLocalLogs.containsKey(str)) {
                    throw new IllegalArgumentException("key " + str + " already present");
                }
                sLocalLogs.put(str, new LocalLog(i));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void removeLocalLogForTest(String str) {
        synchronized (sLocalLogs) {
            sLocalLogs.remove(str);
        }
    }

    public static void localLog(String str, String str2) {
        synchronized (sLocalLogs) {
            try {
                if (!sLocalLogs.containsKey(str)) {
                    throw new IllegalArgumentException("key " + str + " not found");
                }
                ((LocalLog) sLocalLogs.get(str)).log(str2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static MetricsCollector getMetricsCollector() {
        return sMetricsCollector;
    }

    private static void reflectAndPrintFlagConfigs(IndentingPrintWriter indentingPrintWriter) {
        try {
            Method[] methods = FeatureFlags.class.getMethods();
            if (methods.length == 0) {
                indentingPrintWriter.println("NONE");
                return;
            }
            for (Method method : methods) {
                indentingPrintWriter.println(method.getName() + "-> " + method.invoke(sFeatureFlags, null));
            }
        } catch (Exception unused) {
            indentingPrintWriter.println("[ERROR]");
        }
    }

    public static void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("PhoneFactory:");
        indentingPrintWriter.println(" sMadeDefaults=" + sMadeDefaults);
        sPhoneSwitcher.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.println();
        Phone[] phones = getPhones();
        for (int i = 0; i < phones.length; i++) {
            indentingPrintWriter.increaseIndent();
            try {
                phones[i].dump(fileDescriptor, indentingPrintWriter, strArr);
                indentingPrintWriter.flush();
                indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
            } catch (Exception e) {
                indentingPrintWriter.println("Telephony DebugService: Could not get Phone[" + i + "] e=" + e);
            }
        }
        indentingPrintWriter.increaseIndent();
        sTelephonyNetworkProvider.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("UiccController:");
        indentingPrintWriter.increaseIndent();
        try {
            sUiccController.dump(fileDescriptor, indentingPrintWriter, strArr);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        indentingPrintWriter.flush();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.flush();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("sRadioHalCapabilities:");
        indentingPrintWriter.increaseIndent();
        try {
            sRadioHalCapabilities.dump(fileDescriptor, indentingPrintWriter, strArr);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        indentingPrintWriter.flush();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("LocalLogs:");
        indentingPrintWriter.increaseIndent();
        synchronized (sLocalLogs) {
            try {
                for (String str : sLocalLogs.keySet()) {
                    indentingPrintWriter.println(str);
                    indentingPrintWriter.increaseIndent();
                    ((LocalLog) sLocalLogs.get(str)).dump(fileDescriptor, indentingPrintWriter, strArr);
                    indentingPrintWriter.decreaseIndent();
                }
                indentingPrintWriter.flush();
            } catch (Throwable th) {
                throw th;
            }
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("SharedPreferences:");
        indentingPrintWriter.increaseIndent();
        try {
            if (sContext != null && TelephonyUtils.IS_DEBUGGABLE) {
                Map<String, ?> all = PreferenceManager.getDefaultSharedPreferences(sContext).getAll();
                for (String str2 : all.keySet()) {
                    indentingPrintWriter.println(((Object) str2) + " : " + all.get(str2));
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("DebugEvents:");
        indentingPrintWriter.increaseIndent();
        try {
            AnomalyReporter.dump(fileDescriptor, indentingPrintWriter, strArr);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        indentingPrintWriter.flush();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
        indentingPrintWriter.println("Flag Configurations:");
        indentingPrintWriter.increaseIndent();
        reflectAndPrintFlagConfigs(indentingPrintWriter);
        indentingPrintWriter.flush();
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
}
