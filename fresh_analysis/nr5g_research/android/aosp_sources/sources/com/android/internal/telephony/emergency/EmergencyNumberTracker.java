package com.android.internal.telephony.emergency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.PhoneNumberUtils;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperEmergencyNumberTracker;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.LocaleTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.metrics.EmergencyNumberStats;
import com.android.internal.telephony.nano.PersistAtomsProto$EmergencyNumbersInfo;
import com.android.internal.telephony.phonenumbers.ShortNumberInfo;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.phone.ecc.nano.ProtobufEccData$AllInfo;
import com.android.phone.ecc.nano.ProtobufEccData$CountryInfo;
import com.android.phone.ecc.nano.ProtobufEccData$EccInfo;
import com.android.telephony.Rlog;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyNumberTracker extends Handler {
    public static final int ADD_EMERGENCY_NUMBER_TEST_MODE = 1;
    public static boolean DBG = false;
    protected static final String EMERGENCY_NUMBER_DB_OTA_FILE_NAME = "emergency_number_db";
    protected static final String EMERGENCY_NUMBER_DB_OTA_FILE_PATH = "misc/emergencynumberdb/emergency_number_db";
    public static final int EVENT_OVERRIDE_OTA_EMERGENCY_NUMBER_DB_FILE_PATH = 6;
    public static final int EVENT_UPDATE_EMERGENCY_NUMBER_LIST_CARRIER_STUDIO = 7;
    public static final int EVENT_UPDATE_OTA_EMERGENCY_NUMBER_DB = 5;
    protected static final int INVALID_DATABASE_VERSION = -1;
    public static final int REMOVE_EMERGENCY_NUMBER_TEST_MODE = 2;
    public static final int RESET_EMERGENCY_NUMBER_TEST_MODE = 3;
    private static final String TAG = "EmergencyNumberTracker";
    private final CommandsInterface mCi;
    protected String mCountryIso;
    private String[] mEmergencyNumberPrefix;
    private final FeatureFlags mFeatureFlags;
    protected IHyperEmergencyNumberTracker mHyperEmergencyNumberTracker;
    public final Phone mPhone;
    private int mPhoneId;
    private Resources mResources;
    private ParcelFileDescriptor mOverridedOtaDbParcelFileDescriptor = null;
    private String mLastKnownEmergencyCountryIso = "";
    protected int mCurrentDatabaseVersion = -1;
    private int mCurrentOtaDatabaseVersion = -1;
    private Map mNormalRoutedNumbers = new ArrayMap();
    public boolean mIsCountrySetByAnotherSub = false;
    protected List<EmergencyNumber> mEmergencyNumberListFromDatabase = new ArrayList();
    protected List<EmergencyNumber> mEmergencyNumberListFromAssetDatabase = new ArrayList();
    protected List<EmergencyNumber> mEmergencyNumberListFromRadio = new ArrayList();
    private List mEmergencyNumberListWithPrefix = new ArrayList();
    private List mEmergencyNumberListFromTestMode = new ArrayList();
    private List mEmergencyNumberListFromCarrierStudio = new ArrayList();
    protected List<EmergencyNumber> mEmergencyNumberList = new ArrayList();
    private final LocalLog mEmergencyNumberListDatabaseLocalLog = new LocalLog(16);
    protected final LocalLog mEmergencyNumberListRadioLocalLog = new LocalLog(16);
    protected final LocalLog mEmergencyNumberListCarrierStudioLocalLog = new LocalLog(16);
    private final LocalLog mEmergencyNumberListPrefixLocalLog = new LocalLog(16);
    protected final LocalLog mEmergencyNumberListTestModeLocalLog = new LocalLog(16);
    protected final LocalLog mEmergencyNumberListLocalLog = new LocalLog(16);
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.emergency.EmergencyNumberTracker.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra;
            if (intent.getAction().equals("android.telephony.action.NETWORK_COUNTRY_CHANGED") && (intExtra = intent.getIntExtra("phone", -1)) == EmergencyNumberTracker.this.mPhone.getPhoneId()) {
                String stringExtra = intent.getStringExtra("android.telephony.extra.NETWORK_COUNTRY");
                EmergencyNumberTracker.this.logd("ACTION_NETWORK_COUNTRY_CHANGED: PhoneId: " + intExtra + " CountryIso: " + stringExtra);
                EmergencyNumberTracker emergencyNumberTracker = EmergencyNumberTracker.this;
                if (stringExtra == null) {
                    stringExtra = "";
                }
                emergencyNumberTracker.updateEmergencyCountryIsoAllPhones(stringExtra);
            }
        }
    };

    public boolean shouldDeterminingOfUrnsAndCategoriesWhileMergingIgnored() {
        return false;
    }

    public EmergencyNumberTracker(Phone phone, CommandsInterface commandsInterface, FeatureFlags featureFlags) {
        this.mResources = null;
        this.mHyperEmergencyNumberTracker = null;
        this.mEmergencyNumberPrefix = new String[0];
        Context context = phone.getContext();
        this.mPhone = phone;
        this.mCi = commandsInterface;
        this.mFeatureFlags = featureFlags;
        this.mResources = context.getResources();
        this.mPhoneId = phone.getPhoneId();
        IHyperEmergencyNumberTracker iHyperEmergencyNumberTracker = (IHyperEmergencyNumberTracker) HyperTelephonyFactory.getInstance().initModule(IHyperEmergencyNumberTracker.DEFAULT, phone);
        this.mHyperEmergencyNumberTracker = iHyperEmergencyNumberTracker;
        iHyperEmergencyNumberTracker.registerForEmergencyNumberListChanged(this, 7, null);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        if (carrierConfigManager != null) {
            PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(phone.getContext(), phone.getSubId(), new String[]{"emergency_number_prefix_string_array"});
            if (!carrierConfigSubset.isEmpty()) {
                this.mEmergencyNumberPrefix = carrierConfigSubset.getStringArray("emergency_number_prefix_string_array");
            }
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.emergency.EmergencyNumberTracker$$ExternalSyntheticLambda1
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.emergency.EmergencyNumberTracker$$ExternalSyntheticLambda2
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
            phone.getContext().registerReceiver(this.mIntentReceiver, new IntentFilter("android.telephony.action.NETWORK_COUNTRY_CHANGED"));
        } else {
            loge("CarrierConfigManager is null.");
        }
        initializeDatabaseEmergencyNumberList();
        commandsInterface.registerForEmergencyNumberList(this, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigUpdated(i);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                Object obj = asyncResult.result;
                if (obj == null) {
                    loge("EVENT_UNSOL_EMERGENCY_NUMBER_LIST: Result from RIL is null.");
                } else if (obj != null && asyncResult.exception == null) {
                    updateRadioEmergencyNumberListAndNotify((List) obj);
                } else {
                    loge("EVENT_UNSOL_EMERGENCY_NUMBER_LIST: Exception from RIL : " + asyncResult.exception);
                }
                break;
            case 2:
                Object obj2 = message.obj;
                if (obj2 == null) {
                    loge("EVENT_UPDATE_DB_COUNTRY_ISO_CHANGED: Result from UpdateCountryIso is null.");
                } else {
                    updateEmergencyNumberListDatabaseAndNotify((String) obj2);
                }
                break;
            case 3:
                Object obj3 = message.obj;
                if (obj3 == null && message.arg1 != 3) {
                    loge("EVENT_UPDATE_EMERGENCY_NUMBER_TEST_MODE: Result from executeEmergencyNumberTestModeCommand is null.");
                } else {
                    updateEmergencyNumberListTestModeAndNotify(message.arg1, (EmergencyNumber) obj3);
                }
                break;
            case 4:
                Object obj4 = message.obj;
                if (obj4 == null) {
                    loge("EVENT_UPDATE_EMERGENCY_NUMBER_PREFIX: Result from onCarrierConfigChanged is null.");
                } else {
                    updateEmergencyNumberPrefixAndNotify((String[]) obj4);
                }
                break;
            case 5:
                updateOtaEmergencyNumberListDatabaseAndNotify();
                break;
            case 6:
                Object obj5 = message.obj;
                if (obj5 == null) {
                    overrideOtaEmergencyNumberDbFilePath(null);
                } else {
                    overrideOtaEmergencyNumberDbFilePath((ParcelFileDescriptor) obj5);
                }
                break;
            case 7:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                Object obj6 = asyncResult2.result;
                if (obj6 == null) {
                    loge("EVENT_UPDATE_EMERGENCY_NUMBER_LIST_CARRIER_STUDIO: Result from CarrierStudioEcc is null.");
                } else if (obj6 != null && asyncResult2.exception == null) {
                    updateCarrierStudioEmergencyNumberListAndNotify((List) obj6);
                } else {
                    loge("EVENT_UPDATE_EMERGENCY_NUMBER_LIST_CARRIER_STUDIO: Exception from CarrierStudioEcc : " + asyncResult2.exception);
                }
                break;
        }
    }

    private boolean isAirplaneModeEnabled() {
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        return serviceStateTracker != null && serviceStateTracker.getServiceState().getState() == 3;
    }

    public boolean isSimAbsent() {
        for (Phone phone : PhoneFactory.getPhones()) {
            int slotIndex = SubscriptionManagerService.getInstance().getSlotIndex(phone.getSubId());
            if (slotIndex != -1) {
                logd("found sim in slotId: " + slotIndex + " subid: " + phone.getSubId());
                return false;
            }
        }
        return true;
    }

    private void initializeDatabaseEmergencyNumberList() {
        if (this.mCountryIso == null) {
            String lowerCase = getInitialCountryIso().toLowerCase(Locale.ROOT);
            updateEmergencyCountryIso(lowerCase);
            if (TextUtils.isEmpty(lowerCase) && isAirplaneModeEnabled()) {
                lowerCase = getCountryIsoForCachingDatabase();
            }
            cacheEmergencyDatabaseByCountry(lowerCase);
            TelephonyCommonUtilsStub.get().updateCustEmergencyNumber(this.mEmergencyNumberListFromDatabase, lowerCase);
        }
    }

    public void updateEmergencyCountryIsoAllPhones(String str) {
        this.mIsCountrySetByAnotherSub = false;
        updateEmergencyNumberDatabaseCountryChange(str);
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getPhoneId() != this.mPhone.getPhoneId() && phone.getEmergencyNumberTracker() != null) {
                EmergencyNumberTracker emergencyNumberTracker = phone.getEmergencyNumberTracker();
                if (!TextUtils.isEmpty(str) && (TextUtils.isEmpty(emergencyNumberTracker.getEmergencyCountryIso()) || emergencyNumberTracker.mIsCountrySetByAnotherSub)) {
                    emergencyNumberTracker.mIsCountrySetByAnotherSub = true;
                    emergencyNumberTracker.updateEmergencyNumberDatabaseCountryChange(str);
                }
            }
        }
    }

    private void onCarrierConfigUpdated(int i) {
        Phone phone = this.mPhone;
        if (phone != null) {
            if (i != phone.getPhoneId()) {
                return;
            }
            PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{"emergency_number_prefix_string_array"});
            if (carrierConfigSubset.isEmpty()) {
                return;
            }
            String[] stringArray = carrierConfigSubset.getStringArray("emergency_number_prefix_string_array");
            if (Arrays.equals(this.mEmergencyNumberPrefix, stringArray)) {
                return;
            }
            obtainMessage(4, stringArray).sendToTarget();
            return;
        }
        loge("onCarrierConfigurationChanged mPhone is null.");
    }

    private String getInitialCountryIso() {
        LocaleTracker localeTracker;
        Phone phone = this.mPhone;
        if (phone != null) {
            ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
            if (serviceStateTracker != null && (localeTracker = serviceStateTracker.getLocaleTracker()) != null) {
                return localeTracker.getCurrentCountry();
            }
            return "";
        }
        loge("getInitialCountryIso mPhone is null.");
        return "";
    }

    public void updateEmergencyNumberDatabaseCountryChange(String str) {
        obtainMessage(2, str).sendToTarget();
    }

    public void updateOtaEmergencyNumberDatabase() {
        obtainMessage(5).sendToTarget();
    }

    public void updateOtaEmergencyNumberDbFilePath(ParcelFileDescriptor parcelFileDescriptor) {
        obtainMessage(6, parcelFileDescriptor).sendToTarget();
    }

    public void resetOtaEmergencyNumberDbFilePath() {
        obtainMessage(6, null).sendToTarget();
    }

    private EmergencyNumber convertEmergencyNumberFromEccInfo(ProtobufEccData$EccInfo protobufEccData$EccInfo, String str, int i) {
        int i2;
        String strTrim = protobufEccData$EccInfo.phoneNumber.trim();
        if (strTrim.isEmpty()) {
            loge("EccInfo has empty phone number.");
            return null;
        }
        int i3 = 0;
        for (int i4 : protobufEccData$EccInfo.types) {
            switch (i4) {
                case 1:
                    i2 = i3 | 1;
                    break;
                case 2:
                    i2 = i3 | 2;
                    break;
                case 3:
                    i2 = i3 | 4;
                    break;
                case 4:
                    i2 = i3 | 8;
                    break;
                case 5:
                    i2 = i3 | 16;
                    break;
                case 6:
                    i2 = i3 | 32;
                    break;
                case 7:
                    i2 = i3 | 64;
                    break;
                default:
                    continue;
                    break;
            }
            i3 = i2;
        }
        return new EmergencyNumber(strTrim, str, "", i3, new ArrayList(), 16, i);
    }

    private int getRoutingInfoFromDB(ProtobufEccData$EccInfo protobufEccData$EccInfo, Map map) {
        int i = protobufEccData$EccInfo.routing;
        int i2 = i != 1 ? i != 2 ? 0 : 2 : 1;
        String strTrim = protobufEccData$EccInfo.phoneNumber.trim();
        if (strTrim.isEmpty()) {
            loge("EccInfo has empty phone number.");
            return i2;
        }
        if (protobufEccData$EccInfo.routing != 2) {
            return i2;
        }
        String[] strArr = protobufEccData$EccInfo.normalRoutingMncs;
        if (strArr.length == 0 || strArr[0].length() <= 0) {
            return 2;
        }
        for (String str : protobufEccData$EccInfo.normalRoutingMncs) {
            if (!map.containsKey(str)) {
                ArraySet arraySet = new ArraySet();
                arraySet.add(strTrim);
                map.put(str, arraySet);
            } else {
                Set set = (Set) map.get(str);
                if (!set.contains(strTrim)) {
                    set.add(strTrim);
                }
            }
        }
        logd("Normal routed mncs with phoneNumbers:" + map);
        return 0;
    }

    protected void cacheEmergencyDatabaseByCountry(String str) {
        if (TelephonyCommonUtilsStub.get().isRemoveEccData()) {
            logd("Miui remove ecc data");
            return;
        }
        ArrayMap arrayMap = new ArrayMap();
        ArrayList arrayList = new ArrayList();
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.mPhone.getContext().getAssets().open("eccdata"));
            try {
                BufferedInputStream eccdataInputStream = TelephonyCommonUtilsStub.get().getEccdataInputStream(bufferedInputStream);
                try {
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(eccdataInputStream);
                    try {
                        ProtobufEccData$AllInfo from = ProtobufEccData$AllInfo.parseFrom(readInputStreamToByteArray(gZIPInputStream));
                        int i = from.revision;
                        logd(str + " asset emergency database is loaded. Ver: " + i + " Phone Id: " + this.mPhone.getPhoneId() + " countryIso: " + str);
                        for (ProtobufEccData$CountryInfo protobufEccData$CountryInfo : from.countries) {
                            if (protobufEccData$CountryInfo.isoCode.equals(str.toUpperCase(Locale.ROOT))) {
                                for (ProtobufEccData$EccInfo protobufEccData$EccInfo : protobufEccData$CountryInfo.eccs) {
                                    arrayList.add(convertEmergencyNumberFromEccInfo(protobufEccData$EccInfo, str, !shouldEmergencyNumberRoutingFromDbBeIgnored() ? getRoutingInfoFromDB(protobufEccData$EccInfo, arrayMap) : 0));
                                }
                            }
                        }
                        EmergencyNumber.mergeSameNumbersInEmergencyNumberList(arrayList);
                        gZIPInputStream.close();
                        if (eccdataInputStream != null) {
                            eccdataInputStream.close();
                        }
                        bufferedInputStream.close();
                        TelephonyCommonUtilsStub.get().updateEccdata(arrayList);
                        int iCacheOtaEmergencyNumberDatabase = cacheOtaEmergencyNumberDatabase();
                        this.mCurrentOtaDatabaseVersion = iCacheOtaEmergencyNumberDatabase;
                        if (iCacheOtaEmergencyNumberDatabase == -1 && i == -1) {
                            loge("No database available. Phone Id: " + this.mPhone.getPhoneId());
                        } else {
                            if (i > iCacheOtaEmergencyNumberDatabase) {
                                logd("Using Asset Emergency database. Version: " + i);
                                this.mCurrentDatabaseVersion = i;
                                this.mEmergencyNumberListFromDatabase = arrayList;
                                this.mNormalRoutedNumbers.clear();
                                this.mNormalRoutedNumbers = arrayMap;
                                return;
                            }
                            logd("Using Ota Emergency database. Version: " + this.mCurrentOtaDatabaseVersion);
                        }
                    } catch (Throwable th) {
                        try {
                            gZIPInputStream.close();
                            throw th;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            throw th;
                        }
                    }
                } catch (Throwable th3) {
                    if (eccdataInputStream == null) {
                        throw th3;
                    }
                    try {
                        eccdataInputStream.close();
                        throw th3;
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                        throw th3;
                    }
                    logw("Cache asset emergency database failure: " + e);
                }
            } catch (Throwable th5) {
                try {
                    bufferedInputStream.close();
                    throw th5;
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                    throw th5;
                }
            }
        } catch (IOException e) {
            logw("Cache asset emergency database failure: " + e);
        }
    }

    protected int cacheOtaEmergencyNumberDatabase() {
        File absoluteFile;
        Throwable th;
        Throwable th2;
        int routingInfoFromDB;
        ArrayMap arrayMap = new ArrayMap();
        ArrayList arrayList = new ArrayList();
        ParcelFileDescriptor parcelFileDescriptor = this.mOverridedOtaDbParcelFileDescriptor;
        if (parcelFileDescriptor == null) {
            absoluteFile = new File(Environment.getDataDirectory(), EMERGENCY_NUMBER_DB_OTA_FILE_PATH);
        } else {
            try {
                absoluteFile = ParcelFileDescriptor.getFile(parcelFileDescriptor.getFileDescriptor()).getAbsoluteFile();
            } catch (IOException e) {
                loge("Cache ota emergency database IOException: " + e);
                return -1;
            }
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(absoluteFile);
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                try {
                    try {
                        GZIPInputStream gZIPInputStream = new GZIPInputStream(bufferedInputStream);
                        try {
                            ProtobufEccData$AllInfo from = ProtobufEccData$AllInfo.parseFrom(readInputStreamToByteArray(gZIPInputStream));
                            String lastKnownEmergencyCountryIso = getLastKnownEmergencyCountryIso();
                            logd(lastKnownEmergencyCountryIso + " ota emergency database is loaded. Ver: -1");
                            int i = from.revision;
                            ProtobufEccData$CountryInfo[] protobufEccData$CountryInfoArr = from.countries;
                            int length = protobufEccData$CountryInfoArr.length;
                            int i2 = 0;
                            while (i2 < length) {
                                ProtobufEccData$CountryInfo protobufEccData$CountryInfo = protobufEccData$CountryInfoArr[i2];
                                if (protobufEccData$CountryInfo.isoCode.equals(lastKnownEmergencyCountryIso.toUpperCase(Locale.ROOT))) {
                                    ProtobufEccData$EccInfo[] protobufEccData$EccInfoArr = protobufEccData$CountryInfo.eccs;
                                    int length2 = protobufEccData$EccInfoArr.length;
                                    int i3 = 0;
                                    while (i3 < length2) {
                                        ProtobufEccData$EccInfo protobufEccData$EccInfo = protobufEccData$EccInfoArr[i3];
                                        if (shouldEmergencyNumberRoutingFromDbBeIgnored()) {
                                            routingInfoFromDB = 0;
                                        } else {
                                            try {
                                                routingInfoFromDB = getRoutingInfoFromDB(protobufEccData$EccInfo, arrayMap);
                                            } catch (Throwable th3) {
                                                th2 = th3;
                                                bufferedInputStream = bufferedInputStream;
                                                try {
                                                    gZIPInputStream.close();
                                                    throw th2;
                                                } catch (Throwable th4) {
                                                    th2.addSuppressed(th4);
                                                    throw th2;
                                                }
                                            }
                                        }
                                        try {
                                            arrayList.add(convertEmergencyNumberFromEccInfo(protobufEccData$EccInfo, lastKnownEmergencyCountryIso, routingInfoFromDB));
                                            i3++;
                                            bufferedInputStream = bufferedInputStream;
                                        } catch (Throwable th5) {
                                            th = th5;
                                            th2 = th;
                                            gZIPInputStream.close();
                                            throw th2;
                                        }
                                    }
                                }
                                i2++;
                                bufferedInputStream = bufferedInputStream;
                            }
                            bufferedInputStream = bufferedInputStream;
                            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(arrayList);
                            gZIPInputStream.close();
                            bufferedInputStream.close();
                            fileInputStream.close();
                            if (i != -1 && this.mCurrentDatabaseVersion < i) {
                                this.mCurrentDatabaseVersion = i;
                                this.mEmergencyNumberListFromDatabase = arrayList;
                                this.mNormalRoutedNumbers.clear();
                                this.mNormalRoutedNumbers = arrayMap;
                            }
                            return i;
                        } catch (Throwable th6) {
                            th = th6;
                            bufferedInputStream = bufferedInputStream;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        th = th;
                        try {
                            bufferedInputStream.close();
                            throw th;
                        } catch (Throwable th8) {
                            th.addSuppressed(th8);
                            throw th;
                        }
                    }
                } catch (Throwable th9) {
                    th = th9;
                    bufferedInputStream = bufferedInputStream;
                    th = th;
                    bufferedInputStream.close();
                    throw th;
                }
            } catch (Throwable th10) {
                try {
                    fileInputStream.close();
                    throw th10;
                } catch (Throwable th11) {
                    th10.addSuppressed(th11);
                    throw th10;
                }
            }
        } catch (IOException e2) {
            loge("Cache ota emergency database IOException: " + e2);
            return -1;
        }
    }

    protected static byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            int i = inputStream.read(bArr, 0, 16384);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    protected void updateRadioEmergencyNumberListAndNotify(List<EmergencyNumber> list) {
        Collections.sort(list);
        logd("updateRadioEmergencyNumberListAndNotify(): receiving " + list);
        if (list.equals(this.mEmergencyNumberListFromRadio)) {
            return;
        }
        try {
            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(list);
            this.mEmergencyNumberListFromRadio = list;
            if (!DBG) {
                this.mEmergencyNumberListRadioLocalLog.log("updateRadioEmergencyNumberList:" + list);
            }
            updateEmergencyNumberList();
            if (!DBG) {
                this.mEmergencyNumberListLocalLog.log("updateRadioEmergencyNumberListAndNotify:" + this.mEmergencyNumberList);
            }
            notifyEmergencyNumberList();
        } catch (NullPointerException e) {
            loge("updateRadioEmergencyNumberListAndNotify() Phone already destroyed: " + e + " EmergencyNumberList not notified");
        }
    }

    private void updateCarrierStudioEmergencyNumberListAndNotify(List list) {
        Collections.sort(list);
        logd("updateCarrierStudioEmergencyNumberListAndNotify(): receiving " + list);
        if (list.equals(this.mEmergencyNumberListFromCarrierStudio)) {
            return;
        }
        try {
            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(list);
            this.mEmergencyNumberListFromCarrierStudio = list;
            if (!DBG) {
                this.mEmergencyNumberListCarrierStudioLocalLog.log("updateCarrierStudioEmergencyNumberList:" + list);
            }
            updateEmergencyNumberList();
            if (!DBG) {
                this.mEmergencyNumberListLocalLog.log("updateCarrierStudioEmergencyNumberListAndNotify:" + this.mEmergencyNumberList);
            }
            notifyEmergencyNumberList();
        } catch (NullPointerException e) {
            loge("updateCarrierStudioEmergencyNumberListAndNotify() Phone already destroyed: " + e + " EmergencyNumberList not notified");
        }
    }

    private void updateEmergencyNumberListDatabaseAndNotify(String str) {
        logd("updateEmergencyNumberListDatabaseAndNotify(): receiving countryIso: " + str);
        updateEmergencyCountryIso(str.toLowerCase(Locale.ROOT));
        if (TextUtils.isEmpty(str)) {
            str = getCountryIsoForCachingDatabase();
            logd("updateEmergencyNumberListDatabaseAndNotify(): using cached APM country " + str);
        }
        cacheEmergencyDatabaseByCountry(str);
        if (!DBG) {
            this.mEmergencyNumberListDatabaseLocalLog.log("updateEmergencyNumberListDatabaseAndNotify:" + this.mEmergencyNumberListFromDatabase);
        }
        TelephonyCommonUtilsStub.get().updateCustEmergencyNumber(this.mEmergencyNumberListFromDatabase, str);
        updateEmergencyNumberList();
        if (!DBG) {
            this.mEmergencyNumberListLocalLog.log("updateEmergencyNumberListDatabaseAndNotify:" + this.mEmergencyNumberList);
        }
        notifyEmergencyNumberList();
    }

    private void overrideOtaEmergencyNumberDbFilePath(ParcelFileDescriptor parcelFileDescriptor) {
        logd("overrideOtaEmergencyNumberDbFilePath:" + parcelFileDescriptor);
        this.mOverridedOtaDbParcelFileDescriptor = parcelFileDescriptor;
    }

    private void updateOtaEmergencyNumberListDatabaseAndNotify() {
        logd("updateOtaEmergencyNumberListDatabaseAndNotify(): receiving Emegency Number database OTA update");
        int iCacheOtaEmergencyNumberDatabase = cacheOtaEmergencyNumberDatabase();
        this.mCurrentOtaDatabaseVersion = iCacheOtaEmergencyNumberDatabase;
        if (iCacheOtaEmergencyNumberDatabase != -1) {
            if (!DBG) {
                this.mEmergencyNumberListDatabaseLocalLog.log("updateOtaEmergencyNumberListDatabaseAndNotify:" + this.mEmergencyNumberListFromDatabase);
            }
            updateEmergencyNumberList();
            if (!DBG) {
                this.mEmergencyNumberListLocalLog.log("updateOtaEmergencyNumberListDatabaseAndNotify:" + this.mEmergencyNumberList);
            }
            notifyEmergencyNumberList();
        }
    }

    private void updateEmergencyNumberPrefixAndNotify(String[] strArr) {
        logd("updateEmergencyNumberPrefixAndNotify(): receiving emergencyNumberPrefix: " + Arrays.toString(strArr));
        this.mEmergencyNumberPrefix = strArr;
        updateEmergencyNumberList();
        if (!DBG) {
            this.mEmergencyNumberListLocalLog.log("updateEmergencyNumberPrefixAndNotify:" + this.mEmergencyNumberList);
        }
        notifyEmergencyNumberList();
    }

    protected void notifyEmergencyNumberList() {
        try {
            if (getEmergencyNumberList() != null) {
                this.mPhone.notifyEmergencyNumberList();
                logd("notifyEmergencyNumberList(): notified");
            }
        } catch (NullPointerException e) {
            loge("notifyEmergencyNumberList(): failure: Phone already destroyed: " + e);
        }
    }

    protected void updateEmergencyNumberList() {
        ArrayList arrayList = new ArrayList(this.mEmergencyNumberListFromDatabase);
        arrayList.addAll(this.mHyperEmergencyNumberTracker.filterDeprecatedEmergencyNumbers(this.mEmergencyNumberListFromRadio));
        arrayList.addAll(this.mEmergencyNumberListFromCarrierStudio);
        this.mEmergencyNumberListWithPrefix.clear();
        if (this.mEmergencyNumberPrefix.length != 0) {
            this.mEmergencyNumberListWithPrefix.addAll(getEmergencyNumberListWithPrefix(this.mEmergencyNumberListFromRadio));
            this.mEmergencyNumberListWithPrefix.addAll(getEmergencyNumberListWithPrefix(this.mEmergencyNumberListFromCarrierStudio));
            this.mEmergencyNumberListWithPrefix.addAll(getEmergencyNumberListWithPrefix(this.mEmergencyNumberListFromDatabase));
        }
        if (!DBG) {
            this.mEmergencyNumberListPrefixLocalLog.log("updateEmergencyNumberList:" + this.mEmergencyNumberListWithPrefix);
        }
        arrayList.addAll(this.mEmergencyNumberListWithPrefix);
        arrayList.addAll(this.mEmergencyNumberListFromTestMode);
        if (shouldDeterminingOfUrnsAndCategoriesWhileMergingIgnored()) {
            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(arrayList);
        } else {
            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(arrayList, true);
        }
        this.mEmergencyNumberList = arrayList;
    }

    public List<EmergencyNumber> getEmergencyNumberList() {
        List<EmergencyNumber> listUnmodifiableList;
        if (!this.mEmergencyNumberListFromRadio.isEmpty() || !this.mEmergencyNumberListFromCarrierStudio.isEmpty()) {
            listUnmodifiableList = Collections.unmodifiableList(this.mEmergencyNumberList);
        } else {
            listUnmodifiableList = getEmergencyNumberListFromEccListDatabaseAndTest();
        }
        return shouldAdjustForRouting() ? adjustRoutingForEmergencyNumbers(listUnmodifiableList) : listUnmodifiableList;
    }

    private boolean shouldAdjustForRouting() {
        return (shouldEmergencyNumberRoutingFromDbBeIgnored() || this.mNormalRoutedNumbers.isEmpty()) ? false : true;
    }

    private List adjustRoutingForEmergencyNumbers(List list) {
        int i;
        String str;
        CellIdentity currentCellIdentity = this.mPhone.getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return list;
        }
        String mncString = currentCellIdentity.getMncString();
        Set set = (Set) this.mNormalRoutedNumbers.get(mncString);
        ArraySet arraySet = new ArraySet();
        if (set != null && !set.isEmpty()) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Set setAddPrefixToEmergencyNumber = addPrefixToEmergencyNumber((String) it.next());
                if (setAddPrefixToEmergencyNumber != null && !setAddPrefixToEmergencyNumber.isEmpty()) {
                    arraySet.addAll(setAddPrefixToEmergencyNumber);
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            EmergencyNumber emergencyNumber = (EmergencyNumber) it2.next();
            int emergencyCallRouting = emergencyNumber.getEmergencyCallRouting();
            String mnc = emergencyNumber.getMnc();
            if (!emergencyNumber.isFromSources(16)) {
                i = emergencyCallRouting;
                str = mnc;
            } else if ((set == null || !set.contains(emergencyNumber.getNumber())) && !arraySet.contains(emergencyNumber.getNumber())) {
                if (emergencyCallRouting == 0) {
                    emergencyCallRouting = 1;
                }
                i = emergencyCallRouting;
                str = mnc;
            } else {
                logd("adjustRoutingForEmergencyNumbers for number" + emergencyNumber.getNumber());
                str = mncString;
                i = 2;
            }
            arrayList.add(new EmergencyNumber(emergencyNumber.getNumber(), emergencyNumber.getCountryIso(), str, emergencyNumber.getEmergencyServiceCategoryBitmask(), emergencyNumber.getEmergencyUrns(), emergencyNumber.getEmergencyNumberSourceBitmask(), i));
        }
        return arrayList;
    }

    private Set addPrefixToEmergencyNumber(String str) {
        ArraySet arraySet = new ArraySet();
        for (String str2 : this.mEmergencyNumberPrefix) {
            if (!str.startsWith(str2)) {
                arraySet.add(str2 + str);
            }
        }
        return arraySet;
    }

    public boolean isEmergencyNumber(String str) {
        if (str == null || PhoneNumberUtils.isUriNumber(str)) {
            return false;
        }
        String strExtractNetworkPortionAlt = PhoneNumberUtils.extractNetworkPortionAlt(str);
        if (!this.mEmergencyNumberListFromRadio.isEmpty() || !this.mEmergencyNumberListFromCarrierStudio.isEmpty()) {
            Iterator<EmergencyNumber> it = this.mEmergencyNumberList.iterator();
            while (it.hasNext()) {
                if (it.next().getNumber().equals(strExtractNetworkPortionAlt)) {
                    logd("Found in mEmergencyNumberList");
                    return true;
                }
            }
            return false;
        }
        boolean zIsEmergencyNumberFromEccList = isEmergencyNumberFromEccList(strExtractNetworkPortionAlt);
        boolean zIsEmergencyNumberFromDatabase = isEmergencyNumberFromDatabase(strExtractNetworkPortionAlt);
        boolean zIsEmergencyNumberForTest = isEmergencyNumberForTest(strExtractNetworkPortionAlt);
        logd("Search results - inRilEccList:" + zIsEmergencyNumberFromEccList + " inEmergencyNumberDb:" + zIsEmergencyNumberFromDatabase + " inEmergencyNumberTestList: " + zIsEmergencyNumberForTest);
        return zIsEmergencyNumberFromEccList || zIsEmergencyNumberFromDatabase || zIsEmergencyNumberForTest;
    }

    public EmergencyNumber getEmergencyNumber(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        for (EmergencyNumber emergencyNumber : getEmergencyNumberList()) {
            if (emergencyNumber.getNumber().equals(strStripSeparators)) {
                return emergencyNumber;
            }
        }
        return null;
    }

    public List<EmergencyNumber> getEmergencyNumbers(String str) {
        final String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        return getEmergencyNumberList().stream().filter(new Predicate() { // from class: com.android.internal.telephony.emergency.EmergencyNumberTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((EmergencyNumber) obj).getNumber().equals(strStripSeparators);
            }
        }).toList();
    }

    public int getEmergencyServiceCategories(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        for (EmergencyNumber emergencyNumber : getEmergencyNumberList()) {
            if (emergencyNumber.getNumber().equals(strStripSeparators) && (emergencyNumber.isFromSources(1) || emergencyNumber.isFromSources(2))) {
                return emergencyNumber.getEmergencyServiceCategoryBitmask();
            }
        }
        return 0;
    }

    public int getEmergencyCallRouting(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        for (EmergencyNumber emergencyNumber : getEmergencyNumberList()) {
            if (emergencyNumber.getNumber().equals(strStripSeparators) && emergencyNumber.isFromSources(16)) {
                return emergencyNumber.getEmergencyCallRouting();
            }
        }
        return 0;
    }

    public String getEmergencyCountryIso() {
        return this.mCountryIso;
    }

    public String getLastKnownEmergencyCountryIso() {
        return this.mLastKnownEmergencyCountryIso;
    }

    private String getCountryIsoForCachingDatabase() {
        LocaleTracker localeTracker;
        ServiceStateTracker serviceStateTracker = this.mPhone.getServiceStateTracker();
        if (serviceStateTracker != null && (localeTracker = serviceStateTracker.getLocaleTracker()) != null) {
            return localeTracker.getLastKnownCountryIso();
        }
        return "";
    }

    public int getEmergencyNumberDbVersion() {
        return this.mCurrentDatabaseVersion;
    }

    public int getEmergencyNumberOtaDbVersion() {
        return this.mCurrentOtaDatabaseVersion;
    }

    private synchronized void updateEmergencyCountryIso(String str) {
        try {
            this.mCountryIso = str;
            if (!TextUtils.isEmpty(str)) {
                this.mLastKnownEmergencyCountryIso = this.mCountryIso;
            }
            this.mCurrentDatabaseVersion = -1;
        } catch (Throwable th) {
            throw th;
        }
    }

    private List getEmergencyNumberListFromEccList() {
        ArrayList arrayList = new ArrayList();
        for (String str : (isSimAbsent() ? "112,911,000,08,110,118,119,999" : "112,911").split(",")) {
            arrayList.add(getLabeledEmergencyNumberForEcclist(str));
        }
        if (this.mEmergencyNumberPrefix.length != 0) {
            arrayList.addAll(getEmergencyNumberListWithPrefix(arrayList));
        }
        EmergencyNumber.mergeSameNumbersInEmergencyNumberList(arrayList);
        return arrayList;
    }

    private List getEmergencyNumberListWithPrefix(List list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                EmergencyNumber emergencyNumber = (EmergencyNumber) it.next();
                Set setAddPrefixToEmergencyNumber = addPrefixToEmergencyNumber(emergencyNumber.getNumber());
                if (setAddPrefixToEmergencyNumber != null && !setAddPrefixToEmergencyNumber.isEmpty()) {
                    Iterator it2 = setAddPrefixToEmergencyNumber.iterator();
                    while (it2.hasNext()) {
                        arrayList.add(new EmergencyNumber((String) it2.next(), emergencyNumber.getCountryIso(), emergencyNumber.getMnc(), emergencyNumber.getEmergencyServiceCategoryBitmask(), emergencyNumber.getEmergencyUrns(), emergencyNumber.getEmergencyNumberSourceBitmask(), emergencyNumber.getEmergencyCallRouting()));
                    }
                }
            }
        }
        return arrayList;
    }

    private boolean isEmergencyNumberForTest(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        Iterator it = this.mEmergencyNumberListFromTestMode.iterator();
        while (it.hasNext()) {
            if (((EmergencyNumber) it.next()).getNumber().equals(strStripSeparators)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmergencyNumberFromDatabase(String str) {
        if (this.mEmergencyNumberListFromDatabase.isEmpty()) {
            return false;
        }
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        Iterator<EmergencyNumber> it = this.mEmergencyNumberListFromDatabase.iterator();
        while (it.hasNext()) {
            if (it.next().getNumber().equals(strStripSeparators)) {
                return true;
            }
        }
        Iterator it2 = getEmergencyNumberListWithPrefix(this.mEmergencyNumberListFromDatabase).iterator();
        while (it2.hasNext()) {
            if (((EmergencyNumber) it2.next()).getNumber().equals(strStripSeparators)) {
                return true;
            }
        }
        return false;
    }

    private EmergencyNumber getLabeledEmergencyNumberForEcclist(String str) {
        String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
        for (EmergencyNumber emergencyNumber : this.mEmergencyNumberListFromDatabase) {
            if (emergencyNumber.getNumber().equals(strStripSeparators)) {
                return new EmergencyNumber(strStripSeparators, getLastKnownEmergencyCountryIso().toLowerCase(Locale.ROOT), "", emergencyNumber.getEmergencyServiceCategoryBitmask(), new ArrayList(), 16, emergencyNumber.getEmergencyCallRouting());
            }
        }
        return new EmergencyNumber(strStripSeparators, "", "", 0, new ArrayList(), 0, 0);
    }

    private boolean isEmergencyNumberFromEccList(String str) {
        if (str == null) {
            return false;
        }
        String lastKnownEmergencyCountryIso = getLastKnownEmergencyCountryIso();
        logd("country:" + lastKnownEmergencyCountryIso);
        logd("System property doesn't provide any emergency numbers. Use embedded logic for determining ones.");
        for (String str2 : (isSimAbsent() ? "112,911,000,08,110,118,119,999" : "112,911").split(",")) {
            if (str.equals(str2)) {
                return true;
            }
            for (String str3 : this.mEmergencyNumberPrefix) {
                if (str.equals(str3 + str2)) {
                    return true;
                }
            }
        }
        if (isSimAbsent() && lastKnownEmergencyCountryIso != null) {
            ShortNumberInfo shortNumberInfo = ShortNumberInfo.getInstance();
            if (shortNumberInfo.isEmergencyNumber(str, lastKnownEmergencyCountryIso.toUpperCase(Locale.ROOT))) {
                return true;
            }
            for (String str4 : this.mEmergencyNumberPrefix) {
                if (shortNumberInfo.isEmergencyNumber(str4 + str, lastKnownEmergencyCountryIso.toUpperCase(Locale.ROOT))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void executeEmergencyNumberTestModeCommand(int i, EmergencyNumber emergencyNumber) {
        obtainMessage(3, i, 0, emergencyNumber).sendToTarget();
    }

    private void updateEmergencyNumberListTestModeAndNotify(int i, EmergencyNumber emergencyNumber) {
        if (i == 1) {
            if (!isEmergencyNumber(emergencyNumber.getNumber())) {
                this.mEmergencyNumberListFromTestMode.add(emergencyNumber);
            }
        } else if (i == 3) {
            this.mEmergencyNumberListFromTestMode.clear();
        } else if (i == 2) {
            this.mEmergencyNumberListFromTestMode.remove(emergencyNumber);
        } else {
            loge("updateEmergencyNumberListTestModeAndNotify: Unexpected action in test mode.");
            return;
        }
        if (!DBG) {
            this.mEmergencyNumberListTestModeLocalLog.log("updateEmergencyNumberListTestModeAndNotify:" + this.mEmergencyNumberListFromTestMode);
        }
        updateEmergencyNumberList();
        if (!DBG) {
            this.mEmergencyNumberListLocalLog.log("updateEmergencyNumberListTestModeAndNotify:" + this.mEmergencyNumberList);
        }
        notifyEmergencyNumberList();
    }

    private List getEmergencyNumberListFromEccListDatabaseAndTest() {
        List emergencyNumberListFromEccList = getEmergencyNumberListFromEccList();
        if (!this.mEmergencyNumberListFromDatabase.isEmpty()) {
            loge("getEmergencyNumberListFromEccListDatabaseAndTest: radio indication is unavailable in 1.4 HAL.");
            emergencyNumberListFromEccList.addAll(this.mEmergencyNumberListFromDatabase);
            emergencyNumberListFromEccList.addAll(getEmergencyNumberListWithPrefix(this.mEmergencyNumberListFromDatabase));
        }
        emergencyNumberListFromEccList.addAll(getEmergencyNumberListTestMode());
        if (shouldDeterminingOfUrnsAndCategoriesWhileMergingIgnored()) {
            EmergencyNumber.mergeSameNumbersInEmergencyNumberList(emergencyNumberListFromEccList);
            return emergencyNumberListFromEccList;
        }
        EmergencyNumber.mergeSameNumbersInEmergencyNumberList(emergencyNumberListFromEccList, true);
        return emergencyNumberListFromEccList;
    }

    public List<EmergencyNumber> getEmergencyNumberListTestMode() {
        return Collections.unmodifiableList(this.mEmergencyNumberListFromTestMode);
    }

    public List<EmergencyNumber> getRadioEmergencyNumberList() {
        return new ArrayList(this.mEmergencyNumberListFromRadio);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d(TAG, "[" + this.mPhoneId + "]" + str);
    }

    private void logw(String str) {
        Rlog.w(TAG, "[" + this.mPhoneId + "]" + str);
    }

    private void loge(String str) {
        Rlog.e(TAG, "[" + this.mPhoneId + "]" + str);
    }

    public boolean shouldEmergencyNumberRoutingFromDbBeIgnored() {
        return this.mResources.getBoolean(17892118);
    }

    public PersistAtomsProto$EmergencyNumbersInfo[] getEmergencyNumbersProtoArray() {
        int iMax = Math.max(0, getEmergencyNumberOtaDbVersion());
        int iMax2 = Math.max(0, getEmergencyNumberDbVersion());
        boolean zShouldEmergencyNumberRoutingFromDbBeIgnored = shouldEmergencyNumberRoutingFromDbBeIgnored();
        List<EmergencyNumber> emergencyNumberList = getEmergencyNumberList();
        logd("log emergency number list=" + emergencyNumberList + " for otaVersion=" + iMax + ", assetVersion=" + iMax2 + ", isDbRoutingIgnored=" + zShouldEmergencyNumberRoutingFromDbBeIgnored);
        return EmergencyNumberStats.getInstance().convertEmergencyNumbersListToProto(emergencyNumberList, iMax2, iMax, zShouldEmergencyNumberRoutingFromDbBeIgnored);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println(" Country Iso:" + getEmergencyCountryIso());
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println(" Database Version:" + getEmergencyNumberDbVersion());
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListDatabaseLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListDatabaseLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListRadioLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListRadioLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListCarrierStudioLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListCarrierStudioLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListPrefixLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListPrefixLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListTestModeLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListTestModeLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("mEmergencyNumberListLocalLog (valid >= 1.4 HAL):");
        indentingPrintWriter.increaseIndent();
        this.mEmergencyNumberListLocalLog.dump(fileDescriptor, printWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.println("Emergency Number List for Phone(" + this.mPhone.getPhoneId() + ")");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(getEmergencyNumberList());
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println(" ========================================= ");
        indentingPrintWriter.flush();
    }
}
