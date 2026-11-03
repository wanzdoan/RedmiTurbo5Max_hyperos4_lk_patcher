package com.android.internal.telephony.metrics;

import android.net.ConnectivityDiagnosticsManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda2;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.MiEventUtils;
import com.android.telephony.Rlog;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class DataStallRecoveryStats {
    private ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback mConnectivityDiagnosticsCallback;
    private ConnectivityDiagnosticsManager mConnectivityDiagnosticsManager;
    private final FeatureFlags mFeatureFlags;
    private final Handler mHandler;
    private final Phone mPhone;
    private final String mTag;
    private final TelephonyManager mTelephonyManager;
    private static final long REFRESH_DURATION_IN_MILLIS = SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_SENDING_MILLIS;
    private static final Executor INLINE_EXECUTOR = new Executor() { // from class: com.android.internal.telephony.metrics.DataStallRecoveryStats$$ExternalSyntheticLambda0
        @Override // java.util.concurrent.Executor
        public final void execute(Runnable runnable) {
            runnable.run();
        }
    };
    private String mIfaceName = null;
    private long mMetricsReflashTime = 0;
    private int mPhoneId = 0;
    private int mCarrierId = -1;
    private int mConvertedMccMnc = -1;
    private int mSignalStrength = 0;
    private int mBand = 0;
    private int mRat = 0;
    private boolean mIsOpportunistic = false;
    private boolean mIsMultiSim = false;
    private int mNetworkRegState = 0;
    private int mOtherSignalStrength = 0;
    private int mOtherNetworkRegState = 0;
    private int mInternetLinkStatus = -1;
    private int mLinkDownBandwidthKbps = 0;
    private int mLinkUpBandwidthKbps = 0;
    private int mNetworkProbesResult = -1;
    private int mNetworkProbesType = -1;
    private int mNetworkValidationResult = -1;
    private int mTcpMetricsCollectionPeriodMillis = -1;
    private int mTcpPacketFailRate = -1;
    private int mDnsConsecutiveTimeouts = -1;

    public DataStallRecoveryStats(Phone phone, FeatureFlags featureFlags, DataNetworkController dataNetworkController) {
        this.mConnectivityDiagnosticsManager = null;
        this.mConnectivityDiagnosticsCallback = null;
        this.mTag = "DSRS-" + phone.getPhoneId();
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        Handler handler = new Handler(BackgroundThread.get().getLooper());
        this.mHandler = handler;
        this.mTelephonyManager = (TelephonyManager) phone.getContext().getSystemService(TelephonyManager.class);
        Objects.requireNonNull(handler);
        dataNetworkController.registerDataNetworkControllerCallback(new DataNetworkController.DataNetworkControllerCallback(new NetworkTypeController$$ExternalSyntheticLambda2(handler)) { // from class: com.android.internal.telephony.metrics.DataStallRecoveryStats.1
            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onConnectedInternetDataNetworksChanged(Set<DataNetwork> set) {
                DataStallRecoveryStats.this.mIfaceName = null;
                Iterator<DataNetwork> it = set.iterator();
                if (it.hasNext()) {
                    DataNetwork next = it.next();
                    DataStallRecoveryStats.this.mIfaceName = next.getLinkProperties().getInterfaceName();
                }
            }

            @Override // com.android.internal.telephony.data.DataNetworkController.DataNetworkControllerCallback
            public void onPhysicalLinkStatusChanged(int i) {
                DataStallRecoveryStats.this.mInternetLinkStatus = i;
            }
        });
        try {
            this.mConnectivityDiagnosticsManager = (ConnectivityDiagnosticsManager) phone.getContext().getSystemService(ConnectivityDiagnosticsManager.class);
            this.mConnectivityDiagnosticsCallback = new ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback() { // from class: com.android.internal.telephony.metrics.DataStallRecoveryStats.2
                @Override // android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback
                public void onConnectivityReportAvailable(ConnectivityDiagnosticsManager.ConnectivityReport connectivityReport) {
                    PersistableBundle additionalInfo = connectivityReport.getAdditionalInfo();
                    DataStallRecoveryStats.this.mNetworkProbesResult = additionalInfo.getInt("networkProbesSucceeded");
                    DataStallRecoveryStats.this.mNetworkProbesType = additionalInfo.getInt("networkProbesAttempted");
                    DataStallRecoveryStats.this.mNetworkValidationResult = additionalInfo.getInt("networkValidationResult");
                }

                @Override // android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback
                public void onDataStallSuspected(ConnectivityDiagnosticsManager.DataStallReport dataStallReport) {
                    PersistableBundle stallDetails = dataStallReport.getStallDetails();
                    DataStallRecoveryStats.this.mTcpMetricsCollectionPeriodMillis = stallDetails.getInt("tcpMetricsCollectionPeriodMillis");
                    DataStallRecoveryStats.this.mTcpPacketFailRate = stallDetails.getInt("tcpPacketFailRate");
                    DataStallRecoveryStats.this.mDnsConsecutiveTimeouts = stallDetails.getInt("dnsConsecutiveTimeouts");
                }
            };
            this.mConnectivityDiagnosticsManager.registerConnectivityDiagnosticsCallback(new NetworkRequest.Builder().clearCapabilities().addCapability(12).addTransportType(0).build(), INLINE_EXECUTOR, this.mConnectivityDiagnosticsCallback);
        } catch (Exception unused) {
            this.mConnectivityDiagnosticsManager = null;
            this.mConnectivityDiagnosticsCallback = null;
        }
    }

    public void uploadMetrics(final int i, final boolean z, final int i2, final int i3, final boolean z2, final int i4) {
        this.mHandler.post(new Runnable() { // from class: com.android.internal.telephony.metrics.DataStallRecoveryStats$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$uploadMetrics$1(i, z, i2, i3, z2, i4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$uploadMetrics$1(int i, boolean z, int i2, int i3, boolean z2, int i4) {
        log("Set recovery action to " + i);
        try {
            refreshMetricsData();
            TelephonyStatsLog.write(315, this.mCarrierId, this.mRat, this.mSignalStrength, i, this.mIsOpportunistic, this.mIsMultiSim, this.mBand, z, i2, i3, this.mOtherSignalStrength, this.mOtherNetworkRegState, this.mNetworkRegState, z2, this.mPhoneId, i4, this.mInternetLinkStatus, this.mLinkUpBandwidthKbps, this.mLinkDownBandwidthKbps);
            log("Upload stats: Action:" + i + ", Recovered:" + z + ", Duration:" + i2 + ", Reason:" + i3 + ", First validation:" + z2 + ", Duration of action:" + i4 + ", " + this);
        } catch (Exception e) {
            loge("The metrics data cannot be refreshed.", e);
        }
    }

    private void refreshMetricsData() {
        logd("Refreshes the metrics data.");
        this.mMetricsReflashTime = SystemClock.elapsedRealtime();
        this.mPhoneId = this.mPhone.getPhoneId() + 1;
        this.mCarrierId = this.mPhone.getCarrierId();
        this.mSignalStrength = this.mPhone.getSignalStrength().getLevel();
        String networkOperator = this.mTelephonyManager.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            try {
                this.mConvertedMccMnc = Integer.parseInt(networkOperator);
            } catch (NumberFormatException unused) {
                loge("Invalid MCCMNC format: " + networkOperator);
                this.mConvertedMccMnc = -1;
            }
        } else {
            this.mConvertedMccMnc = -1;
        }
        updateBandwidths();
        updateRatAndBand();
        this.mIsOpportunistic = getIsOpportunistic(this.mPhone);
        this.mIsMultiSim = SimSlotState.getCurrentState().numActiveSims > 1;
        updateNetworkRegState();
        updateDsdsInfo();
    }

    private void updateBandwidths() {
        this.mLinkUpBandwidthKbps = 0;
        this.mLinkDownBandwidthKbps = 0;
        if (this.mIfaceName == null) {
            loge("Interface name is null");
            return;
        }
        DataNetworkController dataNetworkController = this.mPhone.getDataNetworkController();
        if (dataNetworkController == null) {
            loge("DataNetworkController is null");
            return;
        }
        DataNetwork dataNetworkByInterface = dataNetworkController.getDataNetworkByInterface(this.mIfaceName);
        if (dataNetworkByInterface == null) {
            loge("DataNetwork is null");
            return;
        }
        NetworkCapabilities networkCapabilities = dataNetworkByInterface.getNetworkCapabilities();
        if (networkCapabilities == null) {
            loge("NetworkCapabilities is null");
        } else {
            this.mLinkDownBandwidthKbps = networkCapabilities.getLinkDownstreamBandwidthKbps();
            this.mLinkUpBandwidthKbps = networkCapabilities.getLinkUpstreamBandwidthKbps();
        }
    }

    private void updateRatAndBand() {
        this.mRat = 0;
        this.mBand = 0;
        ServiceState serviceState = this.mPhone.getServiceState();
        if (serviceState == null) {
            loge("ServiceState is null");
            return;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        this.mRat = dataNetworkType;
        this.mBand = dataNetworkType != 18 ? ServiceStateStats.getBand(this.mPhone) : 0;
    }

    private static boolean getIsOpportunistic(Phone phone) {
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(phone.getSubId());
        return subscriptionInfoInternal != null && subscriptionInfoInternal.isOpportunistic();
    }

    private void updateNetworkRegState() {
        this.mNetworkRegState = 0;
        NetworkRegistrationInfo networkRegistrationInfo = this.mPhone.getServiceState().getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo != null) {
            this.mNetworkRegState = networkRegistrationInfo.getRegistrationState();
        }
    }

    private void updateDsdsInfo() {
        this.mOtherSignalStrength = 0;
        this.mOtherNetworkRegState = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getPhoneId() != this.mPhone.getPhoneId() && !getIsOpportunistic(phone)) {
                this.mOtherSignalStrength = phone.getSignalStrength().getLevel();
                NetworkRegistrationInfo networkRegistrationInfo = phone.getServiceState().getNetworkRegistrationInfo(2, 1);
                if (networkRegistrationInfo != null) {
                    this.mOtherNetworkRegState = networkRegistrationInfo.getRegistrationState();
                    return;
                }
                return;
            }
        }
    }

    public Bundle getDataStallRecoveryMetricsData(int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        if (SystemClock.elapsedRealtime() - this.mMetricsReflashTime > REFRESH_DURATION_IN_MILLIS) {
            try {
                refreshMetricsData();
            } catch (Exception e) {
                loge("The metrics data cannot be refreshed.", e);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt("Action", i);
        bundle.putInt("IsRecovered", z ? 1 : 0);
        bundle.putInt("Duration", i2);
        bundle.putInt("Reason", i3);
        bundle.putInt("DurationOfAction", i6);
        bundle.putInt("ValidationCount", i4);
        bundle.putInt("ActionValidationCount", i5);
        bundle.putInt(MiEventUtils.PARAM_PHONE_ID, this.mPhoneId);
        bundle.putInt("CarrierId", this.mCarrierId);
        bundle.putInt("MccMnc", this.mConvertedMccMnc);
        bundle.putInt("SignalStrength", this.mSignalStrength);
        bundle.putInt("Band", this.mBand);
        bundle.putInt("Rat", this.mRat);
        bundle.putInt("IsOpportunistic", this.mIsOpportunistic ? 1 : 0);
        bundle.putInt("IsMultiSim", this.mIsMultiSim ? 1 : 0);
        bundle.putInt("NetworkRegState", this.mNetworkRegState);
        bundle.putInt("OtherSignalStrength", this.mOtherSignalStrength);
        bundle.putInt("OtherNetworkRegState", this.mOtherNetworkRegState);
        bundle.putInt("InternetLinkStatus", this.mInternetLinkStatus);
        bundle.putInt("LinkDownBandwidthKbps", this.mLinkDownBandwidthKbps);
        bundle.putInt("LinkUpBandwidthKbps", this.mLinkUpBandwidthKbps);
        bundle.putInt("NetworkProbesResult", this.mNetworkProbesResult);
        bundle.putInt("NetworkProbesType", this.mNetworkProbesType);
        bundle.putInt("NetworkValidationResult", this.mNetworkValidationResult);
        bundle.putInt("TcpMetricsCollectionPeriodMillis", this.mTcpMetricsCollectionPeriodMillis);
        bundle.putInt("TcpPacketFailRate", this.mTcpPacketFailRate);
        bundle.putInt("DnsConsecutiveTimeouts", this.mDnsConsecutiveTimeouts);
        return bundle;
    }

    private void log(String str) {
        Rlog.i(this.mTag, str);
    }

    private void logd(String str) {
        Rlog.d(this.mTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mTag, str);
    }

    private void loge(String str, Throwable th) {
        Rlog.e(this.mTag, str, th);
    }

    public String toString() {
        return "DataStallRecoveryStats {Phone id:" + this.mPhoneId + ", Signal strength:" + this.mSignalStrength + ", Band:" + this.mBand + ", RAT:" + this.mRat + ", Opportunistic:" + this.mIsOpportunistic + ", Multi-SIM:" + this.mIsMultiSim + ", Network reg state:" + this.mNetworkRegState + ", Other signal strength:" + this.mOtherSignalStrength + ", Other network reg state:" + this.mOtherNetworkRegState + ", Link status:" + this.mInternetLinkStatus + ", Link down bandwidth:" + this.mLinkDownBandwidthKbps + ", Link up bandwidth:" + this.mLinkUpBandwidthKbps + "}";
    }
}
