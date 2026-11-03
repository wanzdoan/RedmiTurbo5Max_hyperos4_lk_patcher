package com.android.libraries.entitlement;

import android.content.Context;
import com.android.libraries.entitlement.eapaka.EapAkaApi;
import com.android.libraries.entitlement.http.HttpResponse;
import com.android.libraries.entitlement.utils.DebugUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ServiceEntitlement {
    private final CarrierConfig carrierConfig;
    private final EapAkaApi eapAkaApi;

    public ServiceEntitlement(Context context, CarrierConfig carrierConfig, int i) {
        this(context, carrierConfig, i, false);
    }

    public ServiceEntitlement(Context context, CarrierConfig carrierConfig, int i, boolean z) {
        this(context, carrierConfig, i, z, DebugUtils.getBypassEapAkaResponse());
    }

    public ServiceEntitlement(Context context, CarrierConfig carrierConfig, int i, boolean z, String str) {
        this.carrierConfig = carrierConfig;
        this.eapAkaApi = new EapAkaApi(context, i, z, str);
    }

    ServiceEntitlement(CarrierConfig carrierConfig, EapAkaApi eapAkaApi) {
        this.carrierConfig = carrierConfig;
        this.eapAkaApi = eapAkaApi;
    }

    public String queryEntitlementStatus(String str, ServiceEntitlementRequest serviceEntitlementRequest) {
        return queryEntitlementStatus(Collections.singletonList(str), serviceEntitlementRequest);
    }

    public String queryEntitlementStatus(List list, ServiceEntitlementRequest serviceEntitlementRequest) {
        return getEntitlementStatusResponse(list, serviceEntitlementRequest).body();
    }

    public HttpResponse getEntitlementStatusResponse(List list, ServiceEntitlementRequest serviceEntitlementRequest) {
        return getEntitlementStatusResponse(list, serviceEntitlementRequest, Collections.EMPTY_MAP);
    }

    public HttpResponse getEntitlementStatusResponse(List list, ServiceEntitlementRequest serviceEntitlementRequest, Map map) {
        return this.eapAkaApi.queryEntitlementStatus(list, this.carrierConfig, serviceEntitlementRequest, map);
    }
}
