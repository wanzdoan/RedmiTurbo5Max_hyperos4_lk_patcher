package com.android.phone.badcells;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class BadCellDynamicWeight {
    public double mBlerWeight;
    private Context mContext;
    public double mNasWeight;
    public double mRachWeight;
    public int mRat;
    public double mRlfWeight;
    public double mSchWeight;

    public BadCellDynamicWeight(int i, Context context) {
        this.mContext = context;
        if (i == 0) {
            this.mRat = i;
            this.mRlfWeight = 0.11928d;
            this.mNasWeight = 0.79392d;
            this.mSchWeight = 0.00379d;
            this.mBlerWeight = 0.01198d;
            this.mRachWeight = 0.07132d;
            return;
        }
        if (i == 1) {
            this.mRat = i;
            this.mRlfWeight = 0.1186d;
            this.mNasWeight = 0.1998d;
            this.mSchWeight = 0.0024d;
            this.mBlerWeight = 0.6334d;
            this.mRachWeight = 0.0458d;
            return;
        }
        Log.e("BadCellDynamicWeight", "wrong rat type");
    }

    public ArrayList getWeights() {
        ArrayList arrayList = new ArrayList(5);
        for (int i = 0; i < 5; i++) {
            arrayList.add(Double.valueOf(0.0d));
        }
        arrayList.set(0, Double.valueOf(this.mRlfWeight));
        arrayList.set(1, Double.valueOf(this.mNasWeight));
        arrayList.set(2, Double.valueOf(this.mSchWeight));
        arrayList.set(3, Double.valueOf(this.mBlerWeight));
        arrayList.set(4, Double.valueOf(this.mRachWeight));
        return arrayList;
    }

    public void eventStatsAndWeightUpdate(Deque deque) {
        List listAsList;
        Iterator it = deque.iterator();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (it.hasNext()) {
            ArrayList arrayList = (ArrayList) it.next();
            int size = arrayList.size();
            int i7 = 0;
            while (i7 < size) {
                Object obj = arrayList.get(i7);
                i7++;
                ErrorEventCounts errorEventCounts = (ErrorEventCounts) obj;
                if (errorEventCounts.mRat == this.mRat) {
                    i2 += errorEventCounts.mRlfCounts;
                    i3 += errorEventCounts.mNasCounts;
                    i4 += errorEventCounts.mSchCounts;
                    i5 += errorEventCounts.mBlerCounts;
                    i6 += errorEventCounts.mRachCounts;
                    i += errorEventCounts.total();
                }
            }
        }
        int i8 = this.mRat;
        if (i > 0) {
            if (i8 == 0) {
                listAsList = Arrays.asList(Double.valueOf(0.11928d), Double.valueOf(0.79392d), Double.valueOf(0.00379d), Double.valueOf(0.01198d), Double.valueOf(0.07132d));
            } else {
                if (i8 != 1) {
                    Log.e("BadCellDynamicWeight", "invalid rat type");
                    return;
                }
                listAsList = Arrays.asList(Double.valueOf(0.1186d), Double.valueOf(0.1998d), Double.valueOf(0.0024d), Double.valueOf(0.6334d), Double.valueOf(0.0458d));
            }
            applyNewWeights(calculateWeight(listAsList, Arrays.asList(Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)), i));
            return;
        }
        Log.d("BadCellDynamicWeight", "no valid eventTotalCounts, rat" + i8 + " dynamic weight pass");
    }

    private List calculateWeight(List list, List list2, int i) {
        if (list == null || list2 == null || list.size() != list2.size() || i <= 0) {
            Folme$$ExternalSyntheticBUOutline0.m("Invalid arguments!");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list2.iterator();
        double d = 0.0d;
        while (it.hasNext()) {
            double dIntValue = ((double) ((Integer) it.next()).intValue()) / ((double) i);
            if (dIntValue <= 0.001d) {
                arrayList.add(Double.valueOf(0.0d));
            } else {
                double d2 = 1.0d / dIntValue;
                arrayList.add(Double.valueOf(d2));
                d += d2;
            }
        }
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            arrayList2.add(Double.valueOf(((Double) obj).doubleValue() / d));
        }
        ArrayList arrayList3 = new ArrayList();
        for (int i3 = 0; i3 < list.size(); i3++) {
            arrayList3.add(Double.valueOf((((Double) list.get(i3)).doubleValue() * 0.9d) + (((Double) arrayList2.get(i3)).doubleValue() * 0.1d)));
        }
        return arrayList3;
    }

    private void applyNewWeights(List list) {
        if (list.size() != 5) {
            Log.e("BadCellDynamicWeight", "invalid newWeights");
            return;
        }
        this.mRlfWeight = ((Double) list.get(0)).doubleValue();
        this.mNasWeight = ((Double) list.get(1)).doubleValue();
        this.mSchWeight = ((Double) list.get(2)).doubleValue();
        this.mBlerWeight = ((Double) list.get(3)).doubleValue();
        this.mRachWeight = ((Double) list.get(4)).doubleValue();
    }

    public void cacheWeights(String str) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString("BadCellDynamicWeight" + str, String.join(",", String.valueOf(this.mRat), String.valueOf(this.mRlfWeight), String.valueOf(this.mNasWeight), String.valueOf(this.mSchWeight), String.valueOf(this.mBlerWeight), String.valueOf(this.mRachWeight)));
    }

    public void restoreWeights(String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!defaultSharedPreferences.contains("BadCellDynamicWeight" + str)) {
            Log.d("BadCellDynamicWeight", "BadCellDynamicWeight " + str + " not found");
            return;
        }
        String string = defaultSharedPreferences.getString("BadCellDynamicWeight" + str, "");
        if (TextUtils.isEmpty(string)) {
            Log.d("BadCellDynamicWeight", "BadCellDynamicWeight " + str + " is null");
            return;
        }
        String[] strArrSplit = string.split(",");
        if (strArrSplit.length != 6) {
            Log.d("BadCellDynamicWeight", "BadCellDynamicWeight " + str + " weights.length != MEMBER_NUM");
            return;
        }
        this.mRat = Integer.valueOf(strArrSplit[0]).intValue();
        this.mRlfWeight = Double.parseDouble(strArrSplit[1]);
        this.mNasWeight = Double.parseDouble(strArrSplit[2]);
        this.mSchWeight = Double.parseDouble(strArrSplit[3]);
        this.mBlerWeight = Double.parseDouble(strArrSplit[4]);
        this.mRachWeight = Double.parseDouble(strArrSplit[5]);
    }
}
