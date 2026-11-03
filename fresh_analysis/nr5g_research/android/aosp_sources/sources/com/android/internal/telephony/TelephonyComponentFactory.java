package com.android.internal.telephony;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.NetworkAgentConfig;
import android.net.NetworkProvider;
import android.net.NetworkScore;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStatVfs;
import android.telephony.data.DataProfile;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsCall;
import com.android.ims.ImsFeatureBinderRepository;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cat.CommandParamsFactory;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.data.AccessNetworksManager;
import com.android.internal.telephony.data.AutoDataSwitchController;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataRetryManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.DataSettingsManager;
import com.android.internal.telephony.data.DataStallRecoveryManager;
import com.android.internal.telephony.data.LinkBandwidthEstimator;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.data.TelephonyNetworkAgent;
import com.android.internal.telephony.data.TelephonyNetworkProvider;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.emergency.RadioOnHelper;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.imsphone.ImsExternalCallTracker;
import com.android.internal.telephony.imsphone.ImsNrSaModeHandler;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.nitz.NitzStateMachineImpl;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.SatelliteModemInterface;
import com.android.internal.telephony.security.CellularIdentifierDisclosureNotifier;
import com.android.internal.telephony.security.CellularNetworkSecuritySafetySource;
import com.android.internal.telephony.security.NullCipherNotifier;
import com.android.internal.telephony.subscription.SubscriptionDatabaseManager;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.CsimFileHandler;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimFileHandler;
import com.android.internal.telephony.uicc.IsimUiccRecords;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMFileHandler;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.UsimFileHandler;
import com.android.telephony.Rlog;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyComponentFactory {
    public static final String LOG_TAG = "TelephonyComponentFactory";
    private static final String TAG = "TelephonyComponentFactory";
    private static TelephonyComponentFactory sInstance;
    private InjectedComponents mInjectedComponents;
    protected final TelephonyFacade mTelephonyFacade = new TelephonyFacade();

    public void initCarrierExpress() {
    }

    public void initGwsdService(Context context) {
    }

    public void initRadioManager(Context context, int i, CommandsInterface[] commandsInterfaceArr) {
    }

    public void makeDataHelper(Context context, Phone[] phoneArr, FeatureFlags featureFlags) {
    }

    public void makeSmartDataSwitchAssistant(Context context, Phone[] phoneArr) {
    }

    public void makeSuppServManager(Context context, Phone[] phoneArr) {
    }

    public void makeUiccPhoneBookController() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class InjectedComponents {
        private final Set mComponentNames;
        private TelephonyComponentFactory mInjectedInstance;
        private String mJarPath;
        private String mPackageName;

        private InjectedComponents() {
            this.mComponentNames = new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getValidatedPaths() {
            Rlog.w(TelephonyComponentFactory.TAG, "getValidatedPaths: " + this.mPackageName + " ," + this.mJarPath);
            if (TextUtils.isEmpty(this.mPackageName) || TextUtils.isEmpty(this.mJarPath)) {
                return null;
            }
            Rlog.d(TelephonyComponentFactory.TAG, "XiaoMi debug mJarPath: " + this.mJarPath);
            String str = this.mJarPath;
            String str2 = File.pathSeparator;
            return (String) Arrays.stream(str.split(str2)).filter(new Predicate() { // from class: com.android.internal.telephony.TelephonyComponentFactory$InjectedComponents$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TelephonyComponentFactory.InjectedComponents.$r8$lambda$PhD0GXKQlqCwhPMYrNUrLm7UuJo((String) obj);
                }
            }).filter(new Predicate() { // from class: com.android.internal.telephony.TelephonyComponentFactory$InjectedComponents$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return TelephonyComponentFactory.InjectedComponents.m456$r8$lambda$Jt2wAgFw04TBtMaDT1EHpHMvew((String) obj);
                }
            }).distinct().collect(Collectors.joining(str2));
        }

        public static /* synthetic */ boolean $r8$lambda$PhD0GXKQlqCwhPMYrNUrLm7UuJo(String str) {
            return str.startsWith("/system/") || str.startsWith("/product/") || str.startsWith("/system_ext/");
        }

        /* JADX INFO: renamed from: $r8$lambda$Jt2wAgFw04TBtMaDT-1EHpHMvew, reason: not valid java name */
        public static /* synthetic */ boolean m456$r8$lambda$Jt2wAgFw04TBtMaDT1EHpHMvew(String str) {
            try {
                StructStatVfs structStatVfsStatvfs = Os.statvfs(str);
                String str2 = TelephonyComponentFactory.TAG;
                StringBuilder sb = new StringBuilder("StructStatVfs: ");
                sb.append(structStatVfsStatvfs.f_flag);
                sb.append(" ,");
                int i = OsConstants.ST_RDONLY;
                sb.append(i);
                Rlog.w(str2, sb.toString());
                return (structStatVfsStatvfs.f_flag & ((long) i)) != 0 || Build.isDebuggable();
            } catch (ErrnoException e) {
                Rlog.w(TelephonyComponentFactory.TAG, "Injection jar is not protected , path: " + str + e.getMessage());
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void makeInjectedInstance() {
            String validatedPaths = getValidatedPaths();
            Rlog.d(TelephonyComponentFactory.TAG, "validated paths: " + validatedPaths);
            if (TextUtils.isEmpty(validatedPaths)) {
                return;
            }
            try {
                this.mInjectedInstance = (TelephonyComponentFactory) new PathClassLoader(validatedPaths, ClassLoader.getSystemClassLoader()).loadClass(this.mPackageName).newInstance();
            } catch (ClassNotFoundException e) {
                Rlog.e(TelephonyComponentFactory.TAG, "failed: " + e.getMessage());
            } catch (IllegalAccessException | InstantiationException e2) {
                Rlog.e(TelephonyComponentFactory.TAG, "injection failed: " + e2.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isComponentInjected(String str) {
            if (this.mInjectedInstance == null) {
                return false;
            }
            return this.mComponentNames.contains(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void parseXml(XmlPullParser xmlPullParser) {
            parseXmlByTag(xmlPullParser, false, new Consumer() { // from class: com.android.internal.telephony.TelephonyComponentFactory$InjectedComponents$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$parseXml$2((XmlPullParser) obj);
                }
            }, "injection");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseXml$2(XmlPullParser xmlPullParser) {
            setAttributes(xmlPullParser);
            parseInjection(xmlPullParser);
        }

        private void parseInjection(XmlPullParser xmlPullParser) {
            parseXmlByTag(xmlPullParser, false, new Consumer() { // from class: com.android.internal.telephony.TelephonyComponentFactory$InjectedComponents$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$parseInjection$3((XmlPullParser) obj);
                }
            }, "components");
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: parseComponents, reason: merged with bridge method [inline-methods] */
        public void lambda$parseInjection$3(XmlPullParser xmlPullParser) {
            parseXmlByTag(xmlPullParser, true, new Consumer() { // from class: com.android.internal.telephony.TelephonyComponentFactory$InjectedComponents$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$parseComponents$4((XmlPullParser) obj);
                }
            }, "component");
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: parseComponent, reason: merged with bridge method [inline-methods] */
        public void lambda$parseComponents$4(XmlPullParser xmlPullParser) {
            try {
                int depth = xmlPullParser.getDepth();
                while (true) {
                    int next = xmlPullParser.next();
                    if (next == 1) {
                        return;
                    }
                    if (next == 3 && xmlPullParser.getDepth() <= depth) {
                        return;
                    }
                    if (next == 4) {
                        this.mComponentNames.add(xmlPullParser.getText());
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                Rlog.e(TelephonyComponentFactory.TAG, "Failed to parse the component.", e);
            }
        }

        private void parseXmlByTag(XmlPullParser xmlPullParser, boolean z, Consumer consumer, String str) {
            try {
                int depth = xmlPullParser.getDepth();
                while (true) {
                    int next = xmlPullParser.next();
                    if (next == 1) {
                        return;
                    }
                    if (next == 3 && xmlPullParser.getDepth() <= depth) {
                        return;
                    }
                    if (next == 2 && str.equals(xmlPullParser.getName())) {
                        consumer.accept(xmlPullParser);
                        if (!z) {
                            return;
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                Rlog.e(TelephonyComponentFactory.TAG, "Failed to parse or find tag: " + str, e);
            }
        }

        private void setAttributes(XmlPullParser xmlPullParser) {
            for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                String attributeName = xmlPullParser.getAttributeName(i);
                String attributeValue = xmlPullParser.getAttributeValue(i);
                if ("package".equals(attributeName)) {
                    this.mPackageName = attributeValue;
                } else if ("jar".equals(attributeName)) {
                    this.mJarPath = attributeValue;
                }
            }
        }
    }

    public static TelephonyComponentFactory getInstance() {
        if (sInstance == null) {
            sInstance = new TelephonyComponentFactory();
        }
        return sInstance;
    }

    public void injectTheComponentFactory(XmlResourceParser xmlResourceParser) {
        if (this.mInjectedComponents != null) {
            Rlog.d(TAG, "Already injected.");
            return;
        }
        if (xmlResourceParser != null) {
            InjectedComponents injectedComponents = new InjectedComponents();
            this.mInjectedComponents = injectedComponents;
            injectedComponents.parseXml(xmlResourceParser);
            this.mInjectedComponents.makeInjectedInstance();
            boolean zIsEmpty = TextUtils.isEmpty(this.mInjectedComponents.getValidatedPaths());
            String str = TAG;
            StringBuilder sb = new StringBuilder("Total components injected: ");
            sb.append(!zIsEmpty ? this.mInjectedComponents.mComponentNames.size() : 0);
            Rlog.d(str, sb.toString());
        }
    }

    public TelephonyComponentFactory inject(String str) {
        InjectedComponents injectedComponents = this.mInjectedComponents;
        if (injectedComponents != null && injectedComponents.isComponentInjected(str)) {
            return this.mInjectedComponents.mInjectedInstance;
        }
        return sInstance;
    }

    public GsmCdmaCallTracker makeGsmCdmaCallTracker(GsmCdmaPhone gsmCdmaPhone, FeatureFlags featureFlags) {
        return new GsmCdmaCallTracker(gsmCdmaPhone, featureFlags);
    }

    public SmsStorageMonitor makeSmsStorageMonitor(Phone phone, FeatureFlags featureFlags) {
        return new SmsStorageMonitor(phone, featureFlags);
    }

    public SmsUsageMonitor makeSmsUsageMonitor(Context context, FeatureFlags featureFlags) {
        Rlog.d(LOG_TAG, "makeSmsUsageMonitor");
        return new SmsUsageMonitor(context, featureFlags);
    }

    public ServiceStateTracker makeServiceStateTracker(GsmCdmaPhone gsmCdmaPhone, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        return new ServiceStateTracker(gsmCdmaPhone, commandsInterface, featureFlags);
    }

    public EmergencyNumberTracker makeEmergencyNumberTracker(Phone phone, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        return new EmergencyNumberTracker(phone, commandsInterface, featureFlags);
    }

    public NitzStateMachine makeNitzStateMachine(GsmCdmaPhone gsmCdmaPhone) {
        return NitzStateMachineImpl.createInstance(gsmCdmaPhone);
    }

    public SimActivationTracker makeSimActivationTracker(Phone phone) {
        return new SimActivationTracker(phone);
    }

    public CarrierSignalAgent makeCarrierSignalAgent(Phone phone) {
        return new CarrierSignalAgent(phone);
    }

    public CarrierActionAgent makeCarrierActionAgent(Phone phone) {
        return new CarrierActionAgent(phone);
    }

    public CarrierResolver makeCarrierResolver(Phone phone, FeatureFlags featureFlags) {
        return new CarrierResolver(phone, featureFlags);
    }

    public IccPhoneBookInterfaceManager makeIccPhoneBookInterfaceManager(Phone phone) {
        return new IccPhoneBookInterfaceManager(phone);
    }

    public IccSmsInterfaceManager makeIccSmsInterfaceManager(Phone phone, FeatureFlags featureFlags) {
        return new IccSmsInterfaceManager(phone, featureFlags);
    }

    public SmsDispatchersController makeSmsDispatchersController(Phone phone, FeatureFlags featureFlags) {
        Rlog.d(LOG_TAG, "makeSmsDispatchersController");
        return new SmsDispatchersController(phone, phone.mSmsStorageMonitor, phone.mSmsUsageMonitor, featureFlags);
    }

    public UiccProfile makeUiccProfile(Context context, CommandsInterface commandsInterface, IccCardStatus iccCardStatus, int i, UiccCard uiccCard, Object obj, FeatureFlags featureFlags) {
        return new UiccProfile(context, commandsInterface, iccCardStatus, i, uiccCard, obj, featureFlags);
    }

    public WspTypeDecoder makeWspTypeDecoder(byte[] bArr) {
        return new WspTypeDecoder(bArr);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] bArr, long j, int i, boolean z, boolean z2, String str, String str2, String str3, boolean z3, int i2, int i3) {
        return new InboundSmsTracker(context, bArr, j, i, z, z2, str, str2, str3, z3, i2, i3);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] bArr, long j, int i, boolean z, String str, String str2, int i2, int i3, int i4, boolean z2, String str3, boolean z3, int i5, int i6) {
        return new InboundSmsTracker(context, bArr, j, i, z, str, str2, i2, i3, i4, z2, str3, z3, i5, i6);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, Cursor cursor, boolean z) {
        return new InboundSmsTracker(context, cursor, z);
    }

    public SmsHeader makeSmsHeader() {
        return new SmsHeader();
    }

    public ImsSmsDispatcher makeImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        return new ImsSmsDispatcher(phone, smsDispatchersController, new ImsSmsDispatcher.FeatureConnectorFactory() { // from class: com.android.internal.telephony.TelephonyComponentFactory$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.ImsSmsDispatcher.FeatureConnectorFactory
            public final FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor) {
                return ImsManager.getConnector(context, i, str, listener, executor);
            }
        }, featureFlags);
    }

    public CdmaSMSDispatcher makeCdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        return new CdmaSMSDispatcher(phone, smsDispatchersController, featureFlags);
    }

    public GsmSMSDispatcher makeGsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler, FeatureFlags featureFlags) {
        return new GsmSMSDispatcher(phone, smsDispatchersController, gsmInboundSmsHandler, featureFlags);
    }

    public void makeSmsBroadcastUndelivered(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler, FeatureFlags featureFlags) {
        SmsBroadcastUndelivered.initialize(context, gsmInboundSmsHandler, cdmaInboundSmsHandler, featureFlags);
    }

    public WapPushOverSms makeWapPushOverSms(Context context, FeatureFlags featureFlags) {
        return new WapPushOverSms(context, featureFlags);
    }

    public GsmInboundSmsHandler makeGsmInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, Looper looper, FeatureFlags featureFlags) {
        return GsmInboundSmsHandler.makeInboundSmsHandler(context, smsStorageMonitor, phone, looper, featureFlags);
    }

    public CdmaInboundSmsHandler makeCdmaInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, CdmaSMSDispatcher cdmaSMSDispatcher, Looper looper, FeatureFlags featureFlags) {
        return new CdmaInboundSmsHandler(context, smsStorageMonitor, phone, cdmaSMSDispatcher, looper, featureFlags);
    }

    public SmsDispatchersController makeSmsDispatchersController(Phone phone, SmsStorageMonitor smsStorageMonitor, SmsUsageMonitor smsUsageMonitor, FeatureFlags featureFlags) {
        return new SmsDispatchersController(phone, phone.mSmsStorageMonitor, phone.mSmsUsageMonitor, featureFlags);
    }

    public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone imsPhone) {
        return makeImsPhoneCallTracker(imsPhone, new FeatureFlagsImpl());
    }

    public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone imsPhone, FeatureFlags featureFlags) {
        return new ImsPhoneCallTracker(imsPhone, new ImsPhoneCallTracker.ConnectorFactory() { // from class: com.android.internal.telephony.TelephonyComponentFactory$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.imsphone.ImsPhoneCallTracker.ConnectorFactory
            public final FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor) {
                return ImsManager.getConnector(context, i, str, listener, executor);
            }
        }, featureFlags);
    }

    public ImsExternalCallTracker makeImsExternalCallTracker(ImsPhone imsPhone) {
        return new ImsExternalCallTracker(imsPhone, imsPhone.getContext().getMainExecutor());
    }

    public ImsNrSaModeHandler makeImsNrSaModeHandler(ImsPhone imsPhone) {
        return new ImsNrSaModeHandler(imsPhone, imsPhone.getLooper());
    }

    public AppSmsManager makeAppSmsManager(Context context) {
        return new AppSmsManager(context);
    }

    public DeviceStateMonitor makeDeviceStateMonitor(Phone phone, FeatureFlags featureFlags) {
        return new DeviceStateMonitor(phone, featureFlags);
    }

    public AccessNetworksManager makeAccessNetworksManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new AccessNetworksManager(phone, looper, featureFlags);
    }

    public LocaleTracker makeLocaleTracker(Phone phone, NitzStateMachine nitzStateMachine, Looper looper, FeatureFlags featureFlags) {
        return new LocaleTracker(phone, nitzStateMachine, looper, featureFlags);
    }

    public Phone makePhone(Context context, CommandsInterface commandsInterface, PhoneNotifier phoneNotifier, int i, int i2, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        return new GsmCdmaPhone(context, commandsInterface, phoneNotifier, i, i2, telephonyComponentFactory, featureFlags);
    }

    public PhoneSwitcher makePhoneSwitcher(int i, Context context, Looper looper, FeatureFlags featureFlags) {
        return PhoneSwitcher.make(i, context, looper, featureFlags);
    }

    public DisplayInfoController makeDisplayInfoController(Phone phone, FeatureFlags featureFlags) {
        return new DisplayInfoController(phone, featureFlags);
    }

    public MultiSimSettingController initMultiSimSettingController(Context context, FeatureFlags featureFlags) {
        return MultiSimSettingController.init(context, featureFlags);
    }

    public RIL makeRil(Context context, int i, Integer num, FeatureFlags featureFlags) {
        return new RIL(context, i, num, featureFlags);
    }

    public SignalStrengthController makeSignalStrengthController(GsmCdmaPhone gsmCdmaPhone) {
        return new SignalStrengthController(gsmCdmaPhone);
    }

    public ProxyController makeProxyController(Context context, FeatureFlags featureFlags) {
        return new ProxyController(context, featureFlags);
    }

    public LinkBandwidthEstimator makeLinkBandwidthEstimator(Phone phone, Looper looper) {
        return new LinkBandwidthEstimator(phone, looper, this.mTelephonyFacade);
    }

    public RIL makeRIL(Context context, int i, Integer num, FeatureFlags featureFlags) {
        Rlog.d(LOG_TAG, "makeRIL");
        return new RIL(context, i, num, featureFlags);
    }

    public void makeExtTelephonyClasses(Context context, Phone[] phoneArr, CommandsInterface[] commandsInterfaceArr) {
        Rlog.d(LOG_TAG, "makeExtTelephonyClasses");
    }

    public CarrierInfoManager makeCarrierInfoManager(Phone phone) {
        Rlog.i(TAG, " makeCarrierInfoManager ");
        return new CarrierInfoManager();
    }

    public DataNetworkController makeDataNetworkController(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new DataNetworkController(phone, looper, featureFlags);
    }

    public UiccController makeUiccController(Context context, FeatureFlags featureFlags) {
        return new UiccController(context, featureFlags);
    }

    public UiccSlot makeUiccSlot(Context context, boolean z, FeatureFlags featureFlags) {
        return new UiccSlot(context, z, featureFlags);
    }

    public DataServiceManager makeDataServiceManager(Phone phone, Looper looper, int i, FeatureFlags featureFlags) {
        return new DataServiceManager(phone, looper, i, featureFlags);
    }

    public DataProfileManager makeDataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, FeatureFlags featureFlags, DataProfileManager.DataProfileManagerCallback dataProfileManagerCallback) {
        return new DataProfileManager(phone, dataNetworkController, dataServiceManager, looper, featureFlags, dataProfileManagerCallback);
    }

    public DataSettingsManager makeDataSettingsManager(Phone phone, DataNetworkController dataNetworkController, SparseArray<DataServiceManager> sparseArray, FeatureFlags featureFlags, Looper looper, DataSettingsManager.DataSettingsManagerCallback dataSettingsManagerCallback) {
        return new DataSettingsManager(phone, dataNetworkController, sparseArray, featureFlags, looper, dataSettingsManagerCallback);
    }

    public CellularNetworkSecuritySafetySource makeCellularNetworkSecuritySafetySource(Context context) {
        return CellularNetworkSecuritySafetySource.getInstance(context);
    }

    public DataConfigManager makeDataConfigManager(Phone phone, Looper looper, FeatureFlags featureFlags) {
        return new DataConfigManager(phone, looper, featureFlags);
    }

    public DataRetryManager makeDataRetryManager(Phone phone, DataNetworkController dataNetworkController, SparseArray<DataServiceManager> sparseArray, Looper looper, FeatureFlags featureFlags, DataRetryManager.DataRetryManagerCallback dataRetryManagerCallback) {
        Rlog.i(TAG, "makeDataRetryManager");
        return new DataRetryManager(phone, dataNetworkController, sparseArray, looper, featureFlags, dataRetryManagerCallback);
    }

    public TelephonyNetworkAgent makeTelephonyNetworkAgent(Phone phone, Looper looper, DataNetwork dataNetwork, NetworkScore networkScore, NetworkAgentConfig networkAgentConfig, NetworkProvider networkProvider, TelephonyNetworkAgent.TelephonyNetworkAgentCallback telephonyNetworkAgentCallback) {
        Rlog.i(TAG, "makeTelephonyNetworkAgent");
        return new TelephonyNetworkAgent(phone, looper, dataNetwork, networkScore, networkAgentConfig, networkProvider, telephonyNetworkAgentCallback);
    }

    public TelephonyNetworkProvider makeTelephonyNetworkProvider(Looper looper, Context context, FeatureFlags featureFlags) {
        Rlog.i(TAG, "make TelephonyNetworkProvider");
        return new TelephonyNetworkProvider(looper, context, featureFlags);
    }

    public SubscriptionManagerService makeSubscriptionManagerService(Context context, Looper looper, FeatureFlags featureFlags) {
        return new SubscriptionManagerService(context, looper, featureFlags);
    }

    public CellularIdentifierDisclosureNotifier makeIdentifierDisclosureNotifier(CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        return CellularIdentifierDisclosureNotifier.getInstance(cellularNetworkSecuritySafetySource);
    }

    public DataNetwork makeDataNetwork(Phone phone, FeatureFlags featureFlags, Looper looper, SparseArray<DataServiceManager> sparseArray, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int i, boolean z, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetwork.DataNetworkCallback dataNetworkCallback) {
        return new DataNetwork(phone, featureFlags, looper, sparseArray, dataProfile, networkRequestList, i, z, dataAllowedReason, dataNetworkCallback);
    }

    public DefaultPhoneNotifier makeDefaultPhoneNotifier(Context context, FeatureFlags featureFlags) {
        return new DefaultPhoneNotifier(context, featureFlags);
    }

    public ImsPhone makeImsPhone(Context context, PhoneNotifier phoneNotifier, Phone phone, FeatureFlags featureFlags) {
        return new ImsPhone(context, phoneNotifier, phone, featureFlags);
    }

    public CatService makeCatService(CommandsInterface commandsInterface, UiccCardApplication uiccCardApplication, IccRecords iccRecords, Context context, IccFileHandler iccFileHandler, UiccProfile uiccProfile, int i, Looper looper, FeatureFlags featureFlags) {
        return new CatService(commandsInterface, uiccCardApplication, iccRecords, context, iccFileHandler, uiccProfile, i, looper, featureFlags);
    }

    public RilMessageDecoder makeRilMessageDecoder(Handler handler, IccFileHandler iccFileHandler, Context context, int i) {
        return new RilMessageDecoder(handler, iccFileHandler, context);
    }

    public CommandParamsFactory makeCommandParamsFactory(RilMessageDecoder rilMessageDecoder, IccFileHandler iccFileHandler, Context context) {
        return new CommandParamsFactory(rilMessageDecoder, iccFileHandler, context);
    }

    public NullCipherNotifier makeNullCipherNotifier(CellularNetworkSecuritySafetySource cellularNetworkSecuritySafetySource) {
        return NullCipherNotifier.getInstance(cellularNetworkSecuritySafetySource);
    }

    public AutoDataSwitchController makeAutoDataSwitchController(Context context, Looper looper, PhoneSwitcher phoneSwitcher, FeatureFlags featureFlags, AutoDataSwitchController.AutoDataSwitchControllerCallback autoDataSwitchControllerCallback) {
        return new AutoDataSwitchController(context, looper, phoneSwitcher, featureFlags, autoDataSwitchControllerCallback);
    }

    public DataStallRecoveryManager makeDataStallRecoveryManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, FeatureFlags featureFlags, Looper looper, DataStallRecoveryManager.DataStallRecoveryManagerCallback dataStallRecoveryManagerCallback) {
        Rlog.i(TAG, "makeDataStallRecoveryManager");
        return new DataStallRecoveryManager(phone, dataNetworkController, dataServiceManager, featureFlags, looper, dataStallRecoveryManagerCallback);
    }

    public SubscriptionDatabaseManager makeSubscriptionDatabaseManager(Context context, Looper looper, FeatureFlags featureFlags, SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback subscriptionDatabaseManagerCallback) {
        Rlog.i(TAG, "makeSubscriptionDatabaseManager");
        return new SubscriptionDatabaseManager(context, looper, featureFlags, subscriptionDatabaseManagerCallback);
    }

    public RadioConfig makeRadioConfig(Context context, HalVersion halVersion) {
        Rlog.d(TAG, "makeRadioConfig");
        return new RadioConfig(context, halVersion);
    }

    public SatelliteModemInterface makeSatelliteInterface(Context context, SatelliteController satelliteController, Looper looper, FeatureFlags featureFlags) {
        Rlog.d(TAG, "makeSatelliteController");
        return new SatelliteModemInterface(context, satelliteController, looper, featureFlags);
    }

    public SatelliteController makeSatelliteController(Context context, Looper looper, FeatureFlags featureFlags) {
        Rlog.d(TAG, "makeSatelliteController");
        return new SatelliteController(context, looper, featureFlags);
    }

    public RadioOnHelper makeRadioOnHelper(Context context) {
        Rlog.d(TAG, "make RadioOnHelper");
        return new RadioOnHelper(context);
    }

    public SIMRecords makeSIMRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new SIMRecords(uiccCardApplication, context, commandsInterface);
    }

    public RuimRecords makeRuimRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface) {
        return new RuimRecords(uiccCardApplication, context, commandsInterface);
    }

    public IsimUiccRecords makeIsimUiccRecords(UiccCardApplication uiccCardApplication, Context context, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        return new IsimUiccRecords(uiccCardApplication, context, commandsInterface, featureFlags);
    }

    public SIMFileHandler makeSIMFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new SIMFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public UsimFileHandler makeUsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new UsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public IsimFileHandler makeIsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new IsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public ImsPhoneMmiCode makeImsPhoneMmiCode(ImsPhone imsPhone, FeatureFlags featureFlags) {
        return new ImsPhoneMmiCode(imsPhone, featureFlags);
    }

    public ImsResolver makeImsResolver(Context context, String str, String str2, int i, ImsFeatureBinderRepository imsFeatureBinderRepository, Looper looper) {
        return new ImsResolver(context, str, str2, i, imsFeatureBinderRepository, looper);
    }

    public NetworkTypeController makeNetworkTypeController(Phone phone, DisplayInfoController displayInfoController, FeatureFlags featureFlags) {
        return new NetworkTypeController(phone, displayInfoController, featureFlags);
    }

    public IccFileHandler makeSimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new SIMFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public IccFileHandler makeCsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface) {
        return new CsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public IccFileHandler makeUsimFileHandler(UiccCardApplication uiccCardApplication, String str, CommandsInterface commandsInterface, Context context) {
        return new UsimFileHandler(uiccCardApplication, str, commandsInterface);
    }

    public AdnRecordCache makeAdnRecordCache(IccFileHandler iccFileHandler) {
        return new AdnRecordCache(iccFileHandler);
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, ImsCall imsCall, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        return new ImsPhoneConnection(phone, imsCall, imsPhoneCallTracker, imsPhoneCall, z);
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, String str, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z, boolean z2, ImsPhone.ImsDialArgs imsDialArgs) {
        return new ImsPhoneConnection(phone, str, imsPhoneCallTracker, imsPhoneCall, z, z2, imsDialArgs);
    }

    public ImsPhoneConnection makeImsPhoneConnection(Phone phone, String[] strArr, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        return new ImsPhoneConnection(phone, strArr, imsPhoneCallTracker, imsPhoneCall, z);
    }

    public void injectTheCustComponentFactory(XmlResourceParser xmlResourceParser) {
        Rlog.d(TAG, "no custParser.");
    }

    public UniTelephonyInjectionFactory makeUniTelephonyInjectionFactory() {
        return new UniTelephonyInjectionFactory();
    }

    public PhoneConfigurationManager makePhoneConfigurationManager(Context context, FeatureFlags featureFlags) {
        return new PhoneConfigurationManager(context, featureFlags);
    }
}
