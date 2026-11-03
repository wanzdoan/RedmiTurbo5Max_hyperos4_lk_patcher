package com.android.phone.dcservice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.android.phone.statistics.MiDataChannelStatistic;
import com.newcalllib.datachannel.V1_0.IImsDataChannelCallback;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import vendor.qti.imsdatachannel.client.ImsDataChannelEventListener;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager;
import vendor.qti.imsdatachannel.client.ImsDataChannelTransport;

/* JADX INFO: loaded from: classes.dex */
public class DataChannel implements ImsDataChannelEventListener {
    private MiDcServiceController mDcServiceController;
    IImsDataChannelCallback mImsDcCallback;
    private String mModemCallId;
    private int mSlotId;
    private String mTelecomCallId;
    private ArrayList mSaveBdcConnections = new ArrayList();
    private ArrayList mSaveAdcConnections = new ArrayList();
    private ArrayList mConnections = new ArrayList();
    private ImsDataChannelServiceManager mDcManager = null;
    private ImsDataChannelTransport mDcTransport = null;
    private int mRetryCount = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.dcservice.DataChannel.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 101) {
                return;
            }
            DataChannel.this.init();
        }
    };

    public DataChannel(int i, String str, String str2) {
        this.mSlotId = i;
        this.mTelecomCallId = str;
        this.mModemCallId = str2;
        init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mDcManager = MiDcServiceManager.getInstance();
        MiDcServiceController miDcServiceController = MiDcServiceController.getInstance();
        this.mDcServiceController = miDcServiceController;
        if (miDcServiceController.isDataChannelServiceAvailable(this.mSlotId)) {
            try {
                this.mDcTransport = this.mDcManager.createDataChannelTransport(this.mSlotId, this.mModemCallId, this);
                MiDataChannelStatistic.getInstance().processBdcRequest(this.mTelecomCallId);
                return;
            } catch (Exception unused) {
                logd("createDataChannelTransport failed");
                retryToInit();
                return;
            }
        }
        retryToInit();
    }

    private void retryToInit() {
        if (this.mRetryCount > 5) {
            logd("failed to init, reached to the max times:5");
            return;
        }
        logd("retryToInit:" + this.mRetryCount);
        this.mHandler.removeMessages(101);
        this.mHandler.sendEmptyMessageDelayed(101, 1000L);
        this.mRetryCount++;
    }

    public void setImsDcCallback(IImsDataChannelCallback iImsDataChannelCallback) {
        this.mImsDcCallback = iImsDataChannelCallback;
        ArrayList arrayList = this.mSaveBdcConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
            try {
                throw null;
            } catch (Exception unused) {
                logd("onBootstrapDataChannelResponse failed");
            }
        }
        this.mSaveBdcConnections.clear();
        logd("save bdc connection clear");
    }

    public void closeDcTransport() {
        logd("closeDataChannelTransport");
        try {
            this.mDcManager.closeDataChannelTransport(this.mDcTransport);
            this.mConnections.clear();
            this.mSaveAdcConnections.clear();
            this.mSaveBdcConnections.clear();
        } catch (RemoteException unused) {
            logd("closeDataChannelTransport RemoteException!!!");
        }
    }

    public void createImsDataChannel(String[] strArr, String str) {
        try {
            if (this.mDcTransport == null) {
                logd("mDcTransport is null can't create dc");
                return;
            }
            logd("create ims DataChannel");
            String[] dcIds = getDcIds(str);
            for (String str2 : strArr) {
                Iterator it = this.mSaveAdcConnections.iterator();
                if (it.hasNext()) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                    throw null;
                }
                MiDataChannelStatistic.getInstance().processAdcRequest(this.mTelecomCallId, str2);
            }
            if (dcIds.length > 0) {
                this.mDcTransport.createDataChannel(dcIds, str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logd("createImsDataChannel failed ");
        }
    }

    private String[] getDcIds(String str) {
        String attributeValue;
        ArrayList arrayList = new ArrayList();
        if (str != null) {
            try {
                XmlPullParserFactory xmlPullParserFactoryNewInstance = XmlPullParserFactory.newInstance();
                xmlPullParserFactoryNewInstance.setNamespaceAware(false);
                XmlPullParser xmlPullParserNewPullParser = xmlPullParserFactoryNewInstance.newPullParser();
                xmlPullParserNewPullParser.setInput(new StringReader(str));
                xmlPullParserNewPullParser.nextTag();
                xmlPullParserNewPullParser.require(2, null, "DataChannelAppInfo");
                while (xmlPullParserNewPullParser.next() != 1) {
                    if (xmlPullParserNewPullParser.getEventType() == 2 && xmlPullParserNewPullParser.getName().equals("DataChannel") && (attributeValue = xmlPullParserNewPullParser.getAttributeValue(null, "dcId")) != null) {
                        arrayList.add(attributeValue);
                        logd("getDcIds got id=" + attributeValue);
                    }
                }
                logd("getDcIds size=" + arrayList.size());
            } catch (Exception unused) {
                logd("getDcIds exception");
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public int getSlotId() {
        return this.mSlotId;
    }

    public String getCallId() {
        return this.mTelecomCallId;
    }

    private void logd(String str) {
        Log.i("DataChannel", "[" + this.mSlotId + "]" + str);
    }
}
