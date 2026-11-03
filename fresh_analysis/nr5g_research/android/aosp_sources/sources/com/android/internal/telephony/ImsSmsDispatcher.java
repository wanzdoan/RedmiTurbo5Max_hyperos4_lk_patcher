package com.android.internal.telephony;

import android.content.Context;
import android.os.Binder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.SmsMessage;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.feature.MmTelFeature;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ImsSmsDispatcher extends SMSDispatcher {
    public static final int MAX_SEND_RETRIES_OVER_IMS = 3;
    private ImsMmTelManager.CapabilityCallback mCapabilityCallback;
    private Runnable mConnectRunnable;
    private FeatureConnectorFactory mConnectorFactory;
    private ImsManager mImsManager;
    private final FeatureConnector mImsManagerConnector;
    private final IImsSmsListener mImsSmsListener;
    private volatile boolean mIsImsServiceUp;
    private volatile boolean mIsRegistered;
    protected volatile boolean mIsSmsCapable;
    private final Object mLock;
    public List<Integer> mMemoryAvailableNotifierList;
    public AtomicInteger mNextToken;
    private RegistrationManager.RegistrationCallback mRegistrationCallback;
    public Map<Integer, SMSDispatcher.SmsTracker> mTrackers;

    public interface FeatureConnectorFactory {
        FeatureConnector<ImsManager> create(Context context, int i, String str, FeatureConnector.Listener<ImsManager> listener, Executor executor);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean shouldBlockSmsForEcbm() {
        return false;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ImsSmsDispatcher$4, reason: invalid class name */
    class AnonymousClass4 extends IImsSmsListener.Stub {
        AnonymousClass4() {
        }

        public void onMemoryAvailableResult(int i, int i2, int i3) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ImsSmsDispatcher.this.logd("onMemoryAvailableResult token=" + i + " status=" + i2 + " networkReasonCode=" + i3);
                boolean zContains = ImsSmsDispatcher.this.mMemoryAvailableNotifierList.contains(Integer.valueOf(i));
                ImsSmsDispatcher imsSmsDispatcher = ImsSmsDispatcher.this;
                if (!zContains) {
                    imsSmsDispatcher.loge("onMemoryAvailableResult Invalid token");
                    return;
                }
                imsSmsDispatcher.mMemoryAvailableNotifierList.remove(Integer.valueOf(i));
                ImsSmsDispatcher imsSmsDispatcher2 = ImsSmsDispatcher.this;
                if (i2 == 3 && !imsSmsDispatcher2.mRPSmmaRetried) {
                    imsSmsDispatcher2.sendMessageDelayed(imsSmsDispatcher2.obtainMessage(11), 2000L);
                    ImsSmsDispatcher.this.mRPSmmaRetried = true;
                } else {
                    imsSmsDispatcher2.mRPSmmaRetried = false;
                }
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        public void onSendSmsResult(int i, int i2, int i3, int i4, int i5) {
            TelephonyAnalytics telephonyAnalytics;
            TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
            int i6;
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ImsSmsDispatcher.this.logd("onSendSmsResult token=" + i + " messageRef=" + i2 + " status=" + i3 + " reason=" + i4 + " networkReasonCode=" + i5);
                SMSDispatcher.SmsTracker smsTracker = ImsSmsDispatcher.this.mTrackers.get(Integer.valueOf(i));
                if (smsTracker == null) {
                    throw new IllegalArgumentException("Invalid token.");
                }
                smsTracker.mMessageRef = i2;
                if (i3 == 1) {
                    if (smsTracker.mDeliveryIntent != null) {
                        ImsSmsDispatcher.this.mSmsDispatchersController.putDeliveryPendingTracker(smsTracker);
                    }
                    MiuiSmsControllerStub.IMiuiSmsController iMiuiSmsController = MiuiSmsControllerStub.get(ImsSmsDispatcher.this.mPhone.getPhoneId());
                    ImsSmsDispatcher imsSmsDispatcher = ImsSmsDispatcher.this;
                    iMiuiSmsController.miuiSmsSendOptimizationStatus(imsSmsDispatcher.mPhone, smsTracker, imsSmsDispatcher.mTrackers, imsSmsDispatcher.mSmsDispatchersController.getSmscAddress(), true, true, i4, ImsSmsDispatcher.this.mSmsDispatchersController);
                    smsTracker.onSent(ImsSmsDispatcher.this.mContext);
                    ImsSmsDispatcher.this.mTrackers.remove(Integer.valueOf(i));
                    ImsSmsDispatcher.this.mPhone.notifySmsSent(smsTracker.mDestAddress);
                    ImsSmsDispatcher.this.mSmsDispatchersController.notifySmsSent(smsTracker, true, smsTracker.isSinglePartOrLastPart(), true);
                } else if (i3 == 2) {
                    MiuiSmsControllerStub.IMiuiSmsController iMiuiSmsController2 = MiuiSmsControllerStub.get(ImsSmsDispatcher.this.mPhone.getPhoneId());
                    ImsSmsDispatcher imsSmsDispatcher2 = ImsSmsDispatcher.this;
                    if (iMiuiSmsController2.miuiSmsSendOptimizationStatus(imsSmsDispatcher2.mPhone, smsTracker, imsSmsDispatcher2.mTrackers, imsSmsDispatcher2.mSmsDispatchersController.getSmscAddress(), true, false, i4, ImsSmsDispatcher.this.mSmsDispatchersController) != 1) {
                        smsTracker.onFailed(ImsSmsDispatcher.this.mContext, i4, i5);
                        ImsSmsDispatcher.this.mTrackers.remove(Integer.valueOf(i));
                        ImsSmsDispatcher.this.notifySmsSentFailedToEmergencyStateTracker(smsTracker, true);
                    }
                } else if (i3 == 3) {
                    int maxRetryCountOverIms = ImsSmsDispatcher.this.getMaxRetryCountOverIms();
                    int i7 = smsTracker.mRetryCount;
                    int maxSmsRetryCount = ImsSmsDispatcher.this.getMaxSmsRetryCount();
                    ImsSmsDispatcher imsSmsDispatcher3 = ImsSmsDispatcher.this;
                    if (i7 < maxSmsRetryCount) {
                        if (maxRetryCountOverIms < imsSmsDispatcher3.getMaxSmsRetryCount() && (i6 = smsTracker.mRetryCount) >= maxRetryCountOverIms) {
                            smsTracker.mRetryCount = i6 + 1;
                            ImsSmsDispatcher.this.mTrackers.remove(Integer.valueOf(i));
                            ImsSmsDispatcher.this.fallbackToPstn(smsTracker);
                        } else {
                            smsTracker.mRetryCount++;
                            ImsSmsDispatcher imsSmsDispatcher4 = ImsSmsDispatcher.this;
                            imsSmsDispatcher4.sendMessageDelayed(imsSmsDispatcher4.obtainMessage(3, smsTracker), ImsSmsDispatcher.this.getSmsRetryDelayValue());
                        }
                    } else {
                        MiuiSmsControllerStub.IMiuiSmsController iMiuiSmsController3 = MiuiSmsControllerStub.get(imsSmsDispatcher3.mPhone.getPhoneId());
                        ImsSmsDispatcher imsSmsDispatcher5 = ImsSmsDispatcher.this;
                        if (iMiuiSmsController3.miuiSmsSendOptimizationStatus(imsSmsDispatcher5.mPhone, smsTracker, imsSmsDispatcher5.mTrackers, imsSmsDispatcher5.mSmsDispatchersController.getSmscAddress(), true, false, i4, ImsSmsDispatcher.this.mSmsDispatchersController) != 1) {
                            smsTracker.onFailed(ImsSmsDispatcher.this.mContext, i4, i5);
                            ImsSmsDispatcher.this.mTrackers.remove(Integer.valueOf(i));
                            ImsSmsDispatcher.this.notifySmsSentFailedToEmergencyStateTracker(smsTracker, true);
                        }
                    }
                } else if (i3 == 4) {
                    smsTracker.mRetryCount++;
                    ImsSmsDispatcher.this.mTrackers.remove(Integer.valueOf(i));
                    ImsSmsDispatcher.this.fallbackToPstn(smsTracker);
                }
                ImsSmsDispatcher.this.mPhone.getSmsStats().onOutgoingSms(true, "3gpp2".equals(ImsSmsDispatcher.this.getFormat()), i3 == 4, i4, i5, smsTracker.mMessageId, smsTracker.isFromDefaultSmsApplication(ImsSmsDispatcher.this.mContext), smsTracker.getInterval(), ImsSmsDispatcher.this.mTelephonyManager.isEmergencyNumber(smsTracker.mDestAddress), smsTracker.isMtSmsPollingMessage(ImsSmsDispatcher.this.mContext), smsTracker.getPduLength(), smsTracker.getAppPackageName(), smsTracker.getAppUid());
                Phone phone = ImsSmsDispatcher.this.mPhone;
                if (phone != null && (telephonyAnalytics = phone.getTelephonyAnalytics()) != null && (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) != null) {
                    smsMmsAnalytics.onOutgoingSms(true, i4);
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }

        public void onSmsStatusReportReceived(int i, String str, byte[] bArr) throws RemoteException {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ImsSmsDispatcher.this.logd("Status report received.");
                SmsMessage smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArr, str);
                if (smsMessageCreateFromPdu == null || smsMessageCreateFromPdu.mWrappedSmsMessage == null) {
                    throw new RemoteException("Status report received with a PDU that could not be parsed.");
                }
                ImsSmsDispatcher.this.mSmsDispatchersController.handleSmsStatusReport(str, bArr);
                try {
                    ImsSmsDispatcher.this.getImsManager().acknowledgeSmsReport(i, smsMessageCreateFromPdu.mWrappedSmsMessage.mMessageRef, 1);
                } catch (ImsException e) {
                    ImsSmsDispatcher.this.loge("Failed to acknowledgeSmsReport(). Error: " + e.getMessage());
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }

        public void onSmsReceived(final int i, String str, byte[] bArr) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ImsSmsDispatcher.this.logd("SMS received.");
                final SmsMessage smsMessageCreateFromPdu = SmsMessage.createFromPdu(bArr, str);
                ImsSmsDispatcher.this.mSmsDispatchersController.injectSmsPdu(smsMessageCreateFromPdu, str, new SmsDispatchersController.SmsInjectionCallback() { // from class: com.android.internal.telephony.ImsSmsDispatcher$4$$ExternalSyntheticLambda0
                    @Override // com.android.internal.telephony.SmsDispatchersController.SmsInjectionCallback
                    public final void onSmsInjectedResult(int i2) {
                        this.f$0.lambda$onSmsReceived$0(smsMessageCreateFromPdu, i, i2);
                    }
                }, true, true, i);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSmsReceived$0(SmsMessage smsMessage, int i, int i2) {
            ImsSmsDispatcher.this.logd("SMS handled result: " + i2);
            if (i2 != -1) {
                int i3 = 1;
                if (i2 != 1) {
                    i3 = 3;
                    if (i2 != 3) {
                        i3 = 4;
                        if (i2 != 4) {
                            i3 = 2;
                        }
                    }
                } else if (smsMessage != null) {
                    ImsSmsDispatcher.this.mSmsDispatchersController.notifySmsReceivedViaImsToEmergencyStateTracker(smsMessage.getOriginatingAddress());
                }
                if (smsMessage != null) {
                    try {
                        if (smsMessage.mWrappedSmsMessage != null) {
                            ImsSmsDispatcher.this.getImsManager().acknowledgeSms(i, smsMessage.mWrappedSmsMessage.mMessageRef, i3);
                            return;
                        }
                    } catch (ImsException e) {
                        ImsSmsDispatcher.this.loge("Failed to acknowledgeSms(). Error: " + e.getMessage());
                        return;
                    }
                }
                ImsSmsDispatcher.this.logw("SMS Received with a PDU that could not be parsed.");
                ImsSmsDispatcher.this.getImsManager().acknowledgeSms(i, 0, i3);
            }
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher, android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 3) {
            logd("SMS retry..");
            sendSms((SMSDispatcher.SmsTracker) message.obj);
        } else if (i == 11) {
            logd("SMMA Notification retry..");
            onMemoryAvailable();
        } else {
            super.handleMessage(message);
        }
    }

    public ImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureConnectorFactory featureConnectorFactory, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, featureFlags);
        this.mMemoryAvailableNotifierList = new ArrayList();
        this.mTrackers = new ConcurrentHashMap();
        this.mNextToken = new AtomicInteger();
        this.mLock = new Object();
        this.mConnectRunnable = new Runnable() { // from class: com.android.internal.telephony.ImsSmsDispatcher.1
            @Override // java.lang.Runnable
            public void run() {
                ImsSmsDispatcher.this.mImsManagerConnector.connect();
            }
        };
        this.mRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.internal.telephony.ImsSmsDispatcher.2
            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistered(int i) {
                ImsSmsDispatcher.this.logd("onImsConnected imsRadioTech=" + i);
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mIsRegistered = true;
                }
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistering(int i) {
                ImsSmsDispatcher.this.logd("onImsProgressing imsRadioTech=" + i);
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mIsRegistered = false;
                }
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                ImsSmsDispatcher.this.logd("onImsDisconnected imsReasonInfo=" + imsReasonInfo);
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mIsRegistered = false;
                }
            }
        };
        this.mCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.android.internal.telephony.ImsSmsDispatcher.3
            @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
            public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mIsSmsCapable = mmTelCapabilities.isCapable(8);
                }
            }
        };
        this.mImsSmsListener = new AnonymousClass4();
        this.mConnectorFactory = featureConnectorFactory;
        this.mImsManagerConnector = featureConnectorFactory.create(this.mContext, this.mPhone.getPhoneId(), "ImsSmsDispatcher", new FeatureConnector.Listener<ImsManager>() { // from class: com.android.internal.telephony.ImsSmsDispatcher.5
            public void connectionReady(ImsManager imsManager, int i) throws ImsException {
                ImsSmsDispatcher.this.logd("ImsManager: connection ready.");
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mImsManager = imsManager;
                    ImsSmsDispatcher.this.setListeners();
                    ImsSmsDispatcher.this.mIsImsServiceUp = true;
                    ImsSmsDispatcher imsSmsDispatcher = ImsSmsDispatcher.this;
                    imsSmsDispatcher.mSmsDispatchersController.setImsManager(imsSmsDispatcher.mImsManager);
                }
            }

            public void connectionUnavailable(int i) {
                ImsSmsDispatcher.this.logd("ImsManager: connection unavailable, reason=" + i);
                if (i == 3) {
                    ImsSmsDispatcher.this.loge("connectionUnavailable: unexpected, received server error");
                    ImsSmsDispatcher imsSmsDispatcher = ImsSmsDispatcher.this;
                    imsSmsDispatcher.removeCallbacks(imsSmsDispatcher.mConnectRunnable);
                    ImsSmsDispatcher imsSmsDispatcher2 = ImsSmsDispatcher.this;
                    imsSmsDispatcher2.postDelayed(imsSmsDispatcher2.mConnectRunnable, 5000L);
                }
                synchronized (ImsSmsDispatcher.this.mLock) {
                    ImsSmsDispatcher.this.mImsManager = null;
                    ImsSmsDispatcher.this.mIsImsServiceUp = false;
                    ImsSmsDispatcher.this.mSmsDispatchersController.setImsManager(null);
                }
            }
        }, new ImsSmsDispatcher$$ExternalSyntheticLambda0(this));
        post(this.mConnectRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListeners() {
        getImsManager().addRegistrationCallback(this.mRegistrationCallback, new ImsSmsDispatcher$$ExternalSyntheticLambda0(this));
        getImsManager().addCapabilitiesCallback(this.mCapabilityCallback, new ImsSmsDispatcher$$ExternalSyntheticLambda0(this));
        getImsManager().setSmsListener(getSmsListener());
        getImsManager().onSmsReady();
    }

    private boolean isLteService() {
        return this.mPhone.getServiceState().getRilDataRadioTechnology() == 14 && this.mPhone.getServiceState().getDataRegistrationState() == 0;
    }

    private boolean isLimitedLteService() {
        return this.mPhone.getServiceState().getRilVoiceRadioTechnology() == 14 && this.mPhone.getServiceState().isEmergencyOnly();
    }

    protected boolean isEmergencySmsPossible() {
        return isLteService() || isLimitedLteService();
    }

    public boolean isEmergencySmsSupport(String str) {
        boolean z = false;
        if (!this.mTelephonyManager.isEmergencyNumber(str)) {
            logi(Rlog.pii("ImsSmsDispatcher", str) + " is not emergency number");
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
            if (carrierConfigManager == null) {
                loge("configManager is null");
                return false;
            }
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(getSubId());
            if (configForSubId == null) {
                loge("PersistableBundle is null");
                return false;
            }
            boolean z2 = configForSubId.getBoolean("support_emergency_sms_over_ims_bool");
            boolean zIsEmergencySmsPossible = isEmergencySmsPossible();
            logi("isEmergencySmsSupport emergencySmsCarrierSupport: " + z2 + " destAddr: " + Rlog.pii("ImsSmsDispatcher", str) + " mIsImsServiceUp: " + this.mIsImsServiceUp + " lteOrLimitedLte: " + zIsEmergencySmsPossible);
            if (z2 && this.mIsImsServiceUp && zIsEmergencySmsPossible) {
                z = true;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isAvailable() {
        boolean z;
        synchronized (this.mLock) {
            try {
                logd("isAvailable: up=" + this.mIsImsServiceUp + ", reg= " + this.mIsRegistered + ", cap= " + this.mIsSmsCapable);
                z = this.mIsImsServiceUp && this.mIsRegistered && this.mIsSmsCapable;
            } catch (Throwable th) {
                throw th;
            }
        }
        return z;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public String getFormat() {
        if (this.mLock == null) {
            return "unknown";
        }
        try {
            return getImsManager().getSmsFormat();
        } catch (ImsException e) {
            loge("Failed to get sms format. Error: " + e.getMessage());
            return "unknown";
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public int getMaxSmsRetryCount() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        int i = (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(getSubId())) == null) ? 3 : configForSubId.getInt("imssms.sms_max_retry_count_int");
        Rlog.d("ImsSmsDispatcher", "Retry Count: " + i);
        return i;
    }

    public int getMaxRetryCountOverIms() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        int i = (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(getSubId())) == null) ? 3 : configForSubId.getInt("imssms.sms_max_retry_over_ims_count_int");
        Rlog.d("ImsSmsDispatcher", "Retry Count Over Ims: " + i);
        return i;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public int getSmsRetryDelayValue() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        int i = (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(getSubId())) == null) ? 2000 : configForSubId.getInt("imssms.sms_over_ims_send_retry_delay_millis_int");
        Rlog.d("ImsSmsDispatcher", "Retry delay: " + i);
        return i;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2) {
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, str3, z, smsHeader, i, i2);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z) {
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, i, bArr, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2, int i3) {
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, str3, z, smsHeader, i, i2, i3);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z, int i2) {
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, i, bArr, z, i2);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence charSequence, boolean z) {
        return SMSDispatcherUtil.calculateLength(isCdmaMo(), charSequence, z);
    }

    public void onMemoryAvailable() {
        logd("onMemoryAvailable ");
        int iIncrementAndGet = this.mNextToken.incrementAndGet();
        try {
            logd("onMemoryAvailable: token = " + iIncrementAndGet);
            this.mMemoryAvailableNotifierList.add(Integer.valueOf(iIncrementAndGet));
            getImsManager().onMemoryAvailable(iIncrementAndGet);
        } catch (ImsException e) {
            loge("onMemoryAvailable failed: " + e.getMessage());
            if (this.mMemoryAvailableNotifierList.contains(Integer.valueOf(iIncrementAndGet))) {
                this.mMemoryAvailableNotifierList.remove(Integer.valueOf(iIncrementAndGet));
            }
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public void sendSms(SMSDispatcher.SmsTracker smsTracker) {
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        logd("sendSms:  mRetryCount=" + smsTracker.mRetryCount + " mMessageRef=" + smsTracker.mMessageRef + " SS=" + this.mPhone.getServiceState().getState());
        smsTracker.mUsesImsServiceForIms = true;
        HashMap<String, Object> data = smsTracker.getData();
        byte[] bArr = (byte[]) data.get("pdu");
        byte[] bArr2 = (byte[]) data.get("smsc");
        boolean z = smsTracker.mRetryCount > 0;
        String format = getFormat();
        if ("3gpp".equals(format) && z) {
            byte b = bArr[0];
            if ((b & 1) == 1) {
                bArr[0] = (byte) (b | 4);
                bArr[1] = (byte) smsTracker.mMessageRef;
            }
        }
        int iIncrementAndGet = this.mNextToken.incrementAndGet();
        this.mTrackers.put(Integer.valueOf(iIncrementAndGet), smsTracker);
        try {
            getImsManager().sendSms(iIncrementAndGet, smsTracker.mMessageRef, format, bArr2 != null ? IccUtils.bytesToHexString(bArr2) : null, z, bArr);
        } catch (ImsException e) {
            loge("sendSms failed. Falling back to PSTN. Error: " + e.getMessage());
            this.mTrackers.remove(Integer.valueOf(iIncrementAndGet));
            fallbackToPstn(smsTracker);
            this.mPhone.getSmsStats().onOutgoingSms(true, "3gpp2".equals(format), true, 15, smsTracker.mMessageId, smsTracker.isFromDefaultSmsApplication(this.mContext), smsTracker.getInterval(), this.mTelephonyManager.isEmergencyNumber(smsTracker.mDestAddress), smsTracker.isMtSmsPollingMessage(this.mContext), smsTracker.getPduLength(), smsTracker.getAppPackageName(), smsTracker.getAppUid());
            Phone phone = this.mPhone;
            if (phone == null || (telephonyAnalytics = phone.getTelephonyAnalytics()) == null || (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) == null) {
                return;
            }
            smsMmsAnalytics.onOutgoingSms(true, 15);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ImsManager getImsManager() {
        ImsManager imsManager;
        synchronized (this.mLock) {
            try {
                imsManager = this.mImsManager;
                if (imsManager == null) {
                    throw new ImsException("ImsManager not up", 106);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return imsManager;
    }

    public void fallbackToPstn(SMSDispatcher.SmsTracker smsTracker) {
        this.mSmsDispatchersController.sendRetrySms(smsTracker);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean isCdmaMo() {
        return this.mSmsDispatchersController.isCdmaFormat(getFormat());
    }

    public IImsSmsListener getSmsListener() {
        return this.mImsSmsListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d("ImsSmsDispatcher [" + getPhoneId(this.mPhone) + "]", str);
    }

    private void logi(String str) {
        Rlog.i("ImsSmsDispatcher [" + getPhoneId(this.mPhone) + "]", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        Rlog.w("ImsSmsDispatcher [" + getPhoneId(this.mPhone) + "]", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e("ImsSmsDispatcher [" + getPhoneId(this.mPhone) + "]", str);
    }

    private static String getPhoneId(Phone phone) {
        return phone != null ? Integer.toString(phone.getPhoneId()) : "?";
    }
}
