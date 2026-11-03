package com.android.internal.telephony.ntndata;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.MiuiDataServiceManagerStub;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDataServiceManagerImpl implements MiuiDataServiceManagerStub.IMiuiDataServiceManager {
    private static final String LOG_TAG = "MiuiDataServiceManagerImpl";
    private static final String SATELLITE_DATA_ENABLED = "satellite_data_enabled";
    private static final int SATELLITE_DATA_OFF = 0;
    private static final int SATELLITE_DATA_ON = 1;
    public static final String SATELLITE_DATA_SERVICE_PACKAGE = "com.xiaomi.phone";
    public static final String SERVICE_INTERFACE = "android.telephony.ntndata.SatelliteService";
    private Context mContext;
    private DataServiceManager mDataServiceManager;
    private Phone mPhone;
    private boolean mPreSatelliteEnable = false;
    private volatile boolean mSatelliteDataEnable = false;
    private boolean mPreSatelliteDataEnable = false;
    private ContentObserver mSatelliteObserver = new ContentObserver(new Handler()) { // from class: com.android.internal.telephony.ntndata.MiuiDataServiceManagerImpl.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            boolean satelliteEnable = MiuiDataServiceManagerImpl.this.getSatelliteEnable();
            MiuiDataServiceManagerImpl.this.log("satelliteEnable = " + satelliteEnable + ", mPreSatelliteEnable = " + MiuiDataServiceManagerImpl.this.mPreSatelliteEnable + ", PhoneId = " + MiuiDataServiceManagerImpl.this.mPhone.getPhoneId());
            if (!satelliteEnable && MiuiDataServiceManagerImpl.this.mPreSatelliteEnable && MiuiDataServiceManagerImpl.this.mDataServiceManager != null) {
                MiuiDataServiceManagerImpl.this.mDataServiceManager.sendMessage(MiuiDataServiceManagerImpl.this.mDataServiceManager.obtainMessage(1));
            }
            MiuiDataServiceManagerImpl.this.mPreSatelliteEnable = satelliteEnable;
        }
    };
    private ContentObserver mSatelliteDataObserver = new ContentObserver(new Handler()) { // from class: com.android.internal.telephony.ntndata.MiuiDataServiceManagerImpl.2
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            MiuiDataServiceManagerImpl.this.mSatelliteDataEnable = MiuiDataServiceManagerImpl.this.getSatelliteDataEnabled();
            MiuiDataServiceManagerImpl.this.log("mSatelliteDataEnable = " + MiuiDataServiceManagerImpl.this.mSatelliteDataEnable + ", mPreSatelliteDataEnable = " + MiuiDataServiceManagerImpl.this.mPreSatelliteDataEnable + ", PhoneId = " + MiuiDataServiceManagerImpl.this.mPhone.getPhoneId());
            if (MiuiDataServiceManagerImpl.this.mSatelliteDataEnable && !MiuiDataServiceManagerImpl.this.mPreSatelliteDataEnable && MiuiDataServiceManagerImpl.this.mDataServiceManager != null) {
                MiuiDataServiceManagerImpl.this.mDataServiceManager.sendMessage(MiuiDataServiceManagerImpl.this.mDataServiceManager.obtainMessage(1));
            }
            MiuiDataServiceManagerImpl.this.mPreSatelliteDataEnable = MiuiDataServiceManagerImpl.this.mSatelliteDataEnable;
        }
    };

    public void init(Phone phone, Object... arg) {
        this.mPhone = phone;
        this.mContext = this.mPhone.getContext();
        if (arg.length >= 1 && (arg[0] instanceof DataServiceManager)) {
            this.mDataServiceManager = (DataServiceManager) arg[0];
        }
        registerAllEvents();
    }

    public Intent getDataServiceIntent(int transportType, Intent intent, String className, String packageName) {
        Intent intent2;
        if (this.mSatelliteDataEnable && transportType == 1) {
            if (TextUtils.isEmpty(className)) {
                intent2 = new Intent(SERVICE_INTERFACE);
                intent2.setPackage(packageName);
            } else {
                ComponentName cm = new ComponentName(packageName, className);
                Intent intent3 = new Intent(SERVICE_INTERFACE).setComponent(cm);
                intent2 = intent3;
            }
            log("getDataServiceIntent intent = " + intent2);
            return intent2;
        }
        return intent;
    }

    public String getDataServicePackageName(int transportType, String packageName) {
        if (this.mSatelliteDataEnable && transportType == 1) {
            packageName = SATELLITE_DATA_SERVICE_PACKAGE;
        }
        log("getDataServicePackageName packageName = " + packageName);
        return packageName;
    }

    private void registerAllEvents() {
        new IntentFilter();
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("satellite_state"), false, this.mSatelliteObserver);
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor(SATELLITE_DATA_ENABLED), false, this.mSatelliteDataObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSatelliteDataEnabled() {
        int isSatelliteDataEnabled = Settings.System.getInt(this.mContext.getContentResolver(), SATELLITE_DATA_ENABLED, 0);
        log("isSatelliteDataEnabled = " + isSatelliteDataEnabled);
        return isSatelliteDataEnabled == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSatelliteEnable() {
        boolean isSatelliteEnabled = Settings.System.getInt(this.mContext.getContentResolver(), "satellite_state", 0) == 1;
        log("isSatelliteEnabled = " + isSatelliteEnabled);
        return isSatelliteEnabled;
    }

    private void unregisterAllEvents() {
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mSatelliteObserver);
        this.mSatelliteObserver = null;
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mSatelliteDataObserver);
        this.mSatelliteDataObserver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d("MiuiDataServiceManagerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    private void loge(String s) {
        Rlog.e("MiuiDataServiceManagerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }
}
