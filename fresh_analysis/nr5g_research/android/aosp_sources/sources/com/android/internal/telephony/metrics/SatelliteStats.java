package com.android.internal.telephony.metrics;

import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteControllerStats;
import com.android.internal.telephony.nano.PersistAtomsProto$CarrierRoamingSatelliteSession;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteAccessController;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteConfigUpdater;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteController;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteEntitlement;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteIncomingDatagram;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteOutgoingDatagram;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteProvision;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteSession;
import com.android.internal.telephony.nano.PersistAtomsProto$SatelliteSosMessageRecommender;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteControllerStats;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteStats {
    private static final String TAG = "SatelliteStats";
    private static SatelliteStats sInstance;
    private final PersistAtomsStorage mAtomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();

    public static SatelliteStats getInstance() {
        if (sInstance == null) {
            Rlog.d(TAG, "SatelliteStats created.");
            synchronized (SatelliteStats.class) {
                sInstance = new SatelliteStats();
            }
        }
        return sInstance;
    }

    public static class SatelliteControllerParams {
        private static int sCarrierId = -1;
        private static int sCarrierRoamingSatelliteConfigVersion;
        private static boolean sIsNtnOnlyCarrier;
        private static boolean sIsProvisioned;
        private static int sMaxAllowedDataMode;
        private static int sSupportedConnectionMode;
        private static int sVersionOfSatelliteAccessConfig;
        private final int mCountOfAllowedSatelliteAccess;
        private final int mCountOfDatagramTypeKeepAliveFail;
        private final int mCountOfDatagramTypeKeepAliveSuccess;
        private final int mCountOfDatagramTypeLocationSharingFail;
        private final int mCountOfDatagramTypeLocationSharingSuccess;
        private final int mCountOfDatagramTypeSosSmsFail;
        private final int mCountOfDatagramTypeSosSmsSuccess;
        private final int mCountOfDemoModeIncomingDatagramFail;
        private final int mCountOfDemoModeIncomingDatagramSuccess;
        private final int mCountOfDemoModeOutgoingDatagramFail;
        private final int mCountOfDemoModeOutgoingDatagramSuccess;
        private final int mCountOfDemoModeSatelliteServiceEnablementsFail;
        private final int mCountOfDemoModeSatelliteServiceEnablementsSuccess;
        private final int mCountOfDeprovisionFail;
        private final int mCountOfDeprovisionSuccess;
        private final int mCountOfDisallowedSatelliteAccess;
        private final int mCountOfFailedLocationQueries;
        private final int mCountOfIncomingDatagramFail;
        private final int mCountOfIncomingDatagramSuccess;
        private final int mCountOfIncomingDatagramTypeSmsFail;
        private final int mCountOfIncomingDatagramTypeSmsSuccess;
        private final int mCountOfIncomingDatagramTypeSosSmsFail;
        private final int mCountOfIncomingDatagramTypeSosSmsSuccess;
        private final int mCountOfOutgoingDatagramFail;
        private final int mCountOfOutgoingDatagramSuccess;
        private final int mCountOfOutgoingDatagramTypeSmsFail;
        private final int mCountOfOutgoingDatagramTypeSmsSuccess;
        private final int mCountOfP2PSmsAvailableNotificationRemoved;
        private final int mCountOfP2PSmsAvailableNotificationShown;
        private final int mCountOfProvisionFail;
        private final int mCountOfProvisionSuccess;
        private final int mCountOfSatelliteAccessCheckFail;
        private final int mCountOfSatelliteAllowedStateChangedEvents;
        private final int mCountOfSatelliteServiceEnablementsFail;
        private final int mCountOfSatelliteServiceEnablementsSuccess;
        private final int mCountOfSuccessfulLocationQueries;
        private final int mTotalBatteryChargedTimeSec;
        private final int mTotalBatteryConsumptionPercent;
        private final int mTotalServiceUptimeSec;

        private SatelliteControllerParams(Builder builder) {
            this.mCountOfSatelliteServiceEnablementsSuccess = builder.mCountOfSatelliteServiceEnablementsSuccess;
            this.mCountOfSatelliteServiceEnablementsFail = builder.mCountOfSatelliteServiceEnablementsFail;
            this.mCountOfOutgoingDatagramSuccess = builder.mCountOfOutgoingDatagramSuccess;
            this.mCountOfOutgoingDatagramFail = builder.mCountOfOutgoingDatagramFail;
            this.mCountOfIncomingDatagramSuccess = builder.mCountOfIncomingDatagramSuccess;
            this.mCountOfIncomingDatagramFail = builder.mCountOfIncomingDatagramFail;
            this.mCountOfDatagramTypeSosSmsSuccess = builder.mCountOfDatagramTypeSosSmsSuccess;
            this.mCountOfDatagramTypeSosSmsFail = builder.mCountOfDatagramTypeSosSmsFail;
            this.mCountOfDatagramTypeLocationSharingSuccess = builder.mCountOfDatagramTypeLocationSharingSuccess;
            this.mCountOfDatagramTypeLocationSharingFail = builder.mCountOfDatagramTypeLocationSharingFail;
            this.mCountOfProvisionSuccess = builder.mCountOfProvisionSuccess;
            this.mCountOfProvisionFail = builder.mCountOfProvisionFail;
            this.mCountOfDeprovisionSuccess = builder.mCountOfDeprovisionSuccess;
            this.mCountOfDeprovisionFail = builder.mCountOfDeprovisionFail;
            this.mTotalServiceUptimeSec = builder.mTotalServiceUptimeSec;
            this.mTotalBatteryConsumptionPercent = builder.mTotalBatteryConsumptionPercent;
            this.mTotalBatteryChargedTimeSec = builder.mTotalBatteryChargedTimeSec;
            this.mCountOfDemoModeSatelliteServiceEnablementsSuccess = builder.mCountOfDemoModeSatelliteServiceEnablementsSuccess;
            this.mCountOfDemoModeSatelliteServiceEnablementsFail = builder.mCountOfDemoModeSatelliteServiceEnablementsFail;
            this.mCountOfDemoModeOutgoingDatagramSuccess = builder.mCountOfDemoModeOutgoingDatagramSuccess;
            this.mCountOfDemoModeOutgoingDatagramFail = builder.mCountOfDemoModeOutgoingDatagramFail;
            this.mCountOfDemoModeIncomingDatagramSuccess = builder.mCountOfDemoModeIncomingDatagramSuccess;
            this.mCountOfDemoModeIncomingDatagramFail = builder.mCountOfDemoModeIncomingDatagramFail;
            this.mCountOfDatagramTypeKeepAliveSuccess = builder.mCountOfDatagramTypeKeepAliveSuccess;
            this.mCountOfDatagramTypeKeepAliveFail = builder.mCountOfDatagramTypeKeepAliveFail;
            this.mCountOfAllowedSatelliteAccess = builder.mCountOfAllowedSatelliteAccess;
            this.mCountOfDisallowedSatelliteAccess = builder.mCountOfDisallowedSatelliteAccess;
            this.mCountOfSatelliteAccessCheckFail = builder.mCountOfSatelliteAccessCheckFail;
            if (builder.mIsProvisioned.isPresent()) {
                sIsProvisioned = ((Boolean) builder.mIsProvisioned.get()).booleanValue();
            }
            if (builder.mCarrierId.isPresent()) {
                sCarrierId = ((Integer) builder.mCarrierId.get()).intValue();
            }
            if (builder.mSupportedConnectionMode.isPresent()) {
                sSupportedConnectionMode = ((Integer) builder.mSupportedConnectionMode.get()).intValue();
            }
            this.mCountOfSatelliteAllowedStateChangedEvents = builder.mCountOfSatelliteAllowedStateChangedEvents;
            this.mCountOfSuccessfulLocationQueries = builder.mCountOfSuccessfulLocationQueries;
            this.mCountOfFailedLocationQueries = builder.mCountOfFailedLocationQueries;
            this.mCountOfP2PSmsAvailableNotificationShown = builder.mCountOfP2PSmsAvailableNotificationShown;
            this.mCountOfP2PSmsAvailableNotificationRemoved = builder.mCountOfP2PSmsAvailableNotificationRemoved;
            if (builder.mIsNtnOnlyCarrier.isPresent()) {
                sIsNtnOnlyCarrier = ((Boolean) builder.mIsNtnOnlyCarrier.get()).booleanValue();
            }
            if (builder.mVersionOfSatelliteAccessConfig.isPresent()) {
                sVersionOfSatelliteAccessConfig = ((Integer) builder.mVersionOfSatelliteAccessConfig.get()).intValue();
            }
            this.mCountOfIncomingDatagramTypeSosSmsSuccess = builder.mCountOfIncomingDatagramTypeSosSmsSuccess;
            this.mCountOfIncomingDatagramTypeSosSmsFail = builder.mCountOfIncomingDatagramTypeSosSmsFail;
            this.mCountOfOutgoingDatagramTypeSmsSuccess = builder.mCountOfOutgoingDatagramTypeSmsSuccess;
            this.mCountOfOutgoingDatagramTypeSmsFail = builder.mCountOfOutgoingDatagramTypeSmsFail;
            this.mCountOfIncomingDatagramTypeSmsSuccess = builder.mCountOfIncomingDatagramTypeSmsSuccess;
            this.mCountOfIncomingDatagramTypeSmsFail = builder.mCountOfIncomingDatagramTypeSmsFail;
            if (builder.mCarrierRoamingSatelliteConfigVersion.isPresent()) {
                sCarrierRoamingSatelliteConfigVersion = ((Integer) builder.mCarrierRoamingSatelliteConfigVersion.get()).intValue();
            }
            if (builder.mMaxAllowedDataMode.isPresent()) {
                sMaxAllowedDataMode = ((Integer) builder.mMaxAllowedDataMode.get()).intValue();
            }
        }

        public int getCountOfSatelliteServiceEnablementsSuccess() {
            return this.mCountOfSatelliteServiceEnablementsSuccess;
        }

        public int getCountOfSatelliteServiceEnablementsFail() {
            return this.mCountOfSatelliteServiceEnablementsFail;
        }

        public int getCountOfOutgoingDatagramSuccess() {
            return this.mCountOfOutgoingDatagramSuccess;
        }

        public int getCountOfOutgoingDatagramFail() {
            return this.mCountOfOutgoingDatagramFail;
        }

        public int getCountOfIncomingDatagramSuccess() {
            return this.mCountOfIncomingDatagramSuccess;
        }

        public int getCountOfIncomingDatagramFail() {
            return this.mCountOfIncomingDatagramFail;
        }

        public int getCountOfDatagramTypeSosSmsSuccess() {
            return this.mCountOfDatagramTypeSosSmsSuccess;
        }

        public int getCountOfDatagramTypeSosSmsFail() {
            return this.mCountOfDatagramTypeSosSmsFail;
        }

        public int getCountOfDatagramTypeLocationSharingSuccess() {
            return this.mCountOfDatagramTypeLocationSharingSuccess;
        }

        public int getCountOfDatagramTypeLocationSharingFail() {
            return this.mCountOfDatagramTypeLocationSharingFail;
        }

        public int getCountOfProvisionSuccess() {
            return this.mCountOfProvisionSuccess;
        }

        public int getCountOfProvisionFail() {
            return this.mCountOfProvisionFail;
        }

        public int getCountOfDeprovisionSuccess() {
            return this.mCountOfDeprovisionSuccess;
        }

        public int getCountOfDeprovisionFail() {
            return this.mCountOfDeprovisionFail;
        }

        public int getTotalServiceUptimeSec() {
            return this.mTotalServiceUptimeSec;
        }

        public int getTotalBatteryConsumptionPercent() {
            return this.mTotalBatteryConsumptionPercent;
        }

        public int getTotalBatteryChargedTimeSec() {
            return this.mTotalBatteryChargedTimeSec;
        }

        public int getCountOfDemoModeSatelliteServiceEnablementsSuccess() {
            return this.mCountOfDemoModeSatelliteServiceEnablementsSuccess;
        }

        public int getCountOfDemoModeSatelliteServiceEnablementsFail() {
            return this.mCountOfDemoModeSatelliteServiceEnablementsFail;
        }

        public int getCountOfDemoModeOutgoingDatagramSuccess() {
            return this.mCountOfDemoModeOutgoingDatagramSuccess;
        }

        public int getCountOfDemoModeOutgoingDatagramFail() {
            return this.mCountOfDemoModeOutgoingDatagramFail;
        }

        public int getCountOfDemoModeIncomingDatagramSuccess() {
            return this.mCountOfDemoModeIncomingDatagramSuccess;
        }

        public int getCountOfDemoModeIncomingDatagramFail() {
            return this.mCountOfDemoModeIncomingDatagramFail;
        }

        public int getCountOfDatagramTypeKeepAliveSuccess() {
            return this.mCountOfDatagramTypeKeepAliveSuccess;
        }

        public int getCountOfDatagramTypeKeepAliveFail() {
            return this.mCountOfDatagramTypeKeepAliveFail;
        }

        public int getCountOfAllowedSatelliteAccess() {
            return this.mCountOfAllowedSatelliteAccess;
        }

        public int getCountOfDisallowedSatelliteAccess() {
            return this.mCountOfDisallowedSatelliteAccess;
        }

        public int getCountOfSatelliteAccessCheckFail() {
            return this.mCountOfSatelliteAccessCheckFail;
        }

        public static boolean isProvisioned() {
            return sIsProvisioned;
        }

        public static int getCarrierId() {
            return sCarrierId;
        }

        public int getCountOfSatelliteAllowedStateChangedEvents() {
            return this.mCountOfSatelliteAllowedStateChangedEvents;
        }

        public int getCountOfSuccessfulLocationQueries() {
            return this.mCountOfSuccessfulLocationQueries;
        }

        public int getCountOfFailedLocationQueries() {
            return this.mCountOfFailedLocationQueries;
        }

        public int getCountOfP2PSmsAvailableNotificationShown() {
            return this.mCountOfP2PSmsAvailableNotificationShown;
        }

        public int getCountOfP2PSmsAvailableNotificationRemoved() {
            return this.mCountOfP2PSmsAvailableNotificationRemoved;
        }

        public static boolean isNtnOnlyCarrier() {
            return sIsNtnOnlyCarrier;
        }

        public static int getVersionSatelliteAccessConfig() {
            return sVersionOfSatelliteAccessConfig;
        }

        public int getCountOfIncomingDatagramTypeSosSmsSuccess() {
            return this.mCountOfIncomingDatagramTypeSosSmsSuccess;
        }

        public int getCountOfIncomingDatagramTypeSosSmsFail() {
            return this.mCountOfIncomingDatagramTypeSosSmsFail;
        }

        public int getCountOfOutgoingDatagramTypeSmsSuccess() {
            return this.mCountOfOutgoingDatagramTypeSmsSuccess;
        }

        public int getCountOfOutgoingDatagramTypeSmsFail() {
            return this.mCountOfOutgoingDatagramTypeSmsFail;
        }

        public int getCountOfIncomingDatagramTypeSmsSuccess() {
            return this.mCountOfIncomingDatagramTypeSmsSuccess;
        }

        public int getCountOfIncomingDatagramTypeSmsFail() {
            return this.mCountOfIncomingDatagramTypeSmsFail;
        }

        public static int getCarrierRoamingSatelliteConfigVersion() {
            return sCarrierRoamingSatelliteConfigVersion;
        }

        public static int getMaxAllowedDataMode() {
            return sMaxAllowedDataMode;
        }

        public static int getSupportedConnectionMode() {
            return sSupportedConnectionMode;
        }

        public static class Builder {
            private int mCountOfDeprovisionFail;
            private int mCountOfDeprovisionSuccess;
            private int mCountOfIncomingDatagramTypeSmsFail;
            private int mCountOfIncomingDatagramTypeSmsSuccess;
            private int mCountOfIncomingDatagramTypeSosSmsFail;
            private int mCountOfIncomingDatagramTypeSosSmsSuccess;
            private int mCountOfOutgoingDatagramTypeSmsFail;
            private int mCountOfOutgoingDatagramTypeSmsSuccess;
            private int mCountOfProvisionFail;
            private int mCountOfProvisionSuccess;
            private int mCountOfSatelliteServiceEnablementsSuccess = 0;
            private int mCountOfSatelliteServiceEnablementsFail = 0;
            private int mCountOfOutgoingDatagramSuccess = 0;
            private int mCountOfOutgoingDatagramFail = 0;
            private int mCountOfIncomingDatagramSuccess = 0;
            private int mCountOfIncomingDatagramFail = 0;
            private int mCountOfDatagramTypeSosSmsSuccess = 0;
            private int mCountOfDatagramTypeSosSmsFail = 0;
            private int mCountOfDatagramTypeLocationSharingSuccess = 0;
            private int mCountOfDatagramTypeLocationSharingFail = 0;
            private int mTotalServiceUptimeSec = 0;
            private int mTotalBatteryConsumptionPercent = 0;
            private int mTotalBatteryChargedTimeSec = 0;
            private int mCountOfDemoModeSatelliteServiceEnablementsSuccess = 0;
            private int mCountOfDemoModeSatelliteServiceEnablementsFail = 0;
            private int mCountOfDemoModeOutgoingDatagramSuccess = 0;
            private int mCountOfDemoModeOutgoingDatagramFail = 0;
            private int mCountOfDemoModeIncomingDatagramSuccess = 0;
            private int mCountOfDemoModeIncomingDatagramFail = 0;
            private int mCountOfDatagramTypeKeepAliveSuccess = 0;
            private int mCountOfDatagramTypeKeepAliveFail = 0;
            private int mCountOfAllowedSatelliteAccess = 0;
            private int mCountOfDisallowedSatelliteAccess = 0;
            private int mCountOfSatelliteAccessCheckFail = 0;
            private Optional mIsProvisioned = Optional.empty();
            private Optional mCarrierId = Optional.empty();
            private Optional mSupportedConnectionMode = Optional.empty();
            private int mCountOfSatelliteAllowedStateChangedEvents = 0;
            private int mCountOfSuccessfulLocationQueries = 0;
            private int mCountOfFailedLocationQueries = 0;
            private int mCountOfP2PSmsAvailableNotificationShown = 0;
            private int mCountOfP2PSmsAvailableNotificationRemoved = 0;
            private Optional mIsNtnOnlyCarrier = Optional.empty();
            private Optional mVersionOfSatelliteAccessConfig = Optional.empty();
            private Optional mCarrierRoamingSatelliteConfigVersion = Optional.empty();
            private Optional mMaxAllowedDataMode = Optional.empty();

            public Builder setCountOfSatelliteServiceEnablementsSuccess(int i) {
                this.mCountOfSatelliteServiceEnablementsSuccess = i;
                return this;
            }

            public Builder setCountOfSatelliteServiceEnablementsFail(int i) {
                this.mCountOfSatelliteServiceEnablementsFail = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramSuccess(int i) {
                this.mCountOfOutgoingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramFail(int i) {
                this.mCountOfOutgoingDatagramFail = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramSuccess(int i) {
                this.mCountOfIncomingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramFail(int i) {
                this.mCountOfIncomingDatagramFail = i;
                return this;
            }

            public Builder setCountOfDatagramTypeSosSmsSuccess(int i) {
                this.mCountOfDatagramTypeSosSmsSuccess = i;
                return this;
            }

            public Builder setCountOfDatagramTypeSosSmsFail(int i) {
                this.mCountOfDatagramTypeSosSmsFail = i;
                return this;
            }

            public Builder setCountOfDatagramTypeLocationSharingSuccess(int i) {
                this.mCountOfDatagramTypeLocationSharingSuccess = i;
                return this;
            }

            public Builder setCountOfDatagramTypeLocationSharingFail(int i) {
                this.mCountOfDatagramTypeLocationSharingFail = i;
                return this;
            }

            public Builder setCountOfProvisionSuccess(int i) {
                this.mCountOfProvisionSuccess = i;
                return this;
            }

            public Builder setCountOfProvisionFail(int i) {
                this.mCountOfProvisionFail = i;
                return this;
            }

            public Builder setCountOfDeprovisionSuccess(int i) {
                this.mCountOfDeprovisionSuccess = i;
                return this;
            }

            public Builder setCountOfDeprovisionFail(int i) {
                this.mCountOfDeprovisionFail = i;
                return this;
            }

            public Builder setTotalServiceUptimeSec(int i) {
                this.mTotalServiceUptimeSec = i;
                return this;
            }

            public Builder setTotalBatteryConsumptionPercent(int i) {
                this.mTotalBatteryConsumptionPercent = i;
                return this;
            }

            public Builder setTotalBatteryChargedTimeSec(int i) {
                this.mTotalBatteryChargedTimeSec = i;
                return this;
            }

            public Builder setCountOfDemoModeSatelliteServiceEnablementsSuccess(int i) {
                this.mCountOfDemoModeSatelliteServiceEnablementsSuccess = i;
                return this;
            }

            public Builder setCountOfDemoModeSatelliteServiceEnablementsFail(int i) {
                this.mCountOfDemoModeSatelliteServiceEnablementsFail = i;
                return this;
            }

            public Builder setCountOfDemoModeOutgoingDatagramSuccess(int i) {
                this.mCountOfDemoModeOutgoingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfDemoModeOutgoingDatagramFail(int i) {
                this.mCountOfDemoModeOutgoingDatagramFail = i;
                return this;
            }

            public Builder setCountOfDemoModeIncomingDatagramSuccess(int i) {
                this.mCountOfDemoModeIncomingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfDemoModeIncomingDatagramFail(int i) {
                this.mCountOfDemoModeIncomingDatagramFail = i;
                return this;
            }

            public Builder setCountOfDatagramTypeKeepAliveSuccess(int i) {
                this.mCountOfDatagramTypeKeepAliveSuccess = i;
                return this;
            }

            public Builder setCountOfDatagramTypeKeepAliveFail(int i) {
                this.mCountOfDatagramTypeKeepAliveFail = i;
                return this;
            }

            public Builder setCountOfAllowedSatelliteAccess(int i) {
                this.mCountOfAllowedSatelliteAccess = i;
                return this;
            }

            public Builder setCountOfDisallowedSatelliteAccess(int i) {
                this.mCountOfDisallowedSatelliteAccess = i;
                return this;
            }

            public Builder setCountOfSatelliteAccessCheckFail(int i) {
                this.mCountOfSatelliteAccessCheckFail = i;
                return this;
            }

            public Builder setIsProvisioned(boolean z) {
                this.mIsProvisioned = Optional.of(Boolean.valueOf(z));
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setCountOfSatelliteAllowedStateChangedEvents(int i) {
                this.mCountOfSatelliteAllowedStateChangedEvents = i;
                return this;
            }

            public Builder setCountOfSuccessfulLocationQueries(int i) {
                this.mCountOfSuccessfulLocationQueries = i;
                return this;
            }

            public Builder setCountOfFailedLocationQueries(int i) {
                this.mCountOfFailedLocationQueries = i;
                return this;
            }

            public Builder setCountOfP2PSmsAvailableNotificationShown(int i) {
                this.mCountOfP2PSmsAvailableNotificationShown = i;
                return this;
            }

            public Builder setCountOfP2PSmsAvailableNotificationRemoved(int i) {
                this.mCountOfP2PSmsAvailableNotificationRemoved = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = Optional.of(Boolean.valueOf(z));
                return this;
            }

            public Builder setVersionOfSatelliteAccessControl(int i) {
                this.mVersionOfSatelliteAccessConfig = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setCountOfIncomingDatagramTypeSosSmsSuccess(int i) {
                this.mCountOfIncomingDatagramTypeSosSmsSuccess = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramTypeSosSmsFail(int i) {
                this.mCountOfIncomingDatagramTypeSosSmsFail = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramTypeSmsSuccess(int i) {
                this.mCountOfOutgoingDatagramTypeSmsSuccess = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramTypeSmsFail(int i) {
                this.mCountOfOutgoingDatagramTypeSmsFail = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramTypeSmsSuccess(int i) {
                this.mCountOfIncomingDatagramTypeSmsSuccess = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramTypeSmsFail(int i) {
                this.mCountOfIncomingDatagramTypeSmsFail = i;
                return this;
            }

            public Builder setCarrierRoamingSatelliteConfigVersion(int i) {
                this.mCarrierRoamingSatelliteConfigVersion = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setMaxAllowedDataMode(int i) {
                this.mMaxAllowedDataMode = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = Optional.of(Integer.valueOf(i));
                return this;
            }

            public SatelliteControllerParams build() {
                return new SatelliteControllerParams(this);
            }
        }

        public String toString() {
            return "ControllerParams(, countOfSatelliteServiceEnablementsSuccess=" + this.mCountOfSatelliteServiceEnablementsSuccess + ", countOfSatelliteServiceEnablementsFail=" + this.mCountOfSatelliteServiceEnablementsFail + ", countOfOutgoingDatagramSuccess=" + this.mCountOfOutgoingDatagramSuccess + ", countOfOutgoingDatagramFail=" + this.mCountOfOutgoingDatagramFail + ", countOfIncomingDatagramSuccess=" + this.mCountOfIncomingDatagramSuccess + ", countOfIncomingDatagramFail=" + this.mCountOfIncomingDatagramFail + ", countOfDatagramTypeSosSms=" + this.mCountOfDatagramTypeSosSmsSuccess + ", countOfDatagramTypeSosSms=" + this.mCountOfDatagramTypeSosSmsFail + ", countOfDatagramTypeLocationSharing=" + this.mCountOfDatagramTypeLocationSharingSuccess + ", countOfDatagramTypeLocationSharing=" + this.mCountOfDatagramTypeLocationSharingFail + ", serviceUptimeSec=" + this.mTotalServiceUptimeSec + ", batteryConsumptionPercent=" + this.mTotalBatteryConsumptionPercent + ", batteryChargedTimeSec=" + this.mTotalBatteryChargedTimeSec + ", countOfDemoModeSatelliteServiceEnablementsSuccess=" + this.mCountOfDemoModeSatelliteServiceEnablementsSuccess + ", countOfDemoModeSatelliteServiceEnablementsFail=" + this.mCountOfDemoModeSatelliteServiceEnablementsFail + ", countOfDemoModeOutgoingDatagramSuccess=" + this.mCountOfDemoModeOutgoingDatagramSuccess + ", countOfDemoModeOutgoingDatagramFail=" + this.mCountOfDemoModeOutgoingDatagramFail + ", countOfDemoModeIncomingDatagramSuccess=" + this.mCountOfDemoModeIncomingDatagramSuccess + ", countOfDemoModeIncomingDatagramFail=" + this.mCountOfDemoModeIncomingDatagramFail + ", countOfDatagramTypeKeepAliveSuccess=" + this.mCountOfDatagramTypeKeepAliveSuccess + ", countOfDatagramTypeKeepAliveFail=" + this.mCountOfDatagramTypeKeepAliveFail + ", countOfAllowedSatelliteAccess=" + this.mCountOfAllowedSatelliteAccess + ", countOfDisallowedSatelliteAccess=" + this.mCountOfDisallowedSatelliteAccess + ", countOfSatelliteAccessCheckFail=" + this.mCountOfSatelliteAccessCheckFail + ", isProvisioned=" + sIsProvisioned + ", carrierId=" + sCarrierId + ", countOfSatelliteAllowedStateChangedEvents=" + this.mCountOfSatelliteAllowedStateChangedEvents + ", countOfSuccessfulLocationQueries=" + this.mCountOfSuccessfulLocationQueries + ", countOfFailedLocationQueries=" + this.mCountOfFailedLocationQueries + ", countOfP2PSmsAvailableNotificationShown=" + this.mCountOfP2PSmsAvailableNotificationShown + ", countOfP2PSmsAvailableNotificationRemoved=" + this.mCountOfP2PSmsAvailableNotificationRemoved + ", isNtnOnlyCarrier=" + sIsNtnOnlyCarrier + ", versionOfSatelliteAccessConfig=" + sVersionOfSatelliteAccessConfig + ", countOfIncomingDatagramTypeSosSmsSuccess=" + this.mCountOfIncomingDatagramTypeSosSmsSuccess + ", countOfIncomingDatagramTypeSosSmsFail=" + this.mCountOfIncomingDatagramTypeSosSmsFail + ", countOfOutgoingDatagramTypeSmsSuccess=" + this.mCountOfOutgoingDatagramTypeSmsSuccess + ", countOfOutgoingDatagramTypeSmsFail=" + this.mCountOfOutgoingDatagramTypeSmsFail + ", countOfIncomingDatagramTypeSmsSuccess=" + this.mCountOfIncomingDatagramTypeSmsSuccess + ", countOfIncomingDatagramTypeSmsFail=" + this.mCountOfIncomingDatagramTypeSmsFail + ", carrierRoamingSatelliteConfigVersion=" + sCarrierRoamingSatelliteConfigVersion + ", maxAllowedDataMode=" + sMaxAllowedDataMode + ", supportedConnectionMode=" + sSupportedConnectionMode + ")";
        }
    }

    public static class SatelliteSessionParams {
        private final int mBatteryDesignCapacityMah;
        private final int mBatteryLevelDropPercent;
        private final int mCarrierId;
        private final int mCountOfAutoExitDueToScreenOff;
        private final int mCountOfAutoExitDueToTnNetwork;
        private final int mCountOfIncomingDatagramFailed;
        private final int mCountOfIncomingDatagramSuccess;
        private final int mCountOfOutgoingDatagramFailed;
        private final int mCountOfOutgoingDatagramSuccess;
        private final int mCountOfSatelliteNotificationDisplayed;
        private final long mEnergyConsumedNwh;
        private final long mInitializationProcessingTimeMillis;
        private final boolean mIsDemoMode;
        private final boolean mIsEmergency;
        private final boolean mIsNtnOnlyCarrier;
        private final int mMaxInactivityDurationSec;
        private final int mMaxNtnSignalStrengthLevel;
        private final String mPlmn;
        private final int mSatelliteServiceInitializationResult;
        private final int mSatelliteTechnology;
        private final int mScreenOnTimeSec;
        private final int mSessionConnectionMode;
        private final int mSessionDurationSec;
        private final int mSupportedConnectionMode;
        private final long mTerminationProcessingTimeMillis;
        private final int mTerminationResult;
        private final boolean mWasChargingDuringSession;

        private SatelliteSessionParams(Builder builder) {
            this.mSatelliteServiceInitializationResult = builder.mSatelliteServiceInitializationResult;
            this.mSatelliteTechnology = builder.mSatelliteTechnology;
            this.mTerminationResult = builder.mTerminationResult;
            this.mInitializationProcessingTimeMillis = builder.mInitializationProcessingTimeMillis;
            this.mTerminationProcessingTimeMillis = builder.mTerminationProcessingTimeMillis;
            this.mSessionDurationSec = builder.mSessionDurationSec;
            this.mCountOfOutgoingDatagramSuccess = builder.mCountOfOutgoingDatagramSuccess;
            this.mCountOfOutgoingDatagramFailed = builder.mCountOfOutgoingDatagramFailed;
            this.mCountOfIncomingDatagramSuccess = builder.mCountOfIncomingDatagramSuccess;
            this.mCountOfIncomingDatagramFailed = builder.mCountOfIncomingDatagramFailed;
            this.mIsDemoMode = builder.mIsDemoMode;
            this.mMaxNtnSignalStrengthLevel = builder.mMaxNtnSignalStrengthLevel;
            this.mCarrierId = builder.mCarrierId;
            this.mCountOfSatelliteNotificationDisplayed = builder.mCountOfSatelliteNotificationDisplayed;
            this.mCountOfAutoExitDueToScreenOff = builder.mCountOfAutoExitDueToScreenOff;
            this.mCountOfAutoExitDueToTnNetwork = builder.mCountOfAutoExitDueToTnNetwork;
            this.mIsEmergency = builder.mIsEmergency;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mMaxInactivityDurationSec = builder.mMaxInactivityDurationSec;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
            this.mPlmn = builder.mPlmn;
            this.mScreenOnTimeSec = builder.mScreenOnTimeSec;
            this.mBatteryLevelDropPercent = builder.mBatteryLevelDropPercent;
            this.mWasChargingDuringSession = builder.mWasChargingDuringSession;
            this.mBatteryDesignCapacityMah = builder.mBatteryDesignCapacityMah;
            this.mEnergyConsumedNwh = builder.mEnergyConsumedNwh;
        }

        public int getSatelliteServiceInitializationResult() {
            return this.mSatelliteServiceInitializationResult;
        }

        public int getSatelliteTechnology() {
            return this.mSatelliteTechnology;
        }

        public int getTerminationResult() {
            return this.mTerminationResult;
        }

        public long getInitializationProcessingTime() {
            return this.mInitializationProcessingTimeMillis;
        }

        public long getTerminationProcessingTime() {
            return this.mTerminationProcessingTimeMillis;
        }

        public int getSessionDuration() {
            return this.mSessionDurationSec;
        }

        public int getCountOfOutgoingDatagramSuccess() {
            return this.mCountOfOutgoingDatagramSuccess;
        }

        public int getCountOfOutgoingDatagramFailed() {
            return this.mCountOfOutgoingDatagramFailed;
        }

        public int getCountOfIncomingDatagramSuccess() {
            return this.mCountOfIncomingDatagramSuccess;
        }

        public int getCountOfIncomingDatagramFailed() {
            return this.mCountOfIncomingDatagramFailed;
        }

        public boolean getIsDemoMode() {
            return this.mIsDemoMode;
        }

        public int getMaxNtnSignalStrengthLevel() {
            return this.mMaxNtnSignalStrengthLevel;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public int getCountOfSatelliteNotificationDisplayed() {
            return this.mCountOfSatelliteNotificationDisplayed;
        }

        public int getCountOfAutoExitDueToScreenOff() {
            return this.mCountOfAutoExitDueToScreenOff;
        }

        public int getCountOfAutoExitDueToTnNetwork() {
            return this.mCountOfAutoExitDueToTnNetwork;
        }

        public boolean getIsEmergency() {
            return this.mIsEmergency;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getMaxInactivityDurationSec() {
            return this.mMaxInactivityDurationSec;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public int getScreenOnTimeSec() {
            return this.mScreenOnTimeSec;
        }

        public int getBatteryLevelDropPercent() {
            return this.mBatteryLevelDropPercent;
        }

        public boolean wasChargingDuringSession() {
            return this.mWasChargingDuringSession;
        }

        public int getBatteryDesignCapacityMah() {
            return this.mBatteryDesignCapacityMah;
        }

        public long getEnergyConsumedNwh() {
            return this.mEnergyConsumedNwh;
        }

        public static class Builder {
            private int mSatelliteServiceInitializationResult = -1;
            private int mSatelliteTechnology = -1;
            private int mTerminationResult = -1;
            private long mInitializationProcessingTimeMillis = -1;
            private long mTerminationProcessingTimeMillis = -1;
            private int mSessionDurationSec = -1;
            private int mCountOfOutgoingDatagramSuccess = -1;
            private int mCountOfOutgoingDatagramFailed = -1;
            private int mCountOfIncomingDatagramSuccess = -1;
            private int mCountOfIncomingDatagramFailed = -1;
            private boolean mIsDemoMode = false;
            private int mMaxNtnSignalStrengthLevel = 0;
            private int mCarrierId = -1;
            private int mCountOfSatelliteNotificationDisplayed = -1;
            private int mCountOfAutoExitDueToScreenOff = -1;
            private int mCountOfAutoExitDueToTnNetwork = -1;
            private boolean mIsEmergency = false;
            private boolean mIsNtnOnlyCarrier = false;
            private int mMaxInactivityDurationSec = -1;
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;
            private String mPlmn = SatelliteConstants.DEFAULT_PLMN;
            private int mScreenOnTimeSec = 0;
            private int mBatteryLevelDropPercent = 0;
            private boolean mWasChargingDuringSession = false;
            private int mBatteryDesignCapacityMah = 0;
            private long mEnergyConsumedNwh = 0;

            public Builder setSatelliteServiceInitializationResult(int i) {
                this.mSatelliteServiceInitializationResult = i;
                return this;
            }

            public Builder setSatelliteTechnology(int i) {
                this.mSatelliteTechnology = i;
                return this;
            }

            public Builder setTerminationResult(int i) {
                this.mTerminationResult = i;
                return this;
            }

            public Builder setInitializationProcessingTime(long j) {
                this.mInitializationProcessingTimeMillis = j;
                return this;
            }

            public Builder setTerminationProcessingTime(long j) {
                this.mTerminationProcessingTimeMillis = j;
                return this;
            }

            public Builder setSessionDuration(int i) {
                this.mSessionDurationSec = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramSuccess(int i) {
                this.mCountOfOutgoingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfOutgoingDatagramFailed(int i) {
                this.mCountOfOutgoingDatagramFailed = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramSuccess(int i) {
                this.mCountOfIncomingDatagramSuccess = i;
                return this;
            }

            public Builder setCountOfIncomingDatagramFailed(int i) {
                this.mCountOfIncomingDatagramFailed = i;
                return this;
            }

            public Builder setIsDemoMode(boolean z) {
                this.mIsDemoMode = z;
                return this;
            }

            public Builder setMaxNtnSignalStrengthLevel(int i) {
                this.mMaxNtnSignalStrengthLevel = i;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setCountOfSatelliteNotificationDisplayed(int i) {
                this.mCountOfSatelliteNotificationDisplayed = i;
                return this;
            }

            public Builder setCountOfAutoExitDueToScreenOff(int i) {
                this.mCountOfAutoExitDueToScreenOff = i;
                return this;
            }

            public Builder setCountOfAutoExitDueToTnNetwork(int i) {
                this.mCountOfAutoExitDueToTnNetwork = i;
                return this;
            }

            public Builder setIsEmergency(boolean z) {
                this.mIsEmergency = z;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setMaxInactivityDurationSec(int i) {
                this.mMaxInactivityDurationSec = i;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public Builder setPlmn(String str) {
                this.mPlmn = str;
                return this;
            }

            public Builder setScreenOnTimeSec(int i) {
                this.mScreenOnTimeSec = i;
                return this;
            }

            public Builder setBatteryLevelDropPercent(int i) {
                this.mBatteryLevelDropPercent = i;
                return this;
            }

            public Builder setWasChargingDuringSession(boolean z) {
                this.mWasChargingDuringSession = z;
                return this;
            }

            public Builder setBatteryDesignCapacityMah(int i) {
                this.mBatteryDesignCapacityMah = i;
                return this;
            }

            public Builder setEnergyConsumedNwh(long j) {
                this.mEnergyConsumedNwh = j;
                return this;
            }

            public SatelliteSessionParams build() {
                return new SatelliteSessionParams(this);
            }
        }

        public String toString() {
            return "SessionParams(, satelliteServiceInitializationResult=" + this.mSatelliteServiceInitializationResult + ", TerminationResult=" + this.mTerminationResult + ", InitializationProcessingTimeMillis=" + this.mInitializationProcessingTimeMillis + ", TerminationProcessingTimeMillis=" + this.mTerminationProcessingTimeMillis + ", SessionDurationSec=" + this.mSessionDurationSec + ", CountOfOutgoingDatagramSuccess=" + this.mCountOfOutgoingDatagramSuccess + ", CountOfOutgoingDatagramFailed=" + this.mCountOfOutgoingDatagramFailed + ", CountOfIncomingDatagramSuccess=" + this.mCountOfIncomingDatagramSuccess + ", CountOfIncomingDatagramFailed=" + this.mCountOfIncomingDatagramFailed + ", IsDemoMode=" + this.mIsDemoMode + ", MaxNtnSignalStrengthLevel=" + this.mMaxNtnSignalStrengthLevel + ", CarrierId=" + this.mCarrierId + ", CountOfSatelliteNotificationDisplayed" + this.mCountOfSatelliteNotificationDisplayed + ", CountOfAutoExitDueToScreenOff" + this.mCountOfAutoExitDueToScreenOff + ", CountOfAutoExitDueToTnNetwork" + this.mCountOfAutoExitDueToTnNetwork + ", IsEmergency=" + this.mIsEmergency + ", IsNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + ", MaxInactivityDurationSec=" + this.mMaxInactivityDurationSec + ", SupportedConnectionMode=" + this.mSupportedConnectionMode + ", SessionConnectionMode=" + this.mSessionConnectionMode + ", PLMN=" + this.mPlmn + ", ScreenOntimeSec=" + this.mScreenOnTimeSec + ", BatteryLevelDropPercent=" + this.mBatteryLevelDropPercent + ", WasChargingDuringSession=" + this.mWasChargingDuringSession + ", BatteryDesignCapacityMah=" + this.mBatteryDesignCapacityMah + ", EnergyConsumedNwh=" + this.mEnergyConsumedNwh + ")";
        }
    }

    public static class SatelliteIncomingDatagramParams {
        private final int mCarrierId;
        private final int mDatagramSizeBytes;
        private final long mDatagramTransferTimeMillis;
        private final boolean mIsDemoMode;
        private final boolean mIsNtnOnlyCarrier;
        private final String mPlmn;
        private final int mResultCode;
        private final int mSessionConnectionMode;
        private final int mSupportedConnectionMode;

        private SatelliteIncomingDatagramParams(Builder builder) {
            this.mResultCode = builder.mResultCode;
            this.mDatagramSizeBytes = builder.mDatagramSizeBytes;
            this.mDatagramTransferTimeMillis = builder.mDatagramTransferTimeMillis;
            this.mIsDemoMode = builder.mIsDemoMode;
            this.mCarrierId = builder.mCarrierId;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
            this.mPlmn = builder.mPlmn;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getDatagramSizeBytes() {
            return this.mDatagramSizeBytes;
        }

        public long getDatagramTransferTimeMillis() {
            return this.mDatagramTransferTimeMillis;
        }

        public boolean getIsDemoMode() {
            return this.mIsDemoMode;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public static class Builder {
            private int mResultCode = -1;
            private int mDatagramSizeBytes = -1;
            private long mDatagramTransferTimeMillis = -1;
            private boolean mIsDemoMode = false;
            private int mCarrierId = -1;
            private boolean mIsNtnOnlyCarrier = false;
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;
            private String mPlmn = SatelliteConstants.DEFAULT_PLMN;

            public Builder setResultCode(int i) {
                this.mResultCode = i;
                return this;
            }

            public Builder setDatagramSizeBytes(int i) {
                this.mDatagramSizeBytes = i;
                return this;
            }

            public Builder setDatagramTransferTimeMillis(long j) {
                this.mDatagramTransferTimeMillis = j;
                return this;
            }

            public Builder setIsDemoMode(boolean z) {
                this.mIsDemoMode = z;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public Builder setPlmn(String str) {
                this.mPlmn = str;
                return this;
            }

            public SatelliteIncomingDatagramParams build() {
                return new SatelliteIncomingDatagramParams(this);
            }
        }

        public String toString() {
            return "IncomingDatagramParams(, resultCode=" + this.mResultCode + ", datagramSizeBytes=" + this.mDatagramSizeBytes + ", datagramTransferTimeMillis=" + this.mDatagramTransferTimeMillis + ", isDemoMode=" + this.mIsDemoMode + ", CarrierId=" + this.mCarrierId + ", isNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", sessionConnectionMode=" + this.mSessionConnectionMode + ", plmn=" + this.mPlmn + ")";
        }
    }

    public static class SatelliteOutgoingDatagramParams {
        private final int mCarrierId;
        private final int mDatagramSizeBytes;
        private final long mDatagramTransferTimeMillis;
        private final int mDatagramType;
        private final boolean mIsDemoMode;
        private final boolean mIsNtnOnlyCarrier;
        private final String mPlmn;
        private final int mResultCode;
        private final int mSessionConnectionMode;
        private final int mSupportedConnectionMode;

        private SatelliteOutgoingDatagramParams(Builder builder) {
            this.mDatagramType = builder.mDatagramType;
            this.mResultCode = builder.mResultCode;
            this.mDatagramSizeBytes = builder.mDatagramSizeBytes;
            this.mDatagramTransferTimeMillis = builder.mDatagramTransferTimeMillis;
            this.mIsDemoMode = builder.mIsDemoMode;
            this.mCarrierId = builder.mCarrierId;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
            this.mPlmn = builder.mPlmn;
        }

        public int getDatagramType() {
            return this.mDatagramType;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getDatagramSizeBytes() {
            return this.mDatagramSizeBytes;
        }

        public long getDatagramTransferTimeMillis() {
            return this.mDatagramTransferTimeMillis;
        }

        public boolean getIsDemoMode() {
            return this.mIsDemoMode;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public static class Builder {
            private int mDatagramType = -1;
            private int mResultCode = -1;
            private int mDatagramSizeBytes = -1;
            private long mDatagramTransferTimeMillis = -1;
            private boolean mIsDemoMode = false;
            private int mCarrierId = -1;
            private boolean mIsNtnOnlyCarrier = false;
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;
            private String mPlmn = SatelliteConstants.DEFAULT_PLMN;

            public Builder setDatagramType(int i) {
                this.mDatagramType = i;
                return this;
            }

            public Builder setResultCode(int i) {
                this.mResultCode = i;
                return this;
            }

            public Builder setDatagramSizeBytes(int i) {
                this.mDatagramSizeBytes = i;
                return this;
            }

            public Builder setDatagramTransferTimeMillis(long j) {
                this.mDatagramTransferTimeMillis = j;
                return this;
            }

            public Builder setIsDemoMode(boolean z) {
                this.mIsDemoMode = z;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public Builder setPlmn(String str) {
                this.mPlmn = str;
                return this;
            }

            public SatelliteOutgoingDatagramParams build() {
                return new SatelliteOutgoingDatagramParams(this);
            }
        }

        public String toString() {
            return "OutgoingDatagramParams(datagramType=" + this.mDatagramType + ", resultCode=" + this.mResultCode + ", datagramSizeBytes=" + this.mDatagramSizeBytes + ", datagramTransferTimeMillis=" + this.mDatagramTransferTimeMillis + ", isDemoMode=" + this.mIsDemoMode + ", CarrierId=" + this.mCarrierId + ", isNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", sessionConnectionMode=" + this.mSessionConnectionMode + ", plmn=" + this.mPlmn + ")";
        }
    }

    public static class SatelliteProvisionParams {
        private final int mCarrierId;
        private final boolean mIsCanceled;
        private final boolean mIsNtnOnlyCarrier;
        private final boolean mIsProvisionRequest;
        private final int mProvisioningTimeSec;
        private final int mResultCode;
        private final int mSupportedConnectionMode;

        private SatelliteProvisionParams(Builder builder) {
            this.mResultCode = builder.mResultCode;
            this.mProvisioningTimeSec = builder.mProvisioningTimeSec;
            this.mIsProvisionRequest = builder.mIsProvisionRequest;
            this.mIsCanceled = builder.mIsCanceled;
            this.mCarrierId = builder.mCarrierId;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getProvisioningTimeSec() {
            return this.mProvisioningTimeSec;
        }

        public boolean getIsProvisionRequest() {
            return this.mIsProvisionRequest;
        }

        public boolean getIsCanceled() {
            return this.mIsCanceled;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public static class Builder {
            private int mResultCode = -1;
            private int mProvisioningTimeSec = -1;
            private boolean mIsProvisionRequest = false;
            private boolean mIsCanceled = false;
            private int mCarrierId = -1;
            private boolean mIsNtnOnlyCarrier = false;
            private int mSupportedConnectionMode = 0;

            public Builder setResultCode(int i) {
                this.mResultCode = i;
                return this;
            }

            public Builder setProvisioningTimeSec(int i) {
                this.mProvisioningTimeSec = i;
                return this;
            }

            public Builder setIsProvisionRequest(boolean z) {
                this.mIsProvisionRequest = z;
                return this;
            }

            public Builder setIsCanceled(boolean z) {
                this.mIsCanceled = z;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public SatelliteProvisionParams build() {
                return new SatelliteProvisionParams(this);
            }
        }

        public String toString() {
            return "ProvisionParams(resultCode=" + this.mResultCode + ", provisioningTimeSec=" + this.mProvisioningTimeSec + ", isProvisionRequest=" + this.mIsProvisionRequest + ", isCanceled" + this.mIsCanceled + ", CarrierId=" + this.mCarrierId + ", isNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + "' supportedConnectionMode=" + this.mSupportedConnectionMode + ")";
        }
    }

    public static class SatelliteSosMessageRecommenderParams {
        private final int mCarrierId;
        private final int mCarrierRoamingSatelliteEmergencyMessagingProvider;
        private final int mCellularServiceState;
        private final int mCountOfTimerStarted;
        private final int mEmergencyNumberSourceUsedInHandoverIntent;
        private final boolean mIsDisplaySosMessageSent;
        private final boolean mIsImsRegistered;
        private final boolean mIsInCarrierRoamingNtnMode;
        private final boolean mIsMultiSim;
        private final boolean mIsNtnOnlyCarrier;
        private final boolean mIsSatelliteAllowedInCurrentLocation;
        private final boolean mIsWifiConnected;
        private final String mPlmn;
        private final int mRecommendingHandoverType;
        private final int mSessionConnectionMode;
        private final int mSupportedConnectionMode;

        private SatelliteSosMessageRecommenderParams(Builder builder) {
            this.mIsDisplaySosMessageSent = builder.mIsDisplaySosMessageSent;
            this.mCountOfTimerStarted = builder.mCountOfTimerStarted;
            this.mIsImsRegistered = builder.mIsImsRegistered;
            this.mCellularServiceState = builder.mCellularServiceState;
            this.mIsMultiSim = builder.mIsMultiSim;
            this.mRecommendingHandoverType = builder.mRecommendingHandoverType;
            this.mIsSatelliteAllowedInCurrentLocation = builder.mIsSatelliteAllowedInCurrentLocation;
            this.mIsWifiConnected = builder.mIsWifiConnected;
            this.mCarrierId = builder.mCarrierId;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
            this.mPlmn = builder.mPlmn;
            this.mIsInCarrierRoamingNtnMode = builder.mIsInCarrierRoamingNtnMode;
            this.mCarrierRoamingSatelliteEmergencyMessagingProvider = builder.mCarrierRoamingSatelliteEmergencyMessagingProvider;
            this.mEmergencyNumberSourceUsedInHandoverIntent = builder.mEmergencyNumberSourceUsedInHandoverIntent;
        }

        public boolean isDisplaySosMessageSent() {
            return this.mIsDisplaySosMessageSent;
        }

        public int getCountOfTimerStarted() {
            return this.mCountOfTimerStarted;
        }

        public boolean isImsRegistered() {
            return this.mIsImsRegistered;
        }

        public int getCellularServiceState() {
            return this.mCellularServiceState;
        }

        public boolean isMultiSim() {
            return this.mIsMultiSim;
        }

        public int getRecommendingHandoverType() {
            return this.mRecommendingHandoverType;
        }

        public boolean isSatelliteAllowedInCurrentLocation() {
            return this.mIsSatelliteAllowedInCurrentLocation;
        }

        public boolean isWifiConnected() {
            return this.mIsWifiConnected;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public boolean getIsInCarrierRoamingNtnMode() {
            return this.mIsInCarrierRoamingNtnMode;
        }

        public int getCarrierRoamingSatelliteEmergencyMessagingProvider() {
            return this.mCarrierRoamingSatelliteEmergencyMessagingProvider;
        }

        public int getEmergencyNumberSourceUsedInHandoverIntent() {
            return this.mEmergencyNumberSourceUsedInHandoverIntent;
        }

        public static class Builder {
            private boolean mIsDisplaySosMessageSent = false;
            private int mCountOfTimerStarted = -1;
            private boolean mIsImsRegistered = false;
            private int mCellularServiceState = -1;
            private boolean mIsMultiSim = false;
            private int mRecommendingHandoverType = -1;
            private boolean mIsSatelliteAllowedInCurrentLocation = false;
            private boolean mIsWifiConnected = false;
            private int mCarrierId = -1;
            private boolean mIsNtnOnlyCarrier = false;
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;
            private String mPlmn = SatelliteConstants.DEFAULT_PLMN;
            private boolean mIsInCarrierRoamingNtnMode = false;
            private int mCarrierRoamingSatelliteEmergencyMessagingProvider = 0;
            private int mEmergencyNumberSourceUsedInHandoverIntent = 0;

            public Builder setDisplaySosMessageSent(boolean z) {
                this.mIsDisplaySosMessageSent = z;
                return this;
            }

            public Builder setCountOfTimerStarted(int i) {
                this.mCountOfTimerStarted = i;
                return this;
            }

            public Builder setImsRegistered(boolean z) {
                this.mIsImsRegistered = z;
                return this;
            }

            public Builder setCellularServiceState(int i) {
                this.mCellularServiceState = i;
                return this;
            }

            public Builder setIsMultiSim(boolean z) {
                this.mIsMultiSim = z;
                return this;
            }

            public Builder setRecommendingHandoverType(int i) {
                this.mRecommendingHandoverType = i;
                return this;
            }

            public Builder setIsSatelliteAllowedInCurrentLocation(boolean z) {
                this.mIsSatelliteAllowedInCurrentLocation = z;
                return this;
            }

            public Builder setIsWifiConnected(boolean z) {
                this.mIsWifiConnected = z;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public Builder setPlmn(String str) {
                this.mPlmn = str;
                return this;
            }

            public Builder setIsInCarrierRoamingNtnMode(boolean z) {
                this.mIsInCarrierRoamingNtnMode = z;
                return this;
            }

            public Builder setCarrierRoamingSatelliteEmergencyMessagingProvider(int i) {
                this.mCarrierRoamingSatelliteEmergencyMessagingProvider = i;
                return this;
            }

            public Builder setEmergencyNumberSourceUsedInHandoverIntent(int i) {
                this.mEmergencyNumberSourceUsedInHandoverIntent = i;
                return this;
            }

            public SatelliteSosMessageRecommenderParams build() {
                return new SatelliteSosMessageRecommenderParams(this);
            }
        }

        public String toString() {
            return "SosMessageRecommenderParams(isDisplaySosMessageSent=" + this.mIsDisplaySosMessageSent + ", countOfTimerStarted=" + this.mCountOfTimerStarted + ", isImsRegistered=" + this.mIsImsRegistered + ", cellularServiceState=" + this.mCellularServiceState + ", isMultiSim=" + this.mIsMultiSim + ", recommendingHandoverType=" + this.mRecommendingHandoverType + ", isSatelliteAllowedInCurrentLocation=" + this.mIsSatelliteAllowedInCurrentLocation + ", isWifiConnected=" + this.mIsWifiConnected + ", carrierId=" + this.mCarrierId + ", isNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", sessionConnectionMode=" + this.mSessionConnectionMode + ", plmn=" + this.mPlmn + ", isInCarrierRoamingNtnMode=" + this.mIsInCarrierRoamingNtnMode + ", carrierRoamingSatelliteEmergencyMessagingProvider =" + this.mCarrierRoamingSatelliteEmergencyMessagingProvider + ", mEmergencyNumberSourceUsedInHandoverIntent =" + this.mEmergencyNumberSourceUsedInHandoverIntent + ")";
        }
    }

    public static class CarrierRoamingSatelliteSessionParams {
        private final int mAverageDownlinkBandwidthKbps;
        private final int mAverageUplinkBandwidthKbps;
        private final int mAvgDurationOfSatelliteConnectionSec;
        private final int mBatteryDesignCapacityMah;
        private final int mBatteryLevelDropPercent;
        private final int mCarrierId;
        private final int mCountOfDataConnections;
        private final int mCountOfDataDisconnections;
        private final int mCountOfDataStalls;
        private final int mCountOfEmergencyDialerButtonDisplayed;
        private final int mCountOfIncomingMms;
        private final int mCountOfIncomingSms;
        private final int mCountOfNonEmergencyDialerDialogDisplayed;
        private final int mCountOfOutgoingMms;
        private final int mCountOfOutgoingSms;
        private final int mCountOfSatelliteNotificationDisplayed;
        private final int mEligibilitySource;
        private final long mEnergyConsumedNwh;
        private final boolean mIsMultiSim;
        private final boolean mIsNbIotNtn;
        private final boolean mIsNtnRoamingInHomeCountry;
        private final boolean mIsWfcEnabled;
        private final boolean mIsWfcRegistered;
        private final boolean mIsWifiConnected;
        private final boolean mIsWifiEnabled;
        private final int[] mLastFailCauses;
        private final int mMaxDownlinkBandwidthKbps;
        private final int mMaxUplinkBandwidthKbps;
        private final int mMinDownlinkBandwidthKbps;
        private final int mMinUplinkBandwidthKbps;
        private final int mNumberOfSatelliteConnections;
        private final long[] mPerAppRxDataBytes;
        private final long[] mPerAppSatelliteDataConsumedBytes;
        private final long[] mPerAppTxDataBytes;
        private final String mPlmn;
        private final int mRsrpAvg;
        private final int mRsrpMedian;
        private final int mRssnrAvg;
        private final int mRssnrMedian;
        private final int mSatelliteConnectionGapAvgSec;
        private final int mSatelliteConnectionGapMaxSec;
        private final int mSatelliteConnectionGapMinSec;
        private final long mSatelliteDataConsumedBytes;
        private final String[] mSatelliteSupportedApps;
        private final int[] mSatelliteSupportedUids;
        private final int mScreenOnTimeSec;
        private final int mServiceDataPolicy;
        private final int mSessionConnectionMode;
        private final int mSupportedConnectionMode;
        private final int[] mSupportedSatelliteServices;
        private final long mTotalRxDataBytes;
        private final int mTotalSatelliteModeTimeSec;
        private final long mTotalTxDataBytes;
        private final boolean mWasChargingDuringSession;

        private CarrierRoamingSatelliteSessionParams(Builder builder) {
            this.mCarrierId = builder.mCarrierId;
            this.mIsNtnRoamingInHomeCountry = builder.mIsNtnRoamingInHomeCountry;
            this.mTotalSatelliteModeTimeSec = builder.mTotalSatelliteModeTimeSec;
            this.mNumberOfSatelliteConnections = builder.mNumberOfSatelliteConnections;
            this.mAvgDurationOfSatelliteConnectionSec = builder.mAvgDurationOfSatelliteConnectionSec;
            this.mSatelliteConnectionGapMinSec = builder.mSatelliteConnectionGapMinSec;
            this.mSatelliteConnectionGapAvgSec = builder.mSatelliteConnectionGapAvgSec;
            this.mSatelliteConnectionGapMaxSec = builder.mSatelliteConnectionGapMaxSec;
            this.mRsrpAvg = builder.mRsrpAvg;
            this.mRsrpMedian = builder.mRsrpMedian;
            this.mRssnrAvg = builder.mRssnrAvg;
            this.mRssnrMedian = builder.mRssnrMedian;
            this.mCountOfIncomingSms = builder.mCountOfIncomingSms;
            this.mCountOfOutgoingSms = builder.mCountOfOutgoingSms;
            this.mCountOfIncomingMms = builder.mCountOfIncomingMms;
            this.mCountOfOutgoingMms = builder.mCountOfOutgoingMms;
            this.mSupportedSatelliteServices = builder.mSupportedSatelliteServices;
            this.mServiceDataPolicy = builder.mServiceDataPolicy;
            this.mSatelliteDataConsumedBytes = builder.mSatelliteDataConsumedBytes;
            this.mTotalRxDataBytes = builder.mTotalRxDataBytes;
            this.mTotalTxDataBytes = builder.mTotalTxDataBytes;
            this.mIsMultiSim = builder.mIsMultiSim;
            this.mIsNbIotNtn = builder.mIsNbIotNtn;
            this.mCountOfDataConnections = builder.mCountOfDataConnections;
            this.mLastFailCauses = builder.mLastFailCauses;
            this.mCountOfDataDisconnections = builder.mCountOfDataDisconnections;
            this.mCountOfDataStalls = builder.mCountOfDataStalls;
            this.mAverageUplinkBandwidthKbps = builder.mAverageUplinkBandwidthKbps;
            this.mAverageDownlinkBandwidthKbps = builder.mAverageDownlinkBandwidthKbps;
            this.mMinUplinkBandwidthKbps = builder.mMinUplinkBandwidthKbps;
            this.mMaxUplinkBandwidthKbps = builder.mMaxUplinkBandwidthKbps;
            this.mMinDownlinkBandwidthKbps = builder.mMinDownlinkBandwidthKbps;
            this.mMaxDownlinkBandwidthKbps = builder.mMaxDownlinkBandwidthKbps;
            this.mSatelliteSupportedApps = builder.mSatelliteSupportedApps;
            this.mSatelliteSupportedUids = builder.mSatelliteSupportedUids;
            this.mPerAppSatelliteDataConsumedBytes = builder.mPerAppSatelliteDataConsumedBytes;
            this.mPerAppRxDataBytes = builder.mPerAppRxDataBytes;
            this.mPerAppTxDataBytes = builder.mPerAppTxDataBytes;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
            this.mPlmn = builder.mPlmn;
            this.mIsWifiEnabled = builder.mIsWifiEnabled;
            this.mIsWfcEnabled = builder.mIsWfcEnabled;
            this.mIsWfcRegistered = builder.mIsWfcRegistered;
            this.mScreenOnTimeSec = builder.mScreenOnTimeSec;
            this.mBatteryLevelDropPercent = builder.mBatteryLevelDropPercent;
            this.mWasChargingDuringSession = builder.mWasChargingDuringSession;
            this.mBatteryDesignCapacityMah = builder.mBatteryDesignCapacityMah;
            this.mEnergyConsumedNwh = builder.mEnergyConsumedNwh;
            this.mEligibilitySource = builder.mEligibilitySource;
            this.mIsWifiConnected = builder.mIsWifiConnected;
            this.mCountOfNonEmergencyDialerDialogDisplayed = builder.mCountOfNonEmergencyDialerDialogDisplayed;
            this.mCountOfEmergencyDialerButtonDisplayed = builder.mCountOfEmergencyDialerButtonDisplayed;
            this.mCountOfSatelliteNotificationDisplayed = builder.mCountOfSatelliteNotificationDisplayed;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean getIsNtnRoamingInHomeCountry() {
            return this.mIsNtnRoamingInHomeCountry;
        }

        public int getTotalSatelliteModeTimeSec() {
            return this.mTotalSatelliteModeTimeSec;
        }

        public int getNumberOfSatelliteConnections() {
            return this.mNumberOfSatelliteConnections;
        }

        public int getAvgDurationOfSatelliteConnectionSec() {
            return this.mAvgDurationOfSatelliteConnectionSec;
        }

        public int getSatelliteConnectionGapMinSec() {
            return this.mSatelliteConnectionGapMinSec;
        }

        public int getSatelliteConnectionGapAvgSec() {
            return this.mSatelliteConnectionGapAvgSec;
        }

        public int getSatelliteConnectionGapMaxSec() {
            return this.mSatelliteConnectionGapMaxSec;
        }

        public int getRsrpAvg() {
            return this.mRsrpAvg;
        }

        public int getRsrpMedian() {
            return this.mRsrpMedian;
        }

        public int getRssnrAvg() {
            return this.mRssnrAvg;
        }

        public int getRssnrMedian() {
            return this.mRssnrMedian;
        }

        public int getCountOfIncomingSms() {
            return this.mCountOfIncomingSms;
        }

        public int getCountOfOutgoingSms() {
            return this.mCountOfOutgoingSms;
        }

        public int getCountOfIncomingMms() {
            return this.mCountOfIncomingMms;
        }

        public int getCountOfOutgoingMms() {
            return this.mCountOfOutgoingMms;
        }

        public int[] getSupportedSatelliteServices() {
            return this.mSupportedSatelliteServices;
        }

        public int getServiceDataPolicy() {
            return this.mServiceDataPolicy;
        }

        public long getSatelliteDataConsumedBytes() {
            return this.mSatelliteDataConsumedBytes;
        }

        public long getTotalRxDataBytes() {
            return this.mTotalRxDataBytes;
        }

        public long getTotalTxDataBytes() {
            return this.mTotalTxDataBytes;
        }

        public boolean isMultiSim() {
            return this.mIsMultiSim;
        }

        public boolean isNbIotNtn() {
            return this.mIsNbIotNtn;
        }

        public int getCountOfDataConnections() {
            return this.mCountOfDataConnections;
        }

        public int[] getLastFailCauses() {
            return this.mLastFailCauses;
        }

        public int getCountOfDataDisconnections() {
            return this.mCountOfDataDisconnections;
        }

        public int getCountOfDataStalls() {
            return this.mCountOfDataStalls;
        }

        public int getAverageUplinkBandwidthKbps() {
            return this.mAverageUplinkBandwidthKbps;
        }

        public int getAverageDownlinkBandwidthKbps() {
            return this.mAverageDownlinkBandwidthKbps;
        }

        public int getMinimumUplinkBandwidthKbps() {
            return this.mMinUplinkBandwidthKbps;
        }

        public int getMaximumUplinkBandwidthKbps() {
            return this.mMaxUplinkBandwidthKbps;
        }

        public int getMinimumDownlinkBandwidthKbps() {
            return this.mMinDownlinkBandwidthKbps;
        }

        public int getMaximumDownlinkBandwidthKbps() {
            return this.mMaxDownlinkBandwidthKbps;
        }

        public String[] getSatelliteSupportedApps() {
            return this.mSatelliteSupportedApps;
        }

        public int[] getSatelliteSupportedUids() {
            return this.mSatelliteSupportedUids;
        }

        public long[] getPerAppSatelliteDataConsumedBytes() {
            return this.mPerAppSatelliteDataConsumedBytes;
        }

        public long[] getPerAppRxDataBytes() {
            return this.mPerAppRxDataBytes;
        }

        public long[] getPerAppTxDataBytes() {
            return this.mPerAppTxDataBytes;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public String getPlmn() {
            return this.mPlmn;
        }

        public boolean isWifiEnabled() {
            return this.mIsWifiEnabled;
        }

        public boolean isWfcEnabled() {
            return this.mIsWfcEnabled;
        }

        public boolean isWfcRegistered() {
            return this.mIsWfcRegistered;
        }

        public int getScreenOnTimeSec() {
            return this.mScreenOnTimeSec;
        }

        public int getBatteryLevelDropPercent() {
            return this.mBatteryLevelDropPercent;
        }

        public boolean wasChargingDuringSession() {
            return this.mWasChargingDuringSession;
        }

        public int getBatteryDesignCapacityMah() {
            return this.mBatteryDesignCapacityMah;
        }

        public long getEnergyConsumedNwh() {
            return this.mEnergyConsumedNwh;
        }

        public int getEligibilitySource() {
            return this.mEligibilitySource;
        }

        public boolean isWifiConnected() {
            return this.mIsWifiConnected;
        }

        public int getCountOfNonEmergencyDialerDialogDisplayed() {
            return this.mCountOfNonEmergencyDialerDialogDisplayed;
        }

        public int getCountOfEmergencyDialerButtonDisplayed() {
            return this.mCountOfEmergencyDialerButtonDisplayed;
        }

        public int getCountOfSatelliteNotificationDisplayed() {
            return this.mCountOfSatelliteNotificationDisplayed;
        }

        public static class Builder {
            private int mCarrierId = -1;
            private boolean mIsNtnRoamingInHomeCountry = false;
            private int mTotalSatelliteModeTimeSec = 0;
            private int mNumberOfSatelliteConnections = 0;
            private int mAvgDurationOfSatelliteConnectionSec = 0;
            private int mSatelliteConnectionGapMinSec = 0;
            private int mSatelliteConnectionGapAvgSec = 0;
            private int mSatelliteConnectionGapMaxSec = 0;
            private int mRsrpAvg = 0;
            private int mRsrpMedian = 0;
            private int mRssnrAvg = 0;
            private int mRssnrMedian = 0;
            private int mCountOfIncomingSms = 0;
            private int mCountOfOutgoingSms = 0;
            private int mCountOfIncomingMms = 0;
            private int mCountOfOutgoingMms = 0;
            private int[] mSupportedSatelliteServices = new int[0];
            int mServiceDataPolicy = 0;
            long mSatelliteDataConsumedBytes = 0;
            private boolean mIsMultiSim = false;
            private boolean mIsNbIotNtn = false;
            private int mCountOfDataConnections = 0;
            private int[] mLastFailCauses = new int[5];
            private int mCountOfDataDisconnections = 0;
            private int mCountOfDataStalls = 0;
            private int mAverageUplinkBandwidthKbps = 0;
            private int mAverageDownlinkBandwidthKbps = 0;
            private int mMinUplinkBandwidthKbps = Integer.MAX_VALUE;
            private int mMaxUplinkBandwidthKbps = 0;
            private int mMinDownlinkBandwidthKbps = Integer.MAX_VALUE;
            private int mMaxDownlinkBandwidthKbps = 0;
            private String[] mSatelliteSupportedApps = null;
            private int[] mSatelliteSupportedUids = new int[5];
            private long[] mPerAppSatelliteDataConsumedBytes = {0};
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;
            private String mPlmn = SatelliteConstants.DEFAULT_PLMN;
            private boolean mIsWifiEnabled = false;
            private boolean mIsWfcEnabled = false;
            private boolean mIsWfcRegistered = false;
            private int mScreenOnTimeSec = 0;
            private int mBatteryLevelDropPercent = 0;
            private boolean mWasChargingDuringSession = false;
            private int mBatteryDesignCapacityMah = 0;
            private long mEnergyConsumedNwh = 0;
            private int mEligibilitySource = 0;
            private boolean mIsWifiConnected = false;
            private int mCountOfNonEmergencyDialerDialogDisplayed = 0;
            private int mCountOfEmergencyDialerButtonDisplayed = 0;
            private int mCountOfSatelliteNotificationDisplayed = 0;
            private long mTotalRxDataBytes = 0;
            private long mTotalTxDataBytes = 0;
            private long[] mPerAppRxDataBytes = {0};
            private long[] mPerAppTxDataBytes = {0};

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsNtnRoamingInHomeCountry(boolean z) {
                this.mIsNtnRoamingInHomeCountry = z;
                return this;
            }

            public Builder setTotalSatelliteModeTimeSec(int i) {
                this.mTotalSatelliteModeTimeSec = i;
                return this;
            }

            public Builder setNumberOfSatelliteConnections(int i) {
                this.mNumberOfSatelliteConnections = i;
                return this;
            }

            public Builder setAvgDurationOfSatelliteConnectionSec(int i) {
                this.mAvgDurationOfSatelliteConnectionSec = i;
                return this;
            }

            public Builder setSatelliteConnectionGapMinSec(int i) {
                this.mSatelliteConnectionGapMinSec = i;
                return this;
            }

            public Builder setSatelliteConnectionGapAvgSec(int i) {
                this.mSatelliteConnectionGapAvgSec = i;
                return this;
            }

            public Builder setSatelliteConnectionGapMaxSec(int i) {
                this.mSatelliteConnectionGapMaxSec = i;
                return this;
            }

            public Builder setRsrpAvg(int i) {
                this.mRsrpAvg = i;
                return this;
            }

            public Builder setRsrpMedian(int i) {
                this.mRsrpMedian = i;
                return this;
            }

            public Builder setRssnrAvg(int i) {
                this.mRssnrAvg = i;
                return this;
            }

            public Builder setRssnrMedian(int i) {
                this.mRssnrMedian = i;
                return this;
            }

            public Builder setCountOfIncomingSms(int i) {
                this.mCountOfIncomingSms = i;
                return this;
            }

            public Builder setCountOfOutgoingSms(int i) {
                this.mCountOfOutgoingSms = i;
                return this;
            }

            public Builder setCountOfIncomingMms(int i) {
                this.mCountOfIncomingMms = i;
                return this;
            }

            public Builder setCountOfOutgoingMms(int i) {
                this.mCountOfOutgoingMms = i;
                return this;
            }

            public Builder setSupportedSatelliteServices(int[] iArr) {
                this.mSupportedSatelliteServices = iArr;
                Arrays.sort(iArr);
                return this;
            }

            public Builder setServiceDataPolicy(int i) {
                this.mServiceDataPolicy = i;
                return this;
            }

            public Builder setSatelliteDataConsumedBytes(long j) {
                this.mSatelliteDataConsumedBytes = j;
                return this;
            }

            public Builder setTotalRxDataBytes(long j) {
                this.mTotalRxDataBytes = j;
                return this;
            }

            public Builder setTotalTxDataBytes(long j) {
                this.mTotalTxDataBytes = j;
                return this;
            }

            public Builder setIsMultiSim(boolean z) {
                this.mIsMultiSim = z;
                return this;
            }

            public Builder setIsNbIotNtn(boolean z) {
                this.mIsNbIotNtn = z;
                return this;
            }

            public Builder setCountOfDataConnections(int i) {
                this.mCountOfDataConnections = i;
                return this;
            }

            public Builder setLastFailCauses(int[] iArr) {
                this.mLastFailCauses = iArr;
                Arrays.sort(iArr);
                return this;
            }

            public Builder setCountOfDataDisconnections(int i) {
                this.mCountOfDataDisconnections = i;
                return this;
            }

            public Builder setCountOfDataStalls(int i) {
                this.mCountOfDataStalls = i;
                return this;
            }

            public Builder setAverageUplinkBandwidthKbps(int i) {
                this.mAverageUplinkBandwidthKbps = i;
                return this;
            }

            public Builder setAverageDownlinkBandwidthKbps(int i) {
                this.mAverageDownlinkBandwidthKbps = i;
                return this;
            }

            public Builder setMinimumUplinkBandwidthKbps(int i) {
                this.mMinUplinkBandwidthKbps = i;
                return this;
            }

            public Builder setMaximumUplinkBandwidthKbps(int i) {
                this.mMaxUplinkBandwidthKbps = i;
                return this;
            }

            public Builder setMinimumDownlinkBandwidthKbps(int i) {
                this.mMinDownlinkBandwidthKbps = i;
                return this;
            }

            public Builder setMaximumDownlinkBandwidthKbps(int i) {
                this.mMaxDownlinkBandwidthKbps = i;
                return this;
            }

            public Builder setSatelliteSupportedApps(String[] strArr) {
                this.mSatelliteSupportedApps = strArr;
                return this;
            }

            public Builder setSatelliteSupportedUids(int[] iArr) {
                this.mSatelliteSupportedUids = iArr;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public Builder setPlmn(String str) {
                this.mPlmn = str;
                return this;
            }

            public Builder setPerAppSatelliteDataConsumedBytes(long[] jArr) {
                this.mPerAppSatelliteDataConsumedBytes = jArr;
                return this;
            }

            public Builder setPerAppRxDataBytes(long[] jArr) {
                this.mPerAppRxDataBytes = jArr;
                return this;
            }

            public Builder setPerAppTxDataBytes(long[] jArr) {
                this.mPerAppTxDataBytes = jArr;
                return this;
            }

            public Builder setIsWifiEnabled(boolean z) {
                this.mIsWifiEnabled = z;
                return this;
            }

            public Builder setIsWfcEnabled(boolean z) {
                this.mIsWfcEnabled = z;
                return this;
            }

            public Builder setIsWfcRegistered(boolean z) {
                this.mIsWfcRegistered = z;
                return this;
            }

            public Builder setScreenOnTimeSec(int i) {
                this.mScreenOnTimeSec = i;
                return this;
            }

            public Builder setBatteryLevelDropPercent(int i) {
                this.mBatteryLevelDropPercent = i;
                return this;
            }

            public Builder setWasChargingDuringSession(boolean z) {
                this.mWasChargingDuringSession = z;
                return this;
            }

            public Builder setBatteryDesignCapacityMah(int i) {
                this.mBatteryDesignCapacityMah = i;
                return this;
            }

            public Builder setEnergyConsumedNwh(long j) {
                this.mEnergyConsumedNwh = j;
                return this;
            }

            public Builder setEligibilitySource(int i) {
                this.mEligibilitySource = i;
                return this;
            }

            public Builder setIsWifiConnected(boolean z) {
                this.mIsWifiConnected = z;
                return this;
            }

            public Builder setCountOfNonEmergencyDialerDialogDisplayed(int i) {
                this.mCountOfNonEmergencyDialerDialogDisplayed = i;
                return this;
            }

            public Builder setCountOfEmergencyDialerButtonDisplayed(int i) {
                this.mCountOfEmergencyDialerButtonDisplayed = i;
                return this;
            }

            public Builder setCountOfSatelliteNotificationDisplayed(int i) {
                this.mCountOfSatelliteNotificationDisplayed = i;
                return this;
            }

            public CarrierRoamingSatelliteSessionParams build() {
                return new CarrierRoamingSatelliteSessionParams(this);
            }
        }

        public String toString() {
            return "CarrierRoamingSatelliteSessionParams(carrierId=" + this.mCarrierId + ", isNtnRoamingInHomeCountry=" + this.mIsNtnRoamingInHomeCountry + ", totalSatelliteModeTimeSec=" + this.mTotalSatelliteModeTimeSec + ", numberOfSatelliteConnections=" + this.mNumberOfSatelliteConnections + ", avgDurationOfSatelliteConnectionSec=" + this.mAvgDurationOfSatelliteConnectionSec + ", satelliteConnectionGapMinSec=" + this.mSatelliteConnectionGapMinSec + ", satelliteConnectionGapAvgSec=" + this.mSatelliteConnectionGapAvgSec + ", satelliteConnectionGapMaxSec=" + this.mSatelliteConnectionGapMaxSec + ", rsrpAvg=" + this.mRsrpAvg + ", rsrpMedian=" + this.mRsrpMedian + ", rssnrAvg=" + this.mRssnrAvg + ", rssnrMedian=" + this.mRssnrMedian + ", countOfIncomingSms=" + this.mCountOfIncomingSms + ", countOfOutgoingSms=" + this.mCountOfOutgoingSms + ", countOfIncomingMms=" + this.mCountOfIncomingMms + ", countOfOutgoingMms=" + this.mCountOfOutgoingMms + ", supportedSatelliteServices=" + Arrays.toString(this.mSupportedSatelliteServices) + ", serviceDataPolicy=" + this.mServiceDataPolicy + ", SatelliteDataConsumedBytes=" + this.mSatelliteDataConsumedBytes + ", TotalRxDataBytes=" + this.mTotalRxDataBytes + ", TotalTxDataBytes=" + this.mTotalTxDataBytes + ", isMultiSim=" + this.mIsMultiSim + ", isNbIotNtn=" + this.mIsNbIotNtn + ", countOfDataConnections=" + this.mCountOfDataConnections + ", lastFailCauses=" + Arrays.toString(this.mLastFailCauses) + ", countOfDataDisconnections=" + this.mCountOfDataDisconnections + ", countOfDataStalls=" + this.mCountOfDataStalls + ", averageUplinkBandwidthKbps=" + this.mAverageUplinkBandwidthKbps + ", averageDownlinkBandwidthKbps=" + this.mAverageDownlinkBandwidthKbps + ", minUplinkBandwidthKbps=" + this.mMinUplinkBandwidthKbps + ", maxUplinkBandwidthKbps=" + this.mMaxUplinkBandwidthKbps + ", minDownlinkBandwidthKbps=" + this.mMinDownlinkBandwidthKbps + ", maxDownlinkBandwidthKbps=" + this.mMaxDownlinkBandwidthKbps + ", satelliteSupportedApps=" + Arrays.toString(this.mSatelliteSupportedApps) + ", satelliteSupportedUids=" + Arrays.toString(this.mSatelliteSupportedUids) + ", perAppSatelliteDataConsumedBytes=" + Arrays.toString(this.mPerAppSatelliteDataConsumedBytes) + ", perAppRxDataBytes=" + Arrays.toString(this.mPerAppRxDataBytes) + ", perAppTxDataBytes=" + Arrays.toString(this.mPerAppTxDataBytes) + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", sessionConnectionMode=" + this.mSessionConnectionMode + ", plmn=" + this.mPlmn + ", mIsWifiEnabled=" + this.mIsWifiEnabled + ", mIsWfcEnabled=" + this.mIsWfcEnabled + ", mIsWfcRegistered=" + this.mIsWfcRegistered + ", ScreenOntimeSec=" + this.mScreenOnTimeSec + ", BatteryLevelDropPercent=" + this.mBatteryLevelDropPercent + ", WasChargingDuringSession=" + this.mWasChargingDuringSession + ", BatteryDesignCapacityMah=" + this.mBatteryDesignCapacityMah + ", EnergyConsumedNwh=" + this.mEnergyConsumedNwh + ", eligibilitySource=" + this.mEligibilitySource + ", isWifiConnected=" + this.mIsWifiConnected + ", countOfNonEmergencyDialerDialogDisplayed=" + this.mCountOfNonEmergencyDialerDialogDisplayed + ", countOfEmergencyDialerButtonDisplayed=" + this.mCountOfEmergencyDialerButtonDisplayed + ", countOfSatelliteNotificationDisplayed=" + this.mCountOfSatelliteNotificationDisplayed + ")";
        }
    }

    public static class CarrierRoamingSatelliteControllerStatsParams {
        private final int mCarrierId;
        private final int mConfigDataSource;
        private final int mCountOfEntitlementStatusQueryRequest;
        private final int mCountOfSatelliteConfigUpdateRequest;
        private final int mCountOfSatelliteNotificationDisplayed;
        private final int mCountOfSatelliteSessions;
        private final int mCountOfSessionConnectionModeAutomatic;
        private final int mCountOfSessionConnectionModeManual;
        private final int mEligibilitySource;
        private final boolean mIsDeviceEntitled;
        private final boolean mIsMultiSim;
        private final boolean mIsNbIotNtn;
        private final boolean mIsSatelliteAttachSupported;
        private final int mSatelliteSessionGapAvgSec;
        private final int mSatelliteSessionGapMaxSec;
        private final int mSatelliteSessionGapMinSec;
        private final int mServiceDataPolicy;
        private final int mSessionDurationSec;
        private final int mSupportedConnectionMode;

        private CarrierRoamingSatelliteControllerStatsParams(Builder builder) {
            this.mConfigDataSource = builder.mConfigDataSource;
            this.mCountOfEntitlementStatusQueryRequest = builder.mCountOfEntitlementStatusQueryRequest;
            this.mCountOfSatelliteConfigUpdateRequest = builder.mCountOfSatelliteConfigUpdateRequest;
            this.mCountOfSatelliteNotificationDisplayed = builder.mCountOfSatelliteNotificationDisplayed;
            this.mCountOfSessionConnectionModeAutomatic = builder.mCountOfSessionConnectionModeAutomatic;
            this.mCountOfSessionConnectionModeManual = builder.mCountOfSessionConnectionModeManual;
            this.mSatelliteSessionGapMinSec = ((Integer) builder.mSatelliteSessionGapMinSec.orElse(-1)).intValue();
            this.mSatelliteSessionGapAvgSec = ((Integer) builder.mSatelliteSessionGapAvgSec.orElse(-1)).intValue();
            this.mSatelliteSessionGapMaxSec = ((Integer) builder.mSatelliteSessionGapMaxSec.orElse(-1)).intValue();
            this.mCarrierId = builder.mCarrierId;
            this.mIsDeviceEntitled = builder.mIsDeviceEntitled;
            this.mIsMultiSim = builder.mIsMultiSim;
            this.mCountOfSatelliteSessions = builder.mCountOfSatelliteSessions;
            this.mIsNbIotNtn = builder.mIsNbIotNtn;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mServiceDataPolicy = builder.mServiceDataPolicy;
            this.mSessionDurationSec = builder.mSessionDurationSec;
            this.mIsSatelliteAttachSupported = builder.mIsSatelliteAttachSupported;
            this.mEligibilitySource = builder.mEligibilitySource;
        }

        public int getConfigDataSource() {
            return this.mConfigDataSource;
        }

        public int getCountOfEntitlementStatusQueryRequest() {
            return this.mCountOfEntitlementStatusQueryRequest;
        }

        public int getCountOfSatelliteConfigUpdateRequest() {
            return this.mCountOfSatelliteConfigUpdateRequest;
        }

        public int getCountOfSatelliteNotificationDisplayed() {
            return this.mCountOfSatelliteNotificationDisplayed;
        }

        public int getSatelliteSessionGapMinSec() {
            return this.mSatelliteSessionGapMinSec;
        }

        public int getSatelliteSessionGapAvgSec() {
            return this.mSatelliteSessionGapAvgSec;
        }

        public int getSatelliteSessionGapMaxSec() {
            return this.mSatelliteSessionGapMaxSec;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public boolean isDeviceEntitled() {
            return this.mIsDeviceEntitled;
        }

        public boolean isMultiSim() {
            return this.mIsMultiSim;
        }

        public int getCountOfSatelliteSessions() {
            return this.mCountOfSatelliteSessions;
        }

        public boolean isNbIotNtn() {
            return this.mIsNbIotNtn;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getCountOfSessionConnectionModeAutomatic() {
            return this.mCountOfSessionConnectionModeAutomatic;
        }

        public int getCountOfSessionConnectionModeManual() {
            return this.mCountOfSessionConnectionModeManual;
        }

        public int getServiceDataPolicy() {
            return this.mServiceDataPolicy;
        }

        public int getSessionDurationSec() {
            return this.mSessionDurationSec;
        }

        public boolean isSatelliteAttachSupported() {
            return this.mIsSatelliteAttachSupported;
        }

        public int getEligibilitySource() {
            return this.mEligibilitySource;
        }

        public static class Builder {
            private int mCarrierId;
            private boolean mIsDeviceEntitled;
            private boolean mIsMultiSim;
            private boolean mIsNbIotNtn;
            private boolean mIsSatelliteAttachSupported;
            private int mConfigDataSource = 0;
            private int mCountOfEntitlementStatusQueryRequest = 0;
            private int mCountOfSatelliteConfigUpdateRequest = 0;
            private int mCountOfSatelliteNotificationDisplayed = 0;
            private Optional mSatelliteSessionGapMinSec = Optional.empty();
            private Optional mSatelliteSessionGapAvgSec = Optional.empty();
            private Optional mSatelliteSessionGapMaxSec = Optional.empty();
            private int mCountOfSatelliteSessions = 0;
            private int mSupportedConnectionMode = 0;
            private int mCountOfSessionConnectionModeAutomatic = 0;
            private int mCountOfSessionConnectionModeManual = 0;
            private int mServiceDataPolicy = 0;
            private int mSessionDurationSec = 0;
            private int mEligibilitySource = 0;

            public Builder setConfigDataSource(int i) {
                this.mConfigDataSource = i;
                return this;
            }

            public Builder setCountOfEntitlementStatusQueryRequest(int i) {
                this.mCountOfEntitlementStatusQueryRequest = i;
                return this;
            }

            public Builder setCountOfSatelliteConfigUpdateRequest(int i) {
                this.mCountOfSatelliteConfigUpdateRequest = i;
                return this;
            }

            public Builder setCountOfSatelliteNotificationDisplayed(int i) {
                this.mCountOfSatelliteNotificationDisplayed = i;
                return this;
            }

            public Builder setSatelliteSessionGapMinSec(int i) {
                this.mSatelliteSessionGapMinSec = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setSatelliteSessionGapAvgSec(int i) {
                this.mSatelliteSessionGapAvgSec = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setSatelliteSessionGapMaxSec(int i) {
                this.mSatelliteSessionGapMaxSec = Optional.of(Integer.valueOf(i));
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setIsDeviceEntitled(boolean z) {
                this.mIsDeviceEntitled = z;
                return this;
            }

            public Builder setIsMultiSim(boolean z) {
                this.mIsMultiSim = z;
                return this;
            }

            public Builder increaseCountOfSatelliteSessions() {
                this.mCountOfSatelliteSessions++;
                return this;
            }

            public Builder increaseCountOfSessionConnectionModeAutomatic(boolean z) {
                if (!z) {
                    return this;
                }
                this.mCountOfSessionConnectionModeAutomatic++;
                return this;
            }

            public Builder increaseCountOfSessionConnectionModeManual(boolean z) {
                if (!z) {
                    return this;
                }
                this.mCountOfSessionConnectionModeManual++;
                return this;
            }

            public Builder setIsNbIotNtn(boolean z) {
                this.mIsNbIotNtn = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionDurationSec(int i) {
                this.mSessionDurationSec = i;
                return this;
            }

            public Builder setServiceDataPolicy(int i) {
                this.mServiceDataPolicy = i;
                return this;
            }

            public Builder setSatelliteAttachSupported(boolean z) {
                this.mIsSatelliteAttachSupported = z;
                return this;
            }

            public Builder setEligibilitySource(int i) {
                this.mEligibilitySource = i;
                return this;
            }

            public CarrierRoamingSatelliteControllerStatsParams build() {
                return new CarrierRoamingSatelliteControllerStatsParams(this);
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                CarrierRoamingSatelliteControllerStatsParams carrierRoamingSatelliteControllerStatsParams = (CarrierRoamingSatelliteControllerStatsParams) obj;
                if (this.mConfigDataSource == carrierRoamingSatelliteControllerStatsParams.getConfigDataSource() && this.mCountOfEntitlementStatusQueryRequest == carrierRoamingSatelliteControllerStatsParams.getCountOfEntitlementStatusQueryRequest() && this.mCountOfSatelliteConfigUpdateRequest == carrierRoamingSatelliteControllerStatsParams.getCountOfSatelliteConfigUpdateRequest() && this.mCountOfSatelliteNotificationDisplayed == carrierRoamingSatelliteControllerStatsParams.getCountOfSatelliteNotificationDisplayed() && this.mSatelliteSessionGapMinSec == carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapMinSec() && this.mSatelliteSessionGapAvgSec == carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapAvgSec() && this.mSatelliteSessionGapMaxSec == carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapMaxSec() && this.mCarrierId == carrierRoamingSatelliteControllerStatsParams.getCarrierId() && this.mIsDeviceEntitled == carrierRoamingSatelliteControllerStatsParams.isDeviceEntitled() && this.mIsMultiSim == carrierRoamingSatelliteControllerStatsParams.isMultiSim() && this.mCountOfSatelliteSessions == carrierRoamingSatelliteControllerStatsParams.getCountOfSatelliteSessions() && this.mIsNbIotNtn == carrierRoamingSatelliteControllerStatsParams.isNbIotNtn() && this.mSupportedConnectionMode == carrierRoamingSatelliteControllerStatsParams.getSupportedConnectionMode() && this.mCountOfSessionConnectionModeAutomatic == carrierRoamingSatelliteControllerStatsParams.getCountOfSessionConnectionModeAutomatic() && this.mCountOfSessionConnectionModeManual == carrierRoamingSatelliteControllerStatsParams.getCountOfSessionConnectionModeManual() && this.mServiceDataPolicy == carrierRoamingSatelliteControllerStatsParams.getServiceDataPolicy() && this.mSessionDurationSec == carrierRoamingSatelliteControllerStatsParams.getSessionDurationSec() && this.mIsSatelliteAttachSupported == carrierRoamingSatelliteControllerStatsParams.isSatelliteAttachSupported() && this.mEligibilitySource == carrierRoamingSatelliteControllerStatsParams.getEligibilitySource()) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mConfigDataSource), Integer.valueOf(this.mCountOfEntitlementStatusQueryRequest), Integer.valueOf(this.mCountOfSatelliteConfigUpdateRequest), Integer.valueOf(this.mCountOfSatelliteNotificationDisplayed), Integer.valueOf(this.mSatelliteSessionGapMinSec), Integer.valueOf(this.mSatelliteSessionGapAvgSec), Integer.valueOf(this.mSatelliteSessionGapMaxSec), Integer.valueOf(this.mCarrierId), Boolean.valueOf(this.mIsDeviceEntitled), Boolean.valueOf(this.mIsMultiSim), Integer.valueOf(this.mCountOfSatelliteSessions), Boolean.valueOf(this.mIsNbIotNtn), Integer.valueOf(this.mSupportedConnectionMode), Integer.valueOf(this.mCountOfSessionConnectionModeAutomatic), Integer.valueOf(this.mCountOfSessionConnectionModeManual), Integer.valueOf(this.mSessionDurationSec), Boolean.valueOf(this.mIsSatelliteAttachSupported), Integer.valueOf(this.mEligibilitySource));
        }

        public String toString() {
            return "CarrierRoamingSatelliteControllerStatsParams(configDataSource=" + this.mConfigDataSource + ", countOfEntitlementStatusQueryRequest=" + this.mCountOfEntitlementStatusQueryRequest + ", countOfSatelliteConfigUpdateRequest=" + this.mCountOfSatelliteConfigUpdateRequest + ", countOfSatelliteNotificationDisplayed=" + this.mCountOfSatelliteNotificationDisplayed + ", satelliteSessionGapMinSec=" + this.mSatelliteSessionGapMinSec + ", satelliteSessionGapAvgSec=" + this.mSatelliteSessionGapAvgSec + ", satelliteSessionGapMaxSec=" + this.mSatelliteSessionGapMaxSec + ", carrierId=" + this.mCarrierId + ", isDeviceEntitled=" + this.mIsDeviceEntitled + ", isMultiSim=" + this.mIsMultiSim + ", countOfSatelliteSession=" + this.mCountOfSatelliteSessions + ", isNbIotNtn=" + this.mIsNbIotNtn + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", countOfSessionConnectionModeAutomatic=" + this.mCountOfSessionConnectionModeAutomatic + ", countOfSessionConnectionModeManual=" + this.mCountOfSessionConnectionModeManual + ", serviceDataPolicy=" + this.mServiceDataPolicy + ", totalSessionDurationSec" + this.mSessionDurationSec + ", satelliteAttachSupported=" + this.mIsSatelliteAttachSupported + ", eligibilitySource=" + this.mEligibilitySource + ")";
        }
    }

    public static class SatelliteEntitlementParams {
        private final int mCarrierId;
        private final int mCount;
        private final int mEntitlementDataPolicy;
        private final int[] mEntitlementServiceType;
        private final int mEntitlementStatus;
        private final int mHttpStatusCode;
        private final boolean mIsAllowedServiceEntitlement;
        private final boolean mIsRetry;
        private final int mResult;
        private final int mSupportedConnectionMode;
        private final int mTriggerEvent;

        private SatelliteEntitlementParams(Builder builder) {
            this.mCarrierId = builder.mCarrierId;
            this.mResult = builder.mResult;
            this.mEntitlementStatus = builder.mEntitlementStatus;
            this.mIsRetry = builder.mIsRetry;
            this.mCount = builder.mCount;
            this.mIsAllowedServiceEntitlement = builder.mIsAllowedServiceEntitlement;
            this.mEntitlementServiceType = builder.mEntitlementServiceType;
            this.mEntitlementDataPolicy = builder.mEntitlementDataPolicy;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mHttpStatusCode = builder.mHttpStatusCode;
            this.mTriggerEvent = builder.mTriggerEvent;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public int getResult() {
            return this.mResult;
        }

        public int getEntitlementStatus() {
            return this.mEntitlementStatus;
        }

        public boolean getIsRetry() {
            return this.mIsRetry;
        }

        public int getCount() {
            return this.mCount;
        }

        public boolean getIsAllowedServiceEntitlement() {
            return this.mIsAllowedServiceEntitlement;
        }

        public int[] getEntitlementServiceType() {
            return this.mEntitlementServiceType;
        }

        public int getEntitlementDataPolicy() {
            return this.mEntitlementDataPolicy;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getHttpStatusCode() {
            return this.mHttpStatusCode;
        }

        public int getTriggerEvent() {
            return this.mTriggerEvent;
        }

        public static class Builder {
            private int mCarrierId = -1;
            private int mResult = -1;
            private int mEntitlementStatus = -1;
            private boolean mIsRetry = false;
            private int mCount = -1;
            private boolean mIsAllowedServiceEntitlement = false;
            private int[] mEntitlementServiceType = new int[0];
            private int mEntitlementDataPolicy = 0;
            private int mSupportedConnectionMode = 0;
            private int mHttpStatusCode = 0;
            private int mTriggerEvent = 0;

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setResult(int i) {
                this.mResult = i;
                return this;
            }

            public Builder setEntitlementStatus(int i) {
                this.mEntitlementStatus = i;
                return this;
            }

            public Builder setIsRetry(boolean z) {
                this.mIsRetry = z;
                return this;
            }

            public Builder setCount(int i) {
                this.mCount = i;
                return this;
            }

            public Builder setIsAllowedServiceEntitlement(boolean z) {
                this.mIsAllowedServiceEntitlement = z;
                return this;
            }

            public Builder setEntitlementServiceType(int[] iArr) {
                this.mEntitlementServiceType = iArr;
                Arrays.sort(iArr);
                return this;
            }

            public Builder setEntitlementDataPolicy(int i) {
                this.mEntitlementDataPolicy = i;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setHttpStatusCode(int i) {
                this.mHttpStatusCode = i;
                return this;
            }

            public Builder setTriggerEvent(int i) {
                this.mTriggerEvent = i;
                return this;
            }

            public SatelliteEntitlementParams build() {
                return new SatelliteEntitlementParams(this);
            }
        }

        public String toString() {
            return "SatelliteEntitlementParams(carrierId=" + this.mCarrierId + ", result=" + this.mResult + ", entitlementStatus=" + this.mEntitlementStatus + ", isRetry=" + this.mIsRetry + ", count=" + this.mCount + ", isAllowedServiceEntitlement=" + this.mIsAllowedServiceEntitlement + ", entitlementServiceType=" + Arrays.toString(this.mEntitlementServiceType) + ", entitlementServicePolicy=" + this.mEntitlementDataPolicy + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", HttpStatusCode=" + this.mHttpStatusCode + ", TriggerEvent=" + this.mTriggerEvent + ")";
        }
    }

    public static class SatelliteConfigUpdaterParams {
        private final int mCarrierConfigResult;
        private final int mConfigVersion;
        private final int mCount;
        private final int mOemConfigResult;

        private SatelliteConfigUpdaterParams(Builder builder) {
            this.mConfigVersion = builder.mConfigVersion;
            this.mOemConfigResult = builder.mOemConfigResult;
            this.mCarrierConfigResult = builder.mCarrierConfigResult;
            this.mCount = builder.mCount;
        }

        public int getConfigVersion() {
            return this.mConfigVersion;
        }

        public int getOemConfigResult() {
            return this.mOemConfigResult;
        }

        public int getCarrierConfigResult() {
            return this.mCarrierConfigResult;
        }

        public int getCount() {
            return this.mCount;
        }

        public static class Builder {
            private int mConfigVersion = -1;
            private int mOemConfigResult = -1;
            private int mCarrierConfigResult = -1;
            private int mCount = -1;

            public Builder setConfigVersion(int i) {
                this.mConfigVersion = i;
                return this;
            }

            public Builder setOemConfigResult(int i) {
                this.mOemConfigResult = i;
                return this;
            }

            public Builder setCarrierConfigResult(int i) {
                this.mCarrierConfigResult = i;
                return this;
            }

            public Builder setCount(int i) {
                this.mCount = i;
                return this;
            }

            public SatelliteConfigUpdaterParams build() {
                return new SatelliteConfigUpdaterParams(this);
            }
        }

        public String toString() {
            return "SatelliteConfigUpdaterParams(configVersion=" + this.mConfigVersion + ", oemConfigResult=" + this.mOemConfigResult + ", carrierConfigResult=" + this.mCarrierConfigResult + ", count=" + this.mCount + ")";
        }
    }

    public static class SatelliteAccessControllerParams {
        private final int mAccessControlType;
        private final int mCarrierId;
        private final int mConfigDataSource;
        private final String[] mCountryCodes;
        private final boolean mIsAllowed;
        private final boolean mIsEmergency;
        private final boolean mIsNtnOnlyCarrier;
        private final long mLocationQueryTimeMillis;
        private final long mOnDeviceLookupTimeMillis;
        private final int mResultCode;
        private final int mSessionConnectionMode;
        private final int mSupportedConnectionMode;
        private final long mTotalCheckingTimeMillis;
        private final int mTriggeringEvent;

        private SatelliteAccessControllerParams(Builder builder) {
            this.mAccessControlType = builder.mAccessControlType;
            this.mLocationQueryTimeMillis = builder.mLocationQueryTimeMillis;
            this.mOnDeviceLookupTimeMillis = builder.mOnDeviceLookupTimeMillis;
            this.mTotalCheckingTimeMillis = builder.mTotalCheckingTimeMillis;
            this.mIsAllowed = builder.mIsAllowed;
            this.mIsEmergency = builder.mIsEmergency;
            this.mResultCode = builder.mResultCode;
            this.mCountryCodes = builder.mCountryCodes;
            this.mConfigDataSource = builder.mConfigDataSource;
            this.mCarrierId = builder.mCarrierId;
            this.mTriggeringEvent = builder.mTriggeringEvent;
            this.mIsNtnOnlyCarrier = builder.mIsNtnOnlyCarrier;
            this.mSupportedConnectionMode = builder.mSupportedConnectionMode;
            this.mSessionConnectionMode = builder.mSessionConnectionMode;
        }

        public int getAccessControlType() {
            return this.mAccessControlType;
        }

        public long getLocationQueryTime() {
            return this.mLocationQueryTimeMillis;
        }

        public long getOnDeviceLookupTime() {
            return this.mOnDeviceLookupTimeMillis;
        }

        public long getTotalCheckingTime() {
            return this.mTotalCheckingTimeMillis;
        }

        public boolean getIsAllowed() {
            return this.mIsAllowed;
        }

        public boolean getIsEmergency() {
            return this.mIsEmergency;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public String[] getCountryCodes() {
            return this.mCountryCodes;
        }

        public int getConfigDataSource() {
            return this.mConfigDataSource;
        }

        public int getCarrierId() {
            return this.mCarrierId;
        }

        public int getTriggeringEvent() {
            return this.mTriggeringEvent;
        }

        public boolean isNtnOnlyCarrier() {
            return this.mIsNtnOnlyCarrier;
        }

        public int getSupportedConnectionMode() {
            return this.mSupportedConnectionMode;
        }

        public int getSessionConnectionMode() {
            return this.mSessionConnectionMode;
        }

        public static class Builder {
            private int mAccessControlType;
            private int mConfigDataSource;
            private String[] mCountryCodes;
            private boolean mIsAllowed;
            private boolean mIsEmergency;
            private long mLocationQueryTimeMillis;
            private long mOnDeviceLookupTimeMillis;
            private int mResultCode;
            private long mTotalCheckingTimeMillis;
            private int mCarrierId = -1;
            private int mTriggeringEvent = 0;
            private boolean mIsNtnOnlyCarrier = false;
            private int mSupportedConnectionMode = 0;
            private int mSessionConnectionMode = 0;

            public Builder setAccessControlType(int i) {
                this.mAccessControlType = i;
                return this;
            }

            public Builder setLocationQueryTime(long j) {
                this.mLocationQueryTimeMillis = j;
                return this;
            }

            public Builder setOnDeviceLookupTime(long j) {
                this.mOnDeviceLookupTimeMillis = j;
                return this;
            }

            public Builder setTotalCheckingTime(long j) {
                this.mTotalCheckingTimeMillis = j;
                return this;
            }

            public Builder setIsAllowed(boolean z) {
                this.mIsAllowed = z;
                return this;
            }

            public Builder setIsEmergency(boolean z) {
                this.mIsEmergency = z;
                return this;
            }

            public Builder setResult(int i) {
                this.mResultCode = i;
                return this;
            }

            /* JADX INFO: renamed from: $r8$lambda$Ax19GOk1HIviIUmcXecr-7C1gx0, reason: not valid java name */
            public static /* synthetic */ String[] m1353$r8$lambda$Ax19GOk1HIviIUmcXecr7C1gx0(int i) {
                return new String[i];
            }

            public Builder setCountryCodes(String[] strArr) {
                this.mCountryCodes = (String[]) Arrays.stream(strArr).toArray(new IntFunction() { // from class: com.android.internal.telephony.metrics.SatelliteStats$SatelliteAccessControllerParams$Builder$$ExternalSyntheticLambda0
                    @Override // java.util.function.IntFunction
                    public final Object apply(int i) {
                        return SatelliteStats.SatelliteAccessControllerParams.Builder.m1353$r8$lambda$Ax19GOk1HIviIUmcXecr7C1gx0(i);
                    }
                });
                return this;
            }

            public Builder setConfigDatasource(int i) {
                this.mConfigDataSource = i;
                return this;
            }

            public Builder setCarrierId(int i) {
                this.mCarrierId = i;
                return this;
            }

            public Builder setTriggeringEvent(int i) {
                this.mTriggeringEvent = i;
                return this;
            }

            public Builder setIsNtnOnlyCarrier(boolean z) {
                this.mIsNtnOnlyCarrier = z;
                return this;
            }

            public Builder setSupportedConnectionMode(int i) {
                this.mSupportedConnectionMode = i;
                return this;
            }

            public Builder setSessionConnectionMode(int i) {
                this.mSessionConnectionMode = i;
                return this;
            }

            public SatelliteAccessControllerParams build() {
                return new SatelliteAccessControllerParams(this);
            }
        }

        public String toString() {
            return "AccessControllerParams(, AccessControlType=" + this.mAccessControlType + ", LocationQueryTime=" + this.mLocationQueryTimeMillis + ", OnDeviceLookupTime=" + this.mOnDeviceLookupTimeMillis + ", TotalCheckingTime=" + this.mTotalCheckingTimeMillis + ", IsAllowed=" + this.mIsAllowed + ", IsEmergency=" + this.mIsEmergency + ", ResultCode=" + this.mResultCode + ", CountryCodes=" + Arrays.toString(this.mCountryCodes) + ", ConfigDataSource=" + this.mConfigDataSource + ", CarrierId=" + this.mCarrierId + ", TriggeringEvent=" + this.mTriggeringEvent + ", IsNtnOnlyCarrier=" + this.mIsNtnOnlyCarrier + ", supportedConnectionMode=" + this.mSupportedConnectionMode + ", sessionConnectionMode=" + this.mSessionConnectionMode + ")";
        }
    }

    public synchronized void onSatelliteControllerMetrics(SatelliteControllerParams satelliteControllerParams) {
        PersistAtomsProto$SatelliteController persistAtomsProto$SatelliteController = new PersistAtomsProto$SatelliteController();
        persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsSuccess = satelliteControllerParams.getCountOfSatelliteServiceEnablementsSuccess();
        persistAtomsProto$SatelliteController.countOfSatelliteServiceEnablementsFail = satelliteControllerParams.getCountOfSatelliteServiceEnablementsFail();
        persistAtomsProto$SatelliteController.countOfOutgoingDatagramSuccess = satelliteControllerParams.getCountOfOutgoingDatagramSuccess();
        persistAtomsProto$SatelliteController.countOfOutgoingDatagramFail = satelliteControllerParams.getCountOfOutgoingDatagramFail();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramSuccess = satelliteControllerParams.getCountOfIncomingDatagramSuccess();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramFail = satelliteControllerParams.getCountOfIncomingDatagramFail();
        persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsSuccess = satelliteControllerParams.getCountOfDatagramTypeSosSmsSuccess();
        persistAtomsProto$SatelliteController.countOfDatagramTypeSosSmsFail = satelliteControllerParams.getCountOfDatagramTypeSosSmsFail();
        persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingSuccess = satelliteControllerParams.getCountOfDatagramTypeLocationSharingSuccess();
        persistAtomsProto$SatelliteController.countOfDatagramTypeLocationSharingFail = satelliteControllerParams.getCountOfDatagramTypeLocationSharingFail();
        persistAtomsProto$SatelliteController.countOfProvisionSuccess = satelliteControllerParams.getCountOfProvisionSuccess();
        persistAtomsProto$SatelliteController.countOfProvisionFail = satelliteControllerParams.getCountOfProvisionFail();
        persistAtomsProto$SatelliteController.countOfDeprovisionSuccess = satelliteControllerParams.getCountOfDeprovisionSuccess();
        persistAtomsProto$SatelliteController.countOfDeprovisionFail = satelliteControllerParams.getCountOfDeprovisionFail();
        persistAtomsProto$SatelliteController.totalServiceUptimeSec = satelliteControllerParams.getTotalServiceUptimeSec();
        persistAtomsProto$SatelliteController.totalBatteryConsumptionPercent = satelliteControllerParams.getTotalBatteryConsumptionPercent();
        persistAtomsProto$SatelliteController.totalBatteryChargedTimeSec = satelliteControllerParams.getTotalBatteryChargedTimeSec();
        persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsSuccess = satelliteControllerParams.getCountOfDemoModeSatelliteServiceEnablementsSuccess();
        persistAtomsProto$SatelliteController.countOfDemoModeSatelliteServiceEnablementsFail = satelliteControllerParams.getCountOfDemoModeSatelliteServiceEnablementsFail();
        persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramSuccess = satelliteControllerParams.getCountOfDemoModeOutgoingDatagramSuccess();
        persistAtomsProto$SatelliteController.countOfDemoModeOutgoingDatagramFail = satelliteControllerParams.getCountOfDemoModeOutgoingDatagramFail();
        persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramSuccess = satelliteControllerParams.getCountOfDemoModeIncomingDatagramSuccess();
        persistAtomsProto$SatelliteController.countOfDemoModeIncomingDatagramFail = satelliteControllerParams.getCountOfDemoModeIncomingDatagramFail();
        persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveSuccess = satelliteControllerParams.getCountOfDatagramTypeKeepAliveSuccess();
        persistAtomsProto$SatelliteController.countOfDatagramTypeKeepAliveFail = satelliteControllerParams.getCountOfDatagramTypeKeepAliveFail();
        persistAtomsProto$SatelliteController.countOfAllowedSatelliteAccess = satelliteControllerParams.getCountOfAllowedSatelliteAccess();
        persistAtomsProto$SatelliteController.countOfDisallowedSatelliteAccess = satelliteControllerParams.getCountOfDisallowedSatelliteAccess();
        persistAtomsProto$SatelliteController.countOfSatelliteAccessCheckFail = satelliteControllerParams.getCountOfSatelliteAccessCheckFail();
        persistAtomsProto$SatelliteController.isProvisioned = SatelliteControllerParams.isProvisioned();
        persistAtomsProto$SatelliteController.carrierId = SatelliteControllerParams.getCarrierId();
        persistAtomsProto$SatelliteController.countOfSatelliteAllowedStateChangedEvents = satelliteControllerParams.getCountOfSatelliteAllowedStateChangedEvents();
        persistAtomsProto$SatelliteController.countOfSuccessfulLocationQueries = satelliteControllerParams.getCountOfSuccessfulLocationQueries();
        persistAtomsProto$SatelliteController.countOfFailedLocationQueries = satelliteControllerParams.getCountOfFailedLocationQueries();
        persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationShown = satelliteControllerParams.getCountOfP2PSmsAvailableNotificationShown();
        persistAtomsProto$SatelliteController.countOfP2PSmsAvailableNotificationRemoved = satelliteControllerParams.getCountOfP2PSmsAvailableNotificationRemoved();
        persistAtomsProto$SatelliteController.isNtnOnlyCarrier = SatelliteControllerParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteController.versionOfSatelliteAccessConfig = SatelliteControllerParams.getVersionSatelliteAccessConfig();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsSuccess = satelliteControllerParams.getCountOfIncomingDatagramTypeSosSmsSuccess();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSosSmsFail = satelliteControllerParams.getCountOfIncomingDatagramTypeSosSmsFail();
        persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsSuccess = satelliteControllerParams.getCountOfOutgoingDatagramTypeSmsSuccess();
        persistAtomsProto$SatelliteController.countOfOutgoingDatagramTypeSmsFail = satelliteControllerParams.getCountOfOutgoingDatagramTypeSmsFail();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsSuccess = satelliteControllerParams.getCountOfIncomingDatagramTypeSmsSuccess();
        persistAtomsProto$SatelliteController.countOfIncomingDatagramTypeSmsFail = satelliteControllerParams.getCountOfIncomingDatagramTypeSmsFail();
        persistAtomsProto$SatelliteController.carrierRoamingSatelliteConfigVersion = SatelliteControllerParams.getCarrierRoamingSatelliteConfigVersion();
        persistAtomsProto$SatelliteController.maxAllowedDataMode = SatelliteControllerParams.getMaxAllowedDataMode();
        persistAtomsProto$SatelliteController.supportedConnectionMode = SatelliteControllerParams.getSupportedConnectionMode();
        this.mAtomsStorage.addSatelliteControllerStats(persistAtomsProto$SatelliteController);
    }

    public synchronized void onSatelliteSessionMetrics(SatelliteSessionParams satelliteSessionParams) {
        PersistAtomsProto$SatelliteSession persistAtomsProto$SatelliteSession = new PersistAtomsProto$SatelliteSession();
        persistAtomsProto$SatelliteSession.satelliteServiceInitializationResult = satelliteSessionParams.getSatelliteServiceInitializationResult();
        persistAtomsProto$SatelliteSession.satelliteTechnology = satelliteSessionParams.getSatelliteTechnology();
        persistAtomsProto$SatelliteSession.count = 1;
        persistAtomsProto$SatelliteSession.satelliteServiceTerminationResult = satelliteSessionParams.getTerminationResult();
        persistAtomsProto$SatelliteSession.initializationProcessingTimeMillis = satelliteSessionParams.getInitializationProcessingTime();
        persistAtomsProto$SatelliteSession.terminationProcessingTimeMillis = satelliteSessionParams.getTerminationProcessingTime();
        persistAtomsProto$SatelliteSession.sessionDurationSeconds = satelliteSessionParams.getSessionDuration();
        persistAtomsProto$SatelliteSession.countOfOutgoingDatagramSuccess = satelliteSessionParams.getCountOfOutgoingDatagramSuccess();
        persistAtomsProto$SatelliteSession.countOfOutgoingDatagramFailed = satelliteSessionParams.getCountOfOutgoingDatagramFailed();
        persistAtomsProto$SatelliteSession.countOfIncomingDatagramSuccess = satelliteSessionParams.getCountOfIncomingDatagramSuccess();
        persistAtomsProto$SatelliteSession.countOfIncomingDatagramFailed = satelliteSessionParams.getCountOfIncomingDatagramFailed();
        persistAtomsProto$SatelliteSession.isDemoMode = satelliteSessionParams.getIsDemoMode();
        persistAtomsProto$SatelliteSession.maxNtnSignalStrengthLevel = satelliteSessionParams.getMaxNtnSignalStrengthLevel();
        persistAtomsProto$SatelliteSession.carrierId = satelliteSessionParams.getCarrierId();
        persistAtomsProto$SatelliteSession.countOfSatelliteNotificationDisplayed = satelliteSessionParams.getCountOfSatelliteNotificationDisplayed();
        persistAtomsProto$SatelliteSession.countOfAutoExitDueToScreenOff = satelliteSessionParams.getCountOfAutoExitDueToScreenOff();
        persistAtomsProto$SatelliteSession.countOfAutoExitDueToTnNetwork = satelliteSessionParams.getCountOfAutoExitDueToTnNetwork();
        persistAtomsProto$SatelliteSession.isEmergency = satelliteSessionParams.getIsEmergency();
        persistAtomsProto$SatelliteSession.isNtnOnlyCarrier = satelliteSessionParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteSession.maxInactivityDurationSec = satelliteSessionParams.getMaxInactivityDurationSec();
        persistAtomsProto$SatelliteSession.supportedConnectionMode = satelliteSessionParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteSession.sessionConnectionMode = satelliteSessionParams.getSessionConnectionMode();
        persistAtomsProto$SatelliteSession.screenOnTimeSec = satelliteSessionParams.getScreenOnTimeSec();
        persistAtomsProto$SatelliteSession.batteryLevelDropPercent = satelliteSessionParams.getBatteryLevelDropPercent();
        persistAtomsProto$SatelliteSession.wasChargingDuringSession = satelliteSessionParams.wasChargingDuringSession();
        persistAtomsProto$SatelliteSession.batteryDesignCapacityMah = satelliteSessionParams.getBatteryDesignCapacityMah();
        persistAtomsProto$SatelliteSession.energyConsumedNwh = satelliteSessionParams.getEnergyConsumedNwh();
        this.mAtomsStorage.addSatelliteSessionStats(persistAtomsProto$SatelliteSession);
    }

    public synchronized void onSatelliteIncomingDatagramMetrics(SatelliteIncomingDatagramParams satelliteIncomingDatagramParams) {
        PersistAtomsProto$SatelliteIncomingDatagram persistAtomsProto$SatelliteIncomingDatagram = new PersistAtomsProto$SatelliteIncomingDatagram();
        persistAtomsProto$SatelliteIncomingDatagram.resultCode = satelliteIncomingDatagramParams.getResultCode();
        persistAtomsProto$SatelliteIncomingDatagram.datagramSizeBytes = satelliteIncomingDatagramParams.getDatagramSizeBytes();
        persistAtomsProto$SatelliteIncomingDatagram.datagramTransferTimeMillis = satelliteIncomingDatagramParams.getDatagramTransferTimeMillis();
        persistAtomsProto$SatelliteIncomingDatagram.isDemoMode = satelliteIncomingDatagramParams.getIsDemoMode();
        persistAtomsProto$SatelliteIncomingDatagram.carrierId = satelliteIncomingDatagramParams.getCarrierId();
        persistAtomsProto$SatelliteIncomingDatagram.isNtnOnlyCarrier = satelliteIncomingDatagramParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteIncomingDatagram.supportedConnectionMode = satelliteIncomingDatagramParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteIncomingDatagram.sessionConnectionMode = satelliteIncomingDatagramParams.getSessionConnectionMode();
        this.mAtomsStorage.addSatelliteIncomingDatagramStats(persistAtomsProto$SatelliteIncomingDatagram);
    }

    public synchronized void onSatelliteOutgoingDatagramMetrics(SatelliteOutgoingDatagramParams satelliteOutgoingDatagramParams) {
        PersistAtomsProto$SatelliteOutgoingDatagram persistAtomsProto$SatelliteOutgoingDatagram = new PersistAtomsProto$SatelliteOutgoingDatagram();
        persistAtomsProto$SatelliteOutgoingDatagram.datagramType = satelliteOutgoingDatagramParams.getDatagramType();
        persistAtomsProto$SatelliteOutgoingDatagram.resultCode = satelliteOutgoingDatagramParams.getResultCode();
        persistAtomsProto$SatelliteOutgoingDatagram.datagramSizeBytes = satelliteOutgoingDatagramParams.getDatagramSizeBytes();
        persistAtomsProto$SatelliteOutgoingDatagram.datagramTransferTimeMillis = satelliteOutgoingDatagramParams.getDatagramTransferTimeMillis();
        persistAtomsProto$SatelliteOutgoingDatagram.isDemoMode = satelliteOutgoingDatagramParams.getIsDemoMode();
        persistAtomsProto$SatelliteOutgoingDatagram.carrierId = satelliteOutgoingDatagramParams.getCarrierId();
        persistAtomsProto$SatelliteOutgoingDatagram.isNtnOnlyCarrier = satelliteOutgoingDatagramParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteOutgoingDatagram.supportedConnectionMode = satelliteOutgoingDatagramParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteOutgoingDatagram.sessionConnectionMode = satelliteOutgoingDatagramParams.getSessionConnectionMode();
        this.mAtomsStorage.addSatelliteOutgoingDatagramStats(persistAtomsProto$SatelliteOutgoingDatagram);
    }

    public synchronized void onSatelliteProvisionMetrics(SatelliteProvisionParams satelliteProvisionParams) {
        PersistAtomsProto$SatelliteProvision persistAtomsProto$SatelliteProvision = new PersistAtomsProto$SatelliteProvision();
        persistAtomsProto$SatelliteProvision.resultCode = satelliteProvisionParams.getResultCode();
        persistAtomsProto$SatelliteProvision.provisioningTimeSec = satelliteProvisionParams.getProvisioningTimeSec();
        persistAtomsProto$SatelliteProvision.isProvisionRequest = satelliteProvisionParams.getIsProvisionRequest();
        persistAtomsProto$SatelliteProvision.isCanceled = satelliteProvisionParams.getIsCanceled();
        persistAtomsProto$SatelliteProvision.carrierId = satelliteProvisionParams.getCarrierId();
        persistAtomsProto$SatelliteProvision.isNtnOnlyCarrier = satelliteProvisionParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteProvision.supportedConnectionMode = satelliteProvisionParams.getSupportedConnectionMode();
        this.mAtomsStorage.addSatelliteProvisionStats(persistAtomsProto$SatelliteProvision);
    }

    public synchronized void onSatelliteSosMessageRecommender(SatelliteSosMessageRecommenderParams satelliteSosMessageRecommenderParams) {
        PersistAtomsProto$SatelliteSosMessageRecommender persistAtomsProto$SatelliteSosMessageRecommender = new PersistAtomsProto$SatelliteSosMessageRecommender();
        persistAtomsProto$SatelliteSosMessageRecommender.isDisplaySosMessageSent = satelliteSosMessageRecommenderParams.isDisplaySosMessageSent();
        persistAtomsProto$SatelliteSosMessageRecommender.countOfTimerStarted = satelliteSosMessageRecommenderParams.getCountOfTimerStarted();
        persistAtomsProto$SatelliteSosMessageRecommender.isImsRegistered = satelliteSosMessageRecommenderParams.isImsRegistered();
        persistAtomsProto$SatelliteSosMessageRecommender.cellularServiceState = satelliteSosMessageRecommenderParams.getCellularServiceState();
        persistAtomsProto$SatelliteSosMessageRecommender.isMultiSim = satelliteSosMessageRecommenderParams.isMultiSim();
        persistAtomsProto$SatelliteSosMessageRecommender.recommendingHandoverType = satelliteSosMessageRecommenderParams.getRecommendingHandoverType();
        persistAtomsProto$SatelliteSosMessageRecommender.isSatelliteAllowedInCurrentLocation = satelliteSosMessageRecommenderParams.isSatelliteAllowedInCurrentLocation();
        persistAtomsProto$SatelliteSosMessageRecommender.isWifiConnected = satelliteSosMessageRecommenderParams.isWifiConnected();
        persistAtomsProto$SatelliteSosMessageRecommender.carrierId = satelliteSosMessageRecommenderParams.getCarrierId();
        persistAtomsProto$SatelliteSosMessageRecommender.isNtnOnlyCarrier = satelliteSosMessageRecommenderParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteSosMessageRecommender.count = 1;
        persistAtomsProto$SatelliteSosMessageRecommender.supportedConnectionMode = satelliteSosMessageRecommenderParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteSosMessageRecommender.sessionConnectionMode = satelliteSosMessageRecommenderParams.getSessionConnectionMode();
        persistAtomsProto$SatelliteSosMessageRecommender.plmn = satelliteSosMessageRecommenderParams.getPlmn();
        persistAtomsProto$SatelliteSosMessageRecommender.isInCarrierRoamingNtnMode = satelliteSosMessageRecommenderParams.getIsInCarrierRoamingNtnMode();
        persistAtomsProto$SatelliteSosMessageRecommender.carrierRoamingSatelliteEmergencyMessagingProvider = satelliteSosMessageRecommenderParams.getCarrierRoamingSatelliteEmergencyMessagingProvider();
        persistAtomsProto$SatelliteSosMessageRecommender.emergencyNumberSourceUsedInHandoverIntent = satelliteSosMessageRecommenderParams.getEmergencyNumberSourceUsedInHandoverIntent();
        this.mAtomsStorage.addSatelliteSosMessageRecommenderStats(persistAtomsProto$SatelliteSosMessageRecommender);
    }

    public synchronized void onCarrierRoamingSatelliteSessionMetrics(CarrierRoamingSatelliteSessionParams carrierRoamingSatelliteSessionParams) {
        PersistAtomsProto$CarrierRoamingSatelliteSession persistAtomsProto$CarrierRoamingSatelliteSession = new PersistAtomsProto$CarrierRoamingSatelliteSession();
        persistAtomsProto$CarrierRoamingSatelliteSession.carrierId = carrierRoamingSatelliteSessionParams.getCarrierId();
        persistAtomsProto$CarrierRoamingSatelliteSession.isNtnRoamingInHomeCountry = carrierRoamingSatelliteSessionParams.getIsNtnRoamingInHomeCountry();
        persistAtomsProto$CarrierRoamingSatelliteSession.totalSatelliteModeTimeSec = carrierRoamingSatelliteSessionParams.getTotalSatelliteModeTimeSec();
        persistAtomsProto$CarrierRoamingSatelliteSession.numberOfSatelliteConnections = carrierRoamingSatelliteSessionParams.getNumberOfSatelliteConnections();
        persistAtomsProto$CarrierRoamingSatelliteSession.avgDurationOfSatelliteConnectionSec = carrierRoamingSatelliteSessionParams.getAvgDurationOfSatelliteConnectionSec();
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapMinSec = carrierRoamingSatelliteSessionParams.mSatelliteConnectionGapMinSec;
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapAvgSec = carrierRoamingSatelliteSessionParams.mSatelliteConnectionGapAvgSec;
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteConnectionGapMaxSec = carrierRoamingSatelliteSessionParams.mSatelliteConnectionGapMaxSec;
        persistAtomsProto$CarrierRoamingSatelliteSession.rsrpAvg = carrierRoamingSatelliteSessionParams.mRsrpAvg;
        persistAtomsProto$CarrierRoamingSatelliteSession.rsrpMedian = carrierRoamingSatelliteSessionParams.mRsrpMedian;
        persistAtomsProto$CarrierRoamingSatelliteSession.rssnrAvg = carrierRoamingSatelliteSessionParams.mRssnrAvg;
        persistAtomsProto$CarrierRoamingSatelliteSession.rssnrMedian = carrierRoamingSatelliteSessionParams.mRssnrMedian;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfIncomingSms = carrierRoamingSatelliteSessionParams.mCountOfIncomingSms;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfOutgoingSms = carrierRoamingSatelliteSessionParams.mCountOfOutgoingSms;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfIncomingMms = carrierRoamingSatelliteSessionParams.mCountOfIncomingMms;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfOutgoingMms = carrierRoamingSatelliteSessionParams.mCountOfOutgoingMms;
        persistAtomsProto$CarrierRoamingSatelliteSession.supportedSatelliteServices = carrierRoamingSatelliteSessionParams.mSupportedSatelliteServices;
        persistAtomsProto$CarrierRoamingSatelliteSession.serviceDataPolicy = carrierRoamingSatelliteSessionParams.mServiceDataPolicy;
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteDataConsumedBytes = carrierRoamingSatelliteSessionParams.mSatelliteDataConsumedBytes;
        persistAtomsProto$CarrierRoamingSatelliteSession.totalRxDataBytes = carrierRoamingSatelliteSessionParams.getTotalRxDataBytes();
        persistAtomsProto$CarrierRoamingSatelliteSession.totalTxDataBytes = carrierRoamingSatelliteSessionParams.getTotalTxDataBytes();
        persistAtomsProto$CarrierRoamingSatelliteSession.isMultiSim = carrierRoamingSatelliteSessionParams.isMultiSim();
        persistAtomsProto$CarrierRoamingSatelliteSession.isNbIotNtn = carrierRoamingSatelliteSessionParams.isNbIotNtn();
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataConnections = carrierRoamingSatelliteSessionParams.mCountOfDataConnections;
        persistAtomsProto$CarrierRoamingSatelliteSession.lastFailCauses = carrierRoamingSatelliteSessionParams.mLastFailCauses;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataDisconnections = carrierRoamingSatelliteSessionParams.mCountOfDataDisconnections;
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfDataStalls = carrierRoamingSatelliteSessionParams.mCountOfDataStalls;
        persistAtomsProto$CarrierRoamingSatelliteSession.averageUplinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mAverageUplinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.averageDownlinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mAverageDownlinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.minUplinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mMinUplinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.maxUplinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mMaxUplinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.minDownlinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mMinDownlinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.maxDownlinkBandwidthKbps = carrierRoamingSatelliteSessionParams.mMaxDownlinkBandwidthKbps;
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteSupportedApps = carrierRoamingSatelliteSessionParams.mSatelliteSupportedApps;
        persistAtomsProto$CarrierRoamingSatelliteSession.satelliteSupportedUids = carrierRoamingSatelliteSessionParams.mSatelliteSupportedUids;
        persistAtomsProto$CarrierRoamingSatelliteSession.perAppSatelliteDataConsumedBytes = carrierRoamingSatelliteSessionParams.mPerAppSatelliteDataConsumedBytes;
        persistAtomsProto$CarrierRoamingSatelliteSession.perAppRxDataBytes = carrierRoamingSatelliteSessionParams.getPerAppRxDataBytes();
        persistAtomsProto$CarrierRoamingSatelliteSession.perAppTxDataBytes = carrierRoamingSatelliteSessionParams.getPerAppTxDataBytes();
        persistAtomsProto$CarrierRoamingSatelliteSession.supportedConnectionMode = carrierRoamingSatelliteSessionParams.mSupportedConnectionMode;
        persistAtomsProto$CarrierRoamingSatelliteSession.sessionConnectionMode = carrierRoamingSatelliteSessionParams.getSessionConnectionMode();
        persistAtomsProto$CarrierRoamingSatelliteSession.isWifiEnabled = carrierRoamingSatelliteSessionParams.isWifiEnabled();
        persistAtomsProto$CarrierRoamingSatelliteSession.isWfcEnabled = carrierRoamingSatelliteSessionParams.isWfcEnabled();
        persistAtomsProto$CarrierRoamingSatelliteSession.isWfcRegistered = carrierRoamingSatelliteSessionParams.isWfcRegistered();
        persistAtomsProto$CarrierRoamingSatelliteSession.screenOnTimeSec = carrierRoamingSatelliteSessionParams.getScreenOnTimeSec();
        persistAtomsProto$CarrierRoamingSatelliteSession.batteryLevelDropPercent = carrierRoamingSatelliteSessionParams.getBatteryLevelDropPercent();
        persistAtomsProto$CarrierRoamingSatelliteSession.wasChargingDuringSession = carrierRoamingSatelliteSessionParams.wasChargingDuringSession();
        persistAtomsProto$CarrierRoamingSatelliteSession.batteryDesignCapacityMah = carrierRoamingSatelliteSessionParams.getBatteryDesignCapacityMah();
        persistAtomsProto$CarrierRoamingSatelliteSession.energyConsumedNwh = carrierRoamingSatelliteSessionParams.getEnergyConsumedNwh();
        persistAtomsProto$CarrierRoamingSatelliteSession.eligibilitySource = carrierRoamingSatelliteSessionParams.getEligibilitySource();
        persistAtomsProto$CarrierRoamingSatelliteSession.plmn = carrierRoamingSatelliteSessionParams.getPlmn();
        persistAtomsProto$CarrierRoamingSatelliteSession.isWifiConnected = carrierRoamingSatelliteSessionParams.isWifiConnected();
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfNonEmergencyDialerDialogDisplayed = carrierRoamingSatelliteSessionParams.getCountOfNonEmergencyDialerDialogDisplayed();
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfEmergencyDialerButtonDisplayed = carrierRoamingSatelliteSessionParams.getCountOfEmergencyDialerButtonDisplayed();
        persistAtomsProto$CarrierRoamingSatelliteSession.countOfSatelliteNotificationDisplayed = carrierRoamingSatelliteSessionParams.getCountOfSatelliteNotificationDisplayed();
        this.mAtomsStorage.addCarrierRoamingSatelliteSessionStats(persistAtomsProto$CarrierRoamingSatelliteSession);
    }

    public synchronized void onCarrierRoamingSatelliteControllerStatsMetrics(CarrierRoamingSatelliteControllerStatsParams carrierRoamingSatelliteControllerStatsParams) {
        if (carrierRoamingSatelliteControllerStatsParams.getCarrierId() == -1) {
            logd("onCarrierRoamingSatelliteControllerStatsMetrics: carrier id is -1, ignore.");
            return;
        }
        PersistAtomsProto$CarrierRoamingSatelliteControllerStats persistAtomsProto$CarrierRoamingSatelliteControllerStats = new PersistAtomsProto$CarrierRoamingSatelliteControllerStats();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.configDataSource = carrierRoamingSatelliteControllerStatsParams.mConfigDataSource;
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfEntitlementStatusQueryRequest = carrierRoamingSatelliteControllerStatsParams.mCountOfEntitlementStatusQueryRequest;
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteConfigUpdateRequest = carrierRoamingSatelliteControllerStatsParams.mCountOfSatelliteConfigUpdateRequest;
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteNotificationDisplayed = carrierRoamingSatelliteControllerStatsParams.mCountOfSatelliteNotificationDisplayed;
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMinSec = carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapMinSec();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapAvgSec = carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapAvgSec();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteSessionGapMaxSec = carrierRoamingSatelliteControllerStatsParams.getSatelliteSessionGapMaxSec();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.carrierId = carrierRoamingSatelliteControllerStatsParams.getCarrierId();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.isDeviceEntitled = carrierRoamingSatelliteControllerStatsParams.isDeviceEntitled();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.isMultiSim = carrierRoamingSatelliteControllerStatsParams.isMultiSim();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSatelliteSessions = carrierRoamingSatelliteControllerStatsParams.getCountOfSatelliteSessions();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.isNbIotNtn = carrierRoamingSatelliteControllerStatsParams.isNbIotNtn();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.supportedConnectionMode = carrierRoamingSatelliteControllerStatsParams.getSupportedConnectionMode();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeAutomatic = carrierRoamingSatelliteControllerStatsParams.getCountOfSessionConnectionModeAutomatic();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.countOfSessionConnectionModeManual = carrierRoamingSatelliteControllerStatsParams.getCountOfSessionConnectionModeManual();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.serviceDataPolicy = carrierRoamingSatelliteControllerStatsParams.getServiceDataPolicy();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.totalSessionDurationSec = carrierRoamingSatelliteControllerStatsParams.getSessionDurationSec();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.satelliteAttachSupported = carrierRoamingSatelliteControllerStatsParams.isSatelliteAttachSupported();
        persistAtomsProto$CarrierRoamingSatelliteControllerStats.eligibilitySource = carrierRoamingSatelliteControllerStatsParams.getEligibilitySource();
        this.mAtomsStorage.addCarrierRoamingSatelliteControllerStats(persistAtomsProto$CarrierRoamingSatelliteControllerStats);
    }

    public synchronized void resetCarrierRoamingSatelliteControllerStats() {
        CarrierRoamingSatelliteControllerStats.getOrCreateInstance().resetSessionGapLists();
    }

    public synchronized void onSatelliteEntitlementMetrics(SatelliteEntitlementParams satelliteEntitlementParams) {
        PersistAtomsProto$SatelliteEntitlement persistAtomsProto$SatelliteEntitlement = new PersistAtomsProto$SatelliteEntitlement();
        persistAtomsProto$SatelliteEntitlement.carrierId = satelliteEntitlementParams.getCarrierId();
        persistAtomsProto$SatelliteEntitlement.result = satelliteEntitlementParams.getResult();
        persistAtomsProto$SatelliteEntitlement.entitlementStatus = satelliteEntitlementParams.getEntitlementStatus();
        persistAtomsProto$SatelliteEntitlement.isRetry = satelliteEntitlementParams.getIsRetry();
        persistAtomsProto$SatelliteEntitlement.count = satelliteEntitlementParams.getCount();
        persistAtomsProto$SatelliteEntitlement.isAllowedServiceEntitlement = satelliteEntitlementParams.getIsAllowedServiceEntitlement();
        persistAtomsProto$SatelliteEntitlement.entitlementServiceType = satelliteEntitlementParams.getEntitlementServiceType();
        persistAtomsProto$SatelliteEntitlement.entitlementDataPolicy = satelliteEntitlementParams.getEntitlementDataPolicy();
        persistAtomsProto$SatelliteEntitlement.supportedConnectionMode = satelliteEntitlementParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteEntitlement.httpStatusCode = satelliteEntitlementParams.getHttpStatusCode();
        persistAtomsProto$SatelliteEntitlement.triggerEvent = satelliteEntitlementParams.getTriggerEvent();
        this.mAtomsStorage.addSatelliteEntitlementStats(persistAtomsProto$SatelliteEntitlement);
    }

    public synchronized void onSatelliteConfigUpdaterMetrics(SatelliteConfigUpdaterParams satelliteConfigUpdaterParams) {
        PersistAtomsProto$SatelliteConfigUpdater persistAtomsProto$SatelliteConfigUpdater = new PersistAtomsProto$SatelliteConfigUpdater();
        persistAtomsProto$SatelliteConfigUpdater.configVersion = satelliteConfigUpdaterParams.getConfigVersion();
        persistAtomsProto$SatelliteConfigUpdater.oemConfigResult = satelliteConfigUpdaterParams.getOemConfigResult();
        persistAtomsProto$SatelliteConfigUpdater.carrierConfigResult = satelliteConfigUpdaterParams.getCarrierConfigResult();
        persistAtomsProto$SatelliteConfigUpdater.count = satelliteConfigUpdaterParams.getCount();
        this.mAtomsStorage.addSatelliteConfigUpdaterStats(persistAtomsProto$SatelliteConfigUpdater);
    }

    public synchronized void onSatelliteAccessControllerMetrics(SatelliteAccessControllerParams satelliteAccessControllerParams) {
        PersistAtomsProto$SatelliteAccessController persistAtomsProto$SatelliteAccessController = new PersistAtomsProto$SatelliteAccessController();
        persistAtomsProto$SatelliteAccessController.accessControlType = satelliteAccessControllerParams.getAccessControlType();
        persistAtomsProto$SatelliteAccessController.locationQueryTimeMillis = satelliteAccessControllerParams.getLocationQueryTime();
        persistAtomsProto$SatelliteAccessController.onDeviceLookupTimeMillis = satelliteAccessControllerParams.getOnDeviceLookupTime();
        persistAtomsProto$SatelliteAccessController.totalCheckingTimeMillis = satelliteAccessControllerParams.getTotalCheckingTime();
        persistAtomsProto$SatelliteAccessController.isAllowed = satelliteAccessControllerParams.getIsAllowed();
        persistAtomsProto$SatelliteAccessController.isEmergency = satelliteAccessControllerParams.getIsEmergency();
        persistAtomsProto$SatelliteAccessController.resultCode = satelliteAccessControllerParams.getResultCode();
        persistAtomsProto$SatelliteAccessController.countryCodes = satelliteAccessControllerParams.getCountryCodes();
        persistAtomsProto$SatelliteAccessController.configDataSource = satelliteAccessControllerParams.getConfigDataSource();
        persistAtomsProto$SatelliteAccessController.carrierId = satelliteAccessControllerParams.getCarrierId();
        persistAtomsProto$SatelliteAccessController.triggeringEvent = satelliteAccessControllerParams.getTriggeringEvent();
        persistAtomsProto$SatelliteAccessController.isNtnOnlyCarrier = satelliteAccessControllerParams.isNtnOnlyCarrier();
        persistAtomsProto$SatelliteAccessController.supportedConnectionMode = satelliteAccessControllerParams.getSupportedConnectionMode();
        persistAtomsProto$SatelliteAccessController.sessionConnectionMode = satelliteAccessControllerParams.getSessionConnectionMode();
        this.mAtomsStorage.addSatelliteAccessControllerStats(persistAtomsProto$SatelliteAccessController);
    }

    private static void logd(String str) {
        Rlog.d(TAG, str);
    }
}
