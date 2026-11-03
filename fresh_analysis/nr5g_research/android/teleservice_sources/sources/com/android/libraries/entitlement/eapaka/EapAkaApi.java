package com.android.libraries.entitlement.eapaka;

import android.content.Context;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.libraries.entitlement.CarrierConfig;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.libraries.entitlement.ServiceEntitlementRequest;
import com.android.libraries.entitlement.http.HttpClient;
import com.android.libraries.entitlement.http.HttpCookieJar;
import com.android.libraries.entitlement.http.HttpRequest;
import com.android.libraries.entitlement.http.HttpResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class EapAkaApi {
    private final String mAppVersion;
    private final String mBypassEapAkaResponse;
    private final Context mContext;
    private final HttpClient mHttpClient;
    private final int mSimSubscriptionId;
    private final TelephonyManager mTelephonyManager;

    public EapAkaApi(Context context, int i, boolean z, String str) {
        this(context, i, new HttpClient(z), str);
    }

    EapAkaApi(Context context, int i, HttpClient httpClient, String str) {
        this.mContext = context;
        this.mSimSubscriptionId = i;
        this.mHttpClient = httpClient;
        this.mBypassEapAkaResponse = str;
        this.mAppVersion = getAppVersion(context);
        this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    public HttpResponse queryEntitlementStatus(List list, CarrierConfig carrierConfig, ServiceEntitlementRequest serviceEntitlementRequest, Map map) throws ServiceEntitlementException {
        JSONObject jSONObject;
        HttpResponse httpResponseHttpGet;
        Uri.Builder builder = null;
        if (carrierConfig.useHttpPost()) {
            JSONObject jSONObject2 = new JSONObject();
            appendParametersForAuthentication(jSONObject2, serviceEntitlementRequest, carrierConfig);
            appendParametersForServiceEntitlementRequest(jSONObject2, list, serviceEntitlementRequest);
            jSONObject = jSONObject2;
        } else {
            Uri.Builder builderBuildUpon = Uri.parse(carrierConfig.serverUrl()).buildUpon();
            appendParametersForAuthentication(builderBuildUpon, serviceEntitlementRequest, carrierConfig);
            appendParametersForServiceEntitlementRequest(builderBuildUpon, list, serviceEntitlementRequest);
            jSONObject = null;
            builder = builderBuildUpon;
        }
        String userAgent = getUserAgent(carrierConfig.clientTs43(), serviceEntitlementRequest.terminalVendor(), serviceEntitlementRequest.terminalModel(), serviceEntitlementRequest.terminalSoftwareVersion());
        if (!TextUtils.isEmpty(serviceEntitlementRequest.authenticationToken())) {
            Log.d("ServiceEntitlement", "Fast Re-Authentication");
            if (carrierConfig.useHttpPost()) {
                Objects.requireNonNull(jSONObject);
                return httpPost(jSONObject, carrierConfig, serviceEntitlementRequest.acceptContentType(), userAgent, map);
            }
            Uri.Builder builder2 = builder;
            Objects.requireNonNull(builder2);
            return httpGet(builder2.toString(), carrierConfig, serviceEntitlementRequest.acceptContentType(), userAgent, map);
        }
        Uri.Builder builder3 = builder;
        Log.d("ServiceEntitlement", "Full Authentication");
        if (carrierConfig.useHttpPost()) {
            Objects.requireNonNull(jSONObject);
            httpResponseHttpGet = httpPost(jSONObject, carrierConfig, "application/vnd.gsma.eap-relay.v1.0+json", userAgent, map);
        } else {
            Objects.requireNonNull(builder3);
            httpResponseHttpGet = httpGet(builder3.toString(), carrierConfig, "application/vnd.gsma.eap-relay.v1.0+json", userAgent, map);
        }
        String eapAkaChallenge = getEapAkaChallenge(httpResponseHttpGet);
        if (eapAkaChallenge == null) {
            throw new ServiceEntitlementException(32, "Failed to parse EAP-AKA challenge: " + httpResponseHttpGet.body());
        }
        List cookieHeaders = HttpCookieJar.parseSetCookieHeaders(httpResponseHttpGet.cookies()).toCookieHeaders();
        if (carrierConfig.preflattenCookies()) {
            cookieHeaders = Collections.singletonList(String.join("; ", cookieHeaders));
        }
        return respondToEapAkaChallenge(carrierConfig, eapAkaChallenge, cookieHeaders, 3, serviceEntitlementRequest.acceptContentType(), userAgent, map);
    }

    private HttpResponse respondToEapAkaChallenge(CarrierConfig carrierConfig, String str, List list, int i, String str2, String str3, Map map) throws ServiceEntitlementException {
        if (!this.mBypassEapAkaResponse.isEmpty()) {
            return challengeResponse(this.mBypassEapAkaResponse, carrierConfig, list, "application/vnd.gsma.eap-relay.v1.0+json, " + str2, str3, map);
        }
        EapAkaResponse eapAkaResponseRespondToEapAkaChallenge = EapAkaResponse.respondToEapAkaChallenge(this.mContext, this.mSimSubscriptionId, EapAkaChallenge.parseEapAkaChallenge(str), carrierConfig.eapAkaRealm());
        if (eapAkaResponseRespondToEapAkaChallenge.response() != null) {
            HttpResponse httpResponseChallengeResponse = challengeResponse(eapAkaResponseRespondToEapAkaChallenge.response(), carrierConfig, list, "application/vnd.gsma.eap-relay.v1.0+json, " + str2, str3, map);
            String eapAkaChallenge = getEapAkaChallenge(httpResponseChallengeResponse);
            if (eapAkaChallenge == null) {
                return httpResponseChallengeResponse;
            }
            Log.d("ServiceEntitlement", "Received another challenge");
            if (i > 0) {
                return respondToEapAkaChallenge(carrierConfig, eapAkaChallenge, list, i - 1, str2, str3, map);
            }
            throw new ServiceEntitlementException(22, "Unable to EAP-AKA authenticate");
        }
        if (eapAkaResponseRespondToEapAkaChallenge.synchronizationFailureResponse() != null) {
            Log.d("ServiceEntitlement", "synchronization failure");
            HttpResponse httpResponseChallengeResponse2 = challengeResponse(eapAkaResponseRespondToEapAkaChallenge.synchronizationFailureResponse(), carrierConfig, list, "application/vnd.gsma.eap-relay.v1.0+json", str3, map);
            String eapAkaChallenge2 = getEapAkaChallenge(httpResponseChallengeResponse2);
            if (eapAkaChallenge2 != null) {
                if (i > 0) {
                    return respondToEapAkaChallenge(carrierConfig, eapAkaChallenge2, list, i - 1, str2, str3, map);
                }
                throw new ServiceEntitlementException(21, "Unable to recover from EAP-AKA synchroinization failure");
            }
            throw new ServiceEntitlementException(32, "Failed to parse EAP-AKA challenge: " + httpResponseChallengeResponse2.body());
        }
        throw new AssertionError("EapAkaResponse invalid.");
    }

    private HttpResponse challengeResponse(String str, CarrierConfig carrierConfig, List list, String str2, String str3, Map map) throws ServiceEntitlementException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("eap-relay-packet", str);
            return httpPost(jSONObject, carrierConfig, str2, str3, "application/vnd.gsma.eap-relay.v1.0+json", list, map);
        } catch (JSONException e) {
            throw new ServiceEntitlementException(32, "Failed to put post data", e);
        }
    }

    private void appendParametersForAuthentication(Uri.Builder builder, ServiceEntitlementRequest serviceEntitlementRequest, CarrierConfig carrierConfig) {
        if (!TextUtils.isEmpty(serviceEntitlementRequest.authenticationToken())) {
            builder.appendQueryParameter("IMSI", this.mTelephonyManager.getSubscriberId()).appendQueryParameter("token", serviceEntitlementRequest.authenticationToken());
        } else if (!TextUtils.isEmpty(serviceEntitlementRequest.temporaryToken())) {
            builder.appendQueryParameter("temporary_token", serviceEntitlementRequest.temporaryToken());
        } else {
            builder.appendQueryParameter("EAP_ID", getImsiEap(this.mTelephonyManager.getSimOperator(), this.mTelephonyManager.getSubscriberId(), carrierConfig.eapAkaRealm()));
        }
    }

    private void appendParametersForAuthentication(JSONObject jSONObject, ServiceEntitlementRequest serviceEntitlementRequest, CarrierConfig carrierConfig) throws ServiceEntitlementException {
        try {
            if (!TextUtils.isEmpty(serviceEntitlementRequest.authenticationToken())) {
                jSONObject.put("IMSI", this.mTelephonyManager.getSubscriberId());
                jSONObject.put("token", serviceEntitlementRequest.authenticationToken());
            } else if (!TextUtils.isEmpty(serviceEntitlementRequest.temporaryToken())) {
                jSONObject.put("temporary_token", serviceEntitlementRequest.temporaryToken());
            } else {
                jSONObject.put("EAP_ID", getImsiEap(this.mTelephonyManager.getSimOperator(), this.mTelephonyManager.getSubscriberId(), carrierConfig.eapAkaRealm()));
            }
        } catch (JSONException e) {
            throw new ServiceEntitlementException(1, "Failed to compose JSON", e);
        }
    }

    private void appendParametersForServiceEntitlementRequest(Uri.Builder builder, List list, ServiceEntitlementRequest serviceEntitlementRequest) {
        if (!TextUtils.isEmpty(serviceEntitlementRequest.notificationToken())) {
            builder.appendQueryParameter("notif_action", Integer.toString(serviceEntitlementRequest.notificationAction())).appendQueryParameter("notif_token", serviceEntitlementRequest.notificationToken());
        }
        if (TextUtils.isEmpty(serviceEntitlementRequest.terminalId())) {
            builder.appendQueryParameter("terminal_id", this.mTelephonyManager.getImei());
        } else {
            builder.appendQueryParameter("terminal_id", serviceEntitlementRequest.terminalId());
        }
        if (!TextUtils.isEmpty(serviceEntitlementRequest.gid1())) {
            builder.appendQueryParameter("GID1", serviceEntitlementRequest.gid1());
        } else if (new BigDecimal(serviceEntitlementRequest.entitlementVersion()).intValue() >= 12) {
            builder.appendQueryParameter("GID1", this.mTelephonyManager.getGroupIdLevel1());
        }
        appendOptionalQueryParameter(builder, "app_version", serviceEntitlementRequest.appVersion());
        appendOptionalQueryParameter(builder, "app_name", serviceEntitlementRequest.appName());
        appendOptionalQueryParameter(builder, "boost_type", serviceEntitlementRequest.boostType());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            builder.appendQueryParameter("app", (String) it.next());
        }
        builder.appendQueryParameter("terminal_vendor", trimString(serviceEntitlementRequest.terminalVendor(), 4)).appendQueryParameter("terminal_model", trimString(serviceEntitlementRequest.terminalModel(), 10)).appendQueryParameter("terminal_sw_version", trimString(serviceEntitlementRequest.terminalSoftwareVersion(), 20)).appendQueryParameter("vers", Integer.toString(serviceEntitlementRequest.configurationVersion())).appendQueryParameter("entitlement_version", serviceEntitlementRequest.entitlementVersion());
    }

    private void appendParametersForServiceEntitlementRequest(JSONObject jSONObject, List list, ServiceEntitlementRequest serviceEntitlementRequest) throws ServiceEntitlementException {
        try {
            if (!TextUtils.isEmpty(serviceEntitlementRequest.notificationToken())) {
                jSONObject.put("notif_action", Integer.toString(serviceEntitlementRequest.notificationAction()));
                jSONObject.put("notif_token", serviceEntitlementRequest.notificationToken());
            }
            if (TextUtils.isEmpty(serviceEntitlementRequest.terminalId())) {
                jSONObject.put("terminal_id", this.mTelephonyManager.getImei());
            } else {
                jSONObject.put("terminal_id", serviceEntitlementRequest.terminalId());
            }
            if (!TextUtils.isEmpty(serviceEntitlementRequest.gid1())) {
                jSONObject.put("GID1", serviceEntitlementRequest.gid1());
            } else if (new BigDecimal(serviceEntitlementRequest.entitlementVersion()).intValue() >= 12) {
                jSONObject.put("GID1", this.mTelephonyManager.getGroupIdLevel1());
            }
            appendOptionalQueryParameter(jSONObject, "app_version", serviceEntitlementRequest.appVersion());
            appendOptionalQueryParameter(jSONObject, "app_name", serviceEntitlementRequest.appName());
            appendOptionalQueryParameter(jSONObject, "boost_type", serviceEntitlementRequest.boostType());
            if (list.size() == 1) {
                appendOptionalQueryParameter(jSONObject, "app", (String) list.get(0));
            } else {
                appendOptionalQueryParameter(jSONObject, "app", "[" + TextUtils.join(",", list) + "]");
            }
            appendOptionalQueryParameter(jSONObject, "terminal_vendor", trimString(serviceEntitlementRequest.terminalVendor(), 4));
            appendOptionalQueryParameter(jSONObject, "terminal_model", trimString(serviceEntitlementRequest.terminalModel(), 10));
            appendOptionalQueryParameter(jSONObject, "terminal_sw_version", trimString(serviceEntitlementRequest.terminalSoftwareVersion(), 20));
            appendOptionalQueryParameter(jSONObject, "vers", Integer.toString(serviceEntitlementRequest.configurationVersion()));
            appendOptionalQueryParameter(jSONObject, "entitlement_version", serviceEntitlementRequest.entitlementVersion());
        } catch (JSONException e) {
            throw new ServiceEntitlementException(1, "Failed to compose JSON", e);
        }
    }

    private void appendOptionalQueryParameter(Uri.Builder builder, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        builder.appendQueryParameter(str, str2);
    }

    private void appendOptionalQueryParameter(JSONObject jSONObject, String str, String str2) throws JSONException {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        jSONObject.put(str, str2);
    }

    private HttpResponse httpGet(String str, CarrierConfig carrierConfig, String str2, String str3, Map map) {
        HttpRequest.Builder network = HttpRequest.builder().setUrl(str).setRequestMethod("GET").addRequestProperty("Accept", str2).addRequestProperty("User-Agent", str3).setTimeoutInSec(carrierConfig.timeoutInSec()).setNetwork(carrierConfig.network());
        carrierConfig.urlConnectionFactory();
        final HttpRequest.Builder urlConnectionFactory = network.setUrlConnectionFactory(null);
        map.forEach(new BiConsumer() { // from class: com.android.libraries.entitlement.eapaka.EapAkaApi$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                EapAkaApi.$r8$lambda$tzDLHBuEIut6ETYw9KaVwzlYfWc(urlConnectionFactory, (String) obj, (String) obj2);
            }
        });
        return this.mHttpClient.request(urlConnectionFactory.build());
    }

    public static /* synthetic */ void $r8$lambda$tzDLHBuEIut6ETYw9KaVwzlYfWc(HttpRequest.Builder builder, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        builder.addRequestProperty(str, str2);
    }

    private HttpResponse httpPost(JSONObject jSONObject, CarrierConfig carrierConfig, String str, String str2, Map map) {
        return httpPost(jSONObject, carrierConfig, str, str2, "application/json", Collections.EMPTY_LIST, map);
    }

    private HttpResponse httpPost(JSONObject jSONObject, CarrierConfig carrierConfig, String str, String str2, String str3, List list, Map map) {
        HttpRequest.Builder network = HttpRequest.builder().setUrl(carrierConfig.serverUrl()).setRequestMethod("POST").setPostData(jSONObject).addRequestProperty("Accept", str).addRequestProperty("Content-Type", str3).addRequestProperty("Cookie", list).addRequestProperty("User-Agent", str2).setTimeoutInSec(carrierConfig.timeoutInSec()).setNetwork(carrierConfig.network());
        carrierConfig.urlConnectionFactory();
        final HttpRequest.Builder urlConnectionFactory = network.setUrlConnectionFactory(null);
        map.forEach(new BiConsumer() { // from class: com.android.libraries.entitlement.eapaka.EapAkaApi$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                EapAkaApi.m55$r8$lambda$TgjOKYnApZ6JDQs3XWfPvqAuHE(urlConnectionFactory, (String) obj, (String) obj2);
            }
        });
        return this.mHttpClient.request(urlConnectionFactory.build());
    }

    /* JADX INFO: renamed from: $r8$lambda$TgjOKYnApZ6-JDQs3XWfPvqAuHE, reason: not valid java name */
    public static /* synthetic */ void m55$r8$lambda$TgjOKYnApZ6JDQs3XWfPvqAuHE(HttpRequest.Builder builder, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        builder.addRequestProperty(str, str2);
    }

    private String getEapAkaChallenge(HttpResponse httpResponse) throws ServiceEntitlementException {
        String strBody = httpResponse.body();
        if (httpResponse.contentType() == 0) {
            try {
                return new JSONObject(strBody).optString("eap-relay-packet", null);
            } catch (JSONException e) {
                throw new ServiceEntitlementException(32, "Failed to parse json object", e);
            }
        }
        if (httpResponse.contentType() == 1) {
            return null;
        }
        throw new ServiceEntitlementException(32, "Unknown HTTP content type");
    }

    private String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception unused) {
            return "";
        }
    }

    private String getUserAgent(String str, String str2, String str3, String str4) {
        return String.format("PRD-TS43 term-%s/%s %s/%s OS-Android/%s", trimString(str2, 4), trimString(str3, 10), str, this.mAppVersion, trimString(str4, 20));
    }

    private String trimString(String str, int i) {
        return str.substring(0, Math.min(str.length(), i));
    }

    public static String getImsiEap(String str, String str2, String str3) throws ServiceEntitlementException {
        if (str == null || str.length() < 5 || str2 == null) {
            throw new ServiceEntitlementException(11, String.format("Invalid imsi or mccmnc. imsi: %s, mccmnc: %s", str2, str));
        }
        String strSubstring = str.substring(0, 3);
        String strSubstring2 = str.substring(3);
        if (strSubstring2.length() == 2) {
            strSubstring2 = "0".concat(strSubstring2);
        }
        return "0" + str2 + "@" + str3 + ".mnc" + strSubstring2 + ".mcc" + strSubstring + ".3gppnetwork.org";
    }
}
