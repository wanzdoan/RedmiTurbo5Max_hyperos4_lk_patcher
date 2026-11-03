package com.android.phone.network;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiCurrentStateMonitor;
import com.android.telephony.Rlog;
import com.ot.pubsub.a.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetworkMonitor {
    private static MiuiNetworkMonitor mInstance;
    private String mCity;
    private ConnectivityManager mConnManager;
    private String mCountry;
    private Handler mHandler;
    private String mKnownName;
    private final Map mLastLinkParams;
    private double mLatitude;
    private double mLongitude;
    private MiuiCurrentStateMonitor mMiuiCurrentStateMonitor;
    private List mMonitorListenerList;
    private Network mNetwork;
    private List mOptimizationWhiteList;
    private String mState;
    private String mStreet;
    private String mStreetNumber;
    private boolean[] mCongestionParamsHasReport = {false, false, false, false, false};
    private int mCurrentReason = 0;
    private int mMonitorCount = 0;
    private long mLastRttWorseTime = -1;
    private long mLastLinkWorseTime = -1;
    private int mCurWebContentType = -1;
    private boolean mIsStuck = false;
    private int mVideoQuality = 1;
    private IActivityChangeListener.Stub mActivityChangeListener = new IActivityChangeListener.Stub() { // from class: com.android.phone.network.MiuiNetworkMonitor.1
        public void onActivityChanged(ComponentName componentName, ComponentName componentName2) {
            int i;
            if (componentName == null || componentName2 == null) {
                return;
            }
            String string = componentName2.toString();
            componentName.toString();
            List optWhiteList = MiuiNetworkMonitor.this.getOptWhiteList("ai_congestion_live_stream_white_list_default");
            if (optWhiteList == null || optWhiteList.isEmpty()) {
                i = -1;
                break;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= optWhiteList.size()) {
                    i = -1;
                    break;
                } else {
                    if (!TextUtils.isEmpty(string) && string.contains((CharSequence) optWhiteList.get(i2))) {
                        MiuiNetworkMonitor.this.logd("enter live stream scenario");
                        i = 1000;
                        break;
                    }
                    i2++;
                }
            }
            List optWhiteList2 = MiuiNetworkMonitor.this.getOptWhiteList("ai_congestion_short_video_white_list_default");
            if (optWhiteList2 != null && !optWhiteList2.isEmpty()) {
                for (int i3 = 0; i3 < optWhiteList2.size(); i3++) {
                    if (!TextUtils.isEmpty(string) && string.contains((CharSequence) optWhiteList2.get(i3))) {
                        MiuiNetworkMonitor.this.logd("enter short video scenario");
                        i = 1001;
                        break;
                    }
                }
            }
            List optWhiteList3 = MiuiNetworkMonitor.this.getOptWhiteList("ai_congestion_website_white_list_default");
            for (int i4 = 0; i4 < optWhiteList3.size(); i4++) {
                if (!TextUtils.isEmpty(string) && string.contains((CharSequence) optWhiteList3.get(i4))) {
                    MiuiNetworkMonitor.this.logd("enter website scenario");
                    i = 1002;
                    break;
                }
            }
            if (MiuiNetworkMonitor.this.mCurWebContentType == i || i == -1) {
                if (i == -1) {
                    MiuiNetworkMonitor.this.mMonitorCount = 0;
                    return;
                }
                return;
            }
            MiuiNetworkMonitor.this.logd("web content type has been changed to " + i + " from " + MiuiNetworkMonitor.this.mCurWebContentType);
            MiuiNetworkMonitor.this.mCurWebContentType = i;
        }
    };
    private final ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback(1) { // from class: com.android.phone.network.MiuiNetworkMonitor.2
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            if (network != null) {
                MiuiNetworkMonitor.this.mNetwork = network;
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            MiuiNetworkMonitor.this.mNetwork = null;
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.network.MiuiNetworkMonitor.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            MiuiNetworkMonitor.this.logd("intent.getAction():" + intent.getAction());
            if (intent.getAction().equals("com.android.phone.intent.action.LINK_RELIABILITY")) {
                if (intent.hasExtra("RTT_STATUS")) {
                    MiuiNetworkMonitor.this.mHandler.sendMessage(MiuiNetworkMonitor.this.mHandler.obtainMessage(2, intent));
                    return;
                }
                return;
            }
            if (intent.getAction().equals("com.android.phone.intent.action.CELLULAR_LINK_DATA_STAT")) {
                MiuiNetworkMonitor.this.mHandler.sendMessage(MiuiNetworkMonitor.this.mHandler.obtainMessage(3, intent));
                return;
            }
            if (intent.getAction().equals("miui.intent.action.VIDEO_INFO")) {
                int intExtra = intent.getIntExtra("render-quality", 1);
                int intExtra2 = intent.getIntExtra("pid", -1);
                int intExtra3 = intent.getIntExtra("uid", -1);
                int intExtra4 = intent.getIntExtra("codec-id", -1);
                int intExtra5 = intent.getIntExtra("render-start", -1);
                MiuiNetworkMonitor.this.logd("videoQuality:" + intExtra + ",pid:" + intExtra2 + ",uid:" + intExtra3 + ",codecId:" + intExtra4 + ",start:" + intExtra5);
                if (intExtra == 0 && MiuiNetworkMonitor.this.mVideoQuality != intExtra && MiuiNetworkMonitor.this.mCurWebContentType == 1000) {
                    MiuiNetworkMonitor.this.mVideoQuality = 0;
                    MiuiNetworkMonitor.this.reportParamsForAI2(4);
                }
                if (intExtra == 1 && MiuiNetworkMonitor.this.mVideoQuality == 0) {
                    if (MiuiNetworkMonitor.this.mHandler.hasMessages(6)) {
                        return;
                    }
                    MiuiNetworkMonitor.this.logd("will update video quality after 5s...");
                    MiuiNetworkMonitor.this.mHandler.sendMessageDelayed(MiuiNetworkMonitor.this.mHandler.obtainMessage(6), MirrorDesktopHelper.TIMEOUT_MILLIS);
                    return;
                }
                if (MiuiNetworkMonitor.this.mHandler.hasMessages(6)) {
                    MiuiNetworkMonitor.this.mHandler.removeMessages(6);
                    return;
                }
                return;
            }
            if (intent.getAction().equals("com.android.phone.intent.action.AI_CONGESTION_PREDICT")) {
                boolean zEquals = "1".equals(intent.getStringExtra(a.L));
                MiuiNetworkMonitor.this.logd("PREDICT RESULT stuck:" + zEquals);
                if (zEquals != MiuiNetworkMonitor.this.mIsStuck) {
                    MiuiNetworkMonitor.this.mIsStuck = zEquals;
                    MiuiNetworkMonitor.this.logd("startMonitor isStuck:" + MiuiNetworkMonitor.this.mIsStuck + ",webContentType:" + MiuiNetworkMonitor.this.mCurWebContentType + ",mVideoQuality:" + MiuiNetworkMonitor.this.mVideoQuality + ",mMonitorCount:" + MiuiNetworkMonitor.this.mMonitorCount);
                    MiuiNetworkMonitor.this.startMonitor();
                    return;
                }
                return;
            }
            if (intent.getAction().equals("com.android.server.telephony.action.AI_CONGESTION_STATISTIC_REQUESTED_DONE")) {
                MiuiNetworkMonitor.this.mHandler.sendMessage(MiuiNetworkMonitor.this.mHandler.obtainMessage(8, intent));
            } else if (intent.getAction().equals("com.android.server.telephony.action.AI_CONGESTION_PARAMETERS_REQUESTED_DONE")) {
                MiuiNetworkMonitor.this.mHandler.sendMessage(MiuiNetworkMonitor.this.mHandler.obtainMessage(9, intent));
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    public interface IMiuiNetworkMonitorListener {
        default void on3rdAppPreload() {
        }

        default void onTcpLinkBetter(Network network) {
        }

        default void onTcpLinkBlock(Network network) {
        }

        default void onTcpLinkWorse(Network network) {
        }

        void onTcpRTTBetter(Network network, int i);

        void onTcpRTTWorse(Network network, int i, int i2);
    }

    public enum LinkType {
        LINK(0, "LINK_DATA"),
        RTT(1, "RTT_DATA");

        private String paramsName;
        private int value;

        LinkType(int i, String str) {
            this.value = i;
            this.paramsName = str;
        }

        public int getValue() {
            return this.value;
        }

        public String getParamsName() {
            return this.paramsName;
        }

        public static LinkType getByValue(int i) {
            for (LinkType linkType : values()) {
                if (linkType.getValue() == i) {
                    return linkType;
                }
            }
            return null;
        }
    }

    public enum RttStatus {
        RTT_STATUS_UNKNOWN(-1),
        RTT_STATUS_BETTER(0),
        RTT_STATUS_WORSE(1);

        private int value;

        RttStatus(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static RttStatus getByValue(int i) {
            for (RttStatus rttStatus : values()) {
                if (rttStatus.getValue() == i) {
                    return rttStatus;
                }
            }
            return null;
        }
    }

    public enum LinkStatus {
        LINK_STATUS_UNKNOWN(-1),
        LINK_STATUS_BETTER(0),
        LINK_STATUS_WORSE(1),
        LINK_STATUS_BLOCK(2);

        private int value;

        LinkStatus(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static LinkStatus getByValue(int i) {
            for (LinkStatus linkStatus : values()) {
                if (linkStatus.getValue() == i) {
                    return linkStatus;
                }
            }
            return null;
        }
    }

    private MiuiNetworkMonitor() {
        HandlerThread handlerThread = new HandlerThread("MiuiNetworkMonitor");
        handlerThread.start();
        this.mHandler = new WorkHandler(handlerThread.getLooper());
        this.mConnManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mMiuiCurrentStateMonitor = MiuiCurrentStateMonitor.getInstance();
        this.mMonitorListenerList = new ArrayList();
        this.mOptimizationWhiteList = new ArrayList();
        if (MiuiPhoneUtils.isSupportSNANetworkRecovery()) {
            this.mOptimizationWhiteList.addAll(getOptWhiteList("rtt_opt_white_list_default"));
        }
        if (MiuiPhoneUtils.isSupportProfessionModeOptimization() && !Build.IS_INTERNATIONAL_BUILD) {
            this.mOptimizationWhiteList.addAll(getOptWhiteList("profession_mode_opt_white_list_default"));
        }
        this.mLastLinkParams = new HashMap();
        register();
        if (Build.IS_INTERNATIONAL_BUILD) {
            return;
        }
        registerActivityChangeListener();
    }

    private void registerActivityChangeListener() {
        if (this.mActivityChangeListener != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(getOptWhiteList("ai_congestion_live_stream_white_list_default"));
            arrayList.addAll(getOptWhiteList("ai_congestion_short_video_white_list_default"));
            arrayList.addAll(getOptWhiteList("ai_congestion_website_white_list_default"));
            ProcessManager.unregisterActivityChanageListener(this.mActivityChangeListener);
            ProcessManager.registerActivityChangeListener(this.mOptimizationWhiteList, arrayList, this.mActivityChangeListener);
        }
    }

    public static MiuiNetworkMonitor getInstance() {
        if (mInstance == null) {
            mInstance = new MiuiNetworkMonitor();
        }
        return mInstance;
    }

    private void register() {
        this.mConnManager.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(0).addCapability(12).build(), this.mNetworkCallback, this.mHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
        intentFilter.addAction("com.android.phone.intent.action.CELLULAR_LINK_DATA_STAT");
        intentFilter.addAction("miui.intent.action.VIDEO_INFO");
        intentFilter.addAction("com.android.phone.intent.action.AI_CONGESTION_PREDICT");
        intentFilter.addAction("com.android.server.telephony.action.AI_CONGESTION_STATISTIC_REQUESTED_DONE");
        intentFilter.addAction("com.android.server.telephony.action.AI_CONGESTION_PARAMETERS_REQUESTED_DONE");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    public void init() {
        if ((MiuiPhoneUtils.isSupportSNANetworkRecovery() || MiuiPhoneUtils.isSupportProfessionModeOptimization()) && !TelephonyCloudController.CLOUD_SNA_NETWORK_RECOVERY_DISABLED) {
            MiuiNetworkRecoveryManager.init();
        }
        logd("init");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMonitor() {
        if (this.mIsStuck) {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(7), 4000L);
            int i = this.mMonitorCount;
            if (i == 2) {
                if (this.mCurWebContentType == 1001) {
                    notify3rdAppPreload();
                }
                if (this.mCurWebContentType == 1000 && this.mVideoQuality == 0) {
                    notifyTcpRTTStatus(RttStatus.RTT_STATUS_WORSE, MiuiNetworkRecoveryManager.RttWorseLevelEnum.L2.getLevel(), 1);
                }
            } else if (i == 4) {
                reportParamsForAI2(2);
                if (this.mCurWebContentType == 1000 && this.mVideoQuality == 0) {
                    notifyTcpRTTStatus(RttStatus.RTT_STATUS_WORSE, MiuiNetworkRecoveryManager.RttWorseLevelEnum.L3.getLevel(), 1);
                } else {
                    notifyTcpRTTStatus(RttStatus.RTT_STATUS_WORSE, MiuiNetworkRecoveryManager.RttWorseLevelEnum.L2.getLevel(), 1);
                }
            } else if (i == 8) {
                reportParamsForAI2(3);
                notifyTcpRTTStatus(RttStatus.RTT_STATUS_WORSE, MiuiNetworkRecoveryManager.RttWorseLevelEnum.L3.getLevel(), 1);
                this.mHandler.removeMessages(7);
            }
            this.mMonitorCount++;
            return;
        }
        logd("NW is becoming good, stop any optimization..");
        this.mHandler.removeMessages(7);
        notifyTcpRTTStatus(RttStatus.RTT_STATUS_BETTER, MiuiNetworkRecoveryManager.RttWorseLevelEnum.L0.getLevel(), 1);
        this.mMonitorCount = 0;
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Intent intent;
            Intent intent2;
            MiuiNetworkMonitor.this.logd("handleMessage event:" + message.what);
            switch (message.what) {
                case 1:
                    if (MiuiNetworkMonitor.this.isSatisfiedPackageLimited() && (intent = (Intent) message.obj) != null) {
                        int intExtra = intent.getIntExtra("LINK_STATUS", LinkStatus.LINK_STATUS_UNKNOWN.getValue());
                        String stringExtra = intent.getStringExtra("PARAMS");
                        LinkStatus byValue = LinkStatus.getByValue(intExtra);
                        if (byValue != null) {
                            MiuiNetworkMonitor.this.mLastLinkParams.put(byValue.name(), stringExtra);
                        }
                        MiuiNetworkMonitor.this.notifyTcpLinkStatus(byValue);
                        if (byValue == LinkStatus.LINK_STATUS_WORSE) {
                            long jCurrentTimeMillis = System.currentTimeMillis();
                            if (jCurrentTimeMillis - MiuiNetworkMonitor.this.mLastLinkWorseTime > 1200000) {
                                MiuiNetworkMonitor.this.mMiuiCurrentStateMonitor.startUpdateLocation();
                                MiuiNetworkMonitor.this.mHandler.sendMessageDelayed(MiuiNetworkMonitor.this.mHandler.obtainMessage(5), 3000L);
                            }
                            MiuiNetworkMonitor.this.mLastLinkWorseTime = jCurrentTimeMillis;
                        }
                        MiuiNetworkMonitor.this.logd("link status changed event :" + MiuiNetworkMonitor.this.mMiuiCurrentStateMonitor.getForegroundPackageName() + ", linkStatus=" + byValue);
                        break;
                    }
                    break;
                case 2:
                    if (MiuiNetworkMonitor.this.isSatisfiedPackageLimited() && (intent2 = (Intent) message.obj) != null) {
                        int intExtra2 = intent2.getIntExtra("RTT_STATUS", RttStatus.RTT_STATUS_UNKNOWN.getValue());
                        int intExtra3 = intent2.getIntExtra("RTT_LEVEL", -1);
                        String stringExtra2 = intent2.getStringExtra("PARAMS");
                        RttStatus byValue2 = RttStatus.getByValue(intExtra2);
                        if (byValue2 != null) {
                            MiuiNetworkMonitor.this.mLastLinkParams.put(byValue2.name(), stringExtra2);
                        }
                        if (byValue2 == RttStatus.RTT_STATUS_WORSE) {
                            MiuiNetworkMonitor.this.logd("notifyTcpRTTStatus by SNA2.0");
                            MiuiNetworkMonitor.this.notifyTcpRTTStatus(byValue2, intExtra3, 0);
                            long jCurrentTimeMillis2 = System.currentTimeMillis();
                            if (jCurrentTimeMillis2 - MiuiNetworkMonitor.this.mLastRttWorseTime > 1200000) {
                                MiuiNetworkMonitor.this.mMiuiCurrentStateMonitor.startUpdateLocation();
                                MiuiNetworkMonitor.this.mHandler.sendMessageDelayed(MiuiNetworkMonitor.this.mHandler.obtainMessage(4), 3000L);
                            }
                            MiuiNetworkMonitor.this.mLastRttWorseTime = jCurrentTimeMillis2;
                        }
                        MiuiNetworkMonitor.this.logd("rtt status event :" + MiuiNetworkMonitor.this.mMiuiCurrentStateMonitor.getForegroundPackageName() + ", rttStatus=" + byValue2);
                        break;
                    }
                    break;
                case 3:
                    Intent intent3 = (Intent) message.obj;
                    if (intent3 != null) {
                        int intExtra4 = intent3.getIntExtra("TYPE", -1);
                        String stringExtra3 = intent3.getStringExtra("PARAMS");
                        LinkType byValue3 = LinkType.getByValue(intExtra4);
                        if (byValue3 != null) {
                            MiuiNetworkMonitor.this.mLastLinkParams.put(byValue3.getParamsName(), stringExtra3);
                            MiuiNetworkMonitor.this.logd("update link data:" + byValue3.getParamsName());
                        }
                    }
                    break;
                case 4:
                    MiuiNetworkMonitor.this.reportTcpRttWorse();
                    break;
                case 5:
                    MiuiNetworkMonitor.this.reportTcpLinkWorse();
                    break;
                case 6:
                    MiuiNetworkMonitor.this.logd("mVideoQuality is become LIVE_SMOOTH");
                    MiuiNetworkMonitor.this.mVideoQuality = 1;
                    break;
                case 7:
                    MiuiNetworkMonitor.this.startMonitor();
                    break;
                case 8:
                    MiuiNetworkMonitor.this.onReportStatistics((Intent) message.obj);
                    break;
                case 9:
                    MiuiNetworkMonitor.this.onReportParamsChanged((Intent) message.obj);
                    break;
            }
        }
    }

    public void registerMiuiNetworkMonitorListener(IMiuiNetworkMonitorListener iMiuiNetworkMonitorListener) {
        this.mMonitorListenerList.add(iMiuiNetworkMonitorListener);
    }

    private void notify3rdAppPreload() {
        List list = this.mMonitorListenerList;
        if (list == null || list.size() <= 0) {
            return;
        }
        Iterator it = this.mMonitorListenerList.iterator();
        while (it.hasNext()) {
            ((IMiuiNetworkMonitorListener) it.next()).on3rdAppPreload();
        }
        logd("notify3rdAppPreload done.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTcpRTTStatus(RttStatus rttStatus, int i, int i2) {
        TelephonyEvent.NetworkEvent networkEvent;
        List list = this.mMonitorListenerList;
        if (list != null && list.size() > 0) {
            for (IMiuiNetworkMonitorListener iMiuiNetworkMonitorListener : this.mMonitorListenerList) {
                if (rttStatus == RttStatus.RTT_STATUS_WORSE || rttStatus == RttStatus.RTT_STATUS_UNKNOWN) {
                    iMiuiNetworkMonitorListener.onTcpRTTWorse(this.mNetwork, i, i2);
                } else if (rttStatus == RttStatus.RTT_STATUS_BETTER) {
                    iMiuiNetworkMonitorListener.onTcpRTTBetter(this.mNetwork, i2);
                }
            }
        }
        Bundle bundle = new Bundle();
        if (rttStatus == RttStatus.RTT_STATUS_WORSE || rttStatus == RttStatus.RTT_STATUS_UNKNOWN) {
            bundle.putInt(com.ot.pubsub.b.a.d, i);
            bundle.putInt("version", i2);
            bundle.putParcelable("network", this.mNetwork);
            networkEvent = new TelephonyEvent.NetworkEvent("tcp_rtt_worse", bundle);
        } else if (rttStatus == RttStatus.RTT_STATUS_BETTER) {
            bundle.putInt("version", i2);
            bundle.putParcelable("network", this.mNetwork);
            networkEvent = new TelephonyEvent.NetworkEvent("tcp_rtt_better", bundle);
        } else {
            networkEvent = null;
        }
        if (networkEvent != null) {
            EventHubManager.getInstance().postEvent(networkEvent);
        }
        logd("notifyTcpRTTStatus, done , status:" + rttStatus + ", level:" + i + ", version:" + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTcpLinkStatus(LinkStatus linkStatus) {
        TelephonyEvent.NetworkEvent networkEvent;
        List list = this.mMonitorListenerList;
        if (list != null && list.size() > 0) {
            for (IMiuiNetworkMonitorListener iMiuiNetworkMonitorListener : this.mMonitorListenerList) {
                if (linkStatus == LinkStatus.LINK_STATUS_WORSE) {
                    iMiuiNetworkMonitorListener.onTcpLinkWorse(this.mNetwork);
                } else if (linkStatus == LinkStatus.LINK_STATUS_BETTER) {
                    iMiuiNetworkMonitorListener.onTcpLinkBetter(this.mNetwork);
                } else if (linkStatus == LinkStatus.LINK_STATUS_BLOCK) {
                    iMiuiNetworkMonitorListener.onTcpLinkBlock(this.mNetwork);
                }
            }
        }
        Bundle bundle = new Bundle();
        if (linkStatus == LinkStatus.LINK_STATUS_WORSE) {
            bundle.putParcelable("network", this.mNetwork);
            networkEvent = new TelephonyEvent.NetworkEvent("tcp_link_worse", bundle);
        } else if (linkStatus == LinkStatus.LINK_STATUS_BETTER) {
            bundle.putParcelable("network", this.mNetwork);
            networkEvent = new TelephonyEvent.NetworkEvent("tcp_link_better", bundle);
        } else if (linkStatus == LinkStatus.LINK_STATUS_BLOCK) {
            bundle.putParcelable("network", this.mNetwork);
            networkEvent = new TelephonyEvent.NetworkEvent("tcp_link_block", bundle);
        } else {
            networkEvent = null;
        }
        if (networkEvent != null) {
            EventHubManager.getInstance().postEvent(networkEvent);
        }
        logd("notifyTcpLinkStatus, done , status:" + linkStatus);
    }

    private void updateAddressFromLocation(Location location) {
        if (location == null) {
            logd("location is null");
            return;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        try {
            List<Address> fromLocation = new Geocoder(this.mContext, Locale.ENGLISH).getFromLocation(latitude, longitude, 1);
            if (fromLocation == null || fromLocation.isEmpty()) {
                return;
            }
            Address address = fromLocation.get(0);
            this.mCountry = address.getCountryName();
            this.mState = address.getAdminArea();
            this.mCity = address.getLocality();
            this.mStreet = address.getThoroughfare();
            this.mStreetNumber = address.getPostalCode();
            this.mKnownName = address.getFeatureName();
        } catch (Exception e) {
            loge("Get Location Error");
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportTcpRttWorse() {
        updateAddressFromLocation(this.mMiuiCurrentStateMonitor.getLocation());
        HashMap map = new HashMap();
        map.put("rttWorseParams", (String) this.mLastLinkParams.getOrDefault(RttStatus.RTT_STATUS_WORSE.name(), null));
        map.put("version", "2.0");
        addTcpWorseParams(map);
        MiuiTelephonyStatistic.getInstance().reportTcpRTTWorse(map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportTcpLinkWorse() {
        updateAddressFromLocation(this.mMiuiCurrentStateMonitor.getLocation());
        HashMap map = new HashMap();
        map.put("linkWorseParams", (String) this.mLastLinkParams.getOrDefault(LinkStatus.LINK_STATUS_WORSE.name(), null));
        map.put("version", "2.0");
        addTcpWorseParams(map);
        MiuiTelephonyStatistic.getInstance().reportTcpLinkWorse(map);
    }

    private void addTcpWorseParams(Map map) {
        String str = SystemProperties.get("ro.boot.hwlevel");
        map.put("hwlevel", str);
        if (!"MP".equals(str)) {
            map.put("lat", String.valueOf(this.mLatitude));
            map.put("long", String.valueOf(this.mLongitude));
            map.put("street_number", this.mStreetNumber);
            map.put("known_name", this.mKnownName);
        }
        map.put("country", this.mCountry);
        map.put(MEModuleSignalT1s.EXTRA_STATE, this.mState);
        map.put("city", this.mCity);
        map.put("street", this.mStreet);
        map.put("foregroundPkg", this.mMiuiCurrentStateMonitor.getForegroundPackageName());
    }

    public Map getLastLinkParams() {
        return this.mLastLinkParams;
    }

    public boolean isSatisfiedPackageLimited() {
        return this.mOptimizationWhiteList.contains(this.mMiuiCurrentStateMonitor.getForegroundPackageName());
    }

    public void reportStatistics() {
        logd("reportStatistics request");
        Intent intent = new Intent();
        intent.setAction("com.android.server.telephony.action.AI_CONGESTION_STATISTIC_REQUEST");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        clearReportParamsLimit();
    }

    private void clearReportParamsLimit() {
        this.mCongestionParamsHasReport = new boolean[]{false, false, false, false, false};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReportStatistics(Intent intent) {
        if (intent == null) {
            return;
        }
        HashMap map = new HashMap();
        String stringExtra = intent.getStringExtra("statistic");
        String stringExtra2 = intent.getStringExtra("statistic_signal");
        String stringExtra3 = intent.getStringExtra("statistic_percent");
        map.put("statistic", stringExtra);
        map.put("statistic_signal", stringExtra2);
        map.put("statistic_percent", stringExtra3);
        if (!Utils.isValidParams(map)) {
            logd("record event=congestion_duration_statistic invalid parameters !!!");
        } else {
            logd("record event=congestion_duration_statistic");
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "congestion_duration_statistic", map);
        }
    }

    public void reportParamsForAI2(int i) {
        logd("reportParamsForAI2 request for reason:" + i);
        this.mCurrentReason = i;
        boolean[] zArr = this.mCongestionParamsHasReport;
        if (zArr[i]) {
            return;
        }
        zArr[i] = true;
        Intent intent = new Intent();
        intent.setAction("com.android.server.telephony.action.AI_CONGESTION_PARAMETERS_REQUEST");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReportParamsChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        HashMap map = new HashMap();
        String stringExtra = intent.getStringExtra("params");
        if (stringExtra == null) {
            return;
        }
        map.put("params", stringExtra);
        map.put("reason", String.valueOf(this.mCurrentReason));
        map.put("count", String.valueOf(this.mMonitorCount));
        map.put("web_type", String.valueOf(this.mCurWebContentType));
        if (!Utils.isValidParams(map)) {
            logd("record event=congestion_params_for_ai2 invalid parameters !!!");
        } else {
            logd("record event=congestion_params_for_ai2");
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "congestion_params_for_ai2", map);
        }
    }

    public List getOptWhiteList(String str) {
        if (str == null) {
            return new ArrayList();
        }
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray != null ? stringArray : new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d("MiuiNetworkMonitor", str);
    }

    private void loge(String str) {
        Rlog.e("MiuiNetworkMonitor", str);
    }
}
