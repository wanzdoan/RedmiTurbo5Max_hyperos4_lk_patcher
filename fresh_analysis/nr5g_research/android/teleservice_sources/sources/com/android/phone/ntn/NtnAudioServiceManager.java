package com.android.phone.ntn;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.Map;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NtnAudioServiceManager {
    public static final PhoneGlobals sPhoneGlobal = PhoneGlobals.getInstance();
    private Map mCallStates;
    private int mCount;
    public Handler mHandler;
    private int mHighRailAudioType;
    private boolean mIsNtnAudioServiceStarted = false;
    private int mLowRailAudioType;
    private TelephonyCallBack mTelephonyCallBack;

    public NtnAudioServiceManager() {
        PhoneGlobals phoneGlobals = sPhoneGlobal;
        this.mHighRailAudioType = phoneGlobals.getResources().getInteger(R.integer.config_high_rail_ntn_call_audio_mode);
        this.mLowRailAudioType = phoneGlobals.getResources().getInteger(R.integer.config_low_rail_ntn_call_audio_mode);
        this.mTelephonyCallBack = null;
        this.mCallStates = new ArrayMap();
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.ntn.NtnAudioServiceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                int ntnVoiceRate = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNtnVoiceRate(true);
                if (ntnVoiceRate == -1) {
                    NtnLogUtils.logd("NtnAudioServiceManager", "voiceRate is invaild, retry " + NtnAudioServiceManager.this.mCount);
                    sendEmptyMessageDelayed(1, 1000L);
                } else {
                    NtnLogUtils.logd("NtnAudioServiceManager", "NtnAudioServiceManager send broadcast, voiceRate:" + ntnVoiceRate);
                    Intent intent = new Intent("com.miui.audiomonitor.csaudio.STARNET54");
                    intent.setPackage(RelayUtils.Relay.AUDIO_PACKAGE_NAME);
                    intent.putExtra("codecType", ntnVoiceRate);
                    PhoneGlobals.getInstance().sendBroadcast(intent);
                }
                NtnAudioServiceManager.this.mCount++;
            }
        };
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onPreciseCallStateChanged(int i, int i2) {
            if (MiuiPhoneUtils.isValidatePhoneId(i)) {
                NtnLogUtils.logd("NtnAudioServiceManager", "onPreciseCallStateChanged phoneid:" + i + ", callstate : " + i2);
                NtnAudioServiceManager.this.handleSatelliteAudio(i, i2);
            }
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            NtnLogUtils.logd("NtnAudioServiceManager", "register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private void unRegisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            NtnLogUtils.logi("NtnAudioServiceManager", "unregister TelephonyCallBack");
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    public void launchAudioServiceManager(boolean z) {
        if (z) {
            registerTelephonyCallBack();
        } else {
            unRegisterTelephonyCallBack();
            this.mCallStates.clear();
        }
    }

    public void handleSatelliteAudio(int i, int i2) {
        NtnLogUtils.logi("NtnAudioServiceManager", "proc service: CSAudioService, state = " + i2);
        if (isCallStateChanged(i, i2)) {
            this.mCallStates.put(Integer.valueOf(i), Integer.valueOf(i2));
            int ntnAudioServiceType = getNtnAudioServiceType();
            if ((i2 != 3 && i2 != 1) || this.mIsNtnAudioServiceStarted) {
                if ((i2 == 8 || i2 == 7 || i2 == 0) && this.mIsNtnAudioServiceStarted) {
                    if (ntnAudioServiceType == 1) {
                        NtnLogUtils.logi("NtnAudioServiceManager", "In 54 platform, clear call audio parameters!");
                        ((AudioManager) PhoneGlobals.getInstance().getSystemService("audio")).setParameters("call_state=1;call_type=UNKNOWN;vsid=286347264");
                        this.mIsNtnAudioServiceStarted = false;
                        return;
                    } else if (ntnAudioServiceType == 2) {
                        stopAudioService();
                        return;
                    } else {
                        NtnLogUtils.logi("NtnAudioServiceManager", "stop handleSatelliteAudio something wrong!");
                        return;
                    }
                }
                return;
            }
            if (ntnAudioServiceType == 1) {
                NtnLogUtils.logi("NtnAudioServiceManager", "set call audio parameters!");
                ((AudioManager) PhoneGlobals.getInstance().getSystemService("audio")).setParameters("call_state=2;call_type=UNKNOWN;vsid=286347264");
                this.mIsNtnAudioServiceStarted = true;
            } else {
                if (ntnAudioServiceType == 2) {
                    try {
                        NtnLogUtils.logi("NtnAudioServiceManager", "start CSAudioService!");
                        sPhoneGlobal.startForegroundService(buildNtnAudioIntent());
                        this.mIsNtnAudioServiceStarted = true;
                        this.mCount = 0;
                        this.mHandler.sendEmptyMessageDelayed(1, 500L);
                        return;
                    } catch (Exception e) {
                        NtnLogUtils.loge("NtnAudioServiceManager", "start CSAudioService failed, exception: " + e);
                        return;
                    }
                }
                NtnLogUtils.logi("NtnAudioServiceManager", "start handleSatelliteAudio something wrong!");
            }
        }
    }

    private void stopAudioService() {
        try {
            if (this.mHandler.hasMessages(1)) {
                this.mHandler.removeMessages(1);
            }
            Intent intentBuildNtnAudioIntent = buildNtnAudioIntent();
            NtnLogUtils.logi("NtnAudioServiceManager", "stop CSAudioService");
            sPhoneGlobal.stopService(intentBuildNtnAudioIntent);
            this.mIsNtnAudioServiceStarted = false;
        } catch (Exception e) {
            NtnLogUtils.loge("NtnAudioServiceManager", "stop CSAudioService is failed, exception: " + e);
        }
    }

    private boolean isCallStateChanged(int i, int i2) {
        boolean z = i2 != (this.mCallStates.isEmpty() ? -1 : ((Integer) this.mCallStates.get(Integer.valueOf(i))).intValue());
        NtnLogUtils.logi("NtnAudioServiceManager", "isCallStateChanged changed=" + z);
        return z;
    }

    private Intent buildNtnAudioIntent() {
        Intent intent = new Intent();
        intent.setPackage(RelayUtils.Relay.AUDIO_PACKAGE_NAME);
        intent.setAction("com.miui.audiomonitor.action.csAudioService");
        if (TelephonyManagerEx.getDefault().isSatelliteBox()) {
            intent.putExtra("type", NtnBoxServiceManager.getInstance().getBtConnectType());
            return intent;
        }
        if (TelephonyCommonUtils.isNtnChipVendor_54() && TelephonyCommonUtils.getNtnEphemType() == TelephonyCommonUtils.NTN_EPHEM_LOW_RAIL) {
            intent.putExtra("type", 2);
        }
        return intent;
    }

    private int getNtnAudioServiceType() {
        int ntnEphemType = TelephonyCommonUtils.getNtnEphemType();
        if (ntnEphemType == TelephonyCommonUtils.NTN_EPHEM_HIGH_RAIL) {
            return this.mHighRailAudioType;
        }
        if (ntnEphemType == TelephonyCommonUtils.NTN_EPHEM_LOW_RAIL) {
            return this.mLowRailAudioType;
        }
        NtnLogUtils.logi("NtnAudioServiceManager", "error, unknown ntn audio service type");
        return 0;
    }
}
