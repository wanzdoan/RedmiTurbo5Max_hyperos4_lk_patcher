package com.mediatek.internal.telephony.data;

import android.R;
import android.net.NetworkCapabilities;
import android.os.Looper;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataProfile;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.MiuiPhoneSwitcherStub;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.Iterator;
import java.util.function.Predicate;
import vendor.mediatek.hardware.netdagent.INetdagents;
import vendor.mediatek.hardware.netdagent.V1_0.INetdagent;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataNetwork extends DataNetwork {
    private static final int BASE = 100;
    private static final boolean DBG = true;
    private static final int EVENT_VOICE_CALL = 101;
    private static final String LOG_TAG = "MtkDN";
    private static final int TEARDOWN_REASON_BASE = 2000;
    private static final int TEARDOWN_REASON_DURING_DATA_CALL = 2006;

    public MtkDataNetwork(Phone phone, FeatureFlags featureFlags, Looper looper, SparseArray<DataServiceManager> dataServiceManagers, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int transport, boolean isSatellite, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetwork.DataNetworkCallback callback) {
        super(phone, featureFlags, looper, dataServiceManagers, dataProfile, networkRequestList, transport, isSatellite, dataAllowedReason, callback);
    }

    protected boolean handleEventsOfConnectedState(int messageId) {
        logDbg("event=" + eventToStringMtk(messageId));
        if (messageId == 101) {
            updateSuspendState();
            return DBG;
        }
        return false;
    }

    protected void onDetachNetworkRequest(TelephonyNetworkRequest networkRequest, boolean shouldRetry) {
        super.onDetachNetworkRequest(networkRequest, shouldRetry);
        if (!this.mAttachedNetworkRequestList.isEmpty()) {
            final int capability = networkRequest.getHighestPriorityApnTypeNetworkCapability();
            TelephonyNetworkRequest sameCapabilityRequest = (TelephonyNetworkRequest) this.mAttachedNetworkRequestList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((TelephonyNetworkRequest) obj).hasCapability(capability);
                }
            }).findFirst().orElse(null);
            if (sameCapabilityRequest == null) {
                ((MtkDataNetworkController) this.mDataNetworkController).syncCapabilityReleaseRequest(capability);
            }
        }
    }

    protected int mtkGetCustomizedMtu(ApnSetting apn, int type) {
        int apnMtuV6;
        int apnMtuV4;
        int testSimMtu;
        String strEmMtu = SystemProperties.get("persist.vendor.radio.mobile.mtu", "");
        if (strEmMtu != null && strEmMtu.length() > 0) {
            int emMtu = 0;
            try {
                emMtu = Integer.parseInt(strEmMtu.substring(0, strEmMtu.length() < 4 ? strEmMtu.length() : 4));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                logErr("emMtu is invalid");
            }
            if (emMtu > 0) {
                if (type == 0) {
                    logDbg("MTU set by EM, emMtu: " + emMtu);
                }
                return emMtu;
            }
        }
        int phoneId = this.mPhone.getPhoneId();
        if (SubscriptionManager.isValidPhoneId(phoneId) && MtkDataHelper.getInstance().isTestIccCard(phoneId) && (testSimMtu = ((MtkDataConfigManager) this.mDataConfigManager).getMtuForTestSim()) > 0) {
            if (type == 0) {
                logDbg("MTU set by test SIM MTU configuration: " + testSimMtu);
            }
            return testSimMtu;
        }
        if (apn != null && type == 0 && (apnMtuV4 = apn.getMtuV4()) > 0) {
            logDbg("MTU-V4 set by apn configuration: " + apnMtuV4);
            return apnMtuV4;
        }
        if (apn != null && type == 1 && (apnMtuV6 = apn.getMtuV6()) > 0) {
            logDbg("MTU-V6 set by apn configuration: " + apnMtuV6);
            return apnMtuV6;
        }
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
        MtkDataProfileManager mtkDpm = (MtkDataProfileManager) mtkDnc.getDataProfileManager();
        int mtuTurbo = mtkDpm.getMtu();
        if (mtuTurbo > 0) {
            if (type == 0) {
                logDbg("MTU set by 'self-created' config resource to: " + mtuTurbo);
            }
            return mtuTurbo;
        }
        int mtuResource = this.mPhone.getContext().getResources().getInteger(R.integer.config_pinnerWebviewPinBytes);
        if (mtuResource > 0) {
            if (type == 0) {
                logDbg("MTU set by config resource to: " + mtuResource);
            }
            return mtuResource;
        }
        return this.mDataConfigManager.getDefaultMtu();
    }

    public void notifyVoiceCallEvent() {
        sendMessage(101);
    }

    public void notifyDsdaStateChanged() {
        int callState = MtkDataHelper.getInstance().getCallState();
        if (callState == 1) {
            sendMessage(101);
        }
    }

    protected NetworkRegistrationInfo getNetworkRegistrationInfo() {
        if (((MtkDataNetworkController) this.mDataNetworkController).getTurboSS() != null && this.mTransport == 1) {
            return ((MtkDataNetworkController) this.mDataNetworkController).getTurboSS().getNetworkRegistrationInfo(2, this.mTransport);
        }
        return super.getNetworkRegistrationInfo();
    }

    private boolean hasImsOrEmergencyCapability() {
        if (this.mNetworkCapabilities == null) {
            return false;
        }
        if (this.mNetworkCapabilities.hasCapability(10) || this.mNetworkCapabilities.hasCapability(4)) {
            return DBG;
        }
        return false;
    }

    protected boolean reviseSuspendedState(boolean suspendedState, NetworkRegistrationInfo nri) {
        int callState = MtkDataHelper.getInstance().getCallState();
        boolean isImsOrEmergency = hasImsOrEmergencyCapability();
        if (nri.getRegistrationState() != 1 && nri.getRegistrationState() != 5 && callState != 1 && !isImsOrEmergency && !this.mNetworkCapabilities.hasCapability(30) && (!this.mNetworkCapabilities.hasCapability(0) || this.mTransport != 2)) {
            return DBG;
        }
        if (this.mTransport == 2) {
            return suspendedState;
        }
        boolean isConcurrent = DBG;
        if (callState == 1) {
            isConcurrent = MtkDataHelper.getInstance().isDataAllowedForConcurrent(this.mPhone.getPhoneId());
        }
        boolean isWifiCalling = callState == 1 ? MtkDataHelper.getInstance().isWifiCallingEnabled() : false;
        logDbg("isSuspended: callState = " + callState + ", isConcurrent = " + isConcurrent + ", isImsOrEmergency = " + isImsOrEmergency + ", isWifiCalling = " + isWifiCalling);
        if (callState != 1 || isConcurrent || isImsOrEmergency || isWifiCalling) {
            return false;
        }
        return DBG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public INetdagents getNetdagentAidlInstance() {
        INetdagents netdagentInstance = INetdagents.Stub.asInterface(ServiceManager.getService(INetdagents.DESCRIPTOR + "/default"));
        if (netdagentInstance == null) {
            logDbg("aidl: getNetdagentInstance, get" + INetdagents.DESCRIPTOR + "aidl service failed");
        }
        return netdagentInstance;
    }

    protected void enableVolteIotFirewall(final boolean enable) {
        String simType;
        if (!((MtkDataConfigManager) this.mDataConfigManager).getVolteIotFirewallConfig()) {
            return;
        }
        final String ifc = getLinkProperties().getInterfaceName();
        if (ifc == null || ifc.length() == 0) {
            logDbg("enableVolteIotFirewall, interface name is null");
            return;
        }
        boolean isImsApn = false;
        if (this.mDataProfile.getApnSetting() != null) {
            Iterator it = this.mDataProfile.getApnSetting().getApnTypes().iterator();
            while (it.hasNext()) {
                int apnType = ((Integer) it.next()).intValue();
                if (apnType == 64) {
                    isImsApn = DBG;
                    break;
                }
            }
        }
        if (!isImsApn) {
            logDbg("enableVolteIotFirewall, not ims apn");
            return;
        }
        logDbg("enableVolteIotFirewall, ifc:" + ifc + ", " + enable);
        String simType2 = SystemProperties.get("vendor.gsm.sim.ril.testsim");
        if ((simType2 == null || !simType2.equals(RadioCapabilitySwitchUtil.IMSI_READY)) && ((simType = SystemProperties.get("vendor.gsm.sim.ril.testsim.2")) == null || !simType.equals(RadioCapabilitySwitchUtil.IMSI_READY))) {
            logDbg("enableVolteIotFirewall, not TEST SIM");
        } else {
            Thread thread = new Thread("enableVolteIotFirewall") { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String cmd;
                    try {
                        if (enable) {
                            cmd = String.format("netdagent firewall set_volte_nsiot_firewall %s", ifc);
                        } else {
                            cmd = String.format("netdagent firewall clear_volte_nsiot_firewall %s", ifc);
                        }
                        MtkDataNetwork.this.logDbg("cmd:" + cmd);
                        INetdagents netdagentAidl = MtkDataNetwork.this.getNetdagentAidlInstance();
                        MtkDataNetwork mtkDataNetwork = MtkDataNetwork.this;
                        if (netdagentAidl == null) {
                            mtkDataNetwork.logDbg("aidl: netdagentAidl is null");
                            INetdagent agent = INetdagent.getService();
                            if (agent == null) {
                                MtkDataNetwork.this.logDbg("agnent is null");
                                return;
                            } else {
                                agent.dispatchNetdagentCmd(cmd);
                                return;
                            }
                        }
                        mtkDataNetwork.logDbg("aidl: netdagentAidl setIotFirewall");
                        netdagentAidl.dispatchNetdagentCmd(cmd);
                    } catch (Exception e) {
                        MtkDataNetwork.this.logDbg("enableVolteIotFirewall:" + e);
                    }
                }
            };
            thread.start();
        }
    }

    protected boolean mtkSkipDataStallAlarm(String apn) {
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
        MtkDataConfigManager mtkDcm = (MtkDataConfigManager) mtkDnc.getDataConfigManager();
        return mtkDcm.shouldSkipDataStall(apn);
    }

    protected int getDataNetworkType(int transport) {
        NetworkRegistrationInfo nrsTurbo;
        if (((MtkDataNetworkController) this.mDataNetworkController).getTurboSS() != null && transport == 1 && (nrsTurbo = ((MtkDataNetworkController) this.mDataNetworkController).getTurboSS().getNetworkRegistrationInfo(2, transport)) != null) {
            int networkType = nrsTurbo.getAccessNetworkTechnology();
            logDbg("getTurboSS network type: " + networkType);
            return networkType;
        }
        return super.getDataNetworkType(transport);
    }

    protected void mtkUpdateNetworkCapabilities(NetworkCapabilities.Builder builder) {
        NetworkCapabilities nc = builder.build();
        if (nc == null || !nc.hasCapability(12)) {
            return;
        }
        boolean hasVsimReq = false;
        for (TelephonyNetworkRequest networkRequest : this.mAttachedNetworkRequestList) {
            if (networkRequest.hasCapability(30)) {
                hasVsimReq = DBG;
                break;
            }
        }
        if (hasVsimReq && !MtkDataHelper.isPreferredDataPhone(this.mPhone) && !MtkDataHelper.isDefaultDataPhone(this.mPhone)) {
            logDbg("remove INTERNET capability for VSIM");
            builder.removeCapability(12);
        }
    }

    protected int replaceTearDownReason(int reason) {
        boolean isDataDuringCall;
        if (TelephonyBaseUtilsStub.isMiuiRom()) {
            isDataDuringCall = MiuiPhoneSwitcherStub.get().getTempDataSwitching();
        } else {
            isDataDuringCall = MtkPhoneSwitcher.getInstance().getTempDataSwitchState();
        }
        logDbg("replaceTearDownReason reason=" + reason + " data during call=" + isDataDuringCall);
        if (reason == 30 && isDataDuringCall && this.mDataNetworkController.getDataSettingsManager().isDataEnabled() && this.mNetworkCapabilities.hasCapability(12) && MtkDataHelper.isDefaultDataPhone(this.mPhone)) {
            return 2006;
        }
        return super.replaceTearDownReason(reason);
    }

    private static String eventToStringMtk(int event) {
        if (event < 100) {
            return eventToString(event);
        }
        return event == 101 ? "EVENT_VOICE_CALL" : "Unknown(" + event + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDbg(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + (getCurrentState() != null ? getCurrentState().getName() + ": " : "") + s);
    }

    private void logErr(String s) {
        Rlog.e(LOG_TAG, this.mLogTag + ": " + (getCurrentState() != null ? getCurrentState().getName() + ": " : "") + s);
    }
}
