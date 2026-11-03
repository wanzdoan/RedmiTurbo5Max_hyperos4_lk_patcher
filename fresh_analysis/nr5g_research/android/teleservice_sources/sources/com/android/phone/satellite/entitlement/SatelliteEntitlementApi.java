package com.android.phone.satellite.entitlement;

import android.content.Context;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.satellite.SatelliteConfig;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.libraries.entitlement.CarrierConfig;
import com.android.libraries.entitlement.ServiceEntitlement;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.libraries.entitlement.ServiceEntitlementRequest;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteEntitlementApi {
    public static final int CONFIGURATION_VERSION = 1;
    public static final String ENTITLEMENT_VERSION = "12.0";
    private final PersistableBundle mCarrierConfig;
    private final Context mContext;
    private final ServiceEntitlement mServiceEntitlement;
    private String mOverriddenEntilementStatusResponse = null;
    private boolean mShouldThrowExceptionForCtsTest = false;

    public SatelliteEntitlementApi(Context context, PersistableBundle persistableBundle, int i) {
        this.mContext = context;
        this.mServiceEntitlement = new ServiceEntitlement(context, getCarrierConfigFromEntitlementServerUrl(persistableBundle, i), i);
        this.mCarrierConfig = persistableBundle;
    }

    public SatelliteEntitlementResult checkEntitlementStatus() throws ServiceEntitlementException {
        ServiceEntitlementRequest.Builder builder = ServiceEntitlementRequest.builder();
        builder.setAcceptContentType("application/json");
        builder.setAppName(getSatelliteEntitlementAppName(this.mCarrierConfig));
        builder.setEntitlementVersion(ENTITLEMENT_VERSION);
        builder.setConfigurationVersion(1);
        try {
            SatelliteEntitlementResponse satelliteEntitlementResponse = new SatelliteEntitlementResponse(queryEntitlementStatus("ap2016", builder.build()));
            return new SatelliteEntitlementResult(satelliteEntitlementResponse.getEntitlementStatus(), satelliteEntitlementResponse.getPlmnAllowed(), satelliteEntitlementResponse.getPlmnBarredList());
        } catch (Throwable th) {
            throw new ServiceEntitlementException(32, "Failed to parse entitlement response", th);
        }
    }

    void overrideEntilementStatusResponseForCtsTest(String str, boolean z) {
        this.mOverriddenEntilementStatusResponse = str;
        this.mShouldThrowExceptionForCtsTest = z;
    }

    private String getEntitlementServerUrlFromSatelliteConfig(int i) {
        SatelliteConfig satelliteConfig = SatelliteController.getInstance().getSatelliteConfig();
        if (satelliteConfig == null) {
            Log.d("SatelliteEntitlementApi", "getEntitlementServerUrlFromSatelliteConfig return null (SatelliteConfig is null)");
            return null;
        }
        return satelliteConfig.getSatelliteEntitlementServerUrlBySubId(i);
    }

    private String getSatelliteEntitlementServerUrl(PersistableBundle persistableBundle, int i) {
        String entitlementServerUrlFromSatelliteConfig = getEntitlementServerUrlFromSatelliteConfig(i);
        if (!TextUtils.isEmpty(entitlementServerUrlFromSatelliteConfig)) {
            Log.d("SatelliteEntitlementApi", "getSatelliteEntitlementServerUrl: using SatelliteConfig for subId=" + i + ", entitlementServerUrl=" + entitlementServerUrlFromSatelliteConfig);
            return entitlementServerUrlFromSatelliteConfig;
        }
        return persistableBundle.getString("imsserviceentitlement.entitlement_server_url_string", "");
    }

    private CarrierConfig getCarrierConfigFromEntitlementServerUrl(PersistableBundle persistableBundle, int i) {
        return CarrierConfig.builder().setServerUrl(getSatelliteEntitlementServerUrl(persistableBundle, i)).build();
    }

    private String getSatelliteEntitlementAppName(PersistableBundle persistableBundle) {
        return persistableBundle.getString("satellite_entitlement_app_name_string", "androidSatmode");
    }

    private String queryEntitlementStatus(String str, ServiceEntitlementRequest serviceEntitlementRequest) throws ServiceEntitlementException {
        if (this.mShouldThrowExceptionForCtsTest) {
            throw new ServiceEntitlementException(22, "Throw exception for CTS test");
        }
        if (!TextUtils.isEmpty(this.mOverriddenEntilementStatusResponse)) {
            Log.d("SatelliteEntitlementApi", "queryEntitlementStatus: return the overridden response for CTS tests, response=" + this.mOverriddenEntilementStatusResponse);
            return this.mOverriddenEntilementStatusResponse;
        }
        return this.mServiceEntitlement.queryEntitlementStatus(str, serviceEntitlementRequest);
    }
}
