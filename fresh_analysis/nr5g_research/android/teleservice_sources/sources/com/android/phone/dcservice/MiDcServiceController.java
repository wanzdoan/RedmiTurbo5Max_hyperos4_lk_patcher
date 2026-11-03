package com.android.phone.dcservice;

import android.telephony.TelephonyManager;
import android.util.Log;
import com.newcalllib.datachannel.V1_0.IImsDataChannelCallback;
import com.newcalllib.datachannel.V1_0.IImsDataChannelServiceController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiDcServiceController {
    private static MiDcServiceController sInstance;
    private final DataChannelServiceSub[] mDcServiceSubs;
    private ImsDataChannelServiceBinder mImsDcServcieBinder;
    private final ArrayList mDcChannels = new ArrayList();
    private final ConcurrentHashMap mDcCallbackMaps = new ConcurrentHashMap();

    private MiDcServiceController() {
        int phoneCount = TelephonyManager.getDefault().getPhoneCount();
        this.mDcServiceSubs = new DataChannelServiceSub[phoneCount];
        for (int i = 0; i < phoneCount; i++) {
            this.mDcServiceSubs[i] = new DataChannelServiceSub(i);
        }
    }

    public static synchronized MiDcServiceController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new MiDcServiceController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    public ImsDataChannelServiceBinder getBinder() {
        if (this.mImsDcServcieBinder == null) {
            this.mImsDcServcieBinder = new ImsDataChannelServiceBinder();
        }
        return this.mImsDcServcieBinder;
    }

    public void onServiceConnected() {
        Log.i("MiDcServiceController", "ims DcService connected");
        for (DataChannelServiceSub dataChannelServiceSub : this.mDcServiceSubs) {
            dataChannelServiceSub.onServiceConnected();
        }
    }

    public void onServiceDisconnected() {
        Log.i("MiDcServiceController", "ims DcService disconnected");
        for (DataChannelServiceSub dataChannelServiceSub : this.mDcServiceSubs) {
            dataChannelServiceSub.onServiceDisconnected();
        }
    }

    public boolean isDataChannelServiceAvailable(int i) {
        DataChannelServiceSub[] dataChannelServiceSubArr;
        boolean zIsDataChannelServiceAvailable = (i < 0 || (dataChannelServiceSubArr = this.mDcServiceSubs) == null || i >= dataChannelServiceSubArr.length) ? false : dataChannelServiceSubArr[i].isDataChannelServiceAvailable();
        Log.i("MiDcServiceController", "isDataChannelServiceAvailable: " + zIsDataChannelServiceAvailable + "for slotId " + i);
        return zIsDataChannelServiceAvailable;
    }

    public void initDataChannel(int i, String str, String str2) {
        ArrayList arrayList = this.mDcChannels;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            DataChannel dataChannel = (DataChannel) obj;
            if (i == dataChannel.getSlotId() && str.equals(dataChannel.getCallId())) {
                Log.i("MiDcServiceController", "initDataChannel existed");
                return;
            }
        }
        DataChannel dataChannel2 = new DataChannel(i, str, str2);
        this.mDcChannels.add(dataChannel2);
        if (this.mDcCallbackMaps.isEmpty()) {
            return;
        }
        Log.i("MiDcServiceController", "set ImsDcCallback");
        dataChannel2.setImsDcCallback((IImsDataChannelCallback) this.mDcCallbackMaps.get(str));
        this.mDcCallbackMaps.remove(str);
    }

    public DataChannel getDataChannelByCallId(String str) {
        ArrayList arrayList = this.mDcChannels;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            DataChannel dataChannel = (DataChannel) obj;
            if (str != null && str.equals(dataChannel.getCallId())) {
                Log.i("MiDcServiceController", "getDataChannel existed");
                return dataChannel;
            }
        }
        Log.i("MiDcServiceController", "DataChannel is not init");
        return null;
    }

    public void dispose() {
        Log.i("MiDcServiceController", "dispose");
        ArrayList arrayList = this.mDcChannels;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            DataChannel dataChannel = (DataChannel) obj;
            if (dataChannel != null) {
                Log.i("MiDcServiceController", "closeDataChannelTransport");
                dataChannel.closeDcTransport();
            }
        }
        this.mDcChannels.clear();
        this.mDcCallbackMaps.clear();
    }

    final class ImsDataChannelServiceBinder extends IImsDataChannelServiceController.Stub {
        private ImsDataChannelServiceBinder() {
        }

        @Override // com.newcalllib.datachannel.V1_0.IImsDataChannelServiceController
        public void createImsDataChannel(String[] strArr, String str, int i, String str2, String str3) {
            Log.i("MiDcServiceController", "sdk create ims Dc , labels: " + Arrays.toString(strArr) + ", telecomCallId: " + str2 + "slotId： " + i);
            if (str2 == null || str2.isEmpty()) {
                Log.i("MiDcServiceController", "createImsDataChannel telecomCallId is null");
                return;
            }
            DataChannel dataChannelByCallId = MiDcServiceController.this.getDataChannelByCallId(str2);
            if (dataChannelByCallId != null) {
                dataChannelByCallId.createImsDataChannel(strArr, str);
            }
        }

        @Override // com.newcalllib.datachannel.V1_0.IImsDataChannelServiceController
        public void setImsDataChannelCallback(IImsDataChannelCallback iImsDataChannelCallback, int i, String str) {
            if (str == null || str.isEmpty()) {
                Log.i("MiDcServiceController", "setImsDataChannelCallback telecomCallId is null");
                return;
            }
            Log.i("MiDcServiceController", "sdk set ims dc callback-- " + iImsDataChannelCallback + "telecomCallId: " + str + "slotId" + i);
            DataChannel dataChannelByCallId = MiDcServiceController.this.getDataChannelByCallId(str);
            if (dataChannelByCallId != null) {
                if (iImsDataChannelCallback == null) {
                    dataChannelByCallId.closeDcTransport();
                    MiDcServiceController.this.mDcChannels.remove(dataChannelByCallId);
                    return;
                } else {
                    dataChannelByCallId.setImsDcCallback(iImsDataChannelCallback);
                    return;
                }
            }
            if (iImsDataChannelCallback != null) {
                Log.i("MiDcServiceController", "data channel is null, cache callback");
                MiDcServiceController.this.mDcCallbackMaps.put(str, iImsDataChannelCallback);
            } else {
                MiDcServiceController.this.mDcCallbackMaps.remove(str);
            }
        }
    }
}
