package com.hyper.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.LocaleTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class HyperCountryCodeResolver extends Handler {
    private static final int EVENT_LOCATION_CHANGED = 1;
    private static final int EVENT_LOCATION_COUNTRY_CODE_CHANGED = 2;
    private static final int EVENT_LOCATION_MODE_CHANGED = 5;
    private static final int EVENT_NETWORK_COUNTRY_CODE_CHANGED = 3;
    private static final int EVENT_REMOVE_SINGLE_LOCATION_UPDATE_LISTENER = 6;
    private static final int EVENT_WIFI_CONNECTIVITY_STATE_CHANGED = 4;
    private static final String LOG_TAG = "HyperCountryCodeResolver";
    private static final long TIME_BETWEEN_TIMEZONE_LOCATION_UPDATES_MILLIS = TimeUnit.MINUTES.toMillis(10);
    private static final long WAIT_FOR_REMOVE_SINGLE_LOCATION_UPDATE_LISTENER_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(15);
    private static int numPhones = TelephonyCommonProxyFactory.PHONE_COUNT;
    private static HyperCountryCodeResolver sInstance;
    private final ConnectivityManager mConnectivityManager;
    private final Geocoder mGeocoder;
    private boolean mIsTimeZoneSetByLocationWithNullNitz;
    private boolean mIsWifiNetworkConnected;
    private String mLocationCountryCode;
    private List<LocationCountryCodeListener> mLocationCountryCodeListeners;
    private final LocationManager mLocationManager;
    private BroadcastReceiver mLocationModeChangedBroadcastReceiver;
    private final Object mLock;
    private final LocationListener mSingleTimeLocationListener;

    public interface LocationCountryCodeListener {
        void onLocationCountryCodeChanged(String str);
    }

    private class TelephonyGeocodeListener implements Geocoder.GeocodeListener {
        private TelephonyGeocodeListener() {
        }

        @Override // android.location.Geocoder.GeocodeListener
        public void onGeocode(List<Address> addresses) {
            if (addresses != null && !addresses.isEmpty()) {
                String countryCode = addresses.get(0).getCountryCode();
                HyperCountryCodeResolver.logd("onGeocode: countryCode=" + countryCode);
                HyperCountryCodeResolver.this.sendRequestAsync(2, countryCode);
                return;
            }
            HyperCountryCodeResolver.logd("onGeocode: addresses is not available");
        }

        @Override // android.location.Geocoder.GeocodeListener
        public void onError(String errorMessage) {
            HyperCountryCodeResolver.loge("GeocodeListener.onError=" + errorMessage);
        }
    }

    public HyperCountryCodeResolver(Looper looper, Context context) {
        super(looper);
        this.mLock = new Object();
        this.mLocationCountryCode = null;
        this.mIsTimeZoneSetByLocationWithNullNitz = false;
        this.mIsWifiNetworkConnected = false;
        this.mLocationCountryCodeListeners = new ArrayList();
        this.mLocationModeChangedBroadcastReceiver = new BroadcastReceiver() { // from class: com.hyper.internal.telephony.HyperCountryCodeResolver.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.location.MODE_CHANGED")) {
                    HyperCountryCodeResolver.logd("LocationManager mode is changed");
                    HyperCountryCodeResolver.this.sendRequestAsync(5, null);
                }
            }
        };
        this.mSingleTimeLocationListener = new LocationListener() { // from class: com.hyper.internal.telephony.HyperCountryCodeResolver.2
            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                HyperCountryCodeResolver.logd("onLocationChanged: " + (location != null));
                HyperCountryCodeResolver.this.removeSingleLocationUpdateListener();
                if (location != null) {
                    HyperCountryCodeResolver.this.sendRequestAsync(1, location);
                }
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(String provider) {
                HyperCountryCodeResolver.logd("onProviderDisabled: provider=" + provider);
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String provider) {
                HyperCountryCodeResolver.logd("onProviderEnabled: provider=" + provider);
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String provider, int status, Bundle extras) {
                HyperCountryCodeResolver.logd("onStatusChanged: provider=" + provider + ", status=" + status + ", extras=" + extras);
            }
        };
        this.mLocationManager = (LocationManager) context.getSystemService(LocationManager.class);
        this.mGeocoder = new Geocoder(context);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        initialize(context);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                queryCountryCodeForLocation((Location) msg.obj);
                break;
            case 2:
                setLocationCountryCode((String) msg.obj);
                break;
            case 3:
                handleNetworkCountryCodeChangedEvent();
                break;
            case 4:
                handleEventWifiConnectivityStateChanged(((Boolean) msg.obj).booleanValue());
                break;
            case 5:
                handleLocationModeChangedEvent();
                break;
            case 6:
                logd("15 secs timeout for remove single location update listener");
                removeSingleLocationUpdateListener();
                break;
            default:
                logw("unexpected message code: " + msg.what);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRequestAsync(int command, Object argument) {
        Message msg = obtainMessage(command, argument);
        msg.sendToTarget();
    }

    public void onNetworkCountryCodeChanged() {
        sendRequestAsync(3, null);
    }

    public static HyperCountryCodeResolver getInstance(Context context) {
        HyperCountryCodeResolver hyperCountryCodeResolver;
        synchronized (HyperCountryCodeResolver.class) {
            if (sInstance == null) {
                HandlerThread handlerThread = new HandlerThread(LOG_TAG);
                handlerThread.start();
                sInstance = new HyperCountryCodeResolver(handlerThread.getLooper(), context);
            }
            hyperCountryCodeResolver = sInstance;
        }
        return hyperCountryCodeResolver;
    }

    private void initialize(Context context) {
        evaluateRequestingLocationUpdates();
        registerForWifiConnectivityStateChanged();
        registerLocationModeChangedBroadcastReceiver(context);
    }

    private void evaluateRequestingLocationUpdates() {
        logd("evaluateRequestingLocationUpdates");
        boolean isWifiNetworkConnected = isWifiNetworkConnected();
        boolean isCurrentNetworkCountryIsoEmpty = getCurrentNetworkCountryIso().isEmpty();
        if (isCurrentNetworkCountryIsoEmpty) {
            String provider = isWifiNetworkConnected ? "network" : "gps";
            registerForSingleLocationUpdates(provider);
        } else {
            unregisterForSingleLocationUpdates();
        }
    }

    private void registerForWifiConnectivityStateChanged() {
        logd("registerForWifiConnectivityStateChanged");
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(1).addCapability(12);
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.hyper.internal.telephony.HyperCountryCodeResolver.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                HyperCountryCodeResolver.logd("onCapabilitiesChanged: " + networkCapabilities);
                HyperCountryCodeResolver.this.sendRequestAsync(4, Boolean.valueOf(HyperCountryCodeResolver.this.isInternetAvailable(networkCapabilities)));
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                HyperCountryCodeResolver.logd("Wifi network lost: " + network);
                HyperCountryCodeResolver.this.sendRequestAsync(4, false);
            }
        };
        this.mConnectivityManager.registerNetworkCallback(builder.build(), networkCallback);
    }

    private void registerLocationModeChangedBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        context.registerReceiver(this.mLocationModeChangedBroadcastReceiver, intentFilter);
    }

    private void registerForSingleLocationUpdates(String provider) {
        if (!isGeoCoderImplemented()) {
            logd("Geocoder is not implemented on the device");
            return;
        }
        if (TextUtils.isEmpty(provider) || !this.mLocationManager.isProviderEnabled(provider)) {
            logd("Location provider " + provider + " is not enabled");
            return;
        }
        synchronized (this.mLock) {
            if (this.mIsTimeZoneSetByLocationWithNullNitz) {
                logd("Already set timezone by location with null nitz");
                return;
            }
            this.mIsTimeZoneSetByLocationWithNullNitz = true;
            logd("set TimeZoneByLocationStatus true");
            Location location = this.mLocationManager.getLastKnownLocation(provider);
            if (location != null && (SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos()) / 1000000 <= TIME_BETWEEN_TIMEZONE_LOCATION_UPDATES_MILLIS) {
                this.mSingleTimeLocationListener.onLocationChanged(location);
                logd("onLocationChanged with get last known location");
            } else {
                removeSingleLocationUpdateListener();
                this.mLocationManager.requestSingleUpdate(provider, this.mSingleTimeLocationListener, (Looper) null);
                logd("requestSingleUpdate with provider " + provider);
                removeMessages(6);
                sendMessageDelayed(obtainMessage(6), WAIT_FOR_REMOVE_SINGLE_LOCATION_UPDATE_LISTENER_TIMEOUT_MILLIS);
            }
        }
    }

    private void unregisterForSingleLocationUpdates() {
        synchronized (this.mLock) {
            this.mIsTimeZoneSetByLocationWithNullNitz = false;
            logd("set TimeZoneByLocationStatus false");
        }
        removeSingleLocationUpdateListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSingleLocationUpdateListener() {
        logd("removeSingleLocationUpdateListener");
        this.mLocationManager.removeUpdates(this.mSingleTimeLocationListener);
    }

    public List<String> getCurrentNetworkCountryIso() {
        List<String> result = new ArrayList<>();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                String countryIso = getNetworkCountryIsoForPhone(phone);
                if (isValid(countryIso)) {
                    String countryIsoInUpperCase = countryIso.toUpperCase(Locale.US);
                    if (!result.contains(countryIsoInUpperCase)) {
                        result.add(countryIsoInUpperCase);
                    }
                } else {
                    logd("getCurrentNetworkCountryIso: invalid countryIso=" + countryIso + " for phoneId=" + phone.getPhoneId() + ", subId=" + phone.getSubId());
                }
            }
        } catch (IllegalStateException e) {
            logd("Phones not initialized yet: " + e.getMessage());
        }
        return result;
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

    private static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < numPhones;
    }

    public boolean isWifiNetworkConnected() {
        logd("isWifiNetworkConnected: " + this.mIsWifiNetworkConnected);
        return this.mIsWifiNetworkConnected;
    }

    private void handleNetworkCountryCodeChangedEvent() {
        logd("handleNetworkCountryCodeChangedEvent");
        evaluateRequestingLocationUpdates();
    }

    private void handleEventWifiConnectivityStateChanged(boolean connected) {
        logd("handleEventWifiConnectivityStateChanged: " + connected);
        if (connected != this.mIsWifiNetworkConnected) {
            this.mIsWifiNetworkConnected = connected;
            sendRequestAsync(2, "");
            unregisterForSingleLocationUpdates();
            evaluateRequestingLocationUpdates();
        }
    }

    private void handleLocationModeChangedEvent() {
        logd("handleLocationModeChangedEvent");
        if (!this.mLocationManager.isLocationEnabled()) {
            sendRequestAsync(2, "");
        }
        unregisterForSingleLocationUpdates();
        evaluateRequestingLocationUpdates();
    }

    protected void queryCountryCodeForLocation(Location location) {
        this.mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1, new TelephonyGeocodeListener());
    }

    private void setLocationCountryCode(String countryCode) {
        logd("Set location country code to: " + countryCode);
        synchronized (this.mLock) {
            this.mLocationCountryCode = countryCode != null ? countryCode.toUpperCase(Locale.US) : "";
            notifyLocationCountryCodeListeners(this.mLocationCountryCode);
        }
    }

    private boolean isGeoCoderImplemented() {
        return Geocoder.isPresent();
    }

    private static boolean isValid(String countryCode) {
        return countryCode != null && countryCode.length() == 2 && countryCode.chars().allMatch(new IntPredicate() { // from class: com.hyper.internal.telephony.HyperCountryCodeResolver$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return Character.isLetterOrDigit(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInternetAvailable(NetworkCapabilities networkCapabilities) {
        boolean isWifiConnected = networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
        logd("isWifiConnected: " + isWifiConnected);
        return isWifiConnected;
    }

    public void addLocationCountryCodeListener(LocationCountryCodeListener listener) {
        if (!this.mLocationCountryCodeListeners.contains(listener)) {
            this.mLocationCountryCodeListeners.add(listener);
        }
    }

    public void removeLocationCountryCodeListener(LocationCountryCodeListener listener) {
        this.mLocationCountryCodeListeners.remove(listener);
    }

    protected void clearLocationCountryCodeListeners() {
        if (this.mLocationCountryCodeListeners != null) {
            this.mLocationCountryCodeListeners.clear();
        }
    }

    private void notifyLocationCountryCodeListeners(String countryCode) {
        logd("notifyLocationCountryCodeListeners");
        for (LocationCountryCodeListener listener : new ArrayList(this.mLocationCountryCodeListeners)) {
            listener.onLocationCountryCodeChanged(countryCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String log) {
        Rlog.d(LOG_TAG, log);
    }

    private static void logw(String log) {
        Rlog.w(LOG_TAG, log);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String log) {
        Rlog.e(LOG_TAG, log);
    }
}
