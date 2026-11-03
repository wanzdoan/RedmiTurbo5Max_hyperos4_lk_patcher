package com.android.phone.satellite.accesscontrol;

import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public abstract class SatelliteOnDeviceAccessController implements Closeable {

    public abstract class LocationToken {
    }

    public abstract Integer getRegionalConfigIdForLocation(LocationToken locationToken);

    public abstract int getS2Level();

    public abstract boolean isSatCommunicationAllowedAtLocation(LocationToken locationToken) throws IOException;

    public static SatelliteOnDeviceAccessController create(File file, FeatureFlags featureFlags) {
        return S2RangeSatelliteOnDeviceAccessController.create(file, featureFlags);
    }

    public static LocationToken createLocationTokenForLatLng(double d, double d2, int i) {
        return S2RangeSatelliteOnDeviceAccessController.createLocationTokenForLatLng(d, d2, i);
    }
}
