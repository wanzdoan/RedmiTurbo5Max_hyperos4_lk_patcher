package com.android.phone.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkRecoveryStrategyBase;
import com.android.phone.PhoneGlobals;
import com.android.phone.RilHook;
import com.android.phone.badcells.BadCellStats;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.MiuiCurrentStateMonitor;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.phone.utils.VideoPreloadScene;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.telephony.Rlog;
import com.ot.pubsub.b.a;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiNetworkRecoveryManager extends Handler {
    protected static MiuiNetworkRecoveryManager mInstance;
    private String TAG;
    private final BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private CellIdentity mCurrentCell;
    private boolean mCurrentCellIsNr;
    private Scene mCurrentScene;
    private int mCurrentVoiceRat;
    private DataNetworkController.DataNetworkControllerCallback mDNCCallback;
    private DataNetworkController mDataNetworkController;
    private int mDdsPhoneId;
    private int mDePriorityLTECellChangedCount;
    private CellIdentity mDepriorityLTECell;
    private boolean mDeprioritySaFlag;
    private CellIdentity mExceptionCell;
    private boolean mExist;
    private boolean mIsHstMode;
    private boolean mIsInternetNetworkConnected;
    private boolean mIsRadioPowerOff;
    private boolean mIsSubwayMode;
    private boolean mIsValidNetwork;
    private CellIdentity mLastCell;
    private boolean mLastCellIsNr;
    private long mLastDePrioritySaTime;
    private RttWorseLevelEnum mLastLevel;
    private long mLastRecoveryEndMs;
    private long mLastRecoveryStartMs;
    private RttWorseLevelEnum mLastRttLevel;
    private long mLastSnsDataStallTime;
    private long mLastValidTimeMs;
    private int mLastVoiceRat;
    private final MiuiCurrentStateMonitor mMiuiCurrentStateMonitor;
    private final MiuiNetworkMonitor mNetworkMonitor;
    protected Phone mPhone;
    protected final Phone[] mPhones;
    private List mPreloadVideoWhiteList;
    private int mRecoveryAction;
    private MiuiCurrentStateMonitor.AppType mRecoveryAppType;
    private int mRecoveryLevel;
    private RilHook mRilHook;
    private ServiceState mServiceState;
    private boolean mSnsBarFlag;
    private boolean mSnsDataStallFlag;
    ContentObserver mSubwayStateObserver;
    private TelephonyCallBack mTelephonyCallBack;
    private MiuiNetworkMonitor.IMiuiNetworkMonitorListener networkMonitorListener;
    private Map reportData;
    private static final int PHONE_COUNT = TelephonyManager.getDefault().getPhoneCount();
    private static final HashSet mListeners = new HashSet();

    private int inRangeOrUnavailable(int i, int i2, int i3) {
        if (i < i2 || i > i3) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    private boolean isLteNwType(int i) {
        return i == 13 || i == 19;
    }

    private boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    protected boolean doBarDubiousCell(boolean z, CellIdentity cellIdentity) {
        return false;
    }

    protected boolean doSnsDataStall(boolean z, int i) {
        return false;
    }

    public boolean isSubwayStartStopStateUpdate(int i) {
        return (i & 3) != 0;
    }

    protected boolean isSupportSetDubiousCell() {
        return false;
    }

    protected MiuiNetworkRecoveryManager() {
        super(MiuiThreadUtil.getWorkThreadLooper());
        this.TAG = "MiuiNetworkRecoveryManager";
        this.mIsSubwayMode = false;
        this.mIsHstMode = false;
        this.mDdsPhoneId = -1;
        this.mTelephonyCallBack = null;
        this.mDePriorityLTECellChangedCount = 0;
        this.mPreloadVideoWhiteList = new ArrayList();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.network.MiuiNetworkRecoveryManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                boolean booleanExtra;
                if (intent == null || intent.getAction() == null) {
                    return;
                }
                if (intent.getAction().equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                    MiuiNetworkRecoveryManager miuiNetworkRecoveryManager = MiuiNetworkRecoveryManager.this;
                    miuiNetworkRecoveryManager.sendMessage(miuiNetworkRecoveryManager.obtainMessage(16, intent));
                } else {
                    if (!intent.getAction().equals("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED") || (booleanExtra = intent.getBooleanExtra("mIsHstMode", false)) == MiuiNetworkRecoveryManager.this.mIsHstMode) {
                        return;
                    }
                    MiuiNetworkRecoveryManager.this.mIsHstMode = booleanExtra;
                }
            }
        };
        this.mDNCCallback = new AnonymousClass2(new Executor() { // from class: com.android.phone.network.MiuiNetworkRecoveryManager$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        });
        this.networkMonitorListener = new MiuiNetworkMonitor.IMiuiNetworkMonitorListener() { // from class: com.android.phone.network.MiuiNetworkRecoveryManager.3
            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpRTTWorse(Network network, int i, int i2) {
                int level = MiuiNetworkRecoveryManager.this.mLastLevel.getLevel();
                if (i2 == 0) {
                    MiuiNetworkRecoveryManager.this.mLastRttLevel = RttWorseLevelEnum.getInstance(i);
                } else if (i2 == 1) {
                    MiuiNetworkRecoveryManager.this.mLastLevel = RttWorseLevelEnum.getInstance(i);
                }
                if (!MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected || i <= level) {
                    return;
                }
                if ((MiuiNetworkRecoveryManager.this.mCurrentScene == Scene.NONE || MiuiNetworkRecoveryManager.this.mCurrentScene == Scene.RTT_WORSE) && !MiuiNetworkRecoveryManager.this.mDeprioritySaFlag) {
                    MiuiNetworkRecoveryManager.this.log("onTcpRTTWorse: " + network + ", Scene: " + MiuiNetworkRecoveryManager.this.mCurrentScene + ", level:" + i);
                    MiuiNetworkRecoveryManager.this.mLastLevel = RttWorseLevelEnum.getInstance(i);
                    long jCurrentTimeMillis = System.currentTimeMillis() - MiuiNetworkRecoveryManager.this.mLastSnsDataStallTime;
                    long jCurrentTimeMillis2 = System.currentTimeMillis() - MiuiNetworkRecoveryManager.this.mLastDePrioritySaTime;
                    MiuiNetworkRecoveryManager.this.log("sns_time_var: " + jCurrentTimeMillis + ", dsa_time_var: " + jCurrentTimeMillis2 + ", THRESHOLD_SNS_DATA_STALL_NEXT:900000");
                    if (jCurrentTimeMillis > 900000 || jCurrentTimeMillis2 > 900000) {
                        MiuiNetworkRecoveryManager.this.startDoRecoveryWithLevel(Scene.RTT_WORSE, i);
                    }
                }
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpRTTBetter(Network network, int i) {
                if (i == 0) {
                    MiuiNetworkRecoveryManager.this.mLastRttLevel = RttWorseLevelEnum.L0;
                }
                MiuiNetworkRecoveryManager.this.log("onTcpRTTBetter: " + network + ", Scene: " + MiuiNetworkRecoveryManager.this.mCurrentScene);
                MiuiNetworkRecoveryManager.this.mLastLevel = RttWorseLevelEnum.L0;
                MiuiNetworkRecoveryManager.this.mLastRecoveryEndMs = SystemClock.elapsedRealtime();
                if (MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected && MiuiNetworkRecoveryManager.this.mCurrentScene == Scene.RTT_WORSE) {
                    MiuiNetworkRecoveryManager miuiNetworkRecoveryManager = MiuiNetworkRecoveryManager.this;
                    miuiNetworkRecoveryManager.sendMessage(miuiNetworkRecoveryManager.obtainMessage(11));
                }
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpLinkWorse(Network network) {
                if (MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected && MiuiNetworkRecoveryManager.this.mCurrentScene == Scene.NONE) {
                    MiuiNetworkRecoveryManager.this.log("onTcpLinkWorse: " + network + ", Scene: " + MiuiNetworkRecoveryManager.this.mCurrentScene);
                    MiuiNetworkRecoveryManager.this.startDoRecovery(Scene.LINK_WORSE);
                }
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpLinkBetter(Network network) {
                if (MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected && MiuiNetworkRecoveryManager.this.mCurrentScene == Scene.LINK_WORSE) {
                    MiuiNetworkRecoveryManager.this.log("onTcpLinkBetter: " + network + ", Scene: " + MiuiNetworkRecoveryManager.this.mCurrentScene);
                    MiuiNetworkRecoveryManager miuiNetworkRecoveryManager = MiuiNetworkRecoveryManager.this;
                    miuiNetworkRecoveryManager.sendMessage(miuiNetworkRecoveryManager.obtainMessage(11));
                }
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void onTcpLinkBlock(Network network) {
                MiuiNetworkRecoveryManager.this.log("onTcpLinkBlock: " + network + ", Scene: " + MiuiNetworkRecoveryManager.this.mCurrentScene);
            }

            @Override // com.android.phone.network.MiuiNetworkMonitor.IMiuiNetworkMonitorListener
            public void on3rdAppPreload() {
                MiuiNetworkRecoveryManager.this.notify3rdAppPreload();
            }
        };
        this.mSubwayStateObserver = new ContentObserver(this) { // from class: com.android.phone.network.MiuiNetworkRecoveryManager.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                MiuiNetworkRecoveryManager.this.updateSubwayStateChange();
            }
        };
        this.mNetworkMonitor = MiuiNetworkMonitor.getInstance();
        this.mPhones = PhoneFactory.getPhones();
        this.mDdsPhoneId = getDefaultDataSlotId();
        this.mMiuiCurrentStateMonitor = MiuiCurrentStateMonitor.getInstance();
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mRilHook = RilHook.init(phoneGlobals);
        initPreloadVideoWhiteList();
        registerAllEvents();
        initValue();
        logi("MiuiNetworkRecoveryManager created.");
    }

    public void initValue() {
        this.mRecoveryAction = 0;
        this.mCurrentScene = Scene.NONE;
        this.mRecoveryAppType = MiuiCurrentStateMonitor.AppType.NONE;
        RttWorseLevelEnum rttWorseLevelEnum = RttWorseLevelEnum.L0;
        this.mLastLevel = rttWorseLevelEnum;
        this.mLastRttLevel = rttWorseLevelEnum;
        this.mRecoveryLevel = rttWorseLevelEnum.getLevel();
        this.reportData = new ConcurrentHashMap();
    }

    public static void init() {
        synchronized (MiuiNetworkRecoveryManager.class) {
            try {
                if (mInstance == null) {
                    mInstance = MiuiNetworkRecoveryManagerEx.make();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        MiuiNetworkMonitor.getInstance().registerMiuiNetworkMonitorListener(this.networkMonitorListener);
        TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
        this.mTelephonyCallBack = telephonyCallBack;
        TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        registerDdsEvents();
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("network_mode_state"), false, this.mSubwayStateObserver);
    }

    private void registerDdsEvents() {
        Phone phone;
        if (this.mPhones == null || !MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId) || (phone = this.mPhones[this.mDdsPhoneId]) == null) {
            return;
        }
        this.mPhone = phone;
        DataNetworkController dataNetworkController = phone.getDataNetworkController();
        this.mDataNetworkController = dataNetworkController;
        dataNetworkController.registerDataNetworkControllerCallback(this.mDNCCallback);
        this.mPhone.mCi.registerForRadioStateChanged(this, 2, (Object) null);
        this.mCurrentCell = this.mPhone.getCurrentCellIdentity();
        this.mLastCell = this.mPhone.getCurrentCellIdentity();
    }

    private void unRegisterDdsEvents(int i) {
        Phone phone;
        if (this.mPhones == null || !MiuiPhoneUtils.isValidatePhoneId(i) || (phone = this.mPhones[i]) == null) {
            return;
        }
        phone.getDataNetworkController().unregisterDataNetworkControllerCallback(this.mDNCCallback);
        this.mPhones[i].mCi.unregisterForRadioStateChanged(this);
    }

    private int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: renamed from: com.android.phone.network.MiuiNetworkRecoveryManager$2, reason: invalid class name */
    class AnonymousClass2 extends DataNetworkController.DataNetworkControllerCallback {
        AnonymousClass2(Executor executor) {
            super(executor);
        }

        public void onInternetDataNetworkValidationStatusChanged(int i) {
            MiuiNetworkRecoveryManager.this.onInternetValidationStatusChanged(i);
            MiuiNetworkRecoveryManager.this.log("onInternetValidationStatusChanged: " + i);
        }

        public void onConnectedInternetDataNetworksChanged(Set set) {
            MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected = set.stream().anyMatch(new Predicate() { // from class: com.android.phone.network.MiuiNetworkRecoveryManager$2$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return MiuiNetworkRecoveryManager.AnonymousClass2.$r8$lambda$fgSptXMeVuPiywQwVr5wdbWEAYk((DataNetwork) obj);
                }
            });
            MiuiNetworkRecoveryManager.this.log("onConnectedInternetDataNetworksChanged: " + MiuiNetworkRecoveryManager.this.mIsInternetNetworkConnected);
        }

        public static /* synthetic */ boolean $r8$lambda$fgSptXMeVuPiywQwVr5wdbWEAYk(DataNetwork dataNetwork) {
            NetworkCapabilities networkCapabilities = dataNetwork.getNetworkCapabilities();
            return networkCapabilities.hasCapability(13) && networkCapabilities.hasCapability(14) && networkCapabilities.hasCapability(15);
        }
    }

    private int getCurrentSignalRsrp(int i) {
        SignalStrength signalStrength = this.mPhone.getSignalStrength();
        if (signalStrength == null) {
            return Integer.MAX_VALUE;
        }
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if ((cellSignalStrength instanceof CellSignalStrengthLte) && isLteNwType(i)) {
                return inRangeOrUnavailable(((CellSignalStrengthLte) cellSignalStrength).getRsrp(), -140, -43);
            }
            if ((cellSignalStrength instanceof CellSignalStrengthNr) && isNrNwType(i)) {
                return inRangeOrUnavailable(((CellSignalStrengthNr) cellSignalStrength).getSsRsrp(), -140, -44);
            }
        }
        return Integer.MAX_VALUE;
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0091  */
    private boolean isSatisfyDataStallSceneForSignal() {
        boolean z;
        boolean z2 = false;
        try {
            if (MiuiPhoneUtils.isValidatePhoneId(this.mDdsPhoneId)) {
                int dataNetworkType = this.mPhone.getServiceState().getDataNetworkType();
                log("isSatisfyDataStallSceneForSignal,mDdsPhoneId= " + this.mDdsPhoneId + ",dataType= " + dataNetworkType);
                float[] neighborRsrpRsrqInfo = getNeighborRsrpRsrqInfo(this.mDdsPhoneId, dataNetworkType);
                if (neighborRsrpRsrqInfo != null) {
                    if (isLteNwType(dataNetworkType)) {
                        z = inRangeOrUnavailable((int) neighborRsrpRsrqInfo[0], -140, -43) != Integer.MAX_VALUE && inRangeOrUnavailable((int) neighborRsrpRsrqInfo[1], -34, 3) != Integer.MAX_VALUE && neighborRsrpRsrqInfo[0] > -110.0f && neighborRsrpRsrqInfo[1] > -20.0f;
                        try {
                            int currentSignalRsrp = getCurrentSignalRsrp(dataNetworkType);
                            log("isSatisfyDataStallSceneForSignal current curLteRsrp:" + currentSignalRsrp);
                            if (z || currentSignalRsrp >= -90) {
                                z = true;
                            } else {
                                z = false;
                            }
                        } catch (Exception e) {
                            e = e;
                            z2 = z;
                            e.printStackTrace();
                            return z2;
                        }
                    } else {
                        if (isNrNwType(dataNetworkType)) {
                            boolean z3 = inRangeOrUnavailable((int) neighborRsrpRsrqInfo[0], -140, -44) != Integer.MAX_VALUE && inRangeOrUnavailable((int) neighborRsrpRsrqInfo[1], -43, 20) != Integer.MAX_VALUE && neighborRsrpRsrqInfo[0] > -108.0f && neighborRsrpRsrqInfo[1] > -15.0f;
                            try {
                                int currentSignalRsrp2 = getCurrentSignalRsrp(dataNetworkType);
                                log("isSatisfyDataStallSceneForSignal current curNrRsrp:" + currentSignalRsrp2);
                                if (z3 || currentSignalRsrp2 >= -85) {
                                    z = true;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                z2 = z3;
                                e.printStackTrace();
                                return z2;
                            }
                        }
                        z = false;
                    }
                    log("isSatisfyDataStallSceneForSignal curNeighborSignal[0]:" + neighborRsrpRsrqInfo[0] + "curNeighborSignal[1]:" + neighborRsrpRsrqInfo[1] + " rst:" + z);
                    return z;
                }
            }
            return false;
        } catch (Exception e3) {
            e = e3;
        }
    }

    private float[] getNeighborRsrpRsrqInfo(int i, int i2) {
        ByteBuffer nr5gBestNbrMeasInfo;
        try {
            if (isLteNwType(i2)) {
                nr5gBestNbrMeasInfo = this.mRilHook.getLteBestNbrMeasInfo(i);
            } else {
                nr5gBestNbrMeasInfo = isNrNwType(i2) ? this.mRilHook.getNr5gBestNbrMeasInfo(i) : null;
            }
            if (nr5gBestNbrMeasInfo == null) {
                log("getNeighborRsrpRsrqInfo return null");
                return null;
            }
            nr5gBestNbrMeasInfo.order(ByteOrder.nativeOrder());
            byte b = nr5gBestNbrMeasInfo.get();
            log("getNeighborRsrpRsrqInfo version:" + ((int) b) + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            float f = nr5gBestNbrMeasInfo.getFloat();
            log("getNeighborRsrpRsrqInfo curNeighborRsrp:" + f + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            float f2 = nr5gBestNbrMeasInfo.getFloat();
            log("getNeighborRsrpRsrqInfo version:" + ((int) b) + "curNeighborRsrp:" + f + " curNeighborRsrq:" + f2 + " remaining:" + nr5gBestNbrMeasInfo.remaining());
            return new float[]{f, f2};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void notify3rdAppPreload() {
        String topAppName = MiuiPhoneUtils.getTopAppName();
        if (!isPreloadableApp(topAppName) || this.mIsSubwayMode || this.mIsHstMode) {
            return;
        }
        log("notify3rdAppPreload:" + topAppName);
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().videoAppsPolicyNotify(1, 8, -1, VideoPreloadScene.AI_CONGESTION.getValue());
        }
    }

    private boolean isPreloadableApp(String str) {
        if (this.mPreloadVideoWhiteList.size() == 0) {
            return false;
        }
        log("isPreloadVideoWhiteList topAppName:" + str);
        Iterator it = this.mPreloadVideoWhiteList.iterator();
        while (it.hasNext()) {
            if (str.equals((String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private void initPreloadVideoWhiteList() {
        if (this.mPreloadVideoWhiteList.size() > 0) {
            this.mPreloadVideoWhiteList.clear();
        }
        FiveGManagerBase.getInstance();
        List preloadVideoAppsWhiteListByCloud = FiveGManagerBase.getPreloadVideoAppsWhiteListByCloud(this.mContext);
        this.mPreloadVideoWhiteList = preloadVideoAppsWhiteListByCloud;
        if (preloadVideoAppsWhiteListByCloud.size() != 0) {
            log("mPreloadVideoWhiteList=" + this.mPreloadVideoWhiteList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInternetValidationStatusChanged(int i) {
        boolean z = i == 1;
        if (z) {
            this.mLastValidTimeMs = SystemClock.elapsedRealtime();
            this.mIsValidNetwork = true;
        }
        log("onInternetValidationStatusChanged: " + i + ", Scene: " + this.mCurrentScene);
        if (z && this.mCurrentScene == Scene.DATA_STALL) {
            sendMessage(obtainMessage(12));
            return;
        }
        if (!z && this.mCurrentScene == Scene.NONE && this.mIsValidNetwork && this.mIsInternetNetworkConnected) {
            this.mIsValidNetwork = false;
            startDoRecoveryWithLevel(Scene.DATA_STALL, RttWorseLevelEnum.L2.getLevel());
            log("trigger data stall recovery");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDoRecoveryWithLevel(Scene scene, int i) {
        if (this.mIsInternetNetworkConnected && this.mNetworkMonitor.isSatisfiedPackageLimited()) {
            if (this.mIsSubwayMode || this.mIsHstMode) {
                log("skip recovery as subway mode or hst mode");
                return;
            }
            Phone[] phoneArr = this.mPhones;
            if (phoneArr == null || phoneArr[this.mDdsPhoneId] == null) {
                return;
            }
            log("enter Scene: " + scene + ", level: " + i);
            if (this.mCurrentScene == Scene.NONE) {
                this.mLastRecoveryStartMs = SystemClock.elapsedRealtime();
                this.mCurrentScene = scene;
            }
            this.mExceptionCell = this.mCurrentCell;
            if (i == RttWorseLevelEnum.L1.getLevel()) {
                if (!isSupportSetDubiousCell() || this.mSnsDataStallFlag || this.mDeprioritySaFlag) {
                    return;
                }
                this.mRecoveryLevel = i;
                reportRecoveryLater(i);
                sendMessage(obtainMessage(1));
                this.mRecoveryAction = 0;
                return;
            }
            if (i == RttWorseLevelEnum.L2.getLevel() && this.mRecoveryAction == 0) {
                this.mRecoveryLevel = i;
                if (isSupportSetDubiousCell()) {
                    reportRecoveryLater(this.mRecoveryLevel);
                    sendMessage(obtainMessage(1));
                    this.mRecoveryAction = 1;
                    return;
                } else {
                    sendMessage(obtainMessage(1));
                    this.mRecoveryAction = 0;
                    return;
                }
            }
            if (i == RttWorseLevelEnum.L3.getLevel() && this.mRecoveryAction == 0) {
                if (isSupportSetDubiousCell() && this.mDeprioritySaFlag) {
                    return;
                }
                removeMessages(1);
                this.mRecoveryLevel = i;
                reportRecoveryLater(i);
                sendMessage(obtainMessage(1));
                this.mRecoveryAction = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDoRecovery(Scene scene) {
        if (this.mIsInternetNetworkConnected && this.mNetworkMonitor.isSatisfiedPackageLimited()) {
            log("enter Scene: " + scene);
            Phone[] phoneArr = this.mPhones;
            if (phoneArr == null || phoneArr[this.mDdsPhoneId] == null) {
                return;
            }
            this.mLastRecoveryStartMs = SystemClock.elapsedRealtime();
            this.mExceptionCell = this.mCurrentCell;
            this.mCurrentScene = scene;
            reportRecoveryLater(RttWorseLevelEnum.L0.getLevel());
            sendMessage(obtainMessage(1));
        }
    }

    private void clearReportRecovery() {
        removeMessages(8);
        this.reportData.clear();
    }

    private void reportRecoveryLater(int i) {
        removeMessages(19);
        sendMessage(obtainMessage(19, Integer.valueOf(i)));
    }

    private void doReportRecoveryLater(int i) {
        clearReportRecovery();
        this.reportData.put(a.d, String.valueOf(i));
        this.reportData.put("scene", this.mCurrentScene.name());
        MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.getInstance();
        if (miuiTelephonyStatistic != null) {
            this.reportData = miuiTelephonyStatistic.reportSNANetworkRecoveryBefore(this.reportData);
        }
        sendMessageDelayed(obtainMessage(8), 200000L);
    }

    private boolean isRecoveryNeeded() {
        log("enter: isRecoveryNeeded()");
        if (!this.mIsInternetNetworkConnected) {
            log("skip data stall recovery as data not connected");
            return false;
        }
        if (this.mIsSubwayMode || this.mIsHstMode) {
            log("skip recovery as subway mode or hst mode");
        }
        if (this.mPhone.getState() != PhoneConstants.State.IDLE && this.mRecoveryAction > 0) {
            log("skip data stall recovery as there is an active call");
            return false;
        }
        if (!checkSatisfiedMinSignalQuality(true)) {
            log("skip recovery as in poor signal condition");
            return false;
        }
        DataNetworkController dataNetworkController = this.mDataNetworkController;
        if (dataNetworkController == null || dataNetworkController.isInternetDataAllowed(false)) {
            return true;
        }
        log("skip data stall recovery as data not allowed.");
        return false;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        MiuiCurrentStateMonitor.AppType appType;
        CellIdentity cellIdentity;
        CellIdentity cellIdentity2;
        log("handleMessage = " + message.what);
        switch (message.what) {
            case 1:
                removeMessages(1);
                doRecovery();
                break;
            case 2:
                if (this.mPhone.mCi.getRadioState() == 0) {
                    this.mIsRadioPowerOff = true;
                } else {
                    this.mIsRadioPowerOff = false;
                    this.mLastSnsDataStallTime = -1L;
                    this.mLastDePrioritySaTime = -1L;
                    sendMessage(obtainMessage(7));
                }
                break;
            case 3:
                removeMessages(3);
                if (MiuiPhoneUtils.isDuringCall()) {
                    log("if you are on call or never do data stall ,ignore cancel data stall.");
                    sendMessageDelayed(obtainMessage(3), 1200000L);
                    break;
                } else if (this.mSnsDataStallFlag && (appType = this.mRecoveryAppType) != MiuiCurrentStateMonitor.AppType.NONE) {
                    doSnsDataStall(false, appType.getValue());
                    this.mSnsDataStallFlag = false;
                    break;
                }
                break;
            case 4:
                if (MiuiPhoneUtils.isDuringCall() || ((cellIdentity = this.mExceptionCell) != null && !cellIdentity.isSameCell(this.mCurrentCell))) {
                    log("if you are on call or cell changed, ignore unbar.");
                    sendMessage(obtainMessage(12));
                } else if (this.mDeprioritySaFlag) {
                    log("if you had exe dsa, ignore unbar.");
                    sendMessage(obtainMessage(12));
                } else if (System.currentTimeMillis() - this.mLastSnsDataStallTime < 900000) {
                    log("if you had exe unbar action in 15 minutes, ignore unbar.");
                    sendMessage(obtainMessage(12));
                } else {
                    int rxRate = (int) this.mMiuiCurrentStateMonitor.getRxRate();
                    log("rxRate = " + rxRate + "/Bps, threshold = 128000/Bps");
                    boolean z = rxRate < 128000;
                    MiuiCurrentStateMonitor.AppType foregroundAppType = this.mMiuiCurrentStateMonitor.getForegroundAppType();
                    this.mRecoveryAppType = foregroundAppType;
                    if (foregroundAppType != MiuiCurrentStateMonitor.AppType.NONE && z && isSatisfyDataStallSceneForSignal()) {
                        this.mSnsDataStallFlag = doSnsDataStall(true, this.mRecoveryAppType.getValue());
                    } else {
                        this.mSnsDataStallFlag = false;
                    }
                    if (this.mSnsDataStallFlag) {
                        this.mLastSnsDataStallTime = System.currentTimeMillis();
                        log("Do recovery with data stall due to neighbor cell is available.");
                        sendMessageDelayed(obtainMessage(8), 100000L);
                        sendMessageDelayed(obtainMessage(1), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                        sendMessageDelayed(obtainMessage(3), 1200000L);
                    } else if (!isSupportSetDubiousCell()) {
                        sendMessage(obtainMessage(1));
                    } else {
                        sendMessage(obtainMessage(12));
                    }
                    setRecoveryAction(2);
                }
                break;
            case 5:
                removeMessages(5);
                removeMessages(17);
                if (MiuiPhoneUtils.isDuringCall()) {
                    log("if you are on call or Thermal scene, ignore the upgrade to 5G until 15 min later.");
                    sendMessageDelayed(obtainMessage(5), 900000L);
                } else if (this.mDeprioritySaFlag) {
                    notifyEnableSA(true);
                    this.mDepriorityLTECell = null;
                }
                break;
            case 6:
                removeMessages(6);
                if (MiuiPhoneUtils.isDuringCall()) {
                    log("if you are on call or has ever done end recovery");
                    sendMessage(obtainMessage(12));
                } else if (Build.IS_INTERNATIONAL_BUILD) {
                    log("if you are on international build, ignore dsa recovery");
                    sendMessage(obtainMessage(12));
                } else if (System.currentTimeMillis() - this.mLastDePrioritySaTime < 900000) {
                    log("if you had exe de sa action in 15 minutes, ignore de sa.");
                    sendMessage(obtainMessage(12));
                } else if (getCurrentSaStatus() != 1 || !this.mCurrentCellIsNr) {
                    log("you are not on SA");
                    sendMessage(obtainMessage(12));
                } else {
                    notifyEnableSA(false);
                    if (this.mDeprioritySaFlag) {
                        this.mLastRecoveryEndMs = SystemClock.elapsedRealtime();
                        sendMessageDelayed(obtainMessage(5), 900000L);
                        removeMessages(8);
                    }
                    sendMessage(obtainMessage(1));
                    setRecoveryAction(3);
                }
                break;
            case 7:
                processEndRecovery();
                break;
            case 8:
                reportNetworkRecovery();
                break;
            case 9:
                removeMessages(9);
                if (MiuiPhoneUtils.isDuringCall()) {
                    log("if you are on call or never bar ,ignore unbar until 20 min later.");
                    sendMessageDelayed(obtainMessage(9), 1200000L);
                } else if (this.mSnsBarFlag) {
                    doBarDubiousCell(false, null);
                    this.mSnsBarFlag = false;
                }
                break;
            case 10:
                if (MiuiPhoneUtils.isDuringCall() || ((cellIdentity2 = this.mExceptionCell) != null && !cellIdentity2.isSameCell(this.mCurrentCell))) {
                    log("if you are on call or cell changed, ignore unbar.");
                    sendMessage(obtainMessage(12));
                } else {
                    this.mSnsBarFlag = doBarDubiousCell(true, this.mCurrentCell);
                    sendMessageDelayed(obtainMessage(1), RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    sendMessageDelayed(obtainMessage(9), 1200000L);
                    setRecoveryAction(2);
                }
                break;
            case 11:
                resetStatus();
                break;
            case 12:
                resetStatus();
                break;
            case 13:
                int defaultDataSlotId = getDefaultDataSlotId();
                unRegisterDdsEvents(this.mDdsPhoneId);
                this.mDdsPhoneId = defaultDataSlotId;
                registerDdsEvents();
                break;
            case 14:
                processCellLocationChangeEvent(message.arg1, (CellLocation) message.obj);
                break;
            case 15:
                processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                break;
            case 16:
                processEndRecovery();
                sendMessage(obtainMessage(13));
                break;
            case 17:
                log("check depri sa effectiveness");
                removeMessages(17);
                if (isLteNetworkGreaterThanNr()) {
                    sendMessageDelayed(obtainMessage(17), 30000L);
                } else {
                    sendMessage(obtainMessage(7));
                }
                break;
            case 18:
                log("check depri sa signal");
                removeMessages(18);
                if (!checkSatisfiedMinSignalQuality(false)) {
                    log("dePrioritySA signal check, need revert");
                    sendMessage(obtainMessage(7));
                    removeMessages(8);
                    removeMessages(17);
                }
                break;
            case 19:
                doReportRecoveryLater(message.arg1);
                break;
            default:
                log("Unexpected message = " + message);
                break;
        }
    }

    private void processEndRecovery() {
        if (this.mSnsDataStallFlag) {
            removeMessages(3);
            sendMessage(obtainMessage(3));
        }
        if (this.mDeprioritySaFlag) {
            removeMessages(5);
            sendMessage(obtainMessage(5));
        }
        resetStatus();
    }

    private void doRecovery() {
        if (!isRecoveryNeeded()) {
            clearReportRecovery();
            return;
        }
        int i = this.mRecoveryAction;
        if (i != 0) {
            if (i == 1) {
                sendMessage(obtainMessage(4));
                return;
            }
            if (i == 2) {
                if (this.mSnsDataStallFlag) {
                    sendMessage(obtainMessage(3));
                }
                sendMessage(obtainMessage(6));
                return;
            } else if (i == 3) {
                sendMessage(obtainMessage(12));
                return;
            } else {
                throw new RuntimeException("doRecovery: Invalid recoveryAction = " + i);
            }
        }
        CellIdentity currentCellIdentity = this.mPhone.getCurrentCellIdentity();
        if (BadCellStats.getInstance() != null) {
            this.mExist = BadCellStats.getInstance().hasExistBadCell(currentCellIdentity);
        }
        log("doRecovery(): is bad cell: " + this.mExist);
        if (this.mExist) {
            sendMessage(obtainMessage(1));
        } else {
            sendMessage(obtainMessage(12));
            clearReportRecovery();
        }
        if (isSupportSetDubiousCell()) {
            setRecoveryAction(1);
        } else {
            setRecoveryAction(2);
        }
    }

    private void setRecoveryAction(int i) {
        this.mRecoveryAction = i;
        log("setRecoveryAction: " + this.mRecoveryAction);
    }

    private void resetStatus() {
        removeMessages(1);
        removeMessages(10);
        removeMessages(4);
        removeMessages(6);
        removeMessages(7);
        this.mIsValidNetwork = true;
        this.mRecoveryAction = 0;
        this.mCurrentScene = Scene.NONE;
        this.mRecoveryAppType = MiuiCurrentStateMonitor.AppType.NONE;
        this.mRecoveryLevel = RttWorseLevelEnum.L0.getLevel();
        this.mExceptionCell = null;
        this.mExist = false;
        log("reset status");
    }

    private static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < PHONE_COUNT;
    }

    private void notifyEnableSA(boolean z) {
        int currentSaStatus = getCurrentSaStatus();
        log("priority sa , currentSaStatus = " + currentSaStatus + ", enable = " + z);
        if ((currentSaStatus == 1 && z) || ((currentSaStatus == 0 && !z) || currentSaStatus == -1)) {
            log("maybe do nothing about sa , currentSaStatus = " + currentSaStatus + ", enable = " + z);
        }
        this.mDeprioritySaFlag = !z;
        if (!z) {
            this.mLastDePrioritySaTime = System.currentTimeMillis();
        }
        Iterator it = mListeners.iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
            this.mPhone.getPhoneId();
            throw null;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("sa_status", z);
        bundle.putInt(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_PHONE_ID, this.mPhone.getPhoneId());
        EventHubManager.getInstance().postEvent(new TelephonyEvent.NetworkEvent("enable_sa_state_changed", bundle));
    }

    private int getCurrentSaStatus() {
        int phoneId = this.mPhone.getPhoneId();
        if (!isValidatePhoneId(phoneId)) {
            return -1;
        }
        boolean[] saPowerEnable = ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaPowerEnable();
        return (saPowerEnable.length <= 0 || !saPowerEnable[phoneId]) ? 0 : 1;
    }

    private boolean isLteNetworkGreaterThanNr() {
        if (this.mLastRttLevel.getLevel() >= RttWorseLevelEnum.L2.getLevel()) {
            log("rtt level is greater than or equal to L2");
            return false;
        }
        if (this.mLastLevel.getLevel() < RttWorseLevelEnum.L3.getLevel()) {
            return true;
        }
        log("ai congestion level is greater than or equal to L3");
        return false;
    }

    private void reportNetworkRecovery() {
        if (this.reportData.isEmpty()) {
            return;
        }
        removeMessages(8);
        this.reportData.put("hwlevel", SystemProperties.get("ro.boot.hwlevel"));
        this.reportData.put("resourceEvent", "sna_network_recovery");
        this.reportData.put("version", "2.0");
        this.reportData.put("time", String.valueOf(this.mLastRecoveryEndMs - this.mLastRecoveryStartMs));
        this.reportData.put("exist", String.valueOf(this.mExist));
        this.reportData.put("recoveryFlag", String.valueOf(this.mSnsDataStallFlag) + ',' + String.valueOf(this.mDeprioritySaFlag));
        Map lastLinkParams = this.mNetworkMonitor.getLastLinkParams();
        this.reportData.put("currentLevel", String.valueOf(this.mLastLevel.getLevel()));
        this.reportData.put("rttWorseParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.RttStatus.RTT_STATUS_WORSE.name(), null));
        this.reportData.put("rttBetterParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.RttStatus.RTT_STATUS_BETTER.name(), null));
        this.reportData.put("rttDataParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.LinkType.RTT.getParamsName(), null));
        this.reportData.put("linkWorseParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.LinkStatus.LINK_STATUS_WORSE.name(), null));
        this.reportData.put("linkBetterParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.LinkStatus.LINK_STATUS_BETTER.name(), null));
        this.reportData.put("linkDataParams", (String) lastLinkParams.getOrDefault(MiuiNetworkMonitor.LinkType.LINK.getParamsName(), null));
        MiuiTelephonyStatistic.getInstance().reportSNANetworkRecovery(this.reportData);
        this.reportData.clear();
        log("reportNetworkRecovery done");
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            MiuiNetworkRecoveryManager.this.removeMessages(15);
            MiuiNetworkRecoveryManager miuiNetworkRecoveryManager = MiuiNetworkRecoveryManager.this;
            miuiNetworkRecoveryManager.sendMessage(miuiNetworkRecoveryManager.obtainMessage(15, i, 0, serviceState));
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            MiuiNetworkRecoveryManager miuiNetworkRecoveryManager = MiuiNetworkRecoveryManager.this;
            miuiNetworkRecoveryManager.sendMessage(miuiNetworkRecoveryManager.obtainMessage(14, i, 0, cellLocation));
        }
    }

    private synchronized void processCellLocationChangeEvent(int i, CellLocation cellLocation) {
        Phone[] phoneArr;
        CellIdentity cellIdentity;
        if (cellLocation != null) {
            try {
                int i2 = this.mDdsPhoneId;
                if (i == i2 && (phoneArr = this.mPhones) != null && phoneArr[i2] != null) {
                    CellIdentity currentCellIdentity = this.mPhone.getCurrentCellIdentity();
                    if (this.mLastCell != null && (cellIdentity = this.mCurrentCell) != null && !cellIdentity.isSameCell(currentCellIdentity)) {
                        CellIdentity cellIdentity2 = this.mCurrentCell;
                        this.mLastCell = cellIdentity2;
                        this.mCurrentCell = currentCellIdentity;
                        this.mLastCellIsNr = isNrCell(cellIdentity2);
                        this.mCurrentCellIsNr = isNrCell(this.mCurrentCell);
                        this.mLastVoiceRat = this.mCurrentVoiceRat;
                        ServiceState serviceState = this.mPhone.getServiceState();
                        if (serviceState != null) {
                            this.mCurrentVoiceRat = serviceState.getVoiceNetworkType();
                        }
                        log("previous rat: " + this.mLastVoiceRat + ", current rat: " + this.mCurrentVoiceRat + ", previous isNrCell: " + this.mLastCellIsNr + ", current isNrCell: " + this.mCurrentCellIsNr + ", previous cell: " + getCellInfo(this.mLastCell) + ", current cell: " + getCellInfo(this.mCurrentCell) + ", mDeprioritySaFlag: " + this.mDeprioritySaFlag);
                        if (this.mDeprioritySaFlag) {
                            CellIdentity cellIdentity3 = this.mDepriorityLTECell;
                            if (cellIdentity3 == null && this.mLastCellIsNr && !this.mCurrentCellIsNr) {
                                log("dePrioritySA success cell: " + this.mCurrentCell.toString() + ", current rat: " + this.mCurrentVoiceRat);
                                this.mDepriorityLTECell = this.mCurrentCell;
                                sendMessageDelayed(obtainMessage(18), 3000L);
                                sendMessageDelayed(obtainMessage(8), 120000L);
                                sendMessageDelayed(obtainMessage(17), 40000L);
                                this.mDePriorityLTECellChangedCount = 0;
                            } else if (!this.mLastCellIsNr && this.mCurrentCellIsNr) {
                                removeMessages(17);
                                sendMessage(obtainMessage(7));
                            } else if (cellIdentity3 != null) {
                                if (!cellIdentity3.isSameCell(this.mCurrentCell)) {
                                    int i3 = this.mDePriorityLTECellChangedCount + 1;
                                    this.mDePriorityLTECellChangedCount = i3;
                                    if (i3 >= 3) {
                                        this.mDepriorityLTECell = null;
                                        log("DSA LTE cell changed more than 3times, then cancel recovery");
                                        sendMessage(obtainMessage(7));
                                        this.mDePriorityLTECellChangedCount = 0;
                                    }
                                } else {
                                    this.mDePriorityLTECellChangedCount = 0;
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean checkSatisfiedMinSignalQuality(boolean z, int i, int i2, int i3, boolean z2) {
        boolean z3 = (!z && i > -110 && i2 > -20 && (z2 || i3 > 0)) || (z && i > -108 && i2 > -15 && (z2 || i3 > 0));
        log("signal check, " + z3 + ", rsrp: " + i + ", rsrq: " + i2 + ", snr: " + i3 + ", noRelateSnr: " + z2);
        return z3;
    }

    private boolean checkSatisfiedMinSignalQuality(boolean z) {
        CellSignalStrength currentSignalStrengthFromAP;
        Phone phone = this.mPhone;
        if (phone == null || (currentSignalStrengthFromAP = getCurrentSignalStrengthFromAP(isNrNwType(phone.getServiceState().getDataNetworkType()))) == null) {
            return false;
        }
        if (currentSignalStrengthFromAP instanceof CellSignalStrengthLte) {
            CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) currentSignalStrengthFromAP;
            return checkSatisfiedMinSignalQuality(false, cellSignalStrengthLte.getRsrp(), cellSignalStrengthLte.getRsrq(), cellSignalStrengthLte.getRssnr(), z);
        }
        if (!(currentSignalStrengthFromAP instanceof CellSignalStrengthNr)) {
            return false;
        }
        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) currentSignalStrengthFromAP;
        return checkSatisfiedMinSignalQuality(true, cellSignalStrengthNr.getSsRsrp(), cellSignalStrengthNr.getSsRsrq(), cellSignalStrengthNr.getSsSinr(), z);
    }

    private CellSignalStrength getCurrentSignalStrengthFromAP(boolean z) {
        SignalStrength signalStrength;
        List<CellSignalStrength> cellSignalStrengths;
        Phone phone = this.mPhone;
        if (phone == null || (signalStrength = phone.getSignalStrength()) == null || (cellSignalStrengths = signalStrength.getCellSignalStrengths()) == null) {
            return null;
        }
        for (CellSignalStrength cellSignalStrength : cellSignalStrengths) {
            if ((cellSignalStrength instanceof CellSignalStrengthLte) && !z) {
                return cellSignalStrength;
            }
            if ((cellSignalStrength instanceof CellSignalStrengthNr) && z) {
                return cellSignalStrength;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubwayStateChange() {
        boolean zIsSubwayStartStopStateUpdate = isSubwayStartStopStateUpdate(Settings.Global.getInt(this.mContext.getContentResolver(), "network_mode_state", 0) & 3);
        if (this.mIsSubwayMode == zIsSubwayStartStopStateUpdate) {
            return;
        }
        log("updateSubwayStateChange mIsSubwayMode:" + this.mIsSubwayMode + ", isSubwayState:" + zIsSubwayStartStopStateUpdate);
        this.mIsSubwayMode = zIsSubwayStartStopStateUpdate;
    }

    private boolean isNrCell(CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            return false;
        }
        return cellIdentity instanceof CellIdentityNr;
    }

    private String getCellInfo(CellIdentity cellIdentity) {
        if (cellIdentity == null) {
            return "";
        }
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            return cellIdentityNr.getPci() + "_" + cellIdentityNr.getTac() + "_" + cellIdentityNr.getNrarfcn();
        }
        if (!(cellIdentity instanceof CellIdentityLte)) {
            return "";
        }
        CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
        return cellIdentityLte.getPci() + "_" + cellIdentityLte.getTac() + "_" + cellIdentityLte.getEarfcn();
    }

    private synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        if (i == this.mDdsPhoneId) {
            this.mServiceState = serviceState;
        }
    }

    protected void logi(String str) {
        Rlog.i(this.TAG, str);
    }

    protected void log(String str) {
        Rlog.d(this.TAG, str);
    }

    enum Scene {
        NONE(0),
        DATA_STALL(1),
        RTT_WORSE(2),
        LINK_WORSE(3);

        private int value;

        Scene(int i) {
            this.value = i;
        }
    }

    public enum RttWorseLevelEnum {
        L0(-1),
        L1(1),
        L2(2),
        L3(3);

        private int level;

        RttWorseLevelEnum(int i) {
            this.level = i;
        }

        public int getLevel() {
            return this.level;
        }

        public static RttWorseLevelEnum getInstance(int i) {
            for (RttWorseLevelEnum rttWorseLevelEnum : values()) {
                if (i == rttWorseLevelEnum.getLevel()) {
                    return rttWorseLevelEnum;
                }
            }
            return L0;
        }
    }
}
