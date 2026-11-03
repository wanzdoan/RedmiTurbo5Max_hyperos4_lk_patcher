package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.AccessNetworkConstants;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.SubscriptionManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.emergency.EmergencyNumber;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.ntndata.TestDataCallResult;
import com.android.internal.telephony.ntnphone.NtnAntConstants;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.ntnphone.NtnRILUtils;
import com.android.internal.telephony.utils.NtnBoxOptimization;
import com.android.telephony.Rlog;
import vendor.xiaomi.hardware.satellite.Dial;
import vendor.xiaomi.hardware.satellite.GsmSmsMessage;
import vendor.xiaomi.hardware.satellite.INTNRadio;
import vendor.xiaomi.hardware.satellite.RadioResponseInfo;
import vendor.xiaomi.hardware.satellite.UusInfo;
import vendor.xiaomi.hardware.satellite.VoiceRegStateResult;

/* JADX INFO: loaded from: classes.dex */
public class NtnRIL extends RIL {
    public static final int MAX_SERVICE_IDX = 8;
    protected Registrant mAntInfoSignalStrengthRegistrant;
    protected RegistrantList mChannelStateRegistrantList;
    protected Registrant mDataCallListChangedRegistrants;
    private IBinder.DeathRecipient mDeathRecipient;
    protected Registrant mGpsMinRangeRegistrant;
    protected Registrant mGpsRegistrant;
    protected Registrant mHighPenetrationRegistrant;
    protected Registrant mHlserrorReportRegistrant;
    protected Registrant mImeiQueryRegistrant;
    protected Registrant mImsiQueryRegistrant;
    private RIL mModemRil;
    private HalVersion mNtnHalVersion;
    private NtnIndication mNtnIndication;
    protected Registrant mNtnPosInfoRegistrant;
    private volatile NtnRadioProxy mNtnRadioProxy;
    private NtnResponse mNtnResponse;
    private final NtnRilHandler mNtnRilHandler;
    protected Registrant mNtnSmsRegistrant;
    protected Registrant mNtnSmsSendResultRegistrant;
    protected Registrant mNtnStatusReportRegistrant;
    protected RegistrantList mOtaStateChangedRegistrants;
    protected Registrant mSimAutoRegistrant;
    protected Registrant mSnrRegistrant;
    protected Registrant mTtlogInfoRegistrant;
    protected Registrant mTxPwrRegistrant;
    protected RegistrantList mUnsolNtnCdcMsgRegistrants;
    protected Registrant mVoiceControlRegistrant;
    public static final HalVersion NTN_RADIO_HAL_VERSION_UNSUPPORTED = HalVersion.UNSUPPORTED;
    public static final HalVersion NTN_RADIO_HAL_VERSION_UNKNOWN = HalVersion.UNKNOWN;
    public static final HalVersion NTN_RADIO_HAL_VERSION_1_0 = new HalVersion(1, 0);
    public static final HalVersion NTN_RADIO_HAL_VERSION_2_0 = new HalVersion(2, 0);
    public static final HalVersion NTN_RADIO_HAL_VERSION_3_0 = new HalVersion(3, 0);

    private class NtnRilHandler extends Handler {
        public NtnRilHandler() {
            super(Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            NtnRIL.this.ntnRiljLog("handleMessage " + i);
            if (i != 100) {
                return;
            }
            NtnRIL.this.shutDownNtnProxyAndRequestList();
        }
    }

    @Override // com.android.internal.telephony.RIL
    protected synchronized void resetProxyAndRequestList(int i) {
        if (i == 8) {
            this.mNtnRadioProxy = null;
            getNtnRadioProxy(null);
        }
    }

