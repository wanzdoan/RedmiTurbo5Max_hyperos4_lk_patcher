package com.android.internal.telephony.data;

import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.internal.telephony.TelephonyConfigData;
import com.android.telephony.Rlog;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class DataConfig {
    private final TelephonyConfigData.DataConfigProto mConfigData;
    private final int mVersion;
    private final Map mConnectionCapabilities = new ArrayMap();
    private final Map mApnRequiredMap = new ArrayMap();
    private final Map mHomeMeteredCapabilities = new ArrayMap();
    private final Map mRoamingMeteredCapabilities = new ArrayMap();
    private final Set mAllNetworkCapabilities = new HashSet();

    public DataConfig(TelephonyConfigData.DataConfigProto dataConfigProto) {
        logd("DataConfig: constructing with configData version: " + dataConfigProto.getVersion());
        this.mVersion = dataConfigProto.getVersion();
        this.mConfigData = dataConfigProto;
        if (dataConfigProto.hasConnectionCapabilityConfigs()) {
            TelephonyConfigData.ConnectionCapabilityConfig connectionCapabilityConfigs = dataConfigProto.getConnectionCapabilityConfigs();
            if (connectionCapabilityConfigs.hasDefaultConnectionCapabilityConfig()) {
                parseAndStoreConnectionCapabilityRules(0, connectionCapabilityConfigs.getDefaultConnectionCapabilityConfig().getRulesList());
            }
            for (TelephonyConfigData.ConnectionCapabilityMap connectionCapabilityMap : connectionCapabilityConfigs.getCarrierConnectionCapabilityConfigsList()) {
                if (connectionCapabilityMap.hasCarrierId() && connectionCapabilityMap.getCarrierId() > 0) {
                    parseAndStoreConnectionCapabilityRules(connectionCapabilityMap.getCarrierId(), connectionCapabilityMap.getRulesList());
                }
            }
        }
        if (dataConfigProto.hasHomeMeteredCapabilityConfigs()) {
            TelephonyConfigData.MeteredCapabilityConfig homeMeteredCapabilityConfigs = dataConfigProto.getHomeMeteredCapabilityConfigs();
            if (homeMeteredCapabilityConfigs.hasDefaultMeteredCapabilityConfig()) {
                this.mHomeMeteredCapabilities.put(0, new HashSet(homeMeteredCapabilityConfigs.getDefaultMeteredCapabilityConfig().getCapabilityIdsList()));
            }
            for (TelephonyConfigData.MeteredCapabilities meteredCapabilities : homeMeteredCapabilityConfigs.getCarrierMeteredCapabilityConfigsList()) {
                if (meteredCapabilities.hasCarrierId() && meteredCapabilities.getCarrierId() > 0) {
                    this.mHomeMeteredCapabilities.put(Integer.valueOf(meteredCapabilities.getCarrierId()), new HashSet(meteredCapabilities.getCapabilityIdsList()));
                }
            }
        }
        if (dataConfigProto.hasRoamMeteredCapabilityConfigs()) {
            TelephonyConfigData.MeteredCapabilityConfig roamMeteredCapabilityConfigs = dataConfigProto.getRoamMeteredCapabilityConfigs();
            if (roamMeteredCapabilityConfigs.hasDefaultMeteredCapabilityConfig()) {
                this.mRoamingMeteredCapabilities.put(0, new HashSet(roamMeteredCapabilityConfigs.getDefaultMeteredCapabilityConfig().getCapabilityIdsList()));
            }
            for (TelephonyConfigData.MeteredCapabilities meteredCapabilities2 : roamMeteredCapabilityConfigs.getCarrierMeteredCapabilityConfigsList()) {
                if (meteredCapabilities2.hasCarrierId() && meteredCapabilities2.getCarrierId() > 0) {
                    this.mRoamingMeteredCapabilities.put(Integer.valueOf(meteredCapabilities2.getCarrierId()), new HashSet(meteredCapabilities2.getCapabilityIdsList()));
                }
            }
        }
    }

    public TelephonyConfigData.DataConfigProto getConfigData() {
        return this.mConfigData;
    }

    private void parseAndStoreConnectionCapabilityRules(int i, List list) {
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (!TextUtils.isEmpty(str)) {
                String[] strArrSplit = str.split(":");
                if (strArrSplit.length == 3) {
                    try {
                        int i2 = Integer.parseInt(strArrSplit[0]);
                        int i3 = Integer.parseInt(strArrSplit[1]);
                        boolean z = Boolean.parseBoolean(strArrSplit[2]);
                        arrayMap.put(Integer.valueOf(i2), Integer.valueOf(i3));
                        arrayMap2.put(Integer.valueOf(i2), Boolean.valueOf(z));
                    } catch (NumberFormatException unused) {
                        Rlog.e("DataConfig", "Invalid format in rule: ".concat(str));
                    }
                }
            }
        }
        this.mConnectionCapabilities.put(Integer.valueOf(i), arrayMap);
        this.mApnRequiredMap.put(Integer.valueOf(i), arrayMap2);
        this.mAllNetworkCapabilities.addAll(arrayMap.keySet());
    }

    public int getVersion() {
        return this.mVersion;
    }

    public Map<Integer, Integer> getConnectionCapabilities(int i) {
        boolean zContainsKey = this.mConnectionCapabilities.containsKey(Integer.valueOf(i));
        Map map = this.mConnectionCapabilities;
        if (zContainsKey) {
            return (Map) map.get(Integer.valueOf(i));
        }
        return (Map) map.getOrDefault(0, null);
    }

    public Map<Integer, Boolean> getApnRequired(int i) {
        boolean zContainsKey = this.mApnRequiredMap.containsKey(Integer.valueOf(i));
        Map map = this.mApnRequiredMap;
        if (zContainsKey) {
            return (Map) map.get(Integer.valueOf(i));
        }
        return (Map) map.getOrDefault(0, null);
    }

    public Set<Integer> getMeteredNetworkCapabilities(int i, boolean z) {
        Map map = z ? this.mRoamingMeteredCapabilities : this.mHomeMeteredCapabilities;
        if (map.containsKey(Integer.valueOf(i))) {
            return (Set) map.get(Integer.valueOf(i));
        }
        return (Set) map.getOrDefault(0, null);
    }

    public Set<Integer> getAllNetworkCapabilities() {
        return this.mAllNetworkCapabilities;
    }

    private Set getConfiguredCarrierIds() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.mConnectionCapabilities.keySet());
        hashSet.addAll(this.mApnRequiredMap.keySet());
        hashSet.addAll(this.mHomeMeteredCapabilities.keySet());
        hashSet.addAll(this.mRoamingMeteredCapabilities.keySet());
        return hashSet;
    }

    private static void logd(String str) {
        Rlog.d("DataConfig", str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            DataConfig dataConfig = (DataConfig) obj;
            if (this.mConnectionCapabilities.equals(dataConfig.mConnectionCapabilities) && this.mApnRequiredMap.equals(dataConfig.mApnRequiredMap) && this.mHomeMeteredCapabilities.equals(dataConfig.mHomeMeteredCapabilities) && this.mRoamingMeteredCapabilities.equals(dataConfig.mRoamingMeteredCapabilities)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mConnectionCapabilities, this.mApnRequiredMap, this.mHomeMeteredCapabilities, this.mRoamingMeteredCapabilities);
    }

    public static class DataConfigDiff {
        private final Map mAffectedCarrierCapabilities = new HashMap();

        public DataConfigDiff(Map<Integer, Set<Integer>> map) {
            for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
                this.mAffectedCarrierCapabilities.put(entry.getKey(), new HashSet(entry.getValue()));
            }
        }

        public boolean isConnectionCapabilityAffected(int i, Set<Integer> set) {
            if (isAffectedInternal(i, set)) {
                return true;
            }
            return isAffectedInternal(0, set);
        }

        private boolean isAffectedInternal(int i, Set set) {
            Set set2 = (Set) this.mAffectedCarrierCapabilities.get(Integer.valueOf(i));
            return (set2 == null || Collections.disjoint(set2, set)) ? false : true;
        }

        public String toString() {
            return "DataConfigDiff{affectedCarrierCapabilities=" + this.mAffectedCarrierCapabilities + '}';
        }
    }

    public static DataConfigDiff calculateDiff(DataConfig dataConfig, DataConfig dataConfig2) {
        HashMap map = new HashMap();
        if (dataConfig == null && dataConfig2 == null) {
            return new DataConfigDiff(map);
        }
        if (dataConfig == null) {
            for (Integer num : dataConfig2.getConfiguredCarrierIds()) {
                num.intValue();
                map.put(num, new HashSet(dataConfig2.getAllNetworkCapabilities()));
            }
            return new DataConfigDiff(map);
        }
        if (dataConfig2 == null) {
            for (Integer num2 : dataConfig.getConfiguredCarrierIds()) {
                num2.intValue();
                map.put(num2, new HashSet(dataConfig.getAllNetworkCapabilities()));
            }
            return new DataConfigDiff(map);
        }
        HashSet<Integer> hashSet = new HashSet();
        hashSet.addAll(dataConfig.getConfiguredCarrierIds());
        hashSet.addAll(dataConfig2.getConfiguredCarrierIds());
        for (Integer num3 : hashSet) {
            int iIntValue = num3.intValue();
            Map<Integer, Integer> connectionCapabilities = dataConfig.getConnectionCapabilities(iIntValue);
            Map<Integer, Integer> connectionCapabilities2 = dataConfig2.getConnectionCapabilities(iIntValue);
            Map<Integer, Boolean> apnRequired = dataConfig.getApnRequired(iIntValue);
            Map<Integer, Boolean> apnRequired2 = dataConfig2.getApnRequired(iIntValue);
            HashSet hashSet2 = new HashSet();
            HashSet<Integer> hashSet3 = new HashSet();
            if (connectionCapabilities != null) {
                hashSet3.addAll(connectionCapabilities.keySet());
            }
            if (connectionCapabilities2 != null) {
                hashSet3.addAll(connectionCapabilities2.keySet());
            }
            if (apnRequired != null) {
                hashSet3.addAll(apnRequired.keySet());
            }
            if (apnRequired2 != null) {
                hashSet3.addAll(apnRequired2.keySet());
            }
            for (Integer num4 : hashSet3) {
                num4.intValue();
                Integer num5 = connectionCapabilities != null ? connectionCapabilities.get(num4) : null;
                Integer num6 = connectionCapabilities2 != null ? connectionCapabilities2.get(num4) : null;
                Boolean bool = apnRequired != null ? apnRequired.get(num4) : null;
                Boolean bool2 = apnRequired2 != null ? apnRequired2.get(num4) : null;
                if (!Objects.equals(num5, num6) || !Objects.equals(bool, bool2)) {
                    hashSet2.add(num4);
                }
            }
            if (!hashSet2.isEmpty()) {
                map.put(num3, hashSet2);
            }
        }
        return new DataConfigDiff(map);
    }
}
