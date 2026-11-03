package com.android.phone.freqopt.data;

import android.database.Cursor;
import com.android.phone.freqopt.util.LogUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FreqOptLearningRecord {
    private long mActionTime;
    private int mConnectedCount;
    private int mCount;
    private long mCreatedTime;
    private String mExtData;
    private long mId;
    private long mRefreshTime;
    private String mSourceCellId;
    private int mState;
    private String mStrategyId;
    private String mTargetCellId;

    public FreqOptLearningRecord(String str, String str2, String str3) {
        this.mStrategyId = str;
        this.mSourceCellId = str2;
        this.mTargetCellId = str3;
        this.mCount = 1;
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.mRefreshTime = jCurrentTimeMillis;
        this.mActionTime = 0L;
        this.mState = 0;
        this.mCreatedTime = jCurrentTimeMillis;
    }

    public FreqOptLearningRecord(Cursor cursor) {
        int columnIndexOrThrow = cursor.getColumnIndexOrThrow("id");
        int columnIndexOrThrow2 = cursor.getColumnIndexOrThrow("source_cell_id");
        int columnIndexOrThrow3 = cursor.getColumnIndexOrThrow("target_cell_id");
        int columnIndexOrThrow4 = cursor.getColumnIndexOrThrow("count");
        int columnIndexOrThrow5 = cursor.getColumnIndexOrThrow("refresh_time");
        int columnIndexOrThrow6 = cursor.getColumnIndexOrThrow("action_time");
        int columnIndexOrThrow7 = cursor.getColumnIndexOrThrow("ext_data");
        int columnIndexOrThrow8 = cursor.getColumnIndexOrThrow(MEModuleSignalT1s.EXTRA_STATE);
        int columnIndexOrThrow9 = cursor.getColumnIndexOrThrow("created_time");
        int columnIndexOrThrow10 = cursor.getColumnIndexOrThrow("connected_count");
        this.mId = cursor.getLong(columnIndexOrThrow);
        this.mSourceCellId = cursor.getString(columnIndexOrThrow2);
        this.mTargetCellId = cursor.getString(columnIndexOrThrow3);
        this.mCount = cursor.getInt(columnIndexOrThrow4);
        this.mRefreshTime = cursor.getLong(columnIndexOrThrow5);
        this.mActionTime = cursor.getLong(columnIndexOrThrow6);
        this.mExtData = cursor.getString(columnIndexOrThrow7);
        this.mState = cursor.getInt(columnIndexOrThrow8);
        this.mCreatedTime = cursor.getLong(columnIndexOrThrow9);
        this.mConnectedCount = cursor.getInt(columnIndexOrThrow10);
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long j) {
        this.mId = j;
    }

    public String getStrategyId() {
        return this.mStrategyId;
    }

    public void setStrategyId(String str) {
        this.mStrategyId = str;
    }

    public String getSourceCellId() {
        return this.mSourceCellId;
    }

    public String getTargetCellId() {
        return this.mTargetCellId;
    }

    public int getCount() {
        return this.mCount;
    }

    public void setCount(int i) {
        this.mCount = i;
    }

    public int getConnectedCount() {
        return this.mConnectedCount;
    }

    public void setConnectedCount(int i) {
        this.mConnectedCount = i;
    }

    public long getRefreshTime() {
        return this.mRefreshTime;
    }

    public long getActionTime() {
        return this.mActionTime;
    }

    public String getExtData() {
        return this.mExtData;
    }

    public void setExtData(String str) {
        this.mExtData = str;
    }

    public int getState() {
        return this.mState;
    }

    public long getCreatedTime() {
        return this.mCreatedTime;
    }

    public void setExtFields(Map map) {
        if (map == null || map.isEmpty()) {
            this.mExtData = null;
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry entry : map.entrySet()) {
                jSONObject.put((String) entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            LogUtils.w("Freq_Opt_LearningRecord", "setExtFields JSON error", e);
        }
        this.mExtData = jSONObject.toString();
    }

    public Map getExtFields() {
        HashMap map = new HashMap();
        String str = this.mExtData;
        if (str != null && !str.isEmpty()) {
            try {
                JSONObject jSONObject = new JSONObject(this.mExtData);
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, jSONObject.get(next));
                }
            } catch (JSONException e) {
                LogUtils.w("Freq_Opt_LearningRecord", "getExtFields JSON error", e);
            }
        }
        return map;
    }

    public int getCountForState(int i) {
        return i == 0 ? this.mConnectedCount : this.mCount;
    }

    private static String formatTime(long j) {
        if (j <= 0) {
            return "0";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(j));
    }

    public String toString() {
        return "FreqOptLearningRecord{id=" + this.mId + ", strategyId=" + this.mStrategyId + ", source=" + this.mSourceCellId + ", target=" + this.mTargetCellId + ", count=" + this.mCount + ", connectedCount=" + this.mConnectedCount + ", refreshTime=" + formatTime(this.mRefreshTime) + ", actionTime=" + formatTime(this.mActionTime) + ", state=" + this.mState + ", extData=" + this.mExtData + "}";
    }
}
