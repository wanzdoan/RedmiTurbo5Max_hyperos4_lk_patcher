package com.android.phone.freqopt.debug;

import android.content.Intent;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.core.IFreqOptStrategy;
import com.android.phone.freqopt.data.FreqOptDBManager;
import com.android.phone.freqopt.data.FreqOptLearningRecord;
import com.android.phone.freqopt.strategy.NrLteStrategy;
import com.android.phone.freqopt.util.LogUtils;
import com.ot.pubsub.util.v;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class DbDebugHelper {
    private final FreqOptController mController;

    DbDebugHelper(FreqOptController freqOptController) {
        this.mController = freqOptController;
    }

    boolean dispatch(String str, Intent intent) {
        str.hashCode();
        switch (str) {
            case "db_delete":
                handleDbDelete(intent);
                return true;
            case "db_expire":
                handleDbExpire(intent);
                return true;
            case "db_insert":
                handleDbInsert(intent);
                return true;
            case "db_delete_source":
                handleDbDeleteSource(intent);
                return true;
            case "db_query":
                handleDbQuery(intent);
                return true;
            case "db_reset":
                handleDbReset(intent);
                return true;
            default:
                return false;
        }
    }

    private void handleDbQuery(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        if (stringExtra == null) {
            stringExtra = "fdd_tdd";
        }
        FreqOptDBManager dbManager = this.mController.getDbManager();
        long ttlForStrategy = getTtlForStrategy(stringExtra);
        List listQueryAllByStrategy = dbManager.queryAllByStrategy(stringExtra, ttlForStrategy);
        LogUtils.i("Freq_Opt_Db_Debug", "db_query: strategy=" + stringExtra + ", ttlMs=" + ttlForStrategy + ", total=" + listQueryAllByStrategy.size());
        for (int i = 0; i < listQueryAllByStrategy.size(); i++) {
            LogUtils.i("Freq_Opt_Db_Debug", "db_query [" + i + "]: " + listQueryAllByStrategy.get(i));
        }
    }

    private void handleDbInsert(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra("source_cell");
        String stringExtra3 = intent.getStringExtra("target_cell");
        int intExtra = intent.getIntExtra("count", 1);
        int intExtra2 = intent.getIntExtra("connected_count", 0);
        String stringExtra4 = intent.getStringExtra("ext_data");
        if (stringExtra == null || stringExtra2 == null || stringExtra3 == null) {
            LogUtils.w("Freq_Opt_Db_Debug", "db_insert: missing required params");
            return;
        }
        FreqOptDBManager dbManager = this.mController.getDbManager();
        FreqOptLearningRecord freqOptLearningRecordQueryRecord = dbManager.queryRecord(stringExtra, stringExtra2, stringExtra3);
        if (freqOptLearningRecordQueryRecord != null) {
            dbManager.deleteRecord(stringExtra, freqOptLearningRecordQueryRecord.getId());
        }
        FreqOptLearningRecord freqOptLearningRecord = new FreqOptLearningRecord(stringExtra, stringExtra2, stringExtra3);
        freqOptLearningRecord.setCount(intExtra);
        freqOptLearningRecord.setConnectedCount(intExtra2);
        freqOptLearningRecord.setExtData(stringExtra4);
        LogUtils.d("Freq_Opt_Db_Debug", "db_insert: id=" + dbManager.insertOrUpdate(freqOptLearningRecord, getTtlForStrategy(stringExtra), false) + ", strategy=" + stringExtra + ", count=" + intExtra + ", connectedCount=" + intExtra2);
    }

    private void handleDbDelete(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        long longExtra = intent.getLongExtra("record_id", -1L);
        if (stringExtra == null || longExtra < 0) {
            LogUtils.w("Freq_Opt_Db_Debug", "db_delete: missing strategy or record_id");
            return;
        }
        this.mController.getDbManager().deleteRecord(stringExtra, longExtra);
        LogUtils.d("Freq_Opt_Db_Debug", "db_delete: strategy=" + stringExtra + ", recordId=" + longExtra);
    }

    private void handleDbDeleteSource(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra("source_cell");
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_Db_Debug", "db_delete_source: missing strategy or source_cell");
            return;
        }
        this.mController.getDbManager().deleteBySourceCell(stringExtra, stringExtra2);
        LogUtils.d("Freq_Opt_Db_Debug", "db_delete_source: strategy=" + stringExtra + ", source=" + stringExtra2);
    }

    private void handleDbReset(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        if (stringExtra == null) {
            LogUtils.w("Freq_Opt_Db_Debug", "db_reset: missing strategy");
            return;
        }
        FreqOptDBManager dbManager = this.mController.getDbManager();
        List listQueryAllByStrategy = dbManager.queryAllByStrategy(stringExtra, getTtlForStrategy(stringExtra));
        Iterator it = listQueryAllByStrategy.iterator();
        while (it.hasNext()) {
            dbManager.deleteRecord(stringExtra, ((FreqOptLearningRecord) it.next()).getId());
        }
        LogUtils.d("Freq_Opt_Db_Debug", "db_reset: strategy=" + stringExtra + ", deleted=" + listQueryAllByStrategy.size());
    }

    private void handleDbExpire(Intent intent) {
        String stringExtra = intent.getStringExtra("strategy");
        String stringExtra2 = intent.getStringExtra("source_cell");
        int intExtra = intent.getIntExtra("days", 121);
        if (stringExtra == null || stringExtra2 == null) {
            LogUtils.w("Freq_Opt_Db_Debug", "db_expire: missing strategy or source_cell");
            return;
        }
        LogUtils.i("Freq_Opt_Db_Debug", "db_expire: strategy=" + stringExtra + ", source=" + stringExtra2 + ", days=" + intExtra + ", rows=" + this.mController.getDbManager().updateRefreshTime(stringExtra, stringExtra2, System.currentTimeMillis() - (((long) intExtra) * v.b)));
    }

    long getTtlForStrategy(String str) {
        IFreqOptStrategy iFreqOptStrategy = (IFreqOptStrategy) this.mController.getStrategies().get(str);
        if (iFreqOptStrategy != null) {
            return iFreqOptStrategy.getTtlMs();
        }
        NrLteStrategy nrLteStrategy = this.mController.getNrLteStrategy();
        if (nrLteStrategy != null) {
            return nrLteStrategy.getTtlMs();
        }
        return 10368000000L;
    }
}
