package com.android.phone.badcells;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.phone.FiveGCloudController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.SprdModemDump$$ExternalSyntheticLambda1;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.i.a;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class BadCellStats {
    private static volatile BadCellStats sInstance;
    private Context mContext;
    private Handler mHandler;
    private BadCellDynamicWeight mLteWeights;
    private BadCellDynamicWeight mNrWeights;
    private HandlerThread mWorkThread;
    private boolean mEnbaleDynamicWeight = true;
    private boolean mHasInvalidHistoryCacheInfo = true;
    private HashMap mCellListMap = new HashMap();
    private LinkedHashMap mTopBadCellListNR = new LinkedHashMap();
    private LinkedHashMap mTopBadCellListLTE = new LinkedHashMap();
    private Deque mEventCountsQueue = new LinkedList();
    private int mFoundCn = 0;
    private int mNotFoundCn = 0;
    private final int ERROR_EVENT_NUM = 5;
    private final int MAX_EVENT_STATS_QUE_SIZE = 7;
    private final int MIN_BAD_CELL_SCORE = 5;
    private final int MAX_UPDATE_PERIOD = 7;
    private final int RAT_TYPE_NUM = 2;
    private final int MAX_BAD_CELL_NUM = 10;

    /* JADX INFO: renamed from: $r8$lambda$LqyNBgs8Vd4-TJmjfoLPYVk9TYs, reason: not valid java name */
    public static /* synthetic */ BadCellInfo m2329$r8$lambda$LqyNBgs8Vd4TJmjfoLPYVk9TYs(BadCellInfo badCellInfo, BadCellInfo badCellInfo2) {
        return badCellInfo;
    }

    public static /* synthetic */ BadCellInfo $r8$lambda$fXdkURdv_NdkoiBfJyp4t8hnAP8(BadCellInfo badCellInfo, BadCellInfo badCellInfo2) {
        return badCellInfo;
    }

    public static synchronized void init(Context context) {
        try {
            if (sInstance == null) {
                sInstance = new BadCellStats(context);
                logd("BadCellStats module init..");
            } else {
                logd("BadCellStats module already initialized..");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public static synchronized BadCellStats getInstance() {
        if (sInstance == null) {
            loge("BadCellStats is null");
            return null;
        }
        return sInstance;
    }

    private BadCellStats(Context context) {
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("BadCellStatsModule");
        this.mWorkThread = handlerThread;
        handlerThread.start();
        WorkerHandler workerHandler = new WorkerHandler(this.mWorkThread.getLooper());
        this.mHandler = workerHandler;
        workerHandler.sendEmptyMessage(0);
        this.mNrWeights = new BadCellDynamicWeight(0, this.mContext);
        this.mLteWeights = new BadCellDynamicWeight(1, this.mContext);
    }

    class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            BadCellStats.logd("msg = " + message.what);
            switch (message.what) {
                case 0:
                    BadCellStats.this.infoRestore();
                    break;
                case 1:
                    BadCellStats.this.dynamicWeightsAdjustCheck();
                    break;
                case 2:
                    BadCellStats.this.calculateCellScore();
                    break;
                case 3:
                    BadCellStats.this.removeExpiredCells();
                    break;
                case 4:
                    BadCellStats.this.reportBadCellStat();
                    break;
                case 5:
                    BadCellStats.this.dealWithReportEnd();
                    break;
                case 6:
                    BadCellStats.this.infoCache();
                    break;
                case 7:
                    BadCellStats.this.printBadCellList();
                    break;
                default:
                    BadCellStats.loge("unknown message");
                    break;
            }
        }
    }

    public void syncBadCellInfo(String str, ArrayList arrayList) {
        if (!MiuiPhoneUtils.isSupportBadCellOptimization()) {
            loge("bad cell opt not supported");
            return;
        }
        int iEventNameToEventNum = eventNameToEventNum(str);
        if (iEventNameToEventNum < 0) {
            loge("syncBadCellInfo(): undefined eventName, exit syncBadCellInfo()");
            return;
        }
        int eventRat = EventConstants.getEventRat(iEventNameToEventNum);
        if (eventRat < 0) {
            loge("syncBadCellInfo(): undefined ratType, exit syncBadCellInfo()");
            return;
        }
        if (arrayList == null) {
            loge("syncBadCellInfo(): cellMapList is null, exit syncBadCellInfo()");
            return;
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Map map = (Map) obj;
            if (!validCellMap(map)) {
                loge("syncBadCellInfo(): Invalid cellMap");
            } else {
                String str2 = String.valueOf(map.get("GCI")) + "-" + String.valueOf(map.get("arfcn")) + "-" + String.valueOf(map.get("pci"));
                int iQcErrEvtCount = qcErrEvtCount(iEventNameToEventNum, map);
                if (this.mCellListMap.containsKey(str2)) {
                    ((BadCellInfo) this.mCellListMap.get(str2)).recordEventCounts(iEventNameToEventNum, iQcErrEvtCount);
                } else {
                    BadCellInfo badCellInfo = new BadCellInfo(eventRat);
                    badCellInfo.recordEventCounts(iEventNameToEventNum, iQcErrEvtCount);
                    this.mCellListMap.put(str2, badCellInfo);
                }
            }
        }
    }

    private int eventNameToEventNum(String str) {
        str.hashCode();
        switch (str) {
            case "getNr5gNasRejectInfo":
                return 405;
            case "getNr5gRlfInfo":
                return 403;
            case "getLteRlfInfo":
                return 402;
            case "getLteFullSchInfo":
                return 406;
            case "getNr5gBlerAbnormalInfo":
                return 409;
            case "getLteRachFailInfo":
                return 412;
            case "getLteBlerAbnormalInfo":
                return ResponseObject.RELAY_TIMEOUT_CODE;
            case "getNr5gRachFailInfo":
                return 413;
            case "getNr5gFullSchInfo":
                return 407;
            case "getLteNasRejectInfo":
                return a.c;
            default:
                loge("eventNameToEventNum(): Unknown event");
                return -1;
        }
    }

    private boolean validCellMap(Map map) {
        return map.containsKey("arfcn") && map.containsKey("pci") && map.containsKey("GCI");
    }

    private int qcErrEvtCount(int i, Map map) {
        switch (i) {
            case 402:
            case 403:
            case 412:
            case 413:
                return ((Integer) map.get("A1")).intValue() + ((Integer) map.get("A3")).intValue();
            case a.c /* 404 */:
            case 405:
                return 1;
            case 406:
            case 407:
            case ResponseObject.RELAY_TIMEOUT_CODE /* 408 */:
            case 409:
                return ((Integer) map.get("6sCnt")).intValue();
            case 410:
            case 411:
            default:
                return 0;
        }
    }

    public void notifySyncCellInfoCompleted() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dynamicWeightsAdjustCheck() {
        Object obj;
        boolean z = this.mEnbaleDynamicWeight;
        if (this.mCellListMap.isEmpty()) {
            loge("mCellListMap is empty, exit dynamicWeightsAdjustCheck, daily BadcellStats terminated");
            return;
        }
        ArrayList arrayList = new ArrayList(Collections.nCopies(2, null));
        arrayList.set(0, new ErrorEventCounts(0));
        arrayList.set(1, new ErrorEventCounts(1));
        new ErrorEventCounts();
        Iterator it = this.mCellListMap.entrySet().iterator();
        while (it.hasNext()) {
            BadCellInfo badCellInfo = (BadCellInfo) ((Map.Entry) it.next()).getValue();
            if (badCellInfo.mRat == 0) {
                obj = arrayList.get(0);
            } else {
                obj = arrayList.get(1);
            }
            ErrorEventCounts errorEventCounts = (ErrorEventCounts) obj;
            errorEventCounts.mRlfCounts += badCellInfo.mRlfCounts;
            errorEventCounts.mNasCounts += badCellInfo.mNasCounts;
            errorEventCounts.mSchCounts += badCellInfo.mSchCounts;
            errorEventCounts.mBlerCounts += badCellInfo.mBlerCounts;
            errorEventCounts.mRachCounts += badCellInfo.mRachCounts;
            badCellInfo.updateWeeklyStats();
        }
        if (((ErrorEventCounts) arrayList.get(0)).total() > 0 || ((ErrorEventCounts) arrayList.get(1)).total() > 0) {
            this.mEventCountsQueue.addLast(arrayList);
        }
        int size = this.mEventCountsQueue.size();
        Deque deque = this.mEventCountsQueue;
        if (size > 7) {
            deque.removeFirst();
        } else if (deque.size() < 7) {
            logd("dynamicWeightsAdjustCheck: No valid seven-day statistics exist");
            z = false;
        }
        if (z) {
            this.mNrWeights.eventStatsAndWeightUpdate(this.mEventCountsQueue);
            this.mLteWeights.eventStatsAndWeightUpdate(this.mEventCountsQueue);
        }
        this.mHandler.obtainMessage(2).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateCellScore() {
        if (this.mCellListMap.isEmpty()) {
            loge("calculateCellScore: mCellListMap is empty, exit calculateCellScore");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : this.mCellListMap.keySet()) {
            BadCellInfo badCellInfo = (BadCellInfo) this.mCellListMap.get(str);
            int i = badCellInfo.mRat;
            if (i == 0) {
                badCellInfo.cellScoreCalcu(this.mNrWeights.getWeights());
            } else if (i == 1) {
                badCellInfo.cellScoreCalcu(this.mLteWeights.getWeights());
            }
            badCellInfo.mTimeStamp = LocalDateTime.now();
            if (badCellInfo.mTotalScore.doubleValue() <= 0.0d) {
                arrayList.add(str);
            }
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            this.mCellListMap.remove((String) obj);
        }
        this.mHandler.obtainMessage(3).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExpiredCells() {
        if (this.mCellListMap.isEmpty()) {
            loge("removeExpiredCells(): mCellListMap is empty, exit removeExpiredCells()");
            return;
        }
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Iterator it = this.mCellListMap.entrySet().iterator();
        while (it.hasNext()) {
            if (Duration.between(((BadCellInfo) ((Map.Entry) it.next()).getValue()).mTimeStamp, localDateTimeNow).toDays() > 7) {
                it.remove();
            }
        }
        this.mTopBadCellListNR = (LinkedHashMap) this.mCellListMap.entrySet().stream().filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return BadCellStats.$r8$lambda$GEfbpDKnEZhhsKRxSSHEUxlt8BU((Map.Entry) obj);
            }
        }).sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(new ToDoubleFunction() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda13
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((BadCellInfo) obj).mTotalScore.doubleValue();
            }
        })).reversed()).limit(10L).collect(Collectors.toMap(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (String) ((Map.Entry) obj).getKey();
            }
        }, new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (BadCellInfo) ((Map.Entry) obj).getValue();
            }
        }, new BinaryOperator() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda16
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return BadCellStats.$r8$lambda$fXdkURdv_NdkoiBfJyp4t8hnAP8((BadCellInfo) obj, (BadCellInfo) obj2);
            }
        }, new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final Object get() {
                return new LinkedHashMap();
            }
        }));
        this.mTopBadCellListLTE = (LinkedHashMap) this.mCellListMap.entrySet().stream().filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return BadCellStats.$r8$lambda$k3IRf919kch0K4X9hUESn7QJNbk((Map.Entry) obj);
            }
        }).sorted(Map.Entry.comparingByValue(Comparator.comparingDouble(new ToDoubleFunction() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda18
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((BadCellInfo) obj).mTotalScore.doubleValue();
            }
        })).reversed()).limit(10L).collect(Collectors.toMap(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (String) ((Map.Entry) obj).getKey();
            }
        }, new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (BadCellInfo) ((Map.Entry) obj).getValue();
            }
        }, new BinaryOperator() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda11
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return BadCellStats.m2329$r8$lambda$LqyNBgs8Vd4TJmjfoLPYVk9TYs((BadCellInfo) obj, (BadCellInfo) obj2);
            }
        }, new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda12
            @Override // java.util.function.Supplier
            public final Object get() {
                return new LinkedHashMap();
            }
        }));
        this.mHandler.obtainMessage(4).sendToTarget();
    }

    public static /* synthetic */ boolean $r8$lambda$GEfbpDKnEZhhsKRxSSHEUxlt8BU(Map.Entry entry) {
        return ((BadCellInfo) entry.getValue()).mRat == 0;
    }

    public static /* synthetic */ boolean $r8$lambda$k3IRf919kch0K4X9hUESn7QJNbk(Map.Entry entry) {
        return ((BadCellInfo) entry.getValue()).mRat == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void infoCache() {
        if (this.mCellListMap.isEmpty()) {
            loge("infoCache: mCellListMap is empty, no need to cache");
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : this.mCellListMap.entrySet()) {
            String str = (String) entry.getKey();
            BadCellInfo badCellInfo = (BadCellInfo) entry.getValue();
            sb.append(String.join("#", str, String.valueOf(badCellInfo.mRat), badCellInfo.mTimeStamp.toString(), String.valueOf(badCellInfo.mTotalScore), String.valueOf(badCellInfo.mWeeklyBlerCounts), String.valueOf(badCellInfo.mWeeklyRlfCounts), String.valueOf(badCellInfo.mWeeklyNasCounts), String.valueOf(badCellInfo.mWeeklySchCounts), String.valueOf(badCellInfo.mWeeklyRachCounts), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailyScores.stream().map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return String.valueOf((Double) obj);
                }
            }).collect(Collectors.toList())), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailyRlfCountQue.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.toList())), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailyNasCountQue.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.toList())), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailyRachCountQue.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.toList())), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailyBlerCountQue.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.toList())), String.join(",", (Iterable<? extends CharSequence>) badCellInfo.mDailySchCountQue.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.toList()))));
            sb.append(";");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        editorEdit.putString("mCellListMap", sb.toString());
        if (this.mEnbaleDynamicWeight) {
            this.mNrWeights.cacheWeights("nr");
            this.mLteWeights.cacheWeights("lte");
        }
        if (!this.mEventCountsQueue.isEmpty() && this.mEnbaleDynamicWeight) {
            logd("infoCache(): mEventCountsQueue is not empty");
            StringBuilder sb2 = new StringBuilder();
            for (ArrayList arrayList : this.mEventCountsQueue) {
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    Object obj = arrayList.get(i);
                    i++;
                    ErrorEventCounts errorEventCounts = (ErrorEventCounts) obj;
                    arrayList2.add(String.join(",", String.valueOf(errorEventCounts.mRat), String.valueOf(errorEventCounts.mRlfCounts), String.valueOf(errorEventCounts.mNasCounts), String.valueOf(errorEventCounts.mSchCounts), String.valueOf(errorEventCounts.mBlerCounts), String.valueOf(errorEventCounts.mRachCounts)));
                }
                sb2.append(String.join("#", arrayList2));
                sb2.append(";");
            }
            if (sb2.length() > 0) {
                sb2.setLength(sb2.length() - 1);
            }
            editorEdit.putString("mEventCountsQueue", sb2.toString());
        }
        editorEdit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void infoRestore() {
        SharedPreferences sharedPreferences;
        char c;
        String str;
        String str2;
        char c2;
        int i;
        String str3;
        char c3;
        char c4;
        char c5;
        ensureSafeInfoRestore();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (defaultSharedPreferences.contains("mCellListMap")) {
            String string = defaultSharedPreferences.getString("mCellListMap", "");
            char c6 = 6;
            char c7 = 2;
            String str4 = "#";
            String str5 = ";";
            char c8 = 1;
            int i2 = 0;
            String str6 = ",";
            if (!TextUtils.isEmpty(string)) {
                String[] strArrSplit = string.split(";");
                int length = strArrSplit.length;
                int i3 = 0;
                while (i3 < length) {
                    String[] strArrSplit2 = strArrSplit[i3].split(str4);
                    if (strArrSplit2.length >= 15) {
                        String str7 = strArrSplit2[i2];
                        int i4 = Integer.parseInt(strArrSplit2[c8]);
                        LocalDateTime localDateTime = LocalDateTime.parse(strArrSplit2[c7]);
                        double d = Double.parseDouble(strArrSplit2[3]);
                        int i5 = Integer.parseInt(strArrSplit2[4]);
                        int i6 = Integer.parseInt(strArrSplit2[5]);
                        int i7 = Integer.parseInt(strArrSplit2[c6]);
                        int i8 = Integer.parseInt(strArrSplit2[7]);
                        int i9 = Integer.parseInt(strArrSplit2[8]);
                        Queue queue = (Queue) Arrays.stream(strArrSplit2[9].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$7X80KtdpI8f4bxQ2evcF2zxANE0((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda6
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Double.valueOf(Double.parseDouble((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        Queue queue2 = (Queue) Arrays.stream(strArrSplit2[10].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda7
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$LyPki8zzQld4NiP50EJ4Psd1tYg((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Integer.valueOf(Integer.parseInt((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        Queue queue3 = (Queue) Arrays.stream(strArrSplit2[11].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda8
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$PZwEcJnIZuoWT40f5LMy4cVMccY((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Integer.valueOf(Integer.parseInt((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        Queue queue4 = (Queue) Arrays.stream(strArrSplit2[12].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda9
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$LwSBbsZna4OwJQYtzXR2IIAWCd8((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Integer.valueOf(Integer.parseInt((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        Queue queue5 = (Queue) Arrays.stream(strArrSplit2[13].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda4
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$egzliAIzcIzvb_MWsIu6L8vEsas((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Integer.valueOf(Integer.parseInt((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        Queue queue6 = (Queue) Arrays.stream(strArrSplit2[14].split(str6)).filter(new Predicate() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda5
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return BadCellStats.$r8$lambda$Hb9kcdip9I6Ro7g9fCZrwL0XGFc((String) obj);
                            }
                        }).map(new Function() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                return Integer.valueOf(Integer.parseInt((String) obj));
                            }
                        }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.phone.badcells.BadCellStats$$ExternalSyntheticLambda3
                            @Override // java.util.function.Supplier
                            public final Object get() {
                                return new LinkedList();
                            }
                        }));
                        BadCellInfo badCellInfo = new BadCellInfo();
                        badCellInfo.mRat = i4;
                        badCellInfo.mTimeStamp = localDateTime;
                        badCellInfo.mTotalScore = Double.valueOf(d);
                        badCellInfo.mWeeklyBlerCounts = i5;
                        badCellInfo.mWeeklyRlfCounts = i6;
                        badCellInfo.mWeeklyNasCounts = i7;
                        badCellInfo.mWeeklySchCounts = i8;
                        badCellInfo.mWeeklyRachCounts = i9;
                        badCellInfo.mDailyScores = queue;
                        badCellInfo.mDailyRlfCountQue = queue2;
                        badCellInfo.mDailyNasCountQue = queue3;
                        badCellInfo.mDailyRachCountQue = queue4;
                        badCellInfo.mDailyBlerCountQue = queue5;
                        badCellInfo.mDailySchCountQue = queue6;
                        this.mCellListMap.put(str7, badCellInfo);
                    }
                    i3++;
                    c7 = c7;
                    c8 = c8;
                    i2 = i2;
                    strArrSplit = strArrSplit;
                    length = length;
                    str6 = str6;
                    str4 = str4;
                    str5 = str5;
                    defaultSharedPreferences = defaultSharedPreferences;
                    c6 = 6;
                }
                sharedPreferences = defaultSharedPreferences;
                c = c7;
                str = str4;
                str2 = str5;
                c2 = c8;
                i = i2;
                str3 = str6;
                c3 = 5;
                c4 = 4;
                c5 = 3;
            } else {
                sharedPreferences = defaultSharedPreferences;
                c = 2;
                str = "#";
                str2 = ";";
                c2 = 1;
                i = 0;
                str3 = ",";
                c3 = 5;
                c4 = 4;
                c5 = 3;
                logd("infoRestore(): no mCellListMap cache");
            }
            this.mNrWeights.restoreWeights("nr");
            this.mLteWeights.restoreWeights("lte");
            SharedPreferences sharedPreferences2 = sharedPreferences;
            if (sharedPreferences2.contains("mEventCountsQueue") && this.mEnbaleDynamicWeight) {
                String string2 = sharedPreferences2.getString("mEventCountsQueue", null);
                if (TextUtils.isEmpty(string2)) {
                    logd("infoRestore(): no mEventCountsQueue cache");
                    return;
                }
                String[] strArrSplit3 = string2.split(str2);
                int length2 = strArrSplit3.length;
                int i10 = i;
                while (i10 < length2) {
                    String str8 = strArrSplit3[i10];
                    ArrayList arrayList = new ArrayList();
                    String str9 = str;
                    String[] strArrSplit4 = str8.split(str9);
                    int length3 = strArrSplit4.length;
                    int i11 = i;
                    while (i11 < length3) {
                        String str10 = str3;
                        String[] strArrSplit5 = strArrSplit4[i11].split(str10);
                        if (strArrSplit5.length == 6) {
                            ErrorEventCounts errorEventCounts = new ErrorEventCounts(Integer.parseInt(strArrSplit5[i]));
                            errorEventCounts.mRlfCounts = Integer.parseInt(strArrSplit5[c2]);
                            errorEventCounts.mNasCounts = Integer.parseInt(strArrSplit5[c]);
                            errorEventCounts.mSchCounts = Integer.parseInt(strArrSplit5[c5]);
                            errorEventCounts.mBlerCounts = Integer.parseInt(strArrSplit5[c4]);
                            errorEventCounts.mRachCounts = Integer.parseInt(strArrSplit5[c3]);
                            arrayList.add(errorEventCounts);
                        } else {
                            logd("infoRestore(): Incorrect number of fields in ErrorEventCounts object");
                        }
                        i11++;
                        str3 = str10;
                    }
                    this.mEventCountsQueue.addLast(arrayList);
                    i10++;
                    str = str9;
                }
                return;
            }
            return;
        }
        logd("infoRestore: no valid info cache.");
    }

    public static /* synthetic */ boolean $r8$lambda$7X80KtdpI8f4bxQ2evcF2zxANE0(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ boolean $r8$lambda$LyPki8zzQld4NiP50EJ4Psd1tYg(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ boolean $r8$lambda$PZwEcJnIZuoWT40f5LMy4cVMccY(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ boolean $r8$lambda$LwSBbsZna4OwJQYtzXR2IIAWCd8(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ boolean $r8$lambda$egzliAIzcIzvb_MWsIu6L8vEsas(String str) {
        return !str.isEmpty();
    }

    public static /* synthetic */ boolean $r8$lambda$Hb9kcdip9I6Ro7g9fCZrwL0XGFc(String str) {
        return !str.isEmpty();
    }

    public void printBadCellList() {
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        for (Map.Entry entry : this.mCellListMap.entrySet()) {
            BadCellInfo badCellInfo = (BadCellInfo) entry.getValue();
            if (badCellInfo.mRat == 0) {
                map.put((String) entry.getKey(), badCellInfo.mTotalScore);
            }
            if (badCellInfo.mRat == 1) {
                map2.put((String) entry.getKey(), badCellInfo.mTotalScore);
            }
        }
        List<Map.Entry> list = (List) map.entrySet().stream().sorted(Map.Entry.comparingByValue().reversed()).collect(Collectors.toList());
        List<Map.Entry> list2 = (List) map2.entrySet().stream().sorted(Map.Entry.comparingByValue().reversed()).collect(Collectors.toList());
        logd("NR CELL SCORES: [gci-arfcn-pci] => score");
        for (Map.Entry entry2 : list) {
            Objects.toString(entry2.getValue());
            logd(((String) entry2.getKey()) + " => " + entry2.getValue());
        }
        logd("LTE CELL SCORES: [gci-arfcn-pci] => score");
        for (Map.Entry entry3 : list2) {
            Objects.toString(entry3.getValue());
            logd(((String) entry3.getKey()) + " => " + entry3.getValue());
        }
    }

    public void changeWeightsByCloudInfo(FiveGCloudController.FeatureCloudInfo featureCloudInfo) {
        if (featureCloudInfo == null || !featureCloudInfo.getFeatureType().equals("optimization_config_dynamic_bad_cell_event_weight")) {
            loge("invalid cloud ctrl info.");
            return;
        }
        String[] strArrSplit = featureCloudInfo.getParams2().split(",");
        if (featureCloudInfo.getParams1().split(",").length == 5 && strArrSplit.length == 5) {
            return;
        }
        loge("invalid cloud ctrl info for invalid event weights.");
    }

    public boolean hasExistBadCell(CellIdentity cellIdentity) {
        boolean zSuspectBadCell;
        String str = new String();
        if (cellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
            String globalCellId = cellIdentityNr.getGlobalCellId();
            String string = Integer.toString(cellIdentityNr.getNrarfcn());
            String string2 = Integer.toString(cellIdentityNr.getPci());
            if (globalCellId != null && string != null && string2 != null) {
                str = globalCellId + "-" + string + "-" + string2;
            }
            zSuspectBadCell = this.mTopBadCellListNR.containsKey(str) ? ((BadCellInfo) this.mTopBadCellListNR.get(str)).suspectBadCell() : false;
            if (zSuspectBadCell) {
                this.mFoundCn++;
                return zSuspectBadCell;
            }
            this.mNotFoundCn++;
            return zSuspectBadCell;
        }
        if (cellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
            String globalCellId2 = cellIdentityLte.getGlobalCellId();
            String string3 = Integer.toString(cellIdentityLte.getEarfcn());
            String string4 = Integer.toString(cellIdentityLte.getPci());
            if (globalCellId2 != null && string3 != null && string4 != null) {
                str = globalCellId2 + "-" + string3 + "-" + string4;
            }
            zSuspectBadCell = this.mTopBadCellListLTE.containsKey(str) ? ((BadCellInfo) this.mTopBadCellListLTE.get(str)).suspectBadCell() : false;
            if (zSuspectBadCell) {
                this.mFoundCn++;
                return zSuspectBadCell;
            }
            this.mNotFoundCn++;
            return zSuspectBadCell;
        }
        loge("unknown cell identity type");
        this.mNotFoundCn++;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportBadCellStat() {
        if (this.mCellListMap.isEmpty()) {
            loge("reportBadCellStat: no data to upload, mCellListMap is empty.");
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : this.mCellListMap.entrySet()) {
            String str = (String) entry.getKey();
            BadCellInfo badCellInfo = (BadCellInfo) entry.getValue();
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            if (badCellInfo.mTotalScore.doubleValue() >= 5.0d) {
                linkedHashMap2.put("BadCellId", str);
                linkedHashMap2.put("Rat", Integer.valueOf(badCellInfo.mRat));
                linkedHashMap2.put("RecordDays", Integer.valueOf(badCellInfo.mDailyScores.size()));
                linkedHashMap2.put("SuspectCell", Boolean.valueOf(badCellInfo.suspectBadCell()));
                linkedHashMap2.put("DailyScore", badCellInfo.mScore);
                linkedHashMap2.put("TotalScore", badCellInfo.mTotalScore);
                linkedHashMap2.put("DailyRlfCounts", Integer.valueOf(badCellInfo.mRlfCounts));
                linkedHashMap2.put("WeeklyRlfCounts", Integer.valueOf(badCellInfo.mWeeklyRlfCounts));
                linkedHashMap2.put("DailyNasCounts", Integer.valueOf(badCellInfo.mNasCounts));
                linkedHashMap2.put("WeeklyNasCounts", Integer.valueOf(badCellInfo.mWeeklyNasCounts));
                linkedHashMap2.put("DailySchCounts", Integer.valueOf(badCellInfo.mSchCounts));
                linkedHashMap2.put("WeeklySchCounts", Integer.valueOf(badCellInfo.mWeeklySchCounts));
                linkedHashMap2.put("DailyBlerCounts", Integer.valueOf(badCellInfo.mBlerCounts));
                linkedHashMap2.put("WeeklyBlerCounts", Integer.valueOf(badCellInfo.mWeeklyBlerCounts));
                linkedHashMap2.put("DailyRachCounts", Integer.valueOf(badCellInfo.mRachCounts));
                linkedHashMap2.put("WeeklyRachCounts", Integer.valueOf(badCellInfo.mWeeklyRachCounts));
                arrayList.add(linkedHashMap2);
            }
        }
        linkedHashMap.put("BadCellList", arrayList);
        linkedHashMap.put("NrWeights", this.mNrWeights.getWeights());
        linkedHashMap.put("LteWeights", this.mLteWeights.getWeights());
        linkedHashMap.put("foundCn", Integer.valueOf(this.mFoundCn));
        linkedHashMap.put("notFoundCn", Integer.valueOf(this.mNotFoundCn));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_badcell_statistic", linkedHashMap);
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry entry2 : linkedHashMap.entrySet()) {
                jSONObject.put((String) entry2.getKey(), entry2.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("bad_cell_stat_info", jSONObject.toString());
        logd("badcell dump info: " + jSONObject.toString());
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.ACTION_BADCELL_STATISTIC_DUMP");
        intent.putExtra("badCellDump", bundle);
        this.mContext.sendBroadcast(intent);
        this.mHandler.obtainMessage(5).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dealWithReportEnd() {
        Iterator it = this.mCellListMap.entrySet().iterator();
        while (it.hasNext()) {
            BadCellInfo badCellInfo = (BadCellInfo) ((Map.Entry) it.next()).getValue();
            badCellInfo.mScore = Double.valueOf(0.0d);
            badCellInfo.clearEventCounts();
        }
        this.mHandler.obtainMessage(6).sendToTarget();
        this.mHandler.obtainMessage(7).sendToTarget();
        this.mFoundCn = 0;
        this.mNotFoundCn = 0;
    }

    private void ensureSafeInfoRestore() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        boolean z = defaultSharedPreferences.getBoolean("mHasInvalidHistoryCacheInfo-1", true);
        this.mHasInvalidHistoryCacheInfo = z;
        if (z) {
            this.mCellListMap.clear();
            editorEdit.remove("mCellListMap");
            editorEdit.remove("mEventCountsQueue");
            editorEdit.remove("BadCellDynamicWeightnr");
            editorEdit.remove("BadCellDynamicWeightlte");
            this.mHasInvalidHistoryCacheInfo = false;
            editorEdit.putBoolean("mHasInvalidHistoryCacheInfo-1", false);
            editorEdit.apply();
            logd("invalid cache has been cleared");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("BadCellStatsModule", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Log.e("BadCellStatsModule", str);
    }
}
