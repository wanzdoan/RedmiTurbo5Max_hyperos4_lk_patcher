package com.android.internal.telephony.satellite;

import android.telephony.NetworkRegistrationInfo;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class NtnCapabilityResolver {
    public static void resolveNtnCapability(NetworkRegistrationInfo networkRegistrationInfo, int i) {
        String registeredPlmn = networkRegistrationInfo.getRegisteredPlmn();
        if (TextUtils.isEmpty(registeredPlmn)) {
            logd("Registered PLMN is empty, skip NTN capability resolution");
            return;
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        Set<String> allPlmnSet = satelliteController.getAllPlmnSet();
        boolean zIsNonTerrestrialNetwork = networkRegistrationInfo.isNonTerrestrialNetwork();
        boolean zIsDtcSatelliteTechnologySupported = satelliteController.isDtcSatelliteTechnologySupported(i, registeredPlmn);
        logd("isNtn=" + zIsNonTerrestrialNetwork);
        logd("isDtcSupported=" + zIsDtcSatelliteTechnologySupported);
        for (String str : allPlmnSet) {
            if (zIsNonTerrestrialNetwork || (TextUtils.equals(str, registeredPlmn) && zIsDtcSatelliteTechnologySupported)) {
                networkRegistrationInfo.setIsNonTerrestrialNetwork(true);
                List<Integer> supportedSatelliteServicesForPlmn = satelliteController.getSupportedSatelliteServicesForPlmn(i, registeredPlmn);
                networkRegistrationInfo.setAvailableServices(supportedSatelliteServicesForPlmn);
                logd("Registered to satellite PLMN " + registeredPlmn + ", supportedServices = " + supportedSatelliteServicesForPlmn);
                if (networkRegistrationInfo.getSatelliteTechnology() == 0) {
                    networkRegistrationInfo.setSatelliteTechnology(resolveSatelliteTechnology(networkRegistrationInfo, i, registeredPlmn));
                    return;
                }
                return;
            }
        }
    }

    private static int resolveSatelliteTechnology(NetworkRegistrationInfo networkRegistrationInfo, int i, String str) {
        logd("resolveSatelliteTechnology");
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController.isSatelliteEnabledOrBeingEnabled()) {
            logd("resolveSatelliteTechnology: return NT_RADIO_TECHNOLOGY_NB_IOT_NTN");
            return 1;
        }
        int accessNetworkTechnology = networkRegistrationInfo.getAccessNetworkTechnology();
        logd("resolveSatelliteTechnology: supportedSatelliteTechList=" + satelliteController.getSupportedSatelliteTechnologies(i, str));
        if (accessNetworkTechnology == 13) {
            logd("resolveSatelliteTechnology: return NT_RADIO_TECHNOLOGY_LTE_DTC");
            return 5;
        }
        if (accessNetworkTechnology == 20) {
            logd("resolveSatelliteTechnology: return NT_RADIO_TECHNOLOGY_NR_DTC");
            return 6;
        }
        logd("resolveSatelliteTechnology: return NT_RADIO_TECHNOLOGY_UNKNOWN");
        return 0;
    }

    private static void logd(String str) {
        Log.d("NtnCapabilityResolver", str);
    }
}
