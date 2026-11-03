package com.android.services.telephony.relay.phonecall.relayaudio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import com.android.services.telephony.relay.phonecall.RelayLog;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;
import com.miui.audiomonitor.distaudio.data.DistAudioRoute;
import com.miui.audiomonitor.distaudio.utils.ResultCode;
import com.xiaomi.distaudiokit.DistAudioCallback;
import com.xiaomi.distaudiokit.MiAudioManager;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class DistAudioDeviceProvider {
    private static final DistAudioDeviceProvider INSTANCE = new DistAudioDeviceProvider();
    private static final int SIMPLE_FLAG = 2304;
    private static final int SIMPLE_RATE = 8000;
    private static final String TAG = "DistAudioDeviceProvider";
    private MiAudioManager mMiAudioManager;
    private DistAudioRoute mRelayAudio;
    private final Context mContext = RelayUtils.System.getContext();
    private HashMap mDistAudioDeviceInfos = new HashMap();
    private final HashMap mDistAudioRoutes = new HashMap();
    private final DistAudioCallback mDistAudioCallback = new DistAudioCallback() { // from class: com.android.services.telephony.relay.phonecall.relayaudio.DistAudioDeviceProvider.1
        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onDistAudioDeviceAdded(DistAudioDeviceInfo distAudioDeviceInfo) {
            RelayLog.logi(DistAudioDeviceProvider.TAG, "onDistAudioDeviceAdded, " + distAudioDeviceInfo);
            if (distAudioDeviceInfo != null) {
                DistAudioDeviceProvider.this.mDistAudioDeviceInfos.put(distAudioDeviceInfo.getDeviceId(), distAudioDeviceInfo);
                DistAudioDeviceProvider.this.mDistAudioRoutes.put(distAudioDeviceInfo.getDeviceId(), DistAudioDeviceProvider.this.createDistAudioRouteForDevice(distAudioDeviceInfo));
            }
        }

        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onDistAudioDeviceRemoved(DistAudioDeviceInfo distAudioDeviceInfo) {
            RelayLog.logi(DistAudioDeviceProvider.TAG, "onDistAudioDeviceRemoved, " + distAudioDeviceInfo);
            DistAudioDeviceProvider.this.mDistAudioDeviceInfos.remove(distAudioDeviceInfo.getDeviceId());
            DistAudioDeviceProvider.this.mDistAudioRoutes.remove(distAudioDeviceInfo.getDeviceId());
        }

        @Override // com.xiaomi.distaudiokit.DistAudioCallback
        public void onConnectionStatus(DistAudioDeviceInfo distAudioDeviceInfo, int i, String str) {
            RelayLog.logi(DistAudioDeviceProvider.TAG, "onConnectionStatus " + distAudioDeviceInfo + ", " + i + ", " + str);
            if (i == ResultCode.RESULT_OK.getCode()) {
                RelayLog.logi(DistAudioDeviceProvider.TAG, "deviceInfo.getDeviceId() " + distAudioDeviceInfo.getDeviceId());
            }
        }
    };

    private DistAudioDeviceProvider() {
    }

    public static synchronized DistAudioDeviceProvider getInstance() {
        return INSTANCE;
    }

    public void registerMiAudioManager() {
        try {
            RelayLog.logi(TAG, "registerMiAudioManager");
            if (this.mMiAudioManager == null) {
                MiAudioManager miAudioManager = new MiAudioManager(this.mContext);
                this.mMiAudioManager = miAudioManager;
                miAudioManager.registerDistAudioCallback(this.mContext.getPackageName(), this.mDistAudioCallback);
                getAllDistAudioRoutes();
            }
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    public void releaseMiAudioManager() {
        DistAudioDeviceInfo deviceInfo;
        try {
            RelayLog.logi(TAG, "releaseMiAudioManager");
            if (this.mMiAudioManager != null) {
                DistAudioRoute distAudioRoute = this.mRelayAudio;
                if (distAudioRoute != null && (deviceInfo = distAudioRoute.getDeviceInfo()) != null) {
                    disconnectDistAudioDevice(deviceInfo.getDeviceId());
                }
                this.mMiAudioManager.unregisterDistAudioCallback(this.mContext.getPackageName());
                this.mDistAudioRoutes.clear();
                this.mDistAudioDeviceInfos.clear();
                this.mMiAudioManager.release();
                this.mMiAudioManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    public void connectDistAudioDevice(String str) {
        try {
            if (this.mMiAudioManager != null) {
                DistAudioRoute distAudioRoute = (DistAudioRoute) this.mDistAudioRoutes.get(str);
                if (distAudioRoute == null) {
                    RelayLog.logi(TAG, "connectDistAudioDevice not find " + str);
                } else {
                    RelayLog.logi(TAG, "connectDistAudioDevice route=" + distAudioRoute.getDeviceInfo());
                    this.mRelayAudio = distAudioRoute;
                    this.mMiAudioManager.connectDistAudioDevice(distAudioRoute);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    public void disconnectDistAudioDevice(String str) {
        try {
            if (this.mMiAudioManager != null) {
                DistAudioRoute distAudioRoute = (DistAudioRoute) this.mDistAudioRoutes.get(str);
                if (distAudioRoute == null) {
                    RelayLog.logi(TAG, "disconnectDistAudioDevice not find " + str);
                } else {
                    RelayLog.logi(TAG, "disconnectDistAudioDevice route=" + distAudioRoute.getDeviceInfo());
                    this.mRelayAudio = null;
                    this.mMiAudioManager.disconnectDistAudioDevice(distAudioRoute);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    public String getCurDistAudioRoute() {
        DistAudioRoute curDistAudioRoute;
        try {
            MiAudioManager miAudioManager = this.mMiAudioManager;
            curDistAudioRoute = miAudioManager != null ? miAudioManager.getCurDistAudioRoute() : null;
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
        RelayLog.logi(TAG, "getCurDistAudioRoute route=" + curDistAudioRoute);
        if (curDistAudioRoute == null || curDistAudioRoute.getDeviceInfo() == null) {
            return null;
        }
        return curDistAudioRoute.getDeviceInfo().getDeviceId();
    }

    private void getAllDistAudioDevices() {
        this.mDistAudioDeviceInfos = new HashMap();
        try {
            MiAudioManager miAudioManager = this.mMiAudioManager;
            if (miAudioManager != null) {
                ArrayList arrayList = (ArrayList) miAudioManager.getDistAudioDevices();
                if (arrayList != null && arrayList.size() > 0) {
                    int size = arrayList.size();
                    int i = 0;
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        DistAudioDeviceInfo distAudioDeviceInfo = (DistAudioDeviceInfo) obj;
                        if (distAudioDeviceInfo != null) {
                            this.mDistAudioDeviceInfos.put(distAudioDeviceInfo.getDeviceId(), distAudioDeviceInfo);
                        }
                    }
                }
                RelayLog.logi(TAG, "getAllDistAudioDevices distAudioDeviceInfos = " + arrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DistAudioRoute createDistAudioRouteForDevice(DistAudioDeviceInfo distAudioDeviceInfo) {
        return new DistAudioRoute.Builder().setDistAudioDeviceInfo(distAudioDeviceInfo).setAudioAttributes(new AudioAttributes.Builder().setFlags(SIMPLE_FLAG).setUsage(1).setContentType(2).build()).setPlayerFormat(new AudioFormat.Builder().setEncoding(2).setSampleRate(8000).setChannelMask(12).build()).setRecorderFormat(new AudioFormat.Builder().setEncoding(2).setSampleRate(8000).setChannelMask(12).build()).build();
    }

    public HashMap getDistAudioRoutes() {
        return this.mDistAudioRoutes;
    }

    public void getAllDistAudioRoutes() {
        try {
            getAllDistAudioDevices();
            HashMap map = this.mDistAudioDeviceInfos;
            if (map != null && map.size() > 0) {
                for (DistAudioDeviceInfo distAudioDeviceInfo : this.mDistAudioDeviceInfos.values()) {
                    this.mDistAudioRoutes.put(distAudioDeviceInfo.getDeviceId(), createDistAudioRouteForDevice(distAudioDeviceInfo));
                }
            }
            RelayLog.logi(TAG, "getAllDistAudioRoutes mDistAudioRoutes=" + this.mDistAudioRoutes.size());
        } catch (Exception e) {
            e.printStackTrace();
            RelayLog.loge(TAG, e.getMessage());
        }
    }

    public DistAudioRoute findAudioRouteByDeviceId(String str) {
        if (this.mDistAudioRoutes.containsKey(str)) {
            return (DistAudioRoute) this.mDistAudioRoutes.get(str);
        }
        RelayLog.logi(TAG, "Can not find device:" + str);
        return null;
    }

    public DistAudioDeviceInfo findDeviceInfoByDeviceId(String str) {
        if (this.mDistAudioDeviceInfos.containsKey(str)) {
            return (DistAudioDeviceInfo) this.mDistAudioDeviceInfos.get(str);
        }
        RelayLog.logi(TAG, "Can not find device:" + str);
        return null;
    }
}
