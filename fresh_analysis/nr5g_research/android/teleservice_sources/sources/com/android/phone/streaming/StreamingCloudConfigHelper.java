package com.android.phone.streaming;

import android.provider.MiuiSettings;
import android.util.Log;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class StreamingCloudConfigHelper implements BaseEvent.IEventListener {
    private static final AtomicReference sConfigCache = new AtomicReference(CloudConfig.DEFAULT);
    private final OnCloudConfigChangeListener mListener;

    public interface OnCloudConfigChangeListener {
        void onCloudConfigChanged(boolean z, boolean z2, boolean z3);
    }

    @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            handleCloudUpdate();
        }
    }

    public final class CloudConfig {
        public static final CloudConfig DEFAULT = new CloudConfig(true, true, true);
        public final boolean congestionEnabled;
        public final boolean mainEnabled;
        public final boolean overseasTravelEnabled;

        public CloudConfig(boolean z, boolean z2, boolean z3) {
            this.mainEnabled = z;
            this.overseasTravelEnabled = z2;
            this.congestionEnabled = z3;
        }

        public String toString() {
            return "CloudConfig{main=" + this.mainEnabled + ", overseasTravel=" + this.overseasTravelEnabled + ", congestion=" + this.congestionEnabled;
        }
    }

    public StreamingCloudConfigHelper(OnCloudConfigChangeListener onCloudConfigChangeListener) {
        this.mListener = onCloudConfigChangeListener;
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", this);
        CloudConfig cloudConfigFetchCloudConfigSync = fetchCloudConfigSync();
        sConfigCache.set(cloudConfigFetchCloudConfigSync);
        Log.i("StreamingCloudConfigHelper", "Initialized with config: " + cloudConfigFetchCloudConfigSync);
        if (onCloudConfigChangeListener != null) {
            onCloudConfigChangeListener.onCloudConfigChanged(cloudConfigFetchCloudConfigSync.mainEnabled, cloudConfigFetchCloudConfigSync.overseasTravelEnabled, cloudConfigFetchCloudConfigSync.congestionEnabled);
        }
    }

    public static boolean isOverseasTravelEnabled() {
        return getConfig().overseasTravelEnabled;
    }

    public static boolean isCongestionEnabled() {
        return getConfig().congestionEnabled;
    }

    private static CloudConfig getConfig() {
        return (CloudConfig) sConfigCache.get();
    }

    private static CloudConfig fetchCloudConfigSync() {
        try {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            if (phoneGlobals == null) {
                Log.w("StreamingCloudConfigHelper", "Context is null, return default config");
                return CloudConfig.DEFAULT;
            }
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(phoneGlobals.getContentResolver(), "TelephonyCommonFeature");
            if (cloudDataList != null && !cloudDataList.isEmpty()) {
                return parseCloudData(cloudDataList);
            }
            Log.w("StreamingCloudConfigHelper", "Cloud data list is empty, return default config");
            return CloudConfig.DEFAULT;
        } catch (Exception e) {
            Log.e("StreamingCloudConfigHelper", "Failed to fetch cloud config", e);
            return CloudConfig.DEFAULT;
        }
    }

    private static CloudConfig parseCloudData(List list) {
        return buildFinalConfig(extractRawConfig(list));
    }

    private static RawCloudConfig extractRawConfig(List list) {
        RawCloudConfig rawCloudConfig = new RawCloudConfig();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            JSONObject jSONObjectJson = ((MiuiSettings.SettingsCloudData.CloudData) it.next()).json();
            if (jSONObjectJson != null) {
                rawCloudConfig.tryExtractMain(jSONObjectJson);
                rawCloudConfig.tryExtractOverseasTravel(jSONObjectJson);
                rawCloudConfig.tryExtractCongestion(jSONObjectJson);
                if (rawCloudConfig.isComplete()) {
                    break;
                }
            }
        }
        return rawCloudConfig;
    }

    private static CloudConfig buildFinalConfig(RawCloudConfig rawCloudConfig) {
        boolean mainEnabled = rawCloudConfig.getMainEnabled();
        boolean overseasTravelEnabled = rawCloudConfig.getOverseasTravelEnabled(mainEnabled);
        boolean congestionEnabled = rawCloudConfig.getCongestionEnabled(mainEnabled);
        if (!mainEnabled) {
            Log.d("StreamingCloudConfigHelper", "Main switch disabled, all sub-features disabled");
            overseasTravelEnabled = false;
            congestionEnabled = false;
        }
        return new CloudConfig(mainEnabled, overseasTravelEnabled, congestionEnabled);
    }

    class RawCloudConfig {
        private Boolean congestionEnabled;
        private Boolean mainEnabled;
        private Boolean overseasTravelEnabled;

        private RawCloudConfig() {
            this.mainEnabled = null;
            this.overseasTravelEnabled = null;
            this.congestionEnabled = null;
        }

        void tryExtractMain(JSONObject jSONObject) {
            if (this.mainEnabled == null && jSONObject.has("streaming_opt_enable")) {
                this.mainEnabled = Boolean.valueOf(jSONObject.optBoolean("streaming_opt_enable", false));
            }
        }

        void tryExtractOverseasTravel(JSONObject jSONObject) {
            if (this.overseasTravelEnabled == null && jSONObject.has("streaming_opt_overseas_travel_enable")) {
                this.overseasTravelEnabled = Boolean.valueOf(jSONObject.optBoolean("streaming_opt_overseas_travel_enable", false));
            }
        }

        void tryExtractCongestion(JSONObject jSONObject) {
            if (this.congestionEnabled == null && jSONObject.has("streaming_opt_congestion_enable")) {
                this.congestionEnabled = Boolean.valueOf(jSONObject.optBoolean("streaming_opt_congestion_enable", false));
            }
        }

        boolean isComplete() {
            return (this.mainEnabled == null || this.overseasTravelEnabled == null || this.congestionEnabled == null) ? false : true;
        }

        boolean getMainEnabled() {
            Boolean bool = this.mainEnabled;
            if (bool == null) {
                Log.d("StreamingCloudConfigHelper", "Main switch not found, default to enabled");
                return true;
            }
            return bool.booleanValue();
        }

        boolean getOverseasTravelEnabled(boolean z) {
            Boolean bool = this.overseasTravelEnabled;
            if (bool == null) {
                Log.d("StreamingCloudConfigHelper", "Overseas switch not found, follow main switch");
                return z;
            }
            return bool.booleanValue();
        }

        boolean getCongestionEnabled(boolean z) {
            Boolean bool = this.congestionEnabled;
            if (bool == null) {
                Log.d("StreamingCloudConfigHelper", "Congestion switch not found, follow main switch");
                return z;
            }
            return bool.booleanValue();
        }
    }

    public void handleCloudUpdate() {
        OnCloudConfigChangeListener onCloudConfigChangeListener;
        AtomicReference atomicReference = sConfigCache;
        CloudConfig cloudConfig = (CloudConfig) atomicReference.get();
        CloudConfig cloudConfigFetchCloudConfigSync = fetchCloudConfigSync();
        atomicReference.set(cloudConfigFetchCloudConfigSync);
        Log.i("StreamingCloudConfigHelper", "onCloudUpdate: " + cloudConfig + " -> " + cloudConfigFetchCloudConfigSync);
        if (!hasConfigChanged(cloudConfig, cloudConfigFetchCloudConfigSync) || (onCloudConfigChangeListener = this.mListener) == null) {
            return;
        }
        onCloudConfigChangeListener.onCloudConfigChanged(cloudConfigFetchCloudConfigSync.mainEnabled, cloudConfigFetchCloudConfigSync.overseasTravelEnabled, cloudConfigFetchCloudConfigSync.congestionEnabled);
    }

    private static boolean hasConfigChanged(CloudConfig cloudConfig, CloudConfig cloudConfig2) {
        return (cloudConfig != null && cloudConfig2 != null && cloudConfig.mainEnabled == cloudConfig2.mainEnabled && cloudConfig.overseasTravelEnabled == cloudConfig2.overseasTravelEnabled && cloudConfig.congestionEnabled == cloudConfig2.congestionEnabled) ? false : true;
    }
}
