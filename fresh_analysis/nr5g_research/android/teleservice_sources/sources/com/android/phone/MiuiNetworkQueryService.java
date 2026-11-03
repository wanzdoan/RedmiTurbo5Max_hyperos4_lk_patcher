package com.android.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.NetworkScan;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;
import android.util.Log;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.module.ITelephonyModule;
import com.xiaomi.mirilhook.MiRilHook;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntPredicate;
import miui.telephony.PhoneDebug;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetworkQueryService extends Service {
    private static final boolean DBG = PhoneDebug.VDBG;
    private Handler mBackgroundHandler;
    private boolean mDataEnabledBeforeScan;
    private FiveGNetworkScanBase mFiveGNetworkScan;
    private HandlerThread mHandlerThread;
    private TelephonyScanManager.NetworkScanCallback mInternalNetworkScanCallback;
    private NetworkScan mNetworkScanRequester;
    private volatile int mState;
    private String[] mStoreScanInfo;
    private TelephonyManager mTelephonyManager;
    private int mMaxSearchTimeSec = MiRilHook.HOOK_ID_START_URC_BY_AT;
    private List mScanCellInfo = new ArrayList();
    private List mScanResults = new ArrayList();
    private List mScanResultsForCellInfo = new ArrayList();
    private List mScanResultsForoperatorInfos = new ArrayList();
    private final ExecutorService mNetworkScanExecutor = Executors.newFixedThreadPool(1);
    private boolean isClearList = false;
    private final IBinder mLocalBinder = new LocalBinder();
    Handler mHandler = new Handler() { // from class: com.android.phone.MiuiNetworkQueryService.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (!MiuiNetworkQueryService.this.cancelQueryAvailableNetworks(message.what) && message.what == 100) {
                if (MiuiNetworkQueryService.DBG) {
                    MiuiNetworkQueryService.log("scan completed, broadcasting results");
                }
                boolean z = ModemProvider.IS_SPRD;
                MiuiNetworkQueryService miuiNetworkQueryService = MiuiNetworkQueryService.this;
                if (z) {
                    miuiNetworkQueryService.broadcastQueryResultsForSprd((List) message.obj);
                } else {
                    miuiNetworkQueryService.broadcastQueryResults((AsyncResult) message.obj);
                }
            }
        }
    };
    final RemoteCallbackList mCallbacks = new RemoteCallbackList();
    private final IMiuiNetworkQueryService.Stub mBinder = new AnonymousClass2();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiNetworkQueryService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("qualcomm.intent.action.ACTION_INCREMENTAL_NW_SCAN_IND".equals(intent.getAction())) {
                MiuiNetworkQueryService.this.broadcastIncrementalQueryResults(intent);
            }
        }
    };
    private int mSubId = -1;

    /* JADX INFO: renamed from: -$$Nest$fputmState, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m1171$$Nest$fputmState(MiuiNetworkQueryService miuiNetworkQueryService, int i) {
        miuiNetworkQueryService.mState = i;
    }

    /* JADX INFO: renamed from: -$$Nest$mcleanupNetworkScan, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m1178$$Nest$mcleanupNetworkScan(MiuiNetworkQueryService miuiNetworkQueryService, boolean z) {
        miuiNetworkQueryService.cleanupNetworkScan(z);
    }

    /* JADX INFO: renamed from: -$$Nest$smlog, reason: not valid java name */
    static /* bridge */ /* synthetic */ void m1181$$Nest$smlog(String str) {
        log(str);
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public IMiuiNetworkQueryService getService() {
            return MiuiNetworkQueryService.this.mBinder;
        }
    }

    /* JADX INFO: renamed from: com.android.phone.MiuiNetworkQueryService$2, reason: invalid class name */
    class AnonymousClass2 extends IMiuiNetworkQueryService.Stub {
        AnonymousClass2() {
        }

        @Override // com.android.phone.IMiuiNetworkQueryService
        public void startNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i) {
            if (iMiuiNetworkQueryServiceCallback != null) {
                synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                    try {
                        MiuiNetworkQueryService.this.mCallbacks.register(iMiuiNetworkQueryServiceCallback);
                        if (MiuiNetworkQueryService.DBG) {
                            MiuiNetworkQueryService.log("registering callback " + iMiuiNetworkQueryServiceCallback.getClass().toString());
                        }
                        int i2 = MiuiNetworkQueryService.this.mState;
                        if (i2 != -2) {
                            if (i2 == -1) {
                                MiuiNetworkQueryService.this.mStoreScanInfo = null;
                                boolean zStartNetworkScan = MiuiNetworkQueryService.this.mFiveGNetworkScan.startNetworkScan(i);
                                MiuiNetworkQueryService.log("start scan, result:" + zStartNetworkScan);
                                if (!zStartNetworkScan) {
                                    Phone phone = PhoneFactory.getPhone(i);
                                    if (phone != null) {
                                        if (ModemProvider.IS_SPRD) {
                                            MiuiNetworkQueryService.log("startNetworkQuery ");
                                            MiuiNetworkQueryService.this.mState = -2;
                                            final int subId = phone.getSubId();
                                            MiuiNetworkQueryService.this.mBackgroundHandler.post(new Runnable() { // from class: com.android.phone.MiuiNetworkQueryService$2$$ExternalSyntheticLambda0
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    this.f$0.lambda$startNetworkQuery$0(subId);
                                                }
                                            });
                                        } else {
                                            phone.getAvailableNetworks(MiuiNetworkQueryService.this.mHandler.obtainMessage(100));
                                            MiuiNetworkQueryService.this.mState = -2;
                                        }
                                        if (MiuiNetworkQueryService.DBG) {
                                            MiuiNetworkQueryService.log("starting new query");
                                        }
                                    } else if (MiuiNetworkQueryService.DBG) {
                                        MiuiNetworkQueryService.log("phone is null");
                                    }
                                } else {
                                    MiuiNetworkQueryService.this.mState = -2;
                                }
                            }
                        } else if (MiuiNetworkQueryService.DBG) {
                            MiuiNetworkQueryService.log("query already in progress");
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startNetworkQuery$0(int i) {
            TelephonyManager telephonyManager = (TelephonyManager) MiuiNetworkQueryService.this.getSystemService(TelephonyManager.class);
            if (telephonyManager == null) {
                synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                    MiuiNetworkQueryService.this.mState = -1;
                }
                return;
            }
            TelephonyManager telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(i);
            if (telephonyManagerCreateForSubscriptionId == null) {
                MiuiNetworkQueryService.log("startNetworkQuery TelephonyManager is null");
                synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                    MiuiNetworkQueryService.this.mState = -1;
                }
                return;
            }
            synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                try {
                    MiuiNetworkQueryService.this.mTelephonyManager = telephonyManagerCreateForSubscriptionId;
                    if (MiuiNetworkQueryService.this.mNetworkScanRequester != null) {
                        return;
                    }
                    MiuiNetworkQueryService.this.mDataEnabledBeforeScan = telephonyManagerCreateForSubscriptionId.isDataEnabled();
                    MiuiNetworkQueryService.log("requestNetworkScan");
                    MiuiNetworkQueryService miuiNetworkQueryService = MiuiNetworkQueryService.this;
                    miuiNetworkQueryService.mNetworkScanRequester = telephonyManagerCreateForSubscriptionId.requestNetworkScan(miuiNetworkQueryService.createNetworkScanForPreferredAccessNetworks(), MiuiNetworkQueryService.this.mNetworkScanExecutor, MiuiNetworkQueryService.this.mInternalNetworkScanCallback);
                    if (MiuiNetworkQueryService.this.mNetworkScanRequester == null) {
                        MiuiNetworkQueryService.this.mState = -1;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // com.android.phone.IMiuiNetworkQueryService
        public void stopNetworkQuery(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback, int i) {
            if (ModemProvider.IS_SPRD) {
                MiuiNetworkQueryService.log("stopNetworkQuery SPRD");
                synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                    try {
                        if (MiuiNetworkQueryService.this.mNetworkScanRequester != null) {
                            MiuiNetworkQueryService.this.mNetworkScanRequester.stopScan();
                            MiuiNetworkQueryService.this.mNetworkScanRequester = null;
                        }
                        MiuiNetworkQueryService.this.mState = -1;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                unregisterCallback(iMiuiNetworkQueryServiceCallback);
                return;
            }
            MiuiNetworkQueryService.log("stopNetworkQuery ");
            MiuiNetworkQueryService.this.stopQueryAvailableNetworks(i);
            unregisterCallback(iMiuiNetworkQueryServiceCallback);
        }

        @Override // com.android.phone.IMiuiNetworkQueryService
        public void unregisterCallback(IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback) {
            if (iMiuiNetworkQueryServiceCallback != null) {
                synchronized (MiuiNetworkQueryService.this.mCallbacks) {
                    try {
                        if (MiuiNetworkQueryService.DBG) {
                            MiuiNetworkQueryService.log("unregistering callback " + iMiuiNetworkQueryServiceCallback.getClass().toString());
                        }
                        MiuiNetworkQueryService.this.mCallbacks.unregister(iMiuiNetworkQueryServiceCallback);
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        this.mState = -1;
        this.mFiveGNetworkScan = FiveGNetworkScanBase.getInstance();
        if (ModemProvider.IS_SPRD) {
            HandlerThread handlerThread = new HandlerThread("StartNetworkQueryThread");
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            this.mBackgroundHandler = new Handler(this.mHandlerThread.getLooper());
            this.mInternalNetworkScanCallback = new NetworkScanCallbackImpl();
        }
        if (MiuiPhoneUtils.isQcSaNetwork()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("qualcomm.intent.action.ACTION_INCREMENTAL_NW_SCAN_IND");
            registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (MiuiPhoneUtils.isQcSaNetwork()) {
            unregisterReceiver(this.mBroadcastReceiver);
        }
        HandlerThread handlerThread = this.mHandlerThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
        this.mNetworkScanExecutor.shutdown();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        this.mSubId = intent != null ? intent.getIntExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", -1) : Integer.MAX_VALUE;
        log("onStartCommand, subId = " + this.mSubId);
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (DBG) {
            log("binding service implementation");
        }
        if ("com.android.phone.intent.action.LOCAL_BINDER".equals(intent.getAction())) {
            return this.mLocalBinder;
        }
        return this.mBinder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: NullPointerException in pass: BlockProcessor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
        	at jadx.core.dex.visitors.blocks.BlockSplitter.connect(BlockSplitter.java:159)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectSplittersAndHandlers(BlockExceptionHandler.java:501)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.wrapBlocksWithTryCatch(BlockExceptionHandler.java:382)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectExcHandlers(BlockExceptionHandler.java:91)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:62)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:422)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:57)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:50)
        */
    public void broadcastQueryResults(android.os.AsyncResult r7) {
        /*
            r6 = this;
            java.lang.String r0 = "AsyncResult has exception "
            android.os.RemoteCallbackList r1 = r6.mCallbacks
            monitor-enter(r1)
            r2 = -1
            r6.mState = r2     // Catch: java.lang.Throwable -> L14
            if (r7 != 0) goto L18
            boolean r6 = com.android.phone.MiuiNetworkQueryService.DBG     // Catch: java.lang.Throwable -> L14
            if (r6 == 0) goto L16
            java.lang.String r6 = "AsyncResult is null."
            log(r6)     // Catch: java.lang.Throwable -> L14
            goto L16
        L14:
            r6 = move-exception
            goto L71
        L16:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L14
            return
        L18:
            java.lang.Throwable r3 = r7.exception     // Catch: java.lang.Throwable -> L14
            if (r3 != 0) goto L1d
            r2 = 0
        L1d:
            boolean r3 = com.android.phone.MiuiNetworkQueryService.DBG     // Catch: java.lang.Throwable -> L14
            if (r3 == 0) goto L30
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L14
            r3.<init>(r0)     // Catch: java.lang.Throwable -> L14
            r3.append(r2)     // Catch: java.lang.Throwable -> L14
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Throwable -> L14
            log(r0)     // Catch: java.lang.Throwable -> L14
        L30:
            android.os.RemoteCallbackList r0 = r6.mCallbacks     // Catch: java.lang.Throwable -> L14
            int r0 = r0.beginBroadcast()     // Catch: java.lang.Throwable -> L14
            int r0 = r0 + (-1)
        L38:
            android.os.RemoteCallbackList r3 = r6.mCallbacks
            if (r0 < 0) goto L6c
            android.os.IInterface r3 = r3.getBroadcastItem(r0)     // Catch: java.lang.Throwable -> L14
            com.android.phone.IMiuiNetworkQueryServiceCallback r3 = (com.android.phone.IMiuiNetworkQueryServiceCallback) r3     // Catch: java.lang.Throwable -> L14
            boolean r4 = com.android.phone.MiuiNetworkQueryService.DBG     // Catch: java.lang.Throwable -> L14
            if (r4 == 0) goto L62
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L14
            r4.<init>()     // Catch: java.lang.Throwable -> L14
            java.lang.String r5 = "broadcasting results to "
            r4.append(r5)     // Catch: java.lang.Throwable -> L14
            java.lang.Class r5 = r3.getClass()     // Catch: java.lang.Throwable -> L14
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L14
            r4.append(r5)     // Catch: java.lang.Throwable -> L14
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L14
            log(r4)     // Catch: java.lang.Throwable -> L14
        L62:
            java.lang.Object r4 = r7.result     // Catch: java.lang.Throwable -> L14 android.os.RemoteException -> L69
            java.util.ArrayList r4 = (java.util.ArrayList) r4     // Catch: java.lang.Throwable -> L14 android.os.RemoteException -> L69
            r3.onQueryComplete(r4, r2)     // Catch: java.lang.Throwable -> L14 android.os.RemoteException -> L69
        L69:
            int r0 = r0 + (-1)
            goto L38
        L6c:
            r3.finishBroadcast()     // Catch: java.lang.Throwable -> L14
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L14
            return
        L71:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L14
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.phone.MiuiNetworkQueryService.broadcastQueryResults(android.os.AsyncResult):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastIncrementalQueryResults(Intent intent) {
        synchronized (this.mCallbacks) {
            try {
                int intExtra = intent.getIntExtra("scan_result", -1);
                int intExtra2 = intent.getIntExtra("sub_id", Integer.MAX_VALUE);
                if (DBG) {
                    log("broadcastIncrementalQueryResults: phoneid: " + intExtra2);
                }
                if (!SubscriptionManager.isValidPhoneId(intExtra2)) {
                    return;
                }
                String[] stringArrayExtra = intent.getStringArrayExtra("incr_nw_scan_data");
                if (intExtra != 1) {
                    this.mState = -1;
                }
                StringBuilder sb = new StringBuilder("on scan result received, result: ");
                sb.append(intExtra);
                sb.append(", scanInfo.length: ");
                sb.append(stringArrayExtra == null ? 0 : stringArrayExtra.length);
                sb.append(",scanInfo=");
                sb.append(Arrays.toString(stringArrayExtra));
                log(sb.toString());
                if (this.mStoreScanInfo != null && stringArrayExtra != null) {
                    concatScanInfo(stringArrayExtra);
                } else {
                    this.mStoreScanInfo = stringArrayExtra;
                }
                int iBeginBroadcast = this.mCallbacks.beginBroadcast() - 1;
                while (true) {
                    RemoteCallbackList remoteCallbackList = this.mCallbacks;
                    if (iBeginBroadcast >= 0) {
                        IMiuiNetworkQueryServiceCallback iMiuiNetworkQueryServiceCallback = (IMiuiNetworkQueryServiceCallback) remoteCallbackList.getBroadcastItem(iBeginBroadcast);
                        if (DBG) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("broadcasting results to ");
                            sb2.append(iMiuiNetworkQueryServiceCallback.getClass().toString());
                            sb2.append(", mStoreScanInfo = ");
                            String[] strArr = this.mStoreScanInfo;
                            sb2.append(strArr != null ? Arrays.toString(strArr) : "NULL");
                            log(sb2.toString());
                        }
                        try {
                            iMiuiNetworkQueryServiceCallback.onIncrementalManualScanResult(this.mStoreScanInfo, intExtra);
                        } catch (RemoteException e) {
                            if (DBG) {
                                log("Partial query remote exception " + e);
                            }
                        }
                        iBeginBroadcast--;
                    } else {
                        remoteCallbackList.finishBroadcast();
                        return;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void concatScanInfo(String[] strArr) {
        log("concatScanInfo mStoreScanInfo: " + Arrays.toString(strArr) + ";mStoreScanInfo: " + Arrays.toString(strArr));
        String[] strArr2 = this.mStoreScanInfo;
        String[] strArr3 = new String[strArr2.length + strArr.length];
        System.arraycopy(strArr2, 0, strArr3, 0, strArr2.length);
        System.arraycopy(strArr, 0, strArr3, this.mStoreScanInfo.length, strArr.length);
        this.mStoreScanInfo = strArr3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("NetworkQuery", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean cancelQueryAvailableNetworks(int i) {
        log("[handleMessage] msg = " + i);
        if (i != 200) {
            return false;
        }
        if (!DBG) {
            return true;
        }
        log("cancel get available networks action... ");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopQueryAvailableNetworks(int i) {
        boolean z = DBG;
        if (z) {
            log("[stopQueryAvailableNetworks] cancelAvailableNetworks to PhoneID = " + i + "; mState = " + this.mState);
        }
        if (-2 == this.mState) {
            if (!this.mFiveGNetworkScan.stopNetworkScan(i)) {
                if (z) {
                    log("failed to cancel ongoing network query");
                }
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).cancelAvailableNetworks(this.mHandler, 200, this.mSubId);
            }
            this.mState = -1;
        }
    }

    class NetworkScanCallbackImpl extends TelephonyScanManager.NetworkScanCallback {
        private NetworkScanCallbackImpl() {
        }

        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onResults(List list) {
            if (list == null || list.size() == 0) {
                MiuiNetworkQueryService.log("NetworkScanCallbackImpl onResults, results is null");
                return;
            }
            if (MiuiNetworkQueryService.this.isClearList) {
                if (MiuiNetworkQueryService.this.mScanResults != null && MiuiNetworkQueryService.this.mScanResults.size() > 0) {
                    MiuiNetworkQueryService.this.mScanResults.clear();
                }
                if (MiuiNetworkQueryService.this.mScanResultsForoperatorInfos != null && MiuiNetworkQueryService.this.mScanResultsForoperatorInfos.size() > 0) {
                    MiuiNetworkQueryService.this.mScanResultsForoperatorInfos.clear();
                }
                if (MiuiNetworkQueryService.this.mScanResultsForCellInfo != null && MiuiNetworkQueryService.this.mScanResultsForCellInfo.size() > 0) {
                    MiuiNetworkQueryService.this.mScanResultsForCellInfo.clear();
                }
                if (MiuiNetworkQueryService.this.mScanCellInfo != null && MiuiNetworkQueryService.this.mScanCellInfo.size() > 0) {
                    MiuiNetworkQueryService.this.mScanCellInfo.clear();
                }
                MiuiNetworkQueryService.this.isClearList = false;
            }
            if (MiuiNetworkQueryService.this.mScanCellInfo == null) {
                MiuiNetworkQueryService.this.mScanCellInfo = new ArrayList();
            }
            MiuiNetworkQueryService.this.mScanCellInfo.addAll(list);
            MiuiNetworkQueryService miuiNetworkQueryService = MiuiNetworkQueryService.this;
            miuiNetworkQueryService.mScanResultsForCellInfo = SprdCellInfoUtil.convertCellInfoToOperatorInfo(miuiNetworkQueryService.mScanCellInfo);
            if (MiuiNetworkQueryService.this.mScanResultsForCellInfo == null || MiuiNetworkQueryService.this.mScanResultsForCellInfo.size() <= 0) {
                return;
            }
            MiuiNetworkQueryService.log("NetworkScanCallbackImpl add onResults ");
            if (MiuiNetworkQueryService.this.mScanResultsForoperatorInfos == null) {
                MiuiNetworkQueryService.this.mScanResultsForoperatorInfos = new ArrayList();
            }
            MiuiNetworkQueryService.this.mScanResultsForoperatorInfos.addAll(MiuiNetworkQueryService.this.mScanResultsForCellInfo);
        }

        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onComplete() {
            MiuiNetworkQueryService.log("NetworkScanCallbackImpl onComplete ");
            if (MiuiNetworkQueryService.this.mScanResults == null) {
                MiuiNetworkQueryService.this.mScanResults = new ArrayList();
            }
            if (MiuiNetworkQueryService.this.mScanResultsForoperatorInfos != null && MiuiNetworkQueryService.this.mScanResultsForoperatorInfos.size() > 0) {
                for (OperatorInfo operatorInfo : MiuiNetworkQueryService.this.mScanResultsForoperatorInfos) {
                    if (!SprdCellInfoUtil.containsOperatorInfo(MiuiNetworkQueryService.this.mScanResults, operatorInfo)) {
                        MiuiNetworkQueryService.this.mScanResults.add(operatorInfo);
                    }
                }
            }
            SprdCellInfoUtil.getForbiddenList(MiuiNetworkQueryService.this.mTelephonyManager);
            MiuiNetworkQueryService.this.mHandler.sendMessage(MiuiNetworkQueryService.this.mHandler.obtainMessage(100, new ArrayList(SprdCellInfoUtil.sortWithCurrentFirst(MiuiNetworkQueryService.this.mScanResults))));
            MiuiNetworkQueryService.this.isClearList = true;
            MiuiNetworkQueryService.this.cleanupNetworkScan(false);
        }

        /*  JADX ERROR: NullPointerException in pass: BlockProcessor
            java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
            	at jadx.core.dex.visitors.blocks.BlockSplitter.connect(BlockSplitter.java:159)
            	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectSplittersAndHandlers(BlockExceptionHandler.java:501)
            	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.wrapBlocksWithTryCatch(BlockExceptionHandler.java:382)
            	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectExcHandlers(BlockExceptionHandler.java:91)
            	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:62)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:422)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:57)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:50)
            */
        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onError(int r5) {
            /*
                r4 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                java.lang.String r1 = "NetworkScanCallbackImpl onError "
                r0.<init>(r1)
                r0.append(r5)
                java.lang.String r5 = r0.toString()
                com.android.phone.MiuiNetworkQueryService.m1181$$Nest$smlog(r5)
                com.android.phone.MiuiNetworkQueryService r5 = com.android.phone.MiuiNetworkQueryService.this
                r0 = 0
                com.android.phone.MiuiNetworkQueryService.m1178$$Nest$mcleanupNetworkScan(r5, r0)
                com.android.phone.MiuiNetworkQueryService r5 = com.android.phone.MiuiNetworkQueryService.this
                android.os.RemoteCallbackList r5 = r5.mCallbacks
                monitor-enter(r5)
                com.android.phone.MiuiNetworkQueryService r0 = com.android.phone.MiuiNetworkQueryService.this     // Catch: java.lang.Throwable -> L44
                r1 = -1
                com.android.phone.MiuiNetworkQueryService.m1171$$Nest$fputmState(r0, r1)     // Catch: java.lang.Throwable -> L44
                com.android.phone.MiuiNetworkQueryService r0 = com.android.phone.MiuiNetworkQueryService.this     // Catch: java.lang.Throwable -> L44
                android.os.RemoteCallbackList r0 = r0.mCallbacks     // Catch: java.lang.Throwable -> L44
                int r0 = r0.beginBroadcast()     // Catch: java.lang.Throwable -> L44
                int r0 = r0 + (-1)
            L2c:
                com.android.phone.MiuiNetworkQueryService r2 = com.android.phone.MiuiNetworkQueryService.this
                if (r0 < 0) goto L49
                android.os.RemoteCallbackList r2 = r2.mCallbacks     // Catch: java.lang.Throwable -> L44
                android.os.IInterface r2 = r2.getBroadcastItem(r0)     // Catch: java.lang.Throwable -> L44
                com.android.phone.IMiuiNetworkQueryServiceCallback r2 = (com.android.phone.IMiuiNetworkQueryServiceCallback) r2     // Catch: java.lang.Throwable -> L44
                if (r2 != 0) goto L3b
                goto L46
            L3b:
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L44 android.os.RemoteException -> L46
                r3.<init>()     // Catch: java.lang.Throwable -> L44 android.os.RemoteException -> L46
                r2.onQueryComplete(r3, r1)     // Catch: java.lang.Throwable -> L44 android.os.RemoteException -> L46
                goto L46
            L44:
                r4 = move-exception
                goto L50
            L46:
                int r0 = r0 + (-1)
                goto L2c
            L49:
                android.os.RemoteCallbackList r4 = r2.mCallbacks     // Catch: java.lang.Throwable -> L44
                r4.finishBroadcast()     // Catch: java.lang.Throwable -> L44
                monitor-exit(r5)     // Catch: java.lang.Throwable -> L44
                return
            L50:
                monitor-exit(r5)     // Catch: java.lang.Throwable -> L44
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.phone.MiuiNetworkQueryService.NetworkScanCallbackImpl.onError(int):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupNetworkScan(boolean z) {
        TelephonyManager telephonyManager;
        synchronized (this.mCallbacks) {
            try {
                if (this.mNetworkScanRequester != null) {
                    if (z) {
                        log("NetworkScanCallbackImpl stopScan ");
                        this.mNetworkScanRequester.stopScan();
                    }
                    this.mNetworkScanRequester = null;
                }
                this.mState = -1;
                if (this.mDataEnabledBeforeScan && (telephonyManager = this.mTelephonyManager) != null) {
                    telephonyManager.setDataEnabled(true);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        SprdCellInfoUtil.clearCache();
    }

    NetworkScanRequest createNetworkScanForPreferredAccessNetworks() {
        long preferredNetworkTypeBitmask = this.mTelephonyManager.getPreferredNetworkTypeBitmask();
        long j = 906119 & preferredNetworkTypeBitmask;
        log("createNetworkScanForPreferredAccessNetworks, nwBitmask=" + Long.toHexString(preferredNetworkTypeBitmask) + ", 3gppBitmask=" + Long.toHexString(j));
        ArrayList arrayList = new ArrayList();
        if (j == 0 || (32771 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(1, null, null));
        }
        if (j == 0 || (82820 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(2, null, null));
        }
        if (j == 0 || (266240 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(3, null, null));
        }
        if (j == 0 || (hasNrSaCapability() && (preferredNetworkTypeBitmask & 524288) != 0)) {
            arrayList.add(new RadioAccessSpecifier(6, null, null));
            log("radioAccessSpecifiers add NGRAN.");
        }
        return new NetworkScanRequest(0, (RadioAccessSpecifier[]) arrayList.toArray(new RadioAccessSpecifier[arrayList.size()]), 5, this.mMaxSearchTimeSec, true, 3, null);
    }

    private boolean hasNrSaCapability() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager == null) {
            return false;
        }
        return Arrays.stream(telephonyManager.getPhoneCapability().getDeviceNrCapabilities()).anyMatch(new IntPredicate() { // from class: com.android.phone.MiuiNetworkQueryService$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return MiuiNetworkQueryService.$r8$lambda$aLC8bowJUaI2ba8QSfEZ2ksQtbI(i);
            }
        });
    }

    public static /* synthetic */ boolean $r8$lambda$aLC8bowJUaI2ba8QSfEZ2ksQtbI(int i) {
        return i == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: NullPointerException in pass: BlockProcessor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
        	at jadx.core.dex.visitors.blocks.BlockSplitter.connect(BlockSplitter.java:159)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectSplittersAndHandlers(BlockExceptionHandler.java:501)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.wrapBlocksWithTryCatch(BlockExceptionHandler.java:382)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectExcHandlers(BlockExceptionHandler.java:91)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:62)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:422)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:57)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:50)
        */
    public void broadcastQueryResultsForSprd(java.util.List r6) {
        /*
            r5 = this;
            android.os.RemoteCallbackList r0 = r5.mCallbacks
            monitor-enter(r0)
            r1 = -1
            r5.mState = r1     // Catch: java.lang.Throwable -> L12
            if (r6 != 0) goto L16
            boolean r5 = com.android.phone.MiuiNetworkQueryService.DBG     // Catch: java.lang.Throwable -> L12
            if (r5 == 0) goto L14
            java.lang.String r5 = "broadcastQueryResultsForSprd is null."
            log(r5)     // Catch: java.lang.Throwable -> L12
            goto L14
        L12:
            r5 = move-exception
            goto L6d
        L14:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L12
            return
        L16:
            android.os.RemoteCallbackList r1 = r5.mCallbacks     // Catch: java.lang.Throwable -> L12
            int r1 = r1.beginBroadcast()     // Catch: java.lang.Throwable -> L12
            int r1 = r1 + (-1)
        L1e:
            android.os.RemoteCallbackList r2 = r5.mCallbacks
            if (r1 < 0) goto L68
            android.os.IInterface r2 = r2.getBroadcastItem(r1)     // Catch: java.lang.Throwable -> L12
            com.android.phone.IMiuiNetworkQueryServiceCallback r2 = (com.android.phone.IMiuiNetworkQueryServiceCallback) r2     // Catch: java.lang.Throwable -> L12
            boolean r3 = com.android.phone.MiuiNetworkQueryService.DBG     // Catch: java.lang.Throwable -> L12
            if (r3 == 0) goto L48
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L12
            r3.<init>()     // Catch: java.lang.Throwable -> L12
            java.lang.String r4 = "broadcasting results to "
            r3.append(r4)     // Catch: java.lang.Throwable -> L12
            java.lang.Class r4 = r2.getClass()     // Catch: java.lang.Throwable -> L12
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L12
            r3.append(r4)     // Catch: java.lang.Throwable -> L12
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L12
            log(r3)     // Catch: java.lang.Throwable -> L12
        L48:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            r3.<init>()     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            java.lang.String r4 = "nsrlist: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            java.lang.String r4 = r6.toString()     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            r3.append(r4)     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            log(r3)     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
            r3 = 0
            r2.onQueryComplete(r6, r3)     // Catch: java.lang.Throwable -> L12 android.os.RemoteException -> L65
        L65:
            int r1 = r1 + (-1)
            goto L1e
        L68:
            r2.finishBroadcast()     // Catch: java.lang.Throwable -> L12
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L12
            return
        L6d:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L12
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.phone.MiuiNetworkQueryService.broadcastQueryResultsForSprd(java.util.List):void");
    }
}
