package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CrossSimRedialingController extends Handler {
    private static final boolean DBG;
    public static final int MSG_CROSS_STACK_TIMEOUT = 1;
    public static final int MSG_QUICK_CROSS_STACK_TIMEOUT = 2;
    private static final LocalLog sLocalLog;
    private String mCallId;
    private int mCrossStackTimer;
    private EmergencyNumberHelper mEmergencyNumberHelper;
    private int mModemCount;
    private String mNumber;
    private final ArrayList mPermanentRejectedSlots;
    private int mQuickCrossStackTimer;
    private EmergencyCallDomainSelector mSelector;
    private int mSlotId;
    private final ArrayList mStackSelectionHistory;
    private boolean mStartQuickCrossStackTimerWhenInService;
    private int mSubId;
    private final TelephonyManager mTelephonyManager;

    public interface EmergencyNumberHelper {
        boolean isEmergencyNumber(int i, String str);
    }

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
        sLocalLog = new LocalLog(50);
    }

    public CrossSimRedialingController(Context context, Looper looper) {
        super(looper);
        this.mStackSelectionHistory = new ArrayList();
        this.mPermanentRejectedSlots = new ArrayList();
        this.mEmergencyNumberHelper = new EmergencyNumberHelper() { // from class: com.android.services.telephony.domainselection.CrossSimRedialingController.1
            @Override // com.android.services.telephony.domainselection.CrossSimRedialingController.EmergencyNumberHelper
            public boolean isEmergencyNumber(int i, String str) {
                Map<Integer, List<EmergencyNumber>> emergencyNumberList;
                List<EmergencyNumber> list;
                String strStripSeparators = PhoneNumberUtils.stripSeparators(str);
                if (TextUtils.isEmpty(strStripSeparators)) {
                    return false;
                }
                try {
                    emergencyNumberList = CrossSimRedialingController.this.mTelephonyManager.getEmergencyNumberList();
                } catch (IllegalStateException e) {
                    CrossSimRedialingController.this.loge("isEmergencyNumber ise=" + e);
                    emergencyNumberList = null;
                } catch (RuntimeException e2) {
                    CrossSimRedialingController.this.loge("isEmergencyNumber rte=" + e2);
                    emergencyNumberList = null;
                }
                if (emergencyNumberList != null && (list = emergencyNumberList.get(Integer.valueOf(i))) != null && !list.isEmpty()) {
                    Iterator<EmergencyNumber> it = list.iterator();
                    while (it.hasNext()) {
                        if (strStripSeparators.equals(it.next().getNumber())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
    }

    public CrossSimRedialingController(Context context, Looper looper, EmergencyNumberHelper emergencyNumberHelper) {
        this(context, looper);
        this.mEmergencyNumberHelper = emergencyNumberHelper;
    }

    public void startTimer(Context context, EmergencyCallDomainSelector emergencyCallDomainSelector, String str, String str2, boolean z, boolean z2, int i) {
        logi("startTimer callId=" + str + ", in service=" + z + ", roaming=" + z2);
        if (!TextUtils.equals(this.mCallId, str)) {
            logi("startTimer callId changed");
            this.mCallId = str;
            this.mStackSelectionHistory.clear();
            this.mPermanentRejectedSlots.clear();
        }
        this.mSelector = emergencyCallDomainSelector;
        this.mSlotId = emergencyCallDomainSelector.getSlotId();
        this.mSubId = emergencyCallDomainSelector.getSubId();
        this.mNumber = str2;
        this.mModemCount = i;
        updateCarrierConfiguration(context);
        boolean zContains = this.mStackSelectionHistory.contains(Integer.valueOf(this.mSlotId));
        StringBuilder sb = new StringBuilder("startTimer slot=");
        sb.append(this.mSlotId);
        sb.append(", firstAttempt=");
        sb.append(!zContains);
        logi(sb.toString());
        this.mStackSelectionHistory.add(Integer.valueOf(this.mSlotId));
        if (!zContains && this.mQuickCrossStackTimer > 0 && !z2 && (z || !this.mStartQuickCrossStackTimerWhenInService)) {
            logi("startTimer quick timer started");
            sendEmptyMessageDelayed(2, this.mQuickCrossStackTimer);
        } else if (this.mCrossStackTimer > 0) {
            logi("startTimer timer started");
            sendEmptyMessageDelayed(1, this.mCrossStackTimer);
        }
    }

    public void stopTimer() {
        logi("stopTimer");
        removeMessages(1);
        removeMessages(2);
    }

    public void notifyCallFailure(int i) {
        logi("notifyCallFailure cause=" + i);
        if (i == 326) {
            this.mPermanentRejectedSlots.add(Integer.valueOf(this.mSlotId));
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1 || i == 2) {
            handleCrossStackTimeout();
        } else {
            super.handleMessage(message);
        }
    }

    private void handleCrossStackTimeout() {
        logi("handleCrossStackTimeout");
        if (isThereOtherSlot()) {
            this.mSelector.notifyCrossStackTimerExpired();
        } else {
            if (this.mPermanentRejectedSlots.isEmpty()) {
                return;
            }
            this.mSelector.maybeHangupOngoingDialing();
        }
    }

    public boolean isThereOtherSlotInService() {
        return isThereOtherSlot(true);
    }

    public boolean isThereOtherSlot() {
        return isThereOtherSlot(false);
    }

    private boolean isThereOtherSlot(boolean z) {
        logi("isThereOtherSlot modemCount=" + this.mModemCount);
        if (this.mModemCount < 2) {
            return false;
        }
        for (int i = 0; i < this.mModemCount; i++) {
            if (i != this.mSlotId) {
                if (this.mPermanentRejectedSlots.contains(Integer.valueOf(i))) {
                    logi("isThereOtherSlot index=" + i + ", permanent rejected");
                } else {
                    int simState = this.mTelephonyManager.getSimState(i);
                    if (simState != 5) {
                        logi("isThereOtherSlot index=" + i + ", simState=" + simState);
                    } else {
                        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
                        if (this.mEmergencyNumberHelper.isEmergencyNumber(subscriptionId, this.mNumber)) {
                            logi("isThereOtherSlot index=" + i + "(" + subscriptionId + "), found");
                            if (!z || isNetworkRegistered(subscriptionId)) {
                                return true;
                            }
                        } else {
                            logi("isThereOtherSlot index=" + i + "(" + subscriptionId + "), not emergency number");
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isNetworkRegistered(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return false;
        }
        ServiceState serviceState = this.mTelephonyManager.createForSubscriptionId(i).getServiceState();
        if (serviceState != null) {
            NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 1);
            if (networkRegistrationInfo != null && networkRegistrationInfo.isNetworkRegistered()) {
                return true;
            }
            NetworkRegistrationInfo networkRegistrationInfo2 = serviceState.getNetworkRegistrationInfo(1, 1);
            if (networkRegistrationInfo2 != null && networkRegistrationInfo2.isNetworkRegistered()) {
                return true;
            }
        }
        logi("isNetworkRegistered subId=" + i + " not network registered");
        return false;
    }

    private void updateCarrierConfiguration(Context context) {
        PersistableBundle configForSubId = ((CarrierConfigManager) context.getSystemService(CarrierConfigManager.class)).getConfigForSubId(this.mSubId, "imsemergency.cross_stack_redial_timer_sec_int", "imsemergency.quick_cross_stack_redial_timer_sec_int", "imsemergency.start_quick_cross_stack_redial_timer_when_registered_bool");
        if (configForSubId == null) {
            configForSubId = CarrierConfigManager.getDefaultConfig();
        }
        this.mCrossStackTimer = configForSubId.getInt("imsemergency.cross_stack_redial_timer_sec_int") * 1000;
        this.mQuickCrossStackTimer = configForSubId.getInt("imsemergency.quick_cross_stack_redial_timer_sec_int") * 1000;
        this.mStartQuickCrossStackTimerWhenInService = configForSubId.getBoolean("imsemergency.start_quick_cross_stack_redial_timer_when_registered_bool");
        logi("updateCarrierConfiguration , crossStackTimer=" + this.mCrossStackTimer + ", quickCrossStackTimer=" + this.mQuickCrossStackTimer + ", startQuickTimerInService=" + this.mStartQuickCrossStackTimerWhenInService);
    }

    public EmergencyNumberHelper getEmergencyNumberHelper() {
        return this.mEmergencyNumberHelper;
    }

    public void destroy() {
        if (DBG) {
            logd("destroy");
        }
        removeMessages(1);
        removeMessages(2);
    }

    private void logd(String str) {
        Log.d("CrossSimRedialingCtrl", "[" + this.mSlotId + "|" + this.mSubId + "] " + str);
    }

    private void logi(String str) {
        Log.i("CrossSimRedialingCtrl", "[" + this.mSlotId + "|" + this.mSubId + "] " + str);
        sLocalLog.log(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Log.e("CrossSimRedialingCtrl", "[" + this.mSlotId + "|" + this.mSubId + "] " + str);
        sLocalLog.log(str);
    }
}
