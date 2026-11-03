package com.android.phone.ntn;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeRecognition$$ExternalSyntheticLambda0;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.satellite.SatelliteController;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class NtnLocationManager {
    private static String TAG = "NtnLocationManager";
    private static double mAltitude;
    private static float mSpeed;
    private static NtnLocationManager sInstance;
    private Address mAddress;
    private String mAdminArea;
    private String mCityCode;
    private Context mContext;
    private Location mLastNetworkLocation;
    private String mLocality;
    private LocationManager mLocationManager;
    private final RemoteCallbackList mCallbacks = new RemoteCallbackList();
    private final LocationListener mLocationListener = new LocationListener() { // from class: com.android.phone.ntn.NtnLocationManager.2
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.i(NtnLocationManager.TAG, "No valid location need to be update! ");
                return;
            }
            boolean zEquals = "network".equals(location.getProvider());
            NtnLocationManager ntnLocationManager = NtnLocationManager.this;
            if (zEquals) {
                ntnLocationManager.mLastNetworkLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                NtnLocationManager.this.updateAddressInfo(latitude, longitude);
                Log.d(NtnLocationManager.TAG, "onLocationChanged-latitude: " + latitude + " ,longitude: " + longitude);
            } else if (ntnLocationManager.mLastNetworkLocation != null && NtnLocationManager.this.mCityCode != null && "gps".equals(location.getProvider())) {
                long jDistanceTo = (long) location.distanceTo(NtnLocationManager.this.mLastNetworkLocation);
                if (jDistanceTo / 1000 >= 50) {
                    NtnLocationManager.this.mCityCode = null;
                }
                Log.d(NtnLocationManager.TAG, "onLocationChanged-distance: " + jDistanceTo);
            }
            NtnLocationManager.mAltitude = location.getAltitude();
            NtnLocationManager.mSpeed = location.getSpeed();
        }
    };
    private LocationListener mLocationUpdateListener = new AnonymousClass3();

    public static NtnLocationManager getInstance() {
        if (sInstance == null) {
            sInstance = new NtnLocationManager();
        }
        return sInstance;
    }

    private NtnLocationManager() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mLocationManager = (LocationManager) phoneGlobals.getSystemService("location");
    }

    public void updateCurrentLocation(String str) {
        if (!TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() || TelephonyManagerEx.getDefault().isSatelliteBox()) {
            Log.d(TAG, "Not Support for non satellite device or box! ");
            return;
        }
        if (this.mLocationManager.isProviderEnabled("gps") || this.mLocationManager.isProviderEnabled("network")) {
            if (this.mContext.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0 && this.mContext.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                Log.i(TAG, "No permission to get location info! ");
                return;
            }
            LocationRequest locationRequestBuild = new LocationRequest.Builder(1000L).setMinUpdateIntervalMillis(1000L).build();
            LocationManager locationManager = this.mLocationManager;
            Executor mainExecutor = this.mContext.getMainExecutor();
            LocationListener locationListener = this.mLocationListener;
            Objects.requireNonNull(locationListener);
            locationManager.getCurrentLocation(str, locationRequestBuild, null, mainExecutor, new NetworkModeRecognition$$ExternalSyntheticLambda0(locationListener));
            Log.d(TAG, "Update current location by provider: " + str);
            return;
        }
        Log.e(TAG, "GPS provider not available");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAddressInfo(final double d, final double d2) {
        new Thread() { // from class: com.android.phone.ntn.NtnLocationManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    List<Address> fromLocation = new Geocoder(NtnLocationManager.this.mContext, Locale.getDefault()).getFromLocation(d, d2, 1);
                    if (fromLocation != null && fromLocation.size() > 0) {
                        NtnLocationManager.this.mAddress = fromLocation.get(0);
                        NtnLocationManager ntnLocationManager = NtnLocationManager.this;
                        String adminArea = null;
                        ntnLocationManager.mLocality = ntnLocationManager.mAddress == null ? null : NtnLocationManager.this.mAddress.getLocality();
                        NtnLocationManager ntnLocationManager2 = NtnLocationManager.this;
                        ntnLocationManager2.mCityCode = (ntnLocationManager2.mAddress == null || NtnLocationManager.this.mAddress.getExtras() == null) ? null : NtnLocationManager.this.mAddress.getExtras().getString("citycode");
                        NtnLocationManager ntnLocationManager3 = NtnLocationManager.this;
                        if (ntnLocationManager3.mAddress != null) {
                            adminArea = NtnLocationManager.this.mAddress.getAdminArea();
                        }
                        ntnLocationManager3.mAdminArea = adminArea;
                        NtnLocationManager.this.mLocality = NtnLocationManager.this.mAdminArea + "-" + NtnLocationManager.this.mLocality;
                        Log.d(NtnLocationManager.TAG, "Locality: " + NtnLocationManager.this.mLocality + ", City Code: " + NtnLocationManager.this.mCityCode + ", Province: " + NtnLocationManager.this.mAdminArea);
                        return;
                    }
                    Log.e(NtnLocationManager.TAG, "Unable to retrieve address");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void updateAddressInfo(Location location, Address address) {
        if (!TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            Log.d(TAG, "No need to update when it is not supported!");
            return;
        }
        this.mLastNetworkLocation = location;
        this.mCityCode = (address == null || address.getExtras() == null) ? null : address.getExtras().getString("citycode");
        Log.d(TAG, "Update city code: " + this.mCityCode + ", location: " + this.mLastNetworkLocation);
    }

    public void startLocationUpdate(IRemoteCallback iRemoteCallback, int i, int i2) {
        Log.d(TAG, "startLocationUpdate: callback = " + iRemoteCallback);
        this.mCallbacks.register(iRemoteCallback);
        sendLastKnownLocation(iRemoteCallback);
        if (this.mLocationManager == null || callbackSize() != 1) {
            return;
        }
        this.mLocationManager.requestLocationUpdates("gps", i, i2, this.mLocationUpdateListener, Looper.getMainLooper());
    }

    /* JADX INFO: renamed from: com.android.phone.ntn.NtnLocationManager$3, reason: invalid class name */
    class AnonymousClass3 implements LocationListener {
        AnonymousClass3() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(final Location location) {
            if (location == null) {
                return;
            }
            Log.d(NtnLocationManager.TAG, "onLocationChanged.");
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.ntn.NtnLocationManager$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onLocationChanged$1(location);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocationChanged$1(Location location) {
            float fCalculateElevation = (float) NtnLocationManager.this.calculateElevation(location.getLongitude(), location.getLatitude(), location.getAltitude());
            final Bundle locationData = NtnLocationManager.this.getLocationData(location);
            SatelliteController.getInstance().notifyStartBeamSwitching(fCalculateElevation, locationData);
            if (NtnLocationManager.this.callbackSize() == 0) {
                Log.w(NtnLocationManager.TAG, "No callbacks registered, skip notification.");
            } else {
                NtnLocationManager.this.mCallbacks.broadcast(new Consumer() { // from class: com.android.phone.ntn.NtnLocationManager$3$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        NtnLocationManager.AnonymousClass3.$r8$lambda$L0R77w5F5ycMfVQv3f0t8gXh9uc(locationData, (IRemoteCallback) obj);
                    }
                });
            }
        }

        public static /* synthetic */ void $r8$lambda$L0R77w5F5ycMfVQv3f0t8gXh9uc(Bundle bundle, IRemoteCallback iRemoteCallback) {
            try {
                iRemoteCallback.sendResult(bundle);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegistLocationUpdates(IRemoteCallback iRemoteCallback) {
        Log.d(TAG, "unRegistLocation.");
        if (iRemoteCallback != null) {
            this.mCallbacks.unregister(iRemoteCallback);
        }
        if (callbackSize() == 0) {
            Log.w(TAG, "No callbacks registered, remove locationListener.");
            this.mLocationManager.removeUpdates(this.mLocationUpdateListener);
        }
    }

    private void sendLastKnownLocation(IRemoteCallback iRemoteCallback) {
        Location lastKnownLocation = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getLastKnownLocation();
        if (lastKnownLocation != null) {
            Bundle locationData = getLocationData(lastKnownLocation);
            locationData.putBoolean("isLastKnownLocation", true);
            try {
                iRemoteCallback.sendResult(locationData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle getLocationData(Location location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("location", location);
        bundle.putDouble("latitude", location.getLatitude());
        bundle.putDouble("longitude", location.getLongitude());
        bundle.putDouble("altitude", location.getAltitude());
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int callbackSize() {
        int iBeginBroadcast = this.mCallbacks.beginBroadcast();
        this.mCallbacks.finishBroadcast();
        return iBeginBroadcast;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double calculateElevation(double d, double d2, double d3) {
        double dPow = (Math.pow(6378137.0d, 2.0d) - Math.pow(6356752.298215969d, 2.0d)) / Math.pow(6378137.0d, 2.0d);
        double radians = Math.toRadians(101.47d);
        double radians2 = Math.toRadians(1.03d);
        double radians3 = Math.toRadians(d);
        double radians4 = Math.toRadians(d2);
        double dSqrt = 6378137.0d / Math.sqrt(1.0d - (Math.pow(Math.sin(radians2), 2.0d) * dPow));
        double d4 = dSqrt + 3.57729E7d;
        double dCos = Math.cos(radians2) * d4 * Math.cos(radians);
        double dCos2 = d4 * Math.cos(radians2) * Math.sin(radians);
        double d5 = 1.0d - dPow;
        double dSin = ((dSqrt * d5) + 3.57729E7d) * Math.sin(radians2);
        double dSqrt2 = 6378137.0d / Math.sqrt(1.0d - (Math.pow(Math.sin(radians4), 2.0d) * dPow));
        double d6 = dSqrt2 + d3;
        double dCos3 = Math.cos(radians4) * d6 * Math.cos(radians3);
        double dCos4 = d6 * Math.cos(radians4) * Math.sin(radians3);
        double dSin2 = ((d5 * dSqrt2) + d3) * Math.sin(radians4);
        double d7 = dCos - dCos3;
        double d8 = dCos2 - dCos4;
        double d9 = dSin - dSin2;
        return ((Math.acos(((((d7 * dCos3) + (d8 * dCos4)) + (d9 * dSin2)) / Math.sqrt(((d7 * d7) + (d8 * d8)) + (d9 * d9))) / Math.sqrt(((dCos3 * dCos3) + (dCos4 * dCos4)) + (dSin2 * dSin2))) / 3.141592653589793d) * 180.0d) - 90.0d;
    }

    public String getProvince() {
        return this.mAdminArea;
    }

    public String getCity() {
        return this.mLocality;
    }

    public String getAreaCode() {
        return this.mCityCode;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public float getSpeed() {
        float f = mSpeed;
        if (f > 0.0f) {
            return f;
        }
        return 0.0f;
    }
}
