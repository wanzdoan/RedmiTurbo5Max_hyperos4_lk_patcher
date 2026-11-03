package com.android.internal.telephony;

import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Pair;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import javax.sip.InvalidArgumentException;

/* JADX INFO: loaded from: classes.dex */
public class DisplayInfoController extends Handler {
    public static final Set<Pair<Integer, Integer>> VALID_DISPLAY_INFO_SET = Set.of(Pair.create(13, 1), Pair.create(13, 2), Pair.create(13, 3), Pair.create(13, 5), Pair.create(20, 5));
    private PersistableBundle mConfigs;
    private final FeatureFlags mFeatureFlags;
    private final String mLogTag;
    protected NetworkTypeController mNetworkTypeController;
    protected final Phone mPhone;
    protected ServiceState mServiceState;
    protected TelephonyDisplayInfo mTelephonyDisplayInfo;
    private final LocalLog mLocalLog = new LocalLog(128);
    protected final RegistrantList mTelephonyDisplayInfoChangedRegistrants = new RegistrantList();

    public void notifyDisplayInfoChanged() {
    }

    public DisplayInfoController(Phone phone, FeatureFlags featureFlags) {
        this.mPhone = phone;
        this.mFeatureFlags = featureFlags;
        this.mLogTag = "DIC-" + phone.getPhoneId();
        this.mServiceState = phone.getServiceStateTracker().getServiceState();
        this.mConfigs = new PersistableBundle();
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            try {
                this.mConfigs = carrierConfigManager.getConfigForSubId(phone.getSubId(), "show_roaming_indicator_bool");
            } catch (Exception unused) {
            }
        }
        this.mPhone.getServiceStateTracker().registerForServiceStateChanged(this, 1, null);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.DisplayInfoController$$ExternalSyntheticLambda0
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        this.mTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0, false, false, false);
        NetworkTypeController networkTypeControllerMakeNetworkTypeController = TelephonyComponentFactory.getInstance().inject(NetworkTypeController.class.getName()).makeNetworkTypeController(phone, this, featureFlags);
        this.mNetworkTypeController = networkTypeControllerMakeNetworkTypeController;
        networkTypeControllerMakeNetworkTypeController.sendMessage(0);
        log("register for satellite network callback");
        this.mNetworkTypeController.registerForSatelliteNetwork();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i == this.mPhone.getPhoneId()) {
            obtainMessage(2).sendToTarget();
        }
    }

    public TelephonyDisplayInfo getTelephonyDisplayInfo() {
        return this.mTelephonyDisplayInfo;
    }

    public void updateTelephonyDisplayInfo() {
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isDelayUpdateServiceState(this.mPhone)) {
            logl("Skip updating override network type since MIUI policy.");
            return;
        }
        if (this.mNetworkTypeController != null && this.mServiceState != null) {
            TelephonyDisplayInfo telephonyDisplayInfo = new TelephonyDisplayInfo(this.mNetworkTypeController.getDataNetworkType(), this.mNetworkTypeController.getOverrideNetworkType(), isRoaming(), this.mServiceState.isUsingNonTerrestrialNetwork(), this.mNetworkTypeController.getSatelliteConstrainedData());
            if (telephonyDisplayInfo.equals(this.mTelephonyDisplayInfo)) {
                return;
            }
            logl("TelephonyDisplayInfo changed from " + this.mTelephonyDisplayInfo + " to " + telephonyDisplayInfo);
            validateDisplayInfo(telephonyDisplayInfo);
            this.mTelephonyDisplayInfo = telephonyDisplayInfo;
            this.mTelephonyDisplayInfoChangedRegistrants.notifyRegistrants();
            this.mPhone.notifyDisplayInfoChanged(this.mTelephonyDisplayInfo);
            return;
        }
        loge("Found null object");
    }

    protected boolean isRoaming() {
        boolean roaming = this.mServiceState.getRoaming();
        if (!roaming || this.mConfigs.getBoolean("show_roaming_indicator_bool")) {
            return roaming;
        }
        logl("Override roaming for display due to carrier configs.");
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.sip.InvalidArgumentException */
    protected void validateDisplayInfo(TelephonyDisplayInfo telephonyDisplayInfo) {
        try {
            if (telephonyDisplayInfo.getNetworkType() == 19) {
                throw new InvalidArgumentException("LTE_CA is not a valid network type.");
            }
            if (telephonyDisplayInfo.getNetworkType() < 0 && telephonyDisplayInfo.getNetworkType() > 20) {
                throw new InvalidArgumentException("Invalid network type " + telephonyDisplayInfo.getNetworkType());
            }
            if (telephonyDisplayInfo.getOverrideNetworkType() != 0 && !VALID_DISPLAY_INFO_SET.contains(Pair.create(Integer.valueOf(telephonyDisplayInfo.getNetworkType()), Integer.valueOf(telephonyDisplayInfo.getOverrideNetworkType())))) {
                throw new InvalidArgumentException("Invalid network type override " + TelephonyDisplayInfo.overrideNetworkTypeToString(telephonyDisplayInfo.getOverrideNetworkType()) + " for " + TelephonyManager.getNetworkTypeName(telephonyDisplayInfo.getNetworkType()));
            }
        } catch (InvalidArgumentException e) {
            logel(e.getMessage());
            AnomalyReporter.reportAnomaly(UUID.fromString("3aa92a2c-94ed-46a0-a744-d6b1dfec2a56"), e.getMessage(), this.mPhone.getCarrierId());
        }
    }

    public void registerForTelephonyDisplayInfoChanged(Handler handler, int i, Object obj) {
        this.mTelephonyDisplayInfoChangedRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForTelephonyDisplayInfoChanged(Handler handler) {
        this.mTelephonyDisplayInfoChangedRegistrants.remove(handler);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            this.mServiceState = this.mPhone.getServiceStateTracker().getServiceState();
            log("ServiceState updated, isRoaming=" + this.mServiceState.getRoaming());
            updateTelephonyDisplayInfo();
            return;
        }
        if (i != 2) {
            return;
        }
        this.mConfigs = ((CarrierConfigManager) this.mPhone.getContext().getSystemService(CarrierConfigManager.class)).getConfigForSubId(this.mPhone.getSubId(), "show_roaming_indicator_bool");
        log("Carrier configs updated: " + this.mConfigs);
        updateTelephonyDisplayInfo();
    }

    private void log(String str) {
        Rlog.d(this.mLogTag, str);
    }

    private void loge(String str) {
        Rlog.e(this.mLogTag, str);
    }

    protected void logl(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    private void logel(String str) {
        loge(str);
        this.mLocalLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("DisplayInfoController:");
        androidUtilIndentingPrintWriter.println(" mPhone=" + this.mPhone.getPhoneName());
        androidUtilIndentingPrintWriter.println(" mTelephonyDisplayInfo=" + this.mTelephonyDisplayInfo.toString());
        androidUtilIndentingPrintWriter.flush();
        androidUtilIndentingPrintWriter.println("Local logs:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println(" ***************************************");
        this.mNetworkTypeController.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.flush();
    }
}
