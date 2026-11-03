package com.android.phone.dcservice;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import com.android.phone.MiuiPhoneInterfaceManager;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceConnectionCallback;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceManagerImpl;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiDcServiceManager {
    static Executor mExecutor = new ScheduledThreadPoolExecutor(1);
    private static ImsDataChannelServiceManager sInstance = null;

    public static synchronized ImsDataChannelServiceManager getInstance() {
        try {
            if (sInstance == null) {
                Log.i("MiDcServiceManager", "Need to initialize ImsDataChannelServiceManager with context and executor");
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public static void init(Context context) {
        Log.i("MiDcServiceManager", "ServiceManager init");
        if (MiuiPhoneInterfaceManager.IS_QCOM) {
            if (sInstance == null) {
                ImsDataChannelServiceManagerImpl imsDataChannelServiceManagerImpl = new ImsDataChannelServiceManagerImpl();
                sInstance = imsDataChannelServiceManagerImpl;
                imsDataChannelServiceManagerImpl.initialize(context, mExecutor);
                sInstance.connectImsDataChannelService(new ImsDcServiceConnectionCallBack());
                return;
            }
            return;
        }
        if (!MiuiPhoneInterfaceManager.IS_XRING || context == null) {
            return;
        }
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, (Class<?>) MiDcService.class), 2, 1);
        ExtTelephonyManager.getInstance(context).init5gNewCall();
    }

    class ImsDcServiceConnectionCallBack implements ImsDataChannelServiceConnectionCallback {
        private ImsDcServiceConnectionCallBack() {
        }

        @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceConnectionCallback
        public void onServiceConnected() {
            Log.i("MiDcServiceManager", "ImsDataChannelService onServiceConnected");
            MiDcServiceController.getInstance().onServiceConnected();
        }

        @Override // vendor.qti.imsdatachannel.client.ImsDataChannelServiceConnectionCallback
        public void onServiceDisconnected() {
            Log.i("MiDcServiceManager", "ImsDataChannelService onServiceDisconnected");
            MiDcServiceController.getInstance().onServiceDisconnected();
        }
    }
}