    public NtnRIL(Context context, RIL ril) {
        super(context, ril.mAllowedNetworkTypesBitmask, ril.mPhoneId, new FeatureFlagsImpl());
        this.mNtnRadioProxy = null;
        this.mChannelStateRegistrantList = new RegistrantList();
        this.mOtaStateChangedRegistrants = new RegistrantList();
        this.mUnsolNtnCdcMsgRegistrants = new RegistrantList();
        this.mNtnHalVersion = NTN_RADIO_HAL_VERSION_UNKNOWN;
        this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.internal.telephony.NtnRIL.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                NtnRIL.this.ntnRiljLoge("NTN HAL binderDied");
                if (NtnRIL.this.mNtnRadioProxy == null || NtnRIL.this.mNtnRadioProxy.getAidl() == null) {
                    return;
                }
                NtnRIL.this.mNtnRadioProxy.getAidl().asBinder().unlinkToDeath(this, 0);
                if (NtnRIL.this.mNtnRilHandler != null) {
                    NtnRIL.this.mNtnRilHandler.sendEmptyMessage(100);
                }
            }
        };
        this.mModemRil = ril;
        this.mNtnRilHandler = new NtnRilHandler();
        ntnRiljLog("NtnRIL init success;");
    }

    private RIL getModemRil() {
        return (RIL) PhoneFactory.getPhone(this.mPhoneId.intValue()).mCi;
    }

    @Override // com.android.internal.telephony.RIL
    public synchronized RadioServiceProxy getRadioServiceProxy(int i) {
        return getModemRil().getRadioServiceProxy(i);
    }

    @Override // com.android.internal.telephony.RIL
    public <T extends RadioServiceProxy> T getRadioServiceProxy(Class<T> cls) {
        return (T) getModemRil().getRadioServiceProxy(cls);
    }

    public synchronized NtnRadioProxy getNtnRadioProxy(Message message) {
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId.intValue())) {
            return null;
        }
        if (this.mNtnRadioProxy != null) {
            return this.mNtnRadioProxy;
        }
        try {
            IBinder iBinderWaitForDeclaredService = ServiceManager.waitForDeclaredService(INTNRadio.DESCRIPTOR + "/satellite_slot1");
            if (iBinderWaitForDeclaredService != null) {
                ntnRiljLog("getNtnRadioProxy: binder = " + iBinderWaitForDeclaredService);
                this.mNtnRadioProxy = new NtnRadioProxy();
                this.mNtnHalVersion = this.mNtnRadioProxy.setAidl(INTNRadio.Stub.asInterface(iBinderWaitForDeclaredService));
                this.mNtnIndication = new NtnIndication(this);
                this.mNtnResponse = new NtnResponse(this);
                this.mNtnRadioProxy.getAidl().asBinder().linkToDeath(this.mDeathRecipient, 0);
                this.mNtnRadioProxy.getAidl().setResponseFunctions(this.mNtnResponse, this.mNtnIndication);
            } else {
                ntnRiljLoge("getNtnRadioProxy: mNtnRadioProxy for satellite is disabled");
            }
        } catch (RemoteException e) {
            this.mNtnRadioProxy = null;
            ntnRiljLoge("RadioProxy getService/setResponseFunctions: " + e);
        }
        if (this.mNtnRadioProxy == null) {
            ntnRiljLoge("getNtnRadioProxy: mRadioProxy == null");
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(1));
                message.sendToTarget();
            }
        }
        return this.mNtnRadioProxy;
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void setRadioPower(boolean z, boolean z2, boolean z3, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(23, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " on = " + z + " forEmergencyCall= " + z2 + " preferredForEmergencyCall=" + z3);
            try {
                ntnRadioProxy.setRadioPower(rILRequestObtainRequest.mSerial, z, z2, z3);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setRadioPower", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getSignalStrength(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(19, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getSignalStrength(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getSignalStrength", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getHardwareConfig(Message message) {
        ntnRiljLog("getHardwareConfig: not support satellite");
        if (message != null) {
            AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
            message.sendToTarget();
        }
    }

    public void setSignalStrengthReportingCriteria(int i, int[] iArr, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setSignalStrengthReportingCriteria(rILRequestObtainRequest.mSerial, i, iArr);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setSignalStrengthReportingCriteria", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getVoiceRegistrationState(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(20, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getVoiceRegistrationState(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getVoiceRegistrationState", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void dial(final String str, final boolean z, final EmergencyNumber emergencyNumber, final boolean z2, final int i, final Message message) {
        if (MiuiTelephonyManagerStub.isSatelliteBox()) {
            if (NtnBoxOptimization.getInstance() != null) {
                NtnBoxOptimization.getInstance().removeSceneOffMessage();
                if (NtnBoxOptimization.getInstance().getLowPowerState()) {
                    setScreenState(1, null);
                    NtnBoxOptimization.getInstance().setLowPowerState(false);
                }
            }
            this.mNtnRilHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.NtnRIL$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dial$0(str, z, emergencyNumber, z2, i, message);
                }
            }, 500L);
            return;
        }
        lambda$dial$0(str, z, emergencyNumber, z2, i, message);
    }

    /* JADX INFO: renamed from: dialBase, reason: merged with bridge method [inline-methods] */
    public void lambda$dial$0(String str, boolean z, EmergencyNumber emergencyNumber, boolean z2, int i, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(10, message, this.mRILDefaultWorkSource);
            Dial dial = new Dial();
            dial.address = NtnRILUtils.convertNullToEmptyString(str);
            dial.clir = i;
            dial.uusInfo = new UusInfo[0];
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.dial(rILRequestObtainRequest.mSerial, dial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "dial", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getCurrentCalls(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(9, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getCurrentCalls(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getCurrentCalls", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void hangupConnection(int i, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(12, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " gsmIndex = " + i);
            try {
                ntnRadioProxy.hangup(rILRequestObtainRequest.mSerial, i);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "hangupConnection", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void rejectCall(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(17, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.rejectCall(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "rejectCall", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getLastCallFailCause(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(18, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getLastCallFailCause(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getLastCallFailCause", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void acceptCall(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(40, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.acceptCall(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "acceptCall", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void sendDtmf(char c, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(24, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.sendDtmf(rILRequestObtainRequest.mSerial, c + "");
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "sendDtmf", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void startDtmf(char c, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(49, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.startDtmf(rILRequestObtainRequest.mSerial, c + "");
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "startDtmf", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void stopDtmf(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(50, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.stopDtmf(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "stopDtmf", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void sendBurstDtmf(String str, int i, int i2, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(85, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " dtmfString = " + str + " on = " + i + " off = " + i2);
            try {
                ntnRadioProxy.sendBurstDtmf(rILRequestObtainRequest.mSerial, NtnRILUtils.convertNullToEmptyString(str), i, i2);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "sendBurstDtmf", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getMute(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(54, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getMute(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getMute", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void sendSMS(final String str, final String str2, final Message message) {
        if (MiuiTelephonyManagerStub.isSatelliteBox()) {
            if (NtnBoxOptimization.getInstance() != null) {
                NtnBoxOptimization.getInstance().sendScreenOffMessageByLongTime();
                if (NtnBoxOptimization.getInstance().getLowPowerState()) {
                    setScreenState(1, null);
                    NtnBoxOptimization.getInstance().setLowPowerState(false);
                }
            }
            this.mNtnRilHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.NtnRIL$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendSMS$1(str, str2, message);
                }
            }, 500L);
            return;
        }
        lambda$sendSMS$1(str, str2, message);
    }

    /* JADX INFO: renamed from: sendSmsBase, reason: merged with bridge method [inline-methods] */
    public void lambda$sendSMS$1(String str, String str2, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(25, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            GsmSmsMessage gsmSmsMessage = new GsmSmsMessage();
            if (str == null) {
                str = "";
            }
            gsmSmsMessage.smscPdu = str;
            if (str2 == null) {
                str2 = "";
            }
            gsmSmsMessage.pdu = str2;
            try {
                ntnRadioProxy.sendSms(rILRequestObtainRequest.mSerial, gsmSmsMessage);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "sendSMS", e);
            }
        }
    }

    public void setPowerSwitch(boolean z, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(243, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " on = " + z);
            try {
                ntnRadioProxy.setPowerSwitch(rILRequestObtainRequest.mSerial, z);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setPowerSwitch", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.BaseCommands, com.android.internal.telephony.CommandsInterface
    public void requestShutdown(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(129, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.requestShutdown(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "requestShutdown", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getBasebandVersion(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(51, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getBasebandVersion(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "geBasebandVersion", e);
            }
        }
    }

    public void supplyImsiForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMSI, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.supplyImsiForApp(rILRequestObtainRequest.mSerial, str);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyImsiForAppForNtn", e);
            }
        }
    }

    public void supplyIccAuthForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_SIM_AUTH_RESPONSE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.supplyIccAuthRsp(rILRequestObtainRequest.mSerial, str);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyIccAuthForAppForNtn", e);
            }
        }
    }

    public void supplyGpsForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(153, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.supplyGpsForApp(rILRequestObtainRequest.mSerial, str);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyGpsForApp", e);
            }
        }
    }

    public void supplyImeiForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("supplyImeiForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SUPPLY_IMEI, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.supplyImeiForApp(rILRequestObtainRequest.mSerial, str);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyImeiForAppForNtn", e);
            }
        }
    }

    public void getSARMode(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SAR_MODE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getSARMode(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getSARModeResponse", e);
            }
        }
    }

    public void setSARMode(int i, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(148, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setSARMode(rILRequestObtainRequest.mSerial, i);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setSARModeResponse", e);
            }
        }
    }

    public void setScreenState(int i, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SCREEN_STATE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setScreenState(rILRequestObtainRequest.mSerial, i);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setScreenState", e);
            }
        }
    }

    public void getSpeechBitrate(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_SPEECH_BITRATE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getSpeechBitrate(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getSpeechBitrate", e);
            }
        }
    }

    public void sendCustFactAtcmd(int i, String str, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_CUST_FACT_ATCMD, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.sendcustFactAtcmd(rILRequestObtainRequest.mSerial, i, str);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "sendcustFactAtcmd", e);
            }
        }
    }

    public void setRFMode(int i, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("setRFMode", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RF_MODE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setRFMode(rILRequestObtainRequest.mSerial, i);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setRFModeResponse", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getIccCardStatus(Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("getIccCardStatus", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(1, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getIccCardStatus(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getIccCardStatus", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL
    public void getIccidForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("getIccidForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ICCID, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " aid = " + str);
            try {
                ntnRadioProxy.getIccidForApp(rILRequestObtainRequest.mSerial, NtnRILUtils.convertNullToEmptyString(str));
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getIccidForApp", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void getIMSIForApp(String str, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("getIMSIForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(11, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " aid = " + str);
            try {
                ntnRadioProxy.getIMSIForApp(rILRequestObtainRequest.mSerial, NtnRILUtils.convertNullToEmptyString(str));
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getIMSIForApp", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void supplyIccPin(String str, Message message) {
        supplyIccPinForApp(str, null, message);
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void supplyIccPinForApp(String str, String str2, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("supplyIccPinForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(2, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " aid = " + str2);
            try {
                ntnRadioProxy.supplyIccPinForApp(rILRequestObtainRequest.mSerial, NtnRILUtils.convertNullToEmptyString(str), NtnRILUtils.convertNullToEmptyString(str2));
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyIccPinForApp", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void supplyIccPukForApp(String str, String str2, String str3, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("supplyIccPukForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(3, message, this.mRILDefaultWorkSource);
            String strConvertNullToEmptyString = NtnRILUtils.convertNullToEmptyString(str);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " isPukEmpty = " + strConvertNullToEmptyString.isEmpty() + " aid = " + str3);
            try {
                ntnRadioProxy.supplyIccPukForApp(rILRequestObtainRequest.mSerial, strConvertNullToEmptyString, NtnRILUtils.convertNullToEmptyString(str2), NtnRILUtils.convertNullToEmptyString(str3));
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "supplyIccPukForApp", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void changeIccPin(String str, String str2, Message message) {
        changeIccPinForApp(str, str2, null, message);
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void changeIccPinForApp(String str, String str2, String str3, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("changeIccPinForApp", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(6, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " oldPin = " + str + " newPin = " + str2 + " aid = " + str3);
            try {
                ntnRadioProxy.changeIccPinForApp(rILRequestObtainRequest.mSerial, NtnRILUtils.convertNullToEmptyString(str), NtnRILUtils.convertNullToEmptyString(str2), NtnRILUtils.convertNullToEmptyString(str3));
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "changeIccPinForApp", e);
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void iccIOForApp(int i, int i2, String str, int i3, int i4, int i5, String str2, String str3, String str4, Message message) {
        ntnRiljLog("iccIOForApp: REQUEST_NOT_SUPPORTED");
        if (message != null) {
            AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
            message.sendToTarget();
        }
    }

    public void setupDataCall(DataProfile dataProfile, int i, boolean z, boolean z2, boolean z3, int i2, Message message) {
        if (canMakeNtnRILRequest("setupDataCall", message, NTN_RADIO_HAL_VERSION_2_0)) {
            NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
            if (ntnRadioProxy != null) {
                RILRequest rILRequestObtainRequest = obtainRequest(27, message, this.mRILDefaultWorkSource);
                ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + ",reason=" + NtnRILUtils.setupDataReasonToString(i2) + ",accessNetworkType=" + AccessNetworkConstants.AccessNetworkType.toString(i) + ",dataProfile=" + dataProfile + ",roamingAllowed=" + z2);
                try {
                    ntnRadioProxy.setupDataCall(rILRequestObtainRequest.mSerial, i, dataProfile, z, z2, z3);
                } catch (RemoteException | RuntimeException e) {
                    handleRadioProxyExceptionForRR(8, "setupDataCall", e);
                }
            }
            if (SystemProperties.getInt("persist.radio.ntndata", 0) == 1) {
                DataCallResponse dataCallResponseConvertHalTestDataCallResult = NtnRILUtils.convertHalTestDataCallResult(new TestDataCallResult(0, -1, 0, 2, "PROTOCOL_IP", "ntn_data0", "192.168.1.100", "114.114.114.114", "192.168.1.102", null, 1500));
                if (message != null) {
                    AsyncResult.forMessage(message, dataCallResponseConvertHalTestDataCallResult, (Throwable) null);
                    message.sendToTarget();
                }
            }
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void deactivateDataCall(int i, int i2, Message message) {
        if (canMakeNtnRILRequest("deactivateDataCall", message, NTN_RADIO_HAL_VERSION_2_0)) {
            NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
            if (ntnRadioProxy != null) {
                RILRequest rILRequestObtainRequest = obtainRequest(41, message, this.mRILDefaultWorkSource);
                ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest) + " cid = " + i + " reason = " + NtnRILUtils.deactivateDataReasonToString(i2));
                try {
                    ntnRadioProxy.deactivateDataCall(rILRequestObtainRequest.mSerial, i, i2 != 0);
                } catch (RemoteException | RuntimeException e) {
                    handleRadioProxyExceptionForRR(8, "setupDataCall", e);
                }
            }
            if (SystemProperties.getInt("persist.radio.ntndata", 0) != 1 || message == null) {
                return;
            }
            AsyncResult.forMessage(message, (Object) null, (Throwable) null);
            message.sendToTarget();
        }
    }

    public void setSatBeam(int i, int[] iArr, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("setSatBeam", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SAT_BEAM, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setSatBeam(rILRequestObtainRequest.mSerial, i, iArr);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setSatBeam", e);
            }
        }
    }

    public void sendTemp(int i, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SEND_PA_TEMPERATURE, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.sendTemp(rILRequestObtainRequest.mSerial, i);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "sendTemp", e);
            }
        }
    }

    public void setPriorityFrequencySearch(int[] iArr, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("setPriorityFrequencySearch", message, NTN_RADIO_HAL_VERSION_2_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PRIORITY_FREQUENCY_SEARCH, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setPriorityFrequencySearch(rILRequestObtainRequest.mSerial, iArr);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setPriorityFrequencySearch", e);
            }
        }
    }

    public void queryCurrentSatellteCapability(Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_QUERY_CURRENT_SATELLITE_CAPABILITY, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.queryCurrentSatellteCapability(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "queryCurrentSatellteCapability", e);
            }
        }
    }

    public void setSimCardPower(boolean z, Message message) {
        NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(message);
        if (ntnRadioProxy != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_SET_SATELLITE_SIM_POWER, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setSimCardPower(rILRequestObtainRequest.mSerial, z);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setSimCardPower", e);
            }
        }
    }

    public boolean initNtnRadio() {
        NtnRadioProxy ntnRadioProxy;
        int i = 0;
        do {
            ntnRadioProxy = getNtnRadioProxy(null);
            i++;
            if (ntnRadioProxy == null) {
                riljLog("Retry(" + i + ") radioProxy " + ntnRadioProxy);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException unused) {
                }
            }
            if (ntnRadioProxy != null) {
                break;
            }
        } while (i < 10);
        if (ntnRadioProxy != null) {
            riljLog("initNtnRadio successfully");
            return true;
        }
        riljLog("initNtnRadio fail");
        return false;
    }

    public boolean destroyNtnRadio() {
        this.mNtnRadioProxy = null;
        return false;
    }

    private void sendAck(int i) {
        RILRequest rILRequestObtain = RILRequest.obtain(NtnRILConstants.MI_NTN_RIL_RESPONSE_ACKNOWLEDGEMENT, null, this.mRILDefaultWorkSource);
        acquireWakeLock(rILRequestObtain, 1);
        if (i == 8) {
            NtnRadioProxy ntnRadioProxy = getNtnRadioProxy(null);
            if (ntnRadioProxy != null) {
                try {
                    ntnRadioProxy.responseAcknowledgement();
                } catch (RemoteException | RuntimeException e) {
                    handleRadioProxyExceptionForRR(8, "sendAck", e);
                    ntnRiljLog("sendAck: " + e);
                }
            } else {
                Rlog.e("NTN-RILJ", "Error trying to send ack, radioProxy = null");
            }
        }
        rILRequestObtain.release();
    }

    void processIndicationForNtn(int i, int i2) {
        if (i2 == 1) {
            sendAck(i);
            ntnRiljLog("Unsol NTN response received; Sending ack to ril.cpp");
        }
    }

    public RILRequest processResponse(RadioResponseInfo radioResponseInfo) {
        return processResponseInternal(8, radioResponseInfo.serial, radioResponseInfo.error, radioResponseInfo.type);
    }

    public void processResponseDone(RILRequest rILRequest, RadioResponseInfo radioResponseInfo, Object obj) {
        processResponseDoneInternalForNtn(rILRequest, radioResponseInfo.error, radioResponseInfo.type, obj);
    }

    @Override // com.android.internal.telephony.RIL
    protected void handleRadioProxyExceptionForRR(int i, String str, Exception exc) {
        ntnRiljLoge(str + ": " + exc);
        exc.printStackTrace();
        resetNtnProxyAndRequestList(i);
    }

    public synchronized void shutDownNtnProxyAndRequestList() {
        this.mNtnRadioProxy = null;
        setRadioState(2, true);
        RILRequest.resetSerial();
        clearRequestList(1, false);
    }

    private synchronized void resetNtnProxyAndRequestList(int i) {
        if (i == 8) {
            this.mNtnRadioProxy = null;
            setRadioState(2, true);
            RILRequest.resetSerial();
            clearRequestList(1, false);
            getNtnRadioProxy(null);
        }
    }

    private void clearRequestList(int i, boolean z) {
        synchronized (this.mRequestList) {
            int size = this.mRequestList.size();
            if (z) {
                Rlog.d("NTN-RILJ", "clearRequestList  mWakeLockCount=" + this.mWakeLockCount + " mRequestList=" + size);
            }
            int i2 = 0;
            while (true) {
                SparseArray<RILRequest> sparseArray = this.mRequestList;
                if (i2 < size) {
                    RILRequest rILRequestValueAt = sparseArray.valueAt(i2);
                    if (z) {
                        Rlog.d("NTN-RILJ", i2 + ": [" + rILRequestValueAt.mSerial + "] " + RILUtils.requestToString(rILRequestValueAt.mRequest));
                    }
                    rILRequestValueAt.onError(i, null);
                    decrementWakeLock(rILRequestValueAt);
                    rILRequestValueAt.release();
                    i2++;
                } else {
                    sparseArray.clear();
                }
            }
        }
    }

    private void processResponseDoneInternalForNtn(RILRequest rILRequest, int i, int i2, Object obj) {
        if (i == 0) {
            ntnRiljLog(rILRequest.serialString() + "< " + NtnRILUtils.ntnRequestToString(rILRequest.mRequest) + " " + ntnRetToString(rILRequest.mRequest, obj));
        } else {
            ntnRiljLog(rILRequest.serialString() + "< " + NtnRILUtils.ntnRequestToString(rILRequest.mRequest) + " error " + i);
            rILRequest.onError(i, obj);
        }
        processResponseCleanUp(rILRequest, i, i2, obj);
    }

    public void setOnNewNtnSms(Handler handler, int i, Object obj) {
        this.mNtnSmsRegistrant = new Registrant(handler, i, obj);
    }

    public void unSetOnNewNtnSms(Handler handler) {
        Registrant registrant = this.mNtnSmsRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mNtnSmsRegistrant.clear();
        this.mNtnSmsRegistrant = null;
    }

    public void setOnNewNtnSmsStatusReport(Handler handler, int i, Object obj) {
        this.mNtnStatusReportRegistrant = new Registrant(handler, i, obj);
    }

    public void unsetOnNewNtnSmsStatusReport(Handler handler) {
        Registrant registrant = this.mNtnStatusReportRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mNtnStatusReportRegistrant.clear();
        this.mNtnStatusReportRegistrant = null;
    }

    public void setOnNewNtnSmsSendResult(Handler handler, int i, Object obj) {
        this.mNtnSmsSendResultRegistrant = new Registrant(handler, i, obj);
    }

    public void unsetOnNewNtnSmsSendResult(Handler handler) {
        Registrant registrant = this.mNtnSmsSendResultRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mNtnSmsSendResultRegistrant.clear();
        this.mNtnSmsSendResultRegistrant = null;
    }

    public void registerForImsiQuery(Handler handler, int i, Object obj) {
        this.mImsiQueryRegistrant = new Registrant(handler, i, obj);
    }

    public void unregisterForImsiQuery(Handler handler) {
        this.mImsiQueryRegistrant.clear();
    }

    public void registerForImeiQuery(Handler handler, int i, Object obj) {
        this.mImeiQueryRegistrant = new Registrant(handler, i, obj);
    }

    public void unregisterForImeiQuery(Handler handler) {
        this.mImeiQueryRegistrant.clear();
    }

    public void registerForSimAuto(Handler handler, int i, Object obj) {
        this.mSimAutoRegistrant = new Registrant(handler, i, obj);
    }

    public void unregisterForSimAuto(Handler handler) {
        this.mSimAutoRegistrant.clear();
    }

    public void registerTxPwr(Handler handler, int i, Object obj) {
        this.mTxPwrRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterTxPwr(Handler handler) {
        Registrant registrant = this.mTxPwrRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mTxPwrRegistrant.clear();
        this.mTxPwrRegistrant = null;
    }

    public void registerHlserrorReport(Handler handler, int i, Object obj) {
        this.mHlserrorReportRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterHlserrorReport(Handler handler) {
        Registrant registrant = this.mHlserrorReportRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mHlserrorReportRegistrant.clear();
        this.mHlserrorReportRegistrant = null;
    }

    public void registerTtlogInfoReport(Handler handler, int i, Object obj) {
        this.mTtlogInfoRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterTtlogInfoReport(Handler handler) {
        Registrant registrant = this.mTtlogInfoRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mTtlogInfoRegistrant.clear();
        this.mTtlogInfoRegistrant = null;
    }

    public void registerNtnPosInfoReport(Handler handler, int i, Object obj) {
        this.mNtnPosInfoRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterNtnPosInfoReport(Handler handler) {
        Registrant registrant = this.mNtnPosInfoRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mNtnPosInfoRegistrant.clear();
        this.mNtnPosInfoRegistrant = null;
    }

    @Override // com.android.internal.telephony.BaseCommands, com.android.internal.telephony.CommandsInterface
    public void registerForDataCallListChanged(Handler handler, int i, Object obj) {
        this.mDataCallListChangedRegistrants = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.BaseCommands, com.android.internal.telephony.CommandsInterface
    public void unregisterForDataCallListChanged(Handler handler) {
        Registrant registrant = this.mDataCallListChangedRegistrants;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mDataCallListChangedRegistrants.clear();
        this.mDataCallListChangedRegistrants = null;
    }

    public void registerVoiceControl(Handler handler, int i, Object obj) {
        this.mVoiceControlRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterVoiceControl(Handler handler) {
        Registrant registrant = this.mVoiceControlRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mVoiceControlRegistrant.clear();
        this.mVoiceControlRegistrant = null;
    }

    public void registerSnr(Handler handler, int i, Object obj) {
        this.mSnrRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterSnr(Handler handler) {
        Registrant registrant = this.mSnrRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mSnrRegistrant.clear();
        this.mSnrRegistrant = null;
    }

    public void registerGpsQuery(Handler handler, int i, Object obj) {
        this.mGpsRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterGpsQuery(Handler handler) {
        Registrant registrant = this.mGpsRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mGpsRegistrant.clear();
        this.mGpsRegistrant = null;
    }

    public void registerGpsMinRangeQuery(Handler handler, int i, Object obj) {
        this.mGpsMinRangeRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterGpsMinRangeQuery(Handler handler) {
        Registrant registrant = this.mGpsMinRangeRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mGpsMinRangeRegistrant.clear();
        this.mGpsMinRangeRegistrant = null;
    }

    public void registerHighPenetrationState(Handler handler, int i, Object obj) {
        this.mHighPenetrationRegistrant = new Registrant(handler, i, obj);
    }

    public void unRegisterHighPenetrationState(Handler handler) {
        Registrant registrant = this.mHighPenetrationRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mHighPenetrationRegistrant.clear();
        this.mHighPenetrationRegistrant = null;
    }

    public void registerChannelState(Handler handler, int i, Object obj) {
        this.mChannelStateRegistrantList.addUnique(handler, i, obj);
    }

    public void unregisterChannelState(Handler handler) {
        RegistrantList registrantList = this.mChannelStateRegistrantList;
        if (registrantList != null) {
            registrantList.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.RIL, com.android.internal.telephony.CommandsInterface
    public void requestIccSimAuthentication(int i, String str, String str2, Message message) {
        getModemRil().requestIccSimAuthentication(i, str, str2, message);
    }

    void ntnRiljLog(String str) {
        Rlog.d("NTN-RILJ", str + " [PHONE" + this.mPhoneId + "]");
    }

    void ntnRiljLoge(String str) {
        Rlog.e("NTN-RILJ", str + " [PHONE" + this.mPhoneId + "]");
    }

    void ntnUnsljLog(int i) {
        ntnRiljLog("[UNSL]< " + NtnRILUtils.ntnResponseToString(i));
    }

    void ntnUnsljLogvRet(int i, Object obj) {
        ntnRiljLogv("[UNSL]< " + NtnRILUtils.ntnResponseToString(i) + " " + RIL.retToString(i, obj));
    }

    void ntnRiljLogv(String str) {
        Rlog.v("NTN-RILJ", str + " [PHONE" + this.mPhoneId + "]");
    }

    void ntnUnsljLogRet(int i, Object obj) {
        ntnRiljLog("[UNSL]< " + NtnRILUtils.ntnResponseToString(i) + " " + RIL.retToString(i, obj));
    }

    public static String ntnRetToString(int i, Object obj) {
        if (i == 20) {
            if (obj instanceof VoiceRegStateResult) {
                return NtnRILUtils.buildVoiceRegStateResultforNTN((VoiceRegStateResult) obj);
            }
        } else if (i == 146) {
            return "";
        }
        return RIL.retToString(i, obj);
    }

    public HalVersion getNtnHalVersion() {
        return this.mNtnHalVersion;
    }

    public static HalVersion getNtnServiceHalVersion(int i) {
        if (i == 1) {
            return NTN_RADIO_HAL_VERSION_1_0;
        }
        if (i == 2) {
            return NTN_RADIO_HAL_VERSION_2_0;
        }
        if (i == 3) {
            return NTN_RADIO_HAL_VERSION_3_0;
        }
        return NTN_RADIO_HAL_VERSION_UNKNOWN;
    }

    private boolean canMakeNtnRILRequest(String str, Message message, HalVersion halVersion) {
        if (!this.mNtnHalVersion.less(halVersion)) {
            return true;
        }
        ntnRiljLoge(String.format("%s not supported on NTN RADIO < %s.", str, halVersion));
        if (message == null) {
            return false;
        }
        AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
        message.sendToTarget();
        return false;
    }

    public void setAntennaState(NtnAntConstants.TxAntEnableType txAntEnableType, NtnAntConstants.RxAntEnableType rxAntEnableType, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("setAntennaState", message, NTN_RADIO_HAL_VERSION_3_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(244, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setAntennaState(rILRequestObtainRequest.mSerial, txAntEnableType, rxAntEnableType);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setAntennaState", e);
            }
        }
    }

    public void getAntennaState(Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("getAntennaState", message, NTN_RADIO_HAL_VERSION_3_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(245, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.getAntennaState(rILRequestObtainRequest.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "getAntennaState", e);
            }
        }
    }

    public void setAntennaReportMode(NtnAntConstants.RxAntReportMode rxAntReportMode, Message message) {
        NtnRadioProxy ntnRadioProxy;
        if (canMakeNtnRILRequest("setAntennaReportMode", message, NTN_RADIO_HAL_VERSION_3_0) && (ntnRadioProxy = getNtnRadioProxy(message)) != null) {
            RILRequest rILRequestObtainRequest = obtainRequest(246, message, this.mRILDefaultWorkSource);
            ntnRiljLog(rILRequestObtainRequest.serialString() + "> " + NtnRILUtils.ntnRequestToString(rILRequestObtainRequest.mRequest));
            try {
                ntnRadioProxy.setAntennaReportMode(rILRequestObtainRequest.mSerial, rxAntReportMode);
            } catch (RemoteException | RuntimeException e) {
                handleRadioProxyExceptionForRR(8, "setAntennaReportMode", e);
            }
        }
    }

    public void setOnAntInfoSignalStrengthUpdate(Handler handler, int i, Object obj) {
        this.mAntInfoSignalStrengthRegistrant = new Registrant(handler, i, obj);
    }

    public void unSetOnAntInfoSignalStrengthUpdate(Handler handler) {
        Registrant registrant = this.mAntInfoSignalStrengthRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mAntInfoSignalStrengthRegistrant.clear();
        this.mAntInfoSignalStrengthRegistrant = null;
    }

    public void registerForOtaStateChanged(Handler handler, int i, Object obj) {
        this.mOtaStateChangedRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForOtaStateChanged(Handler handler) {
        this.mOtaStateChangedRegistrants.remove(handler);
    }

    public void registerForUnsolNtnCdcMsg(Handler handler, int i, Object obj) {
        this.mUnsolNtnCdcMsgRegistrants.addUnique(handler, i, obj);
    }

    public void unRegisterForUnsolNtnCdcMsg(Handler handler) {
        this.mUnsolNtnCdcMsgRegistrants.remove(handler);
    }
}
