package com.android.internal.telephony.satellite;

import android.R;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.PersistableBundle;
import android.telephony.CellIdentity;
import android.telephony.DropBoxManagerLoggerBackend;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PersistentLogger;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.satellite.AntennaPosition;
import android.telephony.satellite.NtnSignalStrength;
import android.telephony.satellite.PointingInfo;
import android.telephony.satellite.SatelliteCapabilities;
import android.telephony.satellite.SatelliteDatagram;
import android.telephony.satellite.SatelliteInfo;
import android.telephony.satellite.SatelliteManager;
import android.telephony.satellite.stub.EarfcnRange;
import android.telephony.satellite.stub.NetworkInfo;
import android.telephony.satellite.stub.PrioritizedNetworkScanRequest;
import android.telephony.satellite.stub.SatelliteModemEnableRequestAttributes;
import android.telephony.satellite.stub.SatellitePosition;
import android.telephony.satellite.stub.SatelliteSubscriptionInfo;
import android.telephony.satellite.stub.SystemSelectionSpecifier;
import android.telephony.satellite.stub.UUID;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SimultaneousCallingTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.TelephonyUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteServiceUtils {
    public static int fromSessionConnectionMode(int i) {
        if (i != 0) {
            return i != 1 ? 0 : 2;
        }
        return 1;
    }

    public static int fromSupportedConnectionMode(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 0 : 3;
        }
        return 2;
    }

    public static boolean isLastSosMessage(int i) {
        return i == 4 || i == 5;
    }

    public static boolean isSosMessage(int i) {
        return i == 1 || i == 4 || i == 5;
    }

    public static String carrierRoamingNtnConnectTypeToString(int i) {
        if (i == 0) {
            return "AUTOMATIC";
        }
        if (i == 1) {
            return "MANUAL";
        }
        if (i == 2) {
            return "HYBRID";
        }
        return "Unknown(" + i + ")";
    }

    public static int fromSatelliteRadioTechnology(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i == 3) {
            return 4;
        }
        if (i == 4) {
            return 5;
        }
        if (i == 5) {
            return 6;
        }
        loge("Received invalid radio technology: " + i);
        return 0;
    }

    public static int fromSatelliteError(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 11:
                return 12;
            case 12:
                return 13;
            case 13:
                return 14;
            case 14:
                return 15;
            case 15:
                return 16;
            case 16:
                return 17;
            case 17:
                return 18;
            case 18:
                return 19;
            default:
                loge("Received invalid satellite service error: " + i);
                return 3;
        }
    }

    public static int fromSatelliteModemState(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            default:
                loge("Received invalid modem state: " + i);
                return -1;
        }
    }

    public static SatelliteCapabilities fromSatelliteCapabilities(android.telephony.satellite.stub.SatelliteCapabilities satelliteCapabilities) {
        if (satelliteCapabilities == null) {
            return null;
        }
        int[] iArr = satelliteCapabilities.supportedRadioTechnologies;
        if (iArr == null) {
            iArr = new int[0];
        }
        HashMap map = new HashMap();
        int[] iArr2 = satelliteCapabilities.antennaPositionKeys;
        AntennaPosition[] antennaPositionArr = satelliteCapabilities.antennaPositionValues;
        if (iArr2 != null && antennaPositionArr != null && iArr2.length == antennaPositionArr.length) {
            for (int i = 0; i < iArr2.length; i++) {
                map.put(Integer.valueOf(iArr2[i]), antennaPositionArr[i]);
            }
        }
        return new SatelliteCapabilities((Set) Arrays.stream(iArr).map(new IntUnaryOperator() { // from class: com.android.internal.telephony.satellite.SatelliteServiceUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.IntUnaryOperator
            public final int applyAsInt(int i2) {
                return SatelliteServiceUtils.fromSatelliteRadioTechnology(i2);
            }
        }).boxed().collect(Collectors.toSet()), satelliteCapabilities.isPointingRequired, satelliteCapabilities.maxBytesPerOutgoingDatagram, map);
    }

    public static PointingInfo fromPointingInfo(android.telephony.satellite.stub.PointingInfo pointingInfo) {
        if (pointingInfo == null) {
            return null;
        }
        return new PointingInfo(pointingInfo.satelliteAzimuth, pointingInfo.satelliteElevation);
    }

    public static SatelliteDatagram fromSatelliteDatagram(android.telephony.satellite.stub.SatelliteDatagram satelliteDatagram) {
        if (satelliteDatagram == null) {
            return null;
        }
        byte[] bArr = satelliteDatagram.data;
        if (bArr == null) {
            bArr = new byte[0];
        }
        return new SatelliteDatagram(bArr);
    }

    public static NtnSignalStrength fromNtnSignalStrength(android.telephony.satellite.stub.NtnSignalStrength ntnSignalStrength) {
        return new NtnSignalStrength(ntnSignalStrength.signalStrengthLevel);
    }

    public static android.telephony.satellite.stub.SatelliteDatagram toSatelliteDatagram(SatelliteDatagram satelliteDatagram) {
        android.telephony.satellite.stub.SatelliteDatagram satelliteDatagram2 = new android.telephony.satellite.stub.SatelliteDatagram();
        satelliteDatagram2.data = satelliteDatagram.getSatelliteDatagram();
        return satelliteDatagram2;
    }

    public static SatelliteSubscriptionInfo toSatelliteSubscriptionInfo(android.telephony.satellite.SatelliteSubscriptionInfo satelliteSubscriptionInfo) {
        SatelliteSubscriptionInfo satelliteSubscriptionInfo2 = new SatelliteSubscriptionInfo();
        satelliteSubscriptionInfo2.iccId = satelliteSubscriptionInfo.getIccId();
        satelliteSubscriptionInfo2.niddApn = satelliteSubscriptionInfo.getNiddApn();
        return satelliteSubscriptionInfo2;
    }

    public static SatelliteModemEnableRequestAttributes toSatelliteModemEnableRequestAttributes(android.telephony.satellite.SatelliteModemEnableRequestAttributes satelliteModemEnableRequestAttributes) {
        SatelliteModemEnableRequestAttributes satelliteModemEnableRequestAttributes2 = new SatelliteModemEnableRequestAttributes();
        satelliteModemEnableRequestAttributes2.isEnabled = satelliteModemEnableRequestAttributes.isEnabled();
        satelliteModemEnableRequestAttributes2.isDemoMode = satelliteModemEnableRequestAttributes.isForDemoMode();
        satelliteModemEnableRequestAttributes2.isEmergencyMode = satelliteModemEnableRequestAttributes.isForEmergencyMode();
        satelliteModemEnableRequestAttributes2.satelliteSubscriptionInfo = toSatelliteSubscriptionInfo(satelliteModemEnableRequestAttributes.getSatelliteSubscriptionInfo());
        return satelliteModemEnableRequestAttributes2;
    }

    public static int getSatelliteError(AsyncResult asyncResult, String str) {
        int i;
        int iConvertCommandExceptionErrorToSatelliteError;
        SatelliteManager.SatelliteException satelliteException = asyncResult.exception;
        if (satelliteException == null) {
            i = 0;
        } else {
            if (satelliteException instanceof SatelliteManager.SatelliteException) {
                iConvertCommandExceptionErrorToSatelliteError = satelliteException.getErrorCode();
                loge(str + " SatelliteException: " + asyncResult.exception);
            } else if (satelliteException instanceof CommandException) {
                iConvertCommandExceptionErrorToSatelliteError = convertCommandExceptionErrorToSatelliteError(((CommandException) satelliteException).getCommandError());
                loge(str + " CommandException: " + asyncResult.exception);
            } else {
                loge(str + " unknown exception: " + asyncResult.exception);
                i = 1;
            }
            i = iConvertCommandExceptionErrorToSatelliteError;
        }
        logd(str + " error: " + i);
        return i;
    }

    private static int convertCommandExceptionErrorToSatelliteError(CommandException.Error error) {
        logd("convertCommandExceptionErrorToSatelliteError: commandExceptionError=" + error.toString());
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$CommandException$Error[error.ordinal()]) {
            case 1:
                return 11;
            case 2:
                return 10;
            case 3:
            case 4:
            case 5:
                return 7;
            case 6:
                return 4;
            default:
                return 1;
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.satellite.SatelliteServiceUtils$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.REQUEST_NOT_SUPPORTED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INTERNAL_ERR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_STATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_MODEM_STATE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.MODEM_ERR.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public static int getValidSatelliteSubId(int i, Context context) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (SubscriptionManagerService.getInstance().isActiveSubId(i, context.getOpPackageName(), context.getAttributionTag())) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return i;
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            logd("getValidSatelliteSubId: use DEFAULT_SUBSCRIPTION_ID for subId=" + i);
            return Integer.MAX_VALUE;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public static int getNtnOnlySubscriptionId(Context context) {
        int iOrElse = SubscriptionManagerService.getInstance().getAllSubInfoList(context.getOpPackageName(), null).stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteServiceUtils$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((SubscriptionInfo) obj).isOnlyNonTerrestrialNetwork();
            }
        }).mapToInt(new SatelliteController$$ExternalSyntheticLambda1()).findFirst().orElse(-1);
        logd("getNtnOnlySubscriptionId: subId=" + iOrElse);
        return iOrElse;
    }

    public static boolean isNtnOnlySubscriptionId(int i) {
        SubscriptionManagerService subscriptionManagerService = SubscriptionManagerService.getInstance();
        if (subscriptionManagerService == null) {
            logd("isNtnOnlySubscriptionId: subscriptionManagerService is null");
            return false;
        }
        SubscriptionInfo subscriptionInfo = subscriptionManagerService.getSubscriptionInfo(i);
        if (subscriptionInfo == null) {
            logd("isNtnOnlySubscriptionId: subInfo is null for subId=" + i);
            return false;
        }
        return subscriptionInfo.isOnlyNonTerrestrialNetwork();
    }

    public static Map<String, Set<Integer>> parseSupportedSatelliteServices(PersistableBundle persistableBundle) {
        HashMap map = new HashMap();
        if (persistableBundle != null && !persistableBundle.isEmpty()) {
            for (String str : persistableBundle.keySet()) {
                if (TelephonyUtils.isValidPlmn(str)) {
                    HashSet hashSet = new HashSet();
                    for (int i : persistableBundle.getIntArray(str)) {
                        if (TelephonyUtils.isValidService(i)) {
                            hashSet.add(Integer.valueOf(i));
                        } else {
                            loge("parseSupportedSatelliteServices: invalid service type=" + i + " for plmn=" + str);
                        }
                    }
                    logd("parseSupportedSatelliteServices: plmn=" + str + ", supportedServicesSet=" + ((String) hashSet.stream().map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(","))));
                    map.put(str, hashSet);
                } else {
                    loge("parseSupportedSatelliteServices: invalid plmn=" + str);
                }
            }
        }
        return map;
    }

    public static List<String> mergeStrLists(List<String> list, List<String> list2) {
        HashSet hashSet = new HashSet();
        if (list != null) {
            hashSet.addAll(list);
        }
        if (list2 != null) {
            hashSet.addAll(list2);
        }
        return hashSet.stream().toList();
    }

    public static List<String> mergeStrLists(List<String> list, List<String> list2, List<String> list3) {
        HashSet hashSet = new HashSet();
        hashSet.addAll(list);
        hashSet.addAll(list2);
        hashSet.addAll(list3);
        return hashSet.stream().toList();
    }

    public static Phone getPhone() {
        return PhoneFactory.getPhone(0);
    }

    public static Phone getPhone(int i) {
        return PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
    }

    public static boolean isCellularAvailable() {
        for (Phone phone : PhoneFactory.getPhones()) {
            ServiceState serviceState = phone.getServiceState();
            if (serviceState != null) {
                int state = serviceState.getState();
                NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
                boolean z = networkRegistrationInfo != null && networkRegistrationInfo.isInService();
                logd("isCellularAvailable: phoneId=" + phone.getPhoneId() + " state=" + state + " isEmergencyOnly=" + serviceState.isEmergencyOnly() + " isCellularDataInService=" + z);
                if ((state == 0 || state == 2 || serviceState.isEmergencyOnly() || z) && !isSatellitePlmn(phone.getSubId(), serviceState)) {
                    logd("isCellularAvailable true");
                    return true;
                }
            }
        }
        logd("isCellularAvailable false");
        return false;
    }

    public static boolean isSatellitePlmn(int i, ServiceState serviceState) {
        ArrayList arrayList = new ArrayList(SatelliteController.getInstance().getAllPlmnSet());
        Iterator it = serviceState.getNetworkRegistrationInfoListForTransportType(1).iterator();
        while (true) {
            int i2 = 0;
            if (it.hasNext()) {
                NetworkRegistrationInfo networkRegistrationInfo = (NetworkRegistrationInfo) it.next();
                if (networkRegistrationInfo.isNonTerrestrialNetwork()) {
                    logd("isSatellitePlmn: nri.isNonTerrestrialNetwork() is true");
                    return true;
                }
                String registeredPlmn = networkRegistrationInfo.getRegisteredPlmn();
                String mccMnc = getMccMnc(networkRegistrationInfo);
                if (TextUtils.isEmpty(registeredPlmn) && TextUtils.isEmpty(mccMnc)) {
                    logd("isSatellitePlmn: registeredPlmn and cell plmn are empty");
                } else {
                    int size = arrayList.size();
                    while (i2 < size) {
                        Object obj = arrayList.get(i2);
                        i2++;
                        String str = (String) obj;
                        if (TextUtils.equals(str, registeredPlmn) || TextUtils.equals(str, mccMnc)) {
                            logd("isSatellitePlmn: return true, satellitePlmn:" + str + " registeredPlmn:" + registeredPlmn + " mccmnc:" + mccMnc);
                            return true;
                        }
                    }
                }
            } else {
                logd("isSatellitePlmn: return false");
                return false;
            }
        }
    }

    public static String getMccMnc(NetworkRegistrationInfo networkRegistrationInfo) {
        CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
        if (cellIdentity == null) {
            logd("getMccMnc: cellIdentity is null");
            return null;
        }
        String mccString = cellIdentity.getMccString();
        String mncString = cellIdentity.getMncString();
        if (mccString == null || mncString == null) {
            logd("getMccMnc: mcc or mnc is null. mcc=" + mccString + " mnc=" + mncString);
            return null;
        }
        return mccString.concat(mncString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SystemSelectionSpecifier convertSystemSelectionSpecifierToHALFormat(android.telephony.satellite.SystemSelectionSpecifier systemSelectionSpecifier) {
        SystemSelectionSpecifier systemSelectionSpecifier2 = new SystemSelectionSpecifier();
        systemSelectionSpecifier2.mMccMnc = systemSelectionSpecifier.getMccMnc();
        systemSelectionSpecifier2.mBands = systemSelectionSpecifier.getBands();
        systemSelectionSpecifier2.mEarfcs = systemSelectionSpecifier.getEarfcns();
        SatelliteInfo[] satelliteInfoArr = (SatelliteInfo[]) systemSelectionSpecifier.getSatelliteInfos().toArray(new SatelliteInfo[0]);
        android.telephony.satellite.stub.SatelliteInfo[] satelliteInfoArr2 = new android.telephony.satellite.stub.SatelliteInfo[satelliteInfoArr.length];
        for (int i = 0; i < satelliteInfoArr.length; i++) {
            android.telephony.satellite.stub.SatelliteInfo satelliteInfo = new android.telephony.satellite.stub.SatelliteInfo();
            satelliteInfoArr2[i] = satelliteInfo;
            satelliteInfo.id = new UUID();
            satelliteInfoArr2[i].id.mostSigBits = satelliteInfoArr[i].getSatelliteId().getMostSignificantBits();
            satelliteInfoArr2[i].id.leastSigBits = satelliteInfoArr[i].getSatelliteId().getLeastSignificantBits();
            satelliteInfoArr2[i].position = new SatellitePosition();
            satelliteInfoArr2[i].position.longitudeDegree = satelliteInfoArr[i].getSatellitePosition().getLongitudeDegrees();
            satelliteInfoArr2[i].position.altitudeKm = satelliteInfoArr[i].getSatellitePosition().getAltitudeKm();
            satelliteInfoArr2[i].bands = satelliteInfoArr[i].getBands().stream().mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
            List earfcnRanges = satelliteInfoArr[i].getEarfcnRanges();
            satelliteInfoArr2[i].earfcnRanges = new EarfcnRange[earfcnRanges.size()];
            for (int i2 = 0; i2 < earfcnRanges.size(); i2++) {
                satelliteInfoArr2[i].earfcnRanges[i2] = new EarfcnRange();
                satelliteInfoArr2[i].earfcnRanges[i2].startEarfcn = ((android.telephony.satellite.EarfcnRange) earfcnRanges.get(i2)).getStartEarfcn();
                satelliteInfoArr2[i].earfcnRanges[i2].endEarfcn = ((android.telephony.satellite.EarfcnRange) earfcnRanges.get(i2)).getEndEarfcn();
            }
        }
        systemSelectionSpecifier2.satelliteInfos = satelliteInfoArr2;
        systemSelectionSpecifier2.tagIds = systemSelectionSpecifier.getTagIds();
        systemSelectionSpecifier2.mIccId = systemSelectionSpecifier.getIccId();
        systemSelectionSpecifier2.mMccMncs = systemSelectionSpecifier.getMccMncs();
        return systemSelectionSpecifier2;
    }

    public static List<SystemSelectionSpecifier> toSystemSelectionSpecifier(List<android.telephony.satellite.SystemSelectionSpecifier> list) {
        return (List) list.stream().map(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteServiceUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteServiceUtils.convertSystemSelectionSpecifierToHALFormat((android.telephony.satellite.SystemSelectionSpecifier) obj);
            }
        }).collect(Collectors.toList());
    }

    public static NetworkInfo toStubNetworkInfo(android.hardware.radio.network.NetworkInfo networkInfo) {
        NetworkInfo networkInfo2 = new NetworkInfo();
        networkInfo2.plmn = networkInfo.plmn;
        networkInfo2.arfcns = networkInfo.arfcns;
        networkInfo2.accessNetwork = networkInfo.accessNetwork;
        networkInfo2.satelliteTechnology = toStubSatelliteTechnology(networkInfo);
        networkInfo2.hasSamePriorityAsTn = networkInfo.hasSamePriorityAsTn;
        return networkInfo2;
    }

    public static int toStubSatelliteTechnology(android.hardware.radio.network.NetworkInfo networkInfo) {
        int i = networkInfo.satelliteTechnology;
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            return i != 3 ? -1 : 1;
        }
        int i2 = networkInfo.accessNetwork;
        return (i2 != 3 && i2 == 6) ? 5 : 4;
    }

    public static android.telephony.satellite.stub.SatelliteNetworkInfo toStubSatelliteNetworkInfo(android.hardware.radio.network.SatelliteNetworkInfo satelliteNetworkInfo) {
        android.telephony.satellite.stub.SatelliteNetworkInfo satelliteNetworkInfo2 = new android.telephony.satellite.stub.SatelliteNetworkInfo();
        android.hardware.radio.network.NetworkInfo[] networkInfoArr = satelliteNetworkInfo.allowedPlmns;
        int i = 0;
        if (networkInfoArr != null) {
            satelliteNetworkInfo2.allowedPlmns = new NetworkInfo[networkInfoArr.length];
            int i2 = 0;
            while (true) {
                android.hardware.radio.network.NetworkInfo[] networkInfoArr2 = satelliteNetworkInfo.allowedPlmns;
                if (i2 >= networkInfoArr2.length) {
                    break;
                }
                satelliteNetworkInfo2.allowedPlmns[i2] = toStubNetworkInfo(networkInfoArr2[i2]);
                i2++;
            }
        }
        android.hardware.radio.network.NetworkInfo[] networkInfoArr3 = satelliteNetworkInfo.disallowedPlmns;
        if (networkInfoArr3 != null) {
            satelliteNetworkInfo2.disallowedPlmns = new NetworkInfo[networkInfoArr3.length];
            while (true) {
                android.hardware.radio.network.NetworkInfo[] networkInfoArr4 = satelliteNetworkInfo.disallowedPlmns;
                if (i >= networkInfoArr4.length) {
                    break;
                }
                satelliteNetworkInfo2.disallowedPlmns[i] = toStubNetworkInfo(networkInfoArr4[i]);
                i++;
            }
        }
        return satelliteNetworkInfo2;
    }

    public static PrioritizedNetworkScanRequest toStubPrioritizedNetworkScanRequest(android.hardware.radio.network.PrioritizedNetworkScanRequest prioritizedNetworkScanRequest) {
        PrioritizedNetworkScanRequest prioritizedNetworkScanRequest2 = new PrioritizedNetworkScanRequest();
        android.hardware.radio.network.NetworkInfo[] networkInfoArr = prioritizedNetworkScanRequest.networkInfos;
        if (networkInfoArr != null) {
            prioritizedNetworkScanRequest2.networkInfos = new NetworkInfo[networkInfoArr.length];
            int i = 0;
            while (true) {
                android.hardware.radio.network.NetworkInfo[] networkInfoArr2 = prioritizedNetworkScanRequest.networkInfos;
                if (i >= networkInfoArr2.length) {
                    break;
                }
                prioritizedNetworkScanRequest2.networkInfos[i] = toStubNetworkInfo(networkInfoArr2[i]);
                i++;
            }
        }
        prioritizedNetworkScanRequest2.searchIntervalMs = prioritizedNetworkScanRequest.searchIntervalMs;
        prioritizedNetworkScanRequest2.validDurationSec = prioritizedNetworkScanRequest.validDurationSec;
        return prioritizedNetworkScanRequest2;
    }

    public static Map<String, Set<Integer>> parseRegionalSatelliteEarfcns(PersistableBundle persistableBundle) {
        HashMap map = new HashMap();
        if (persistableBundle == null || persistableBundle.isEmpty()) {
            logd("parseRegionalSatelliteEarfcns: earfcnsBundle is null or empty");
            return map;
        }
        for (String str : persistableBundle.keySet()) {
            HashSet hashSet = new HashSet();
            for (int i : persistableBundle.getIntArray(str)) {
                hashSet.add(Integer.valueOf(i));
            }
            logd("parseRegionalSatelliteEarfcns: configId = " + str + ", earfcns =" + ((String) hashSet.stream().map(new SimultaneousCallingTracker$$ExternalSyntheticLambda0()).collect(Collectors.joining(","))));
            map.put(str, hashSet);
        }
        return map;
    }

    public static PersistentLogger getPersistentLogger(Context context) {
        try {
            if (context.getResources().getBoolean(R.bool.config_enableCrossTaskScaleUpAnimation)) {
                return new PersistentLogger(DropBoxManagerLoggerBackend.getInstance(context));
            }
            return null;
        } catch (RuntimeException e) {
            loge("getPersistentLogger: RuntimeException ex=" + e);
            return null;
        }
    }

    public static boolean isNbIotNtn(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone == null) {
            logd("isNbIotNtn(): phone is null");
            return false;
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController == null) {
            logd("isNbIotNtn(): satelliteController is null");
            return false;
        }
        return satelliteController.isInCarrierRoamingNbIotNtn(phone);
    }

    public static boolean isSatelliteTechSupported(int i) {
        if (i == 1 || i == 2 || i == 5 || i == 6) {
            logd("isSatelliteTechSupported: " + i + ", return true");
            return true;
        }
        logw("isSatelliteTechSupported: " + i + ", return false");
        return false;
    }

    public static int getCarrierIdFromSubscription(int i) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null) {
            return phone.getCarrierId();
        }
        return -1;
    }

    public static boolean isDeviceEntitledForSubscription(int i) {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null) {
            return satelliteController.isDeviceEntitledForSubscription(i);
        }
        loge("isDeviceEntitledForSubscription: SatelliteController instance is null");
        return false;
    }

    public static boolean isSatelliteAttachSupported(int i) {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null) {
            return satelliteController.isSatelliteSupportedViaCarrier(i);
        }
        loge("isSatelliteAttachSupported: SatelliteController instance is null");
        return false;
    }

    public static int getSatelliteEligibilitySource(int i) {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null) {
            return satelliteController.getSatelliteEligibilitySource(i);
        }
        loge("getSatelliteEligibilitySource: SatelliteController instance is null");
        return 0;
    }

    public static int getSupportedConnectTypeMetrics(int i) {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null) {
            return satelliteController.getSupportedConnectTypeMetrics(i);
        }
        loge("getSupportedConnectTypeMetrics: SatelliteController instance is null");
        return 0;
    }

    private static void logd(String str) {
        Log.d("SatelliteServiceUtils", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteServiceUtils", str);
    }

    private static void logw(String str) {
        Log.w("SatelliteServiceUtils", str);
    }
}
