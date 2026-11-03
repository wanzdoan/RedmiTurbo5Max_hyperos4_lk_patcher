package com.android.phone.satellite.accesscontrol;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.telephony.sats2range.read.SatS2RangeFileReader;
import com.android.telephony.sats2range.read.SuffixTableRange;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
final class S2RangeSatelliteOnDeviceAccessController extends SatelliteOnDeviceAccessController {
    private final FeatureFlags mFeatureFlags;
    private final int mS2Level;
    private final SatS2RangeFileReader mSatS2RangeFileReader;

    private S2RangeSatelliteOnDeviceAccessController(SatS2RangeFileReader satS2RangeFileReader, int i, FeatureFlags featureFlags) {
        Objects.requireNonNull(satS2RangeFileReader);
        this.mSatS2RangeFileReader = satS2RangeFileReader;
        this.mS2Level = i;
        this.mFeatureFlags = featureFlags;
    }

    public static S2RangeSatelliteOnDeviceAccessController create(File file, FeatureFlags featureFlags) {
        SatS2RangeFileReader satS2RangeFileReaderOpen = SatS2RangeFileReader.open(file);
        return new S2RangeSatelliteOnDeviceAccessController(satS2RangeFileReaderOpen, satS2RangeFileReaderOpen.getS2Level(), featureFlags);
    }

    public static SatelliteOnDeviceAccessController.LocationToken createLocationTokenForLatLng(double d, double d2, int i) {
        return new LocationTokenImpl(getS2CellId(d, d2, i).id());
    }

    @Override // com.android.phone.satellite.accesscontrol.SatelliteOnDeviceAccessController
    public boolean isSatCommunicationAllowedAtLocation(SatelliteOnDeviceAccessController.LocationToken locationToken) {
        if (!(locationToken instanceof LocationTokenImpl)) {
            OrtProvider$$ExternalSyntheticBUOutline0.m("Unknown locationToken=", locationToken);
            return false;
        }
        return isSatCommunicationAllowedAtLocation(((LocationTokenImpl) locationToken).getS2CellId());
    }

    @Override // com.android.phone.satellite.accesscontrol.SatelliteOnDeviceAccessController
    public int getS2Level() {
        return this.mS2Level;
    }

    private boolean isSatCommunicationAllowedAtLocation(long j) throws IOException {
        SuffixTableRange suffixTableRangeFindEntryByCellId = this.mSatS2RangeFileReader.findEntryByCellId(j);
        if (this.mSatS2RangeFileReader.isAllowedList()) {
            return suffixTableRangeFindEntryByCellId != null;
        }
        return suffixTableRangeFindEntryByCellId == null;
    }

    private static S2CellId getS2CellId(double d, double d2, int i) {
        return S2CellId.fromLatLng(S2LatLng.fromDegrees(d, d2)).parent(i);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mSatS2RangeFileReader.close();
    }

    class LocationTokenImpl extends SatelliteOnDeviceAccessController.LocationToken {
        private final long mS2CellId;

        private LocationTokenImpl(long j) {
            this.mS2CellId = j;
        }

        long getS2CellId() {
            return this.mS2CellId;
        }

        public String toString() {
            return "LocationToken{<redacted>}";
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof LocationTokenImpl) && this.mS2CellId == ((LocationTokenImpl) obj).mS2CellId;
        }

        public int hashCode() {
            return Objects.hash(Long.valueOf(this.mS2CellId));
        }
    }

    @Override // com.android.phone.satellite.accesscontrol.SatelliteOnDeviceAccessController
    public Integer getRegionalConfigIdForLocation(SatelliteOnDeviceAccessController.LocationToken locationToken) {
        if (locationToken instanceof LocationTokenImpl) {
            return getRegionalConfigIdForLocation(((LocationTokenImpl) locationToken).getS2CellId());
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unknown locationToken=", locationToken);
        return null;
    }

    private Integer getRegionalConfigIdForLocation(long j) throws IOException {
        SuffixTableRange suffixTableRangeFindEntryByCellId = this.mSatS2RangeFileReader.findEntryByCellId(j);
        if (suffixTableRangeFindEntryByCellId == null) {
            return null;
        }
        return Integer.valueOf(suffixTableRangeFindEntryByCellId.getEntryValue());
    }
}
