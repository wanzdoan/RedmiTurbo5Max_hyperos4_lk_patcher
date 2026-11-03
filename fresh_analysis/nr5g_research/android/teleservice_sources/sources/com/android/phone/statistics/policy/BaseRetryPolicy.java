package com.android.phone.statistics.policy;

import com.android.phone.statistics.utils.Utils;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class BaseRetryPolicy {
    private ArrayList mRetryArray = new ArrayList();
    private int mRetryCount = 0;

    public BaseRetryPolicy() {
        configureRetry("10000,20000,40000,80000,150000,300000,600000,1200000,1800000,3600000");
    }

    private void configureRetry(String str) {
        for (String str2 : str.split(",")) {
            try {
                this.mRetryArray.add(Integer.valueOf(Integer.parseInt(str2)));
            } catch (NumberFormatException e) {
                Utils.logE("BaseRetryPolicy", "configureDataBlockedRetryTimer exception:" + e);
                this.mRetryArray.clear();
                return;
            }
        }
    }

    public void resetAll() {
        this.mRetryCount = 0;
        this.mRetryArray.clear();
    }
}
