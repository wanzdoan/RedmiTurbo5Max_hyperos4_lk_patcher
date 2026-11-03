package com.android.internal.telephony;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.util.WorkerThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCountryDetector extends Handler {
    private static final boolean DEBUG = !"user".equals(Build.TYPE);
    private static final long TIME_BETWEEN_LOCATION_UPDATES_MILLIS = 43200000;
    protected static final long WAIT_FOR_LOCATION_UPDATE_REQUEST_QUOTA_RESET_TIMEOUT_MILLIS = 1800000;
    private static TelephonyCountryDetector sInstance;
    private final ConnectivityManager mConnectivityManager;
    private final RegistrantList mCountryCodeChangedRegistrants;
    private FeatureFlags mFeatureFlags;
    private final Geocoder mGeocoder;
    private boolean mIsCountryCodesOverridden;
    private boolean mIsLocationUpdateRequested;
    private boolean mIsWifiNetworkConnected;
    private String mLocationCountryCode;
    private long mLocationCountryCodeUpdatedTimestampNanos;
    private final LocationListener mLocationListener;
    private final LocationManager mLocationManager;
    private final Object mLock;
    private final Map mNetworkCountryCodeInfoPerPhone;
    private Map mOverriddenCachedNetworkCountryCodes;
    private List mOverriddenCurrentNetworkCountryCodes;
    private String mOverriddenLocationCountryCode;
    private long mOverriddenLocationCountryCodeUpdatedTimestampNanos;
    private final RegistrantList mWifiConnectivityStateChangedRegistrantList;

    private class TelephonyGeocodeListener implements Geocoder.GeocodeListener {
        private long mLocationUpdatedTime;

        TelephonyGeocodeListener(long j) {
            this.mLocationUpdatedTime = j;
        }

        @Override // android.location.Geocoder.GeocodeListener
        public void onGeocode(List<Address> list) {
            if (list != null && !list.isEmpty()) {
                TelephonyCountryDetector.logd("onGeocode: addresses is available");
                TelephonyCountryDetector.this.sendRequestAsync(2, new Pair(list.get(0).getCountryCode(), Long.valueOf(this.mLocationUpdatedTime)));
                return;
            }
            TelephonyCountryDetector.logd("onGeocode: addresses is not available");
        }

        @Override // android.location.Geocoder.GeocodeListener
        public void onError(String str) {
            TelephonyCountryDetector.loge("GeocodeListener.onError=" + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class NetworkCountryCodeInfo {
        public String countryCode;
        public int phoneId;
        public long timestamp;

        private NetworkCountryCodeInfo() {
        }

        public String toString() {
            return "NetworkCountryCodeInfo[phoneId: " + this.phoneId + ", countryCode: " + this.countryCode + ", timestamp: " + this.timestamp + "]";
        }
    }

    protected TelephonyCountryDetector(Looper looper, Context context, LocationManager locationManager, ConnectivityManager connectivityManager, FeatureFlags featureFlags) {
        super(looper);
        this.mWifiConnectivityStateChangedRegistrantList = new RegistrantList();
        this.mLock = new Object();
        this.mNetworkCountryCodeInfoPerPhone = new HashMap();
        this.mLocationCountryCode = null;
        this.mOverriddenLocationCountryCode = null;
        this.mIsLocationUpdateRequested = false;
        this.mLocationCountryCodeUpdatedTimestampNanos = 0L;
        this.mOverriddenLocationCountryCodeUpdatedTimestampNanos = 0L;
        this.mOverriddenCurrentNetworkCountryCodes = null;
        this.mOverriddenCachedNetworkCountryCodes = new HashMap();
        this.mIsCountryCodesOverridden = false;
        this.mCountryCodeChangedRegistrants = new RegistrantList();
        this.mIsWifiNetworkConnected = false;
        this.mFeatureFlags = null;
        this.mLocationListener = new LocationListener() { // from class: com.android.internal.telephony.TelephonyCountryDetector.1
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                StringBuilder sb = new StringBuilder("onLocationChanged: ");
                sb.append(location != null);
                TelephonyCountryDetector.logd(sb.toString());
                if (location != null) {
                    TelephonyCountryDetector.this.sendRequestAsync(1, location);
                }
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
                TelephonyCountryDetector.logd("onProviderDisabled: provider=" + str);
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
                TelephonyCountryDetector.logd("onProviderEnabled: provider=" + str);
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
                TelephonyCountryDetector.logd("onStatusChanged: provider=" + str + ", status=" + i + ", extras=" + bundle);
            }
        };
        this.mLocationManager = locationManager;
        this.mGeocoder = new Geocoder(context);
        this.mConnectivityManager = connectivityManager;
        this.mFeatureFlags = featureFlags;
        initialize();
    }

    public static synchronized TelephonyCountryDetector getInstance(Context context, FeatureFlags featureFlags) {
        try {
            if (sInstance == null) {
                sInstance = new TelephonyCountryDetector(WorkerThread.get().getLooper(), context, (LocationManager) context.getSystemService(LocationManager.class), (ConnectivityManager) context.getSystemService(ConnectivityManager.class), featureFlags);
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public List<String> getCurrentNetworkCountryIso() {
        synchronized (this.mLock) {
            try {
                if (this.mIsCountryCodesOverridden) {
                    logd("mOverriddenCurrentNetworkCountryCodes=" + String.join(", ", this.mOverriddenCurrentNetworkCountryCodes));
                    return this.mOverriddenCurrentNetworkCountryCodes;
                }
                ArrayList arrayList = new ArrayList();
                try {
                    for (Phone phone : PhoneFactory.getPhones()) {
                        String networkCountryIsoForPhone = getNetworkCountryIsoForPhone(phone);
                        if (isValid(networkCountryIsoForPhone)) {
                            String upperCase = networkCountryIsoForPhone.toUpperCase(Locale.US);
                            if (!arrayList.contains(upperCase)) {
                                arrayList.add(upperCase);
                            }
                        } else {
                            logd("getCurrentNetworkCountryIso: invalid countryIso=" + networkCountryIsoForPhone + " for phoneId=" + phone.getPhoneId() + ", subId=" + phone.getSubId());
                        }
                    }
                    return arrayList;
                } catch (IllegalStateException e) {
                    logd("getCurrentNetworkCountryIso: PhoneFactory.getPhones() failed, e=" + e);
                    return arrayList;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public Pair<String, Long> getCachedLocationCountryIsoInfo() {
        synchronized (this.mLock) {
            try {
                if (this.mIsCountryCodesOverridden) {
                    logd("mOverriddenLocationCountryCode=" + this.mOverriddenLocationCountryCode + " will be used");
                    return new Pair<>(this.mOverriddenLocationCountryCode, Long.valueOf(this.mOverriddenLocationCountryCodeUpdatedTimestampNanos));
                }
                return new Pair<>(this.mLocationCountryCode, Long.valueOf(this.mLocationCountryCodeUpdatedTimestampNanos));
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public Map<String, Long> getCachedNetworkCountryIsoInfo() {
        synchronized (this.mLock) {
            try {
                if (this.mIsCountryCodesOverridden) {
                    logd("mOverriddenCachedNetworkCountryCodes = " + String.join(", ", this.mOverriddenCachedNetworkCountryCodes.keySet()) + " will be used");
                    return this.mOverriddenCachedNetworkCountryCodes;
                }
                HashMap map = new HashMap();
                for (NetworkCountryCodeInfo networkCountryCodeInfo : this.mNetworkCountryCodeInfoPerPhone.values()) {
                    boolean zContainsKey = map.containsKey(networkCountryCodeInfo.countryCode);
                    if (!zContainsKey || (zContainsKey && ((Long) map.get(networkCountryCodeInfo.countryCode)).longValue() < networkCountryCodeInfo.timestamp)) {
                        map.put(networkCountryCodeInfo.countryCode, Long.valueOf(networkCountryCodeInfo.timestamp));
                    }
                }
                return map;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            queryCountryCodeForLocation((Location) message.obj);
            return;
        }
        if (i == 2) {
            setLocationCountryCode((Pair) message.obj);
            return;
        }
        if (i == 3) {
            handleNetworkCountryCodeChangedEvent((NetworkCountryCodeInfo) message.obj);
            return;
        }
        if (i == 4) {
            handleEventWifiConnectivityStateChanged(((Boolean) message.obj).booleanValue());
        } else {
            if (i == 5) {
                evaluateRequestingLocationUpdates();
                return;
            }
            logw("CountryDetectorHandler: unexpected message code: " + message.what);
        }
    }

    public void onNetworkCountryCodeChanged(Phone phone, String str) {
        NetworkCountryCodeInfo networkCountryCodeInfo = new NetworkCountryCodeInfo();
        networkCountryCodeInfo.phoneId = phone.getPhoneId();
        networkCountryCodeInfo.countryCode = str;
        sendRequestAsync(3, networkCountryCodeInfo);
    }

    public boolean setCountryCodes(boolean z, List<String> list, Map<String, Long> map, String str, long j) {
        if (!isMockModemAllowed()) {
            logd("setCountryCodes: mock modem is not allowed");
            return false;
        }
        logd("setCountryCodes: currentNetworkCountryCodes=" + String.join(", ", list) + ", locationCountryCode=" + str + ", locationCountryCodeTimestampNanos" + j + ", reset=" + z + ", cachedNetworkCountryCodes=" + String.join(", ", map.keySet()));
        synchronized (this.mLock) {
            try {
                if (z) {
                    this.mIsCountryCodesOverridden = false;
                } else {
                    this.mIsCountryCodesOverridden = true;
                    this.mOverriddenCachedNetworkCountryCodes = map;
                    this.mOverriddenCurrentNetworkCountryCodes = list;
                    this.mOverriddenLocationCountryCode = str;
                    this.mOverriddenLocationCountryCodeUpdatedTimestampNanos = j;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    protected void queryCountryCodeForLocation(Location location) {
        this.mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, new TelephonyGeocodeListener(location.getElapsedRealtimeNanos()));
    }

    protected long getElapsedRealtimeNanos() {
        return SystemClock.elapsedRealtimeNanos();
    }

    private void initialize() {
        evaluateRequestingLocationUpdates();
        registerForWifiConnectivityStateChanged();
    }

    private boolean isGeoCoderImplemented() {
        return Geocoder.isPresent();
    }

    private void registerForLocationUpdates() {
        if (!isGeoCoderImplemented()) {
            logd("Geocoder is not implemented on the device");
            return;
        }
        synchronized (this.mLock) {
            try {
                if (this.mIsLocationUpdateRequested) {
                    logd("Already registered for location updates");
                    return;
                }
                logd("Registering for location updates");
                this.mLocationManager.requestLocationUpdates("passive", TIME_BETWEEN_LOCATION_UPDATES_MILLIS, 2000.0f, this.mLocationListener);
                this.mIsLocationUpdateRequested = true;
                this.mLocationListener.onLocationChanged(getLastKnownLocation());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private Location getLastKnownLocation() {
        Iterator<String> it = this.mLocationManager.getProviders(true).iterator();
        Location location = null;
        while (it.hasNext()) {
            Location lastKnownLocation = this.mLocationManager.getLastKnownLocation(it.next());
            if (lastKnownLocation != null && (location == null || location.getElapsedRealtimeNanos() < lastKnownLocation.getElapsedRealtimeNanos())) {
                location = lastKnownLocation;
            }
        }
        return location;
    }

    private void unregisterForLocationUpdates() {
        synchronized (this.mLock) {
            try {
                if (!this.mIsLocationUpdateRequested) {
                    logd("Location update was not requested yet");
                } else {
                    if (isLocationUpdateRequestQuotaExceeded()) {
                        logd("Removing location updates will be re-evaluated after the quota is refilled");
                        return;
                    }
                    this.mLocationManager.removeUpdates(this.mLocationListener);
                    this.mIsLocationUpdateRequested = false;
                    sendMessageDelayed(obtainMessage(5), WAIT_FOR_LOCATION_UPDATE_REQUEST_QUOTA_RESET_TIMEOUT_MILLIS);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean isLocationUpdateRequestQuotaExceeded() {
        return hasMessages(5);
    }

    private boolean shouldRequestLocationUpdate() {
        return getCurrentNetworkCountryIso().isEmpty() && isWifiNetworkConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRequestAsync(int i, Object obj) {
        obtainMessage(i, obj).sendToTarget();
    }

    private void handleNetworkCountryCodeChangedEvent(NetworkCountryCodeInfo networkCountryCodeInfo) {
        logd("currentNetworkCountryCodeInfo=" + networkCountryCodeInfo);
        if (isValid(networkCountryCodeInfo.countryCode)) {
            synchronized (this.mLock) {
                NetworkCountryCodeInfo networkCountryCodeInfo2 = (NetworkCountryCodeInfo) this.mNetworkCountryCodeInfoPerPhone.computeIfAbsent(Integer.valueOf(networkCountryCodeInfo.phoneId), new Function() { // from class: com.android.internal.telephony.TelephonyCountryDetector$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return TelephonyCountryDetector.m586$r8$lambda$1eSkIn88znkWWqDrRnxf_qwrNg((Integer) obj);
                    }
                });
                networkCountryCodeInfo2.phoneId = networkCountryCodeInfo.phoneId;
                networkCountryCodeInfo2.timestamp = getElapsedRealtimeNanos();
                networkCountryCodeInfo2.countryCode = networkCountryCodeInfo.countryCode.toUpperCase(Locale.US);
            }
        } else {
            logd("handleNetworkCountryCodeChangedEvent: Got invalid or empty country code for phoneId=" + networkCountryCodeInfo.phoneId);
            synchronized (this.mLock) {
                try {
                    if (this.mNetworkCountryCodeInfoPerPhone.containsKey(Integer.valueOf(networkCountryCodeInfo.phoneId))) {
                        ((NetworkCountryCodeInfo) this.mNetworkCountryCodeInfoPerPhone.get(Integer.valueOf(networkCountryCodeInfo.phoneId))).timestamp = getElapsedRealtimeNanos();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        evaluateRequestingLocationUpdates();
        logd("mCountryCodeChangedRegistrants.notifyRegistrants()");
        this.mCountryCodeChangedRegistrants.notifyRegistrants();
    }

    /* JADX INFO: renamed from: $r8$lambda$1eSkIn88zn-kWWqDrRnxf_qwrNg, reason: not valid java name */
    public static /* synthetic */ NetworkCountryCodeInfo m586$r8$lambda$1eSkIn88znkWWqDrRnxf_qwrNg(Integer num) {
        return new NetworkCountryCodeInfo();
    }

    private void handleEventWifiConnectivityStateChanged(boolean z) {
        logd("handleEventWifiConnectivityStateChanged: " + z);
        evaluateNotifyWifiConnectivityStateChangedEvent(z);
        evaluateRequestingLocationUpdates();
    }

    private void evaluateNotifyWifiConnectivityStateChangedEvent(boolean z) {
        if (z != this.mIsWifiNetworkConnected) {
            this.mIsWifiNetworkConnected = z;
            this.mWifiConnectivityStateChangedRegistrantList.notifyResult(Boolean.valueOf(z));
            logd("evaluateNotifyWifiConnectivityStateChangedEvent: wifi connectivity state has changed to " + z);
        }
    }

    private void setLocationCountryCode(Pair pair) {
        logd("Set location country code to: " + ((String) pair.first));
        if (!isValid((String) pair.first)) {
            logd("Received invalid location country code");
            return;
        }
        synchronized (this.mLock) {
            this.mLocationCountryCode = ((String) pair.first).toUpperCase(Locale.US);
            this.mLocationCountryCodeUpdatedTimestampNanos = ((Long) pair.second).longValue();
        }
    }

    private String getNetworkCountryIsoForPhone(Phone phone) {
        ServiceStateTracker serviceStateTracker = phone.getServiceStateTracker();
        if (serviceStateTracker == null) {
            logw("getNetworkCountryIsoForPhone: serviceStateTracker is null");
            return null;
        }
        LocaleTracker localeTracker = serviceStateTracker.getLocaleTracker();
        if (localeTracker == null) {
            logw("getNetworkCountryIsoForPhone: localeTracker is null");
            return null;
        }
        return localeTracker.getCurrentCountry();
    }

    private void registerForWifiConnectivityStateChanged() {
        logd("registerForWifiConnectivityStateChanged");
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(1).addCapability(12);
        this.mConnectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() { // from class: com.android.internal.telephony.TelephonyCountryDetector.2
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                TelephonyCountryDetector.logd("onCapabilitiesChanged: " + networkCapabilities);
                TelephonyCountryDetector telephonyCountryDetector = TelephonyCountryDetector.this;
                telephonyCountryDetector.sendRequestAsync(4, Boolean.valueOf(telephonyCountryDetector.isInternetAvailable(networkCapabilities)));
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                TelephonyCountryDetector.logd("Wifi network lost: " + network);
                TelephonyCountryDetector.this.sendRequestAsync(4, Boolean.FALSE);
            }
        });
    }

    private void evaluateRequestingLocationUpdates() {
        if (shouldRequestLocationUpdate()) {
            registerForLocationUpdates();
        } else {
            unregisterForLocationUpdates();
        }
    }

    public boolean isWifiNetworkConnected() {
        logd("isWifiNetworkConnected: " + this.mIsWifiNetworkConnected);
        return this.mIsWifiNetworkConnected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInternetAvailable(NetworkCapabilities networkCapabilities) {
        boolean z = networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
        logd("isWifiConnected: " + z);
        return z;
    }

    public void registerForWifiConnectivityStateChanged(Handler handler, int i, Object obj) {
        this.mWifiConnectivityStateChangedRegistrantList.add(handler, i, obj);
    }

    public void unregisterForWifiConnectivityStateChanged(Handler handler) {
        this.mWifiConnectivityStateChangedRegistrantList.remove(handler);
    }

    private static boolean isValid(String str) {
        return str != null && str.length() == 2 && str.chars().allMatch(new IntPredicate() { // from class: com.android.internal.telephony.TelephonyCountryDetector$$ExternalSyntheticLambda1
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return Character.isLetterOrDigit(i);
            }
        });
    }

    private static boolean isMockModemAllowed() {
        return DEBUG || SystemProperties.getBoolean("persist.radio.allow_mock_modem", true) || SystemProperties.getBoolean("ro.boot.radio.allow_mock_modem", false);
    }

    public void registerForCountryCodeChanged(Handler handler, int i, Object obj) {
        this.mCountryCodeChangedRegistrants.add(handler, i, obj);
    }

    public void unregisterForCountryCodeChanged(Handler handler) {
        this.mCountryCodeChangedRegistrants.remove(handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Rlog.d("TelephonyCountryDetector", str);
    }

    private static void logw(String str) {
        Rlog.w("TelephonyCountryDetector", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Rlog.e("TelephonyCountryDetector", str);
    }
}
