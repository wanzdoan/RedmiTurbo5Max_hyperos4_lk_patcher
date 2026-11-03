package com.android.internal.telephony;

import android.R;
import android.content.Context;
import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.config.IRadioConfig;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.WorkSource;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotMapping;
import android.util.SparseArray;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes.dex */
public class RadioConfig extends Handler {
    private static Context sContext;
    private static final Object sLock = new Object();
    private static RadioConfig sRadioConfig;
    private final WorkSource mDefaultWorkSource;
    private final int[] mDeviceNrCapabilities;
    private final boolean mIsMobileNetworkSupported;
    private MockModem mMockModem;
    protected final RadioConfigProxy mRadioConfigProxy;
    protected Registrant mSimSlotStatusRegistrant;
    protected Registrant mSimultaneousCallingSupportStatusRegistrant;
    private final SparseArray mRequestList = new SparseArray();
    protected final AtomicLong mRadioConfigProxyCookie = new AtomicLong(0);

    public void configureParameters(int i, Bundle bundle) {
    }

    public boolean hasVendorCapability(int i) {
        return false;
    }

    protected boolean isGetHidlServiceSync() {
        return true;
    }

    public String readNvramByName(String str, int i) {
        return null;
    }

    public byte writeNvramByNamevec(String str, int i, byte[] bArr) {
        return (byte) -1;
    }

    private boolean isMobileDataCapable(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        return telephonyManager != null && telephonyManager.isDataCapable();
    }

