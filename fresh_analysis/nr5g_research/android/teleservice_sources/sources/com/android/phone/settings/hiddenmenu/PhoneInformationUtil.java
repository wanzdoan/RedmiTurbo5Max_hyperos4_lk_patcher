package com.android.phone.settings.hiddenmenu;

import android.R;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver;
import com.android.internal.telephony.satellite.SatelliteConfig;
import com.android.internal.telephony.satellite.SatelliteConfigParser;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public abstract class PhoneInformationUtil {
    public static Intent mNonEsosIntent;
    private static SatelliteConfig sBackedUpSatelliteConfig;
    private static SatelliteConfigParser sBackedUpSatelliteConfigParser;
    private static CarrierConfigManager sCarrierConfigManager;
    public static final String[] PREFERRED_NETWORK_LABELS_RF = {"GSM/WCDMA preferred", "GSM only", "WCDMA only", "GSM/WCDMA auto (PRL)", "GSM/WCDMA/LTE (PRL)", "LTE only", "LTE/WCDMA", "TDSCDMA only", "TDSCDMA/WCDMA", "LTE/TDSCDMA", "TDSCDMA/GSM", "LTE/TDSCDMA/GSM", "TDSCDMA/GSM/WCDMA", "LTE/TDSCDMA/WCDMA", "LTE/TDSCDMA/GSM/WCDMA", "NR only", "NR/LTE", "NR/LTE/GSM/WCDMA", "NR/LTE/WCDMA", "NR/LTE/TDSCDMA", "NR/LTE/TDSCDMA/GSM", "NR/LTE/TDSCDMA/WCDMA", "NR/LTE/TDSCDMA/GSM/WCDMA", "Unknown"};
    public static final List PREFERRED_NETWORK_MODES_RF = Arrays.asList(0, 1, 2, 3, 9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 23, 24, 26, 28, 29, 30, 31, 32, -1);
    public static final Integer[] SIGNAL_STRENGTH_LEVEL = {-1, 0, 1, 2, 3, 4};
    public static final Integer[] MOCK_DATA_NETWORK_TYPE = {-1, 1, 2, 3, 9, 10, 11, 14, 15, 16, 17, 19, 20};
    public static final int[] STARLINK_CHANNELS = {8665};
    public static final int[] STARLINK_BAND = {25};
    public static final int[] AST_CHANNELS = {2625, 2630};
    public static final int[] AST_BAND = {5};
    public static final Integer[] BAND_VALUES = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 65, 66, 68, 70, 71, 72, 73, 74, 85, 87, 88};
    public static final String[] BAND_LABELS = {"SELECT", "BAND_1", "BAND_2", "BAND_3", "BAND_4", "BAND_5", "BAND_6", "BAND_7", "BAND_8", "BAND_9", "BAND_10", "BAND_11", "BAND_12", "BAND_13", "BAND_14", "BAND_17", "BAND_18", "BAND_19", "BAND_20", "BAND_21", "BAND_22", "BAND_23", "BAND_24", "BAND_25", "BAND_26", "BAND_27", "BAND_28", "BAND_30", "BAND_31", "BAND_33", "BAND_34", "BAND_35", "BAND_36", "BAND_37", "BAND_38", "BAND_39", "BAND_40", "BAND_41", "BAND_42", "BAND_43", "BAND_44", "BAND_45", "BAND_46", "BAND_47", "BAND_48", "BAND_49", "BAND_50", "BAND_51", "BAND_52", "BAND_53", "BAND_65", "BAND_66", "BAND_68", "BAND_70", "BAND_71", "BAND_72", "BAND_73", "BAND_74", "BAND_85", "BAND_87", "BAND_88"};

    public static boolean isDsdsSupported() {
        return TelephonyManager.getDefault().isMultiSimSupported() == 0;
    }

    public static boolean isDsdsEnabled() {
        return TelephonyManager.getDefault().getPhoneCount() > 1;
    }

    public static boolean dsdsModeOnly() {
        String str = SystemProperties.get("ro.boot.hardware.dsds");
        return !TextUtils.isEmpty(str) && Integer.parseInt(str) == 1;
    }

    public static String[] getPhoneIndexLabels(TelephonyManager telephonyManager) {
        int activeModemCount = telephonyManager.getActiveModemCount();
        String[] strArr = new String[activeModemCount];
        for (int i = 0; i < activeModemCount; i++) {
            strArr[i] = "Phone " + i;
        }
        return strArr;
    }

    public static Phone getPhone(int i) {
        log("getPhone subId = " + i);
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i));
        if (phone != null) {
            return phone;
        }
        log("return the default phone");
        return PhoneFactory.getDefaultPhone();
    }

    private static String getCellInfoDisplayString(int i) {
        return i != Integer.MAX_VALUE ? Integer.toString(i) : "";
    }

    private static String getCellInfoDisplayString(long j) {
        return j != Long.MAX_VALUE ? Long.toString(j) : "";
    }

    private static String getConnectionStatusString(CellInfo cellInfo) {
        String str;
        String str2 = "";
        String str3 = cellInfo.isRegistered() ? "R" : "";
        int cellConnectionStatus = cellInfo.getCellConnectionStatus();
        if (cellConnectionStatus == 0) {
            str = "N";
        } else if (cellConnectionStatus == 1) {
            str = "P";
        } else {
            str = cellConnectionStatus != 2 ? "" : "S";
        }
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str)) {
            str2 = "+";
        }
        return str3 + str2 + str;
    }

    private static String buildGsmInfoString(CellInfoGsm cellInfoGsm) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-4.4s %-4.4s\n", getConnectionStatusString(cellInfoGsm), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getLac()), getCellInfoDisplayString(cellIdentity.getCid()), getCellInfoDisplayString(cellIdentity.getArfcn()), getCellInfoDisplayString(cellIdentity.getBsic()), getCellInfoDisplayString(cellInfoGsm.getCellSignalStrength().getDbm()));
    }

    private static String buildLteInfoString(CellInfoLte cellInfoLte) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        CellSignalStrengthLte cellSignalStrength = cellInfoLte.getCellSignalStrength();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-2.2s %-4.4s %-4.4s %-2.2s\n", getConnectionStatusString(cellInfoLte), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getTac()), getCellInfoDisplayString(cellIdentity.getCi()), getCellInfoDisplayString(cellIdentity.getPci()), getCellInfoDisplayString(cellIdentity.getEarfcn()), getCellInfoDisplayString(cellIdentity.getBandwidth()), getCellInfoDisplayString(cellSignalStrength.getDbm()), getCellInfoDisplayString(cellSignalStrength.getRsrq()), getCellInfoDisplayString(cellSignalStrength.getTimingAdvance()));
    }

    private static String buildNrInfoString(CellInfoNr cellInfoNr) {
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-7.7s %-7.7s %-7.7s\n", getConnectionStatusString(cellInfoNr), cellIdentityNr.getMccString(), cellIdentityNr.getMncString(), getCellInfoDisplayString(cellIdentityNr.getTac()), getCellInfoDisplayString(cellIdentityNr.getNci()), getCellInfoDisplayString(cellIdentityNr.getPci()), getCellInfoDisplayString(cellIdentityNr.getNrarfcn()), getCellInfoDisplayString(cellSignalStrengthNr.getSsRsrp()), getCellInfoDisplayString(cellSignalStrengthNr.getSsRsrq()));
    }

    private static String buildWcdmaInfoString(CellInfoWcdma cellInfoWcdma) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        return String.format("%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-3.3s %-4.4s\n", getConnectionStatusString(cellInfoWcdma), getCellInfoDisplayString(cellIdentity.getMcc()), getCellInfoDisplayString(cellIdentity.getMnc()), getCellInfoDisplayString(cellIdentity.getLac()), getCellInfoDisplayString(cellIdentity.getCid()), getCellInfoDisplayString(cellIdentity.getUarfcn()), getCellInfoDisplayString(cellIdentity.getPsc()), getCellInfoDisplayString(cellInfoWcdma.getCellSignalStrength().getDbm()));
    }

    public static String buildCellInfoString(List list) {
        String str = new String();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                CellInfo cellInfo = (CellInfo) it.next();
                if (cellInfo instanceof CellInfoLte) {
                    sb2.append(buildLteInfoString((CellInfoLte) cellInfo));
                } else if (cellInfo instanceof CellInfoWcdma) {
                    sb3.append(buildWcdmaInfoString((CellInfoWcdma) cellInfo));
                } else if (cellInfo instanceof CellInfoGsm) {
                    sb.append(buildGsmInfoString((CellInfoGsm) cellInfo));
                } else if (cellInfo instanceof CellInfoNr) {
                    sb4.append(buildNrInfoString((CellInfoNr) cellInfo));
                }
            }
            if (sb4.length() != 0) {
                str = str.concat(String.format("NR\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-7.7s %-7.7s %-7.7s\n", "SRV", "MCC", "MNC", "TAC", "NCI", "PCI", "NRARFCN", "SS-RSRP", "SS-RSRQ")).concat(sb4.toString());
            }
            if (sb2.length() != 0) {
                str = str.concat(String.format("LTE\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-3.3s %-6.6s %-2.2s %-4.4s %-4.4s %-2.2s\n", "SRV", "MCC", "MNC", "TAC", "CID", "PCI", "EARFCN", "BW", "RSRP", "RSRQ", "TA")).concat(sb2.toString());
            }
            if (sb3.length() != 0) {
                str = str.concat(String.format("WCDMA\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-3.3s %-4.4s\n", "SRV", "MCC", "MNC", "LAC", "CID", "UARFCN", "PSC", "RSCP")).concat(sb3.toString());
            }
            if (sb.length() != 0) {
                str = str.concat(String.format("GSM\n%-3.3s %-3.3s %-3.3s %-5.5s %-5.5s %-6.6s %-4.4s %-4.4s\n", "SRV", "MCC", "MNC", "LAC", "CID", "ARFCN", "BSIC", "RSSI")).concat(sb.toString());
            }
        } else {
            str = "unknown";
        }
        return str.toString();
    }

    public static boolean isVoiceServiceAvailable(ImsMmTelManager imsMmTelManager) {
        if (imsMmTelManager == null) {
            return false;
        }
        int[] iArr = {0, 2, 3, 4};
        boolean zIsAvailable = false;
        for (int i = 0; i < 4; i++) {
            try {
                zIsAvailable |= imsMmTelManager.isAvailable(1, iArr[i]);
                if (zIsAvailable) {
                    break;
                }
            } catch (Exception e) {
                loge("isVoiceServiceAvailable: exception=" + e);
            }
        }
        return zIsAvailable;
    }

    public static boolean isVideoServiceAvailable(ImsMmTelManager imsMmTelManager) {
        if (imsMmTelManager == null) {
            return false;
        }
        int[] iArr = {0, 1, 2, 3, 4};
        boolean zIsAvailable = false;
        for (int i = 0; i < 5; i++) {
            try {
                zIsAvailable |= imsMmTelManager.isAvailable(2, iArr[i]);
                if (zIsAvailable) {
                    break;
                }
            } catch (Exception e) {
                loge("isVideoServiceAvailable: exception=" + e);
            }
        }
        return zIsAvailable;
    }

    public static boolean isWfcServiceAvailable(ImsMmTelManager imsMmTelManager) {
        if (imsMmTelManager == null) {
            return false;
        }
        try {
            return imsMmTelManager.isAvailable(1, 1);
        } catch (Exception e) {
            loge("isWfcServiceAvailable: exception=" + e);
            return false;
        }
    }

    public static void uncapMaxAllowedDataMode() {
        SatelliteConfig satelliteConfig;
        log("uncapMaxAllowedDataMode: uncap max allowed data mode by overriding satellite config");
        SatelliteConfigParser configParser = TelephonyConfigUpdateInstallReceiver.getInstance().getConfigParser("satellite");
        SatelliteConfig satelliteConfig2 = configParser != null ? (SatelliteConfig) configParser.getConfig() : null;
        log("uncapMaxAllowedDataMode: backing up satellite config parser: " + configParser);
        sBackedUpSatelliteConfigParser = configParser;
        log("uncapMaxAllowedDataMode: backing up satellite config: " + satelliteConfig2);
        sBackedUpSatelliteConfig = satelliteConfig2;
        if (satelliteConfig2 == null) {
            log("uncapMaxAllowedDataMode: satelliteConfig is null, creating new SatelliteConfig just to uncap max allowed data mode");
            satelliteConfig = new SatelliteConfig();
        } else {
            log("uncapMaxAllowedDataMode: satelliteConfig is not null, make a deepcopy just to uncap max allowed data mode");
            satelliteConfig = new SatelliteConfig(satelliteConfig2);
        }
        satelliteConfig.overrideSatelliteMaxAllowedDataMode(2);
        log("uncapMaxAllowedDataMode: creating uncappedSatelliteConfigParser to uncap max allowed data mode");
        SatelliteConfigParser satelliteConfigParser = new SatelliteConfigParser(new byte[0]);
        satelliteConfigParser.overrideConfig(satelliteConfig);
        TelephonyConfigUpdateInstallReceiver.getInstance().overrideConfigParser(satelliteConfigParser);
    }

    public static void restoreMaxAllowedDataMode() {
        log("restoreMaxAllowedDataMode: restoring max allowed data mode by restoring the backed up satellite config parser: " + sBackedUpSatelliteConfigParser + " and config: " + sBackedUpSatelliteConfig);
        if (sBackedUpSatelliteConfigParser == null) {
            log("restoreMaxAllowedDataMode: mBackedUpSatelliteConfigParser is null, therefore don't have to override mBackedUpSatelliteConfig, as it would null as well");
            TelephonyConfigUpdateInstallReceiver.getInstance().clearOverriddenConfigParser("satellite");
        } else {
            TelephonyConfigUpdateInstallReceiver.getInstance().overrideConfigParser(sBackedUpSatelliteConfigParser);
            TelephonyConfigUpdateInstallReceiver.getInstance().getConfigParser("satellite").overrideConfig(sBackedUpSatelliteConfig);
        }
    }

    public static boolean shouldHideNonEmergencyMode(Context context, int i) {
        if (!Build.isDebuggable() || TextUtils.isEmpty("android.telephony.satellite.action.SATELLITE_START_NON_EMERGENCY_SESSION")) {
            return true;
        }
        if (mNonEsosIntent != null) {
            mNonEsosIntent = null;
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null) {
            loge("shouldHideNonEmergencyMode: cm is null");
            return true;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(i, "satellite_attach_supported_bool", "satellite_esos_supported_bool");
        if (!configForSubId.getBoolean("satellite_esos_supported_bool", false)) {
            log("shouldHideNonEmergencyMode: esos_supported false");
            return true;
        }
        if (!configForSubId.getBoolean("satellite_attach_supported_bool", false)) {
            log("shouldHideNonEmergencyMode: attach_supported false");
            return true;
        }
        String stringFromOverlayConfig = getStringFromOverlayConfig(context, R.string.dream_preview_title);
        String stringFromOverlayConfig2 = getStringFromOverlayConfig(context, R.string.done_accessibility_shortcut_menu_button);
        if (stringFromOverlayConfig == null || stringFromOverlayConfig2 == null || stringFromOverlayConfig.isEmpty() || stringFromOverlayConfig2.isEmpty()) {
            log("shouldHideNonEmergencyMode: packageName or className is null or empty.");
            return true;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.telephony.satellite.action.SATELLITE_START_NON_EMERGENCY_SESSION");
        intent.setComponent(new ComponentName(stringFromOverlayConfig, stringFromOverlayConfig2));
        if (packageManager.queryBroadcastReceivers(intent, 0).isEmpty()) {
            log("shouldHideNonEmergencyMode: Broadcast receiver not found for intent: " + intent);
            return true;
        }
        mNonEsosIntent = intent;
        return false;
    }

    public static PersistableBundle getSatelliteServicesBundleForOperatorPlmn(TelephonyManager telephonyManager, int i, int i2, PersistableBundle persistableBundle) {
        String networkOperatorForPhone = telephonyManager.getNetworkOperatorForPhone(i);
        if (TextUtils.isEmpty(networkOperatorForPhone)) {
            loge("satData: NetworkOperator PLMN is empty");
            networkOperatorForPhone = telephonyManager.getSimOperatorNumeric(i2);
            loge("satData: SimOperator PLMN = " + networkOperatorForPhone);
        }
        int[] iArr = {2, 3, 5, 6};
        PersistableBundle persistableBundle2 = persistableBundle.getPersistableBundle("carrier_supported_satellite_services_per_provider_bundle");
        PersistableBundle persistableBundle3 = new PersistableBundle();
        boolean z = false;
        if (persistableBundle2 != null) {
            for (String str : persistableBundle2.keySet()) {
                if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase(networkOperatorForPhone)) {
                    persistableBundle3.putIntArray(networkOperatorForPhone, iArr);
                    z = true;
                } else {
                    persistableBundle3.putIntArray(str, persistableBundle2.getIntArray(str));
                }
            }
        }
        if (!z) {
            persistableBundle3.putIntArray(networkOperatorForPhone, iArr);
        }
        log("satData: New SatelliteServicesBundle = " + persistableBundle3);
        return persistableBundle3;
    }

    public static void updateCarrierConfigToSupportData(CarrierConfigManager carrierConfigManager, int i, PersistableBundle persistableBundle) {
        int[] iArr;
        int[] intArray = persistableBundle.getIntArray("carrier_roaming_satellite_default_services_int_array");
        if (intArray != null && intArray.length > 0) {
            if (Arrays.stream(intArray).anyMatch(new IntPredicate() { // from class: com.android.phone.settings.hiddenmenu.PhoneInformationUtil$$ExternalSyntheticLambda0
                @Override // java.util.function.IntPredicate
                public final boolean test(int i2) {
                    return PhoneInformationUtil.m3541$r8$lambda$Og8naHnPQTEmoV7FfPIOsnmpOI(i2);
                }
            })) {
                iArr = new int[intArray.length];
                System.arraycopy(intArray, 0, iArr, 0, intArray.length);
            } else {
                int length = intArray.length;
                int[] iArr2 = new int[length + 1];
                System.arraycopy(intArray, 0, iArr2, 0, intArray.length);
                iArr2[length] = 2;
                iArr = iArr2;
            }
        } else {
            iArr = new int[]{2};
        }
        persistableBundle.putIntArray("carrier_roaming_satellite_default_services_int_array", iArr);
        persistableBundle.putBoolean("satellite_entitlement_supported_bool", false);
        persistableBundle.remove("satellite_data_support_mode_int");
        log("satData: changing carrierConfig to : " + persistableBundle);
        carrierConfigManager.overrideConfig(i, persistableBundle, false);
    }

    /* JADX INFO: renamed from: $r8$lambda$Og8naHnPQ-TEmoV7FfPIOsnmpOI, reason: not valid java name */
    public static /* synthetic */ boolean m3541$r8$lambda$Og8naHnPQTEmoV7FfPIOsnmpOI(int i) {
        return i == 2;
    }

    private static String getStringFromOverlayConfig(Context context, int i) {
        try {
            return context.getResources().getString(i);
        } catch (Resources.NotFoundException e) {
            loge("getStringFromOverlayConfig: ex=" + e);
            return null;
        }
    }

    public static CarrierConfigManager getCarrierConfig(Context context) {
        if (sCarrierConfigManager == null) {
            sCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        }
        return sCarrierConfigManager;
    }

    public static void configurePhoneSelectionUi(LinearLayout linearLayout, LinearLayout linearLayout2, TextView textView, TextView textView2, String[] strArr) {
        Context context = linearLayout.getContext();
        boolean zIsRadioInfoRestricted = isRadioInfoRestricted(context, 0);
        boolean zIsRadioInfoRestricted2 = isRadioInfoRestricted(context, 1);
        if (strArr.length > 1) {
            textView.setText(strArr[0]);
            textView2.setText(strArr[1]);
            linearLayout.setVisibility(zIsRadioInfoRestricted ? 8 : 0);
            linearLayout2.setVisibility(zIsRadioInfoRestricted2 ? 8 : 0);
            return;
        }
        if (strArr.length == 1) {
            textView.setText(strArr[0]);
            linearLayout.setVisibility(zIsRadioInfoRestricted ? 8 : 0);
            linearLayout2.setVisibility(8);
        } else {
            linearLayout.setVisibility(8);
            linearLayout2.setVisibility(8);
        }
    }

    private static void log(String str) {
        Log.d("PhoneInformationUtil", str);
    }

    private static void loge(String str) {
        Log.e("PhoneInformationUtil", str);
    }

    public static ApnSetting createUpdatedApnSetting(ApnSetting apnSetting, int i) {
        if (apnSetting == null) {
            return null;
        }
        return new ApnSetting.Builder().setEntryName(apnSetting.getEntryName()).setApnName(apnSetting.getApnName()).setProxyAddress(apnSetting.getProxyAddressAsString()).setProxyPort(apnSetting.getProxyPort()).setMmsc(apnSetting.getMmsc()).setMmsProxyAddress(apnSetting.getMmsProxyAddressAsString()).setMmsProxyPort(apnSetting.getMmsProxyPort()).setUser(apnSetting.getUser()).setPassword(apnSetting.getPassword()).setAuthType(apnSetting.getAuthType()).setApnTypeBitmask(apnSetting.getApnTypeBitmask()).setOperatorNumeric(apnSetting.getOperatorNumeric()).setProtocol(apnSetting.getProtocol()).setRoamingProtocol(apnSetting.getRoamingProtocol()).setMtuV4(apnSetting.getMtuV4()).setMtuV6(apnSetting.getMtuV6()).setCarrierEnabled(apnSetting.isEnabled()).setNetworkTypeBitmask(apnSetting.getNetworkTypeBitmask()).setLingeringNetworkTypeBitmask(apnSetting.getLingeringNetworkTypeBitmask()).setProfileId(apnSetting.getProfileId()).setPersistent(apnSetting.isPersistent()).setMaxConns(apnSetting.getMaxConns()).setWaitTime(apnSetting.getWaitTime()).setMaxConnsTime(apnSetting.getMaxConnsTime()).setMvnoType(apnSetting.getMvnoType()).setMvnoMatchData(apnSetting.getMvnoMatchData()).setApnSetId(apnSetting.getApnSetId()).setCarrierId(apnSetting.getCarrierId()).setSkip464Xlat(apnSetting.getSkip464Xlat()).setAlwaysOn(apnSetting.isAlwaysOn()).setInfrastructureBitmask(i).setEsimBootstrapProvisioning(apnSetting.isEsimBootstrapProvisioning()).build();
    }

    private static String apnTypesToString(int i) {
        ArrayList arrayList = new ArrayList();
        if ((i & 17) == 17) {
            arrayList.add("DEFAULT");
        }
        if ((i & 2) == 2) {
            arrayList.add("MMS");
        }
        if ((i & 4) == 4) {
            arrayList.add("SUPL");
        }
        if ((i & 8) == 8) {
            arrayList.add("DUN");
        }
        if ((i & 16) == 16) {
            arrayList.add("HIPRI");
        }
        if ((i & 32) == 32) {
            arrayList.add("FOTA");
        }
        if ((i & 64) == 64) {
            arrayList.add("IMS");
        }
        if ((i & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 128) {
            arrayList.add("CBS");
        }
        if ((i & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) == 256) {
            arrayList.add("IA");
        }
        if ((i & 512) == 512) {
            arrayList.add("EMERGENCY");
        }
        return TextUtils.join(", ", arrayList);
    }

    public static List updateApnInfrastructureBitmaskForSatellite(Context context, int i, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursorQuery = contentResolver.query(Telephony.Carriers.SIM_APN_URI, null, "carrier_enabled = 1", null, null);
            if (cursorQuery != null) {
                while (cursorQuery.moveToNext()) {
                    ApnSetting apnSettingMakeApnSetting = ApnSetting.makeApnSetting(cursorQuery);
                    Log.d(str, "UpdateAPN: Checking APN: " + apnSettingMakeApnSetting.getApnName() + ", Types: " + apnTypesToString(apnSettingMakeApnSetting.getApnTypeBitmask()) + ", Infrastructure Bitmask: " + apnSettingMakeApnSetting.getInfrastructureBitmask());
                    if (apnSettingMakeApnSetting.canHandleType(17) || apnSettingMakeApnSetting.canHandleType(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER)) {
                        if ((apnSettingMakeApnSetting.getInfrastructureBitmask() & 2) == 0) {
                            ContentValues contentValues = new ContentValues();
                            DatabaseUtils.cursorRowToContentValues(cursorQuery, contentValues);
                            arrayList.add(contentValues);
                            ApnSetting apnSettingCreateUpdatedApnSetting = createUpdatedApnSetting(apnSettingMakeApnSetting, apnSettingMakeApnSetting.getInfrastructureBitmask() | 2);
                            Log.d(str, "UpdateAPN: New APN: " + apnSettingCreateUpdatedApnSetting.getApnName() + ", Types: " + apnTypesToString(apnSettingCreateUpdatedApnSetting.getApnTypeBitmask()) + ", Infrastructure Bitmask: " + apnSettingCreateUpdatedApnSetting.getInfrastructureBitmask());
                            int iUpdate = contentResolver.update(Telephony.Carriers.CONTENT_URI, apnSettingCreateUpdatedApnSetting.toContentValues(), "apn = ?", new String[]{apnSettingMakeApnSetting.getApnName()});
                            StringBuilder sb = new StringBuilder();
                            sb.append("UpdateAPN: Rows updated: ");
                            sb.append(iUpdate);
                            Log.d(str, sb.toString());
                        }
                    }
                }
                cursorQuery.close();
            }
            return arrayList;
        } catch (Exception e) {
            Log.e(str, "Error modifying APN for satellite mock: " + e);
            return arrayList;
        }
    }

    public static void restoreOriginalApns(Context context, List list, String str) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ContentValues contentValues = (ContentValues) it.next();
                String[] strArr = {contentValues.getAsString("apn")};
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("infrastructure_bitmask", contentValues.getAsInteger("infrastructure_bitmask"));
                Log.d(str, "UpdateAPN: RestoreAPN: Rows updated: " + contentResolver.update(Telephony.Carriers.CONTENT_URI, contentValues2, "apn = ?", strArr));
            }
        } catch (Exception e) {
            Log.e(str, "Error restoring APNs: " + e);
        }
    }

    public static String getSubscriberId(TelephonyManager telephonyManager, Resources resources) {
        String subscriberId = telephonyManager.getSubscriberId();
        return subscriberId != null ? subscriberId : resources.getString(com.android.phone.R.string.radioInfo_unknown);
    }

    public static String getGid1(TelephonyManager telephonyManager, Resources resources) {
        String groupIdLevel1 = telephonyManager.getGroupIdLevel1();
        return groupIdLevel1 != null ? groupIdLevel1 : resources.getString(com.android.phone.R.string.radioInfo_unknown);
    }

    public static String getCarrierIdString(TelephonyManager telephonyManager, Resources resources) {
        int simCarrierId = telephonyManager.getSimCarrierId();
        CharSequence simCarrierIdName = telephonyManager.getSimCarrierIdName();
        if (simCarrierId == -1) {
            return resources.getString(com.android.phone.R.string.radioInfo_unknown);
        }
        if (TextUtils.isEmpty(simCarrierIdName)) {
            return String.valueOf(simCarrierId);
        }
        return simCarrierId + " (" + ((Object) simCarrierIdName) + ")";
    }

    public static boolean isUserBuild() {
        return "user".equals(Build.TYPE);
    }

    public static boolean isRadioInfoRestricted(Context context, int i) {
        if (!isUserBuild()) {
            return false;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            return isRadioInfoMenuDisabled(context, subscriptionId);
        }
        return false;
    }

    public static boolean isRadioInfoAccessRestricted(Context context) {
        if (!isUserBuild()) {
            return false;
        }
        int activeModemCount = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getActiveModemCount();
        for (int i = 0; i < activeModemCount; i++) {
            int subscriptionId = SubscriptionManager.getSubscriptionId(i);
            if (SubscriptionManager.isValidSubscriptionId(subscriptionId) && isRadioInfoMenuDisabled(context, subscriptionId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRadioInfoMenuDisabled(Context context, int i) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfig = getCarrierConfig(context);
        if (carrierConfig == null || (configForSubId = carrierConfig.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean("hide_radio_info_on_user_build_bool", false);
    }
}
