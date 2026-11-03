package com.android.phone.signalMap;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class MiuiCellDetectionStrategy extends AbstractCellDetectionStrategy {
    public static boolean DEBUG = false;
    private static MiuiCellDetectionStrategy sInstance;
    L2CacheCellDetectionStrategy mL2CacheCellDetectionStrategy;
    private SignalMapCloudController mSignalMapCloudController;
    PingPongCellDetectionStrategy mPingPongCellDetectionStrategy = new PingPongCellDetectionStrategy();
    L1CacheCellDetectionStrategy mL1CacheCellDetectionStrategy = new L1CacheCellDetectionStrategy();

    public static synchronized MiuiCellDetectionStrategy getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiuiCellDetectionStrategy();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public MiuiCellDetectionStrategy() {
        L2CacheCellDetectionStrategy l2CacheCellDetectionStrategy = new L2CacheCellDetectionStrategy(new MiuiLru(10));
        this.mL2CacheCellDetectionStrategy = l2CacheCellDetectionStrategy;
        PingPongCellDetectionStrategy pingPongCellDetectionStrategy = this.mPingPongCellDetectionStrategy;
        this.nextStrategy = pingPongCellDetectionStrategy;
        pingPongCellDetectionStrategy.nextStrategy = l2CacheCellDetectionStrategy;
        l2CacheCellDetectionStrategy.nextStrategy = this.mL1CacheCellDetectionStrategy;
    }

    @Override // com.android.phone.signalMap.AbstractCellDetectionStrategy
    protected boolean isBadCell(String str) {
        return this.nextStrategy.isBadCell(str);
    }

    public class PingPongCellDetectionStrategy extends AbstractCellDetectionStrategy {
        private final LinkedList mPingPongCellList = new LinkedList();

        public PingPongCellDetectionStrategy() {
        }

        @Override // com.android.phone.signalMap.AbstractCellDetectionStrategy
        protected boolean isBadCell(String str) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            clearExpiredCell(jElapsedRealtime);
            PingPongCellInfo pingPongCellInfo = new PingPongCellInfo(this, jElapsedRealtime, str);
            if (this.mPingPongCellList.contains(pingPongCellInfo)) {
                MiuiCellDetectionStrategy.this.logD("pingpongCellDetection cellInfo = " + pingPongCellInfo.CELL_INFO);
                return false;
            }
            boolean zIsBadCell = this.nextStrategy.isBadCell(str);
            if (zIsBadCell) {
                cachePingPongCell(pingPongCellInfo);
            }
            return zIsBadCell;
        }

        private void cachePingPongCell(PingPongCellInfo pingPongCellInfo) {
            this.mPingPongCellList.addLast(pingPongCellInfo);
            if (this.mPingPongCellList.size() > 10) {
                this.mPingPongCellList.removeFirst();
            }
        }

        private void clearExpiredCell(long j) {
            while (!this.mPingPongCellList.isEmpty()) {
                PingPongCellInfo pingPongCellInfo = (PingPongCellInfo) this.mPingPongCellList.peekFirst();
                if (pingPongCellInfo == null) {
                    this.mPingPongCellList.removeFirst();
                } else if (j - pingPongCellInfo.TIME <= 1200000) {
                    return;
                } else {
                    this.mPingPongCellList.removeFirst();
                }
            }
        }

        class PingPongCellInfo {
            private final String CELL_INFO;
            private final long TIME;

            public PingPongCellInfo(PingPongCellDetectionStrategy pingPongCellDetectionStrategy, long j, String str) {
                this.TIME = j;
                this.CELL_INFO = str;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                return Objects.equals(this.CELL_INFO, ((PingPongCellInfo) obj).CELL_INFO);
            }

            public int hashCode() {
                return Objects.hash(this.CELL_INFO);
            }
        }
    }

    public class L2CacheCellDetectionStrategy extends AbstractCellDetectionStrategy {
        private MiuiLru mCellL2Cache;

        public L2CacheCellDetectionStrategy(MiuiLru miuiLru) {
            this.mCellL2Cache = miuiLru;
        }

        @Override // com.android.phone.signalMap.AbstractCellDetectionStrategy
        protected boolean isBadCell(String str) {
            if (this.mCellL2Cache.containsKey(str)) {
                boolean zBooleanValue = ((Boolean) this.mCellL2Cache.getOrDefault(str, Boolean.FALSE)).booleanValue();
                MiuiCellDetectionStrategy.this.logD("hit L2Cache cellInfo = " + str + ", result = " + zBooleanValue);
                return zBooleanValue;
            }
            boolean zIsBadCell = this.nextStrategy.isBadCell(str);
            this.mCellL2Cache.put(str, Boolean.valueOf(zIsBadCell));
            return zIsBadCell;
        }
    }

    public class L1CacheCellDetectionStrategy extends AbstractCellDetectionStrategy {
        private Set mBlackCellList = new HashSet();

        public L1CacheCellDetectionStrategy() {
        }

        @Override // com.android.phone.signalMap.AbstractCellDetectionStrategy
        protected synchronized boolean isBadCell(String str) {
            boolean zContains;
            try {
                if (this.mBlackCellList.size() == 0) {
                    updateBlackCellListFromDb();
                }
                zContains = this.mBlackCellList.contains(str);
                MiuiCellDetectionStrategy.this.logD("l1cache size = " + this.mBlackCellList.size() + ", detection cell result is " + zContains);
            } catch (Throwable th) {
                throw th;
            }
            return zContains;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void updateBlackCellListFromDb() {
            MiuiCellDetectionStrategy.this.mSignalMapCloudController = SignalMapCloudController.getInstance();
            String city = MiuiCellDetectionStrategy.this.mSignalMapCloudController.getCity();
            boolean zIsEmpty = TextUtils.isEmpty(city);
            MiuiCellDetectionStrategy miuiCellDetectionStrategy = MiuiCellDetectionStrategy.this;
            if (zIsEmpty) {
                miuiCellDetectionStrategy.logD("city is empty!");
                return;
            }
            Set signalMapCellBlackList = miuiCellDetectionStrategy.mSignalMapCloudController.getSignalMapCellBlackList(city);
            if (signalMapCellBlackList != null && !signalMapCellBlackList.isEmpty()) {
                MiuiCellDetectionStrategy.this.logD("blackCellList = " + signalMapCellBlackList);
                this.mBlackCellList.clear();
                this.mBlackCellList.addAll(signalMapCellBlackList);
            }
        }
    }

    public void updateBlackCellList() {
        this.mL1CacheCellDetectionStrategy.updateBlackCellListFromDb();
    }

    class MiuiLru extends LinkedHashMap {
        private final int mCapacity;

        public MiuiLru(int i) {
            super(i, 0.75f, true);
            this.mCapacity = i;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry entry) {
            return size() > this.mCapacity;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (DEBUG) {
            Log.d("CellDetectionStrategy", str);
        }
    }
}
