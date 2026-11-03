package com.android.internal.telephony.configupdate;

import android.content.Context;
import android.content.Intent;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import com.android.internal.telephony.TelephonyConfigData;
import com.android.internal.telephony.data.DataConfig;
import com.android.internal.telephony.data.DataConfigParser;
import com.android.internal.telephony.data.DataUtils;
import com.android.internal.telephony.satellite.SatelliteConfig;
import com.android.internal.telephony.satellite.SatelliteConfigParser;
import com.android.internal.telephony.satellite.metrics.ConfigUpdaterMetricsStats;
import com.android.internal.telephony.server.updates.ConfigUpdateInstallReceiver;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import libcore.io.IoUtils;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyConfigUpdateInstallReceiver extends ConfigUpdateInstallReceiver implements ConfigProviderAdaptor {
    public static final String BACKUP_VERSION = "backup_version";
    protected static final String NEW_CONFIG_CONTENT_PATH = "new_telephony_config.pb";
    protected static final String UPDATE_DIR = "/data/misc/telephonyconfig";
    protected static final String UPDATE_METADATA_PATH = "metadata/";
    protected static final String VALID_CONFIG_CONTENT_PATH = "valid_telephony_config.pb";
    public static final String VERSION = "version";
    private ConcurrentHashMap mCallbackHashMap;
    protected final Map<String, ConfigParser> mConfigParsers;
    private final ConfigUpdaterMetricsStats mConfigUpdaterMetricsStats;
    private int mOriginalVersion;
    private static final ConfigParser EMPTY_PARSER = new ConfigParser(null) { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver.1
        @Override // com.android.internal.telephony.configupdate.ConfigParser
        protected void parseData(byte[] bArr) {
        }

        @Override // com.android.internal.telephony.configupdate.ConfigParser
        public String getDomain() {
            return "";
        }
    };
    public static TelephonyConfigUpdateInstallReceiver sReceiverAdaptorInstance = new TelephonyConfigUpdateInstallReceiver();

    public static TelephonyConfigUpdateInstallReceiver getInstance() {
        return sReceiverAdaptorInstance;
    }

    public TelephonyConfigUpdateInstallReceiver() {
        super(UPDATE_DIR, NEW_CONFIG_CONTENT_PATH, UPDATE_METADATA_PATH, VERSION);
        this.mCallbackHashMap = new ConcurrentHashMap();
        this.mConfigParsers = new ConcurrentHashMap();
        this.mConfigUpdaterMetricsStats = ConfigUpdaterMetricsStats.getOrCreateInstance();
    }

    public byte[] getContentFromContentPath(File file) {
        try {
            return IoUtils.readFileAsByteArray(file.getCanonicalPath());
        } catch (IOException unused) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "Failed to read current content : " + file);
            return null;
        }
    }

    public boolean isValidSatelliteCarrierConfigData(ConfigParser configParser) {
        SatelliteConfig satelliteConfig = (SatelliteConfig) configParser.getConfig();
        if (satelliteConfig == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "satelliteConfig is null");
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(5);
            return false;
        }
        Iterator<Integer> it = satelliteConfig.getAllSatelliteCarrierIds().iterator();
        while (it.hasNext()) {
            int iIntValue = it.next().intValue();
            Map<String, Set<Integer>> supportedSatelliteServices = satelliteConfig.getSupportedSatelliteServices(iIntValue);
            for (String str : supportedSatelliteServices.keySet()) {
                if (!TelephonyUtils.isValidPlmn(str)) {
                    Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid plmn : " + str);
                    this.mConfigUpdaterMetricsStats.reportCarrierConfigError(7);
                    return false;
                }
                Iterator<Integer> it2 = supportedSatelliteServices.get(str).iterator();
                while (it2.hasNext()) {
                    int iIntValue2 = it2.next().intValue();
                    if (!TelephonyUtils.isValidService(iIntValue2)) {
                        Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid service : " + iIntValue2);
                        this.mConfigUpdaterMetricsStats.reportCarrierConfigError(8);
                        return false;
                    }
                }
                SatelliteConfig.PlmnConfig satellitePlmnConfigByCarrierId = satelliteConfig.getSatellitePlmnConfigByCarrierId(iIntValue, str);
                if (satellitePlmnConfigByCarrierId != null && !isValidNtnConnectType(satellitePlmnConfigByCarrierId.getNtnConnectType())) {
                    Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid ntnConnecttype : " + satellitePlmnConfigByCarrierId.getNtnConnectType());
                    this.mConfigUpdaterMetricsStats.reportCarrierConfigError(14);
                    return false;
                }
            }
            Integer satelliteNtnConnectTypeByCarrierId = satelliteConfig.getSatelliteNtnConnectTypeByCarrierId(iIntValue);
            if (satelliteNtnConnectTypeByCarrierId != null && !isValidNtnConnectType(satelliteNtnConnectTypeByCarrierId.intValue())) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid ntnConnecttype : " + satelliteNtnConnectTypeByCarrierId);
                this.mConfigUpdaterMetricsStats.reportCarrierConfigError(15);
                return false;
            }
            Integer satelliteDataSupportModeByCarrierId = satelliteConfig.getSatelliteDataSupportModeByCarrierId(iIntValue);
            if (satelliteDataSupportModeByCarrierId != null && !isValidDataSupportMode(satelliteDataSupportModeByCarrierId.intValue())) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid dataSupportMode : " + satelliteDataSupportModeByCarrierId);
                this.mConfigUpdaterMetricsStats.reportCarrierConfigError(16);
                return false;
            }
            String satelliteEntitlementServerUrlByCarrierId = satelliteConfig.getSatelliteEntitlementServerUrlByCarrierId(iIntValue);
            if (!TextUtils.isEmpty(satelliteEntitlementServerUrlByCarrierId) && !isValidEntitlementServerUrl(satelliteEntitlementServerUrlByCarrierId)) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "found not properly formatted entitlement url: " + satelliteEntitlementServerUrlByCarrierId);
                this.mConfigUpdaterMetricsStats.reportCarrierConfigError(17);
                return false;
            }
        }
        Log.d("TelephonyConfigUpdateInstallReceiver", "the config data is valid");
        return true;
    }

    public boolean isValidMaxAllowedDataMode(ConfigParser configParser) {
        SatelliteConfig satelliteConfig = (SatelliteConfig) configParser.getConfig();
        if (satelliteConfig == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "satelliteConfig is null");
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(5);
            return false;
        }
        Integer satelliteMaxAllowedDataMode = satelliteConfig.getSatelliteMaxAllowedDataMode();
        if (satelliteMaxAllowedDataMode == null) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "maxAllowedDataMode is not set");
            return true;
        }
        if (DataUtils.isValidDataMode(satelliteMaxAllowedDataMode.intValue())) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "maxAllowedDataMode is valid");
            return true;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "found invalid maxAllowedDataMode : " + satelliteMaxAllowedDataMode);
        this.mConfigUpdaterMetricsStats.reportCarrierConfigError(13);
        return false;
    }

    private boolean isValidDataSupportMode(int i) {
        if (i >= 0 && i <= 2) {
            return true;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "isValidDataSupportMode: dataSupportMode=" + i + " is not valid");
        return false;
    }

    private boolean isValidNtnConnectType(int i) {
        if (i >= 0 && i <= 2) {
            return true;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "isValidNtnConnectType: ntnConnectType=" + i + " is not valid");
        return false;
    }

    private boolean isValidEntitlementServerUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "isValidEntitlementServerUrl: url is empty");
            return false;
        }
        if (URLUtil.isHttpsUrl(str)) {
            return true;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "isValidEntitlementServerUrl: url is not https connection");
        return false;
    }

    public boolean isValidSatelliteProvider(ConfigParser configParser) {
        SatelliteConfig satelliteConfig = (SatelliteConfig) configParser.getConfig();
        if (satelliteConfig == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "isValidSatelliteProvider: satelliteConfig is null");
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(5);
            return false;
        }
        List<String> deviceSatelliteProviderList = satelliteConfig.getDeviceSatelliteProviderList();
        if (deviceSatelliteProviderList == null) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "isValidSatelliteProvider: satelliteProviderList is not set");
            return true;
        }
        for (String str : deviceSatelliteProviderList) {
            if (!TelephonyUtils.isValidPlmn(str)) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "isValidSatelliteProvider: invalid plmn = " + str);
                this.mConfigUpdaterMetricsStats.reportOemConfigError(7);
                return false;
            }
        }
        Log.d("TelephonyConfigUpdateInstallReceiver", "satelliteProviderList is valid");
        return true;
    }

    public boolean isValidDataConfig(ConfigParser configParser) {
        Log.d("TelephonyConfigUpdateInstallReceiver", "isValidDataConfig: parser class=".concat(configParser.getClass().getName()));
        if (!(configParser instanceof DataConfigParser)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "isValidDataConfig: parser is not DataConfigParser!");
            return false;
        }
        DataConfig dataConfig = (DataConfig) configParser.getConfig();
        if (dataConfig == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "dataConfig is null");
            return false;
        }
        TelephonyConfigData.DataConfigProto configData = dataConfig.getConfigData();
        if (!configData.hasConnectionCapabilityConfigs()) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "connection_capability_configs is null");
            return true;
        }
        TelephonyConfigData.ConnectionCapabilityConfig connectionCapabilityConfigs = configData.getConnectionCapabilityConfigs();
        if (connectionCapabilityConfigs.hasDefaultConnectionCapabilityConfig() && !isConnectionCapabilityMapValid(connectionCapabilityConfigs.getDefaultConnectionCapabilityConfig())) {
            return false;
        }
        if (connectionCapabilityConfigs.getCarrierConnectionCapabilityConfigsCount() > 0) {
            Iterator<TelephonyConfigData.ConnectionCapabilityMap> it = connectionCapabilityConfigs.getCarrierConnectionCapabilityConfigsList().iterator();
            while (it.hasNext()) {
                if (!isConnectionCapabilityMapValid(it.next())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isConnectionCapabilityMapValid(TelephonyConfigData.ConnectionCapabilityMap connectionCapabilityMap) {
        if (connectionCapabilityMap.getRulesCount() == 0) {
            return true;
        }
        for (String str : connectionCapabilityMap.getRulesList()) {
            String[] strArrSplit = str.split(":");
            if (strArrSplit.length != 3) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "Invalid rule format: ".concat(str));
                return false;
            }
            try {
                Integer.parseInt(strArrSplit[0]);
                Integer.parseInt(strArrSplit[1]);
                Boolean.parseBoolean(strArrSplit[2]);
            } catch (NumberFormatException unused) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "Invalid number format in rule: ".concat(str));
                return false;
            }
        }
        return true;
    }

    @Override // com.android.internal.telephony.server.updates.ConfigUpdateInstallReceiver
    public void postInstall(Context context, Intent intent) {
        postInstall();
    }

    @Override // com.android.internal.telephony.server.updates.ConfigUpdateInstallReceiver
    protected void writeUpdate(File file, File file2, InputStream inputStream) throws Throwable {
        super.writeUpdate(file, file2, inputStream);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x005d  */
    /* JADX WARN: Code duplicated, block: B:17:0x0083  */
    private void postInstall() {
        boolean z;
        boolean z2;
        final ConfigParser newConfigParser = getNewConfigParser(ConfigProviderAdaptor.DOMAIN_SATELLITE, getContentFromContentPath(this.updateContent));
        final ConfigParser newConfigParser2 = getNewConfigParser(ConfigProviderAdaptor.DOMAIN_DATA, getContentFromContentPath(this.updateContent));
        boolean zIsValidConfig = isValidConfig(newConfigParser);
        boolean zIsValidConfig2 = isValidConfig(newConfigParser2);
        if (!zIsValidConfig && !zIsValidConfig2) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "postInstall: All configs are invalid.");
            return;
        }
        if (zIsValidConfig) {
            getInstance().mConfigParsers.compute(newConfigParser.getDomain(), new BiFunction() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda2
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return this.f$0.lambda$postInstall$0(newConfigParser, (String) obj, (ConfigParser) obj2);
                }
            });
            if (getInstance().mConfigParsers.get(newConfigParser.getDomain()) == newConfigParser) {
                this.mConfigUpdaterMetricsStats.setConfigVersion(newConfigParser.getVersion());
                z = true;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        if (zIsValidConfig2) {
            getInstance().mConfigParsers.compute(newConfigParser2.getDomain(), new BiFunction() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda3
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return this.f$0.lambda$postInstall$1(newConfigParser2, (String) obj, (ConfigParser) obj2);
                }
            });
            z2 = getInstance().mConfigParsers.get(newConfigParser2.getDomain()) == newConfigParser2;
        }
        if ((z || z2) && !copySourceFileToTargetFile(NEW_CONFIG_CONTENT_PATH, VALID_CONFIG_CONTENT_PATH)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "fail to copy to the valid satellite carrier config data");
            if (z) {
                this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(11);
            }
        }
        if (z) {
            notifyConfigChanged(newConfigParser);
        }
        if (z2) {
            notifyConfigChanged(newConfigParser2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ConfigParser lambda$postInstall$0(ConfigParser configParser, String str, ConfigParser configParser2) {
        return isUpgradableVersion(configParser2, configParser) ? configParser : configParser2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ConfigParser lambda$postInstall$1(ConfigParser configParser, String str, ConfigParser configParser2) {
        return isUpgradableVersion(configParser2, configParser) ? configParser : configParser2;
    }

    private void notifyConfigChanged(final ConfigParser configParser) {
        if (getInstance().mCallbackHashMap.isEmpty()) {
            return;
        }
        for (Map.Entry entry : getInstance().mCallbackHashMap.entrySet()) {
            Executor executor = (Executor) entry.getKey();
            final ConfigProviderAdaptor.Callback callback = (ConfigProviderAdaptor.Callback) entry.getValue();
            if (callback != null) {
                executor.execute(new Runnable() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.onChanged(configParser);
                    }
                });
            }
        }
    }

    private boolean isUpgradableVersion(ConfigParser configParser, ConfigParser configParser2) {
        String domain = configParser2.getDomain();
        if (configParser == null || configParser == EMPTY_PARSER) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "domain_" + domain + " is upgradable: never been installed previously.");
            return true;
        }
        int i = configParser2.mVersion;
        int i2 = configParser.mVersion;
        if (i <= i2) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "domain_" + domain + " is not upgradable: updated proto Version [" + i + "] is smaller than previous proto Version [" + i2 + "]");
            if (!domain.equals(ConfigProviderAdaptor.DOMAIN_SATELLITE)) {
                return false;
            }
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(3);
            return false;
        }
        Log.d("TelephonyConfigUpdateInstallReceiver", "domain_" + domain + " is upgradable: previous proto version is " + i2 + " | updated proto version is " + i);
        return true;
    }

    private boolean isValidConfig(ConfigParser configParser) {
        if (configParser == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "isValidConfig: ConfigParser is null");
            return false;
        }
        String domain = configParser.getDomain();
        domain.hashCode();
        if (!domain.equals(ConfigProviderAdaptor.DOMAIN_SATELLITE)) {
            if (!domain.equals(ConfigProviderAdaptor.DOMAIN_DATA)) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "Invalid domain: ".concat(domain));
                return false;
            }
            if (isValidDataConfig(configParser)) {
                return true;
            }
            Log.e("TelephonyConfigUpdateInstallReceiver", "received config data has invalid data config");
            return false;
        }
        if (!isValidSatelliteCarrierConfigData(configParser)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "received config data has invalid satellite carrier config data");
            return false;
        }
        if (!isValidMaxAllowedDataMode(configParser)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "received config data has invalid max allowed data mode");
            return false;
        }
        if (isValidSatelliteProvider(configParser)) {
            return true;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "received config data has invalid satellite plmn list");
        return false;
    }

    @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor
    public ConfigParser getConfigParser(String str) {
        Log.d("TelephonyConfigUpdateInstallReceiver", "getConfigParser: domain=" + str);
        ConfigParser configParserComputeIfAbsent = getInstance().mConfigParsers.computeIfAbsent(str, new Function() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$getConfigParser$3((String) obj);
            }
        });
        if (configParserComputeIfAbsent == EMPTY_PARSER) {
            return null;
        }
        return configParserComputeIfAbsent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ ConfigParser lambda$getConfigParser$3(String str) {
        Log.d("TelephonyConfigUpdateInstallReceiver", "CreateNewConfigParser with domain " + str);
        ConfigParser newConfigParser = getNewConfigParser(str, getContentFromContentPath(new File(this.updateDir, VALID_CONFIG_CONTENT_PATH)));
        return (newConfigParser == null || newConfigParser.getConfig() == null) ? EMPTY_PARSER : newConfigParser;
    }

    public void overrideConfigParser(ConfigParser configParser) {
        if (configParser == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "overrideConfigParser: ConfigParser is null");
        } else {
            Log.d("TelephonyConfigUpdateInstallReceiver", "overrideConfigParser");
            getInstance().mConfigParsers.put(configParser.getDomain(), configParser);
        }
    }

    public void clearOverriddenConfigParser(String str) {
        if (str == null) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "clearOverriddenConfigParser: domain is null");
        } else {
            Log.d("TelephonyConfigUpdateInstallReceiver", "clearOverriddenConfigParser domain_".concat(str));
            getInstance().mConfigParsers.computeIfPresent(str, new BiFunction() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return TelephonyConfigUpdateInstallReceiver.$r8$lambda$iTWJqRQvNvb4ncsEb6ZvxUuJAYE((String) obj, (ConfigParser) obj2);
                }
            });
        }
    }

    public static /* synthetic */ ConfigParser $r8$lambda$iTWJqRQvNvb4ncsEb6ZvxUuJAYE(String str, ConfigParser configParser) {
        return null;
    }

    @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor
    public void registerCallback(Executor executor, ConfigProviderAdaptor.Callback callback) {
        this.mCallbackHashMap.put(executor, callback);
    }

    @Override // com.android.internal.telephony.configupdate.ConfigProviderAdaptor
    public void unregisterCallback(ConfigProviderAdaptor.Callback callback) {
        for (Executor executor : this.mCallbackHashMap.keySet()) {
            if (this.mCallbackHashMap.get(executor) == callback) {
                this.mCallbackHashMap.remove(executor);
                return;
            }
        }
    }

    public File getUpdateDir() {
        return getInstance().updateDir;
    }

    public File getUpdateContent() {
        return getInstance().updateContent;
    }

    public ConcurrentHashMap<Executor, ConfigProviderAdaptor.Callback> getCallbackMap() {
        return getInstance().mCallbackHashMap;
    }

    public void setCallbackMap(ConcurrentHashMap<Executor, ConfigProviderAdaptor.Callback> concurrentHashMap) {
        getInstance().mCallbackHashMap = concurrentHashMap;
    }

    public ConfigParser getNewConfigParser(String str, byte[] bArr) {
        if (bArr == null) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "content data is null");
            return null;
        }
        str.hashCode();
        if (str.equals(ConfigProviderAdaptor.DOMAIN_SATELLITE)) {
            return new SatelliteConfigParser(bArr);
        }
        if (!str.equals(ConfigProviderAdaptor.DOMAIN_DATA)) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "DOMAIN should be specified");
            this.mConfigUpdaterMetricsStats.reportOemAndCarrierConfigError(2);
            return null;
        }
        return new DataConfigParser(bArr);
    }

    public boolean copySourceFileToTargetFile(String str, String str2) {
        try {
            File file = new File(UPDATE_DIR, str);
            File file2 = new File(UPDATE_DIR, str2);
            Log.d("TelephonyConfigUpdateInstallReceiver", "copy " + file.getName() + " >> " + file2.getName());
            if (file.exists()) {
                if (file2.exists()) {
                    file2.delete();
                }
                FileUtils.copy(file, file2);
                FileUtils.copyPermissions(file, file2);
                Log.d("TelephonyConfigUpdateInstallReceiver", "success to copy the file " + file.getName() + " to " + file2.getName());
                return true;
            }
            Log.d("TelephonyConfigUpdateInstallReceiver", "source file is not exist, no file to copy");
            return false;
        } catch (Exception e) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "copy error : " + e);
            return false;
        }
    }

    public boolean cleanUpTelephonyConfigs() {
        Log.d("TelephonyConfigUpdateInstallReceiver", "cleanTelephonyConfigs: resetting the telephony configs");
        try {
            writeUpdate(new File(this.updateDir, UPDATE_METADATA_PATH), this.updateVersion, new ByteArrayInputStream(Integer.toString(-1).getBytes()));
            writeUpdate(this.updateDir, this.updateContent, new ByteArrayInputStream(new byte[0]));
            writeUpdate(this.updateDir, new File(this.updateDir, VALID_CONFIG_CONTENT_PATH), new ByteArrayInputStream(new byte[0]));
            Log.d("TelephonyConfigUpdateInstallReceiver", "cleanTelephonyConfigs: resetting the config parser");
            getInstance().mConfigParsers.clear();
            return true;
        } catch (IOException e) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "Failed to clean telephony config files: " + e);
            return false;
        }
    }

    public boolean overrideVersion(boolean z, int i) {
        Log.d("TelephonyConfigUpdateInstallReceiver", "overrideVersion: reset=" + z + ", version=" + i);
        if (z) {
            i = this.mOriginalVersion;
            if (!restoreContentData()) {
                return false;
            }
        } else {
            this.mOriginalVersion = i;
            if (!backupContentData()) {
                return false;
            }
        }
        return overrideVersion(i);
    }

    private boolean overrideVersion(final int i) throws Throwable {
        try {
            writeUpdate(this.updateDir, this.updateVersion, new ByteArrayInputStream(Long.toString(i).getBytes()));
            getInstance().mConfigParsers.forEach(new BiConsumer() { // from class: com.android.internal.telephony.configupdate.TelephonyConfigUpdateInstallReceiver$$ExternalSyntheticLambda5
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    TelephonyConfigUpdateInstallReceiver.$r8$lambda$MwVtIuX3PeI9aurKQjzYB1XYhFA(i, (String) obj, (ConfigParser) obj2);
                }
            });
            return true;
        } catch (IOException e) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "overrideVersion: e=" + e);
            return false;
        }
    }

    public static /* synthetic */ void $r8$lambda$MwVtIuX3PeI9aurKQjzYB1XYhFA(int i, String str, ConfigParser configParser) {
        if (configParser != EMPTY_PARSER) {
            configParser.overrideVersion(i);
        }
    }

    protected boolean isFileExists(String str) {
        Log.d("TelephonyConfigUpdateInstallReceiver", "isFileExists");
        if (str == null) {
            Log.d("TelephonyConfigUpdateInstallReceiver", "fileName cannot be null");
            return false;
        }
        File file = new File(UPDATE_DIR, str);
        return file.exists() && file.isFile();
    }

    private boolean backupContentData() {
        if (isFileExists(VALID_CONFIG_CONTENT_PATH)) {
            if (!copySourceFileToTargetFile(VALID_CONFIG_CONTENT_PATH, "backup_telephony_config.pb")) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "backupContentData: fail to backup the config data");
                return false;
            }
            if (!copySourceFileToTargetFile("metadata/version", "metadata/backup_version")) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "backupContentData: fail to backup the version");
                return false;
            }
            Log.d("TelephonyConfigUpdateInstallReceiver", "backupContentData: backup success");
            return true;
        }
        Log.d("TelephonyConfigUpdateInstallReceiver", "valid_telephony_config.pb does not exist, no need to backup");
        return true;
    }

    protected void writeContentToFile(File file, File file2, byte[] bArr) throws Throwable {
        writeUpdate(file, file2, new ByteArrayInputStream(bArr));
    }

    protected boolean restoreContentData() throws Throwable {
        if (isFileExists("backup_telephony_config.pb")) {
            if (!copySourceFileToTargetFile("backup_telephony_config.pb", NEW_CONFIG_CONTENT_PATH)) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "restoreContentData: fail to restore the config data");
                return false;
            }
            if (!copySourceFileToTargetFile("metadata/backup_version", "metadata/version")) {
                Log.e("TelephonyConfigUpdateInstallReceiver", "restoreContentData: fail to restore the version");
                return false;
            }
            Log.d("TelephonyConfigUpdateInstallReceiver", "restoreContentData: populate the data to Controllers");
            postInstallForRestore();
            Log.d("TelephonyConfigUpdateInstallReceiver", "restoreContentData: success");
            return true;
        }
        Log.d("TelephonyConfigUpdateInstallReceiver", "backup_telephony_config.pb does not exist. Restoring to empty state.");
        try {
            writeContentToFile(this.updateDir, this.updateContent, new byte[0]);
            postInstallForRestore();
            return true;
        } catch (IOException e) {
            Log.e("TelephonyConfigUpdateInstallReceiver", "restoreContentData: Failed to write empty config: " + e);
            return false;
        }
    }

    protected void postInstallForRestore() {
        Log.d("TelephonyConfigUpdateInstallReceiver", "postInstallForRestore: Force-applying restored config");
        byte[] contentFromContentPath = getContentFromContentPath(this.updateContent);
        ConfigParser newConfigParser = getNewConfigParser(ConfigProviderAdaptor.DOMAIN_SATELLITE, contentFromContentPath);
        ConfigParser newConfigParser2 = getNewConfigParser(ConfigProviderAdaptor.DOMAIN_DATA, contentFromContentPath);
        if (newConfigParser != null) {
            getInstance().mConfigParsers.put(ConfigProviderAdaptor.DOMAIN_SATELLITE, newConfigParser);
            this.mConfigUpdaterMetricsStats.setConfigVersion(newConfigParser.getVersion());
            notifyConfigChanged(newConfigParser);
        }
        if (newConfigParser2 != null) {
            getInstance().mConfigParsers.put(ConfigProviderAdaptor.DOMAIN_DATA, newConfigParser2);
            notifyConfigChanged(newConfigParser2);
        }
        if (copySourceFileToTargetFile(NEW_CONFIG_CONTENT_PATH, VALID_CONFIG_CONTENT_PATH)) {
            return;
        }
        Log.e("TelephonyConfigUpdateInstallReceiver", "postInstallForRestore: failed to copy to valid config path");
    }
}
