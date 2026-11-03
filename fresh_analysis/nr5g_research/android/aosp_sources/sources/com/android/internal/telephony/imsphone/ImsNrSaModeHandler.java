package com.android.internal.telephony.imsphone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.function.IntPredicate;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public class ImsNrSaModeHandler extends Handler {
    public static final String TAG = "ImsNrSaModeHandler";
    private final CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private CarrierConfigManager mCarrierConfigManager;
    private NrSaDisableCriteria mEmergencyCriteria;
    private boolean mIsNrSaDisabledForWfc;
    private boolean mIsVoiceCapable;
    private boolean mIsWaitingResponseFromModem;
    private boolean mIsWfcEmergencyOverEpdn;
    private NrSaDisableCriteria mNormalCriteria;
    private boolean mPendingReevaluationForModemResponse;
    private final ImsPhone mPhone;

    /* JADX INFO: Access modifiers changed from: private */
    public static int getPolicyPriority(int i) {
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i != 3) {
            return ServiceStateTracker.CS_NOTIFICATION;
        }
        return 1;
    }

    private static class NrSaDisableCriteria {
        private final boolean mIsEmergency;
        private int mPolicy;
        private boolean mIsWifiRegistered = false;
        private boolean mHasActiveImsCall = false;

        NrSaDisableCriteria(int i, boolean z) {
            this.mPolicy = i;
            this.mIsEmergency = z;
        }

        public void setPolicy(int i) {
            this.mPolicy = i;
        }

        public int getPolicy() {
            return this.mPolicy;
        }

        public boolean isEmergency() {
            return this.mIsEmergency;
        }

        public void setWifiRegistered(boolean z) {
            this.mIsWifiRegistered = z;
        }

        public boolean isWifiRegistered() {
            return this.mIsWifiRegistered;
        }

        public void setActiveImsCall(boolean z) {
            this.mHasActiveImsCall = z;
        }

        public boolean hasActiveImsCall() {
            return this.mHasActiveImsCall;
        }

        public String toString() {
            return "{policy=" + this.mPolicy + ", emergency=" + this.mIsEmergency + ", wifi=" + this.mIsWifiRegistered + ", active=" + this.mHasActiveImsCall + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        setNrSaDisablePolicy(i2);
    }

    public ImsNrSaModeHandler(ImsPhone imsPhone, Looper looper) {
        super(looper);
        this.mNormalCriteria = null;
        this.mEmergencyCriteria = null;
        this.mIsWaitingResponseFromModem = false;
        this.mPendingReevaluationForModemResponse = false;
        this.mCarrierConfigChangeListener = new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.imsphone.ImsNrSaModeHandler$$ExternalSyntheticLambda1
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mPhone = imsPhone;
        this.mCarrierConfigManager = (CarrierConfigManager) imsPhone.getContext().getSystemService("carrier_config");
        registerForCarrierConfigChanges();
    }

    public void tearDown() {
        unregisterForCarrierConfigChanges();
        unregisterForPreciseCallStateChanges();
        this.mNormalCriteria = null;
        this.mEmergencyCriteria = null;
        if (isNrSaDisabledForWfc()) {
            setNrSaMode(true);
        }
    }

    public void onImsRegistered(int i) {
        NrSaDisableCriteria nrSaDisableCriteria;
        if (this.mNormalCriteria == null) {
            return;
        }
        Log.d(TAG, "onImsRegistered: ImsRegistrationTech = " + i);
        boolean z = i == 1;
        if (this.mNormalCriteria.isWifiRegistered() != z) {
            this.mNormalCriteria.setWifiRegistered(z);
            if (!this.mIsWfcEmergencyOverEpdn && (nrSaDisableCriteria = this.mEmergencyCriteria) != null) {
                nrSaDisableCriteria.setWifiRegistered(z);
            }
            calculateAndControlNrSa();
        }
    }

    public void onImsUnregistered(int i) {
        NrSaDisableCriteria nrSaDisableCriteria;
        NrSaDisableCriteria nrSaDisableCriteria2 = this.mNormalCriteria;
        if (nrSaDisableCriteria2 != null && i == 1 && nrSaDisableCriteria2.isWifiRegistered()) {
            Log.d(TAG, "onImsUnregistered : ImsRegistrationTech = " + i);
            this.mNormalCriteria.setWifiRegistered(false);
            if (!this.mIsWfcEmergencyOverEpdn && (nrSaDisableCriteria = this.mEmergencyCriteria) != null) {
                nrSaDisableCriteria.setWifiRegistered(false);
            }
            calculateAndControlNrSa();
        }
    }

    public void onImsEmergencyRegistered(int i) {
        if (!this.mIsWfcEmergencyOverEpdn || this.mEmergencyCriteria == null) {
            return;
        }
        Log.d(TAG, "onImsEmergencyRegistered: ImsRegistrationTech = " + i);
        boolean z = i == 1;
        if (this.mEmergencyCriteria.isWifiRegistered() != z) {
            this.mEmergencyCriteria.setWifiRegistered(z);
            calculateAndControlNrSa();
        }
    }

    public void onImsEmergencyUnregistered(int i) {
        NrSaDisableCriteria nrSaDisableCriteria;
        if (this.mIsWfcEmergencyOverEpdn && (nrSaDisableCriteria = this.mEmergencyCriteria) != null && i == 1 && nrSaDisableCriteria.isWifiRegistered()) {
            Log.d(TAG, "onImsEmergencyUnregistered : ImsRegistrationTech = " + i);
            this.mEmergencyCriteria.setWifiRegistered(false);
            calculateAndControlNrSa();
        }
    }

    public void onPreciseCallStateChanged() {
        ImsPhoneConnection firstConnection = this.mPhone.getForegroundCall().getFirstConnection();
        ImsPhoneConnection firstConnection2 = this.mPhone.getBackgroundCall().getFirstConnection();
        StringBuilder sb = new StringBuilder("onPreciseCallStateChanged :  foreground state = ");
        sb.append(this.mPhone.getForegroundCall().getState());
        sb.append(", emergency = ");
        Object objValueOf = "NA";
        sb.append(firstConnection != null ? Boolean.valueOf(firstConnection.isEmergencyCall()) : "NA");
        sb.append(", background state = ");
        sb.append(this.mPhone.getBackgroundCall().getState());
        sb.append(", emergency = ");
        if (firstConnection2 != null) {
            objValueOf = Boolean.valueOf(firstConnection2.isEmergencyCall());
        }
        sb.append(objValueOf);
        Log.d(TAG, sb.toString());
        boolean z = false;
        boolean z2 = isCallStateRequired(this.mNormalCriteria) && updateCallState(this.mNormalCriteria);
        if (isCallStateRequired(this.mEmergencyCriteria) && updateCallState(this.mEmergencyCriteria)) {
            z = true;
        }
        if (z2 || z) {
            calculateAndControlNrSa();
        }
    }

    public void updateImsCapability(int i) {
        NrSaDisableCriteria nrSaDisableCriteria = this.mNormalCriteria;
        if (nrSaDisableCriteria == null || nrSaDisableCriteria.getPolicy() == 0) {
            return;
        }
        boolean z = (i & 1) != 0;
        if (this.mIsVoiceCapable != z) {
            this.mIsVoiceCapable = z;
            calculateAndControlNrSa();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        Object obj2;
        switch (message.what) {
            case 101:
                onPreciseCallStateChanged();
                return;
            case 102:
                this.mIsWaitingResponseFromModem = false;
                try {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult != null && (obj = asyncResult.result) != null && (obj instanceof Boolean)) {
                        boolean zBooleanValue = ((Boolean) obj).booleanValue();
                        Log.d(TAG, "result of isVoNrEnabled = " + zBooleanValue);
                        if (!zBooleanValue) {
                            setNrSaMode(false);
                        }
                    } else {
                        Log.e(TAG, "isVoNrEnabled query failed : " + asyncResult.exception);
                    }
                    return;
                } finally {
                    handlePendingRequest();
                }
            case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                this.mIsWaitingResponseFromModem = false;
                try {
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2 != null && (obj2 = asyncResult2.userObj) != null && (obj2 instanceof Boolean) && asyncResult2.exception == null) {
                        boolean zBooleanValue2 = ((Boolean) obj2).booleanValue();
                        this.mIsNrSaDisabledForWfc = !zBooleanValue2;
                        Log.d(TAG, "result of setN1ModeEnabled = " + zBooleanValue2);
                    } else {
                        Log.e(TAG, "setN1ModeEnabled request failed : " + asyncResult2.exception);
                    }
                    return;
                } finally {
                    handlePendingRequest();
                }
            default:
                return;
        }
    }

    public boolean isNrSaDisabledForWfc() {
        return this.mIsNrSaDisabledForWfc;
    }

    private void registerForPreciseCallStateChanges() {
        this.mPhone.registerForPreciseCallStateChanged(this, 101, null);
    }

    private void unregisterForPreciseCallStateChanges() {
        this.mPhone.unregisterForPreciseCallStateChanged(this);
    }

    private void registerForCarrierConfigChanges() {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.imsphone.ImsNrSaModeHandler$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, this.mCarrierConfigChangeListener);
        }
    }

    private void unregisterForCarrierConfigChanges() {
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            carrierConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
        }
    }

    private void setNrSaDisablePolicy(int i) {
        CarrierConfigManager carrierConfigManager;
        if (this.mPhone.getSubId() != i || (carrierConfigManager = this.mCarrierConfigManager) == null) {
            return;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId(), "ims.sa_disable_policy_int", "ims.sa_disable_policy_for_emergency_int", "carrier_nr_availabilities_int_array", "imswfc.emergency_call_over_emergency_pdn_bool", "carrier_wfc_ims_available_bool");
        int[] intArray = configForSubId.getIntArray("carrier_nr_availabilities_int_array");
        boolean z = intArray != null && Arrays.stream(intArray).anyMatch(new IntPredicate() { // from class: com.android.internal.telephony.imsphone.ImsNrSaModeHandler$$ExternalSyntheticLambda2
            @Override // java.util.function.IntPredicate
            public final boolean test(int i2) {
                return ImsNrSaModeHandler.m1123$r8$lambda$alQSYD4fIPqMiBYcsSCGz6Y8Yw(i2);
            }
        });
        boolean z2 = configForSubId.getBoolean("carrier_wfc_ims_available_bool");
        if (!z || !z2) {
            this.mNormalCriteria = null;
            this.mEmergencyCriteria = null;
            unregisterForPreciseCallStateChanges();
            return;
        }
        int i2 = configForSubId.getInt("ims.sa_disable_policy_int", 0);
        int i3 = configForSubId.getInt("ims.sa_disable_policy_for_emergency_int", 0);
        this.mIsWfcEmergencyOverEpdn = configForSubId.getBoolean("imswfc.emergency_call_over_emergency_pdn_bool");
        Log.d(TAG, "setNrSaDisablePolicy : normalPolicy = " + i2 + ", emergencyPolicy = " + i3 + ", mIsWfcEmergencyOverEpdn = " + this.mIsWfcEmergencyOverEpdn);
        if (i2 != 0) {
            NrSaDisableCriteria nrSaDisableCriteria = this.mNormalCriteria;
            if (nrSaDisableCriteria == null) {
                this.mNormalCriteria = new NrSaDisableCriteria(i2, false);
            } else {
                nrSaDisableCriteria.setPolicy(i2);
            }
        } else {
            this.mNormalCriteria = null;
        }
        if (i3 != 0) {
            NrSaDisableCriteria nrSaDisableCriteria2 = this.mEmergencyCriteria;
            if (nrSaDisableCriteria2 == null) {
                this.mEmergencyCriteria = new NrSaDisableCriteria(i3, true);
            } else {
                nrSaDisableCriteria2.setPolicy(i3);
            }
            if (!this.mIsWfcEmergencyOverEpdn && this.mNormalCriteria == null) {
                this.mNormalCriteria = new NrSaDisableCriteria(0, false);
            }
        } else {
            this.mEmergencyCriteria = null;
        }
        if (isCallStateRequired(this.mNormalCriteria) || isCallStateRequired(this.mEmergencyCriteria)) {
            registerForPreciseCallStateChanges();
        } else {
            unregisterForPreciseCallStateChanges();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$a-lQSYD4fIPqMiBYcsSCGz6Y8Yw, reason: not valid java name */
    public static /* synthetic */ boolean m1123$r8$lambda$alQSYD4fIPqMiBYcsSCGz6Y8Yw(int i) {
        return i == 2;
    }

    private void setNrSaMode(boolean z) {
        if ((!z) == isNrSaDisabledForWfc()) {
            Log.i(TAG, "It is already in that state [" + z + "]");
            return;
        }
        this.mIsWaitingResponseFromModem = true;
        this.mPhone.getDefaultPhone().setN1ModeEnabled(z, obtainMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, Boolean.valueOf(z)));
        Log.i(TAG, "try setNrSaMode : " + z);
    }

    private boolean updateCallState(NrSaDisableCriteria nrSaDisableCriteria) {
        ImsPhoneCall[] imsPhoneCallArr = {this.mPhone.getForegroundCall(), this.mPhone.getBackgroundCall()};
        if (nrSaDisableCriteria.hasActiveImsCall()) {
            boolean z = false;
            for (int i = 0; i < 2; i++) {
                ImsPhoneConnection firstConnection = imsPhoneCallArr[i].getFirstConnection();
                if (firstConnection != null && ((firstConnection.getState() == Call.State.ACTIVE || firstConnection.getState() == Call.State.HOLDING) && nrSaDisableCriteria.isEmergency() == firstConnection.isEmergencyCall())) {
                    z = true;
                }
            }
            if (!z) {
                nrSaDisableCriteria.setActiveImsCall(false);
                return true;
            }
        } else {
            for (int i2 = 0; i2 < 2; i2++) {
                ImsPhoneConnection firstConnection2 = imsPhoneCallArr[i2].getFirstConnection();
                if (firstConnection2 != null && firstConnection2.getState() == Call.State.ACTIVE && nrSaDisableCriteria.isEmergency() == firstConnection2.isEmergencyCall()) {
                    nrSaDisableCriteria.setActiveImsCall(true);
                    return true;
                }
            }
        }
        return false;
    }

    private void calculateAndControlNrSa() {
        if (this.mIsWaitingResponseFromModem) {
            this.mPendingReevaluationForModemResponse = true;
            Log.d(TAG, "calculateAndControlNrSa: waiting for async result, set pending flag");
            return;
        }
        ArrayList arrayList = new ArrayList();
        NrSaDisableCriteria nrSaDisableCriteria = this.mNormalCriteria;
        if (nrSaDisableCriteria != null && isNrSaDisableNeeded(nrSaDisableCriteria)) {
            arrayList.add(Integer.valueOf(this.mNormalCriteria.getPolicy()));
        }
        NrSaDisableCriteria nrSaDisableCriteria2 = this.mEmergencyCriteria;
        if (nrSaDisableCriteria2 != null && isNrSaDisableNeeded(nrSaDisableCriteria2)) {
            arrayList.add(Integer.valueOf(this.mEmergencyCriteria.getPolicy()));
        }
        if (!arrayList.isEmpty()) {
            arrayList.sort(Comparator.comparingInt(new ToIntFunction() { // from class: com.android.internal.telephony.imsphone.ImsNrSaModeHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ImsNrSaModeHandler.getPolicyPriority(((Integer) obj).intValue());
                }
            }));
            int iIntValue = ((Integer) arrayList.get(0)).intValue();
            Log.d(TAG, "calculateAndControlNrSa: selectedPolicy = " + iIntValue);
            if (iIntValue == 2) {
                this.mIsWaitingResponseFromModem = true;
                this.mPhone.getDefaultPhone().isVoNrEnabled(obtainMessage(102), null);
                return;
            } else {
                setNrSaMode(false);
                return;
            }
        }
        setNrSaMode(true);
    }

    private void handlePendingRequest() {
        if (this.mPendingReevaluationForModemResponse) {
            this.mPendingReevaluationForModemResponse = false;
            Log.d(TAG, "handlePendingRequest: processing deferred re-evaluation");
            calculateAndControlNrSa();
        }
    }

    private static boolean isCallStateRequired(NrSaDisableCriteria nrSaDisableCriteria) {
        if (nrSaDisableCriteria != null) {
            return nrSaDisableCriteria.getPolicy() == 1 || nrSaDisableCriteria.getPolicy() == 2;
        }
        return false;
    }

    private boolean isNrSaDisableNeeded(NrSaDisableCriteria nrSaDisableCriteria) {
        boolean z = nrSaDisableCriteria.isEmergency() || this.mIsVoiceCapable;
        int policy = nrSaDisableCriteria.getPolicy();
        if (policy == 1 || policy == 2) {
            return nrSaDisableCriteria.isWifiRegistered() && z && nrSaDisableCriteria.hasActiveImsCall();
        }
        return policy == 3 && nrSaDisableCriteria.isWifiRegistered() && z;
    }
}
