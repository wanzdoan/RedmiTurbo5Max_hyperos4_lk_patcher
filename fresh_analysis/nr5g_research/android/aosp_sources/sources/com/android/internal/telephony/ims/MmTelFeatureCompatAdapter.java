package com.android.internal.telephony.ims;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.CapabilityChangeRequest;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.feature.MmTelFeature;
import android.util.Log;
import com.android.ims.ImsConfigListener;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsUt;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class MmTelFeatureCompatAdapter extends MmTelFeature {
    public static final String ACTION_IMS_INCOMING_CALL = "com.android.ims.IMS_INCOMING_CALL";
    public static final int FEATURE_DISABLED = 0;
    public static final int FEATURE_ENABLED = 1;
    public static final int FEATURE_TYPE_UNKNOWN = -1;
    public static final int FEATURE_TYPE_UT_OVER_LTE = 4;
    public static final int FEATURE_TYPE_UT_OVER_WIFI = 5;
    public static final int FEATURE_TYPE_VIDEO_OVER_LTE = 1;
    public static final int FEATURE_TYPE_VIDEO_OVER_WIFI = 3;
    public static final int FEATURE_TYPE_VOICE_OVER_LTE = 0;
    public static final int FEATURE_TYPE_VOICE_OVER_WIFI = 2;
    public static final int FEATURE_UNKNOWN = -1;
    private static final Map REG_TECH_TO_NET_TYPE;
    private final MmTelInterfaceAdapter mCompatFeature;
    private ImsRegistrationCompatAdapter mRegCompatAdapter;
    private int mSessionId = -1;
    private final IImsRegistrationListener mListener = new IImsRegistrationListener.Stub() { // from class: com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.1
        public void registrationAssociatedUriChanged(Uri[] uriArr) throws RemoteException {
        }

        public void registrationChangeFailed(int i, ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public void registrationConnected() throws RemoteException {
        }

        public void registrationConnectedWithRadioTech(int i) throws RemoteException {
        }

        public void registrationProgressing() throws RemoteException {
        }

        public void registrationProgressingWithRadioTech(int i) throws RemoteException {
        }

        public void registrationResumed() throws RemoteException {
        }

        public void registrationServiceCapabilityChanged(int i, int i2) throws RemoteException {
        }

        public void registrationSuspended() throws RemoteException {
        }

        public void registrationDisconnected(ImsReasonInfo imsReasonInfo) throws RemoteException {
            Log.i("MmTelFeatureCompat", "registrationDisconnected: resetting MMTEL capabilities.");
            MmTelFeatureCompatAdapter.this.notifyCapabilitiesStatusChanged(new MmTelFeature.MmTelCapabilities());
        }

        public void registrationFeatureCapabilityChanged(int i, int[] iArr, int[] iArr2) throws RemoteException {
            MmTelFeatureCompatAdapter mmTelFeatureCompatAdapter = MmTelFeatureCompatAdapter.this;
            mmTelFeatureCompatAdapter.notifyCapabilitiesStatusChanged(mmTelFeatureCompatAdapter.convertCapabilities(iArr));
        }

        public void voiceMessageCountUpdate(int i) throws RemoteException {
            MmTelFeatureCompatAdapter.this.notifyVoiceMessageCountUpdate(i);
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.i("MmTelFeatureCompat", "onReceive");
            if (intent.getAction().equals(MmTelFeatureCompatAdapter.ACTION_IMS_INCOMING_CALL)) {
                Log.i("MmTelFeatureCompat", "onReceive : incoming call intent.");
                try {
                    MmTelFeatureCompatAdapter.this.notifyIncomingCallSession(MmTelFeatureCompatAdapter.this.mCompatFeature.getPendingCallSession(MmTelFeatureCompatAdapter.this.mSessionId, intent.getStringExtra("android:imsCallID")), intent.getExtras());
                } catch (RemoteException unused) {
                    Log.w("MmTelFeatureCompat", "onReceive: Couldn't get Incoming call session.");
                }
            }
        }
    };

    private int convertCapability(int i, int i2) {
        if (i2 == 0) {
            if (i == 1) {
                return 0;
            }
            if (i != 2) {
                return i != 4 ? -1 : 4;
            }
            return 1;
        }
        if (i2 != 1) {
            return -1;
        }
        if (i == 1) {
            return 2;
        }
        if (i != 2) {
            return i != 4 ? -1 : 5;
        }
        return 3;
    }

    static {
        HashMap map = new HashMap(2);
        REG_TECH_TO_NET_TYPE = map;
        map.put(3, 20);
        map.put(0, 13);
        map.put(1, 18);
        map.put(2, 18);
    }

    private static class ConfigListener extends ImsConfigListener.Stub {
        private final int mCapability;
        private final CountDownLatch mLatch;
        private final int mTech;

        public void getFeatureValueReceived(int i) {
        }

        public void onGetVideoQuality(int i, int i2) throws RemoteException {
        }

        public void onSetVideoQuality(int i) throws RemoteException {
        }

        public void setFeatureValueReceived(int i) {
        }

        public ConfigListener(int i, int i2, CountDownLatch countDownLatch) {
            this.mCapability = i;
            this.mTech = i2;
            this.mLatch = countDownLatch;
        }

        public void onGetFeatureResponse(int i, int i2, int i3, int i4) throws RemoteException {
            if (i == this.mCapability && i2 == this.mTech) {
                this.mLatch.countDown();
                getFeatureValueReceived(i3);
                return;
            }
            Log.i("MmTelFeatureCompat", "onGetFeatureResponse: response different than requested: feature=" + i + " and network=" + i2);
        }

        public void onSetFeatureResponse(int i, int i2, int i3, int i4) throws RemoteException {
            if (i == this.mCapability && i2 == this.mTech) {
                this.mLatch.countDown();
                setFeatureValueReceived(i3);
                return;
            }
            Log.i("MmTelFeatureCompat", "onSetFeatureResponse: response different than requested: feature=" + i + " and network=" + i2);
        }
    }

    private class ImsRegistrationListenerBase extends IImsRegistrationListener.Stub {
        public void registrationAssociatedUriChanged(Uri[] uriArr) throws RemoteException {
        }

        public void registrationChangeFailed(int i, ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public void registrationConnected() throws RemoteException {
        }

        public void registrationConnectedWithRadioTech(int i) throws RemoteException {
        }

        public void registrationDisconnected(ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        public void registrationFeatureCapabilityChanged(int i, int[] iArr, int[] iArr2) throws RemoteException {
        }

        public void registrationProgressing() throws RemoteException {
        }

        public void registrationProgressingWithRadioTech(int i) throws RemoteException {
        }

        public void registrationResumed() throws RemoteException {
        }

        public void registrationServiceCapabilityChanged(int i, int i2) throws RemoteException {
        }

        public void registrationSuspended() throws RemoteException {
        }

        public void voiceMessageCountUpdate(int i) throws RemoteException {
        }

        private ImsRegistrationListenerBase(MmTelFeatureCompatAdapter mmTelFeatureCompatAdapter) {
        }
    }

    public MmTelFeatureCompatAdapter(Context context, int i, MmTelInterfaceAdapter mmTelInterfaceAdapter) {
        initialize(context, i);
        this.mCompatFeature = mmTelInterfaceAdapter;
    }

    public boolean queryCapabilityConfiguration(int i, int i2) {
        int iConvertCapability = convertCapability(i, i2);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final int[] iArr = {-1};
        int iIntValue = ((Integer) REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(i2), -1)).intValue();
        try {
            this.mCompatFeature.getConfigInterface().getFeatureValue(iConvertCapability, iIntValue, new ConfigListener(this, iConvertCapability, iIntValue, countDownLatch) { // from class: com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.3
                @Override // com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.ConfigListener
                public void getFeatureValueReceived(int i3) {
                    iArr[0] = i3;
                }
            });
        } catch (RemoteException unused) {
            Log.w("MmTelFeatureCompat", "queryCapabilityConfiguration");
        }
        try {
            countDownLatch.await(2000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.w("MmTelFeatureCompat", "queryCapabilityConfiguration - error waiting: " + e.getMessage());
        }
        return iArr[0] == 1;
    }

    public void changeEnabledCapabilities(CapabilityChangeRequest capabilityChangeRequest, final ImsFeature.CapabilityCallbackProxy capabilityCallbackProxy) {
        TimeUnit timeUnit;
        int i;
        MmTelFeatureCompatAdapter mmTelFeatureCompatAdapter = this;
        if (capabilityChangeRequest == null) {
            return;
        }
        try {
            IImsConfig configInterface = mmTelFeatureCompatAdapter.mCompatFeature.getConfigInterface();
            Iterator it = capabilityChangeRequest.getCapabilitiesToDisable().iterator();
            while (true) {
                boolean zHasNext = it.hasNext();
                timeUnit = TimeUnit.MILLISECONDS;
                i = 1;
                if (!zHasNext) {
                    break;
                }
                final CapabilityChangeRequest.CapabilityPair capabilityPair = (CapabilityChangeRequest.CapabilityPair) it.next();
                CountDownLatch countDownLatch = new CountDownLatch(1);
                int iConvertCapability = mmTelFeatureCompatAdapter.convertCapability(capabilityPair.getCapability(), capabilityPair.getRadioTech());
                int iIntValue = ((Integer) REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(capabilityPair.getRadioTech()), -1)).intValue();
                Log.i("MmTelFeatureCompat", "changeEnabledCapabilities - cap: " + iConvertCapability + " radioTech: " + iIntValue + " disabled");
                configInterface.setFeatureValue(iConvertCapability, iIntValue, 0, new ConfigListener(mmTelFeatureCompatAdapter, iConvertCapability, iIntValue, countDownLatch) { // from class: com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.4
                    @Override // com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.ConfigListener
                    public void setFeatureValueReceived(int i2) {
                        if (i2 != 0) {
                            ImsFeature.CapabilityCallbackProxy capabilityCallbackProxy2 = capabilityCallbackProxy;
                            if (capabilityCallbackProxy2 == null) {
                                return;
                            } else {
                                capabilityCallbackProxy2.onChangeCapabilityConfigurationError(capabilityPair.getCapability(), capabilityPair.getRadioTech(), -1);
                            }
                        }
                        Log.i("MmTelFeatureCompat", "changeEnabledCapabilities - setFeatureValueReceived with value " + i2);
                    }
                });
                countDownLatch.await(2000L, timeUnit);
            }
            for (final CapabilityChangeRequest.CapabilityPair capabilityPair2 : capabilityChangeRequest.getCapabilitiesToEnable()) {
                CountDownLatch countDownLatch2 = new CountDownLatch(i);
                int iConvertCapability2 = mmTelFeatureCompatAdapter.convertCapability(capabilityPair2.getCapability(), capabilityPair2.getRadioTech());
                int iIntValue2 = ((Integer) REG_TECH_TO_NET_TYPE.getOrDefault(Integer.valueOf(capabilityPair2.getRadioTech()), -1)).intValue();
                Log.i("MmTelFeatureCompat", "changeEnabledCapabilities - cap: " + iConvertCapability2 + " radioTech: " + iIntValue2 + " enabled");
                configInterface.setFeatureValue(iConvertCapability2, iIntValue2, 1, new ConfigListener(mmTelFeatureCompatAdapter, iConvertCapability2, iIntValue2, countDownLatch2) { // from class: com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.5
                    @Override // com.android.internal.telephony.ims.MmTelFeatureCompatAdapter.ConfigListener
                    public void setFeatureValueReceived(int i2) {
                        if (i2 != 1) {
                            ImsFeature.CapabilityCallbackProxy capabilityCallbackProxy2 = capabilityCallbackProxy;
                            if (capabilityCallbackProxy2 == null) {
                                return;
                            } else {
                                capabilityCallbackProxy2.onChangeCapabilityConfigurationError(capabilityPair2.getCapability(), capabilityPair2.getRadioTech(), -1);
                            }
                        }
                        Log.i("MmTelFeatureCompat", "changeEnabledCapabilities - setFeatureValueReceived with value " + i2);
                    }
                });
                countDownLatch2.await(2000L, timeUnit);
                i = 1;
                mmTelFeatureCompatAdapter = this;
            }
        } catch (RemoteException | InterruptedException e) {
            Log.w("MmTelFeatureCompat", "changeEnabledCapabilities: Error processing: " + e.getMessage());
        }
    }

    public ImsCallProfile createCallProfile(int i, int i2) {
        try {
            return this.mCompatFeature.createCallProfile(this.mSessionId, i, i2);
        } catch (RemoteException e) {
            Phone$$ExternalSyntheticBUOutline1.m(e.getMessage());
            return null;
        }
    }

    public IImsCallSession createCallSessionInterface(ImsCallProfile imsCallProfile) throws RemoteException {
        return this.mCompatFeature.createCallSession(this.mSessionId, imsCallProfile);
    }

    public IImsUt getUtInterface() throws RemoteException {
        return this.mCompatFeature.getUtInterface();
    }

    public IImsEcbm getEcbmInterface() throws RemoteException {
        return this.mCompatFeature.getEcbmInterface();
    }

    public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
        return this.mCompatFeature.getMultiEndpointInterface();
    }

    public int getFeatureState() {
        try {
            return this.mCompatFeature.getFeatureState();
        } catch (RemoteException e) {
            Phone$$ExternalSyntheticBUOutline1.m(e.getMessage());
            return 0;
        }
    }

    public void setUiTtyMode(int i, Message message) {
        try {
            this.mCompatFeature.setUiTTYMode(i, message);
        } catch (RemoteException e) {
            Phone$$ExternalSyntheticBUOutline1.m(e.getMessage());
        }
    }

    public void onFeatureRemoved() {
        ((MmTelFeature) this).mContext.unregisterReceiver(this.mReceiver);
        try {
            this.mCompatFeature.endSession(this.mSessionId);
            this.mCompatFeature.removeRegistrationListener(this.mListener);
            ImsRegistrationCompatAdapter imsRegistrationCompatAdapter = this.mRegCompatAdapter;
            if (imsRegistrationCompatAdapter != null) {
                this.mCompatFeature.removeRegistrationListener(imsRegistrationCompatAdapter.getRegistrationListener());
            }
        } catch (RemoteException e) {
            Log.w("MmTelFeatureCompat", "onFeatureRemoved: Couldn't end session: " + e.getMessage());
        }
    }

    public void onFeatureReady() {
        Log.i("MmTelFeatureCompat", "onFeatureReady called!");
        ((MmTelFeature) this).mContext.registerReceiver(this.mReceiver, new IntentFilter(ACTION_IMS_INCOMING_CALL));
        try {
            this.mSessionId = this.mCompatFeature.startSession(createIncomingCallPendingIntent(), new ImsRegistrationListenerBase());
            this.mCompatFeature.addRegistrationListener(this.mListener);
            this.mCompatFeature.addRegistrationListener(this.mRegCompatAdapter.getRegistrationListener());
        } catch (RemoteException e) {
            Log.e("MmTelFeatureCompat", "Couldn't start compat feature: " + e.getMessage());
        }
    }

    public void enableIms() throws RemoteException {
        this.mCompatFeature.turnOnIms();
    }

    public void disableIms() throws RemoteException {
        this.mCompatFeature.turnOffIms();
    }

    public IImsConfig getOldConfigInterface() {
        try {
            return this.mCompatFeature.getConfigInterface();
        } catch (RemoteException e) {
            Log.w("MmTelFeatureCompat", "getOldConfigInterface(): " + e.getMessage());
            return null;
        }
    }

    public void addRegistrationAdapter(ImsRegistrationCompatAdapter imsRegistrationCompatAdapter) throws RemoteException {
        this.mRegCompatAdapter = imsRegistrationCompatAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MmTelFeature.MmTelCapabilities convertCapabilities(int[] iArr) {
        boolean[] zArr = new boolean[iArr.length];
        for (int i = 0; i <= 5 && i < iArr.length; i++) {
            int i2 = iArr[i];
            if (i2 == i) {
                zArr[i] = true;
            } else if (i2 == -1) {
                zArr[i] = false;
            }
        }
        MmTelFeature.MmTelCapabilities mmTelCapabilities = new MmTelFeature.MmTelCapabilities();
        if (zArr[0] || zArr[2]) {
            mmTelCapabilities.addCapabilities(1);
        }
        if (zArr[1] || zArr[3]) {
            mmTelCapabilities.addCapabilities(2);
        }
        if (zArr[4] || zArr[5]) {
            mmTelCapabilities.addCapabilities(4);
        }
        Log.i("MmTelFeatureCompat", "convertCapabilities - capabilities: " + mmTelCapabilities);
        return mmTelCapabilities;
    }

    private PendingIntent createIncomingCallPendingIntent() {
        Intent intent = new Intent(ACTION_IMS_INCOMING_CALL);
        intent.setPackage("com.android.phone");
        return PendingIntent.getBroadcast(((MmTelFeature) this).mContext, 0, intent, 167772160);
    }
}
