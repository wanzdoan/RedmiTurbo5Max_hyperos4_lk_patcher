package com.android.phone.badcells;

import android.util.Log;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;
import com.ot.pubsub.i.a;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class BadCellInfo {
    public int mBlerCounts;
    public Queue mDailyBlerCountQue;
    public Queue mDailyNasCountQue;
    public Queue mDailyRachCountQue;
    public Queue mDailyRlfCountQue;
    public Queue mDailySchCountQue;
    public Queue mDailyScores;
    public int mNasCounts;
    public int mRachCounts;
    public int mRat;
    public int mRlfCounts;
    public int mSchCounts;
    public Double mScore;
    public LocalDateTime mTimeStamp;
    public Double mTotalScore;
    public int mWeeklyBlerCounts;
    public int mWeeklyNasCounts;
    public int mWeeklyRachCounts;
    public int mWeeklyRlfCounts;
    public int mWeeklySchCounts;

    public BadCellInfo() {
        this.mRat = 0;
        Double dValueOf = Double.valueOf(0.0d);
        this.mScore = dValueOf;
        this.mTotalScore = dValueOf;
        this.mTimeStamp = LocalDateTime.now();
        this.mDailyScores = new LinkedList();
        this.mDailyRlfCountQue = new LinkedList();
        this.mDailyNasCountQue = new LinkedList();
        this.mDailyBlerCountQue = new LinkedList();
        this.mDailyRachCountQue = new LinkedList();
        this.mDailySchCountQue = new LinkedList();
    }

    public BadCellInfo(int i) {
        this.mRat = i;
        Double dValueOf = Double.valueOf(0.0d);
        this.mScore = dValueOf;
        this.mTotalScore = dValueOf;
        this.mTimeStamp = LocalDateTime.now();
        this.mDailyScores = new LinkedList();
        this.mDailyRlfCountQue = new LinkedList();
        this.mDailyNasCountQue = new LinkedList();
        this.mDailyBlerCountQue = new LinkedList();
        this.mDailyRachCountQue = new LinkedList();
        this.mDailySchCountQue = new LinkedList();
    }

    public void recordEventCounts(int i, int i2) {
        switch (i) {
            case 402:
            case 403:
                this.mRlfCounts += i2;
                break;
            case a.c /* 404 */:
            case 405:
                this.mNasCounts += i2;
                break;
            case 406:
            case 407:
                this.mSchCounts += i2;
                break;
            case ResponseObject.RELAY_TIMEOUT_CODE /* 408 */:
            case 409:
                this.mBlerCounts += i2;
                break;
            case 410:
            case 411:
            default:
                Log.d("BadCellInfo", "Unknown event type: " + String.valueOf(i));
                break;
            case 412:
            case 413:
                this.mRachCounts += i2;
                break;
        }
    }

    public void cellScoreCalcu(ArrayList arrayList) {
        if (this.mDailyScores.size() == 7) {
            this.mTotalScore = Double.valueOf(this.mTotalScore.doubleValue() - ((Double) this.mDailyScores.poll()).doubleValue());
        }
        Double dValueOf = Double.valueOf(this.mScore.doubleValue() + (((double) this.mRlfCounts) * ((Double) arrayList.get(0)).doubleValue()) + (((double) this.mNasCounts) * ((Double) arrayList.get(1)).doubleValue()) + (((double) this.mSchCounts) * ((Double) arrayList.get(2)).doubleValue()) + (((double) this.mBlerCounts) * ((Double) arrayList.get(3)).doubleValue()) + (((double) this.mRachCounts) * ((Double) arrayList.get(4)).doubleValue()));
        this.mScore = dValueOf;
        this.mDailyScores.offer(dValueOf);
        this.mTotalScore = Double.valueOf(this.mTotalScore.doubleValue() + this.mScore.doubleValue());
    }

    public void clearEventCounts() {
        this.mRlfCounts = 0;
        this.mNasCounts = 0;
        this.mSchCounts = 0;
        this.mBlerCounts = 0;
        this.mRachCounts = 0;
    }

    public void updateWeeklyStats() {
        this.mWeeklyRlfCounts = updateQueueAndCount(this.mDailyRlfCountQue, this.mRlfCounts, this.mWeeklyRlfCounts);
        this.mWeeklyNasCounts = updateQueueAndCount(this.mDailyNasCountQue, this.mNasCounts, this.mWeeklyNasCounts);
        this.mWeeklySchCounts = updateQueueAndCount(this.mDailySchCountQue, this.mSchCounts, this.mWeeklySchCounts);
        this.mWeeklyBlerCounts = updateQueueAndCount(this.mDailyBlerCountQue, this.mBlerCounts, this.mWeeklyBlerCounts);
        this.mWeeklyRachCounts = updateQueueAndCount(this.mDailyRachCountQue, this.mRachCounts, this.mWeeklyRachCounts);
    }

    private int updateQueueAndCount(Queue queue, int i, int i2) {
        if (queue.size() >= 7) {
            i2 -= ((Integer) queue.poll()).intValue();
        }
        queue.offer(Integer.valueOf(i));
        return i2 + i;
    }

    public boolean suspectBadCell() {
        int i = this.mRat;
        if (i == 0) {
            return this.mWeeklyBlerCounts >= 200 || this.mWeeklyRlfCounts >= 7 || this.mWeeklyRachCounts >= 30 || this.mWeeklyNasCounts >= 7 || this.mWeeklySchCounts >= 700;
        }
        if (i == 1) {
            return this.mWeeklyBlerCounts >= 10 || this.mWeeklyRlfCounts >= 10 || this.mWeeklyRachCounts >= 20 || this.mWeeklyNasCounts >= 7 || this.mWeeklySchCounts >= 300;
        }
        return false;
    }
}
