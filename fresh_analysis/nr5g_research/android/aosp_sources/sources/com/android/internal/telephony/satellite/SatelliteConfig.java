package com.android.internal.telephony.satellite;

import android.content.Context;
import android.os.FileUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.telephony.TelephonyConfigData;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteConfig {
    private Integer mCarrierRoamingMaxAllowedDataMode;
    private TelephonyConfigData.SatelliteConfigProto mConfigData;
    private List mDeviceSatelliteProviders;
    private Boolean mIsSatelliteRegionAllowed;
    private File mSatS2File;
    private File mSatelliteAccessConfigJsonFile;
    private final Map mSatelliteCarrierConfig = new ConcurrentHashMap();
    private List mSatelliteRegionCountryCodes;
    private int mVersion;

    public SatelliteConfig() {
        logd("SatelliteConfig: constructing from scratch");
    }

    public SatelliteConfig(SatelliteConfig satelliteConfig) {
        logd("SatelliteConfig: constructing through deep copy of: " + satelliteConfig);
        TelephonyConfigData.SatelliteConfigProto satelliteConfigProto = satelliteConfig.mConfigData;
        if (satelliteConfigProto == null) {
            loge("SatelliteConfig: satelliteConfig.mConfigData is null, return");
        } else {
            init(satelliteConfigProto);
        }
    }

    public SatelliteConfig(TelephonyConfigData.SatelliteConfigProto satelliteConfigProto) {
        logd("SatelliteConfig: constructing with configData: " + satelliteConfigProto);
        init(satelliteConfigProto);
    }

    private void init(TelephonyConfigData.SatelliteConfigProto satelliteConfigProto) {
        this.mConfigData = satelliteConfigProto;
        this.mVersion = satelliteConfigProto.getVersion();
        logd("mVersion: " + this.mVersion);
        buildCarrierSpecificConfigs();
        buildDeviceSpecificCarrierRoamingConfig();
        buildDeviceSatelliteRegion();
    }

    private void buildCarrierSpecificConfigs() {
        for (TelephonyConfigData.CarrierSupportedSatelliteServicesProto carrierSupportedSatelliteServicesProto : this.mConfigData.getCarrierSupportedSatelliteServicesList()) {
            if (!carrierSupportedSatelliteServicesProto.hasCarrierId()) {
                loge("carrierProto doesn't have carrierId, continue ");
            } else {
                int carrierId = carrierSupportedSatelliteServicesProto.getCarrierId();
                if (this.mSatelliteCarrierConfig.containsKey(Integer.valueOf(carrierId))) {
                    loge("Duplicate carrierId found: " + carrierId + ". Skipping further configurations for this ID.");
                } else {
                    SatelliteCarrierConfig satelliteCarrierConfig = new SatelliteCarrierConfig();
                    for (TelephonyConfigData.SatelliteProviderCapabilityProto satelliteProviderCapabilityProto : carrierSupportedSatelliteServicesProto.getSupportedSatelliteProviderCapabilitiesList()) {
                        if (!satelliteProviderCapabilityProto.hasCarrierPlmn()) {
                            loge("no plmn, continue to next");
                        } else {
                            satelliteCarrierConfig.mProviderCapabilities.put(satelliteProviderCapabilityProto.getCarrierPlmn(), new ProviderCapability(satelliteProviderCapabilityProto.hasNtnConnectType() ? Integer.valueOf(satelliteProviderCapabilityProto.getNtnConnectType()) : null, new HashSet(satelliteProviderCapabilityProto.getAllowedServicesList())));
                        }
                    }
                    if (carrierSupportedSatelliteServicesProto.hasAttachSupported()) {
                        satelliteCarrierConfig.mIsAttachSupported = Boolean.valueOf(carrierSupportedSatelliteServicesProto.getAttachSupported());
                    }
                    if (carrierSupportedSatelliteServicesProto.hasDataSupportMode()) {
                        satelliteCarrierConfig.mDataSupportMode = Integer.valueOf(carrierSupportedSatelliteServicesProto.getDataSupportMode());
                    }
                    if (carrierSupportedSatelliteServicesProto.hasNtnConnectType()) {
                        satelliteCarrierConfig.mNtnConnectType = Integer.valueOf(carrierSupportedSatelliteServicesProto.getNtnConnectType());
                    }
                    if (carrierSupportedSatelliteServicesProto.hasEmergencyMessagingSupported()) {
                        satelliteCarrierConfig.mIsEmergencyMessagingSupported = Boolean.valueOf(carrierSupportedSatelliteServicesProto.getEmergencyMessagingSupported());
                    }
                    if (carrierSupportedSatelliteServicesProto.hasEntitlementSupported()) {
                        satelliteCarrierConfig.mIsEntitlementSupported = Boolean.valueOf(carrierSupportedSatelliteServicesProto.getEntitlementSupported());
                    }
                    if (carrierSupportedSatelliteServicesProto.hasEntitlementServerUrl()) {
                        satelliteCarrierConfig.mEntitlementServerUrl = carrierSupportedSatelliteServicesProto.getEntitlementServerUrl();
                    }
                    this.mSatelliteCarrierConfig.put(Integer.valueOf(carrierId), satelliteCarrierConfig);
                }
            }
        }
        logd("mSatelliteCarrierConfig: " + this.mSatelliteCarrierConfig);
    }

    private void buildDeviceSpecificCarrierRoamingConfig() {
        if (!this.mConfigData.hasCarrierRoamingConfig()) {
            logd("buildCarrierRoamingConfig: carrierRoamingConfig empty");
            return;
        }
        this.mCarrierRoamingMaxAllowedDataMode = Integer.valueOf(this.mConfigData.getCarrierRoamingConfig().getMaxAllowedDataMode());
        logd("buildCarrierRoamingConfig: mCarrierRoamingMaxAllowedDataMode is " + this.mCarrierRoamingMaxAllowedDataMode);
        if (this.mConfigData.getCarrierRoamingConfig().getDeviceSatellitePlmnCount() == 0) {
            logd("buildCarrierRoamingConfig: deviceSatellitePlmn is null, set empty list");
            this.mDeviceSatelliteProviders = new ArrayList();
        } else {
            this.mDeviceSatelliteProviders = this.mConfigData.getCarrierRoamingConfig().getDeviceSatellitePlmnList();
            logd("buildCarrierRoamingConfig: mDeviceSatelliteProviders: " + String.join(", ", this.mDeviceSatelliteProviders));
        }
    }

    private void buildDeviceSatelliteRegion() {
        logd("buildDeviceSatelliteRegion");
        if (!this.mConfigData.hasDeviceSatelliteRegion()) {
            logd("mConfigData.deviceSatelliteRegion: empty");
            return;
        }
        if (this.mConfigData.getDeviceSatelliteRegion().getCountryCodesCount() == 0) {
            logd("mConfigData.deviceSatelliteRegion.countryCodes is null, set empty list");
            this.mSatelliteRegionCountryCodes = new ArrayList();
        } else {
            this.mSatelliteRegionCountryCodes = this.mConfigData.getDeviceSatelliteRegion().getCountryCodesList();
            logd("mSatelliteRegionCountryCodes: " + String.join(",", this.mSatelliteRegionCountryCodes));
        }
        this.mIsSatelliteRegionAllowed = Boolean.valueOf(this.mConfigData.getDeviceSatelliteRegion().getIsAllowed());
        logd("mIsSatelliteRegionAllowed: " + this.mIsSatelliteRegionAllowed);
        this.mSatS2File = null;
        if (this.mConfigData.getDeviceSatelliteRegion().hasS2CellFile() && !this.mConfigData.getDeviceSatelliteRegion().getS2CellFile().isEmpty()) {
            logd("s2CellFile size: " + this.mConfigData.getDeviceSatelliteRegion().getS2CellFile().size());
        } else {
            logd("s2CellFile: empty");
        }
        this.mSatelliteAccessConfigJsonFile = null;
        if (this.mConfigData.getDeviceSatelliteRegion().hasSatelliteAccessConfigFile() && !this.mConfigData.getDeviceSatelliteRegion().getSatelliteAccessConfigFile().isEmpty()) {
            logd("satellite_access_config_json size: " + this.mConfigData.getDeviceSatelliteRegion().getSatelliteAccessConfigFile().size());
            return;
        }
        logd("satellite_access_config_json: empty");
    }

    public Integer getSatelliteMaxAllowedDataMode() {
        Integer num = this.mCarrierRoamingMaxAllowedDataMode;
        if (num != null) {
            return num;
        }
        logd("mCarrierRoamingMaxAllowedDataMode : mConfigData is null or no config data");
        return null;
    }

    public Boolean isSatelliteAttachSupportedBySubId(int i) {
        return isSatelliteAttachSupportedByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public Boolean isSatelliteAttachSupportedByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mIsAttachSupported;
        }
        return null;
    }

    public Integer getSatelliteDataSupportModeBySubId(int i) {
        return getSatelliteDataSupportModeByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public Integer getSatelliteDataSupportModeByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mDataSupportMode;
        }
        return null;
    }

    public Integer getSatelliteNtnConnectTypeBySubId(int i) {
        return getSatelliteNtnConnectTypeByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public Integer getSatelliteNtnConnectTypeByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mNtnConnectType;
        }
        return null;
    }

    public List<PlmnConfig> getSatellitePlmnConfigsBySubId(int i) {
        return getSatellitePlmnConfigsByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public List<PlmnConfig> getSatellitePlmnConfigsByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig == null) {
            return Collections.EMPTY_LIST;
        }
        return (List) satelliteCarrierConfig.mProviderCapabilities.entrySet().stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteConfig$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SatelliteConfig.$r8$lambda$UXLA2aqcPv_mIdciUbI9AZ8DkgE((Map.Entry) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteConfig$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteConfig.m1562$r8$lambda$5KufzMQiunPXpODl3iFfPpXJI((Map.Entry) obj);
            }
        }).collect(Collectors.toList());
    }

    public static /* synthetic */ boolean $r8$lambda$UXLA2aqcPv_mIdciUbI9AZ8DkgE(Map.Entry entry) {
        return ((ProviderCapability) entry.getValue()).getNtnConnectType() != null;
    }

    /* JADX INFO: renamed from: $r8$lambda$5KufzMQiunPXpODl--3iFfPpXJI, reason: not valid java name */
    public static /* synthetic */ PlmnConfig m1562$r8$lambda$5KufzMQiunPXpODl3iFfPpXJI(Map.Entry entry) {
        return new PlmnConfig((String) entry.getKey(), ((ProviderCapability) entry.getValue()).getNtnConnectType().intValue());
    }

    public PlmnConfig getSatellitePlmnConfigBySubId(int i, String str) {
        return getSatellitePlmnConfigByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i), str);
    }

    public PlmnConfig getSatellitePlmnConfigByCarrierId(int i, final String str) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig == null) {
            return null;
        }
        return (PlmnConfig) satelliteCarrierConfig.mProviderCapabilities.entrySet().stream().filter(new Predicate() { // from class: com.android.internal.telephony.satellite.SatelliteConfig$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SatelliteConfig.$r8$lambda$2bChal5a9hKRdhuh8M6X4YyUHOA(str, (Map.Entry) obj);
            }
        }).map(new Function() { // from class: com.android.internal.telephony.satellite.SatelliteConfig$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteConfig.$r8$lambda$nRkhi2tO1ASDzeA0ajZjzfHRkVY((Map.Entry) obj);
            }
        }).findFirst().orElse(null);
    }

    public static /* synthetic */ boolean $r8$lambda$2bChal5a9hKRdhuh8M6X4YyUHOA(String str, Map.Entry entry) {
        return ((String) entry.getKey()).equals(str) && ((ProviderCapability) entry.getValue()).getNtnConnectType() != null;
    }

    public static /* synthetic */ PlmnConfig $r8$lambda$nRkhi2tO1ASDzeA0ajZjzfHRkVY(Map.Entry entry) {
        return new PlmnConfig((String) entry.getKey(), ((ProviderCapability) entry.getValue()).getNtnConnectType().intValue());
    }

    public Boolean isEmergencyMessagingSupportedBySubId(int i) {
        return isEmergencyMessagingSupportedByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public Boolean isEmergencyMessagingSupportedByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mIsEmergencyMessagingSupported;
        }
        return null;
    }

    public Boolean isSatelliteEntitlementSupportedBySubId(int i) {
        return isSatelliteEntitlementSupportedByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public Boolean isSatelliteEntitlementSupportedByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mIsEntitlementSupported;
        }
        return null;
    }

    public String getSatelliteEntitlementServerUrlBySubId(int i) {
        return getSatelliteEntitlementServerUrlByCarrierId(SatelliteServiceUtils.getCarrierIdFromSubscription(i));
    }

    public String getSatelliteEntitlementServerUrlByCarrierId(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return satelliteCarrierConfig.mEntitlementServerUrl;
        }
        return null;
    }

    public void overrideSatelliteMaxAllowedDataMode(int i) {
        logd("overrideSatelliteMaxAllowedDataMode: " + i);
        this.mCarrierRoamingMaxAllowedDataMode = Integer.valueOf(i);
    }

    public List<String> getAllSatellitePlmnsForCarrier(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        if (satelliteCarrierConfig != null) {
            return new ArrayList(satelliteCarrierConfig.mProviderCapabilities.keySet());
        }
        return new ArrayList();
    }

    public Map<String, Set<Integer>> getSupportedSatelliteServices(int i) {
        SatelliteCarrierConfig satelliteCarrierConfig = (SatelliteCarrierConfig) this.mSatelliteCarrierConfig.get(Integer.valueOf(i));
        HashMap map = new HashMap();
        if (satelliteCarrierConfig != null) {
            for (Map.Entry entry : satelliteCarrierConfig.mProviderCapabilities.entrySet()) {
                String str = (String) entry.getKey();
                Set allowedServices = ((ProviderCapability) entry.getValue()).getAllowedServices();
                if (!allowedServices.isEmpty()) {
                    map.put(str, allowedServices);
                }
            }
        }
        return map;
    }

    public Set<Integer> getAllSatelliteCarrierIds() {
        if (this.mSatelliteCarrierConfig != null) {
            ArraySet arraySet = new ArraySet(this.mSatelliteCarrierConfig.keySet());
            logd("getAllSatelliteCarrierIds: " + arraySet);
            return arraySet;
        }
        return new ArraySet();
    }

    public List<String> getDeviceSatelliteCountryCodes() {
        List<String> list = this.mSatelliteRegionCountryCodes;
        if (list != null) {
            return list;
        }
        logd("getDeviceSatelliteCountryCodes : mConfigData is null or no config data");
        return new ArrayList();
    }

    public Boolean isSatelliteDataForAllowedRegion() {
        if (this.mIsSatelliteRegionAllowed == null) {
            logd("getIsSatelliteRegionAllowed : mConfigData is null or no config data");
        }
        return this.mIsSatelliteRegionAllowed;
    }

    public List<String> getDeviceSatelliteProviderList() {
        List<String> list = this.mDeviceSatelliteProviders;
        if (list != null) {
            return list;
        }
        logd("getDeviceSatelliteProviderList : mDeviceSatelliteProviders is null");
        return new ArrayList();
    }

    public File getSatelliteS2CellFile(Context context) {
        if (context == null) {
            logd("getSatelliteS2CellFile : context is null");
            return null;
        }
        if (isFileExist(this.mSatS2File)) {
            logd("File mSatS2File is already exist");
            return this.mSatS2File;
        }
        TelephonyConfigData.SatelliteConfigProto satelliteConfigProto = this.mConfigData;
        if (satelliteConfigProto != null && satelliteConfigProto.hasDeviceSatelliteRegion()) {
            File fileCopySatelliteFileToPhoneDirectory = copySatelliteFileToPhoneDirectory(context, this.mConfigData.getDeviceSatelliteRegion().getS2CellFile().toByteArray(), "s2_cell_file");
            this.mSatS2File = fileCopySatelliteFileToPhoneDirectory;
            return fileCopySatelliteFileToPhoneDirectory;
        }
        logd("getSatelliteS2CellFile: mConfigData is null or mConfigData.deviceSatelliteRegion is null");
        return null;
    }

    public File getSatelliteAccessConfigJsonFile(Context context) {
        if (context == null) {
            logd("getSatelliteAccessConfigJsonFile : context is null");
            return null;
        }
        if (isFileExist(this.mSatelliteAccessConfigJsonFile)) {
            logd("File mSatelliteAccessConfigJsonFile is already exist");
            return this.mSatelliteAccessConfigJsonFile;
        }
        TelephonyConfigData.SatelliteConfigProto satelliteConfigProto = this.mConfigData;
        if (satelliteConfigProto != null && satelliteConfigProto.hasDeviceSatelliteRegion()) {
            File fileCopySatelliteFileToPhoneDirectory = copySatelliteFileToPhoneDirectory(context, this.mConfigData.getDeviceSatelliteRegion().getSatelliteAccessConfigFile().toByteArray(), "satelltie_access_config.json");
            this.mSatelliteAccessConfigJsonFile = fileCopySatelliteFileToPhoneDirectory;
            return fileCopySatelliteFileToPhoneDirectory;
        }
        logd("mSatelliteAccessConfigJsonFile: mConfigData is null or mConfigData.deviceSatelliteRegion is null");
        return null;
    }

    public int getSatelliteConfigDataVersion() {
        logd("getSatelliteConfigDataVersion: mVersion: " + this.mVersion);
        return this.mVersion;
    }

    public File copySatelliteFileToPhoneDirectory(Context context, byte[] bArr, String str) {
        if (context == null || bArr == null) {
            logd("copySatelliteFileToPhoneDirectory : context or byteArrayFile are null");
            return null;
        }
        File dir = context.getDir(ConfigProviderAdaptor.DOMAIN_SATELLITE, 0);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Path pathResolve = dir.toPath().resolve(str);
        try {
            Files.copy(new ByteArrayInputStream(bArr), pathResolve, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            loge("copySatelliteFileToPhoneDirectory: ex=" + e);
        }
        logd("targetSatelliteFilePath's path: " + pathResolve.toAbsolutePath().toString());
        return pathResolve.toFile();
    }

    public void cleanOtaResources(Context context) {
        if (context == null) {
            logd("cleanOtaResources : context is null");
            return;
        }
        try {
            File dir = context.getDir(ConfigProviderAdaptor.DOMAIN_SATELLITE, 0);
            if (!dir.exists()) {
                logd("cleanOtaResources: satellite does not exist. No need to clean.");
            } else {
                logd("cleanOtaResources: Deleting contents under satellite");
                FileUtils.deleteContents(dir);
            }
        } catch (Exception e) {
            loge("cleanOtaResources error : " + e);
        }
    }

    public boolean hasSatelliteS2CellFile() {
        TelephonyConfigData.SatelliteConfigProto satelliteConfigProto = this.mConfigData;
        if (satelliteConfigProto != null && satelliteConfigProto.hasDeviceSatelliteRegion() && this.mConfigData.getDeviceSatelliteRegion().hasS2CellFile() && !this.mConfigData.getDeviceSatelliteRegion().getS2CellFile().isEmpty()) {
            logd("hasSatelliteS2CellFile: s2CellFile is exist");
            return true;
        }
        logd("hasSatelliteS2CellFile: s2CellFile is not exist");
        return false;
    }

    public boolean hasSatelliteAccessConfigFile() {
        TelephonyConfigData.SatelliteConfigProto satelliteConfigProto = this.mConfigData;
        if (satelliteConfigProto != null && satelliteConfigProto.hasDeviceSatelliteRegion() && this.mConfigData.getDeviceSatelliteRegion().hasSatelliteAccessConfigFile() && !this.mConfigData.getDeviceSatelliteRegion().getSatelliteAccessConfigFile().isEmpty()) {
            logd("hasSatelliteAccessConfigFile: satelliteAccessConfigFile is exist");
            return true;
        }
        logd("hasSatelliteAccessConfigFile: satelliteAccessConfigFile is not exist");
        return false;
    }

    public boolean isFileExist(File file) {
        if (file == null) {
            logd("isFileExist : file is null");
            return false;
        }
        return file.exists();
    }

    private static void logd(String str) {
        Log.d("SatelliteConfig", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteConfig", str);
    }

    public String toString() {
        return "SatelliteConfig{mVersion=" + this.mVersion + ", mCarrierRoamingMaxAllowedDataMode=" + this.mCarrierRoamingMaxAllowedDataMode + ", mSatelliteRegionCountryCodes=" + this.mSatelliteRegionCountryCodes + ", mIsSatelliteRegionAllowed=" + this.mIsSatelliteRegionAllowed + ", mSatS2File=" + this.mSatS2File + ", mSatelliteAccessConfigJsonFile=" + this.mSatelliteAccessConfigJsonFile + ", mSatelliteCarrierConfig=" + this.mSatelliteCarrierConfig + ", mConfigData=" + this.mConfigData + "}";
    }

    public static class PlmnConfig {
        private final int mNtnConnectType;
        private final String mPlmn;

        public PlmnConfig(String str, int i) {
            this.mPlmn = str;
            this.mNtnConnectType = i;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public int getNtnConnectType() {
            return this.mNtnConnectType;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ProviderCapability {
        private final Set mAllowedServices;
        private final Integer mNtnConnectType;

        ProviderCapability(Integer num, Set set) {
            this.mNtnConnectType = num;
            this.mAllowedServices = set;
        }

        Integer getNtnConnectType() {
            return this.mNtnConnectType;
        }

        Set getAllowedServices() {
            return this.mAllowedServices;
        }

        public String toString() {
            return "ProviderCapability{mNtnConnectType=" + this.mNtnConnectType + ", mAllowedServices=" + this.mAllowedServices + "}";
        }
    }

    private static class SatelliteCarrierConfig {
        Integer mDataSupportMode;
        String mEntitlementServerUrl;
        Boolean mIsAttachSupported;
        Boolean mIsEmergencyMessagingSupported;
        Boolean mIsEntitlementSupported;
        Integer mNtnConnectType;
        Map mProviderCapabilities;

        private SatelliteCarrierConfig() {
            this.mProviderCapabilities = new ConcurrentHashMap();
        }

        public String toString() {
            return "SatelliteCarrierConfig{mProviderCapabilities=" + this.mProviderCapabilities + ", mIsAttachSupported=" + this.mIsAttachSupported + ", mDataSupportMode=" + this.mDataSupportMode + ", mNtnConnectType=" + this.mNtnConnectType + ", mIsEmergencyMessagingSupported=" + this.mIsEmergencyMessagingSupported + ", mIsEntitlementSupported=" + this.mIsEntitlementSupported + ", mEntitlementServerUrl='" + this.mEntitlementServerUrl + "'}";
        }
    }
}