    protected RadioConfig(Context context, HalVersion halVersion) {
        this.mIsMobileNetworkSupported = isMobileDataCapable(context);
        this.mRadioConfigProxy = new RadioConfigProxy(this, halVersion);
        this.mDefaultWorkSource = new WorkSource(context.getApplicationInfo().uid, context.getPackageName());
        boolean z = context.getResources().getBoolean(R.bool.kg_top_align_page_shrink_on_bouncer_visible);
        boolean z2 = context.getResources().getBoolean(R.bool.kg_sim_puk_account_full_screen);
        if (!z && !z2) {
            this.mDeviceNrCapabilities = new int[0];
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (z2) {
            arrayList.add(1);
        }
        if (z) {
            arrayList.add(2);
        }
        this.mDeviceNrCapabilities = arrayList.stream().mapToInt(new RadioConfig$$ExternalSyntheticLambda0()).toArray();
    }

    public static RadioConfig getInstance() {
        RadioConfig radioConfig;
        synchronized (sLock) {
            try {
                radioConfig = sRadioConfig;
                if (radioConfig == null) {
                    throw new RuntimeException("RadioConfig.getInstance can't be called before make()");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return radioConfig;
    }

    public static RadioConfig make(Context context, HalVersion halVersion) {
        RadioConfig radioConfig;
        synchronized (sLock) {
            if (sRadioConfig != null) {
                throw new RuntimeException("RadioConfig.make() should only be called once");
            }
            sContext = context;
            if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
                try {
                    Class<?> cls = Class.forName("com.mediatek.internal.telephony.MtkRadioConfig", false, ClassLoader.getSystemClassLoader());
                    logd("class = " + cls);
                    Constructor<?> constructor = cls.getConstructor(Context.class, HalVersion.class);
                    logd("constructor function = " + constructor);
                    sRadioConfig = (RadioConfig) constructor.newInstance(context, halVersion);
                } catch (Exception e) {
                    loge("No MtkRadioConfig! Used AOSP! e: " + e);
                    sRadioConfig = new RadioConfig(context, halVersion);
                }
            } else {
                sRadioConfig = new RadioConfig(context, halVersion);
            }
            radioConfig = sRadioConfig;
        }
        return radioConfig;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 1) {
            if (i == 2) {
                logd("handleMessage: EVENT_AIDL_SERVICE_DEAD mRadioConfigProxyCookie = " + this.mRadioConfigProxyCookie.get());
                resetProxyAndRequestList("EVENT_AIDL_SERVICE_DEAD", null);
                return;
            }
            return;
        }
        logd("handleMessage: EVENT_HIDL_SERVICE_DEAD cookie = " + message.obj + " mRadioConfigProxyCookie = " + this.mRadioConfigProxyCookie.get());
        if (((Long) message.obj).longValue() == this.mRadioConfigProxyCookie.get()) {
            resetProxyAndRequestList("EVENT_HIDL_SERVICE_DEAD", null);
        }
    }

    private void clearRequestList(int i, boolean z) {
        synchronized (this.mRequestList) {
            int size = this.mRequestList.size();
            if (z) {
                logd("clearRequestList: mRequestList=" + size);
            }
            int i2 = 0;
            while (true) {
                SparseArray sparseArray = this.mRequestList;
                if (i2 < size) {
                    RILRequest rILRequest = (RILRequest) sparseArray.valueAt(i2);
                    if (z) {
                        logd(i2 + ": [" + rILRequest.mSerial + "] " + RILUtils.requestToString(rILRequest.mRequest));
                    }
                    rILRequest.onError(i, null);
                    rILRequest.release();
                    i2++;
                } else {
                    sparseArray.clear();
                }
            }
        }
    }

    private void resetProxyAndRequestList(String str, Exception exc) {
        loge(str + ": " + exc);
        this.mRadioConfigProxy.clear();
        this.mRadioConfigProxyCookie.incrementAndGet();
        RILRequest.resetSerial();
        clearRequestList(1, false);
        getRadioConfigProxy(null);
    }

    public RadioConfigProxy getRadioConfigProxy(Message message) {
        if (!this.mIsMobileNetworkSupported) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(1));
                message.sendToTarget();
            }
            this.mRadioConfigProxy.clear();
            return this.mRadioConfigProxy;
        }
        if (!this.mRadioConfigProxy.isEmpty()) {
            return this.mRadioConfigProxy;
        }
        updateRadioConfigProxy();
        if (this.mRadioConfigProxy.isEmpty() && message != null) {
            AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(1));
            message.sendToTarget();
        }
        return this.mRadioConfigProxy;
    }

    public boolean setModemService(String str) {
        IBinder serviceBinder;
        boolean z = true;
        if (str != null) {
            logd("Overriding connected service to MockModemService");
            this.mMockModem = null;
            MockModem mockModem = new MockModem(sContext, str);
            this.mMockModem = mockModem;
            mockModem.bindToMockModemService(8);
            int i = 0;
            do {
                serviceBinder = this.mMockModem.getServiceBinder(8);
                i++;
                if (serviceBinder == null) {
                    logd("Retry(" + i + ") Mock RadioConfig");
                    try {
                        Thread.sleep(300L);
                    } catch (InterruptedException unused) {
                    }
                }
                if (serviceBinder != null) {
                    break;
                }
            } while (i < 10);
            if (serviceBinder == null) {
                loge("Mock RadioConfig bind fail");
                z = false;
            }
            if (z) {
                resetProxyAndRequestList("EVENT_HIDL_SERVICE_DEAD", null);
            }
        }
        if (str == null || !z) {
            if (z) {
                logd("Unbinding to mock RadioConfig service");
            }
            MockModem mockModem2 = this.mMockModem;
            if (mockModem2 != null) {
                mockModem2.unbindMockModemService(8);
                this.mMockModem = null;
                resetProxyAndRequestList("EVENT_AIDL_SERVICE_DEAD", null);
            }
        }
        return z;
    }

    protected void updateRadioConfigProxy() {
        IBinder serviceBinder;
        MockModem mockModem = this.mMockModem;
        if (mockModem == null) {
            serviceBinder = ServiceManager.waitForDeclaredService(IRadioConfig.DESCRIPTOR + "/default");
        } else {
            serviceBinder = mockModem.getServiceBinder(8);
        }
        if (serviceBinder != null) {
            this.mRadioConfigProxy.setAidl(IRadioConfig.Stub.asInterface(serviceBinder));
        }
        if (this.mRadioConfigProxy.isEmpty()) {
            try {
                this.mRadioConfigProxy.setHidl(RIL.RADIO_HAL_VERSION_1_3, android.hardware.radio.config.V1_3.IRadioConfig.getService(true));
            } catch (RemoteException | NoSuchElementException e) {
                this.mRadioConfigProxy.clear();
                loge("getHidlRadioConfigProxy1_3: RadioConfigProxy getService: " + e);
            }
        }
        if (this.mRadioConfigProxy.isEmpty()) {
            try {
                this.mRadioConfigProxy.setHidl(RIL.RADIO_HAL_VERSION_1_1, android.hardware.radio.config.V1_1.IRadioConfig.getService(true));
            } catch (RemoteException | NoSuchElementException e2) {
                this.mRadioConfigProxy.clear();
                loge("getHidlRadioConfigProxy1_1: RadioConfigProxy getService | linkToDeath: " + e2);
            }
        }
        if (this.mRadioConfigProxy.isEmpty()) {
            loge("IRadioConfig <1.1 is no longer supported.");
        }
        if (!this.mRadioConfigProxy.isEmpty()) {
            try {
                this.mRadioConfigProxy.linkToDeath(this.mRadioConfigProxyCookie.incrementAndGet());
                this.mRadioConfigProxy.setResponseFunctions(this);
                return;
            } catch (RemoteException unused) {
                this.mRadioConfigProxy.clear();
                loge("RadioConfigProxy: failed to linkToDeath() or setResponseFunction()");
            }
        }
        loge("getRadioConfigProxy: mRadioConfigProxy == null");
    }

    private RILRequest obtainRequest(int i, Message message, WorkSource workSource) {
        RILRequest rILRequestObtain = RILRequest.obtain(i, message, workSource);
        Trace.asyncTraceForTrackBegin(2097152L, "RIL", RILUtils.requestToString(rILRequestObtain.mRequest), rILRequestObtain.mSerial);
        synchronized (this.mRequestList) {
            this.mRequestList.append(rILRequestObtain.mSerial, rILRequestObtain);
        }
        return rILRequestObtain;
    }

    private RILRequest findAndRemoveRequestFromList(int i) {
        RILRequest rILRequest;
        synchronized (this.mRequestList) {
            try {
                rILRequest = (RILRequest) this.mRequestList.get(i);
                if (rILRequest != null) {
                    Trace.asyncTraceForTrackEnd(2097152L, "RIL", rILRequest.mSerial);
                    this.mRequestList.remove(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return rILRequest;
    }

    public RILRequest processResponse(RadioResponseInfo radioResponseInfo) {
        int i = radioResponseInfo.serial;
        int i2 = radioResponseInfo.error;
        int i3 = radioResponseInfo.type;
        if (i3 != 0) {
            loge("processResponse: Unexpected response type " + i3);
        }
        RILRequest rILRequestFindAndRemoveRequestFromList = findAndRemoveRequestFromList(i);
        if (rILRequestFindAndRemoveRequestFromList != null) {
            return rILRequestFindAndRemoveRequestFromList;
        }
        loge("processResponse: Unexpected response! serial: " + i + " error: " + i2);
        return null;
    }

    public RILRequest processResponse(android.hardware.radio.V1_0.RadioResponseInfo radioResponseInfo) {
        int i = radioResponseInfo.serial;
        int i2 = radioResponseInfo.error;
        int i3 = radioResponseInfo.type;
        if (i3 != 0) {
            loge("processResponse: Unexpected response type " + i3);
        }
        RILRequest rILRequestFindAndRemoveRequestFromList = findAndRemoveRequestFromList(i);
        if (rILRequestFindAndRemoveRequestFromList != null) {
            return rILRequestFindAndRemoveRequestFromList;
        }
        loge("processResponse: Unexpected response! serial: " + i + " error: " + i2);
        return null;
    }

    public RILRequest processResponse_1_6(android.hardware.radio.V1_6.RadioResponseInfo radioResponseInfo) {
        int i = radioResponseInfo.serial;
        int i2 = radioResponseInfo.error;
        int i3 = radioResponseInfo.type;
        if (i3 != 0) {
            loge("processResponse: Unexpected response type " + i3);
        }
        RILRequest rILRequestFindAndRemoveRequestFromList = findAndRemoveRequestFromList(i);
        if (rILRequestFindAndRemoveRequestFromList != null) {
            return rILRequestFindAndRemoveRequestFromList;
        }
        loge("processResponse: Unexpected response! serial: " + i + " error: " + i2);
        return null;
    }

    public void getSimSlotsStatus(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(200, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.getSimSlotStatus(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("getSimSlotsStatus", e);
        }
    }

    public void setPreferredDataModem(int i, Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(null);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (!isSetPreferredDataCommandSupported()) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PREFERRED_DATA_MODEM, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest) + " " + i);
        try {
            radioConfigProxy.setPreferredDataModem(rILRequestObtainRequest.mSerial, i);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("setPreferredDataModem", e);
        }
    }

    public void updateSimultaneousCallingSupport(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(null);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_2_2)) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(CallFailCause.RADIO_INTERNAL_ERROR, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.updateSimultaneousCallingSupport(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("updateSimultaneousCallingSupport", e);
        }
    }

    public void getPhoneCapability(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(null);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_1_1)) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_PHONE_CAPABILITY, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.getPhoneCapability(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("getPhoneCapability", e);
        }
    }

    public boolean isSetPreferredDataCommandSupported() {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(null);
        return !radioConfigProxy.isEmpty() && radioConfigProxy.getVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_1_1);
    }

    public void setSimSlotsMapping(List<UiccSlotMapping> list, Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(201, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest) + " " + list);
        try {
            radioConfigProxy.setSimSlotsMapping(rILRequestObtainRequest.mSerial, list);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("setSimSlotsMapping", e);
        }
    }

    public void setNumOfLiveModems(int i, Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_1_1)) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_SWITCH_DUAL_SIM_CONFIG, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest) + ", numOfLiveModems = " + i);
        try {
            radioConfigProxy.setNumOfLiveModems(rILRequestObtainRequest.mSerial, i);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("setNumOfLiveModems", e);
        }
    }

    public void setSimType(int[] iArr, Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_2_3)) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(CallFailCause.RADIO_RELEASE_NORMAL, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest) + " " + Arrays.toString(iArr));
        try {
            radioConfigProxy.setSimType(rILRequestObtainRequest.mSerial, RILUtils.convertToAidlSimTypes(iArr));
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("setSimType", e);
        }
    }

    public void getSimTypeInfo(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_2_3)) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(CallFailCause.RADIO_RELEASE_ABNORMAL, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.getSimTypeInfo(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("getSimTypeInfo", e);
        }
    }

    public boolean isGetOrSetSimTypeSupported() {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(null);
        return !radioConfigProxy.isEmpty() && radioConfigProxy.getVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_2_3);
    }

    public void registerForSimultaneousCallingSupportStatusChanged(Handler handler, int i, Object obj) {
        this.mSimultaneousCallingSupportStatusRegistrant = new Registrant(handler, i, obj);
    }

    public void registerForSimSlotStatusChanged(Handler handler, int i, Object obj) {
        this.mSimSlotStatusRegistrant = new Registrant(handler, i, obj);
    }

    public void unregisterForSimSlotStatusChanged(Handler handler) {
        Registrant registrant = this.mSimSlotStatusRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mSimSlotStatusRegistrant.clear();
        this.mSimSlotStatusRegistrant = null;
    }

    public void getHalDeviceCapabilities(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(Message.obtain(message));
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().less(RIL.RADIO_HAL_VERSION_1_3)) {
            if (message != null) {
                logd("RIL_REQUEST_GET_HAL_DEVICE_CAPABILITIES > REQUEST_NOT_SUPPORTED");
                AsyncResult.forMessage(message, radioConfigProxy.getFullCapabilitySet(), CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            logd("RIL_REQUEST_GET_HAL_DEVICE_CAPABILITIES > REQUEST_NOT_SUPPORTED on complete message not set.");
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(NtnRILConstants.MI_NTN_RIL_REQUEST_GET_HAL_DEVICE_CAPABILITIES, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.getHalDeviceCapabilities(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("getHalDeviceCapabilities", e);
        }
    }

    public int[] getDeviceNrCapabilities() {
        return this.mDeviceNrCapabilities;
    }

    public void rebootModem(Message message) {
        RadioConfigProxy radioConfigProxy = getRadioConfigProxy(message);
        if (radioConfigProxy.isEmpty()) {
            return;
        }
        if (radioConfigProxy.getVersion().compareTo(RIL.RADIO_HAL_VERSION_2_4) < 0) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(264, message, this.mDefaultWorkSource);
        logd(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        try {
            radioConfigProxy.rebootModem(rILRequestObtainRequest.mSerial);
        } catch (RemoteException | RuntimeException e) {
            resetProxyAndRequestList("rebootModem", e);
        }
    }

    private static void logd(String str) {
        Rlog.d("RadioConfig", str);
    }

    private static void loge(String str) {
        Rlog.e("RadioConfig", str);
    }

    @Override // android.os.Handler
    public String toString() {
        return "RadioConfig[mRadioConfigProxy=" + this.mRadioConfigProxy + ']';
    }
}
