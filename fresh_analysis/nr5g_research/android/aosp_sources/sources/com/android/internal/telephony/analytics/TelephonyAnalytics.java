package com.android.internal.telephony.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.DisconnectCause;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CarrierPrivilegesTracker$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyAnalytics {
    protected static final int INVALID_SUB_ID = -1;
    private static final String TAG = "TelephonyAnalytics";
    protected CallAnalytics mCallAnalytics;
    protected CallAnalyticsProvider mCallAnalyticsProvider;
    protected Context mContext;
    protected Executor mExecutor;
    private ExecutorService mExecutorService;
    private final Handler mHandler;
    protected Phone mPhone;
    protected ServiceStateAnalytics mServiceStateAnalytics;
    protected ServiceStateAnalyticsProvider mServiceStateAnalyticsProvider;
    private final int mSlotIndex;
    protected SmsMmsAnalytics mSmsMmsAnalytics;
    protected SmsMmsAnalyticsProvider mSmsMmsAnalyticsProvider;
    protected int mSubId;
    protected SubscriptionManager mSubscriptionManager;
    protected final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangeListener;
    protected TelephonyAnalyticsUtil mTelephonyAnalyticsUtil;

    public TelephonyAnalytics(Phone phone) {
        SubscriptionManager.OnSubscriptionsChangedListener onSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.internal.telephony.analytics.TelephonyAnalytics.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                int subId = TelephonyAnalytics.this.getSubId();
                TelephonyAnalytics telephonyAnalytics = TelephonyAnalytics.this;
                int i = telephonyAnalytics.mSubId;
                if (i == subId || subId == -1) {
                    return;
                }
                telephonyAnalytics.stopAnalytics(i);
                TelephonyAnalytics telephonyAnalytics2 = TelephonyAnalytics.this;
                telephonyAnalytics2.mSubId = subId;
                telephonyAnalytics2.startAnalytics(subId);
                Rlog.d(TelephonyAnalytics.TAG, "Started Listener, mSubId = " + TelephonyAnalytics.this.mSubId + "SlotId = " + TelephonyAnalytics.this.mSlotIndex);
            }
        };
        this.mSubscriptionsChangeListener = onSubscriptionsChangedListener;
        this.mPhone = phone;
        this.mContext = phone.getContext();
        this.mExecutor = new CarrierPrivilegesTracker$$ExternalSyntheticLambda0();
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        int phoneId = this.mPhone.getPhoneId();
        this.mSlotIndex = phoneId;
        this.mHandler = new Handler(BackgroundThread.get().getLooper());
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mTelephonyAnalyticsUtil = TelephonyAnalyticsUtil.getInstance(this.mContext);
        initializeAnalyticsClasses();
        this.mCallAnalyticsProvider = new CallAnalyticsProvider(this.mTelephonyAnalyticsUtil, phoneId);
        this.mSmsMmsAnalyticsProvider = new SmsMmsAnalyticsProvider(this.mTelephonyAnalyticsUtil, phoneId);
        this.mServiceStateAnalyticsProvider = new ServiceStateAnalyticsProvider(this.mTelephonyAnalyticsUtil, phoneId);
        startAnalytics(this.mSubId);
        SubscriptionManager subscriptionManager = this.mSubscriptionManager;
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(this.mExecutor, onSubscriptionsChangedListener);
            Rlog.d(TAG, "stopped listener");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSubId() {
        int subscriptionId = -1;
        try {
            subscriptionId = this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(this.mSlotIndex).getSubscriptionId();
            Rlog.d("TelephonyAnalyticsSubId", "SubId = " + subscriptionId + "SlotIndex = " + this.mSlotIndex);
            return subscriptionId;
        } catch (NullPointerException unused) {
            Rlog.e("TelephonyAnalyticsSubId", "Null Pointer Exception Caught");
            return subscriptionId;
        }
    }

    private void initializeAnalyticsClasses() {
        this.mServiceStateAnalytics = new ServiceStateAnalytics(this.mExecutor);
        this.mSmsMmsAnalytics = new SmsMmsAnalytics();
        this.mCallAnalytics = new CallAnalytics();
    }

    protected void startAnalytics(int i) {
        if (i == -1) {
            Rlog.d("StartAnalytics", "Invalid SubId = -1");
        } else {
            this.mServiceStateAnalytics.registerMyListener(this.mContext, i);
        }
    }

    protected void stopAnalytics(int i) {
        ServiceStateAnalytics serviceStateAnalytics = this.mServiceStateAnalytics;
        if (serviceStateAnalytics != null) {
            serviceStateAnalytics.unregisterMyListener(i);
        }
    }

    public SmsMmsAnalytics getSmsMmsAnalytics() {
        return this.mSmsMmsAnalytics;
    }

    public CallAnalytics getCallAnalytics() {
        return this.mCallAnalytics;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        printWriter.println("+    Telephony Analytics Report [2 months] [Slot ID = " + this.mSlotIndex + "]  +");
        printWriter.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        printWriter.println("Call Analytics Summary");
        ArrayList<String> arrayListAggregate = this.mCallAnalyticsProvider.aggregate();
        int size = arrayListAggregate.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            String str = arrayListAggregate.get(i2);
            i2++;
            printWriter.println("\t" + str);
        }
        printWriter.println("-----------------------------------------------");
        printWriter.println("SMS/MMS Analytics Summary");
        ArrayList<String> arrayListAggregate2 = this.mSmsMmsAnalyticsProvider.aggregate();
        int size2 = arrayListAggregate2.size();
        int i3 = 0;
        while (i3 < size2) {
            String str2 = arrayListAggregate2.get(i3);
            i3++;
            printWriter.println("\t\t" + str2);
        }
        printWriter.println("-----------------------------------------------");
        this.mServiceStateAnalytics.recordCurrentStateBeforeDump();
        printWriter.println("Service State Analytics Summary ");
        ArrayList<String> arrayListAggregate3 = this.mServiceStateAnalyticsProvider.aggregate();
        int size3 = arrayListAggregate3.size();
        while (i < size3) {
            String str3 = arrayListAggregate3.get(i);
            i++;
            printWriter.println("\t\t" + str3);
        }
        printWriter.println("-----------------------------------------------");
    }

    public class CallAnalytics {
        private static final String TAG = "CallAnalytics";
        private static final Map sImsCodeMap;

        private enum Status {
            SUCCESS("Success"),
            FAILURE("Failure");

            public String value;

            Status(String str) {
                this.value = str;
            }
        }

        private enum CallType {
            NORMAL_CALL("Normal Call"),
            SOS_CALL("SOS Call");

            public String value;

            CallType(String str) {
                this.value = str;
            }
        }

        public CallAnalytics() {
        }

        public void onCallTerminated(boolean z, boolean z2, int i, int i2, int i3) {
            String str;
            String string;
            String str2;
            if (z) {
                str = CallType.SOS_CALL.value;
            } else {
                str = CallType.NORMAL_CALL.value;
            }
            String str3 = str;
            if (z2) {
                string = (String) sImsCodeMap.getOrDefault(Integer.valueOf(i3), "UNKNOWN_REJECT_CAUSE");
                if (string.equals("UNKNOWN_REJECT_CAUSE")) {
                    Rlog.d(TAG, "UNKNOWN_REJECT_CAUSE: " + i3);
                }
                if (i3 == 501 || i3 == 510) {
                    str2 = Status.SUCCESS.value;
                } else {
                    str2 = Status.FAILURE.value;
                }
            } else {
                string = DisconnectCause.toString(i3);
                if (i3 == 3 || i3 == 2) {
                    str2 = Status.SUCCESS.value;
                } else {
                    str2 = Status.FAILURE.value;
                }
            }
            sendDataToProvider(str3, str2, i2, i, TelephonyManager.getNetworkTypeName(i), i3, string);
        }

        private void sendDataToProvider(final String str, final String str2, final int i, final int i2, final String str3, final int i3, final String str4) {
            TelephonyAnalytics.this.mExecutorService.execute(new Runnable() { // from class: com.android.internal.telephony.analytics.TelephonyAnalytics$CallAnalytics$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendDataToProvider$0(str, str2, i, str3, str4, i3, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendDataToProvider$0(String str, String str2, int i, String str3, String str4, int i2, int i3) {
            TelephonyAnalytics.this.mCallAnalyticsProvider.insertDataToDb(str, str2, i, str3, str4);
            Rlog.d(TAG, new ArrayList(List.of(str, str2, str4, "(" + i2 + ")", str3, "(" + i3 + ")", Integer.toString(i))).toString());
        }

        static {
            HashMap map = new HashMap();
            sImsCodeMap = map;
            map.put(0, "CODE_UNSPECIFIED");
            map.put(101, "CODE_LOCAL_ILLEGAL_ARGUMENT");
            map.put(102, "CODE_LOCAL_ILLEGAL_STATE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING), "CODE_LOCAL_INTERNAL_ERROR");
            map.put(106, "CODE_LOCAL_IMS_SERVICE_DOWN");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS), "CODE_LOCAL_NO_PENDING_CALL");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH), "CODE_LOCAL_ENDED_BY_CONFERENCE_MERGE");
            map.put(111, "CODE_LOCAL_POWER_OFF");
            map.put(112, "CODE_LOCAL_LOW_BATTERY");
            map.put(121, "CODE_LOCAL_NETWORK_NO_SERVICE");
            map.put(122, "CODE_LOCAL_NETWORK_NO_LTE_COVERAGE");
            map.put(123, "CODE_LOCAL_NETWORK_ROAMING");
            map.put(124, "CODE_LOCAL_NETWORK_IP_CHANGED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY), "CODE_LOCAL_SERVICE_UNAVAILABLE");
            map.put(132, "CODE_LOCAL_NOT_REGISTERED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_CARRIER_INFO_IMSI_ENCRYPTION), "CODE_LOCAL_CALL_EXCEEDED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_START_NETWORK_SCAN), "CODE_LOCAL_CALL_BUSY");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_NETWORK_SCAN), "CODE_LOCAL_CALL_DECLINE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_START_KEEPALIVE), "CODE_LOCAL_CALL_VCC_ON_PROGRESSING");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_STOP_KEEPALIVE), "CODE_LOCAL_CALL_RESOURCE_RESERVATION_FAILED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID), "CODE_LOCAL_CALL_CS_RETRY_REQUIRED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE), "CODE_LOCAL_CALL_VOLTE_RETRY_REQUIRED");
            map.put(148, "CODE_LOCAL_CALL_TERMINATED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI), "CODE_LOCAL_HO_NOT_FEASIBLE");
            map.put(201, "CODE_TIMEOUT_1XX_WAITING");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA), "CODE_TIMEOUT_NO_ANSWER");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA), "CODE_TIMEOUT_NO_ANSWER_CALL_UPDATE");
            map.put(240, "CODE_CALL_BARRED");
            map.put(241, "CODE_FDN_BLOCKED");
            map.put(243, "CODE_IMEI_NOT_ACCEPTED");
            map.put(244, "CODE_DIAL_MODIFIED_TO_USSD");
            map.put(245, "CODE_DIAL_MODIFIED_TO_SS");
            map.put(246, "CODE_DIAL_MODIFIED_TO_DIAL");
            map.put(Integer.valueOf(CallFailCause.RADIO_OFF), "CODE_DIAL_MODIFIED_TO_DIAL_VIDEO");
            map.put(248, "CODE_DIAL_VIDEO_MODIFIED_TO_DIAL");
            map.put(Integer.valueOf(CallFailCause.NO_VALID_SIM), "CODE_DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO");
            map.put(Integer.valueOf(CallFailCause.RADIO_INTERNAL_ERROR), "CODE_DIAL_VIDEO_MODIFIED_TO_SS");
            map.put(Integer.valueOf(CallFailCause.NETWORK_RESP_TIMEOUT), "CODE_DIAL_VIDEO_MODIFIED_TO_USSD");
            map.put(321, "CODE_SIP_REDIRECTED");
            map.put(331, "CODE_SIP_BAD_REQUEST");
            map.put(332, "CODE_SIP_FORBIDDEN");
            map.put(333, "CODE_SIP_NOT_FOUND");
            map.put(334, "CODE_SIP_NOT_SUPPORTED");
            map.put(335, "CODE_SIP_REQUEST_TIMEOUT");
            map.put(336, "CODE_SIP_TEMPRARILY_UNAVAILABLE");
            map.put(337, "CODE_SIP_BAD_ADDRESS");
            map.put(338, "CODE_SIP_BUSY");
            map.put(339, "CODE_SIP_REQUEST_CANCELLED");
            map.put(340, "CODE_SIP_NOT_ACCEPTABLE");
            map.put(341, "CODE_SIP_NOT_REACHABLE");
            map.put(342, "CODE_SIP_CLIENT_ERROR");
            map.put(343, "CODE_SIP_TRANSACTION_DOES_NOT_EXIST");
            map.put(351, "CODE_SIP_SERVER_INTERNAL_ERROR");
            map.put(352, "CODE_SIP_SERVICE_UNAVAILABLE");
            map.put(353, "CODE_SIP_SERVER_TIMEOUT");
            map.put(354, "CODE_SIP_SERVER_ERROR");
            map.put(361, "CODE_SIP_USER_REJECTED");
            map.put(362, "CODE_SIP_GLOBAL_ERROR");
            map.put(363, "CODE_EMERGENCY_TEMP_FAILURE");
            map.put(364, "CODE_EMERGENCY_PERM_FAILURE");
            map.put(365, "CODE_SIP_USER_MARKED_UNWANTED");
            map.put(366, "CODE_SIP_METHOD_NOT_ALLOWED");
            map.put(367, "CODE_SIP_PROXY_AUTHENTICATION_REQUIRED");
            map.put(368, "CODE_SIP_REQUEST_ENTITY_TOO_LARGE");
            map.put(369, "CODE_SIP_REQUEST_URI_TOO_LARGE");
            map.put(370, "CODE_SIP_EXTENSION_REQUIRED");
            map.put(371, "CODE_SIP_INTERVAL_TOO_BRIEF");
            map.put(372, "CODE_SIP_CALL_OR_TRANS_DOES_NOT_EXIST");
            map.put(373, "CODE_SIP_LOOP_DETECTED");
            map.put(374, "CODE_SIP_TOO_MANY_HOPS");
            map.put(376, "CODE_SIP_AMBIGUOUS");
            map.put(377, "CODE_SIP_REQUEST_PENDING");
            map.put(378, "CODE_SIP_UNDECIPHERABLE");
            map.put(401, "CODE_MEDIA_INIT_FAILED");
            map.put(402, "CODE_MEDIA_NO_DATA");
            map.put(403, "CODE_MEDIA_NOT_ACCEPTABLE");
            map.put(404, "CODE_MEDIA_UNSPECIFIED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_1), "CODE_USER_TERMINATED");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_2), "CODE_USER_NOANSWER");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_3), "CODE_USER_IGNORE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_4), "CODE_USER_DECLINE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_5), "CODE_LOW_BATTERY");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_6), "CODE_BLACKLISTED_CALL_ID");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_10), "CODE_USER_TERMINATED_BY_REMOTE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_11), "CODE_USER_REJECTED_SESSION_MODIFICATION");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_OEM_ERROR_12), "CODE_USER_CANCELLED_SESSION_MODIFICATION");
            map.put(1517, "CODE_SESSION_MODIFICATION_FAILED");
            map.put(801, "CODE_UT_NOT_SUPPORTED");
            map.put(802, "CODE_UT_SERVICE_UNAVAILABLE");
            map.put(803, "CODE_UT_OPERATION_NOT_ALLOWED");
            map.put(804, "CODE_UT_NETWORK_ERROR");
            map.put(821, "CODE_UT_CB_PASSWORD_MISMATCH");
            map.put(822, "CODE_UT_SS_MODIFIED_TO_DIAL");
            map.put(823, "CODE_UT_SS_MODIFIED_TO_USSD");
            map.put(824, "CODE_UT_SS_MODIFIED_TO_SS");
            map.put(825, "CODE_UT_SS_MODIFIED_TO_DIAL_VIDEO");
            map.put(901, "CODE_ECBM_NOT_SUPPORTED");
            map.put(902, "CODE_MULTIENDPOINT_NOT_SUPPORTED");
            map.put(1000, "CODE_REGISTRATION_ERROR");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_EVENT_NOTIFY), "CODE_ANSWERED_ELSEWHERE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_UNSOL_STK_CALL_SETUP), "CODE_CALL_PULL_OUT_OF_SYNC");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_SMS_STORAGE_FULL), "CODE_CALL_END_CAUSE_CALL_PULL");
            map.put(1100, "CODE_CALL_DROP_IWLAN_TO_LTE_UNAVAILABLE");
            map.put(Integer.valueOf(NtnRILConstants.MI_NTN_RIL_UNSOL_SIM_REFRESH), "CODE_REJECTED_ELSEWHERE");
            map.put(Integer.valueOf(CallFailCause.LOCAL_ILLEGAL_STATE), "CODE_SUPP_SVC_FAILED");
            map.put(Integer.valueOf(CallFailCause.LOCAL_INTERNAL_ERROR), "CODE_SUPP_SVC_CANCELLED");
            map.put(Integer.valueOf(CallFailCause.LOCAL_IMS_SERVICE_DOWN), "CODE_SUPP_SVC_REINVITE_COLLISION");
            map.put(Integer.valueOf(CallFailCause.SIP_REDIRECTED), "CODE_IWLAN_DPD_FAILURE");
            map.put(Integer.valueOf(CallFailCause.MEDIA_INIT_FAILED), "CODE_EPDG_TUNNEL_ESTABLISH_FAILURE");
            map.put(Integer.valueOf(CallFailCause.MEDIA_NO_DATA), "CODE_EPDG_TUNNEL_REKEY_FAILURE");
            map.put(Integer.valueOf(CallFailCause.MEDIA_NOT_ACCEPTABLE), "CODE_EPDG_TUNNEL_LOST_CONNECTION");
            map.put(Integer.valueOf(CallFailCause.MEDIA_UNSPECIFIED), "CODE_MAXIMUM_NUMBER_OF_CALLS_REACHED");
            map.put(1404, "CODE_REMOTE_CALL_DECLINE");
            map.put(1405, "CODE_DATA_LIMIT_REACHED");
            map.put(1406, "CODE_DATA_DISABLED");
            map.put(1407, "CODE_WIFI_LOST");
            map.put(1408, "CODE_IKEV2_AUTH_FAILURE");
            map.put(1500, "CODE_RADIO_OFF");
            map.put(Integer.valueOf(CallFailCause.USER_NOANSWER), "CODE_NO_VALID_SIM");
            map.put(Integer.valueOf(CallFailCause.USER_IGNORE), "CODE_RADIO_INTERNAL_ERROR");
            map.put(Integer.valueOf(CallFailCause.USER_DECLINE), "CODE_NETWORK_RESP_TIMEOUT");
            map.put(Integer.valueOf(CallFailCause.LOW_BATTERY), "CODE_NETWORK_REJECT");
            map.put(Integer.valueOf(CallFailCause.BLACKLISTED_CALL_ID), "CODE_RADIO_ACCESS_FAILURE");
            map.put(1506, "CODE_RADIO_LINK_FAILURE");
            map.put(1507, "CODE_RADIO_LINK_LOST");
            map.put(1508, "CODE_RADIO_UPLINK_FAILURE");
            map.put(1509, "CODE_RADIO_SETUP_FAILURE");
            map.put(Integer.valueOf(CallFailCause.USER_TERMINATED_BY_REMOTE), "CODE_RADIO_RELEASE_NORMAL");
            map.put(1511, "CODE_RADIO_RELEASE_ABNORMAL");
            map.put(1512, "CODE_ACCESS_CLASS_BLOCKED");
            map.put(1513, "CODE_NETWORK_DETACH");
            map.put(1514, "CODE_SIP_ALTERNATE_EMERGENCY_CALL");
            map.put(1515, "CODE_UNOBTAINABLE_NUMBER");
            map.put(1516, "CODE_NO_CSFB_IN_CS_ROAM");
            map.put(1600, "CODE_REJECT_UNKNOWN");
            map.put(1601, "CODE_REJECT_ONGOING_CALL_WAITING_DISABLED");
            map.put(1602, "CODE_REJECT_CALL_ON_OTHER_SUB");
            map.put(1603, "CODE_REJECT_1X_COLLISION");
            map.put(1604, "CODE_REJECT_SERVICE_NOT_REGISTERED");
            map.put(1605, "CODE_REJECT_CALL_TYPE_NOT_ALLOWED");
            map.put(1606, "CODE_REJECT_ONGOING_E911_CALL");
            map.put(1607, "CODE_REJECT_ONGOING_CALL_SETUP");
            map.put(1608, "CODE_REJECT_MAX_CALL_LIMIT_REACHED");
            map.put(1609, "CODE_REJECT_UNSUPPORTED_SIP_HEADERS");
            map.put(1610, "CODE_REJECT_UNSUPPORTED_SDP_HEADERS");
            map.put(1611, "CODE_REJECT_ONGOING_CALL_TRANSFER");
            map.put(1612, "CODE_REJECT_INTERNAL_ERROR");
            map.put(1613, "CODE_REJECT_QOS_FAILURE");
            map.put(1614, "CODE_REJECT_ONGOING_HANDOVER");
            map.put(1615, "CODE_REJECT_VT_TTY_NOT_ALLOWED");
            map.put(1616, "CODE_REJECT_ONGOING_CALL_UPGRADE");
            map.put(1617, "CODE_REJECT_CONFERENCE_TTY_NOT_ALLOWED");
            map.put(1618, "CODE_REJECT_ONGOING_CONFERENCE_CALL");
            map.put(1619, "CODE_REJECT_VT_AVPF_NOT_ALLOWED");
            map.put(1620, "CODE_REJECT_ONGOING_ENCRYPTED_CALL");
            map.put(1621, "CODE_REJECT_ONGOING_CS_CALL");
            map.put(1624, "CODE_NETWORK_CONGESTION");
            map.put(3001, "CODE_RETRY_ON_IMS_WITHOUT_RTT");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_1), "CODE_OEM_CAUSE_1");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_2), "CODE_OEM_CAUSE_2");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_3), "CODE_OEM_CAUSE_3");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_4), "CODE_OEM_CAUSE_4");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_5), "CODE_OEM_CAUSE_5");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_6), "CODE_OEM_CAUSE_6");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_7), "CODE_OEM_CAUSE_7");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_8), "CODE_OEM_CAUSE_8");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_9), "CODE_OEM_CAUSE_9");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_10), "CODE_OEM_CAUSE_10");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_11), "CODE_OEM_CAUSE_11");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_12), "CODE_OEM_CAUSE_12");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_13), "CODE_OEM_CAUSE_13");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_14), "CODE_OEM_CAUSE_14");
            map.put(Integer.valueOf(CallFailCause.OEM_CAUSE_15), "CODE_OEM_CAUSE_15");
        }
    }

    public class ServiceStateAnalytics extends TelephonyCallback implements TelephonyCallback.ServiceStateListener {
        private static final String TAG = ServiceStateAnalytics.class.getSimpleName();
        private final BroadcastReceiver mBroadcastReceiver;
        private final Executor mExecutor;
        private final AtomicReference mLastState = new AtomicReference(null);
        private TelephonyManager mTelephonyManager;

        private enum DeviceStatus {
            APM,
            CELLULAR_OOS_WITH_IWLAN,
            NO_NETWORK_COVERAGE,
            SIM_ABSENT,
            IN_SERVICE
        }

        protected ServiceStateAnalytics(Executor executor) {
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.analytics.TelephonyAnalytics.ServiceStateAnalytics.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    ServiceStateAnalytics.this.getTimeMillis();
                    if (intent.getAction().equals("android.telephony.action.SIM_CARD_STATE_CHANGED") && intent.getIntExtra("android.telephony.extra.SIM_STATE", 0) == 1) {
                        Rlog.d("AnkitSimAbsent", "Sim is Absent");
                        ServiceStateAnalytics.this.logSimAbsentState();
                    }
                }
            };
            this.mBroadcastReceiver = broadcastReceiver;
            this.mExecutor = executor;
            TelephonyAnalytics.this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.telephony.action.SIM_CARD_STATE_CHANGED"));
        }

        @Override // android.telephony.TelephonyCallback.ServiceStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            final int dataRegState = serviceState.getDataRegState();
            final int voiceRegState = serviceState.getVoiceRegState();
            final int rilVoiceRadioTechnology = serviceState.getRilVoiceRadioTechnology();
            final int rilDataRadioTechnology = serviceState.getRilDataRadioTechnology();
            TelephonyAnalytics.this.mExecutorService.execute(new Runnable() { // from class: com.android.internal.telephony.analytics.TelephonyAnalytics$ServiceStateAnalytics$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onServiceStateChanged$0(dataRegState, voiceRegState, rilVoiceRadioTechnology, rilDataRadioTechnology);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: logServiceState, reason: merged with bridge method [inline-methods] */
        public void lambda$onServiceStateChanged$0(int i, int i2, int i3, int i4) {
            long timeMillis = getTimeMillis();
            String strRilRadioTechnologyToString = ServiceState.rilRadioTechnologyToString(i3);
            String strRilRadioTechnologyToString2 = ServiceState.rilRadioTechnologyToString(i4);
            if (isAirplaneModeOn()) {
                if (i4 == 18 && i == 0) {
                    logOosWithIwlan(timeMillis);
                    return;
                } else {
                    logAirplaneModeServiceState(timeMillis);
                    return;
                }
            }
            if (i3 == 0 && i4 == 0) {
                logNoNetworkCoverage(timeMillis);
                return;
            }
            if (i3 != 0 && i4 == 0) {
                if (i2 == 0) {
                    logInServiceData(strRilRadioTechnologyToString, timeMillis);
                    return;
                } else {
                    logNoNetworkCoverage(timeMillis);
                    return;
                }
            }
            if (i3 != 0) {
                if (i == 0 || i2 == 0) {
                    logInServiceData(strRilRadioTechnologyToString, timeMillis);
                    return;
                } else {
                    logNoNetworkCoverage(timeMillis);
                    return;
                }
            }
            if (i != 0) {
                logNoNetworkCoverage(timeMillis);
            } else if (i4 == 18) {
                logOosWithIwlan(timeMillis);
            } else {
                logInServiceData(strRilRadioTechnologyToString2, timeMillis);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logSimAbsentState() {
            long timeMillis = getTimeMillis();
            setCurrentStateAndAddLastState(new TimeStampedServiceState(TelephonyAnalytics.this.mSlotIndex, "NA", DeviceStatus.SIM_ABSENT.name(), timeMillis), timeMillis);
        }

        private void logOosWithIwlan(long j) {
            setCurrentStateAndAddLastState(new TimeStampedServiceState(TelephonyAnalytics.this.mSlotIndex, "NA", DeviceStatus.CELLULAR_OOS_WITH_IWLAN.name(), j), j);
        }

        private void logAirplaneModeServiceState(long j) {
            setCurrentStateAndAddLastState(new TimeStampedServiceState(TelephonyAnalytics.this.mSlotIndex, "NA", DeviceStatus.APM.name(), j), j);
        }

        private void logNoNetworkCoverage(long j) {
            setCurrentStateAndAddLastState(new TimeStampedServiceState(TelephonyAnalytics.this.mSlotIndex, "NA", DeviceStatus.NO_NETWORK_COVERAGE.name(), j), j);
        }

        private void logInServiceData(String str, long j) {
            setCurrentStateAndAddLastState(new TimeStampedServiceState(TelephonyAnalytics.this.mSlotIndex, str, DeviceStatus.IN_SERVICE.name(), j), j);
        }

        private void setCurrentStateAndAddLastState(TimeStampedServiceState timeStampedServiceState, long j) {
            addData((TimeStampedServiceState) this.mLastState.getAndSet(timeStampedServiceState), j);
        }

        private void addData(TimeStampedServiceState timeStampedServiceState, long j) {
            if (timeStampedServiceState != null && j - timeStampedServiceState.mTimestampStart >= 10000) {
                Rlog.d(TAG, "Last State = " + timeStampedServiceState.toString() + "End = " + j);
                TelephonyAnalytics.this.mServiceStateAnalyticsProvider.insertDataToDb(timeStampedServiceState, j);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordCurrentStateBeforeDump() {
            long timeMillis = getTimeMillis();
            Rlog.d("RecordingStateBDump", "Recording " + timeMillis);
            TimeStampedServiceState timeStampedServiceState = (TimeStampedServiceState) this.mLastState.get();
            this.mLastState.set(createCopyWithUpdatedTimestamp(timeStampedServiceState));
            addData(timeStampedServiceState, timeMillis);
        }

        private TimeStampedServiceState createCopyWithUpdatedTimestamp(TimeStampedServiceState timeStampedServiceState) {
            if (timeStampedServiceState == null) {
                return null;
            }
            return new TimeStampedServiceState(timeStampedServiceState.mSlotIndex, timeStampedServiceState.mRAT, timeStampedServiceState.mDeviceStatus, getTimeMillis());
        }

        private boolean isAirplaneModeOn() {
            return Settings.Global.getInt(TelephonyAnalytics.this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0;
        }

        protected long getTimeMillis() {
            return SystemClock.elapsedRealtime();
        }

        void registerMyListener(Context context, int i) {
            try {
                TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
                this.mTelephonyManager = telephonyManagerCreateForSubscriptionId;
                telephonyManagerCreateForSubscriptionId.registerTelephonyCallback(this.mExecutor, this);
            } catch (NullPointerException e) {
                log("Null pointer exception caught " + e);
            }
        }

        void unregisterMyListener(int i) {
            this.mTelephonyManager.unregisterTelephonyCallback(this);
        }

        private void log(String str) {
            Rlog.d(ServiceStateAnalytics.class.getSimpleName(), str);
        }

        public static class TimeStampedServiceState {
            protected final String mDeviceStatus;
            protected final String mRAT;
            protected final int mSlotIndex;
            protected final long mTimestampStart;

            public TimeStampedServiceState(int i, String str, String str2, long j) {
                this.mSlotIndex = i;
                this.mRAT = str;
                this.mDeviceStatus = str2;
                this.mTimestampStart = j;
            }

            public String toString() {
                return "SlotIndex = " + this.mSlotIndex + " RAT = " + this.mRAT + " DeviceStatus = " + this.mDeviceStatus + "TimeStampStart = " + this.mTimestampStart;
            }

            public int getSlotIndex() {
                return this.mSlotIndex;
            }

            public long getTimestampStart() {
                return this.mTimestampStart;
            }

            public String getDeviceStatus() {
                return this.mDeviceStatus;
            }

            public String getRAT() {
                return this.mRAT;
            }
        }
    }

    public class SmsMmsAnalytics {
        private static final String TAG = "SmsMmsAnalytics";

        private static int getIncomingError(int i) {
            if (i == -1 || i == 1) {
                return 0;
            }
            if (i != 3) {
                return i != 4 ? 1 : 3;
            }
            return 2;
        }

        public SmsMmsAnalytics() {
        }

        public void onOutgoingSms(boolean z, int i) {
            Rlog.d(TAG, "Is Over Ims = " + z + " sendErrorCode = " + i + "SlotInfo =" + TelephonyAnalytics.this.mSlotIndex);
            logOutgoingSms(z, i);
        }

        public void onIncomingSmsSuccess(int i) {
            Rlog.d(TAG, " smsSource = " + i);
            logIncomingSms(i, "Success", "NA");
        }

        public void onDroppedIncomingMultipartSms() {
            sendDataToProvider("Failure", "SMS Incoming", ServiceState.rilRadioTechnologyToString(0), "INCOMING_SMS__ERROR__SMS_ERROR_GENERIC");
        }

        public void onIncomingSmsError(int i, int i2) {
            String incomingSmsErrorString = getIncomingSmsErrorString(i2);
            logIncomingSms(i, "Failure", incomingSmsErrorString);
            Rlog.d(TAG, " smsSource = " + i + "Result = " + i2 + "IncomingError = " + incomingSmsErrorString + "(" + getIncomingError(i2) + ")");
        }

        private void logOutgoingSms(boolean z, int i) {
            String str = i == 0 ? "Success" : "Failure";
            try {
                int rat = getRat(z);
                String networkTypeName = TelephonyManager.getNetworkTypeName(rat);
                String smsFailureReasonString = str.equals("Success") ? "NA" : getSmsFailureReasonString(i);
                Rlog.d(TAG, "SlotInfo = " + TelephonyAnalytics.this.mSlotIndex + " Type = SMS Outgoing Status = " + str + "RAT " + networkTypeName + " " + rat + "Failure Reason = " + smsFailureReasonString);
                sendDataToProvider(str, "SMS Outgoing", networkTypeName, smsFailureReasonString);
            } catch (Exception e) {
                Rlog.d(TAG, "Error in SmsLogs" + e);
            }
        }

        private void logIncomingSms(int i, String str, String str2) {
            try {
                int rat = getRat(i);
                String networkTypeName = TelephonyManager.getNetworkTypeName(rat);
                sendDataToProvider(str, "SMS Incoming", networkTypeName, str2);
                Rlog.d(TAG, "SlotInfo =" + TelephonyAnalytics.this.mSlotIndex + " Type = SMS Incoming Status = " + str + " RAT " + networkTypeName + " (" + rat + " ) Failure Reason = " + str2);
            } catch (Exception e) {
                Rlog.e(TAG, "Exception = " + e);
            }
        }

        private void sendDataToProvider(final String str, final String str2, final String str3, final String str4) {
            TelephonyAnalytics.this.mExecutorService.execute(new Runnable() { // from class: com.android.internal.telephony.analytics.TelephonyAnalytics$SmsMmsAnalytics$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendDataToProvider$0(str, str2, str3, str4);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendDataToProvider$0(String str, String str2, String str3, String str4) {
            TelephonyAnalytics.this.mSmsMmsAnalyticsProvider.insertDataToDb(str, str2, str3, str4);
        }

        private static String getIncomingSmsErrorString(int i) {
            if (i == -1 || i == 1) {
                return "INCOMING_SMS__ERROR__SMS_SUCCESS";
            }
            if (i == 3) {
                return "INCOMING_SMS__ERROR__SMS_ERROR_NO_MEMORY";
            }
            if (i == 4) {
                return "INCOMING_SMS__ERROR__SMS_ERROR_NOT_SUPPORTED";
            }
            return "INCOMING_SMS__ERROR__SMS_ERROR_GENERIC";
        }

        private ServiceState getServiceState() {
            Phone defaultPhone = TelephonyAnalytics.this.mPhone;
            if (defaultPhone.getPhoneType() == 5) {
                defaultPhone = TelephonyAnalytics.this.mPhone.getDefaultPhone();
            }
            ServiceStateTracker serviceStateTracker = defaultPhone.getServiceStateTracker();
            if (serviceStateTracker != null) {
                return serviceStateTracker.getServiceState();
            }
            return null;
        }

        private int getRat(int i) {
            if (i == 2) {
                return 0;
            }
            return getRat(i == 1);
        }

        private int getRat(boolean z) {
            if (z && TelephonyAnalytics.this.mPhone.getImsRegistrationTech() == 1) {
                return 18;
            }
            ServiceState serviceState = getServiceState();
            if (serviceState != null) {
                return serviceState.getVoiceNetworkType();
            }
            return 0;
        }

        private String getSmsFailureReasonString(int i) {
            switch (i) {
                case 0:
                    return "RESULT_ERROR_NONE";
                case 1:
                    return "RESULT_ERROR_GENERIC_FAILURE";
                case 2:
                    return "RESULT_ERROR_RADIO_OFF";
                case 3:
                    return "RESULT_ERROR_NULL_PDU";
                case 4:
                    return "RESULT_ERROR_NO_SERVICE";
                case 5:
                    return "RESULT_ERROR_LIMIT_EXCEEDED";
                case 6:
                    return "RESULT_ERROR_FDN_CHECK_FAILURE";
                case 7:
                    return "RESULT_ERROR_SHORT_CODE_NOT_ALLOWED";
                case 8:
                    return "RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED";
                case 9:
                    return "RESULT_RADIO_NOT_AVAILABLE";
                case 10:
                    return "RESULT_NETWORK_REJECT";
                case 11:
                    return "RESULT_INVALID_ARGUMENTS";
                case 12:
                    return "RESULT_INVALID_STATE";
                case 13:
                    return "RESULT_NO_MEMORY";
                case 14:
                    return "RESULT_INVALID_SMS_FORMAT";
                case 15:
                    return "RESULT_SYSTEM_ERROR";
                case 16:
                    return "RESULT_MODEM_ERROR";
                case 17:
                    return "RESULT_NETWORK_ERROR";
                default:
                    switch (i) {
                        case 19:
                            return "RESULT_INVALID_SMSC_ADDRESS";
                        case 20:
                            return "RESULT_OPERATION_NOT_ALLOWED";
                        case 21:
                            return "RESULT_INTERNAL_ERROR";
                        case 22:
                            return "RESULT_NO_RESOURCES";
                        case 23:
                            return "RESULT_CANCELLED";
                        case 24:
                            return "RESULT_REQUEST_NOT_SUPPORTED";
                        case 25:
                            return "RESULT_NO_BLUETOOTH_SERVICE";
                        case 26:
                            return "RESULT_INVALID_BLUETOOTH_ADDRESS";
                        case 27:
                            return "RESULT_BLUETOOTH_DISCONNECTED";
                        case 28:
                            return "RESULT_UNEXPECTED_EVENT_STOP_SENDING";
                        case 29:
                            return "RESULT_SMS_BLOCKED_DURING_EMERGENCY";
                        case 30:
                            return "RESULT_SMS_SEND_RETRY_FAILED";
                        case 31:
                            return "RESULT_REMOTE_EXCEPTION";
                        case 32:
                            return "RESULT_NO_DEFAULT_SMS_APP";
                        case 33:
                            return "RESULT_USER_NOT_ALLOWED";
                        default:
                            switch (i) {
                                case 100:
                                    return "RESULT_RIL_RADIO_NOT_AVAILABLE";
                                case 101:
                                    return "RESULT_RIL_SMS_SEND_FAIL_RETRY";
                                case 102:
                                    return "RESULT_RIL_NETWORK_REJECT";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                                    return "RESULT_RIL_INVALID_STATE";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                                    return "RESULT_RIL_INVALID_ARGUMENTS";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                                    return "RESULT_RIL_NO_MEMORY";
                                case 106:
                                    return "RESULT_RIL_REQUEST_RATE_LIMITED";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                                    return "RESULT_RIL_INVALID_SMS_FORMAT";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                                    return "RESULT_RIL_SYSTEM_ERR";
                                case 109:
                                    return "RESULT_RIL_ENCODING_ERR";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                                    return "RESULT_RIL_INVALID_SMSC_ADDRESS";
                                case 111:
                                    return "RESULT_RIL_MODEM_ERR";
                                case 112:
                                    return "RESULT_RIL_NETWORK_ERR";
                                case 113:
                                    return "RESULT_RIL_INTERNAL_ERR";
                                case 114:
                                    return "RESULT_RIL_REQUEST_NOT_SUPPORTED";
                                case 115:
                                    return "RESULT_RIL_INVALID_MODEM_STATE";
                                case 116:
                                    return "RESULT_RIL_NETWORK_NOT_READY";
                                case 117:
                                    return "RESULT_RIL_OPERATION_NOT_ALLOWED";
                                case 118:
                                    return "RESULT_RIL_NO_RESOURCES";
                                case 119:
                                    return "RESULT_RIL_CANCELLED";
                                case 120:
                                    return "RESULT_RIL_SIM_ABSENT";
                                case 121:
                                    return "RESULT_RIL_SIMULTANEOUS_SMS_AND_CALL_NOT_ALLOWED";
                                case 122:
                                    return "RESULT_RIL_ACCESS_BARRED";
                                case 123:
                                    return "RESULT_RIL_BLOCKED_DUE_TO_CALL";
                                case 124:
                                    return "RESULT_RIL_GENERIC_ERROR";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_SIM_AUTHENTICATION /* 125 */:
                                    return "RESULT_RIL_INVALID_RESPONSE";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_DC_RT_INFO /* 126 */:
                                    return "RESULT_RIL_SIM_PIN2";
                                case 127:
                                    return "RESULT_RIL_SIM_PUK2";
                                case 128:
                                    return "RESULT_RIL_SUBSCRIPTION_NOT_AVAILABLE";
                                case 129:
                                    return "RESULT_RIL_SIM_ERROR";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY /* 130 */:
                                    return "RESULT_RIL_INVALID_SIM_STATE";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY /* 131 */:
                                    return "RESULT_RIL_NO_SMS_TO_ACK";
                                case 132:
                                    return "RESULT_RIL_SIM_BUSY";
                                case 133:
                                    return "RESULT_RIL_SIM_FULL";
                                case 134:
                                    return "RESULT_RIL_NO_SUBSCRIPTION";
                                case 135:
                                    return "RESULT_RIL_NO_NETWORK_FOUND";
                                case 136:
                                    return "RESULT_RIL_DEVICE_IN_USE";
                                case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ALLOWED_CARRIERS /* 137 */:
                                    return "RESULT_RIL_ABORTED";
                                default:
                                    return "NA";
                            }
                    }
            }
        }
    }
}
