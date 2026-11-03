package com.android.phone.settings.sim;

import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.android.phone.settings.sim.bean.AdBean;
import com.android.phone.settings.sim.bean.PackageBean;
import com.android.services.telephony.relay.network.NetworkRelayServerController;
import com.ot.pubsub.a.a;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class Utils$SimApi {
    private static final Uri URI_PACKAGE = Uri.parse("content://com.miui.networkassistant.provider/datausage_status_detailed");
    private static final Uri URI_VIRTUAL_SIM = Uri.parse("content://com.miui.virtualsim.provider.virtualsimInfo");

    /* JADX WARN: Type inference failed for: r10v5, types: [com.android.phone.settings.sim.Utils$Parse] */
    public static List getPackage(Context context) {
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                final Cursor cursorQuery = context.getContentResolver().query(URI_PACKAGE, null, null, null, null);
                if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                    Log.i("SimApi", "getPackage failed, cursor is empty.");
                } else {
                    ?? r10 = new Object(cursorQuery) { // from class: com.android.phone.settings.sim.Utils$Parse
                        private Cursor mCursor;

                        {
                            if (cursorQuery == null) {
                                Folme$$ExternalSyntheticBUOutline0.m("Cursor must not be null.");
                                throw null;
                            }
                            this.mCursor = cursorQuery;
                        }

                        public String getString(String str) {
                            Cursor cursor2 = this.mCursor;
                            return cursor2.getString(cursor2.getColumnIndex(str));
                        }

                        public int getInt(String str) {
                            Cursor cursor2 = this.mCursor;
                            return cursor2.getInt(cursor2.getColumnIndex(str));
                        }
                    };
                    do {
                        PackageBean packageBean = new PackageBean();
                        packageBean.mSlotId = r10.getInt(NetworkRelayServerController.SIM_SLOT);
                        packageBean.mTrafficName = r10.getString("traffic_name");
                        packageBean.mTrafficValue = r10.getString(NetworkRelayServerController.TRAFFIC_VALUE);
                        packageBean.mTrafficUnit = r10.getString(NetworkRelayServerController.TRAFFIC_UNIT);
                        packageBean.mBillName = r10.getString("bill_name");
                        packageBean.mBillValue = r10.getString("bill_value");
                        packageBean.mBillUnit = r10.getString("bill_unit");
                        packageBean.mPackageType = r10.getInt("package_type");
                        packageBean.mClickAction = r10.getString("click_action");
                        Log.i("SimApi", "getPackage: slotId=" + packageBean.mSlotId + ", trafficName=" + packageBean.mTrafficName + ", trafficValue=" + packageBean.mTrafficValue + ", trafficUnit=" + packageBean.mTrafficUnit + ", billName=" + packageBean.mBillName + ", billValue=" + packageBean.mBillValue + ", billUnit=" + packageBean.mBillUnit + ", packageType=" + packageBean.mPackageType + ", clickAction=" + packageBean.mClickAction);
                        arrayList.add(packageBean);
                    } while (cursorQuery.moveToNext());
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                    return arrayList;
                }
            } catch (Exception e) {
                Log.e("SimApi", "Failed to get package.", e);
                if (0 != 0) {
                    cursor.close();
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
                throw th;
            }
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x006f  */
    /* JADX WARN: Multi-variable type inference failed */
    public static AdBean getAd(Context context) throws Throwable {
        ContentProviderClient contentProviderClientAcquireUnstableContentProviderClient;
        Bundle bundleCall;
        ContentProviderClient contentProviderClient = 0;
        try {
            try {
                contentProviderClientAcquireUnstableContentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient(URI_VIRTUAL_SIM);
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    try {
                        bundleCall = contentProviderClientAcquireUnstableContentProviderClient.call("getMisimPromoteMessage", null, null);
                    } catch (Exception e) {
                        e = e;
                        Log.e("SimApi", "Failed to get ad.", e);
                        if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                            contentProviderClientAcquireUnstableContentProviderClient.close();
                        }
                        bundleCall = null;
                    }
                } else {
                    bundleCall = null;
                }
                if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                    contentProviderClientAcquireUnstableContentProviderClient.close();
                }
            } catch (Throwable th) {
                th = th;
                contentProviderClient = context;
                if (contentProviderClient != 0) {
                    contentProviderClient.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            contentProviderClientAcquireUnstableContentProviderClient = null;
        } catch (Throwable th2) {
            th = th2;
            if (contentProviderClient != 0) {
                contentProviderClient.close();
            }
            throw th;
        }
        if (bundleCall == null) {
            Log.i("SimApi", "getAd failed, bundle is null.");
            return null;
        }
        AdBean adBean = new AdBean();
        adBean.mMessage = bundleCall.getString(a.m);
        adBean.mIntentUri = bundleCall.getString("intentUri");
        Log.i("SimApi", "getAd: message=" + adBean.mMessage + ", uri=" + adBean.mIntentUri);
        return adBean;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0097  */
    public static PackageBean getDataUsage(Context context) throws Throwable {
        ContentProviderClient contentProviderClientAcquireUnstableContentProviderClient;
        Bundle bundleCall;
        ContentProviderClient contentProviderClient = null;
        try {
            contentProviderClientAcquireUnstableContentProviderClient = context.getContentResolver().acquireUnstableContentProviderClient(URI_VIRTUAL_SIM);
            if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                try {
                    try {
                        bundleCall = contentProviderClientAcquireUnstableContentProviderClient.call("getMultisimOrderInfo", null, null);
                    } catch (Exception e) {
                        e = e;
                        Log.e("SimApi", "Failed to get data usage.", e);
                        if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                            contentProviderClientAcquireUnstableContentProviderClient.close();
                        }
                        bundleCall = null;
                    }
                } catch (Throwable th) {
                    th = th;
                    contentProviderClient = contentProviderClientAcquireUnstableContentProviderClient;
                    if (contentProviderClient != null) {
                        contentProviderClient.close();
                    }
                    throw th;
                }
            } else {
                bundleCall = null;
            }
            if (contentProviderClientAcquireUnstableContentProviderClient != null) {
                contentProviderClientAcquireUnstableContentProviderClient.close();
            }
        } catch (Exception e2) {
            e = e2;
            contentProviderClientAcquireUnstableContentProviderClient = null;
        } catch (Throwable th2) {
            th = th2;
            if (contentProviderClient != null) {
                contentProviderClient.close();
            }
            throw th;
        }
        if (bundleCall == null) {
            Log.i("SimApi", "getDataUsage failed, bundle is null.");
            return null;
        }
        PackageBean packageBean = new PackageBean();
        packageBean.mTrafficName = bundleCall.getString("multisim_message");
        packageBean.mTrafficValue = bundleCall.getString("multisim_usage");
        packageBean.mTrafficUnit = bundleCall.getString("multisim_unit");
        packageBean.mClickAction = bundleCall.getString("multisim_action");
        Log.i("SimApi", "getDataUsage: multisim_message=" + packageBean.mTrafficName + ", multisim_usage=" + packageBean.mTrafficValue + ", multisim_unit=" + packageBean.mTrafficUnit + ", multisim_action=" + packageBean.mClickAction);
        return packageBean;
    }
}
