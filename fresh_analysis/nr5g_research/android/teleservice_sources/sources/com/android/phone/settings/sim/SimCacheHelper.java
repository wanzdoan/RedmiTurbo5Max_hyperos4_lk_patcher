package com.android.phone.settings.sim;

import android.content.Context;
import com.android.phone.settings.sim.bean.AdBean;
import com.android.phone.settings.sim.bean.PackageBean;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SimCacheHelper {
    private static AdBean mAdCache;
    private static PackageBean mDataUsageCache;
    private static SimCacheHelper mInstance;
    private static List mPackageListCache;

    private SimCacheHelper() {
    }

    public static SimCacheHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SimCacheHelper();
        }
        return mInstance;
    }

    public List getSimInfo(Context context) {
        List list = mPackageListCache;
        if (list != null) {
            return list;
        }
        List list2 = Utils$SimApi.getPackage(context);
        mPackageListCache = list2;
        return list2;
    }

    public AdBean getAdInfo(Context context) throws Throwable {
        AdBean adBean = mAdCache;
        if (adBean != null) {
            return adBean;
        }
        AdBean ad = Utils$SimApi.getAd(context);
        mAdCache = ad;
        return ad;
    }

    public PackageBean getDataUsageInfo(Context context) throws Throwable {
        PackageBean packageBean = mDataUsageCache;
        if (packageBean != null) {
            return packageBean;
        }
        PackageBean dataUsage = Utils$SimApi.getDataUsage(context);
        mDataUsageCache = dataUsage;
        return dataUsage;
    }

    public void clear() {
        mPackageListCache = null;
        mAdCache = null;
        mDataUsageCache = null;
    }
}
